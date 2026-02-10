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
public class BoardCreateRequestDTO {
	
	@NotBlank(message = "Der Board-Name darf nicht leer sein.")
    private String boardName;

    @NotNull(message = "Ein Board muss einem Team zugewiesen werden.")
    private Long teamID;
	

}
