package com.daw.cinemadaw.controller;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.daw.cinemadaw.domain.cinema.Movie;
import com.daw.cinemadaw.domain.cinema.Screening;
import com.daw.cinemadaw.domain.cinema.user.Role;
import com.daw.cinemadaw.domain.cinema.user.User;
import com.daw.cinemadaw.repository.MovieRepository;
import com.daw.cinemadaw.repository.ScreeningRepository;
import com.daw.cinemadaw.repository.UserRepository;
import com.daw.cinemadaw.service.CustomUserDetails;
import com.daw.cinemadaw.service.NewsService;

@Controller
public class HomeController {

    private final NewsService newsService;
    private final MovieRepository movieRepository;
    private final ScreeningRepository screeningRepository;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    public HomeController(NewsService newsService, MovieRepository movieRepository, ScreeningRepository screeningRepository,
            UserRepository userRepository, BCryptPasswordEncoder encoder) {
        this.newsService = newsService;
        this.movieRepository = movieRepository;
        this.screeningRepository = screeningRepository;
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
        List<Movie> movies = movieRepository.findAll();
        Map<Long, List<Screening>> screeningsByMovie = new LinkedHashMap<>();

        for (Movie movie : movies) {
            screeningsByMovie.put(movie.getId(), screeningRepository.findByMovieId(movie.getId()));
        }

        model.addAttribute("movies", movies);
        model.addAttribute("screeningsByMovie", screeningsByMovie);
        model.addAttribute("now", LocalDateTime.now());
        return "client/home";
    }

    @GetMapping("/client/news")
    public String clientNews(Model model) {
        model.addAttribute("llista", new ArrayList<>(newsService.getNews()));
        return "client/news";
    }

    @GetMapping("/perfil")
    public String perfil(@AuthenticationPrincipal CustomUserDetails user, Model model) {
        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute("username", user.getUsername());
        model.addAttribute("role", user.getUser().getRole());
        return "profile";
    }

    

}


