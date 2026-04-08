package com.daw.cinemadaw.config;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@Component
@ControllerAdvice
public class GlobalRoleModelAttributes {

    @ModelAttribute("isAdmin")
    public boolean isAdmin(Authentication authentication) {
        return hasRole(authentication, "ADMIN");
    }

    @ModelAttribute("isClient")
    public boolean isClient(Authentication authentication) {
        return hasRole(authentication, "CLIENT");
    }

    private boolean hasRole(Authentication authentication, String role) {
        if (authentication == null || authentication.getAuthorities() == null) {
            return false;
        }

        String roleName = "ROLE_" + role;
        return authentication.getAuthorities().stream()
                .anyMatch(authority -> roleName.equals(authority.getAuthority()) || role.equals(authority.getAuthority()));
    }
}