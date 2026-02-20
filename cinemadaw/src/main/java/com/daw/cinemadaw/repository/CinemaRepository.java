package com.daw.cinemadaw.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import com.daw.cinemadaw.domain.cinema.Cinema;

@Repository
public interface CinemaRepository extends JpaRepository<Cinema, Long> {

//Aquesta interfície és un repositori de Spring Data JPA que proporciona operacions CRUD (Create, Read, Update, Delete) per a l'entitat Cinema. 
//En extends JpaRepository, hereta una sèrie de mètodes predefinits per a gestionar les entitats Cinema a la base de dades, com ara save(), findById(), findAll(), deleteById(), entre altres. 
//El primer paràmetre de JpaRepository és el tipus d'entitat (Cinema) i el segon paràmetre és el tipus de la clau primària (Long). Aquesta interfície permet interactuar amb la base de dades de manera senzilla i eficient, sense necessitat d'escriure codi SQL manualment.

List<Cinema> findByCity(String city); //Aquest mètode és una consulta personalitzada que permet buscar cinemes per la seva ciutat. Spring Data JPA generarà automàticament la implementació d'aquest mètode basant-se en el nom del mètode i el paràmetre proporcionat. Quan s'executi aquest mètode, retornarà una llista de cinemes que es troben a la ciutat especificada com a paràmetre.



}
