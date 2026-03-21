package com.yildiz.teamsync.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.yildiz.teamsync.security.AuthTokenFilter;
import com.yildiz.teamsync.security.JwtAuthEntryPoint;
import com.yildiz.teamsync.security.JwtUtils;
import com.yildiz.teamsync.security.UserDetailsServiceImpl;

@Configuration
@EnableMethodSecurity // Erlaubt uns später @PreAuthorize im Controller zu nutzen
public class SecurityConfig {

    private final UserDetailsServiceImpl userDetailsService;
    private final JwtAuthEntryPoint unauthorizedHandler;
    private final JwtUtils jwtUtils;

    // Konstruktor Injection (sehr sauber!)
    public SecurityConfig(UserDetailsServiceImpl userDetailsService, JwtAuthEntryPoint unauthorizedHandler,
            JwtUtils jwtUtils) {
        this.userDetailsService = userDetailsService;
        this.unauthorizedHandler = unauthorizedHandler;
        this.jwtUtils = jwtUtils;
    }

    // Wir machen aus deinem AuthTokenFilter eine offizielle @Bean
    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter(jwtUtils, userDetailsService);
    }

    // Der Provider, der dem AuthenticationManager sagt, wie er Passwörter prüfen
    // soll
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(this.userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    // Dient uns später im AuthController als Werkzeug zum Einloggen
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    // Dieser Encoder muss genau der sein, den du auch in deinem Auth/Organization
    // Controller benutzt hast!
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Das ist die eigentliche Türsteher-Konfiguration!
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // 1. CSRF (Cross-Site Request Forgery) Schutz abschalten,
        // weil es bei JWT-APIs und React nicht gebraucht wird.
        http.csrf(csrf -> csrf.disable())

                // 2. Was passiert bei einem Fehler? Rufe unsere JwtAuthEntryPoint auf!
                .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))

                // 3. Sag Spring: Merke dir keine Session-Cookies! Wir sind 100% Stateless (JWT)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 4. Welche Routen sind erlaubt?
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**", "/api/organization/register", "/error").permitAll() // Registrieren/Login ist für ALLE erlaubt!
                        .anyRequest().authenticated() // Für ALLES andere (Boards, Tasks) brauchst du einen JWT!
                );

        // Füge den Provider hinzu (verknüpft DB + PasswordEncoder)
        http.authenticationProvider(authenticationProvider());

        // GANZ WICHTIG: Stelle unseren eigenen Türsteher (AuthTokenFilter) NOCH VOR den
        // Standard-Spring-Türsteher
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
