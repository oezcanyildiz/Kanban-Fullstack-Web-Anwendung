package com.yildiz.teamsync.services.impl;


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
import com.yildiz.teamsync.services.IAuthService;

@Service
public class AuthService implements IAuthService{
	
	private final UserRepository userRepository;
	private final OrganizationRepository organizationRepository;
	private final PasswordEncoder passwordEncoder;
	public AuthService(UserRepository userRepository,
			PasswordEncoder passwordEncoder, 
			OrganizationRepository organizationRepository
			) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.organizationRepository=organizationRepository;
	}
	
	
    ///////////////////////////////
    ///							///
    ///			LOGIN    		///
    ///							///
    ///////////////////////////////
	@Override
	public UserLoginResponseDTO userLogin(UserLoginRequestDTO logindto) {
		String userEmail = logindto.getUserEmail().trim().toLowerCase();
		String passwordeingabe = logindto.getUserPassword();
		User user = userRepository.findByUserEmail(userEmail)
				.orElseThrow(() -> new RuntimeException("User mit dieser Email wurde nicht gefunden!"));
		if(!passwordEncoder.matches(passwordeingabe, user.getUserPassword())) {
			throw new IllegalArgumentException("Ungültiges Passwords");
		}		
		UserLoginResponseDTO responseDTO = new UserLoginResponseDTO();
		responseDTO.setUserEmail(user.getUserEmail());
		responseDTO.setUserName(user.getUserName());
		// jwtToken will be handled elsewhere or left null according to the original dto
		
		return responseDTO;
	}

	
    ///////////////////////////////
    ///							///
    ///     	REGISTER  		///
    ///							///
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
		
		User savedUser= userRepository.save(user);
		
		UserRegisterResponseDTO responsedto = new UserRegisterResponseDTO();
		responsedto.setUserEmail(savedUser.getUserEmail());
		// message is ignored or null according to original dto
		
		return responsedto;
	}
}
