package com.yildiz.teamsync.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.yildiz.teamsync.entities.Team;
import com.yildiz.teamsync.entities.User;
import java.util.List;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {

    List<Team> findByOwnerAndDeletedFalse(User owner);

    // Teams finden, wo der User Owner ist ODER Member
    @Query("SELECT DISTINCT t FROM Team t " +
           "LEFT JOIN t.members tm " +
           "WHERE (t.owner.userID = :userId OR tm.user.userID = :userId) " +
           "AND (t.deleted IS NULL OR t.deleted = false) " +
           "ORDER BY t.teamName ASC")
    List<Team> findTeamsByUserAsOwnerOrMember(@Param("userId") Long userId);
}
