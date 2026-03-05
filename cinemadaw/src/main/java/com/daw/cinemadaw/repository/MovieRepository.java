package com.daw.cinemadaw.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.daw.cinemadaw.domain.cinema.Movie;

import java.util.List;


@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    
    List<Movie> findByGenre(String genre); //Aquest mètode és una consulta personalitzada que permet buscar pel gènere de la pel·lícula. Spring Data JPA generarà automàticament la implementació d'aquest mètode basant-se en el nom del mètode i el paràmetre proporcionat.
}


