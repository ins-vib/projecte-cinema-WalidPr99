package com.daw.cinemadaw.controller;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.daw.cinemadaw.domain.cinema.New;
import com.daw.cinemadaw.repository.CinemaRepository;
import com.daw.cinemadaw.service.NewsService;

@Controller
public class HomeController {

    private NewsService newsService;

    private CinemaRepository cinemaRepository;

    public HomeController(CinemaRepository cinemaRepository, NewsService newsService) {
        this.cinemaRepository = cinemaRepository;
        this.newsService = newsService;
    }

    @GetMapping({"/home"})
    public String home(Model model) {

        ArrayList<New> llista = new ArrayList<>();

        try {

           llista = newsService.getNews();

        } catch (FileNotFoundException e) {

            System.out.println("Error al llegir les notícies: " + e.getMessage());

        }
        model.addAttribute("llista", llista);
        return "home";
    }

    

}


