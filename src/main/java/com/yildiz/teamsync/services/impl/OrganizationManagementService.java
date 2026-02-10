package com.yildiz.teamsync.services.impl;

import java.util.List;

import com.yildiz.teamsync.entities.User;
import com.yildiz.teamsync.enums.UserRole;
import com.yildiz.teamsync.repositories.UserRepository;
import com.yildiz.teamsync.services.IOrganizationManagementService;

public class OrganizationManagementService implements IOrganizationManagementService {
	
	private final UserRepository userRepository;
	
	public OrganizationManagementService(UserRepository userRepository) {
		this.userRepository=userRepository;
	}

	@Override
	public List<User> getAllEmployees(Long organizationID) {
		List <User> users=userRepository.findAllByOrganization_OrganizationID(organizationID);
		if (users.isEmpty()) {
	        throw new RuntimeException("Keine Mitarbeiter für diese Organisation gefunden.");
	    }
	    return users;
	}

	@Override
	public User changeUserRole(Long userID, UserRole newRole, Long adminOrgID) {
		
		//AdminOrgID wird später von JWT gehollt. als CurrentAdmin
		if (!userRepository.existsByUserIDAndOrganization_OrganizationID(userID, adminOrgID)) {
	        throw new RuntimeException("Berechtigungsfehler: Dieser User gehört nicht zu deiner Organisation!");
	    }
		
		
		User userToChange = userRepository.findById(userID).get();
	    userToChange.setRole(newRole);
	    return userRepository.save(userToChange);
	}

	@Override
	public void assignTeamOwner(Long teamID, Long userID) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeUserFromOrganization(Long userID) {
		// TODO Auto-generated method stub
		
	}

}
