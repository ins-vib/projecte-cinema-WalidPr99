package com.daw.cinemadaw.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    @GetMapping("/rooms")
    public String listRooms(Model model) {
        List<Room> rooms = roomRepository.findAll();
        model.addAttribute("llista", rooms);
        return "room/rooms";
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

        Optional<Room> optional = roomRepository.findById(room.getId());

        Long redirectCinemaId = null;

        if (optional.isPresent()) {
            Room existent = optional.get();
            existent.setName(room.getName());
            existent.setCapacity(room.getCapacity());
            roomRepository.save(existent);
            redirectCinemaId = existent.getCinema() != null ? existent.getCinema().getId() : null;
        }

        if (redirectCinemaId != null) {
            return "redirect:/cinema/" + redirectCinemaId;
        }
        return "redirect:/rooms";

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
    public String deleteRoom(@PathVariable Long id, RedirectAttributes redirectAttributes) {

        Optional<Room> optional = roomRepository.findById(id);

        if (optional.isPresent()) {

            Room room = optional.get();
            Long cinemaId = room.getCinema() != null ? room.getCinema().getId() : null;

            try {
                roomRepository.deleteById(id);
                redirectAttributes.addFlashAttribute("successMessage",
                        "Sala '" + room.getName() + "' eliminada correctament.");
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("errorMessage",
                        "No s'ha pogut eliminar la sala: " + e.getMessage());
            }

            if (cinemaId != null) {
                return "redirect:/cinema/" + cinemaId;
            }
            return "redirect:/rooms";
        }

        redirectAttributes.addFlashAttribute("errorMessage", "La sala no existeix.");
        return "redirect:/rooms";
    }

}