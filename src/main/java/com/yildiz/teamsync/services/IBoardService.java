package com.yildiz.teamsync.services;

import com.yildiz.teamsync.dto.BoardCreateRequestDTO;
import com.yildiz.teamsync.dto.BoardCreateResponseDTO;
import com.yildiz.teamsync.dto.BoardUpdateRequestDTO;
import com.yildiz.teamsync.dto.BoardUpdateResponseDTO;

public interface IBoardService {
	
	public BoardCreateResponseDTO createBoard(BoardCreateRequestDTO createdto);
	
	public BoardUpdateResponseDTO updateBoard(BoardUpdateRequestDTO updateddto);

}
