package com.example.klantenportaal.service;

import static com.example.klantenportaal.ObjectMother.adminUser;
import static com.example.klantenportaal.ObjectMother.token1;
import static com.example.klantenportaal.ObjectMother.user1;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.klantenportaal.dto.RegistrationUserDTO;
import com.example.klantenportaal.jpa.RefreshTokenRepository;
import com.example.klantenportaal.jpa.UserRepository;
import com.example.klantenportaal.models.RefreshToken;
import com.example.klantenportaal.models.User;
import com.example.klantenportaal.services.RefreshTokenService;
import com.example.klantenportaal.services.UserService;

public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    private User user1 = user1();
    @Test
    void getAllUsers(){
        User user2 = adminUser();
        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));
        List<User> users = userService.getAllUsers();
        assertNotNull(users);
        assertEquals(2, users.size());
        verify(userRepository).findAll();
    }
}
