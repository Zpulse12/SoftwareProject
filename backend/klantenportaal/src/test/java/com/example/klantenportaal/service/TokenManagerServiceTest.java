package com.example.klantenportaal.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.klantenportaal.exceptions.TokenRetrievalException;
import com.example.klantenportaal.services.*;
import com.example.klantenportaal.services.TokenManagerService.TokenInfo;

public class TokenManagerServiceTest {
    @Mock
    private TokenService tokenService;

    @InjectMocks
    private TokenManagerService tokenManagerService;

    private String companyCode;
    private String token;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        companyCode = "COMPANY_CODE";
        token = "token-value";
    }
    @Test
    void getBearerTokenTest() throws TokenRetrievalException{
        when(tokenService.getToken(companyCode)).thenReturn(token);
        String bearerToken = tokenManagerService.getBearerToken(companyCode);
        assertNotNull(bearerToken);
        assertEquals("Bearer " + token, bearerToken);
        verify(tokenService, times(1)).getToken(companyCode);
    }
}
