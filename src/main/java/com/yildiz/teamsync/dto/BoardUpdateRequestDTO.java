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
public class BoardUpdateRequestDTO {
	
	@NotNull(message = "Board-ID muss angegeben werden.")
    private Long boardID; 
	
    @NotBlank(message = "Der neue Name darf nicht leer sein")
    private String newBoardName;

}
