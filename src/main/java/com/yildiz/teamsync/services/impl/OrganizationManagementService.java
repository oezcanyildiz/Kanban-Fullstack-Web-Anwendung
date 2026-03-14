package com.yildiz.teamsync.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.yildiz.teamsync.dto.UserListResponseDTO;
import com.yildiz.teamsync.entities.User;
import com.yildiz.teamsync.enums.UserRole;

import com.yildiz.teamsync.repositories.UserRepository;
import com.yildiz.teamsync.services.IOrganizationManagementService;

@Service
public class OrganizationManagementService implements IOrganizationManagementService {
	
	private final UserRepository userRepository;

	public OrganizationManagementService(UserRepository userRepository) {
		this.userRepository=userRepository;
	}

    ///////////////////////////////
    ///							///
    ///     Alle Employee  		///
    ///							///
    ///////////////////////////////
	@Override
	public List<UserListResponseDTO> getAllEmployees(Long organizationID) {
	    // 1. Entity-Liste aus DB holen
	    List<User> users = userRepository.findAllByOrganization_OrganizationID(organizationID);
	    
	    if (users.isEmpty()) {
	        throw new RuntimeException("Keine Mitarbeiter für diese Organisation gefunden.");
	    }
	    
	    return users.stream().map(user -> {
	        UserListResponseDTO dto = new UserListResponseDTO();
	        dto.setUserID(user.getUserID());
	        dto.setUserName(user.getUserName());
	        dto.setUserLastName(user.getUserLastName());
	        dto.setUserEmail(user.getUserEmail());
	        dto.setOnline(user.isOnline());
	        dto.setRole(user.getRole());
	        if (user.getOrganization() != null) {
	            dto.setOrganizationID(user.getOrganization().getOrganizationID());
	        }
	        return dto;
	    }).toList();
	}

    ///////////////////////////////
    ///							///
    ///     Role Verteilung  	///
    ///							///
    ///////////////////////////////
	@Override
	public void promoteToTeamLeader(Long userID) {
		User currentAdmin = getAuthenticatedUser();
	    Long adminOrgID = currentAdmin.getOrganization().getOrganizationID();
	    
	    User user = userRepository.findById(userID)
	            .orElseThrow(() -> new RuntimeException("User nicht gefunden."));
	
	    if (!user.getOrganization().getOrganizationID().equals(adminOrgID)) {
	        throw new RuntimeException("Unbefugter Zugriff!");
	    }
	    
	    user.setRole(UserRole.TEAM_LEADER);
	    userRepository.save(user);
	}


    ///////////////////////////////
    ///							///
    ///     Employee Löschen  	///
    ///							///
    ///////////////////////////////
	@Override
	public void removeUserFromOrganization(Long userID) {
		User currentAdmin = getAuthenticatedUser();
	    Long adminOrgID = currentAdmin.getOrganization().getOrganizationID();
	    
	    User user = userRepository.findById(userID)
	            .orElseThrow(() -> new RuntimeException("User nicht gefunden."));

	    if (!user.getOrganization().getOrganizationID().equals(adminOrgID)) {
	        throw new RuntimeException("Unbefugter Zugriff!");
	    }

	    if (user.getUserID().equals(currentAdmin.getUserID())) {
	        throw new RuntimeException("Ein Admin kann sich nicht selbst aus der Organisation löschen.");
	    }

	    userRepository.delete(user);
	}

	
	
    ///////////////////////////////
    ///							///
    ///     HILFSMETHODEN  		///
    ///							///
    ///////////////////////////////
    private User getAuthenticatedUser() {
        
        return userRepository.findById(1L).get(); 
    }
}
