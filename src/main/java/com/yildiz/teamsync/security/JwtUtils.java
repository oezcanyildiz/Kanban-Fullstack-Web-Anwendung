package com.yildiz.teamsync.security;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtUtils {

    @Value("${teamsync.jwt.secret}")
    private String jwtSecret;

    @Value("${teamsync.jwt.expirationMs}")
    private int jwtExpirationMs;

    // Erstellt den Key für die Signatur
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    // Generiert den JWT Token
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username) // Der "Besitzer" des Tokens
                .setIssuedAt(new Date()) // Wann wurde es erstellt?
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs)) // Wann läuft es ab?
                .signWith(getSigningKey(), SignatureAlgorithm.HS256) // Die Signatur!
                .compact();
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // 3. TOKEN VALIDIEREN (Ist es echt? Ist es abgelaufen?)
    public boolean validateJwtToken(String authToken) {
        if (jwtSecret == null || jwtSecret.isBlank()) {
            log.error("JWT Secret ist nicht gesetzt. Bitte setzen Sie teamsync.jwt.secret in Umgebungsvariablen oder application.properties.");
            return false;
        }

        try {
            Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(authToken);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.error("Ungültiges JWT Token: {}", e.getMessage());
        }
        return false;
    }

}
