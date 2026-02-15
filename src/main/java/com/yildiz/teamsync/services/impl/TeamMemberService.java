package com.yildiz.teamsync.services.impl;

import com.yildiz.teamsync.dto.TeamMemberAddRequestDTO;
import com.yildiz.teamsync.dto.UserSearchResponseDTO;
import com.yildiz.teamsync.entities.Team;
import com.yildiz.teamsync.entities.TeamMember;
import com.yildiz.teamsync.entities.User;
import com.yildiz.teamsync.enums.UserRole;
import com.yildiz.teamsync.repositories.TeamMemberRepository;
import com.yildiz.teamsync.repositories.TeamRepository;
import com.yildiz.teamsync.repositories.UserRepository;
import com.yildiz.teamsync.services.ITeamMemberService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TeamMemberService implements ITeamMemberService {

    private final TeamRepository teamRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public void addMemberToTeam(TeamMemberAddRequestDTO requestdto) {
        // 1. Team und den aktuell eingeloggten User (Leader/Admin) holen
        Team team = teamRepository.findById(requestdto.getTeamID())
                .orElseThrow(() -> new RuntimeException("Team nicht gefunden!"));
        
        User currentUser = getAuthenticatedUser(); // Die Hilfsmethode von gestern

        // 2. BERECHTIGUNGSPRÜFUNG
        boolean isAdmin = currentUser.getRole() == UserRole.ORG_ADMIN;
        boolean isTeamLeader = team.getOwner().getUserID().equals(currentUser.getUserID());

        if (!isAdmin && !isTeamLeader) {
            throw new RuntimeException("Nur der Teamleader oder ein Admin kann Mitglieder hinzufügen!");
        }

        // 3. PRÜFUNG: Ist der neue Mitarbeiter schon im Team?
        boolean alreadyMember = teamMemberRepository.existsByTeam_TeamIDAndUser_UserID(
                requestdto.getTeamID(), requestdto.getUserID());
        
        if (alreadyMember) {
            throw new RuntimeException("Dieser User ist bereits Mitglied im Team.");
        }

        // 4. Den neuen User laden
        User newUser = userRepository.findById(requestdto.getUserID())
                .orElseThrow(() -> new RuntimeException("Zuzuweisender User wurde nicht gefunden!"));

        // 5. Verknüpfung erstellen und speichern
        TeamMember newMembership = new TeamMember();
        newMembership.setTeam(team);
        newMembership.setUser(newUser);

        teamMemberRepository.save(newMembership);
    }

    private User getAuthenticatedUser() {
        // Hier später die echte Security-Logik. Für jetzt nehmen wir ID 1 (Admin/Leader)
        return userRepository.findById(1L).orElseThrow();
    }

	@Override
	public List<UserSearchResponseDTO> searchUsers(String query) {
		if (query == null || query.length() < 3) {
	        return List.of(); // Suche erst ab 3 Zeichen starten (Performance!)
	    }
	    
	    return userRepository.findByUsernameContainingIgnoreCase(query)
	            .stream()
	            .map(user -> new UserSearchResponseDTO(user.getUserID(), user.getUserName(), user.getUserEmail()))
	            .collect(Collectors.toList());
	}
}