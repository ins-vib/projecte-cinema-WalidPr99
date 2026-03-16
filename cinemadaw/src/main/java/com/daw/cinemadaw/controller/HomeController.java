package com.daw.cinemadaw.controller;
import java.util.ArrayList;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.daw.cinemadaw.domain.cinema.New;
import com.daw.cinemadaw.service.NewsService;

@Controller
public class HomeController {

    private final NewsService newsService;

    public HomeController(NewsService newsService) {
        this.newsService = newsService;
    }

    @GetMapping({"/", "/home"})
    public String home(Model model) {

        ArrayList<New> llista = new ArrayList<>(newsService.getNews());

        model.addAttribute("llista", llista);
        return "home";
    }

    

}


