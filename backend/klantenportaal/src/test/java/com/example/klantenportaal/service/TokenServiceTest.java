package com.example.klantenportaal.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.example.klantenportaal.dto.TokenDTO;
import com.example.klantenportaal.exceptions.TokenRetrievalException;
import com.example.klantenportaal.jpa.UserRepository;
import com.example.klantenportaal.services.RegisterService;
import com.example.klantenportaal.services.TokenService;

public class TokenServiceTest {
    @InjectMocks
    private TokenService tokenService;
    @Mock
    private RestTemplate restTemplate;

    private static final String BASE_URL = "https://sw11-1.devops-ap.be/authenticate/token";
    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    //Throws com.example.klantenportaal.exceptions.TokenRetrievalException: Failed to retrieve token.
    
    @Test
    void getTokenTest() throws TokenRetrievalException{
        String companyCode = "COMPANY123";
        String expectedToken = "{\"group\":\"" + companyCode + "\",\"apiKey\":\"SECRET-KEY-" + companyCode + "\"}";
        // Arrange
        TokenDTO tokenDTO = new TokenDTO("testToken");
        ResponseEntity<TokenDTO> responseEntity = new ResponseEntity<>(tokenDTO, HttpStatus.OK);
        
        when(restTemplate.postForEntity(any(String.class), any(HttpEntity.class), eq(TokenDTO.class)))
                .thenReturn(responseEntity);

        String token = tokenService.getToken(companyCode);

        assertEquals("testToken", token);
        verify(restTemplate).postForEntity(eq(BASE_URL), any(HttpEntity.class), eq(TokenDTO.class));
    
    
    }
    
}
