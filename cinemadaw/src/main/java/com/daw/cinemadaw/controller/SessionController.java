package com.daw.cinemadaw.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/session")
public class SessionController {

    // Establir dades a la sessio
    @GetMapping("/setSessionData")
    public String setSessionData(@RequestParam("username") String username, HttpSession session) {
        session.setAttribute("username", username);

        String horaActual = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        session.setAttribute("horaActual", horaActual);

        return "redirect:/session/showSessionData";
    }

    // Mostrar dades de la sessio
    @GetMapping("/showSessionData")
    public String showSessionData(HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");
        model.addAttribute("username", username);

        return "session/sessionData";
    }

    // Ruta per mostrar el formulari
    @GetMapping({"", "/"})
    public String index() {
        return "session/index";
    }
}
