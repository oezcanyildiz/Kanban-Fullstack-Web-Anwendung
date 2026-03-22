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
import com.yildiz.teamsync.exceptions.AccessDeniedException;
import com.yildiz.teamsync.exceptions.ResourceNotFoundException;
import com.yildiz.teamsync.repositories.BoardColumnRepository;
import com.yildiz.teamsync.repositories.BoardRepository;
import com.yildiz.teamsync.repositories.TeamRepository;
import com.yildiz.teamsync.services.IBoardService;
import com.yildiz.teamsync.repositories.BoardTaskRepository;
import com.yildiz.teamsync.repositories.TeamMemberRepository;
import com.yildiz.teamsync.dto.BoardDetailsResponseDTO;
import com.yildiz.teamsync.config.SecurityUtils;
import com.yildiz.teamsync.dto.BoardColumnDTO;
import com.yildiz.teamsync.dto.TaskDTO;
import com.yildiz.teamsync.entities.BoardTask;
import com.yildiz.teamsync.entities.TeamMember;
import com.yildiz.teamsync.dto.TeamMemberSimpleDTO;

@Service
public class BoardService implements IBoardService {
	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(BoardService.class);


	private final TeamRepository teamRepository;
	private final BoardRepository boardRepository;
	private final BoardColumnRepository boardColumnRepository;
	private final BoardTaskRepository boardTaskRepository;
	private final TeamMemberRepository teamMemberRepository;
	private final SecurityUtils securityUtils;

	public BoardService(TeamRepository teamRepository, BoardRepository boardRepository,
			BoardColumnRepository boardColumnRepository, BoardTaskRepository boardTaskRepository,
			TeamMemberRepository teamMemberRepository, SecurityUtils securityUtils) {
		this.teamRepository = teamRepository;
		this.boardRepository = boardRepository;
		this.boardColumnRepository = boardColumnRepository;
		this.boardTaskRepository = boardTaskRepository;
		this.teamMemberRepository = teamMemberRepository;
		this.securityUtils = securityUtils;
	}

	///////////////////////////////
	/// ///
	/// ERSTELLUNG ///
	/// ///
	///////////////////////////////
	@Override
	@Transactional // Ganz wichtig: Speichert Board UND Spalten oder gar nichts!
	public BoardCreateResponseDTO createBoard(BoardCreateRequestDTO createdto) {
		// 1. Team holen und Berechtigung prüfen (Dein Code - super!)
		Team team = teamRepository.findById(createdto.getTeamID())
				.orElseThrow(() -> new ResourceNotFoundException("Team nicht gefunden!"));

		User currentUser = securityUtils.getCurrentUserEntity();

		boolean isAdmin = currentUser.getRole() == UserRole.ORG_ADMIN;
		boolean isOwner = team.getOwner().getUserID().equals(currentUser.getUserID());

		if (!isAdmin && !isOwner) {
			throw new AccessDeniedException("Keine Berechtigung, ein Board für dieses Team zu erstellen.");
		}

		Board board = new Board();
		board.setBoardName(createdto.getBoardName());
		board.setTeam(team);
		board.setDeleted(false);
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
		log.info("Board created: {} with default columns for Team ID: {}", savedBoard.getBoardID(), team.getTeamID());
		BoardCreateResponseDTO responseDTO = new BoardCreateResponseDTO();
		responseDTO.setBoardID(savedBoard.getBoardID());
		responseDTO.setBoardName(savedBoard.getBoardName());
		return responseDTO;
	}

	///////////////////////////////
	/// ///
	/// UPDATE ///
	/// ///
	///////////////////////////////

	@Override
	public BoardUpdateResponseDTO updateBoard(BoardUpdateRequestDTO updateddto) {
		// 1. Bestehendes Board laden
		Board board = boardRepository.findById(updateddto.getBoardID())
				.orElseThrow(() -> new ResourceNotFoundException("Board nicht gefunden!"));

		// 2. Berechtigung prüfen
		User currentUser = securityUtils.getCurrentUserEntity();
		boolean isAdmin = currentUser.getRole() == UserRole.ORG_ADMIN;
		boolean isOwner = board.getTeam().getOwner().getUserID().equals(currentUser.getUserID());

		if (!isAdmin && !isOwner) {
			throw new AccessDeniedException("Nur der Admin oder Team-Owner darf das Board bearbeiten.");
		}

		board.setBoardName(updateddto.getNewBoardName());
		Board savedBoard = boardRepository.save(board);

		BoardUpdateResponseDTO responseDTO = new BoardUpdateResponseDTO();
		responseDTO.setBoardID(savedBoard.getBoardID());
		responseDTO.setBoardName(savedBoard.getBoardName());
		return responseDTO;
	}

