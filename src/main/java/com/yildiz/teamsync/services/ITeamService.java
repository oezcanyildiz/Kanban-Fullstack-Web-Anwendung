package com.yildiz.teamsync.services;

import java.util.List;
import com.yildiz.teamsync.dto.TeamCreateRequestDTO;
import com.yildiz.teamsync.dto.TeamCreateResponseDTO;

public interface ITeamService {
    TeamCreateResponseDTO createTeam(TeamCreateRequestDTO requestDTO);
    
    List<TeamCreateResponseDTO> getMyTeams();

    void deleteTeam(Long teamId);
}
