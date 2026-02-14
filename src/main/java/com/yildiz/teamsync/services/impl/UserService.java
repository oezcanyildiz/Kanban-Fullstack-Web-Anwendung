package com.yildiz.teamsync.services.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.yildiz.teamsync.dto.UserProfileRequestDTO;
import com.yildiz.teamsync.dto.UserProfileResponseDTO;
import com.yildiz.teamsync.entities.User;
import com.yildiz.teamsync.mappers.UserMapper;
import com.yildiz.teamsync.repositories.UserRepository;
import com.yildiz.teamsync.services.IUserService;

@Service
public class UserService implements IUserService {
	
	public final UserRepository userRepository;
	public final UserMapper userMapper;
	private final PasswordEncoder passwordEncoder;
	
	public UserService (UserRepository userRepository , UserMapper userMapper,PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.userMapper=userMapper;
		this.passwordEncoder =passwordEncoder;
	}

    ///////////////////////////////
    ///							///
    ///         UPDATE  		///
    ///							///
    ///////////////////////////////
	@Override
	public UserProfileResponseDTO updateProfile(UserProfileRequestDTO requestdto) {
		User currentUser = getAuthenticatedUser();
		User user = userRepository.findById(currentUser.getUserID()).orElseThrow(()-> new RuntimeException("User wurde nicht gefunden"));
	
		if(requestdto.getUserPassword() != null && !requestdto.getOldPassword().isBlank()) {
			if (!passwordEncoder.matches(requestdto.getOldPassword(), user.getUserPassword())) {
			    throw new RuntimeException("Altes Passwort nicht korrekt.");
			}
			user.setUserPassword(passwordEncoder.encode(requestdto.getUserPassword()));
		}
		userMapper.updateEntityFromDto(requestdto, user);
		
		return userMapper.toProfileResponse(userRepository.save(user));
	}

    ///////////////////////////////
    ///							///
    ///     HILFSMETHODEN  		///
    ///							///
    ///////////////////////////////
    private User getAuthenticatedUser() {
        
        return userRepository.findById(1L).get(); 
    }

}
