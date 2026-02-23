package com.daw.cinemadaw.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.daw.cinemadaw.domain.cinema.Cinema;
import com.daw.cinemadaw.domain.cinema.Room;
import com.daw.cinemadaw.repository.CinemaRepository;
import com.daw.cinemadaw.repository.RoomRepository;
import jakarta.transaction.Transactional;

import java.util.Optional;
import java.util.List;



@Component
public class Proves implements CommandLineRunner { //La classe Proves implementa la interfície CommandLineRunner, que és una funcionalitat de Spring Boot que permet executar codi després que l'aplicació s'hagi iniciat completament. Aquesta classe està anotada amb @Component, el que significa que serà detectada i gestionada pel context de Spring com un component.
    //Aquesta classe és útil per a realitzar proves o inicialitzacions després que l'aplicació s'hagi iniciat, com per exemple inserir dades de prova a la base de dades o executar qualsevol codi que necessiti accedir als components de l'aplicació.

    private CinemaRepository cinemaRepository; //Atribut cinemaRepository de tipus CinemaRepository, que és un repositori de Spring Data JPA per a l'entitat Cinema. Aquest atribut serà utilitzat per interactuar amb la base de dades i realitzar operacions relacionades amb els cinemes.
    private RoomRepository roomRepository; //Atribut roomRepository de tipus RoomRepository, que és un repositori de Spring Data JPA per a l'entitat Room. Aquest atribut serà utilitzat per interactuar amb la base de dades i realitzar operacions relacionades amb les sales de cinema.

    public Proves(CinemaRepository cinemaRepository, RoomRepository roomRepository) { //Constructor de la classe Proves que rep un CinemaRepository com a paràmetre. Aquest constructor és utilitzat per a la injecció de dependències, on Spring injectarà automàticament una instància de CinemaRepository quan es creï una instància de Proves. Això permet que Proves pugui utilitzar el cinemaRepository per realitzar operacions a la base de dades.
        this.cinemaRepository = cinemaRepository;
        this.roomRepository = roomRepository;
    }

