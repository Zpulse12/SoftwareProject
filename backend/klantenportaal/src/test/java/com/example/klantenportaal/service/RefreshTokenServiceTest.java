package com.example.klantenportaal.service;

import static com.example.klantenportaal.ObjectMother.token1;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.klantenportaal.jpa.RefreshTokenRepository;
import com.example.klantenportaal.models.RefreshToken;
import com.example.klantenportaal.services.RefreshTokenService;

public class RefreshTokenServiceTest {
    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @InjectMocks
    private RefreshTokenService refreshTokenService;

    private RefreshToken refreshToken = token1();

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test 
    void refreshTokenSaveTest(){
        when(refreshTokenRepository.save(refreshToken)).thenReturn(refreshToken);
        RefreshToken savedToken = refreshTokenRepository.save(refreshToken);
        assertNotNull(savedToken);
        verify(refreshTokenRepository).save(refreshToken);
    }

    @Test
    void findByTokenTest(){
        when(refreshTokenRepository.findByToken(refreshToken.getToken())).thenReturn(Optional.of(refreshToken));
        Optional<RefreshToken> actualTokenOptional = refreshTokenRepository.findByToken(refreshToken.getToken());
        RefreshToken actualToken = actualTokenOptional.orElseThrow(() -> new AssertionError("Token not found"));
        assertEquals(refreshToken, actualToken);
        verify(refreshTokenRepository).findByToken(actualToken.getToken());
    }

    @Test
    void deleteTokenTest(){
        when(refreshTokenRepository.findByToken(refreshToken.getToken())).thenReturn(Optional.of(refreshToken));
        doNothing().when(refreshTokenRepository).delete(refreshToken);
        refreshTokenService.delete(refreshToken);
        verify(refreshTokenRepository).delete(refreshToken);
    }
}
