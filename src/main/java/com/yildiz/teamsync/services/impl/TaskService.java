package com.yildiz.teamsync.services.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yildiz.teamsync.config.SecurityUtils;
import com.yildiz.teamsync.dto.TaskCreateRequestDTO;
import com.yildiz.teamsync.dto.TaskCreateResponseDTO;
import com.yildiz.teamsync.dto.TaskUpdateRequestDTO;
import com.yildiz.teamsync.dto.TaskUpdateResponseDTO;
import com.yildiz.teamsync.entities.Board;
import com.yildiz.teamsync.entities.BoardColumn;
import com.yildiz.teamsync.entities.BoardTask;
import com.yildiz.teamsync.entities.User;
import com.yildiz.teamsync.enums.UserRole;

import com.yildiz.teamsync.repositories.BoardColumnRepository;
import com.yildiz.teamsync.repositories.BoardTaskRepository;
import com.yildiz.teamsync.repositories.TeamMemberRepository;
import com.yildiz.teamsync.repositories.UserRepository;
import com.yildiz.teamsync.services.ITaskService;

@Service
public class TaskService implements ITaskService {

	private final BoardColumnRepository boardColumnRepository;
	private final UserRepository userRepository;
	private final BoardTaskRepository taskRepository;
	private final TeamMemberRepository teamMemberRepository;
	private final SecurityUtils securityUtils;

	public TaskService(BoardColumnRepository boardColumnRepository,
			UserRepository userRepository, BoardTaskRepository taskRepository,
			TeamMemberRepository teamMemberRepository, SecurityUtils securityUtils) {
		this.boardColumnRepository = boardColumnRepository;

		this.userRepository = userRepository;
		this.taskRepository = taskRepository;
		this.teamMemberRepository = teamMemberRepository;
		this.securityUtils = securityUtils;
	}

	@Override
	@Transactional
	public TaskCreateResponseDTO createTask(TaskCreateRequestDTO requestdto) {
		BoardColumn column = boardColumnRepository.findById(requestdto.getColumnID())
				.orElseThrow(() -> new RuntimeException("Spalte nicht gefunden."));
		Board board = column.getBoard();

		User currentUser = securityUtils.getCurrentUserEntity();
		checkPermission(board, currentUser);

		long totalTasksOnBoard = taskRepository.countByBoardColumn_BoardColumnID(board.getBoardID());
		if (totalTasksOnBoard >= 50) {
			throw new RuntimeException("Das Board-Limit von 50 Tasks wurde erreicht!");
		}

		if (column.getColumnPosition() != 0) {
			throw new RuntimeException("Tasks können nur in der ersten Spalte erstellt werden!");
		}

		long tasksInColumn = taskRepository.countByBoardColumn_BoardColumnID(column.getBoardColumnID());
		if (column.getWipLimit() != null && tasksInColumn >= column.getWipLimit()) {
			throw new RuntimeException("WIP-Limit dieser Spalte erreicht!");
		}

		BoardTask task = new BoardTask();
		task.setBoardTaskTitle(requestdto.getTitle());
		task.setBoardTaskDescription(requestdto.getDescription());
		task.setPriority(requestdto.getPriority());

		task.setBoardColumn(column);
		task.setPosition((int) tasksInColumn);

		task.setCreator(currentUser);

		BoardTask savedTask = taskRepository.save(task);

		TaskCreateResponseDTO responseDTO = new TaskCreateResponseDTO();
		responseDTO.setTaskID(savedTask.getBoardTaskID());
		responseDTO.setTitle(savedTask.getBoardTaskTitle());
		responseDTO.setDescription(savedTask.getBoardTaskDescription());
		responseDTO.setPriority(savedTask.getPriority());
		if (savedTask.getBoardColumn() != null) {
			responseDTO.setColumnID(savedTask.getBoardColumn().getBoardColumnID());
		}
		return responseDTO;
	}

