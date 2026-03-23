package com.daw.cinemadaw.controller;

import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.daw.cinemadaw.domain.cinema.Movie;
import com.daw.cinemadaw.domain.cinema.Room;
import com.daw.cinemadaw.domain.cinema.Screening;
import com.daw.cinemadaw.repository.MovieRepository;
import com.daw.cinemadaw.repository.RoomRepository;
import com.daw.cinemadaw.repository.ScreeningRepository;

@Controller
public class ScreeningController {

	private final ScreeningRepository screeningRepository;
	private final MovieRepository movieRepository;
	private final RoomRepository roomRepository;

	public ScreeningController(ScreeningRepository screeningRepository, MovieRepository movieRepository,
			RoomRepository roomRepository) {
		this.screeningRepository = screeningRepository;
		this.movieRepository = movieRepository;
		this.roomRepository = roomRepository;
	}

	@GetMapping("/movie/{movieId}/screenings")
	public String listMovieScreenings(@PathVariable Long movieId, Model model) {

		Optional<Movie> optionalMovie = movieRepository.findById(movieId);

		if (optionalMovie.isEmpty()) {
			return "redirect:/home";
		}

		model.addAttribute("movie", optionalMovie.get());
		model.addAttribute("screenings", screeningRepository.findByMovieId(movieId));
		return "screenings/detail-screening";
	}

	@GetMapping("/movie/{movieId}/screening/create")
	public String showCreateScreeningForm(@PathVariable Long movieId, Model model) {

		Optional<Movie> optionalMovie = movieRepository.findById(movieId);

		if (optionalMovie.isEmpty()) {
			return "redirect:/home";
		}

		Screening screening = new Screening();
		screening.setMovie(optionalMovie.get());
		model.addAttribute("screening", screening);
		model.addAttribute("rooms", roomRepository.findAll());
		return "screenings/create-screening";
	}

	@PostMapping("/movie/{movieId}/screening/create")
	public String createScreening(@PathVariable Long movieId, @ModelAttribute Screening screening, Model model) {

		Optional<Movie> optionalMovie = movieRepository.findById(movieId);

		if (optionalMovie.isEmpty()) {
			return "redirect:/home";
		}

		if (screening.getRoom() == null || screening.getRoom().getId() == null) {
			screening.setMovie(optionalMovie.get());
			model.addAttribute("screening", screening);
			model.addAttribute("rooms", roomRepository.findAll());
			return "screenings/create-screening";
		}

		Optional<Room> optionalRoom = roomRepository.findById(screening.getRoom().getId());

		if (optionalRoom.isEmpty()) {
			screening.setMovie(optionalMovie.get());
			model.addAttribute("screening", screening);
			model.addAttribute("rooms", roomRepository.findAll());
			return "screenings/create-screening";
		}

		screening.setMovie(optionalMovie.get());
		screening.setRoom(optionalRoom.get());
		screeningRepository.save(screening);
		return "redirect:/movie/" + movieId + "/screenings";
	}

	@GetMapping("/screening/edit/{id}")
	public String showEditScreeningForm(@PathVariable Long id, Model model) {

		Optional<Screening> optionalScreening = screeningRepository.findById(id);

		if (optionalScreening.isEmpty()) {
			return "redirect:/home";
		}

		model.addAttribute("screening", optionalScreening.get());
		model.addAttribute("rooms", roomRepository.findAll());
		return "screenings/edit-screeining";
	}

	@PostMapping("/screening/edit")
	public String editScreening(@ModelAttribute Screening screening, Model model) {

		if (screening.getMovie() == null || screening.getMovie().getId() == null || screening.getRoom() == null
				|| screening.getRoom().getId() == null) {
			model.addAttribute("screening", screening);
			model.addAttribute("rooms", roomRepository.findAll());
			return "screenings/edit-screeining";
		}

		Optional<Movie> optionalMovie = movieRepository.findById(screening.getMovie().getId());
		Optional<Room> optionalRoom = roomRepository.findById(screening.getRoom().getId());

		if (optionalMovie.isEmpty() || optionalRoom.isEmpty()) {
			model.addAttribute("screening", screening);
			model.addAttribute("rooms", roomRepository.findAll());
			return "screenings/edit-screeining";
		}

		screening.setMovie(optionalMovie.get());
		screening.setRoom(optionalRoom.get());
		screeningRepository.save(screening);
		return "redirect:/movie/" + screening.getMovie().getId() + "/screenings";
	}

	@GetMapping("/screening/delete/{id}")
	public String deleteScreening(@PathVariable Long id) {

		Optional<Screening> optionalScreening = screeningRepository.findById(id);

		if (optionalScreening.isPresent()) {
			Screening screening = optionalScreening.get();
			Long movieId = screening.getMovie().getId();
			screeningRepository.deleteById(id);
			return "redirect:/movie/" + movieId + "/screenings";
		}

		return "redirect:/home";
	}

}
