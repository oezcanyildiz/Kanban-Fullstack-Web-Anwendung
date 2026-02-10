package com.yildiz.teamsync.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrganizationResponseDTO {
	
    private String organizationName;

    private String organizationEmail;
	
    private String invitationCode;

}
