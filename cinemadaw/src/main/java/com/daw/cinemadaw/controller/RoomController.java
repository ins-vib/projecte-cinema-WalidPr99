package com.daw.cinemadaw.controller;

import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.daw.cinemadaw.domain.cinema.Cinema;
import com.daw.cinemadaw.domain.cinema.Room;
import com.daw.cinemadaw.repository.CinemaRepository;
import com.daw.cinemadaw.repository.RoomRepository;

import jakarta.validation.Valid;


@Controller
public class RoomController {

    private final RoomRepository roomRepository;
    private final CinemaRepository cinemaRepository;

    public RoomController(RoomRepository roomRepository, CinemaRepository cinemaRepository) {
        this.roomRepository = roomRepository;
        this.cinemaRepository = cinemaRepository;
    }

    @GetMapping("/room/create/{cinemaId}")
    public String mostrarFormulariAlta(@PathVariable("cinemaId") Long cinemaId, Model model) {

        Room room = new Room();

        Optional<Cinema> optional = cinemaRepository.findById(cinemaId);
        room.setCinema(optional.get());
        model.addAttribute("room", room);
        return "room/create-room";

    }

    @PostMapping("/room/create/{cinemaId}")
    public String altaRoom(@Valid @PathVariable("cinemaId") Long cinemaId, @ModelAttribute Room room, BindingResult result) {

        if (result.hasErrors()) {
            return "room/create-room";
        }

        roomRepository.save(room);
        return "redirect:/cinema/" + room.getCinema().getId();
    }

    @GetMapping("/room/edit/{id}")
    public String mostrarFormulariEdicio(@PathVariable Long id, Model model) {

        Optional<Room> optional = roomRepository.findById(id);

        if (optional.isPresent()) {

            Room room = optional.get();
            model.addAttribute("room", room);
            model.addAttribute("cinemaId", room.getCinema().getId());
            
        }

        return "room/edit-room";
    }

    @PostMapping("/room/edit")
    public String editRoom(@Valid @ModelAttribute Room room , BindingResult result) {

        if (result.hasErrors()) {
            return "room/edit-room";
        }

       roomRepository.save(room);

        return "redirect:/cinema/" + room.getCinema().getId();

    }

    @GetMapping("/room/view/{id}")
    public String viewRoom(@PathVariable Long id, Model model) {

        Optional<Room> optional = roomRepository.findById(id);

        if (optional.isPresent()) {

            Room room = optional.get();
            model.addAttribute("room", room);
            return "room/view-room";

        } else {

            return "redirect:/home";

        }
    }

    @GetMapping("/room/delete/{id}")
    public String deleteRoom(@PathVariable Long id) {

        Optional<Room> optional = roomRepository.findById(id);

        Long cinemaId = null;
        Room room = null;

        if (optional.isPresent()) {

            room = optional.get();
            cinemaId = room.getCinema().getId();
            roomRepository.deleteById(id);
            return "redirect:/cinema/" + cinemaId;

        }

        return "redirect:/home";
    }

}