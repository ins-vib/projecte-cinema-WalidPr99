package com.daw.cinemadaw.domain.cinema;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity //Indica que aquesta classe és una entitat JPA (Java Persistence API) i que serà mapejada a una taula a la base de dades. Aquesta anotació és essencial per a que el framework de persistència (com Hibernate) pugui gestionar aquesta classe com una entitat de base de dades.
public class Cinema {

    @Id //Indica que aquest atribut és la clau primària de l'entitat Cinema. La clau primària és un identificador únic per a cada registre a la base de dades.
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Indica que el valor de l'atribut id serà generat automàticament per la base de dades quan s'insereixi un nou registre. La estratègia GenerationType.IDENTITY és una de les maneres més comunes de generar valors per a claus primàries, on la base de dades assigna un valor únic i autoincremental a cada nou registre.

    private Long id; //Atribut id per a la base de dades de tipus Long, ja que és comú utilitzar aquest tipus per a les claus primàries en bases de dades relacionals. Aquest atribut serà utilitzat per identificar de manera única cada cinema a la base de dades.

    //Atributs
    
    @NotBlank(message = "El nom del cinema es obligatori") //Indica que el camp name no pot estar vacío. Si se intenta guardar un objeto Cinema con el campo name vacío, se lanzará una excepción de validación con el mensaje especificado.
    @Size (min = 2, max = 110, message = "El nom del cinema no pot superar els 150 caràcters ni ser inferiro a 5 caracters") //Indica que el campo name no puede tener más de 100 caracteres. Si se intenta guardar un objeto Cinema con un campo name que exceda los 100 caracteres, se lanzará una excepción de validación con el mensaje especificado.
    @Column
    private String name;

    @NotBlank(message = "L'adreça del cinema es obligatoria")
    @Size (min = 5, max = 150, message = "L'adreça del cinema no pot superar els 150 caràcters ni ser inferiro a 5 caracters")
    @Column
    private String address;

    @NotBlank(message = "La ciutat del cinema es obligatòria")
    @Size (min = 2, max = 150, message = "La ciutat del cinema no pot superar els 150 caràcters ni ser inferiro a 5 caracters")
    @Column
    private String city;

    @NotBlank(message = "El codi postal del cinema es obligatori")
    @Pattern(regexp = "\\d{5}", message = "El codi postal del cinema ha de ser un número de 5 dígitos") //Indica que el campo postalCode debe seguir un patrón específico, en este caso, debe ser un número de 5 dígitos. Si se intenta guardar un objeto Cinema con un campo postalCode que no cumpla con este patrón, se lanzará una excepción de validación con el mensaje especificado.
    @Column
    private String postalCode;

    @OneToMany (mappedBy = "cinema", cascade = CascadeType.ALL, orphanRemoval=true) //Indica que hi ha una relació OneToMany entre Cinema i Room, on un cinema pot tenir moltes sales (rooms). El paràmetre mappedBy especifica que la relació està mapejada per l'atribut "cinema" a la classe Room, el que significa que la classe Room és la propietària de la relació i conté la clau forana que connecta les sales amb el cinema corresponent a la base de dades.
    private List<Room> rooms = new ArrayList<>(); //Atribut rooms que és una llista de Room, representant les sales associades a aquest cinema. Aquesta llista serà utilitzada per gestionar la relació entre Cinema i Room, permetent accedir a les sales d'un cinema i realitzar operacions relacionades amb elles. La inicialització amb new ArrayList<>() assegura que la llista no sigui null i estigui preparada per ser utilitzada.

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

    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }
    
    @Override

    public String toString() {

        return "Cinema [id=" + id + ", name=" + name + ", address=" + address + ", city=" + city + ", postalCode=" + postalCode + "]";

    }     
}
