package com.daw.cinemadaw.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Comparator;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.daw.cinemadaw.domain.cinema.Seat;
import com.daw.cinemadaw.domain.cinema.SeatType;
import com.daw.cinemadaw.domain.cinema.Room;
import com.daw.cinemadaw.repository.RoomRepository;
import com.daw.cinemadaw.repository.SeatRepository;

@Controller
public class SeatController {

    private static final int SEATS_PER_ROW = 5;

    private final SeatRepository seatRepository;
    private final RoomRepository roomRepository;

    public SeatController(SeatRepository seatRepository, RoomRepository roomRepository) {

        this.seatRepository = seatRepository;
        this.roomRepository = roomRepository;
    }

    @GetMapping("/seats/{id}")
    public String viewSeats(@PathVariable Long id,
                            @RequestParam(value = "reservedSeat", required = false) Long reservedSeat,
                            Model model) {

        Optional<Room> optionalRoom = roomRepository.findById(id);
        if (optionalRoom.isEmpty()) {
            return "redirect:/home";
        }

        Room room = optionalRoom.get();
        List<Seat> seats = synchronizeSeatsWithCapacity(room);

        int rowCount = (int) Math.ceil((double) room.getCapacity() / SEATS_PER_ROW);

        model.addAttribute("room", room);
        model.addAttribute("llista", seats);
        model.addAttribute("svgWidth", (SEATS_PER_ROW * 34) + 24);
        model.addAttribute("svgHeight", (rowCount * 34) + 24);
        model.addAttribute("reserveSuccess", reservedSeat != null);
        return "seats/view-seat";
    }

    @GetMapping("/seat/{id}")
    public String detailSeat(@PathVariable Long id,
                             @RequestParam(value = "alreadyReserved", required = false) boolean alreadyReserved,
                             Model model) {

        Optional<Seat> optionalSeat = seatRepository.findById(id);

        if (optionalSeat.isPresent()) {

            model.addAttribute("seat", optionalSeat.get());
            model.addAttribute("alreadyReserved", alreadyReserved);
            return "seats/detail-seat";

        } else {

            return "redirect:/home";

        }
    }

    @PostMapping("/seat/{id}/reserve")
    public String reserveSeat(@PathVariable Long id, Authentication authentication) {

        if (authentication == null || authentication.getAuthorities().stream()
                .noneMatch(authority -> "ROLE_CLIENT".equals(authority.getAuthority()))) {
            return "redirect:/seat/" + id;
        }

        Optional<Seat> optionalSeat = seatRepository.findById(id);
        if (optionalSeat.isEmpty()) {
            return "redirect:/home";
        }

        Seat seat = optionalSeat.get();
        if (!seat.isState()) {
            return "redirect:/seat/" + id + "?alreadyReserved=true";
        }

        seat.setState(false);
        seatRepository.save(seat);

        if (seat.getRoom() != null) {
            return "redirect:/seats/" + seat.getRoom().getId() + "?reservedSeat=" + seat.getId();
        }

        return "redirect:/movies";
    }

    @GetMapping("/seat/edit/{id}")

    public String editSeat(@PathVariable Long id, Model model) {

        Optional<Seat> optionalSeat = seatRepository.findById(id);

        if (optionalSeat.isEmpty()) {
            return "redirect:/rooms";

        }

        model.addAttribute("seat", optionalSeat.get());
        model.addAttribute("types", SeatType.values());
        return "seats/edit-seat";

    }

    @PostMapping("/seat/edit/{id}")
    public String updateSeat(
            @PathVariable Long id,
            @RequestParam("seatRow") String seatRow,
            @RequestParam("number") int number,
            @RequestParam("x") int x,
            @RequestParam("y") int y,
            @RequestParam("type") SeatType type,
            @RequestParam(value = "state", defaultValue = "false") boolean state) {

        Optional<Seat> optionalSeat = seatRepository.findById(id);

        if (optionalSeat.isEmpty()) {
            return "redirect:/rooms";
        }

        Seat seat = optionalSeat.get();
        seat.setSeatRow(seatRow);
        seat.setNumber(number);
        seat.setX(x);
        seat.setY(y);
        seat.setType(type);
        seat.setState(state);

        seatRepository.save(seat);

        if (seat.getRoom() != null) {
            return "redirect:/seats/" + seat.getRoom().getId();
        }

        return "redirect:/home";

    }

    @GetMapping("/seat/delete/{id}")
    public String deleteSeat(@PathVariable Long id) {

        Optional<Seat> optionalSeat = seatRepository.findById(id);

        if (optionalSeat.isEmpty()) {
            return "redirect:/home";
        }

        Seat seat = optionalSeat.get();
        Long roomId = seat.getRoom() != null ? seat.getRoom().getId() : null;

        seatRepository.deleteById(id);

        if (roomId != null) {
            return "redirect:/seats/" + roomId;
        }

        return "redirect:/home";
    }

    private List<Seat> synchronizeSeatsWithCapacity(Room room) {

        int capacity = Math.max(room.getCapacity(), 0);
        List<Seat> seats = new ArrayList<>(seatRepository.findByRoomIdOrderByIdAsc(room.getId()));

        while (seats.size() < capacity) {
            Seat generatedSeat = new Seat(true, "A", 1, 0, 0, room);
            generatedSeat.setType(SeatType.Standard);
            seats.add(seatRepository.save(generatedSeat));
        }

        seats.sort(Comparator.comparing(Seat::getId));

        int visibleSeats = Math.min(capacity, seats.size());
        for (int index = 0; index < visibleSeats; index++) {
            Seat seat = seats.get(index);
            int rowIndex = index / SEATS_PER_ROW;
            int numberInRow = (index % SEATS_PER_ROW) + 1;

            seat.setSeatRow(toSeatRowLabel(rowIndex));
            seat.setNumber(numberInRow);
            seat.setX(index % SEATS_PER_ROW);
            seat.setY(rowIndex);
        }

        if (visibleSeats > 0) {
            seatRepository.saveAll(seats.subList(0, visibleSeats));
        }

        return new ArrayList<>(seats.subList(0, visibleSeats));
    }

    private String toSeatRowLabel(int rowIndex) {

        StringBuilder label = new StringBuilder();
        int value = rowIndex;

        do {
            label.insert(0, (char) ('A' + (value % 26)));
            value = (value / 26) - 1;
        } while (value >= 0);

        return label.toString();
    }

}
