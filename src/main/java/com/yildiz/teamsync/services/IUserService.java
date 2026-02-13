package com.yildiz.teamsync.services;

import com.yildiz.teamsync.dto.UserProfileRequestDTO;
import com.yildiz.teamsync.dto.UserProfileResponseDTO;

public interface IUserService {
	
	UserProfileResponseDTO updateProfile( UserProfileRequestDTO dto);

}
