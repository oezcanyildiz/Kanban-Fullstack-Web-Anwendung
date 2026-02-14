package com.yildiz.teamsync.dto;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class BoardColumnResponseDTO {

	private Long boardColumnID;
    private String columnTitle;
    private Integer columnPosition;
    private Integer wipLimit;
    // Hinweis: Hier fügen wir morgen die List<TaskResponseDTO> tasks ein!
}
