package com.daw.cinemadaw.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.daw.cinemadaw.domain.cinema.Room;
import com.daw.cinemadaw.repository.CinemaRepository;
import com.daw.cinemadaw.repository.RoomRepository;
import com.daw.cinemadaw.repository.SeatRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import java.util.Optional;
import com.daw.cinemadaw.domain.cinema.Seat;
import java.util.List;
import com.daw.cinemadaw.domain.cinema.Cinema;

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

        List<Seat> listSeats = seatRepository.findByRoomId(id);

        if (!listSeats.isEmpty()) {

            model.addAttribute("llista", listSeats);
            return "seats/view-seat";

        } else {

            return "redirect:/home";

        }
    }

}
