package com.yildiz.teamsync.dto;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BoardColumnCreateRequestDTO {

	private Long boardID;        // Damit wir wissen, zu welchem Board sie gehört
	@NotBlank(message = "Der neue Name darf nicht leer sein")
    private String columnTitle;
    private Integer wipLimit;    // Optional, kann auch null sein
}
