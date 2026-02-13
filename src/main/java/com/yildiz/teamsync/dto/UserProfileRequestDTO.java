package com.yildiz.teamsync.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileRequestDTO {
	
	@NotBlank(message = "Die Email darf nicht leer sein")
	private String userEmail;
	
	@NotBlank(message = "Die Nachname darf nicht leer sein")
	private String userLastName;
	
	private String oldPassword;
	
	@Size(min = 8, max = 30, message = "Das Passwort muss zwischen 8 und 30 Zeichen lang sein")
	@Pattern(
	    regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!]).*$",
	    message = "Das Passwort muss mindestens eine Zahl, einen Großbuchstaben, einen Kleinbuchstaben und ein Sonderzeichen enthalten")
	private String userPassword;

}
