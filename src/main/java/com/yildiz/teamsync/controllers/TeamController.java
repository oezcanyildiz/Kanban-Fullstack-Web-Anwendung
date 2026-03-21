package com.yildiz.teamsync.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yildiz.teamsync.dto.TeamCreateRequestDTO;
import com.yildiz.teamsync.dto.TeamCreateResponseDTO;
import com.yildiz.teamsync.services.ITeamService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/teams")
public class TeamController {

    private final ITeamService teamService;

    public TeamController(ITeamService teamService) {
        this.teamService = teamService;
    }

    @PostMapping("/create")
    public ResponseEntity<TeamCreateResponseDTO> createTeam(@Valid @RequestBody TeamCreateRequestDTO requestDTO) {
        TeamCreateResponseDTO response = teamService.createTeam(requestDTO);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/my-teams")
    public ResponseEntity<List<TeamCreateResponseDTO>> getMyTeams() {
        return ResponseEntity.ok(teamService.getMyTeams());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeam(@PathVariable Long id) {
        teamService.deleteTeam(id);
        return ResponseEntity.ok().build();
    }
}
