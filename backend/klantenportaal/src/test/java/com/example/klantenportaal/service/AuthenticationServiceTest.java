package com.example.klantenportaal.service;

import com.example.klantenportaal.jpa.UserRepository;
import com.example.klantenportaal.models.User;
import com.example.klantenportaal.services.AuthenticationService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static com.example.klantenportaal.ObjectMother.user1;

class AuthenticationServiceTest {
    @InjectMocks
    private AuthenticationService service;

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AuthenticationManager authenticationManager;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void signup() {
        User user = user1();
        String password = user.getPassword() + "";
        service.signup(user.getFullName(), user.getEmail(), user.getPassword(), user.getCompanyCode(), user.getVat(),
                user.getEori(), user.getPhoneNumber(), user.getCompanyName(), user.getStreetName(),
                user.getHouseNumber(), user.getCity(), user.getCountry(), user.getStatus());

        verify(passwordEncoder, times(1)).encode(password);
        verify(userRepository, times(1)).save(any(User.class));
        verifyNoMoreInteractions(passwordEncoder, userRepository);
    }

    @Test
    void authenticate_throws_error() {
        String email = "tst@example.com";
        String password = "tstpwd";

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new AuthenticationCredentialsNotFoundException("test"));

        AuthenticationException exception = assertThrows(AuthenticationException.class,
                () -> service.authenticate(email, password));
        assertEquals("test", exception.getMessage());
    }

    @Test
    void authenticate() {
        String email = "tst@example.com";
        String password = "tstpwd";
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(new User()));
        service.authenticate(email, password);
        verify(userRepository, times(1)).findByEmail(email);
        verifyNoMoreInteractions(userRepository);
    }
}
