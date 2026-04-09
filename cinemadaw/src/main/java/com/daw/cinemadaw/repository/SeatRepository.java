package com.daw.cinemadaw.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.daw.cinemadaw.domain.cinema.Seat;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {

    Optional<List<Seat>> findByRoomId(Long roomId); //Aquest mètode és una consulta personalitzada que permet buscar seients associats a una sala específica. Spring Data JPA generarà automàticament la implementació d'aquest mètode basant-se en el nom del mètode i el paràmetre proporcionat. Quan s'executi aquest mètode, retornarà una llista de seients que estan associats a la sala amb l'identificador especificat com a paràmetre.

    List<Seat> findByRoomIdOrderByIdAsc(Long roomId);

    List<Seat> findByRoomIdAndIdIn(Long roomId, List<Long> ids);

    
}
