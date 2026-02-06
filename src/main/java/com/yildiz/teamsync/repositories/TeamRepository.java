package com.yildiz.teamsync.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.yildiz.teamsync.entities.Team;

@Repository

public interface TeamRepository extends JpaRepository<Team, Long> {

}
