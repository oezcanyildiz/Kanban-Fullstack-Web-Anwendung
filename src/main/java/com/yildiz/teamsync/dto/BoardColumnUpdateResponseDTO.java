package com.yildiz.teamsync.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BoardColumnUpdateResponseDTO {
    
    private Long boardColumnID;
    private String columnTitle;
    private Integer columnPosition;
    private Integer wipLimit;
    private Long boardID; 
    
}