package com.daw.cinemadaw.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.daw.cinemadaw.domain.cinema.Seat;
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

    

}
