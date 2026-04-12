package com.yildiz.teamsync.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.yildiz.teamsync.config.SecurityUtils;
import com.yildiz.teamsync.dto.UserProfileRequestDTO;
import com.yildiz.teamsync.dto.UserProfileResponseDTO;
import com.yildiz.teamsync.entities.User;
import com.yildiz.teamsync.exceptions.BadRequestException;
import com.yildiz.teamsync.exceptions.ResourceNotFoundException;
import com.yildiz.teamsync.repositories.UserRepository;
import com.yildiz.teamsync.services.impl.UserService;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    //Diese werden als Dummies erstellt — keine echte DB
    @Mock private UserRepository userRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private SecurityUtils securityUtils;

    @InjectMocks
    private UserService userService;

    // Testdaten die wir in mehreren Tests brauchen
    private User currentUser;

    @BeforeEach
    void setUp(){

        currentUser= new User();
        currentUser.setUserID(10L);
        currentUser.setUserPassword("Abc123456");
    }

    @Test
    void updateProfile_ResourcenotFoundgeworfen(){
        UserProfileRequestDTO dto= new UserProfileRequestDTO();

        when(securityUtils.getCurrentUserEntity()).thenReturn(currentUser);
        when(userRepository.findById(10L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, ()-> userService.updateProfile(dto));
    }
    @Test
    void updateProfile_Badrequestgeworfen(){
        UserProfileRequestDTO dto= new UserProfileRequestDTO();

        dto.setUserPassword("Abc123456");
        dto.setOldPassword("falschePassword");

        when(securityUtils.getCurrentUserEntity()).thenReturn(currentUser);
        when(userRepository.findById(10L)).thenReturn(Optional.of(currentUser));
        when(passwordEncoder.matches(dto.getOldPassword(), currentUser.getUserPassword())).thenReturn(false);
        assertThrows(BadRequestException.class , ()-> userService.updateProfile(dto));
    }
    @Test
    void updateProfile_happyPath(){
        UserProfileRequestDTO dto = new UserProfileRequestDTO();

        dto.setUserEmail("ozcan@yildiz.com");
        dto.setUserLastName("Yildiz");

        when(securityUtils.getCurrentUserEntity()).thenReturn(currentUser);
        when(userRepository.findById(10L)).thenReturn(Optional.of(currentUser));
        when(userRepository.save(any())).thenReturn(currentUser);

        UserProfileResponseDTO result = userService.updateProfile(dto);
        assertEquals("ozcan@yildiz.com", result.getUserEmail());
        assertEquals("Yildiz", result.getUserLastName());




    }


}
