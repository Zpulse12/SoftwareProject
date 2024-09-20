package com.example.klantenportaal.services;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.klantenportaal.dto.TokenDTO;
import com.example.klantenportaal.exceptions.TokenRetrievalException;
/**
 * Service voor het beheren van tokens.
*/
@Service
public class TokenService {

    private static final String BASE_URL = "https://sw11-1.devops-ap.be/authenticate/token";

    private final RestTemplate restTemplate;
    /**
     * Construeert een TokenService.
    */
    public TokenService() {
        this.restTemplate = new RestTemplate();
    }
    /**
     * Haalt een token op voor een bedrijf op basis van het bedrijfsnummer.
     * @param companyCode het bedrijfsnummer van het bedrijf
     * @return het token
     * @throws TokenRetrievalException als het ophalen van het token mislukt
    */
    public String getToken(String companyCode) throws TokenRetrievalException {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            String requestBody = "{\"group\":\"" + companyCode + "\",\"apiKey\":\"SECRET-KEY-" + companyCode + "\"}";

            HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<TokenDTO> response = restTemplate.postForEntity(BASE_URL, requestEntity,
                    TokenDTO.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return response.getBody().token();
            } else {
                throw new TokenRetrievalException("Failed to retrieve token. HTTP Status: " + response.getStatusCode());
            }
        } catch (Exception e) {
            throw new TokenRetrievalException("Failed to retrieve token.", e);
        }
    }
}
