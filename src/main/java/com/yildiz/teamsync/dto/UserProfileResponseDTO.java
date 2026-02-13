package com.yildiz.teamsync.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileResponseDTO {
	
	private String userName;
	
	private String userLastName;
	
	private String userEmail;


}
