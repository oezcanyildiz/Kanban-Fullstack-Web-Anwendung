package com.yildiz.teamsync.services.impl;



import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yildiz.teamsync.dto.BoardCreateRequestDTO;
import com.yildiz.teamsync.dto.BoardCreateResponseDTO;
import com.yildiz.teamsync.dto.BoardListResponseDTO;
import com.yildiz.teamsync.dto.BoardUpdateRequestDTO;
import com.yildiz.teamsync.dto.BoardUpdateResponseDTO;
import com.yildiz.teamsync.entities.Board;
import com.yildiz.teamsync.entities.BoardColumn;
import com.yildiz.teamsync.entities.Team;
import com.yildiz.teamsync.entities.User;
import com.yildiz.teamsync.enums.UserRole;

import com.yildiz.teamsync.repositories.BoardColumnRepository;
import com.yildiz.teamsync.repositories.BoardRepository;
import com.yildiz.teamsync.repositories.TeamRepository;
import com.yildiz.teamsync.repositories.UserRepository;
import com.yildiz.teamsync.services.IBoardService;
@Service
public class BoardService implements IBoardService{
	
	private final TeamRepository teamRepository;
	private final BoardRepository boardRepository;
	private final UserRepository userRepository;
	private final BoardColumnRepository boardColumnRepository;
	
	public BoardService(TeamRepository teamRepository, BoardRepository boardRepository, UserRepository userRepository, BoardColumnRepository boardColumnRepository) {
		this.teamRepository = teamRepository;
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

	    Board board = new Board();
	    board.setBoardName(createdto.getBoardName());
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
	    BoardCreateResponseDTO responseDTO = new BoardCreateResponseDTO();
	    responseDTO.setBoardID(savedBoard.getBoardID());
	    responseDTO.setBoardName(savedBoard.getBoardName());
	    return responseDTO;
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

        board.setBoardName(updateddto.getNewBoardName());
        Board savedBoard = boardRepository.save(board);
        
        BoardUpdateResponseDTO responseDTO = new BoardUpdateResponseDTO();
        responseDTO.setBoardID(savedBoard.getBoardID());
        responseDTO.setBoardName(savedBoard.getBoardName());
        return responseDTO;
    }
    
	///////////////////////////////
	///                         ///
	///          LESEN          ///
	///                         ///
	///////////////////////////////
	@Override
	@Transactional(readOnly = true)
	public BoardCreateResponseDTO getBoardDetails(Long boardID) {
	    Board board = boardRepository.findById(boardID)
	            .orElseThrow(() -> new RuntimeException("Board nicht gefunden!"));

	    User currentUser = getAuthenticatedUser();

	    // PRÜFUNG: Gehört der User zur selben Organisation wie das Team des Boards?
	    // Wir vergleichen die IDs der Organisationen
	    boolean sameOrganization = currentUser.getOrganization().getOrganizationID()
	            .equals(board.getTeam().getOrganization().getOrganizationID());
	    
	 // 1. Die Berechtigungen berechnen
	    boolean isAdmin = currentUser.getRole() == UserRole.ORG_ADMIN;
	    boolean isTeamOwner = board.getTeam().getOwner().getUserID().equals(currentUser.getUserID());

	    // 2. Der optimierte Check
	    // Wenn er Admin oder Owner ist, ist die Organisation egal (da er sowieso Rechte hat).
	    // Wenn er keines von beiden ist, MUSS er zumindest in der selben Organisation sein.
	    if (isAdmin || isTeamOwner || sameOrganization) {
	        BoardCreateResponseDTO responseDTO = new BoardCreateResponseDTO();
	        responseDTO.setBoardID(board.getBoardID());
	        responseDTO.setBoardName(board.getBoardName());
	        return responseDTO;
	    } else {
	        throw new RuntimeException("Zugriff verweigert: Sie haben keine Berechtigung für dieses Board.");
	    }
	}
	
	///////////////////////////////
	///                         ///
	///   Listen von Baords     ///
	///                         ///
	///////////////////////////////
	
	@Override
	@Transactional(readOnly = true)
	public List<BoardListResponseDTO> getMyBoards() {
	    User currentUser = getAuthenticatedUser();
	    
	    // Wir holen alle Boards der Organisation des Users
	    List<Board> boards = boardRepository.findAllByTeam_Organization_OrganizationID(
	        currentUser.getOrganization().getOrganizationID()
	    );
	    
	    return boards.stream().map(board -> {
	        BoardListResponseDTO dto = new BoardListResponseDTO();
	        dto.setBoardID(board.getBoardID());
	        dto.setBoardName(board.getBoardName());
	        if (board.getTeam() != null) {
	            dto.setTeamName(board.getTeam().getTeamName());
	        }
	        return dto;
	    }).toList();
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
