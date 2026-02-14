package com.yildiz.teamsync.dto;

import com.yildiz.teamsync.enums.TaskPriority;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TaskUpdateRequestDTO {
	
	@NotNull(message = "Task-ID darf nicht fehlen.")
    private Long taskID; // Werden wir updaten?

    @NotBlank(message = "Der Titel darf nicht leer sein.")
    private String title;

    private String description;
    
    @NotNull(message = "Die Spalten-ID muss angegeben werden.")
    private Long columnID; // Wichtig für Drag & Drop in andere Spalten!

    @NotNull(message = "Bitte gib eine Priorität an.")
    private TaskPriority priority;
    
    private Long assigneeID; // Nur die ID, nicht das ganze User-Objekt!
    
    private Integer taskPosition; // Falls du innerhalb der Spalte sortierst
}
