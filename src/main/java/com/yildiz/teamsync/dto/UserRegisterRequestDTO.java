package com.yildiz.teamsync.dto;

import jakarta.validation.constraints.Email;
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

public class UserRegisterRequestDTO {
	

	@NotBlank(message = "Für Registirieung wird eine Einladungscode benötigt.")
	private String invitationCode;
	
	@NotBlank(message = "Die Vorname darf nicht leer sein")
	private String userName;

	@NotBlank(message = "Die Nachname darf nicht leer sein")
	private String userLastName;

	@NotBlank(message = "Die E-Mail darf nicht leer sein")
	@Email(regexp = ".+@.+\\..+", message = "Bitte geben Sie eine gültige E-Mail-Adresse an")
	private String userEmail;

	@NotBlank(message = "Das Passwort darf nicht leer sein")
	@Size(min = 8, max = 30, message = "Das Passwort muss zwischen 8 und 30 Zeichen lang sein")
	@Pattern(
	    regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!]).*$",
	    message = "Das Passwort muss mindestens eine Zahl, einen Großbuchstaben, einen Kleinbuchstaben und ein Sonderzeichen enthalten")
	private String userPassword;

	
}
