package com.daw.cinemadaw.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.daw.cinemadaw.domain.cinema.Seat;
import com.daw.cinemadaw.domain.cinema.SeatType;
import com.daw.cinemadaw.repository.CinemaRepository;
import com.daw.cinemadaw.repository.RoomRepository;
import com.daw.cinemadaw.repository.SeatRepository;

@Controller
public class SeatController {

    private final SeatRepository seatRepository;
    private final RoomRepository roomRepository;
    private final CinemaRepository cinemaRepository;

    public SeatController(SeatRepository seatRepository, RoomRepository roomRepository, CinemaRepository cinemaRepository) {

        this.seatRepository = seatRepository;
        this.roomRepository = roomRepository;
        this.cinemaRepository = cinemaRepository;
    }

    @GetMapping("/seats/{id}")
    public String viewSeats(@PathVariable Long id, Model model) {

        Optional<List<Seat>> optionalSeats = seatRepository.findByRoomId(id);

        if (optionalSeats.isPresent() && !optionalSeats.get().isEmpty()) {

            model.addAttribute("llista", optionalSeats.get());
            return "seats/view-seat";

        } else {

            return "redirect:/home";

        }
    }

    @GetMapping("/seat/{id}")
    public String detailSeat(@PathVariable Long id, Model model) {

        Optional<Seat> optionalSeat = seatRepository.findById(id);

        if (optionalSeat.isPresent()) {

            model.addAttribute("seat", optionalSeat.get());
            return "seats/detail-seat";

        } else {

            return "redirect:/home";

        }
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

}
