package com.yildiz.teamsync.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class BoardUpdateResponseDTO {
	

	private Long boardID; 
    private String boardName;
    
    public String getMsg() {
        return "Board mit der ID " + boardID + " wurde erfolgreich in '" + boardName + "' umbenannt.";
    }

}
