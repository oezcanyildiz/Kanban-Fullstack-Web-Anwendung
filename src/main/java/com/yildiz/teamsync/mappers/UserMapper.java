package com.yildiz.teamsync.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.yildiz.teamsync.dto.UserLoginResponseDTO;
import com.yildiz.teamsync.dto.UserRegisterRequestDTO;
import com.yildiz.teamsync.dto.UserRegisterResponseDTO;
import com.yildiz.teamsync.entities.User;

@Mapper(componentModel="spring")
public interface UserMapper {
    
 // 1. REGISTRIERUNG: Request -> Entity
    @Mapping(target = "userPassword", ignore = true)
    @Mapping(target = "organization", ignore = true)
    User toEntity(UserRegisterRequestDTO dto);

    // 2. REGISTRIERUNG: Entity -> Response
    UserRegisterResponseDTO toRegisterResponse(User user);

    // 3. LOGIN: Entity -> Response
    @Mapping(target = "jwtToken", ignore = true)
    UserLoginResponseDTO toLoginResponse(User user);
    
    // 4. PROFIL: Entity -> Response (Falls du später eine Profil-Seite hast)
    // UserProfileResponseDTO toProfileResponse(User user);

}
