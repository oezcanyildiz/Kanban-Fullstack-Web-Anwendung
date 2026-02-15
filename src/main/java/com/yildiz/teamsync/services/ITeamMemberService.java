package com.yildiz.teamsync.services;

import java.util.List;

import com.yildiz.teamsync.dto.TeamMemberAddRequestDTO;
import com.yildiz.teamsync.dto.UserSearchResponseDTO;

public interface ITeamMemberService {
	public void addMemberToTeam(TeamMemberAddRequestDTO requestdto);
	
	List<UserSearchResponseDTO> searchUsers(String query);

}
