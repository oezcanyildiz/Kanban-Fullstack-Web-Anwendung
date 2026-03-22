package com.yildiz.teamsync.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.yildiz.teamsync.dto.UserListResponseDTO;
import com.yildiz.teamsync.entities.User;
import com.yildiz.teamsync.enums.UserRole;
import com.yildiz.teamsync.exceptions.AccessDeniedException;
import com.yildiz.teamsync.exceptions.BadRequestException;
import com.yildiz.teamsync.exceptions.ResourceNotFoundException;
import com.yildiz.teamsync.exceptions.UnauthorizedException;
import com.yildiz.teamsync.repositories.UserRepository;
import com.yildiz.teamsync.repositories.TeamMemberRepository; // Added
import com.yildiz.teamsync.services.IOrganizationManagementService;

import com.yildiz.teamsync.config.SecurityUtils; // Restored original import path

@Service
public class OrganizationManagementService implements IOrganizationManagementService {
	
	private final UserRepository userRepository;
	private final SecurityUtils securityUtils;
	private final TeamMemberRepository teamMemberRepository; // Added

	public OrganizationManagementService(UserRepository userRepository, SecurityUtils securityUtils, TeamMemberRepository teamMemberRepository) { // Modified constructor
		this.userRepository=userRepository;
		this.securityUtils = securityUtils;
		this.teamMemberRepository = teamMemberRepository; // Initialized
	}

    ///////////////////////////////
    ///							///
    ///     Alle Employee  		///
    ///							///
    ///////////////////////////////
	@Override
	public List<UserListResponseDTO> getAllEmployees(Long organizationID) {
		
        User currentAdmin = getAuthenticatedUser();
        Long adminOrgID = currentAdmin.getOrganization().getOrganizationID();

        // Überprüft ob der angemeldete Admin und die gesuchte Org-ID zusammenpasst
        if (!adminOrgID.equals(organizationID)) {
            throw new AccessDeniedException("Unbefugter Zugriff auf diese Organisation!");
        }

	    List<User> employees = userRepository.findAllByOrganization_OrganizationID(organizationID); // Restored correct name
	    return employees.stream().map(emp -> { // Changed variable name
	        UserListResponseDTO dto = new UserListResponseDTO();
	        dto.setUserID(emp.getUserID());
	        dto.setUserName(emp.getUserName());
	        dto.setUserLastName(emp.getUserLastName());
	        dto.setUserEmail(emp.getUserEmail());
	        dto.setOnline(emp.isOnline());
	        dto.setRole(emp.getRole());
	        
	        if (emp.getOrganization() != null) {
	            dto.setOrganizationID(emp.getOrganization().getOrganizationID());
	        }
	        
	        // Hole die Teams des Nutzers
	        java.util.List<String> teamNames = teamMemberRepository.findByUser_UserID(emp.getUserID())
	            .stream()
	            .map(tm -> tm.getTeam().getTeamName())
	            .collect(java.util.stream.Collectors.toList());
	        dto.setTeams(teamNames);
	        
	        return dto;
	    }).collect(java.util.stream.Collectors.toList()); // Changed to collect
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
	            .orElseThrow(() -> new ResourceNotFoundException("User nicht gefunden."));
	
	    if (!user.getOrganization().getOrganizationID().equals(adminOrgID)) {
	        throw new UnauthorizedException("Unbefugter Zugriff!");
	    }
	    
	    user.setRole(UserRole.TEAM_LEADER);
	    userRepository.save(user);
	}

    ///////////////////////////////
    ///							///
    ///     Role Entfernen    	///
    ///							///
    ///////////////////////////////
	@Override
	public void demoteToUser(Long userID) {
		User currentAdmin = getAuthenticatedUser();
	    Long adminOrgID = currentAdmin.getOrganization().getOrganizationID();
	    
	    User user = userRepository.findById(userID)
	            .orElseThrow(() -> new RuntimeException("User nicht gefunden."));
	
	    if (!user.getOrganization().getOrganizationID().equals(adminOrgID)) {
	        throw new UnauthorizedException("Unbefugter Zugriff!");
	    }
	    
	    user.setRole(UserRole.USER);
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
	        throw new UnauthorizedException("Unbefugter Zugriff!");
	    }

	    if (user.getUserID().equals(currentAdmin.getUserID())) {
	        throw new BadRequestException("Ein Admin kann sich nicht selbst aus der Organisation löschen.");
	    }

	    userRepository.delete(user);
	}

	
	
    ///////////////////////////////
    ///							///
    ///     HILFSMETHODEN  		///
    ///							///
    ///////////////////////////////
    private User getAuthenticatedUser() {
        return securityUtils.getCurrentUserEntity();
    }
}
