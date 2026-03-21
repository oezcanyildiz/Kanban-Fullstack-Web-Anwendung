package com.yildiz.teamsync.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class UserLoginResponseDTO {
	
	private String userEmail;
	
	private String userName;

	private String userLastName;

	private String jwtToken;

	private com.yildiz.teamsync.enums.UserRole role;
	private Long organizationID;
	private String organizationName;
	private String invitationCode;
	private Long userID;
}
