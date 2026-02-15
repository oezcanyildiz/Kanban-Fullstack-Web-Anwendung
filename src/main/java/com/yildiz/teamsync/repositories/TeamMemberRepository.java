package com.yildiz.teamsync.repositories;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.yildiz.teamsync.entities.TeamMember;

@Repository
public interface TeamMemberRepository extends JpaRepository<TeamMember , Long> {
	
	boolean existsByTeam_TeamIDAndUser_UserID(Long teamID, Long userID);
	
	List<TeamMember> findByTeam_TeamID(Long teamID);


}
