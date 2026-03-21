package com.yildiz.teamsync.services;

import java.util.List;

import com.yildiz.teamsync.dto.BoardCreateRequestDTO;
import com.yildiz.teamsync.dto.BoardCreateResponseDTO;
import com.yildiz.teamsync.dto.BoardListResponseDTO;
import com.yildiz.teamsync.dto.BoardUpdateRequestDTO;
import com.yildiz.teamsync.dto.BoardUpdateResponseDTO;


import com.yildiz.teamsync.dto.BoardDetailsResponseDTO;

public interface IBoardService {
	
	public BoardCreateResponseDTO createBoard(BoardCreateRequestDTO createdto);
	
	public BoardUpdateResponseDTO updateBoard(BoardUpdateRequestDTO updateddto);
	
	public BoardDetailsResponseDTO getBoardDetails(Long boardID);
	
	public List<BoardListResponseDTO> getMyBoards();

	public void deleteBoard(Long boardID);

}
