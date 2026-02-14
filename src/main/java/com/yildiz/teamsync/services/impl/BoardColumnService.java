package com.yildiz.teamsync.services.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yildiz.teamsync.dto.BoardColumnCreateRequestDTO;
import com.yildiz.teamsync.dto.BoardColumnCreateResponseDTO;
import com.yildiz.teamsync.dto.BoardColumnUpdateRequestDTO;
import com.yildiz.teamsync.dto.BoardColumnUpdateResponseDTO;
import com.yildiz.teamsync.entities.Board;
import com.yildiz.teamsync.entities.BoardColumn;
import com.yildiz.teamsync.entities.User;
import com.yildiz.teamsync.enums.UserRole;
import com.yildiz.teamsync.mappers.BoardColumnMapper;
import com.yildiz.teamsync.repositories.BoardColumnRepository;
import com.yildiz.teamsync.repositories.BoardRepository;
import com.yildiz.teamsync.repositories.UserRepository;
import com.yildiz.teamsync.services.IBoardColumnService;

@Service
public class BoardColumnService implements IBoardColumnService{
	
	private final BoardColumnRepository boardColumnRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final BoardColumnMapper boardColumnMapper;

    public BoardColumnService(BoardColumnRepository boardColumnRepository, BoardRepository boardRepository, 
                              UserRepository userRepository, BoardColumnMapper boardColumnMapper) {
        this.boardColumnRepository = boardColumnRepository;
        this.boardRepository = boardRepository;
        this.userRepository = userRepository;
        this.boardColumnMapper = boardColumnMapper;
    }

	@Override
	@Transactional
	public BoardColumnCreateResponseDTO createColumn(BoardColumnCreateRequestDTO createdto) {
		Board board = boardRepository.findById(createdto.getBoardID())
				.orElseThrow(()-> new RuntimeException("Board mit der ID "+ createdto.getBoardID()+" nicht gefunden!"));
		User currentUser=getAuthenticatedUser();
		checkPermission(board,currentUser);
		
		long currentColumns = boardColumnRepository.countByBoard_BoardID(board.getBoardID());
        if (currentColumns >= 8) {
            throw new RuntimeException("Maximale Anzahl von 8 Spalten erreicht!");
        }
        BoardColumn column = boardColumnMapper.toEntity(createdto);
        
        column.setBoard(board);
        column.setColumnPosition((int) currentColumns);
        
        BoardColumn savedColumn = boardColumnRepository.save(column);
        
        return boardColumnMapper.toResponse(savedColumn);
	}

	@Override
	public BoardColumnUpdateResponseDTO updateColumn(BoardColumnUpdateRequestDTO updatedto) {
		BoardColumn column = boardColumnRepository.findById(updatedto.getBoardColumnID())
				.orElseThrow(()-> new RuntimeException("Column mit der ID "+ updatedto.getBoardColumnID()+" nicht gefunden!"));
		
		User currentUser = getAuthenticatedUser();
	    checkPermission(column.getBoard(), currentUser);
	    
	    boardColumnMapper.updateEntityFromDto(updatedto, column);
	    BoardColumn updatedColumn=boardColumnRepository.save(column);
		return boardColumnMapper.toUpdateResponse(updatedColumn);
	}

	@Override
	@Transactional
	public void deleteColumn(Long columnID) {
		BoardColumn column = boardColumnRepository.findById(columnID)
	            .orElseThrow(() -> new RuntimeException("Spalte nicht gefunden!"));
	    
	    checkPermission(column.getBoard(), getAuthenticatedUser());
	    
	    boardColumnRepository.delete(column);	
	}

	
	
	private void checkPermission(Board board, User user) {
        boolean isAdmin = user.getRole() == UserRole.ORG_ADMIN;
        boolean isOwner = board.getTeam().getOwner().getUserID().equals(user.getUserID());
        
        if (!isAdmin && !isOwner) {
            throw new RuntimeException("Keine Berechtigung, Spalten zu verwalten.");
        }
    }

    private User getAuthenticatedUser() {
        return userRepository.findById(1L).get(); // Dummy für jetzt
    }
}
