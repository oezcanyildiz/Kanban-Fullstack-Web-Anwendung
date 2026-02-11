package com.yildiz.teamsync.services.impl;



import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.yildiz.teamsync.dto.BoardCreateRequestDTO;
import com.yildiz.teamsync.dto.BoardCreateResponseDTO;
import com.yildiz.teamsync.dto.BoardUpdateRequestDTO;
import com.yildiz.teamsync.dto.BoardUpdateResponseDTO;
import com.yildiz.teamsync.entities.Board;
import com.yildiz.teamsync.entities.BoardColumn;
import com.yildiz.teamsync.entities.Team;
import com.yildiz.teamsync.entities.User;
import com.yildiz.teamsync.enums.UserRole;
import com.yildiz.teamsync.mappers.BoardMapper;
import com.yildiz.teamsync.repositories.BoardColumnRepository;
import com.yildiz.teamsync.repositories.BoardRepository;
import com.yildiz.teamsync.repositories.TeamRepository;
import com.yildiz.teamsync.repositories.UserRepository;
import com.yildiz.teamsync.services.IBoardService;

public class BoardService implements IBoardService{
	
	private final TeamRepository teamRepository;
	private final BoardRepository boardRepository;
	private final BoardMapper boardMapper;
	private final UserRepository userRepository;
	private final BoardColumnRepository boardColumnRepository;
	
	public BoardService(TeamRepository teamRepository, BoardMapper boardMapper, BoardRepository boardRepository, UserRepository userRepository, BoardColumnRepository boardColumnRepository) {
		this.teamRepository = teamRepository;
		this.boardMapper = boardMapper;
		this.boardRepository=boardRepository;
		this.userRepository =userRepository;
		this.boardColumnRepository=boardColumnRepository;
	}

    ///////////////////////////////
    ///							///
    ///     	ERSTELLUNG  	///
    ///							///
    ///////////////////////////////
	@Override
	@Transactional // Ganz wichtig: Speichert Board UND Spalten oder gar nichts!
	public BoardCreateResponseDTO createBoard(BoardCreateRequestDTO createdto) {
	    // 1. Team holen und Berechtigung prüfen (Dein Code - super!)
	    Team team = teamRepository.findById(createdto.getTeamID())
	            .orElseThrow(() -> new RuntimeException("Team nicht gefunden!"));

	    User currentUser = getAuthenticatedUser();

	    boolean isAdmin = currentUser.getRole() == UserRole.ORG_ADMIN;
	    boolean isOwner = team.getOwner().getUserID().equals(currentUser.getUserID());

	    if (!isAdmin && !isOwner) {
	        throw new RuntimeException("Keine Berechtigung, ein Board für dieses Team zu erstellen.");
	    }

	    // 2. Das Board erstellen und speichern
	    Board board = boardMapper.toEntity(createdto);
	    board.setTeam(team);
	    Board savedBoard = boardRepository.save(board);

	    // 3. AUTOMATIK: Die Standard-Spalten anlegen
	    List<String> defaultColumns = List.of("To Do", "In Progress", "Done");
	    
	    for (int i = 0; i < defaultColumns.size(); i++) {
	        BoardColumn column = new BoardColumn();
	        column.setColumnTitle(defaultColumns.get(i));
	        column.setColumnPosition(i); 
	        column.setBoard(savedBoard);
	        column.setWipLimit(10); 
	        boardColumnRepository.save(column);
	    }


	    return boardMapper.toCreateResponse(savedBoard);
	}
	
	
    ///////////////////////////////
    ///							///
    ///     	UPDATE  		///
    ///							///
    ///////////////////////////////

	@Override
    public BoardUpdateResponseDTO updateBoard(BoardUpdateRequestDTO updateddto) {
        // 1. Bestehendes Board laden
        Board board = boardRepository.findById(updateddto.getBoardID())
                .orElseThrow(() -> new RuntimeException("Board nicht gefunden!"));

        // 2. Berechtigung prüfen
        User currentUser = getAuthenticatedUser();
        boolean isAdmin = currentUser.getRole() == UserRole.ORG_ADMIN;
        boolean isOwner = board.getTeam().getOwner().getUserID().equals(currentUser.getUserID());

        if (!isAdmin && !isOwner) {
            throw new RuntimeException("Nur der Admin oder Team-Owner darf das Board bearbeiten.");
        }

        boardMapper.updateEntityFromDto(updateddto, board);
        
        return boardMapper.toUpdateResponse(boardRepository.save(board));
    }
    
    ///////////////////////////////
    ///							///
    ///     HILFSMETHODEN  		///
    ///							///
    ///////////////////////////////
    private User getAuthenticatedUser() {
        
        return userRepository.findById(1L).get(); 
    }

}
