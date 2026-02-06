package com.yildiz.teamsync.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.yildiz.teamsync.entities.TeamMember;

@Repository
public interface TeamMemberRepository extends JpaRepository<TeamMember , Long> {

}
