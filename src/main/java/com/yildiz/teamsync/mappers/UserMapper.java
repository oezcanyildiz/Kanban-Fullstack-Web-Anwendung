package com.yildiz.teamsync.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.yildiz.teamsync.dto.UserListResponseDTO;
import com.yildiz.teamsync.dto.UserLoginResponseDTO;
import com.yildiz.teamsync.dto.UserProfileRequestDTO;
import com.yildiz.teamsync.dto.UserProfileResponseDTO;
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
    
    // 4. PROFIL: Entity -> Response 
    UserProfileResponseDTO toProfileResponse(User user);
    

    // 5. UPDATE: Bestehenden User mit DTO-Daten aktualisieren
    @Mapping(target = "userID", ignore = true)        // ID bleibt gleich
    @Mapping(target = "userName", ignore = true)     // VORNAME WIRD IGNORIERT
    @Mapping(target = "role", ignore = true)          // Rolle darf User nicht selbst ändern
    @Mapping(target = "organization", ignore = true)  // Org bleibt gleich
    @Mapping(target = "userPassword", ignore = true)  // Passwort machen wir manuell im Service
    @Mapping(target = "oldPassword", ignore = true)
    void updateEntityFromDto(UserProfileRequestDTO dto, @MappingTarget User user);
    
    
    @Mapping(target = "organizationID", source = "organization.organizationID")
    UserListResponseDTO toListResponse(User user);
    
    List<UserListResponseDTO> toListResponseList(List<User> users);
    
  
}
