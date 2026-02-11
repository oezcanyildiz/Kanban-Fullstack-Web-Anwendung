package com.yildiz.teamsync.dto;

import com.yildiz.teamsync.enums.UserRole;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserListResponseDTO {

		private Long userID;

		private String userName;

		private String userLastName;

		private String userEmail;

	    private boolean isOnline = false;
		
	    private UserRole role;
	    
	    private Long organizationID;

}
