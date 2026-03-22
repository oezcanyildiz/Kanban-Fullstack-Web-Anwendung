package com.yildiz.teamsync.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yildiz.teamsync.dto.UserProfileRequestDTO;
import com.yildiz.teamsync.dto.UserProfileResponseDTO;
import com.yildiz.teamsync.services.IUserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final IUserService userService;

    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @PatchMapping("/update")
    public ResponseEntity<UserProfileResponseDTO> updateProfile(@RequestBody @Valid UserProfileRequestDTO requestDTO) {
        UserProfileResponseDTO responseDTO = userService.updateProfile(requestDTO);
        return ResponseEntity.ok(responseDTO);
    }
}
