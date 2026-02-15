package com.yildiz.teamsync.dto;

import com.yildiz.teamsync.enums.TaskPriority;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TaskUpdateResponseDTO {
	private Long taskID;
    private String title;
    private String description;
    private TaskPriority priority;
    private Integer taskPosition;
    private Long columnID; // Damit das Frontend weiß, in welcher Spalte der Task jetzt sitzt
    private Long assigneeID; // ID des zuständigen Users (falls geändert)
    private String assigneeName; // Bonus: Der Name des Users für die Anzeige auf der Task-Karte
}