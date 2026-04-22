package com.daw.cinemadaw.domain.cinema;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Entity

public class Screening {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "La data i hora de la projecció és obligatòria")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime dateTime;

    @Positive(message = "El preu ha de ser superior a 0")
    @DecimalMax(value = "1000.0", message = "El preu no pot superar els 1000€")
    private double price;

    @NotNull(message = "La pel·lícula és obligatòria")
    @ManyToOne
    private Movie movie;

    @NotNull(message = "La sala és obligatòria")
    @ManyToOne
    private Room room;

    public Screening() {
    }

    public Screening(LocalDateTime dateTime, double price, Movie movie, Room room) {
        this.dateTime = dateTime;
        this.price = price;
        this.movie = movie;
        this.room = room;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }  

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }
}
