package com.yildiz.teamsync.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BoardCreateResponseDTO {
	
	private Long boardID; // Es ist immer gut, die neue ID zurückzugeben
    private String boardName;
    
    public String getMsg() {
        return boardName + " wurde erfolgreich erstellt.";
    }

}
