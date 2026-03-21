package com.yildiz.teamsync.dto;



import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TeamCreateResponseDTO {
    
    private Long teamID;
    private String teamName;
    private java.util.List<BoardListResponseDTO> boards;

}
