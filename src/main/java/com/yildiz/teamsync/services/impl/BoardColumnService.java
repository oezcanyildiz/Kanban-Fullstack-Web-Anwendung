package com.yildiz.teamsync.services.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yildiz.teamsync.config.SecurityUtils;
import com.yildiz.teamsync.dto.BoardColumnCreateRequestDTO;
import com.yildiz.teamsync.dto.BoardColumnCreateResponseDTO;
import com.yildiz.teamsync.dto.BoardColumnUpdateRequestDTO;
import com.yildiz.teamsync.dto.BoardColumnUpdateResponseDTO;
import com.yildiz.teamsync.entities.Board;
import com.yildiz.teamsync.entities.BoardColumn;
import com.yildiz.teamsync.entities.User;
import com.yildiz.teamsync.enums.UserRole;

import com.yildiz.teamsync.repositories.BoardColumnRepository;
import com.yildiz.teamsync.repositories.BoardRepository;
import com.yildiz.teamsync.services.IBoardColumnService;

@Service
public class BoardColumnService implements IBoardColumnService {

	private final BoardColumnRepository boardColumnRepository;
	private final BoardRepository boardRepository;

	private final SecurityUtils securityUtils;

	public BoardColumnService(BoardColumnRepository boardColumnRepository, BoardRepository boardRepository,
			SecurityUtils securityUtils) {
		this.boardColumnRepository = boardColumnRepository;
		this.boardRepository = boardRepository;
		this.securityUtils = securityUtils;
	}

	@Override
	@Transactional
	public BoardColumnCreateResponseDTO createColumn(BoardColumnCreateRequestDTO createdto) {
		Board board = boardRepository.findById(createdto.getBoardID())
				.orElseThrow(
						() -> new RuntimeException("Board mit der ID " + createdto.getBoardID() + " nicht gefunden!"));
		User currentUser = securityUtils.getCurrentUserEntity();
		checkPermission(board, currentUser);

		long currentColumns = boardColumnRepository.countByBoard_BoardID(board.getBoardID());
		if (currentColumns >= 5) {
			throw new RuntimeException("Maximale Anzahl von 5 Spalten erreicht!");
		}
		BoardColumn column = new BoardColumn();
		column.setColumnTitle(createdto.getColumnTitle());
		column.setWipLimit(createdto.getWipLimit());

		column.setBoard(board);
		column.setColumnPosition((int) currentColumns);

		BoardColumn savedColumn = boardColumnRepository.save(column);

		BoardColumnCreateResponseDTO responseDTO = new BoardColumnCreateResponseDTO();
		responseDTO.setBoardColumnID(savedColumn.getBoardColumnID());
		responseDTO.setColumnTitle(savedColumn.getColumnTitle());
		responseDTO.setColumnPosition(savedColumn.getColumnPosition());
		responseDTO.setWipLimit(savedColumn.getWipLimit());
		if (savedColumn.getBoard() != null) {
			responseDTO.setBoardID(savedColumn.getBoard().getBoardID());
		}

		return responseDTO;
	}

	@Override
	public BoardColumnUpdateResponseDTO updateColumn(BoardColumnUpdateRequestDTO updatedto) {
		BoardColumn column = boardColumnRepository.findById(updatedto.getBoardColumnID())
				.orElseThrow(() -> new RuntimeException(
						"Column mit der ID " + updatedto.getBoardColumnID() + " nicht gefunden!"));

		User currentUser = securityUtils.getCurrentUserEntity();
		checkPermission(column.getBoard(), currentUser);

		column.setColumnTitle(updatedto.getColumnTitle());
		column.setColumnPosition(updatedto.getColumnPosition());
		column.setWipLimit(updatedto.getWipLimit());

		BoardColumn updatedColumn = boardColumnRepository.save(column);

		BoardColumnUpdateResponseDTO responseDTO = new BoardColumnUpdateResponseDTO();
		responseDTO.setBoardColumnID(updatedColumn.getBoardColumnID());
		responseDTO.setColumnTitle(updatedColumn.getColumnTitle());
		responseDTO.setColumnPosition(updatedColumn.getColumnPosition());
		responseDTO.setWipLimit(updatedColumn.getWipLimit());
		if (updatedColumn.getBoard() != null) {
			responseDTO.setBoardID(updatedColumn.getBoard().getBoardID());
		}

		return responseDTO;
	}

	@Override
	@Transactional
	public void deleteColumn(Long columnID) {
		BoardColumn column = boardColumnRepository.findById(columnID)
				.orElseThrow(() -> new RuntimeException("Spalte nicht gefunden!"));

		checkPermission(column.getBoard(), securityUtils.getCurrentUserEntity());

		String title = column.getColumnTitle().toLowerCase().trim();
		if (title.equals("to do") || title.equals("in progress") || title.equals("done") ||
			title.equals("todo") || title.equals("bereit") || title.equals("erledigt") || title.equals("in arbeit")) {
			throw new RuntimeException("Diese Standard-Spalte kann nicht gelöscht werden!");
		}

		boardColumnRepository.delete(column);
	}

	private void checkPermission(Board board, User user) {
		boolean isAdmin = user.getRole() == UserRole.ORG_ADMIN;
		boolean isOwner = board.getTeam().getOwner().getUserID().equals(user.getUserID());

		if (!isAdmin && !isOwner) {
			throw new RuntimeException("Keine Berechtigung, Spalten zu verwalten.");
		}
	}
}
