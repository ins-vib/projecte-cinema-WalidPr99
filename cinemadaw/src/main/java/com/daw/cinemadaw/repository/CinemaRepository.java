package com.daw.cinemadaw.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.daw.cinemadaw.domain.cinema.Cinema;

@Repository
public interface CinemaRepository extends JpaRepository<Cinema, Long> {

//Aquesta interfície és un repositori de Spring Data JPA que proporciona operacions CRUD (Create, Read, Update, Delete) per a l'entitat Cinema. 
//En extends JpaRepository, hereta una sèrie de mètodes predefinits per a gestionar les entitats Cinema a la base de dades, com ara save(), findById(), findAll(), deleteById(), entre altres. 
//El primer paràmetre de JpaRepository és el tipus d'entitat (Cinema) i el segon paràmetre és el tipus de la clau primària (Long). Aquesta interfície permet interactuar amb la base de dades de manera senzilla i eficient, sense necessitat d'escriure codi SQL manualment.


}
