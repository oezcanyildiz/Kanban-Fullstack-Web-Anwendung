package com.yildiz.teamsync.services.impl;



import com.yildiz.teamsync.dto.BoardCreateRequestDTO;
import com.yildiz.teamsync.dto.BoardCreateResponseDTO;
import com.yildiz.teamsync.dto.BoardUpdateRequestDTO;
import com.yildiz.teamsync.dto.BoardUpdateResponseDTO;
import com.yildiz.teamsync.entities.Board;
import com.yildiz.teamsync.entities.Team;
import com.yildiz.teamsync.entities.User;
import com.yildiz.teamsync.enums.UserRole;
import com.yildiz.teamsync.mappers.BoardMapper;
import com.yildiz.teamsync.repositories.BoardRepository;
import com.yildiz.teamsync.repositories.TeamRepository;
import com.yildiz.teamsync.repositories.UserRepository;
import com.yildiz.teamsync.services.IBoardService;

public class BoardService implements IBoardService{
	
	private final TeamRepository teamRepository;
	private final BoardRepository boardRepository;
	private final BoardMapper boardMapper;
	private final UserRepository userRepository;
	
	public BoardService(TeamRepository teamRepository, BoardMapper boardMapper, BoardRepository boardRepository, UserRepository userRepository) {
		this.teamRepository = teamRepository;
		this.boardMapper = boardMapper;
		this.boardRepository=boardRepository;
		this.userRepository =userRepository;
	}

    ///////////////////////////////
    ///							///
    ///     	ERSTELLUNG  	///
    ///							///
    ///////////////////////////////
	@Override
	public BoardCreateResponseDTO createBoard(BoardCreateRequestDTO createdto) {
		Team team = teamRepository.findById(createdto.getTeamID())
                .orElseThrow(() -> new RuntimeException("Team nicht gefunden!"));

		 User currentUser = getAuthenticatedUser();

        boolean isAdmin = currentUser.getRole() == UserRole.ORG_ADMIN;
        boolean isOwner = team.getOwner().getUserID().equals(currentUser.getUserID());

        if (!isAdmin && !isOwner) {
            throw new RuntimeException("Keine Berechtigung für dieses Team.");
        }

        Board board = boardMapper.toEntity(createdto);
        board.setTeam(team);
        return boardMapper.toCreateResponse(boardRepository.save(board));
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
