package com.yildiz.teamsync.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.yildiz.teamsync.entities.Board;

@Repository
public interface BoardRepository extends JpaRepository<Board,Long>{

}
