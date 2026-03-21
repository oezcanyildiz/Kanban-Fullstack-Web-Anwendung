package com.yildiz.teamsync.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.yildiz.teamsync.entities.Team;
import com.yildiz.teamsync.entities.User;
import java.util.List;

@Repository

public interface TeamRepository extends JpaRepository<Team, Long> {

    List<Team> findByOwnerAndDeletedFalse(User owner);

}
