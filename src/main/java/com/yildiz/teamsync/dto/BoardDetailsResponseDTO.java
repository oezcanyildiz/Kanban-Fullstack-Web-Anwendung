package com.yildiz.teamsync.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BoardDetailsResponseDTO {
    private Long boardID;
    private String boardName;
    private List<BoardColumnDTO> columns;
    private List<TeamMemberSimpleDTO> teamMembers;
}
