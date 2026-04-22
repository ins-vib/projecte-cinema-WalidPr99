package com.daw.cinemadaw.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.daw.cinemadaw.domain.cinema.Comanda;
import com.daw.cinemadaw.domain.cinema.OrderStatus;
import com.daw.cinemadaw.domain.cinema.Screening;
import com.daw.cinemadaw.domain.cinema.Seat;
import com.daw.cinemadaw.domain.cinema.Ticket;
import com.daw.cinemadaw.repository.ComandaRepository;
import com.daw.cinemadaw.repository.ScreeningRepository;
import com.daw.cinemadaw.repository.SeatRepository;
import com.daw.cinemadaw.repository.TicketRepository;

import jakarta.transaction.Transactional;

@Service
public class TicketService {

    private final ScreeningRepository screeningRepository;
    private final TicketRepository ticketRepository;
    private final SeatRepository seatRepository;
    private final ComandaRepository comandaRepository;

    public TicketService(ScreeningRepository screeningRepository, TicketRepository ticketRepository,
            SeatRepository seatRepository, ComandaRepository comandaRepository) {
        this.screeningRepository = screeningRepository;
        this.ticketRepository = ticketRepository;
        this.seatRepository = seatRepository;
        this.comandaRepository = comandaRepository;
    }
    
    @Transactional
    public Comanda createOrder(Map<Long, List<Long>> cart, String clientName, String email) {
        return crearOrdreTickets(cart, clientName, email);
    }

    public Comanda crearOrdreTickets(Map<Long, List<Long>> cart, String clientName, String email) {

        Comanda comanda = new Comanda();
        comanda.setDateTime(LocalDateTime.now());
        comanda.setClientName(clientName);
        comanda.setEmail(email);
        comanda.setStatus(OrderStatus.CONFIRMADA);

        List<Ticket> tickets = new ArrayList<>();
        double totalAmount = 0.0;

        // instruccions per crear l'ordre a partir del carret de compra
        // per cada screeiningId i llista de seatIds al cart:
        // 1. recuperar la screening i els seients corresponents
        // 2. crear els tickets associats a l'ordre
        // 3. guardar l'ordre i els tickets a la base de dades
        // nomes instruccions base de dades per crear una transacció
        for (Map.Entry<Long, List<Long>> entry : cart.entrySet()) {
            Long screeningId = entry.getKey();
            List<Long> seatIds = entry.getValue();

            if (seatIds == null || seatIds.isEmpty()) {
                continue;
            }

            Screening screening = screeningRepository.findById(screeningId)
                    .orElseThrow(() -> new IllegalArgumentException("Screening no trobat: " + screeningId));

            if (screening.getRoom() == null) {
                throw new IllegalStateException("Screening sense sala: " + screeningId);
            }

            List<Long> uniqueSeatIds = new ArrayList<>(new LinkedHashSet<>(seatIds));
            List<Seat> seats = seatRepository.findByRoomIdAndIdIn(screening.getRoom().getId(), uniqueSeatIds);

            for (Seat seat : seats) {
                if (!seat.isState()) {
                    throw new IllegalStateException("Seient no disponible: " + seat.getId());
                }

                seat.setState(false);

                Ticket ticket = new Ticket(screening.getPrice(), seat, screening, comanda);
                tickets.add(ticket);
                totalAmount += screening.getPrice();
            }

            seatRepository.saveAll(seats);
        }

        comanda.setTotalAmount(totalAmount);
        comanda.setTickets(tickets);

        Comanda savedComanda = comandaRepository.saveAndFlush(comanda);
        ticketRepository.saveAll(tickets);

        return savedComanda;
    }
}





