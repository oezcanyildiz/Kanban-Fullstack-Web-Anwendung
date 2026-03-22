package com.yildiz.teamsync.controllers;

import com.yildiz.teamsync.services.IOrganizationManagementService;
import com.yildiz.teamsync.services.IOrganizationRegisterService;

import jakarta.validation.Valid;

import com.yildiz.teamsync.dto.OrganizationRequestDTO;
import com.yildiz.teamsync.dto.OrganizationResponseDTO;
import java.util.List;
import com.yildiz.teamsync.dto.UserListResponseDTO;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/organization")
public class OrganizationController {

    private final IOrganizationManagementService organizationManagementService;
    private final IOrganizationRegisterService organizationRegisterService;

    public OrganizationController(IOrganizationManagementService organizationManagementService,
            IOrganizationRegisterService organizationRegisterService) {
        this.organizationManagementService = organizationManagementService;
        this.organizationRegisterService = organizationRegisterService;
    }

    @PostMapping("/register")
    public ResponseEntity<OrganizationResponseDTO> register(@Valid 
            @RequestBody OrganizationRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(organizationRegisterService.registerOrganization(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<UserListResponseDTO>> getEmployeesByOrganizationId(@PathVariable Long id) {
        return ResponseEntity.ok(organizationManagementService.getAllEmployees(id));
    }

    @PatchMapping("/promote/{userID}")
    public ResponseEntity<Void> promoteToLeader(@PathVariable Long userID) {
        organizationManagementService.promoteToTeamLeader(userID);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/demote/{userID}")
    public ResponseEntity<Void> demoteToUser(@PathVariable Long userID) {
        organizationManagementService.demoteToUser(userID);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/remove/{userID}")
    public ResponseEntity<Void> removeUserFromOrganization(@PathVariable Long userID) {
        organizationManagementService.removeUserFromOrganization(userID);
        return ResponseEntity.ok().build();
    }

}
