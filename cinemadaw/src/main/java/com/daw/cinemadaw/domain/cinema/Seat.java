package com.daw.cinemadaw.domain.cinema;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;



@Entity
public class Seat {

    @ManyToOne
    private Room room; //Relació ManyToOne amb Room, ja que un seient (Seat) pertany a una sala (Room). Aquesta relació es pot implementar utilitzant anotacions de JPA com @ManyToOne i @JoinColumn per establir la clau forana que connecta el seient amb la sala corresponent a la base de dades.

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "La fila del seient es obligatoria")
    @Size(min = 1, max = 10, message = "La fila del seient no pot superar els 10 caràcters ni ser inferior a 1 caràcter")
    @Column
    private String seatRow;

    @Min(value = 1, message = "El número del seient no puede ser inferior a 1")
    @Max(value = 500, message = "El número del seient no puede superar los 500")
    @Column
    private int number;

    @Column
    private int x;

    @Column
    private int y;

    @Column
    @Enumerated(EnumType.STRING)
    private SeatType type = SeatType.Standard;

    @Column
    private boolean state = true;

    @OneToMany(mappedBy = "seat", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ticket> tickets = new ArrayList<>();

    public Seat() {
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }

    public Seat(boolean state, String seatRow, int number, int x, int y, Room room) {
        this.state = state;
        this.seatRow = seatRow;
        this.number = number;
        this.x = x;
        this.y = y;
        this.room = room;
    }

    public Long getId() {
        return id;
    }

    public String getSeatRow() {
        return seatRow;
    }

    public void setSeatRow(String seatRow) {
        this.seatRow = seatRow;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public SeatType getType() {
        return type;
    }

    public void setType(SeatType type) {
        this.type = type;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    @Override
    public String toString() {
        return "Seat [id=" + id + ", seatRow=" + seatRow + ", number=" + number + ", x=" + x + ", y=" + y + ", type=" + type
                + ", state=" + state + "]";
    }
    

    
}
