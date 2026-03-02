package com.daw.cinemadaw.controller;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.daw.cinemadaw.domain.cinema.Cinema;
import com.daw.cinemadaw.repository.CinemaRepository;

@Controller
public class HomeController {

    private CinemaRepository cinemaRepository;

    public HomeController(CinemaRepository cinemaRepository) {
        this.cinemaRepository = cinemaRepository;
    }

    @GetMapping("/home")
    public String home() {
        return "home";
    }

    @GetMapping("/cinemes")
    public String cinemes(Model model) {

        List<Cinema> cinemes = cinemaRepository.findAll();
        model.addAttribute("llista", cinemes);
        return "cinemes";
    }

    //Detall de cinema

    @GetMapping("/cinema/{id}")
    public String detall(@PathVariable Long id, Model model) {

        Optional<Cinema> optional = cinemaRepository.findById(id);

        if(optional.isPresent()) {

            Cinema cinema = optional.get();
            model.addAttribute("cinema", cinema);
            return "detall-cinema";

        } else {

            return "redirect:/home";

        }
    }

    @GetMapping("/cinema/delete/{id}")
    public String delete(@PathVariable Long id) {

        Optional<Cinema> optional = cinemaRepository.findById(id);

        if(optional.isPresent()) {

           cinemaRepository.deleteById(id);

        }

        return "redirect:/cinemes";

    }

    @GetMapping("/cinema/create")
    public String mostrarFormulariAlta() {
        return "create-cinema";
    }

    @PostMapping("/cinema/create")
    public String altacinema() {

        return "redirect:/cinemes";
    }

}


