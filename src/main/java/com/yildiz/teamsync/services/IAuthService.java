package com.yildiz.teamsync.services;

import com.yildiz.teamsync.dto.UserLoginRequestDTO;
import com.yildiz.teamsync.dto.UserLoginResponseDTO;
import com.yildiz.teamsync.dto.UserRegisterRequestDTO;
import com.yildiz.teamsync.dto.UserRegisterResponseDTO;


public interface IAuthService {
	
	public UserLoginResponseDTO  userLogin( UserLoginRequestDTO userLoginRequest);

	public UserRegisterResponseDTO userRegister(UserRegisterRequestDTO userRegisterRequest);
}
