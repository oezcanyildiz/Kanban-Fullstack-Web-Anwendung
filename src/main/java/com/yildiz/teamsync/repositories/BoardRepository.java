package com.yildiz.teamsync.repositories;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.yildiz.teamsync.entities.Board;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    
    // Wir übergeben die ID der Organisation des Users
    List<Board> findAllByTeam_Organization_OrganizationID(Long organizationID);

}