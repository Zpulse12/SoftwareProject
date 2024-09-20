package com.example.klantenportaal.service;

import static com.example.klantenportaal.ObjectMother.adminUser;
import static com.example.klantenportaal.ObjectMother.user1;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.klantenportaal.dto.RegistrationUserDTO;
import com.example.klantenportaal.jpa.UserRepository;
import com.example.klantenportaal.models.User;
import com.example.klantenportaal.services.RegisterService;

class RegisterServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private RegisterService registerService;

    private int id = 1;

    private User user = user1();

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAllUsers() {
        User user2 = adminUser();
        when(userRepository.findAll()).thenReturn(Arrays.asList(user, user2));

        List<RegistrationUserDTO> users = registerService.findAll();

        assertNotNull(users);
        assertEquals(2, users.size());
        verify(userRepository).findAll();
    }

    @Test
    void testGetCompanyCode() {

        String expectedCode = "COMP123";
        when(userRepository.findCompanyCodeById(id)).thenReturn(expectedCode);

        String code = registerService.getCompanyCode(id);

        assertEquals(expectedCode, code);
        verify(userRepository).findCompanyCodeById(id);
    }

    @Test
    void testSaveUser() {

        when(userRepository.save(user)).thenReturn(user);

        RegistrationUserDTO savedUser = registerService.save(user);

        assertNotNull(savedUser);
        verify(userRepository).save(user);
    }

    @Test
    void testFindById() {

        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        User actualUser = registerService.findById(id);

        assertEquals(user, actualUser);
        verify(userRepository).findById(id);
    }

    @Test
    void testDeleteRegistration() {

        when(userRepository.existsById(id)).thenReturn(true);
        doNothing().when(userRepository).deleteById(id);

        registerService.deleteRegistration(id);

        verify(userRepository).deleteById(id);
    }
}
