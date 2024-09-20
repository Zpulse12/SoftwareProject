package com.example.klantenportaal.services;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import com.example.klantenportaal.exceptions.TokenRetrievalException;
/**
 * Service voor het beheren van tokens.
 */
@Component
public class TokenManagerService {

    private final TokenService tokenService;
    private final Map<String, TokenInfo> tokenMap = new ConcurrentHashMap<>();
    /**
     * Construeert een TokenManagerService met de opgegeven TokenService.
     *
     * @param tokenService de service voor het beheren van tokens
    */
    public TokenManagerService(TokenService tokenService) {
        this.tokenService = tokenService;
    }
    /**
     * Haalt een Bearer token op voor een bedrijf op basis van het bedrijfsnummer.
     * @param companyCode het bedrijfsnummer van het bedrijf
     * @return het Bearer token
     * @throws TokenRetrievalException als het ophalen van het token mislukt
     */
    public String getBearerToken(String companyCode) throws TokenRetrievalException {
        TokenInfo cachedToken = tokenMap.get(companyCode);
        if (cachedToken != null && !cachedToken.isExpired()) {
            return cachedToken.getToken();
        } else {
            String newToken = "Bearer " + tokenService.getToken(companyCode);
            TokenInfo newTokenInfo = new TokenInfo(newToken);
            tokenMap.put(companyCode, newTokenInfo);
            return newToken;
        }
    }

    private static class TokenInfo {
        private final String token;
        private final long expirationTime;

        public TokenInfo(String token) {
            this.token = token;
            this.expirationTime = System.currentTimeMillis() + 3300000;
        }

        public String getToken() {
            return token;
        }

        public boolean isExpired() {
            return System.currentTimeMillis() >= expirationTime;
        }
    }
}