	@Override
	@Transactional
	public TaskUpdateResponseDTO updateTask(TaskUpdateRequestDTO requestdto) {
		// 1. Task laden
		BoardTask task = taskRepository.findById(requestdto.getTaskID())
				.orElseThrow(() -> new RuntimeException("Task nicht gefunden!"));

		// 2. Berechtigung prüfen (Darf dieses Team-Mitglied das?)
		checkTeamMemberPermission(task.getBoardColumn().getBoard(), securityUtils.getCurrentUserEntity());

		// 3. Spaltenwechsel & WIP-Limit prüfen
		// Wenn die columnID im DTO anders ist als die aktuelle columnID des Tasks:
		if (!task.getBoardColumn().getBoardColumnID().equals(requestdto.getColumnID())) {
			BoardColumn targetColumn = boardColumnRepository.findById(requestdto.getColumnID())
					.orElseThrow(() -> new RuntimeException("Ziel-Spalte nicht gefunden!"));

			// WIP-Limit der ZIEL-Spalte prüfen
			long tasksInTarget = taskRepository.countByBoardColumn_BoardColumnID(targetColumn.getBoardColumnID());
			if (targetColumn.getWipLimit() != null && tasksInTarget >= targetColumn.getWipLimit()) {
				throw new RuntimeException("Ziel-Spalte ist voll! WIP-Limit erreicht.");
			}
			task.setBoardColumn(targetColumn); // Spalte wechseln
		}

		// 4. Daten aktualisieren (Titel, Beschreibung, Prio, Position)
		task.setBoardTaskTitle(requestdto.getTitle());
		task.setBoardTaskDescription(requestdto.getDescription());
		task.setPriority(requestdto.getPriority());
		if (requestdto.getTaskPosition() != null) {
			task.setPosition(requestdto.getTaskPosition());
		}

		if (requestdto.getAssigneeID() != null) {
			User assigneeUser = userRepository.findById(requestdto.getAssigneeID())
					.orElseThrow(() -> new RuntimeException("Zuzuweisender User nicht gefunden!"));

			task.setAssignee(assigneeUser);
		}

		BoardTask updatedTask = taskRepository.save(task);

		TaskUpdateResponseDTO responseDTO = new TaskUpdateResponseDTO();
		responseDTO.setTaskID(updatedTask.getBoardTaskID());
		responseDTO.setTitle(updatedTask.getBoardTaskTitle());
		responseDTO.setDescription(updatedTask.getBoardTaskDescription());
		responseDTO.setPriority(updatedTask.getPriority());
		responseDTO.setTaskPosition(updatedTask.getPosition());
		if (updatedTask.getBoardColumn() != null) {
			responseDTO.setColumnID(updatedTask.getBoardColumn().getBoardColumnID());
		}
		if (updatedTask.getAssignee() != null) {
			responseDTO.setAssigneeID(updatedTask.getAssignee().getUserID());
			responseDTO.setAssigneeName(updatedTask.getAssignee().getUserName());
		}
		return responseDTO;
	}

	@Override
	@Transactional
	public void deleteTask(Long taskID) {
		// 1. Task laden
		BoardTask task = taskRepository.findById(taskID)
				.orElseThrow(() -> new RuntimeException("Task mit der ID " + taskID + " wurde nicht gefunden."));

		User currentUser = securityUtils.getCurrentUserEntity();

		checkTeamMemberPermission(task.getBoardColumn().getBoard(), currentUser);

		task.setDeleted(true);

		taskRepository.save(task);

	}

	private void checkTeamMemberPermission(Board board, User user) {
		// 1. Ist er der Admin der Organisation?
		boolean isAdmin = user.getRole() == UserRole.ORG_ADMIN;

		// 2. Ist er der Owner des Teams?
		boolean isOwner = board.getTeam().getOwner().getUserID().equals(user.getUserID());

		// 3. Ist er ein einfaches Mitglied (Check über das TeamMemberRepository)?
		boolean isMember = teamMemberRepository.existsByTeam_TeamIDAndUser_UserID(
				board.getTeam().getTeamID(),
				user.getUserID());

		if (!isAdmin && !isOwner && !isMember) {
			throw new RuntimeException("Zugriff verweigert: Du gehörst nicht zu diesem Team!");
		}
	}

	private void checkPermission(Board board, User user) {
		boolean isAdmin = user.getRole() == UserRole.ORG_ADMIN;
		boolean isOwner = board.getTeam().getOwner().getUserID().equals(user.getUserID());

		if (!isAdmin && !isOwner) {
			throw new RuntimeException("Keine Berechtigung, Spalten zu verwalten.");
		}
	}

}
