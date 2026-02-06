package com.yildiz.teamsync.entities;

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
