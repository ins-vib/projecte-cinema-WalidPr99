package com.daw.cinemadaw.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.daw.cinemadaw.domain.cinema.Comanda;
import com.daw.cinemadaw.domain.cinema.OrderStatus;
import com.daw.cinemadaw.domain.cinema.Screening;
import com.daw.cinemadaw.domain.cinema.Seat;
import com.daw.cinemadaw.domain.cinema.Ticket;
import com.daw.cinemadaw.dto.CartEntryDTO;
import com.daw.cinemadaw.repository.ComandaRepository;
import com.daw.cinemadaw.repository.ScreeningRepository;
import com.daw.cinemadaw.repository.SeatRepository;
import com.daw.cinemadaw.service.CartSessionService;

import jakarta.servlet.http.HttpSession;

@Controller
public class CartController {

    private final ScreeningRepository screeningRepository;
    private final SeatRepository seatRepository;
    private final CartSessionService cartSessionService;
    private final ComandaRepository comandaRepository;

    public CartController(ScreeningRepository screeningRepository, SeatRepository seatRepository,
                          CartSessionService cartSessionService, ComandaRepository comandaRepository) {
        this.screeningRepository = screeningRepository;
        this.seatRepository = seatRepository;
        this.cartSessionService = cartSessionService;
        this.comandaRepository = comandaRepository;
    }

    @GetMapping("/client/cart")
    public String viewCart(@RequestParam(value = "addedCount", required = false, defaultValue = "0") int addedCount,
                           @RequestParam(value = "alreadyInCartCount", required = false, defaultValue = "0") int alreadyInCartCount,
                           @RequestParam(value = "skippedCount", required = false, defaultValue = "0") int skippedCount,
                           @RequestParam(value = "reservedCount", required = false, defaultValue = "0") int reservedCount,
                           @RequestParam(value = "checkoutSkippedCount", required = false, defaultValue = "0") int checkoutSkippedCount,
                           @RequestParam(value = "screeningRemoved", required = false, defaultValue = "false") boolean screeningRemoved,
                           @RequestParam(value = "seatRemoved", required = false, defaultValue = "false") boolean seatRemoved,
                           Authentication authentication,
                           HttpSession session,
                           Model model) {

        if (!isClient(authentication)) {
            return "redirect:/home";
        }

        List<CartEntryDTO> entries = buildCartEntries(session);

        int seatCount = 0;
        double totalPrice = 0.0;

        for (CartEntryDTO entry : entries) {
            seatCount += entry.getSeats().size();
            totalPrice += entry.getSubtotal();
        }

        model.addAttribute("entries", entries);
        model.addAttribute("seatCount", seatCount);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("addedCount", Math.max(addedCount, 0));
        model.addAttribute("alreadyInCartCount", Math.max(alreadyInCartCount, 0));
        model.addAttribute("skippedCount", Math.max(skippedCount, 0));
        model.addAttribute("reservedCount", Math.max(reservedCount, 0));
        model.addAttribute("checkoutSkippedCount", Math.max(checkoutSkippedCount, 0));
        model.addAttribute("screeningRemoved", screeningRemoved);
        model.addAttribute("seatRemoved", seatRemoved);
        return "client/cart";
    }

    @PostMapping("/client/cart/remove-seat")
    public String removeSeatFromCart(@RequestParam("screeningId") Long screeningId,
                                     @RequestParam("seatId") Long seatId,
                                     Authentication authentication,
                                     HttpSession session) {

        if (!isClient(authentication)) {
            return "redirect:/home";
        }

        cartSessionService.removeSeat(session, screeningId, seatId);
        return "redirect:/client/cart?seatRemoved=true";
    }

    @PostMapping("/client/cart/remove-screening")
    public String removeScreeningFromCart(@RequestParam("screeningId") Long screeningId,
                                          Authentication authentication,
                                          HttpSession session) {

        if (!isClient(authentication)) {
            return "redirect:/home";
        }

        cartSessionService.removeScreening(session, screeningId);
        return "redirect:/client/cart?screeningRemoved=true";
    }

