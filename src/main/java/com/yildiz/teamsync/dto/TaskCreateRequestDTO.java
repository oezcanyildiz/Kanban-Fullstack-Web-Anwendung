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
public class TaskCreateRequestDTO {
	

	@NotBlank(message = "Der Titel darf nicht leer sein.")
    private String title;

    private String description;
    
    @NotNull(message = "Die Spalten-ID muss angegeben werden.")
    private Long columnID;

    @NotNull(message = "Bitte gib eine Priorität an.")
    private TaskPriority priority;

}
