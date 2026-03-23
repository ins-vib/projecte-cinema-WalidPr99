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

import com.daw.cinemadaw.domain.cinema.Movie;
import com.daw.cinemadaw.repository.MovieRepository;
import com.daw.cinemadaw.repository.ScreeningRepository;

import jakarta.validation.Valid;


@Controller
public class MovieController {

    private final MovieRepository movieRepository;
    private final ScreeningRepository screeningRepository;

    public MovieController(MovieRepository movieRepository, ScreeningRepository screeningRepository) {
        this.movieRepository = movieRepository;
        this.screeningRepository = screeningRepository;
    }

    @GetMapping("/movies")
    public String movies(Model model) {

        List<Movie> movies = movieRepository.findAll();
        model.addAttribute("llista", movies);
        return "movies/movie";
    }

    @GetMapping("/movies/genero")
    public String moviesByGenre(String genre, Model model) {

        List<Movie> movies = movieRepository.findByGenre(genre);
        model.addAttribute("llista", movies);
        return "movies/movie";
    }

    @GetMapping("/movie/{id}")
    public String detailMovie(@PathVariable Long id, Model model) {

        Optional<Movie> optional = movieRepository.findById(id);

        if(optional.isPresent()) {

            Movie movie = optional.get();
            model.addAttribute("movie", movie);
            model.addAttribute("screenings", screeningRepository.findByMovieId(id));
            return "movies/view-movie";

        } else {

            return "redirect:/home";

        }
    }

    @GetMapping("/movie/delete/{id}")
    public String deleteMovie(@PathVariable Long id) {

        Optional<Movie> optional = movieRepository.findById(id);

        if(optional.isPresent()) {
            screeningRepository.deleteByMovieId(id);
            movieRepository.deleteById(id);
        }

        return "redirect:/movies";
    }

    @GetMapping("/movie/create")
    public String showCreateMovieForm(@Valid Model model, @ModelAttribute Movie movie, BindingResult result) {

        if (movie == null) {
            movie = new Movie();
        }
        model.addAttribute("movie", movie);
        return "movies/create-movie";
    }

    @PostMapping("/movie/create")
    public String createMovie(@ModelAttribute Movie movie) {

        movieRepository.save(movie);
        return "redirect:/movies";
    }

    @GetMapping("/movie/edit/{id}")
    public String showEditMovieForm(@PathVariable Long id, Model model) {

        Optional<Movie> optional = movieRepository.findById(id);

        if(optional.isPresent()) {

            Movie movie = optional.get();
            model.addAttribute("movie", movie);
            return "movies/edit-movie";

        } else {

            return "redirect:/home";

        }
    }

    @PostMapping("/movie/edit")
    public String editMovie(@Valid @ModelAttribute Movie movie, BindingResult result) {

        if (result.hasErrors()) {
            return "movies/edit-movie";
        }

        movieRepository.save(movie);
        return "redirect:/movies";
    }

}
