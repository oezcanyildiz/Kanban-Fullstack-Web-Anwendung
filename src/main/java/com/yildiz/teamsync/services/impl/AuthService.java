package com.yildiz.teamsync.services.impl;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.yildiz.teamsync.dto.UserLoginRequestDTO;
import com.yildiz.teamsync.dto.UserLoginResponseDTO;
import com.yildiz.teamsync.dto.UserRegisterRequestDTO;
import com.yildiz.teamsync.dto.UserRegisterResponseDTO;
import com.yildiz.teamsync.entities.Organization;
import com.yildiz.teamsync.entities.User;
import com.yildiz.teamsync.enums.UserRole;

import com.yildiz.teamsync.repositories.OrganizationRepository;
import com.yildiz.teamsync.repositories.UserRepository;
import com.yildiz.teamsync.security.JwtUtils;
import com.yildiz.teamsync.services.IAuthService;

@Service
public class AuthService implements IAuthService {

	private final UserRepository userRepository;
	private final OrganizationRepository organizationRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtUtils jwtUtils;
	private final AuthenticationManager authenticationManager;

	public AuthService(UserRepository userRepository,
			PasswordEncoder passwordEncoder,
			OrganizationRepository organizationRepository,
			JwtUtils jwtUtils,
			AuthenticationManager authenticationManager) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.organizationRepository = organizationRepository;
		this.jwtUtils = jwtUtils;
		this.authenticationManager = authenticationManager;
	}

	///////////////////////////////
	/// ///
	/// LOGIN ///
	/// ///
	///////////////////////////////
	@Override
	public UserLoginResponseDTO userLogin(UserLoginRequestDTO logindto) {
		String userEmail = logindto.getUserEmail().trim().toLowerCase();
		String passwordeingabe = logindto.getUserPassword();

		// 1. Authentifizierung durchführen
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(userEmail, passwordeingabe));

		// 2. Wenn erfolgreich, im SecurityContext speichern
		SecurityContextHolder.getContext().setAuthentication(authentication);

		// 3. Den User aus der DB holen für die Response-Daten
		User user = userRepository.findByUserEmail(userEmail)
				.orElseThrow(() -> new RuntimeException("User mit dieser Email wurde nicht gefunden!"));

		// 4. Den JWT Token generieren
		String jwt = jwtUtils.generateToken(authentication.getName());

		// 5. Response zusammenbauen
		UserLoginResponseDTO responseDTO = new UserLoginResponseDTO();
		responseDTO.setUserEmail(user.getUserEmail());
		responseDTO.setUserName(user.getUserName());
		responseDTO.setUserLastName(user.getUserLastName());
		responseDTO.setJwtToken(jwt);
		responseDTO.setRole(user.getRole());
		responseDTO.setUserID(user.getUserID());
		if (user.getOrganization() != null) {
			responseDTO.setOrganizationID(user.getOrganization().getOrganizationID());
			responseDTO.setOrganizationName(user.getOrganization().getOrganizationName());
			responseDTO.setInvitationCode(user.getOrganization().getInvitationCode());
		}

		return responseDTO;
	}

	///////////////////////////////
	/// ///
	/// REGISTER ///
	/// ///
	///////////////////////////////
	@Override
	public UserRegisterResponseDTO userRegister(UserRegisterRequestDTO registerdto) {
		String code = registerdto.getInvitationCode();
		String userEmail = registerdto.getUserEmail().trim().toLowerCase();

		Organization organization = organizationRepository.findByinvitationCode(code)
				.orElseThrow(() -> new RuntimeException("dieser Organization wurde nicht gefunden!"));

		if (userRepository.findByUserEmail(userEmail).isPresent()) {
			throw new IllegalArgumentException("Mit dieser Email existiert schon ein Account.");
		}
		User user = new User();
		user.setUserName(registerdto.getUserName());
		user.setUserLastName(registerdto.getUserLastName());
		user.setUserEmail(registerdto.getUserEmail());
		// Password and Org are handled manually already
		user.setUserPassword(passwordEncoder.encode(registerdto.getUserPassword()));
		user.setOrganization(organization);

		// Wenn die Organisation noch keine User hat, ist der erste der Admin
		if (userRepository.countByOrganization(organization) == 0) {
			user.setRole(UserRole.ORG_ADMIN);
		} else {
			user.setRole(UserRole.USER);
		}

		User savedUser = userRepository.save(user);

		UserRegisterResponseDTO responsedto = new UserRegisterResponseDTO();
		responsedto.setUserEmail(savedUser.getUserEmail());

		String message = String.format(
				"Willkommen, %s! Dein Account für die Organisation %s wurde erstellt. Bitte logge dich jetzt ein.",
				savedUser.getUserName(),
				organization.getOrganizationName());
		responsedto.setMessage(message);

		return responsedto;
	}
}
