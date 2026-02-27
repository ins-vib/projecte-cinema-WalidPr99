package com.daw.cinemadaw.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.daw.cinemadaw.repository.CinemaRepository;
import com.daw.cinemadaw.domain.cinema.Cinema;
import org.springframework.ui.Model;

import java.util.List;

@Controller
public class HomeController {

    private CinemaRepository cinemaRepository;

    public HomeController(CinemaRepository cinemaRepository) {
        this.cinemaRepository = cinemaRepository;
    }

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/cinemes")
    public String cinemes(Model model) {

        List<Cinema> cinemes = cinemaRepository.findAll();
        model.addAttribute("llista", cinemes);





        return "cinemes";
    }

}