    @Transactional //Anotació @Transactional que indica que el mètode run() serà executat dins d'una transacció. Això significa que totes les operacions realitzades dins d'aquest mètode seran atòmiques, és a dir, o bé es completaran totes amb èxit o bé es revertiran en cas d'error. Aquesta anotació és útil per garantir la integritat de les dades a la base de dades i evitar problemes de concurrència.
    @Override
    public void run(String... args) throws Exception { //Implementació del mètode run() de la interfície CommandLineRunner. Aquest mètode serà executat automàticament per Spring Boot després que l'aplicació s'hagi iniciat. El paràmetre args és un array de Strings que pot contenir arguments de línia de comandes, però en aquest cas no s'estan utilitzant.
        System.out.println("Proves de Spring Boot");

        
        Cinema cinema1 = new Cinema("Cinemes Girona", "Carrer Girona, 175", "Barcelona", "08037");
        cinemaRepository.save(cinema1);

        Room room1 = new Room("Sala 1", 50);
        room1.setCinema(cinema1);
        roomRepository.save(room1);

        Room room2 = new Room("Sala 2", 100);
        room2.setCinema(cinema1);
        roomRepository.save(room2);

        Room room3 = new Room("Sala 3", 150);
        room3.setCinema(cinema1);
        roomRepository.save(room3);

        System.out.println(room3.getCinema().getName());

        Optional<Cinema> optionalCinema = cinemaRepository.findById(1L); //Utilitza el mètode findById() del cinemaRepository per buscar un cinema amb l'id 1L (Long).

        if(optionalCinema.isPresent()){ //Comprova si el cinema amb l'id 1L existeix a la base de dades utilitzant el mètode isPresent() de l'Optional. Si el cinema existeix, es procedeix a imprimir-lo a la consola.

            Cinema cinema = optionalCinema.get(); //Si el cinema existeix, es recupera l'objecte Cinema de l'Optional utilitzant el mètode get() i es guarda a la variable cinema.
            List<Room> rooms = cinema.getRooms(); //Utilitza el mètode getRooms() de l'objecte Cinema per obtenir la llista de sales (rooms) associades a aquest cinema. Aquesta llista es guarda a la variable rooms.
            
            for(Room room: rooms){ //Bucle for per recorrer la llista de sales (rooms) associades al cinema i imprimir cada sala a la consola.

                System.out.println(room);

            }

        }else{

            System.out.println("No trobbat"); //Si el cinema no existeix, es imprimeix un missatge a la consola indicant que el cinema amb l'id indicat no existeix a la base de dades.
        }
/* 
//READ-----------------------------------------------------------------------------------------------------------------------------------------------------------

        //per imprimir TOTS els cinemes a la consola

        List<Cinema> cinemas = cinemaRepository.findAll(); //Utilitza el mètode findAll() del cinemaRepository per obtenir una llista de tots els cinemes emmagatzemats a la base de dades. Aquest mètode retorna una llista de Cinema, que es guarda a la variable cinemas.

        for (Cinema cinema : cinemas) { //Bucle for per recorrer la llista i imprimir cada cinema a la consola.

            System.out.println(cinema);

        }

        //per imprimir un cinema per id a la consola

        Optional<Cinema> optionalCinema = cinemaRepository.findById(1L); //Utilitza el mètode findById() del cinemaRepository per buscar un cinema amb l'id 1L (Long).

        if(optionalCinema.isPresent()){ //Comprova si el cinema amb l'id 1L existeix a la base de dades utilitzant el mètode isPresent() de l'Optional. Si el cinema existeix, es procedeix a imprimir-lo a la consola.

            Cinema cinema = optionalCinema.get(); //Si el cinema existeix, es recupera l'objecte Cinema de l'Optional utilitzant el mètode get() i es guarda a la variable cinema.
            System.out.println(cinema); //Imprimeix el cinema recuperat a la consola.

//UPDATE---------------------------------------------------------------------------------------------------------------------------------------------------------

            cinema.setCity("Reus"); //Modifica la ciutat del cinema recuperat a "Reus" utilitzant el mètode setCity() de l'objecte Cinema.
            cinemaRepository.save(cinema); //Guarda el cinema modificat a la base de dades utilitzant el mètode save() del cinemaRepository. Aquest mètode actualitzarà el registre existent a la base de dades amb les noves dades del cinema.

        }else{

            System.out.println("No trobbat"); //Si el cinema no existeix, es imprimeix un missatge a la consola indicant que el cinema amb l'id indicat no existeix a la base de dades.
        }

        List<Cinema> llista2 = cinemaRepository.findByCity("Reus"); //Utilitza el mètode findByCity() del cinemaRepository per buscar cinemes per ciutat. Aquest mètode retorna una llista de cinemes que es troben a la ciutat especificada com a paràmetre. En aquest cas, es busca la ciutat "Barcelona".

        for (Cinema cinema : llista2) { //Bucle for per recorrer la llista de cinemes trobats per ciutat i imprimir cada cinema a la consola.

            System.out.println(cinema);

        }

//DELETE---------------------------------------------------------------------------------------------------------------------------------------------------------

        //Borrar un cinmea per posició a la llista

        cinemaRepository.delete(llista2.get(0)); //Utilitza el mètode delete() del cinemaRepository per eliminar un cinema de la base de dades. En aquest cas, es elimina el primer cinema de la llista de cinemes trobats per ciutat (llista2.get(0)). Aquest mètode eliminarà el registre corresponent a aquest cinema de la base de dades.
        cinemas = cinemaRepository.findAll(); //Després d'eliminar el cinema, es torna a obtenir la llista de tots els cinemes a la base de dades utilitzant el mètode findAll() del cinemaRepository. Aquesta nova llista reflectirà els canvis després de l'eliminació.

        for (Cinema cinema : cinemas) { //Bucle for per recorrer la nova llista de cinemes trobats per ciutat després de l'eliminació i imprimir cada cinema a la consola.

            System.out.println(cinema);

        }
    }

    /* */
    
    }
}

    
