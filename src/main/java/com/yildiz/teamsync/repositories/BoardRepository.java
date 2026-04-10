package com.yildiz.teamsync.repositories;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.yildiz.teamsync.entities.Board;
import com.yildiz.teamsync.entities.BoardTask;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    
    // Wir übergeben die ID der Organisation des Users
    List<Board> findAllByTeam_Organization_OrganizationID(Long organizationID);

    List<Board> findByTeam_TeamIDAndDeletedFalse(Long teamID);
    
    List<Board> findByTeam_TeamIDInAndDeletedFalse(List<Long> teamIDs);

    List<BoardTask> findByBoardColumn_Board_BoardIDAndDeletedFalseOrderByPositionAsc(Long boardID);

}