package com.daw.cinemadaw.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.daw.cinemadaw.domain.cinema.Movie;


@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    
    List<Movie> findByGenre(String genre); //Aquest mètode és una consulta personalitzada que permet buscar pel gènere de la pel·lícula. Spring Data JPA generarà automàticament la implementació d'aquest mètode basant-se en el nom del mètode i el paràmetre proporcionat.
    
    @Query("""
            
            SELECT DISTINCT s.movie
            FROM Screening s
            WHERE s.dateTime >= CURRENT_TIMESTAMP

            """)

            List<Movie> findMovieesWithFuturesScreenings(); 
}


