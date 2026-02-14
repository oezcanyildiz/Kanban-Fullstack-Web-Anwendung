package com.yildiz.teamsync.entities;

import com.yildiz.teamsync.enums.TaskPriority;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="board_task")

public class BoardTask extends BaseEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long boardTaskID;
	
	@Column(nullable = false, columnDefinition = "TEXT")
	private String boardTaskTitle;
	
	@Column(nullable = false, columnDefinition = "TEXT")
	private String boardTaskDescription;
	
	@Column(nullable=false)
	private Integer	position;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "priority", nullable = false)
	private TaskPriority priority = TaskPriority.MEDIUM; // Default-Wert
	
	@Column(nullable = false)
	private boolean deleted=false;
	
	// Viele Tasks gehören zu einer Spalte
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "column_id", nullable = false)
    private BoardColumn boardColumn;
	
    // Viele Tasks wurden von einem User erstellt
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", nullable = false)
    private User creator;
	
    // Viele Tasks können einem User zugewiesen sein (kann null sein, wenn niemand dran arbeitet)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignee_id")
    private User assignee;
	

}
