package com.daw.cinemadaw.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.daw.cinemadaw.domain.cinema.Comanda;
import com.daw.cinemadaw.domain.cinema.user.Role;
import com.daw.cinemadaw.repository.ComandaRepository;
import com.daw.cinemadaw.service.CustomUserDetails;

@Controller
public class OrderController {

    private final ComandaRepository comandaRepository;

    public OrderController(ComandaRepository comandaRepository) {
        this.comandaRepository = comandaRepository;
    }

    @GetMapping("/client/orders")
    public String listOrders(@AuthenticationPrincipal CustomUserDetails user, Model model) {

        if (!isClient(user)) {
            return "redirect:/home";
        }

        String username = user.getUsername();
        List<Comanda> orders = comandaRepository.findByClientNameOrderByDateTimeDesc(username);

        model.addAttribute("orders", orders);
        return "client/orders";
    }

    @GetMapping("/client/order/{id}")
    public String orderDetail(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails user, Model model) {

        if (!isClient(user)) {
            return "redirect:/home";
        }

        try {
            Optional<Comanda> optional = comandaRepository.findById(id);
            if (optional.isEmpty()) {
                return "redirect:/client/orders";
            }

            Comanda comanda = optional.get();

            if (!comanda.getClientName().equals(user.getUsername())) {
                return "redirect:/client/orders";
            }

            model.addAttribute("comanda", comanda);
            return "client/order-detail";
        } catch (Exception ex) {
            System.out.println("[COMANDA GET ERROR] Error al recuperar la comanda con id=" + id);
            System.out.println("[COMANDA GET ERROR] " + ex.getMessage());
            return "redirect:/client/orders";
        }
    }

    private boolean isClient(CustomUserDetails user) {
        return user != null
                && user.getUser() != null
                && Role.CLIENT.equals(user.getUser().getRole());
    }
}
