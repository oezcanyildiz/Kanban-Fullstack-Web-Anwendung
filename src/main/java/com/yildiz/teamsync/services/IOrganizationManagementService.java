package com.yildiz.teamsync.services;

import java.util.List;

import com.yildiz.teamsync.dto.UserListResponseDTO;

public interface IOrganizationManagementService {
	
	 List<UserListResponseDTO> getAllEmployees(Long organizationID);
    
    void promoteToTeamLeader(Long userID);
    
    void removeUserFromOrganization(Long userID);

}
