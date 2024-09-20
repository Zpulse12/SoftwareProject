package com.example.klantenportaal.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import com.example.klantenportaal.services.JwtService;

import java.security.Key;
import java.util.*;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtServiceTest {

    private JwtService jwtService;

    @Mock
    private UserDetails userDetails;

    private String secretKey = Base64.getEncoder().encodeToString("your-very-secret-key-for-jwt-which-is-very-long".getBytes());
    private String refreshTokenSecretKey = Base64.getEncoder().encodeToString("your-very-secret-key-for-refresh-token-which-is-very-long".getBytes());
    private long jwtExpiration = 3600000; // 1 hour
    private long refreshTokenExpiration = 7200000; // 2 hours

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        jwtService = new JwtService();
        when(userDetails.getUsername()).thenReturn("testUser");
        ReflectionTestUtils.setField(jwtService, "secretKey", secretKey);
        ReflectionTestUtils.setField(jwtService, "refreshTokenSecretKey", refreshTokenSecretKey);
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", jwtExpiration);
        ReflectionTestUtils.setField(jwtService, "refreshTokenExpiration", refreshTokenExpiration);
    }

    @Test
    void testExtractUsername() {
        String token = jwtService.generateToken(userDetails);
        String username = jwtService.extractUsername(token);
        assertEquals("testUser", username);
    }

    @Test
    void testGenerateToken() {
        String token = jwtService.generateToken(userDetails);
        assertNotNull(token);
        assertTrue(jwtService.isTokenValid(token, userDetails));
    }


    @Test
    void testIsTokenExpired() {
        String token = jwtService.generateToken(userDetails);
        assertFalse(jwtService.isTokenExpired(token));    //make isTokenExpired() public
    }

    @Test
    void testExtractAllClaims() {
        String token = jwtService.generateToken(userDetails);
        Claims claims = jwtService.extractAllClaims(token); //make extractAllClaims() public
        assertEquals("testUser", claims.getSubject());
    }

    @Test
    void testBuildToken() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", Arrays.asList("ROLE_USER"));
        String token = jwtService.generateToken(claims, userDetails);
        assertNotNull(token);
    }

    @Test
    void testGetSignInKey() {
        assertNotNull(jwtService.getSignInKey()); //make getSignInKey() public
    }

    @Test
    void testGetSignInKeyFromRefreshKey() {
        assertNotNull(jwtService.getSignInKeyFromRefreshKey()); //make getSignInKeyFromRefreshKey() public
    }
}
