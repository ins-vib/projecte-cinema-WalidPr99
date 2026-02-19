package com.daw.cinemadaw.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.daw.cinemadaw.domain.cinema.Cinema;
import com.daw.cinemadaw.repository.CinemaRepository;

@Component
public class Proves implements CommandLineRunner { //La classe Proves implementa la interfície CommandLineRunner, que és una funcionalitat de Spring Boot que permet executar codi després que l'aplicació s'hagi iniciat completament. Aquesta classe està anotada amb @Component, el que significa que serà detectada i gestionada pel context de Spring com un component.
    //Aquesta classe és útil per a realitzar proves o inicialitzacions després que l'aplicació s'hagi iniciat, com per exemple inserir dades de prova a la base de dades o executar qualsevol codi que necessiti accedir als components de l'aplicació.

    private CinemaRepository cinemaRepository; //Atribut cinemaRepository de tipus CinemaRepository, que és un repositori de Spring Data JPA per a l'entitat Cinema. Aquest atribut serà utilitzat per interactuar amb la base de dades i realitzar operacions relacionades amb els cinemes.

    public Proves(CinemaRepository cinemaRepository) { //Constructor de la classe Proves que rep un CinemaRepository com a paràmetre. Aquest constructor és utilitzat per a la injecció de dependències, on Spring injectarà automàticament una instància de CinemaRepository quan es creï una instància de Proves. Això permet que Proves pugui utilitzar el cinemaRepository per realitzar operacions a la base de dades.
        this.cinemaRepository = cinemaRepository;
    }

    @Override
    public void run(String... args) throws Exception { //Implementació del mètode run() de la interfície CommandLineRunner. Aquest mètode serà executat automàticament per Spring Boot després que l'aplicació s'hagi iniciat. El paràmetre args és un array de Strings que pot contenir arguments de línia de comandes, però en aquest cas no s'estan utilitzant.
        System.out.println("Proves de Spring Boot");

        Cinema cinema1 = new Cinema("Cinemes Girona", "Carrer Girona, 175", "Barcelona", "08037");
        cinemaRepository.save(cinema1);
    }

}
