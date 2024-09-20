package com.example.klantenportaal;

import com.example.klantenportaal.services.TokenService;
import com.example.klantenportaal.exceptions.TokenRetrievalException;
import com.example.klantenportaal.services.TokenManagerService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class TokenUpdaterTest {

    @Mock
    private TokenService tokenService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testUpdateBearerToken() throws TokenRetrievalException {
        String mockToken = "mockToken";
        String companyCode = "mockCode";
        when(tokenService.getToken(companyCode)).thenReturn(mockToken);

        TokenManagerService tokenUpdater = new TokenManagerService(tokenService);
        String actualToken = tokenUpdater.getBearerToken(companyCode);

        String expectedBearerToken = "Bearer " + mockToken;
        assertEquals(expectedBearerToken, actualToken);
    }

    @Test
    void testUpdateBearerTokenCached() throws TokenRetrievalException {
        String mockToken = "mockToken";
        String companyCode = "mockCode";
        when(tokenService.getToken(companyCode)).thenReturn(mockToken);

        TokenManagerService tokenUpdater = new TokenManagerService(tokenService);

        verify(tokenService, times(0)).getToken(companyCode);

        String actualToken = tokenUpdater.getBearerToken(companyCode);
        String actualCachedToken = tokenUpdater.getBearerToken(companyCode);

        verify(tokenService, times(1)).getToken(companyCode);

        assertEquals(actualToken, actualCachedToken);
    }
}