    @PostMapping("/client/cart/checkout")
    @Transactional
    public String checkout(Authentication authentication, HttpSession session) {

        if (!isClient(authentication)) {
            return "redirect:/home";
        }

        HashMap<Long, ArrayList<Long>> cart = cartSessionService.getOrCreateCart(session);

        int reservedCount = 0;
        int skippedCount = 0;

        Comanda comanda = new Comanda();
        comanda.setDateTime(LocalDateTime.now());
        comanda.setClientName(authentication.getName());
        comanda.setEmail(authentication.getName());
        comanda.setStatus(OrderStatus.CONFIRMADA);
        comanda.setTotalAmount(0);

        List<Ticket> tickets = new ArrayList<>();

        for (Map.Entry<Long, ArrayList<Long>> cartEntry : cart.entrySet()) {
            Long screeningId = cartEntry.getKey();
            ArrayList<Long> seatIds = new ArrayList<>(new LinkedHashSet<>(cartEntry.getValue()));

            if (seatIds.isEmpty()) {
                continue;
            }

            Optional<Screening> optionalScreening = screeningRepository.findById(screeningId);
            if (optionalScreening.isEmpty() || optionalScreening.get().getRoom() == null) {
                skippedCount += seatIds.size();
                continue;
            }

            Screening screening = optionalScreening.get();
            Long roomId = screening.getRoom().getId();
            List<Seat> seats = seatRepository.findByRoomIdAndIdIn(roomId, seatIds);

            for (Seat seat : seats) {
                if (seat.isState()) {
                    seat.setState(false);
                    Ticket ticket = new Ticket(screening.getPrice(), seat, screening, comanda);
                    tickets.add(ticket);
                    reservedCount++;
                } else {
                    skippedCount++;
                }
            }

            seatRepository.saveAll(seats);
        }

        if (reservedCount > 0) {
            double total = tickets.stream().mapToDouble(Ticket::getPrice).sum();
            comanda.setTotalAmount(total);
            comanda.setTickets(tickets);
            tickets.forEach(t -> t.setComanda(comanda));
            comandaRepository.save(comanda);
        }

        cartSessionService.clearCart(session);

        String redirect = "redirect:/client/cart?reservedCount=" + reservedCount;
        if (skippedCount > 0) {
            redirect += "&checkoutSkippedCount=" + skippedCount;
        }
        return redirect;
    }

    private List<CartEntryDTO> buildCartEntries(HttpSession session) {

        HashMap<Long, ArrayList<Long>> cart = cartSessionService.getOrCreateCart(session);
        List<CartEntryDTO> entries = new ArrayList<>();
        List<Long> screeningIdsToRemove = new ArrayList<>();

        for (Map.Entry<Long, ArrayList<Long>> cartEntry : cart.entrySet()) {
            Long screeningId = cartEntry.getKey();
            List<Long> requestedSeatIds = new ArrayList<>(new LinkedHashSet<>(cartEntry.getValue()));

            if (requestedSeatIds.isEmpty()) {
                screeningIdsToRemove.add(screeningId);
                continue;
            }

            Optional<Screening> optionalScreening = screeningRepository.findById(screeningId);
            if (optionalScreening.isEmpty() || optionalScreening.get().getRoom() == null) {
                screeningIdsToRemove.add(screeningId);
                continue;
            }

            Screening screening = optionalScreening.get();
            Long roomId = screening.getRoom().getId();
            List<Seat> seats = seatRepository.findByRoomIdAndIdIn(roomId, requestedSeatIds);

            if (seats.isEmpty()) {
                screeningIdsToRemove.add(screeningId);
                continue;
            }

            seats.sort(Comparator.comparingInt(Seat::getY).thenComparingInt(Seat::getX));

            ArrayList<Long> normalizedSeatIds = new ArrayList<>();
            int unavailableCount = 0;
            for (Seat seat : seats) {
                normalizedSeatIds.add(seat.getId());
                if (!seat.isState()) {
                    unavailableCount++;
                }
            }

            cartEntry.setValue(normalizedSeatIds);

            CartEntryDTO entry = new CartEntryDTO();
            entry.setScreeningId(screeningId);
            entry.setMovieTitle(screening.getMovie() != null ? screening.getMovie().getTitle() : "Pellicula no disponible");
            entry.setCinemaName(screening.getRoom().getCinema() != null ? screening.getRoom().getCinema().getName() : "Cinema no disponible");
            entry.setRoomName(screening.getRoom().getName());
            entry.setDateTime(screening.getDateTime());
            entry.setPricePerSeat(screening.getPrice());
            entry.setSeats(seats);
            entry.setUnavailableCount(unavailableCount);
            entry.setAvailableCount(seats.size() - unavailableCount);
            entry.setSubtotal(screening.getPrice() * seats.size());

            entries.add(entry);
        }

        for (Long screeningId : screeningIdsToRemove) {
            cart.remove(screeningId);
        }

        session.setAttribute(CartSessionService.CART_SESSION_KEY, cart);

        entries.sort((left, right) -> {
            if (left.getDateTime() == null && right.getDateTime() == null) {
                return left.getScreeningId().compareTo(right.getScreeningId());
            }
            if (left.getDateTime() == null) {
                return 1;
            }
            if (right.getDateTime() == null) {
                return -1;
            }
            return left.getDateTime().compareTo(right.getDateTime());
        });

        return entries;
    }

    private boolean isClient(Authentication authentication) {

        if (authentication == null || authentication.getAuthorities() == null) {
            return false;
        }

        return authentication.getAuthorities().stream()
                .anyMatch(authority -> "ROLE_CLIENT".equals(authority.getAuthority()));
    }
}
