package com.daw.cinemadaw.controller;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.daw.cinemadaw.domain.cinema.Cinema;
import com.daw.cinemadaw.repository.CinemaRepository;

@Controller
public class CinemaController {

    private CinemaRepository cinemaRepository;

    public CinemaController(CinemaRepository cinemaRepository) {
        this.cinemaRepository = cinemaRepository;
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
    public String mostrarFormulariAlta(Model model) {
        Cinema cinema = new Cinema();
        cinema.setCity("Tarragona"); // Valor per defecte
        model.addAttribute("cinema", cinema);
        return "create-cinema";
    }

    @PostMapping("/cinema/create")
    public String altacinema(@ModelAttribute Cinema cinema) {
        cinemaRepository.save(cinema);
        return "redirect:/cinemes";
    }

    @GetMapping("/cinema/edit/{id}")
    public String mostrarFormulariEdicio(@PathVariable Long id, Model model) {

        Optional<Cinema> optional = cinemaRepository.findById(id);

        if(optional.isPresent()) {

            Cinema cinema = optional.get();
            model.addAttribute("cinema", cinema);
            return "edit-cinema";

        } else {

            return "redirect:/home";

        }
    }

    @PostMapping("/cinema/edit")
    public String editCinema(@ModelAttribute Cinema cinema) {

       cinemaRepository.save(cinema);

        return "redirect:/cinemes";

    }

}