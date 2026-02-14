package com.yildiz.teamsync.dto;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BoardColumnCreateResponseDTO {
	
	private Long boardColumnID;    // Ganz wichtig für das Frontend (für spätere Updates/Löschen)
    private String columnTitle;
    private Integer columnPosition; // Damit das Frontend weiß, an welcher Stelle die Spalte steht
    private Integer wipLimit;
    private Long boardID;

}
