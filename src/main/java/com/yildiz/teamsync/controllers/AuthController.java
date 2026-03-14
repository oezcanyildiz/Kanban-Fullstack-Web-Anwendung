package com.yildiz.teamsync.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yildiz.teamsync.dto.UserLoginRequestDTO;
import com.yildiz.teamsync.dto.UserLoginResponseDTO;
import com.yildiz.teamsync.dto.UserRegisterRequestDTO;
import com.yildiz.teamsync.dto.UserRegisterResponseDTO;
import com.yildiz.teamsync.services.IAuthService;
import com.yildiz.teamsync.services.impl.AuthService;

@RestController
@RequestMapping("/api/auth")

public class AuthController {

    private final IAuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;

    }

    @PostMapping("/login")
    public ResponseEntity<UserLoginResponseDTO> login(@RequestBody UserLoginRequestDTO request) {
        return ResponseEntity.ok(authService.userLogin(request));
    }

    @PostMapping("/register")
    public ResponseEntity<UserRegisterResponseDTO> register(@RequestBody UserRegisterRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.userRegister(request));
    }
}