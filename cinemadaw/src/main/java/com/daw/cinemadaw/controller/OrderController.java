package com.daw.cinemadaw.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.daw.cinemadaw.domain.cinema.Comanda;
import com.daw.cinemadaw.repository.ComandaRepository;

@Controller
public class OrderController {

    private final ComandaRepository comandaRepository;

    public OrderController(ComandaRepository comandaRepository) {
        this.comandaRepository = comandaRepository;
    }

    @GetMapping("/client/orders")
    public String listOrders(Authentication authentication, Model model) {

        if (!isClient(authentication)) {
            return "redirect:/home";
        }

        String username = authentication.getName();
        List<Comanda> orders = comandaRepository.findByClientNameOrderByDateTimeDesc(username);

        model.addAttribute("orders", orders);
        return "client/orders";
    }

    @GetMapping("/client/order/{id}")
    public String orderDetail(@PathVariable Long id, Authentication authentication, Model model) {

        if (!isClient(authentication)) {
            return "redirect:/home";
        }

        Optional<Comanda> optional = comandaRepository.findById(id);
        if (optional.isEmpty()) {
            return "redirect:/client/orders";
        }

        Comanda comanda = optional.get();

        if (!comanda.getClientName().equals(authentication.getName())) {
            return "redirect:/client/orders";
        }

        model.addAttribute("comanda", comanda);
        return "client/order-detail";
    }

    private boolean isClient(Authentication authentication) {
        if (authentication == null || authentication.getAuthorities() == null) {
            return false;
        }
        return authentication.getAuthorities().stream()
                .anyMatch(authority -> "ROLE_CLIENT".equals(authority.getAuthority()));
    }
}
