package com.yildiz.teamsync.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginRequestDTO {
	
	
	@NotBlank(message = "Die E-Mail darf nicht leer sein")
	@Email(regexp = ".+@.+\\..+", message = "Bitte geben Sie eine gültige E-Mail-Adresse an")
	private String userEmail;
	
	@NotBlank(message = "Das Passwort darf nicht leer sein")
	private String userPassword;

}
