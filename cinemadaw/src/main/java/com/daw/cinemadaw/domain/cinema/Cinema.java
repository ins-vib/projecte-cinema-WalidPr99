package com.daw.cinemadaw.domain.cinema;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity //Indica que aquesta classe és una entitat JPA (Java Persistence API) i que serà mapejada a una taula a la base de dades. Aquesta anotació és essencial per a que el framework de persistència (com Hibernate) pugui gestionar aquesta classe com una entitat de base de dades.
public class Cinema {

    @Id //Indica que aquest atribut és la clau primària de l'entitat Cinema. La clau primària és un identificador únic per a cada registre a la base de dades.
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Indica que el valor de l'atribut id serà generat automàticament per la base de dades quan s'insereixi un nou registre. La estratègia GenerationType.IDENTITY és una de les maneres més comunes de generar valors per a claus primàries, on la base de dades assigna un valor únic i autoincremental a cada nou registre.

    private Long id; //Atribut id per a la base de dades de tipus Long, ja que és comú utilitzar aquest tipus per a les claus primàries en bases de dades relacionals. Aquest atribut serà utilitzat per identificar de manera única cada cinema a la base de dades.

    //Atributs

    @Column
    private String name;

    @Column
    private String address;

    @Column
    private String city;

    @Column
    private String postalCode;

    //Constructor buit

    public Cinema() {
    }

    //Constructor amb paràmetres

    public Cinema(String name, String address, String city, String postalCode) {
        this.name = name;
        this.address = address;
        this.city = city;
        this.postalCode = postalCode;
    }

    //Getters i Setters

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public String getPostalCode() {
        return postalCode;
    }
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    } 

    @Override

    public String toString() {

        return "Cinema [id=" + id + ", name=" + name + ", address=" + address + ", city=" + city + ", postalCode=" + postalCode + "]";

    }
    
    
}
