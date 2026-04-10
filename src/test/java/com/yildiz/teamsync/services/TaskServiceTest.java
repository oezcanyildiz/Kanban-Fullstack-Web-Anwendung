package com.yildiz.teamsync.services;

import com.yildiz.teamsync.config.SecurityUtils;
import com.yildiz.teamsync.dto.TaskUpdateRequestDTO;
import com.yildiz.teamsync.dto.TaskCreateRequestDTO;

import com.yildiz.teamsync.entities.*;
import com.yildiz.teamsync.enums.TaskPriority;
import com.yildiz.teamsync.enums.UserRole;
import com.yildiz.teamsync.exceptions.BadRequestException;
import com.yildiz.teamsync.repositories.*;
import com.yildiz.teamsync.services.impl.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)  // Mockito aktivieren
class TaskServiceTest {

    // Diese werden als Dummies erstellt — keine echte DB
    @Mock private BoardColumnRepository boardColumnRepository;
    @Mock private UserRepository userRepository;
    @Mock private BoardTaskRepository taskRepository;
    @Mock private TeamMemberRepository teamMemberRepository;
    @Mock private SecurityUtils securityUtils;

    // TaskService bekommt die Mocks injiziert
    @InjectMocks
    private TaskService taskService;

    // Testdaten die wir in mehreren Tests brauchen
    private User currentUser;
    private User assigneeUser;
    private BoardTask task;
    private Board board;
    private Team team;

    @BeforeEach  // Läuft vor JEDEM Test
    void setUp() {
        // Team aufbauen
        team = new Team();
        team.setTeamID(1L);
        User owner = new User();
        owner.setUserID(99L);
        team.setOwner(owner);

        // Board aufbauen
        board = new Board();
        board.setBoardID(1L);
        board.setTeam(team);

        // Spalte aufbauen
        BoardColumn column = new BoardColumn();
        column.setBoardColumnID(1L);
        column.setBoard(board);

        // Task aufbauen
        task = new BoardTask();
        task.setBoardTaskID(1L);
        task.setBoardColumn(column);

        // Aktueller User (macht den Request)
        currentUser = new User();
        currentUser.setUserID(10L);
        currentUser.setRole(UserRole.USER);

        // Assignee User (wird zugewiesen)
        assigneeUser = new User();
        assigneeUser.setUserID(20L);
    }

    @Test
    void updateTask_wennAssigneeNichtImTeam_wirdBadRequestExceptionGeworfen() {
        // ARRANGE
        TaskUpdateRequestDTO dto = new TaskUpdateRequestDTO();
        dto.setTaskID(1L);
        dto.setTitle("Test Task");
        dto.setColumnID(1L);
        dto.setAssigneeID(20L); // Assignee der nicht im Team ist

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(securityUtils.getCurrentUserEntity()).thenReturn(currentUser);
        when(teamMemberRepository.existsByTeam_TeamIDAndUser_UserID(1L, 10L))
            .thenReturn(true); // currentUser ist im Team
        when(userRepository.findById(20L)).thenReturn(Optional.of(assigneeUser));
        when(teamMemberRepository.existsByTeam_TeamIDAndUser_UserID(1L, 20L))
            .thenReturn(false); // assigneeUser ist NICHT im Team

        // ACT + ASSERT
        assertThrows(BadRequestException.class, () -> taskService.updateTask(dto));
    }

    @Test
    void updateTask_wennAssigneeImTeam_wirdTaskGespeichert() {
        // ARRANGE
        TaskUpdateRequestDTO dto = new TaskUpdateRequestDTO();
        dto.setTaskID(1L);
        dto.setTitle("Test Task");
        dto.setColumnID(1L);
        dto.setAssigneeID(20L);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(securityUtils.getCurrentUserEntity()).thenReturn(currentUser);
        when(teamMemberRepository.existsByTeam_TeamIDAndUser_UserID(1L, 10L))
            .thenReturn(true);
        when(userRepository.findById(20L)).thenReturn(Optional.of(assigneeUser));
        when(teamMemberRepository.existsByTeam_TeamIDAndUser_UserID(1L, 20L))
            .thenReturn(true); // assigneeUser IST im Team
        when(taskRepository.save(any())).thenReturn(task);

        // ACT + ASSERT — kein Fehler erwartet
        assertDoesNotThrow(() -> taskService.updateTask(dto));
        verify(taskRepository, times(1)).save(task); // wurde save() aufgerufen?
    }
    @Test
    void createTask_wenn50Tasklimiterreicht_wirdBadRequestgeworfen(){
        //Taskanzahl von 50 erreicht deswegen badrequestException werfen
        TaskCreateRequestDTO dtocreate = new TaskCreateRequestDTO();

        dtocreate.setColumnID(1L);
        dtocreate.setDescription("Test Description");
        dtocreate.setPriority(TaskPriority.LOW);
        dtocreate.setTitle("Test Title");

        team.getOwner().setUserID(10L);

        BoardColumn column = task.getBoardColumn();

        when(boardColumnRepository.findById(1L)).thenReturn(Optional.of(column));
        when(securityUtils.getCurrentUserEntity()).thenReturn(currentUser);
        when(taskRepository.countByBoardColumn_Board_BoardIDAndDeletedFalse(board.getBoardID())).thenReturn(51L);

        // ACT + ASSERT —  Fehler erwartet
        // wir haben anzahl 51. daher erwarte ich fehler 
        assertThrows(BadRequestException.class, () -> taskService.createTask(dtocreate));
    }
    @Test
    void createTask_wennWIPlimiterreicht_BadRequestgeworfen(){
        TaskCreateRequestDTO dto = new TaskCreateRequestDTO();

        dto.setColumnID(1L);
        dto.setDescription("Test");
        dto.setPriority(TaskPriority.LOW);
        dto.setTitle("Test Title");

        team.getOwner().setUserID(10L);

        BoardColumn column = task.getBoardColumn();
        column.setWipLimit(5);

        
        when(boardColumnRepository.findById(1L)).thenReturn(Optional.of(column));
        when(securityUtils.getCurrentUserEntity()).thenReturn(currentUser);
        when(taskRepository.countByBoardColumn_BoardColumnIDAndDeletedFalse(column.getBoardColumnID())).thenReturn(6L);


        assertThrows(BadRequestException.class,()-> taskService.createTask(dto));
    }



}
