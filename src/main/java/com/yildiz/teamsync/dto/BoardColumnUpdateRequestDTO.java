package com.yildiz.teamsync.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BoardColumnUpdateRequestDTO {
	private Long boardColumnID;  // Die ID der Spalte, die geändert wird
	@NotBlank(message = "Der neue Name darf nicht leer sein")
    private String columnTitle;
    private Integer columnPosition; // Falls der Admin die Spalten verschiebt
    private Integer wipLimit;

}
