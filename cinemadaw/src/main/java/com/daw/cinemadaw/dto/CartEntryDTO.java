package com.daw.cinemadaw.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.daw.cinemadaw.domain.cinema.Seat;

public class CartEntryDTO {

    private Long screeningId;
    private String movieTitle;
    private String cinemaName;
    private String roomName;
    private LocalDateTime dateTime;
    private double pricePerSeat;
    private List<Seat> seats = new ArrayList<>();
    private int availableCount;
    private int unavailableCount;
    private double subtotal;

    public Long getScreeningId() {
        return screeningId;
    }

    public void setScreeningId(Long screeningId) {
        this.screeningId = screeningId;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public String getCinemaName() {
        return cinemaName;
    }

    public void setCinemaName(String cinemaName) {
        this.cinemaName = cinemaName;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public double getPricePerSeat() {
        return pricePerSeat;
    }

    public void setPricePerSeat(double pricePerSeat) {
        this.pricePerSeat = pricePerSeat;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public void setSeats(List<Seat> seats) {
        this.seats = seats != null ? seats : new ArrayList<>();
    }

    public int getAvailableCount() {
        return availableCount;
    }

    public void setAvailableCount(int availableCount) {
        this.availableCount = availableCount;
    }

    public int getUnavailableCount() {
        return unavailableCount;
    }

    public void setUnavailableCount(int unavailableCount) {
        this.unavailableCount = unavailableCount;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }
}
