package com.yildiz.teamsync.dto;
import lombok.*;

@Getter 
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TeamMemberAddRequestDTO {

	private Long teamID;
    private Long userID;
}
