package com.yildiz.teamsync.dto;


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
	

	@NotBlank(message = "Der Task Title darf nicht leer sein.")
    private String boardTaskTitle;

    @NotBlank(message = "Die Beschreibung darf nicht leer sein.")
    private String boardTaskDescription;
    
    @NotNull(message = "Die Spalten-ID muss angegeben werden.")
    private Long columnID; // Damit wir wissen, wo der Task landet!

    @NotNull(message = "Die Position darf nicht leer sein.")
    private Integer position;

}
