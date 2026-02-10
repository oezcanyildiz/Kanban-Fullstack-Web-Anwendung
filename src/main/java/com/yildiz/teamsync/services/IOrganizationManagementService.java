package com.yildiz.teamsync.services;

import java.util.List;

import com.yildiz.teamsync.entities.User;
import com.yildiz.teamsync.enums.UserRole;


public interface IOrganizationManagementService {
	
	// Gibt eine Liste aller User einer Organisation zurück
    List<User> getAllEmployees(Long organizationID);
    
    // Ändert die Rolle eines spezifischen Users
    User changeUserRole(Long userID, UserRole newRole, Long adminOrgID);
	
	// Weist einem Team einen neuen Besitzer zu
    void assignTeamOwner(Long teamID, Long userID);
    
    // User aus der Organisation entfernen (optional, aber nützlich)
    void removeUserFromOrganization(Long userID);

}
