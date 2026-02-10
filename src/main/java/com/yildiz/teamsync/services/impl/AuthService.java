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
import com.yildiz.teamsync.mappers.UserMapper;
import com.yildiz.teamsync.repositories.OrganizationRepository;
import com.yildiz.teamsync.repositories.UserRepository;
import com.yildiz.teamsync.services.IAuthService;

@Service
public class AuthService implements IAuthService{
	
	private final UserRepository userRepository;
	private final OrganizationRepository organizationRepository;
	private final PasswordEncoder passwordEncoder;
	private final UserMapper userMapper;
	

	public AuthService(UserRepository userRepository,
			PasswordEncoder passwordEncoder, 
			UserMapper userMapper,
			OrganizationRepository organizationRepository
			) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.userMapper= userMapper;
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
		UserLoginResponseDTO responseDTO = userMapper.toLoginResponse(user);	
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
		Organization organization = organizationRepository.findByinvitationCode(code).orElseThrow(() -> new RuntimeException("dieser Organization wurde nicht gefunden!"));	
		if (userRepository.findByUserEmail(userEmail).isPresent()) {
			throw new IllegalArgumentException("Mit dieser Email existiert schon ein Account.");
		}
		User user = userMapper.toEntity(registerdto);
		user.setUserPassword(passwordEncoder.encode(registerdto.getUserPassword()));
		user.setOrganization(organization);
		
		// Wenn die Organisation noch keine User hat, ist der erste der Admin
		if (userRepository.countByOrganization(organization) == 0) {
		    user.setRole(UserRole.ORG_ADMIN);
		} else {
		    user.setRole(UserRole.USER);
		}
		
		User savedUser= userRepository.save(user);
		UserRegisterResponseDTO responsedto=userMapper.toRegisterResponse(savedUser);
		return responsedto;
	}
}
