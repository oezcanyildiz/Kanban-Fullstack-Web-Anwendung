package com.yildiz.teamsync.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yildiz.teamsync.dto.TeamMemberAddRequestDTO;
import com.yildiz.teamsync.dto.UserSearchResponseDTO;
import com.yildiz.teamsync.services.ITeamMemberService;

@RestController
@RequestMapping("/team-members")
public class TeamMemberController {

    private final ITeamMemberService teamMemberService;

    public TeamMemberController(ITeamMemberService teamMemberService) {
        this.teamMemberService = teamMemberService;
    }

    @PostMapping("/add")
    public ResponseEntity<Void> addMemberToTeam(@RequestBody TeamMemberAddRequestDTO request) {
        teamMemberService.addMemberToTeam(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<UserSearchResponseDTO>> searchUsers(@RequestParam String query) {
        return ResponseEntity.ok(teamMemberService.searchUsers(query));
    }
}
