package com.daw.cinemadaw.domain.cinema;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.CascadeType;


@Entity
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    @Column
    private String name;

    @Column
    private int capacity;

    @ManyToOne
    private Cinema cinema; //Relació ManyToOne amb Cinema, ja que una sala (Room) pertany a un cinema (Cinema). Aquesta relació es pot implementar utilitzant anotacions de JPA com @ManyToOne i @JoinColumn per establir la clau forana que connecta la sala amb el cinema corresponent a la base de dades.

    @OneToMany(mappedBy = "room", cascade= CascadeType.ALL, orphanRemoval=true) //Indica que hi ha una relació OneToMany entre Room i Seat, on una sala (Room) pot tenir molts seients (Seats). El paràmetre mappedBy especifica que la relació està mapejada per l'atribut "room" a la classe Seat, el que significa que la classe Seat és la propietària de la relació i conté la clau forana que connecta els seients amb la sala corresponent a la base de dades.
    private List<Seat> seats = new ArrayList<>(); 

    //Constructor buit

    public Room() {
    }

    //Constructor amb paràmetres

    public Room(String name, int capacity) {
        this.name = name;
        this.capacity = capacity;
    }

    //Getters i Setters

    public List<Seat> getSeats() {
        return seats;
    }

    public void setSeats(List<Seat> seats) {
        this.seats = seats;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

     public Cinema getCinema() {
        return cinema;
    }

    public void setCinema(Cinema cinema) {
        this.cinema = cinema;
    }

    @Override

    public String toString() {
        return "Room [id=" + id + ", name=" + name + ", capacity=" + capacity + "]";
    }




}
