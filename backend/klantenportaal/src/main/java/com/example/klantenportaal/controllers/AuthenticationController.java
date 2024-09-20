package com.example.klantenportaal.controllers;

import com.example.klantenportaal.models.User;
import com.example.klantenportaal.models.RefreshToken;
import com.example.klantenportaal.dto.LoginRequestDTO;
import com.example.klantenportaal.dto.AuthenticateResponseDTO;
import com.example.klantenportaal.dto.RefreshRequestDTO;
import com.example.klantenportaal.dto.RegisterUserRequestDTO;
import com.example.klantenportaal.dto.UserResponseDTO;
import com.example.klantenportaal.jpa.UserRepository;
import com.example.klantenportaal.services.AuthenticationService;
import com.example.klantenportaal.services.JwtService;
import com.example.klantenportaal.services.RefreshTokenService;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
/**
 * Controller voor authenticatie.
 */
@RequestMapping("/api/auth")
@RestController
public class AuthenticationController {
    private final JwtService jwtService;

    private final AuthenticationService authenticationService;

    private final RefreshTokenService refreshTokenService;

    private final UserRepository userRepository;

    /**
     * Construeert een AuthenticationController met de opgegeven services en repository.
     *
     * @param jwtService de service voor het beheren van JWT-tokens
     * @param authenticationService de service voor authenticatie
     * @param refreshTokenService de service voor het beheren van refreshtoken
     * @param userRepository de repository voor gebruikers
     */
    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService,
            RefreshTokenService refreshTokenService, UserRepository userRepository) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
        this.refreshTokenService = refreshTokenService;
        this.userRepository = userRepository;
    }

    /**
     * Registreert een nieuwe gebruiker.
     *
     * @param dto het dto met de gegevens van de gebruiker
     * @return een ResponseEntity met de geregistreerde gebruiker
     */
    @PostMapping("/signup")
    public ResponseEntity<UserResponseDTO> register(@RequestBody RegisterUserRequestDTO dto) {
        User user = authenticationService.signup(
                dto.fullName(),
                dto.email(),
                dto.password(),
                dto.companyCode(),
                dto.vat(),
                dto.eori(),
                dto.phoneNumber(),
                dto.companyName(),
                dto.streetName(),
                dto.houseNumber(),
                dto.city(),
                dto.country(),
                dto.status());

        return ResponseEntity.ok(new UserResponseDTO(user.getId(), user.getFullName(), user.getEmail()));
    }

    /**
     * Logt een gebruiker in.
     *
     * @param dto het dto met de inloggegevens
     * @return een ResponseEntity met de authenticatierespons of een Unauthorized status
     */
    @PostMapping("/login")
    public ResponseEntity<AuthenticateResponseDTO> login(@RequestBody LoginRequestDTO dto) {
        User authenticatedUser = authenticationService.authenticate(dto.email(), dto.password());
        if (authenticatedUser == null || !authenticatedUser.isApproved()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return ResponseEntity.ok(authenticationService.generateAuthenticateResponseDTO(authenticatedUser, null));
    }

    /**
     * Vernieuwt een JWT-token.
     *
     * @param dto het dto met de refreshtoken
     * @return een ResponseEntity met de nieuwe authenticatierespons
     * @throws EntityNotFoundException als het refreshtoken niet bestaat
     * @throws IllegalArgumentException als het refreshtoken al gebruikt is
     */
    @PostMapping("/refresh")
    public ResponseEntity<AuthenticateResponseDTO> refresh(@RequestBody RefreshRequestDTO dto) {
        RefreshToken usedRefreshToken = refreshTokenService.findByToken(dto.token())
                .orElseThrow(() -> new EntityNotFoundException("RefreshToken bestaat niet"));
        if (usedRefreshToken.isUsed()) {
            refreshTokenService.delete(usedRefreshToken);
            throw new IllegalArgumentException("RefreshToken is al gebruikt");
        }
        return ResponseEntity.ok(authenticationService.generateAuthenticateResponseDTO(
                userRepository.findByEmail(jwtService.extractUsernameFromRefreshToken(dto.token())).get(),
                usedRefreshToken));
    }
}
