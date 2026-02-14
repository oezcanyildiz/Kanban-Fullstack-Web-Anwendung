package com.yildiz.teamsync.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.yildiz.teamsync.entities.BoardTask;
@Repository
public interface BoardTaskRepository extends JpaRepository<BoardTask, Long>{

	long countByBoardColumn_BoardColumnID(Long columnID);
}
