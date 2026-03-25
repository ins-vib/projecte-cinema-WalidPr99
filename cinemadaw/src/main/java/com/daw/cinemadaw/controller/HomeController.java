package com.daw.cinemadaw.controller;
import java.util.ArrayList;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.daw.cinemadaw.domain.cinema.user.Role;
import com.daw.cinemadaw.domain.cinema.user.User;
import com.daw.cinemadaw.repository.MovieRepository;
import com.daw.cinemadaw.repository.UserRepository;
import com.daw.cinemadaw.service.NewsService;

@Controller
public class HomeController {

    private final NewsService newsService;
    private final MovieRepository movieRepository;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    public HomeController(NewsService newsService, MovieRepository movieRepository, UserRepository userRepository,
            BCryptPasswordEncoder encoder) {
        this.newsService = newsService;
        this.movieRepository = movieRepository;
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    @GetMapping({"/", "/home", "/login"})
    public String login(Model model) {
        model.addAttribute("llista", new ArrayList<>(newsService.getNews()));
        return "login";
    }

    @PostMapping("/register")
    public String register(@RequestParam String username, @RequestParam String password) {

        String cleanUsername = username == null ? "" : username.trim();

        if (cleanUsername.isEmpty() || password == null || password.isBlank()) {
            return "redirect:/login?registerError=empty";
        }

        if (userRepository.findByUsername(cleanUsername).isPresent()) {
            return "redirect:/login?registerError=exists";
        }

        User client = new User();
        client.setUsername(cleanUsername);
        client.setPassword(encoder.encode(password));
        client.setRole(Role.CLIENT);
        userRepository.save(client);

        return "redirect:/login?registered";
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


