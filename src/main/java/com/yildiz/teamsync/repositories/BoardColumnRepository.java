package com.yildiz.teamsync.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.yildiz.teamsync.entities.BoardColumn;

@Repository

public interface BoardColumnRepository extends JpaRepository<BoardColumn , Long> {
	long countByBoard_BoardID(Long boardID);

}
