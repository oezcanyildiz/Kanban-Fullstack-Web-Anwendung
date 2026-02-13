package com.yildiz.teamsync.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BoardListResponseDTO {
    private Long boardID;
    private String boardName;
    private String teamName; 
}