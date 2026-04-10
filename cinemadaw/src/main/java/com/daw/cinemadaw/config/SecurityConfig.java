
// config/SecurityConfig.java

package com.daw.cinemadaw.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

         http
        // Desactiva CSRF (necessari per H2)
        .csrf(csrf -> csrf.disable())

        // Permet carregar la consola H2 en un iframe
        .headers(headers -> headers
            .frameOptions(frame -> frame.disable())
        )

        // Configuració d'autoritzacions
        .authorizeHttpRequests(auth -> auth

            // Accés públic
            .requestMatchers("/h2-console/**").permitAll()
            .requestMatchers("/", "/home", "/login", "/register", "/css/**", "/CSS/**", "/session", "/session/**").permitAll()

            // Panell d'administració només per ADMIN
            .requestMatchers("/admin", "/admin/**").hasRole("ADMIN")

            // Panell de client visible per CLIENT i ADMIN
            .requestMatchers("/client", "/client/**").hasAnyRole("CLIENT", "ADMIN")

            // Reserva de seients només per CLIENT
            .requestMatchers("/seat/*/reserve", "/seats/*/reserve").hasRole("CLIENT")

            
            .requestMatchers("/cookies", "/cookies/**").hasAnyRole("CLIENT", "ADMIN")

            // Rutes de manteniment (crear/editar/esborrar) només ADMIN
            .requestMatchers(
                "/cinema/create",
                "/cinema/edit/**",
                "/cinema/edit",
                "/cinema/delete/**",
                "/movie/create",
                "/movie/edit/**",
                "/movie/edit",
                "/movie/delete/**",
                "/room/create/**",
                "/room/edit/**",
                "/room/edit",
                "/room/delete/**",
                "/movie/*/screening/create",
                "/screening/edit/**",
                "/screening/edit",
                "/screening/delete/**",
                "/seat/edit/**",
                "/seat/delete/**"
            ).hasRole("ADMIN")

            // Rutes de consulta accessibles a CLIENT i ADMIN
            .requestMatchers(
                "/cinemes",
                "/cinema/**",
                "/movies",
                "/movies/**",
                "/movie/**",
                "/room/**",
                "/seat/**",
                "/seats/**",
                "/screenings/**"
            ).hasAnyRole("ADMIN", "CLIENT")

            // Qualsevol altra petició necessita autenticació
            .anyRequest().authenticated()
        )

        // Configuració del formulari de login
        .formLogin(form -> form
            .loginPage("/login") // pàgina personalitzada de login
            .successHandler(new CustomLoginSuccessHandler()) // redirecció segons rol
            .permitAll()
        )

        // Manté la sessió iniciada entre reinicis de navegador
        .rememberMe(remember -> remember
            .key("cinemadaw-remember-key")
            .tokenValiditySeconds(7 * 24 * 60 * 60)
            .alwaysRemember(true)
        )

        // Configuració del logout
        .logout(logout -> logout
            .logoutUrl("/logout")
            .logoutSuccessUrl("/login?logout")
            .permitAll()
        );

        return http.build();


    }

    // Bean per encriptar contrasenyes amb BCrypt
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}