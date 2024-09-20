package com.example.klantenportaal.dto;

public record AuthenticateResponseDTO(
        String token,
        long expiresIn,
        String refreshToken,
        String role,
        long refreshTokenExpiresIn,
        String userName) {
}