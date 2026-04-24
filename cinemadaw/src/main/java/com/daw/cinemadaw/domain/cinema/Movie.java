package com.daw.cinemadaw.domain.cinema;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
public class Movie {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    @NotBlank(message = "El títol de la pel·lícula es obligatori")
    @Size (min = 2, max = 110, message = "El títol de la pel·lícula no pot superar els 150 caràcters ni ser inferiro a 5 caracters")
    @Column
    private String title;
    
    @Min(value = 25, message = "La duració de la pel·lícula no pot ser inferior a 25")
    @Max(value = 500, message = "La duració de la pel·lícula no pot ser superior a 500")
    @Column
    private int duration;

    @NotBlank(message = "El gènere de la pel·lícula es obligatori")
    @Size (min = 2, max = 110, message = "El gènere de la pel·lícula no pot superar els 150 caràcters ni ser inferiro a 5 caracters")
    @Column
    private String genre;

    @NotBlank(message = "La descripció de la pel·lícula es obligatoria")
    @Size (min = 10, max = 500, message = "La descripció de la pel·lícula no pot superar els 500 caràcters ni ser inferiro a 10 caracters")
    @Column
    private String description;

    @NotNull(message = "La data de llançament de la pel·lícula es obligatoria")
    @Column(name = "release_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate releaseDate;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Screening> screenings = new ArrayList<>();

    public Movie() {
    }

    public List<Screening> getScreenings() {
        return screenings;
    }

    public void setScreenings(List<Screening> screenings) {
        this.screenings = screenings;
    }

    public Movie(String description, int duration, String genre, LocalDate releaseDate, String title) {
        this.description = description;
        this.duration = duration;
        this.genre = genre;
        this.releaseDate = releaseDate;
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    @Override

    public String toString() {
        return "Movie [id=" + id + ", title=" + title + ", duration=" + duration + ", genre=" + genre
                + ", description=" + description + ", releaseDate=" + releaseDate + "]";
    }

}