	///////////////////////////////
	/// ///
	/// LESEN ///
	/// ///
	///////////////////////////////
	@Override
	@Transactional(readOnly = true)
	public BoardDetailsResponseDTO getBoardDetails(Long boardID) {
		Board board = boardRepository.findById(boardID)
				.filter(b -> !b.isDeleted())
				.filter(b -> !b.getTeam().isDeleted())
				.orElseThrow(() -> new ResourceNotFoundException("Board nicht gefunden!"));

		User currentUser = securityUtils.getCurrentUserEntity();

		// 1. Die Berechtigungen berechnen
		boolean isAdmin = currentUser.getRole() == UserRole.ORG_ADMIN;
		boolean isTeamOwner = board.getTeam().getOwner().getUserID().equals(currentUser.getUserID());
        boolean isTeamMember = teamMemberRepository.existsByTeam_TeamIDAndUser_UserID(board.getTeam().getTeamID(), currentUser.getUserID());

		// 2. Der optimierte Check (Sicherheitsgrenze)
		if (isAdmin || isTeamOwner || isTeamMember) {
			BoardDetailsResponseDTO responseDTO = new BoardDetailsResponseDTO();
			responseDTO.setBoardID(board.getBoardID());
			responseDTO.setBoardName(board.getBoardName());

			// 3. Spalten laden und umwandeln
			List<BoardColumn> columns = boardColumnRepository
					.findByBoard_BoardIDOrderByColumnPositionAsc(board.getBoardID());
			List<BoardColumnDTO> columnDTOs = columns.stream().map(col -> {
				BoardColumnDTO colDTO = new BoardColumnDTO();
				colDTO.setBoardColumnID(col.getBoardColumnID());
				colDTO.setColumnTitle(col.getColumnTitle());
				colDTO.setColumnPosition(col.getColumnPosition());
				colDTO.setWipLimit(col.getWipLimit());

				// 4. Tasks für diese Spalte laden (nicht-gelöschte)
				List<BoardTask> tasks = boardTaskRepository
						.findByBoardColumn_BoardColumnIDAndDeletedFalseOrderByPositionAsc(col.getBoardColumnID());
				List<TaskDTO> taskDTOs = tasks.stream()
						.map(task -> {
							TaskDTO taskDTO = new TaskDTO();
							taskDTO.setTaskID(task.getBoardTaskID());
							taskDTO.setTitle(task.getBoardTaskTitle());
							taskDTO.setDescription(task.getBoardTaskDescription());
							taskDTO.setPriority(task.getPriority());
							taskDTO.setPosition(task.getPosition());
							if (task.getAssignee() != null) {
								taskDTO.setAssigneeID(task.getAssignee().getUserID());
								taskDTO.setAssigneeName(task.getAssignee().getUserName());
							}
							return taskDTO;
						}).toList();

				colDTO.setTasks(taskDTOs);
				return colDTO;
			}).toList();

			responseDTO.setColumns(columnDTOs);
			log.info("Returning {} columns for Board ID: {}", columnDTOs.size(), board.getBoardID());


			// 5. Teammitglieder laden (für Assignee-Dropdown relevant)
			List<TeamMember> members = teamMemberRepository.findByTeam_TeamID(board.getTeam().getTeamID());
			List<TeamMemberSimpleDTO> memberDTOs = members.stream().map(m -> {
				return new TeamMemberSimpleDTO(
						m.getUser().getUserID(),
						m.getUser().getUserName(),
						m.getUser().getUserLastName());
			}).toList();
			responseDTO.setTeamMembers(memberDTOs);

			return responseDTO;
		} else {
			throw new RuntimeException("Zugriff verweigert: Sie haben keine Berechtigung für dieses Board.");
		}
	}

	///////////////////////////////
	/// ///
	/// Listen von Baords ///
	/// ///
	///////////////////////////////

	@Override
	@Transactional(readOnly = true)
	public List<BoardListResponseDTO> getMyBoards() {
		User currentUser = securityUtils.getCurrentUserEntity();

		// 1. Nur die Teams laden, in denen der Nutzer als Mitglied registriert ist
		List<TeamMember> userMemberships = teamMemberRepository.findByUser_UserID(currentUser.getUserID());
		List<Long> teamIDs = userMemberships.stream()
				.filter(tm -> !tm.getTeam().isDeleted())
				.map(tm -> tm.getTeam().getTeamID())
				.toList();

		List<Board> boards;
		if (teamIDs.isEmpty()) {
			boards = new java.util.ArrayList<>();
		} else {
			boards = boardRepository.findAll().stream()
					.filter(b -> teamIDs.contains(b.getTeam().getTeamID()))
					.filter(b -> !b.isDeleted())
					.toList();
		}

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
	/// ///
	/// DELETE ///
	/// ///
	///////////////////////////////
	@Override
	@Transactional
	public void deleteBoard(Long boardID) {
		Board board = boardRepository.findById(boardID)
				.orElseThrow(() -> new RuntimeException("Board nicht gefunden!"));

		User currentUser = securityUtils.getCurrentUserEntity();
		boolean isAdmin = currentUser.getRole() == UserRole.ORG_ADMIN;
		boolean isOwner = board.getTeam().getOwner().getUserID().equals(currentUser.getUserID());

		if (!isAdmin && !isOwner) {
			throw new AccessDeniedException("Nur der Admin oder der Teamleiter darf dieses Board löschen.");
		}

		board.setDeleted(true);
		boardRepository.save(board);
	}

}
