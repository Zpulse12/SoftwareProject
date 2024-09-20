package com.example.klantenportaal.services;

import com.example.klantenportaal.dto.AuthenticateResponseDTO;
import com.example.klantenportaal.jpa.UserRepository;
import com.example.klantenportaal.models.RefreshToken;
import com.example.klantenportaal.models.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
/**
 * Service voor authenticatie.
*/
@Service
public class AuthenticationService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    private final RefreshTokenService refreshTokenService;
    /**
     * Construeert een AuthenticationService met de opgegeven dependencies.
     *
     * @param userRepository de repository voor gebruikers
     * @param authenticationManager de manager voor authenticatie
     * @param passwordEncoder de encoder voor wachtwoorden
     * @param jwtService de service voor het beheren van JWT-tokens
     * @param refreshTokenService de service voor het beheren van refreshtoken
    */
    public AuthenticationService(
            UserRepository userRepository,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder, JwtService jwtService, RefreshTokenService refreshTokenService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
    }
    /**
     * Registreert een nieuwe gebruiker.
     *
     * @param name de naam van de gebruiker
     * @param email het e-mailadres van de gebruiker
     * @param password het wachtwoord van de gebruiker
     * @param companyCode de bedrijfsnummer van de gebruiker
     * @param vat het btw-nummer van de gebruiker
     * @param eori het EORI-nummer van de gebruiker
     * @param phoneNumber het telefoonnummer van de gebruiker
     * @param companyName de bedrijfsnaam van de gebruiker
     * @param streetName de straatnaam van de gebruiker
     * @param houseNumber het huisnummer van de gebruiker
     * @param city de stad van de gebruiker
     * @param country het land van de gebruiker
     * @param status de status van de gebruiker
     * @return de geregistreerde gebruiker
    */
    public User signup(String name, String email, String password, String companyCode, String vat, String eori,
            String phoneNumber, String companyName, String streetName, String houseNumber, String city, String country,
            User.Status status) {
        return userRepository.save(new User(name, email, passwordEncoder.encode(password),
                companyCode, vat, eori, phoneNumber, companyName, streetName, houseNumber,
                city, country, status, "user"));
    }
    /**
     * Authenticeert een gebruiker.
     *
     * @param email het e-mailadres van de gebruiker
     * @param password het wachtwoord van de gebruiker
     * @return de geauthenticeerde gebruiker
    */
    public User authenticate(String email, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));

        return userRepository.findByEmail(email).orElseThrow();
    }
    /**
     * Genereert een AuthenticateResponseDTO voor een geauthenticeerde gebruiker.
     *
     * @param user de geauthenticeerde gebruiker
     * @param usedRefreshToken het gebruikte verfrissingstoken
     * @return de authenticatierespons DTO
    */
    public AuthenticateResponseDTO generateAuthenticateResponseDTO(User user, RefreshToken usedRefreshToken) {
        String jwtAccessToken = jwtService.generateToken(user);
        long expirationTime = jwtService.getExpirationTime();
        String role = user.getRole();
        String userName = user.getFullName();

        String jwtRefreshToken = jwtService.generateRefreshToken(user);
        long refreshTokenExpirationTime = jwtService.getRefreshTokenExpirationTime();

        RefreshToken newRefreshToken = new RefreshToken(jwtRefreshToken, false, usedRefreshToken, null);
        if (usedRefreshToken != null) {
            usedRefreshToken.setUsed(true);
            usedRefreshToken.setChildRefreshToken(newRefreshToken);
        }
        refreshTokenService.save(newRefreshToken);

        return new AuthenticateResponseDTO(jwtAccessToken, expirationTime, jwtRefreshToken, role,
                refreshTokenExpirationTime, userName);
    }
}
