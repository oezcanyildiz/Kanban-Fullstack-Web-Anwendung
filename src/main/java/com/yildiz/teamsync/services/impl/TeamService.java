package com.yildiz.teamsync.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yildiz.teamsync.config.SecurityUtils;
import com.yildiz.teamsync.dto.TeamCreateRequestDTO;
import com.yildiz.teamsync.dto.TeamCreateResponseDTO;
import com.yildiz.teamsync.entities.Team;
import com.yildiz.teamsync.entities.TeamMember;
import com.yildiz.teamsync.entities.User;
import com.yildiz.teamsync.enums.UserRole;
import com.yildiz.teamsync.exceptions.AccessDeniedException;
import com.yildiz.teamsync.repositories.TeamRepository;
import com.yildiz.teamsync.repositories.TeamMemberRepository;
import com.yildiz.teamsync.repositories.BoardRepository;
import com.yildiz.teamsync.services.ITeamService;
import com.yildiz.teamsync.dto.BoardListResponseDTO;

@Service
public class TeamService implements ITeamService {

    private final TeamRepository teamRepository;
    private final SecurityUtils securityUtils;
    private final BoardRepository boardRepository;
    private final TeamMemberRepository teamMemberRepository;

    public TeamService(TeamRepository teamRepository, SecurityUtils securityUtils, BoardRepository boardRepository, TeamMemberRepository teamMemberRepository) {
        this.teamRepository = teamRepository;
        this.securityUtils = securityUtils;
        this.boardRepository = boardRepository;
        this.teamMemberRepository = teamMemberRepository;
    }

    @Override
    @Transactional
    public TeamCreateResponseDTO createTeam(TeamCreateRequestDTO requestDTO) {
        User currentUser = securityUtils.getCurrentUserEntity();

        if (currentUser.getRole() != UserRole.ORG_ADMIN && currentUser.getRole() != UserRole.TEAM_LEADER) {
            throw new AccessDeniedException("Nur Admins oder Team-Leader können Teams erstellen.");
        }

        Team team = new Team();
        team.setTeamName(requestDTO.getTeamName());
        team.setOwner(currentUser);
        team.setOrganization(currentUser.getOrganization());

        Team savedTeam = teamRepository.save(team);

        // Automatisch den Ersteller auch direkt als Team-Mitglied registrieren
        TeamMember ownerMember = new TeamMember();
        ownerMember.setTeam(savedTeam);
        ownerMember.setUser(currentUser);
        teamMemberRepository.save(ownerMember);

        TeamCreateResponseDTO response = new TeamCreateResponseDTO();
        response.setTeamID(savedTeam.getTeamID());
        response.setTeamName(savedTeam.getTeamName());
        
        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TeamCreateResponseDTO> getMyTeams() {
        User currentUser = securityUtils.getCurrentUserEntity();

        // Teams finden, wo der User Owner ist ODER Member
        List<Team> teams = teamRepository.findTeamsByUserAsOwnerOrMember(currentUser.getUserID());
        return teams.stream()
                .map(team -> {
                    TeamCreateResponseDTO dto = new TeamCreateResponseDTO();
                    dto.setTeamID(team.getTeamID());
                    dto.setTeamName(team.getTeamName());

                    java.util.List<BoardListResponseDTO> boards = boardRepository.findAll().stream()
                        .filter(b -> b.getTeam() != null && b.getTeam().getTeamID() != null && b.getTeam().getTeamID().equals(team.getTeamID()))
                        .filter(b -> !b.isDeleted())
                        .map(b -> {
                            BoardListResponseDTO bd = new BoardListResponseDTO();
                            bd.setBoardID(b.getBoardID());
                            bd.setBoardName(b.getBoardName());
                            bd.setTeamName(team.getTeamName());
                            return bd;
                        })
                        .toList();
                    dto.setBoards(boards);

                    return dto;
                })
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteTeam(Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team nicht gefunden!"));

        User currentUser = securityUtils.getCurrentUserEntity();
        boolean isAdmin = currentUser.getRole() == UserRole.ORG_ADMIN;
        boolean isOwner = team.getOwner().getUserID().equals(currentUser.getUserID());

        if (!isAdmin && !isOwner) {
            throw new AccessDeniedException("Nur der Admin oder der Team-Owner darf dieses Team löschen.");
        }

        team.setDeleted(true);
        teamRepository.save(team);
    }
}
