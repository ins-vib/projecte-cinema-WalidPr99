package com.daw.cinemadaw.controller;
import java.util.ArrayList;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.daw.cinemadaw.domain.cinema.New;
import com.daw.cinemadaw.repository.MovieRepository;
import com.daw.cinemadaw.service.NewsService;

@Controller
public class HomeController {

    private final NewsService newsService;
    private final MovieRepository movieRepository;

    public HomeController(NewsService newsService, MovieRepository movieRepository) {
        this.newsService = newsService;
        this.movieRepository = movieRepository;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping({"/", "/home"})
    public String home(Model model) {

        ArrayList<New> llista = new ArrayList<>(newsService.getNews());

        model.addAttribute("llista", llista);
        return "home";
    }

    @GetMapping("/admin")
    public String admin() {
        return "admin/home";
    }

    @GetMapping("/client")
    public String client(Model model) {
        model.addAttribute("movies", movieRepository.findAll());
        return "client/home";
    }

    

}


