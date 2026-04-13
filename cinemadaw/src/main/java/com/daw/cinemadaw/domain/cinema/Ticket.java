package com.daw.cinemadaw.domain.cinema;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private double price;

    @ManyToOne
    private Seat seat;

    @ManyToOne
    private Screening screening;

    @ManyToOne
    private Comanda comanda;

    public Ticket() {
    }

    public Ticket(double price, Seat seat, Screening screening, Comanda comanda) {
        this.price = price;
        this.seat = seat;
        this.screening = screening;
        this.comanda = comanda;
    }

    public Long getId() {
        return id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Seat getSeat() {
        return seat;
    }

    public void setSeat(Seat seat) {
        this.seat = seat;
    }

    public Screening getScreening() {
        return screening;
    }

    public void setScreening(Screening screening) {
        this.screening = screening;
    }

    public Comanda getComanda() {
        return comanda;
    }

    public void setComanda(Comanda comanda) {
        this.comanda = comanda;
    }

    @Override
    public String toString() {
        return "Ticket [id=" + id + ", price=" + price + ", seat=" + seat + ", screening=" + screening + "]";
    }
}
