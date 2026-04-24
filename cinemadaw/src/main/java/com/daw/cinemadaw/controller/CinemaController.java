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
import com.daw.cinemadaw.dto.ServicesListDTO;
import com.daw.cinemadaw.repository.CinemaRepository;

import jakarta.validation.Valid;

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
        return "cinemes/cinemes";
    }

    //Detall de cinema

    @GetMapping("/cinema/{id}")
    public String detall(@PathVariable Long id, Model model) {

        Optional<Cinema> optional = cinemaRepository.findById(id);

        if(optional.isPresent()) {

            Cinema cinema = optional.get();
            model.addAttribute("cinema", cinema);
            return "cinemes/view-cinema";

        } else {

            return "redirect:/home";

        }
    }

    @GetMapping("/cinema/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {

        Optional<Cinema> optional = cinemaRepository.findById(id);

        if (optional.isPresent()) {
            try {
                cinemaRepository.deleteById(id);
                redirectAttributes.addFlashAttribute("successMessage",
                        "Cinema '" + optional.get().getName() + "' eliminat correctament.");
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("errorMessage",
                        "No s'ha pogut eliminar el cinema: " + e.getMessage());
            }
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "El cinema no existeix.");
        }

        return "redirect:/cinemes";

    }

    @GetMapping("/cinema/create")
    public String mostrarFormulariAlta(Model model) {
        Cinema cinema = new Cinema();
        cinema.setCity("Tarragona"); // Valor per defecte
        model.addAttribute("cinema", cinema);
        return "cinemes/create-cinema";
    }

    @PostMapping("/cinema/create")
    public String altacinema(@Valid @ModelAttribute Cinema cinema, BindingResult result) {
        
        if (result.hasErrors()) {
            return "cinemes/create-cinema";
        }

        cinemaRepository.save(cinema);
        return "redirect:/cinemes";
    }

    @GetMapping("/cinema/edit/{id}")
    public String mostrarFormulariEdicio(@PathVariable Long id, Model model) {

        Optional<Cinema> optional = cinemaRepository.findById(id);

        if(optional.isPresent()) {

            Cinema cinema = optional.get();
            model.addAttribute("cinema", cinema);
            return "cinemes/edit-cinema";

        } else {

            return "redirect:/home";

        }
    }

    @PostMapping("/cinema/edit")
    public String editCinema(@Valid @ModelAttribute Cinema cinema, BindingResult result) {

        if (result.hasErrors()) {
            return "cinemes/edit-cinema";
        }

        Optional<Cinema> optional = cinemaRepository.findById(cinema.getId());

        if (optional.isPresent()) {
            Cinema existent = optional.get();
            existent.setName(cinema.getName());
            existent.setAddress(cinema.getAddress());
            existent.setCity(cinema.getCity());
            existent.setPostalCode(cinema.getPostalCode());
            cinemaRepository.save(existent);
        }

        return "redirect:/cinemes";

    }

    @GetMapping("/cinema/services")
    public String showForm(Model model) {

        model.addAttribute("servicesDTO", new ServicesListDTO());

        model.addAttribute("allServices", List.of(
            "crispetes",
            "parking",
            "begudes",
            "vip",
            "imax"
        ));

        return "cinemes/services-form";
    }

    @PostMapping("/cinema/services")
    public String save(@ModelAttribute ServicesListDTO dto) {

        // Mostrar resultats per consola
        System.out.println(dto.getServices());

        return "redirect:/"; 
    }

}