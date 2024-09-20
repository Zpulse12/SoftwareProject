package com.example.klantenportaal.services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.klantenportaal.jpa.RefreshTokenRepository;
import com.example.klantenportaal.models.RefreshToken;
/**
 * Service voor het beheren van refreshtokens.
 */
@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    /**
     * Construeert een RefreshTokenService met de opgegeven RefreshTokenRepository.
     * @param refreshTokenRepository de repository die wordt gebruikt voor het beheren van refreshtokens
    */
    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public void save(RefreshToken refreshToken) {
        refreshTokenRepository.save(refreshToken);
    }

    public Optional<RefreshToken> findByToken(String tokenString) {
        return refreshTokenRepository.findByToken(tokenString);
    }

    public void delete(RefreshToken refreshToken) {
        refreshTokenRepository.delete(refreshToken);
    }
}
