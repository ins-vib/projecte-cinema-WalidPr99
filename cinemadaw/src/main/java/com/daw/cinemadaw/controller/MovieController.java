package com.daw.cinemadaw.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.daw.cinemadaw.repository.MovieRepository;
import com.daw.cinemadaw.domain.cinema.Movie;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;
import org.springframework.ui.Model;


@Controller
public class MovieController {

    private MovieRepository movieRepository;

    public MovieController(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
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
            return "movies/detall-movie";

        } else {

            return "redirect:/home";

        }
    }

    @GetMapping("/movie/delete/{id}")
    public String deleteMovie(@PathVariable Long id) {

        Optional<Movie> optional = movieRepository.findById(id);

        if(optional.isPresent()) {
            movieRepository.deleteById(id);
        }

        return "redirect:/movies";
    }

    @GetMapping("/movie/create")
    public String showCreateMovieForm(Model model) {

        Movie movie = new Movie();
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
    public String editMovie(@ModelAttribute Movie movie) {

        movieRepository.save(movie);
        return "redirect:/movies";
    }

}
