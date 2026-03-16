package com.yildiz.teamsync.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.yildiz.teamsync.entities.User;
import com.yildiz.teamsync.repositories.UserRepository;
import com.yildiz.teamsync.security.UserDetailsImpl;

@Component
public class SecurityUtils {

    private final UserRepository userRepository;

    // Nur das Repository wird injiziert!
    public SecurityUtils(UserRepository userRepository) {
        this.userRepository = userRepository;

    }

    public Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetailsImpl) {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            return userDetails.getId();
        }
        return null;
    }

    // 2. Den kompletten User aus der DB holen (praktisch für Services)
    public User getCurrentUserEntity() {
        Long id = getCurrentUserId();
        if (id == null) {
            throw new RuntimeException("Kein authentifizierter Benutzer gefunden!");
        }
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Benutzer mit ID " + id + " nicht in DB gefunden"));
    }
}
