package com.daw.cinemadaw.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.daw.cinemadaw.domain.cinema.Comanda;

@Repository
public interface ComandaRepository extends JpaRepository<Comanda, Long> {

    List<Comanda> findByClientNameOrderByDateTimeDesc(String clientName);
}
