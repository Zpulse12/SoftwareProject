package com.example.klantenportaal.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
/**
 * Service voor het genereren en valideren van JWT-tokens.
*/
@Service
public class JwtService {

    @Value("${security.jwt.secret-key}")
    private String secretKey;

    @Value("${security.jwt.refresh-token-secret-key}")
    private String refreshTokenSecretKey;

    @Value("${security.jwt.expiration-time}")
    private long jwtExpiration;

    @Value("${security.jwt.refresh-token-expiration-time}")
    private long refreshTokenExpiration;
    /**
     * Haalt de gebruikersnaam op uit het JWT-token.
     *
     * @param token het JWT-token
     * @return de gebruikersnaam
    */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }
      /**
     * Haalt een specifieke claim op uit het JWT-token.
     *
     * @param token het JWT-token
     * @param claimsResolver de functie die de gewenste claim ophaalt
     * @param <T> het type van de claim
     * @return de gewenste claim
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        return claimsResolver.apply(extractAllClaims(token));
    }
    /**
     * Haalt de gebruikersnaam op uit het refreshtoken.
     *
     * @param token het refreshtoken
     * @return de gebruikersnaam
     */
    public String extractUsernameFromRefreshToken(String token) {
        return extractUsernameFromRefreshToken(token, Claims::getSubject);
    }
    /**
     * Haalt een specifieke claim op uit het refreshtoken.
     *
     * @param token het refreshtoken
     * @param claimsResolver de functie die de gewenste claim ophaalt
     * @param <T> het type van de claim
     * @return de gewenste claim
     */
    public <T> T extractUsernameFromRefreshToken(String token, Function<Claims, T> claimsResolver) {
        return claimsResolver.apply(extractAllClaimsFromRefreshToken(token));
    }
    /**
     * Genereert een JWT-token voor de opgegeven gebruiker.
     *
     * @param userDetails de gegevens van de gebruiker
     * @return het JWT-token
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", userDetails.getAuthorities().stream()
                .map(authority -> authority.getAuthority())
                .collect(Collectors.toList()));
        return generateToken(claims, userDetails);
    }
    /**
     * Genereert een JWT-token voor de opgegeven gebruiker met extra claims.
     *
     * @param extraClaims de extra claims
     * @param userDetails de gegevens van de gebruiker
     * @return het JWT-token
     */
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return buildToken(extraClaims, userDetails, jwtExpiration);
    }
    /**
     * Genereert een refreshtoken voor de opgegeven gebruiker.
     *
     * @param userDetails de gegevens van de gebruiker
     * @return het refreshtoken
     */
    public String generateRefreshToken(UserDetails userDetails) {
        return generateRefreshToken(new HashMap<>(), userDetails);
    }
    /**
     * Genereert een refreshtoken voor de opgegeven gebruiker met extra claims.
     *
     * @param extraClaims de extra claims
     * @param userDetails de gegevens van de gebruiker
     * @return het refreshtoken
     */
    public String generateRefreshToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return buildRefreshToken(extraClaims, userDetails, refreshTokenExpiration);
    }
    /**
     * Haalt de vervaltijd van het JWT-token op.
     *
     * @return de vervaltijd van het JWT-token
     */
    public long getExpirationTime() {
        return jwtExpiration;
    }
    /**
     * Haalt de vervaltijd van het refreshtoken op.
     *
     * @return de vervaltijd van het refreshtoken
     */
    public long getRefreshTokenExpirationTime() {
        return refreshTokenExpiration;
    }
    private String buildToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails,
            long expiration) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(convertToDate(LocalDateTime.now()))
                .setExpiration(convertToDate(LocalDateTime.now().plus(expiration, ChronoUnit.MILLIS)))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    
    private String buildRefreshToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails,
            long expiration) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(convertToDate(LocalDateTime.now()))
                .setExpiration(convertToDate(LocalDateTime.now().plus(expiration, ChronoUnit.MILLIS)))
                .signWith(getSignInKeyFromRefreshKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    /**
     * Controleert of het JWT-token geldig is voor de opgegeven gebruiker.
     *
     * @param token het JWT-token
     * @param userDetails de gegevens van de gebruiker
     * @return true als het token geldig is, anders false
    */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }
    /**
     * Controleert of het refreshtoken geldig is voor de opgegeven gebruiker.
     *
     * @param token het refreshtoken
     * @param userDetails de gegevens van de gebruiker
     * @return true als het token geldig is, anders false
    */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).isBefore(LocalDateTime.now());
    }
    /**
     * Haalt de vervaltijd van het JWT-token op.
     *
     * @param token het JWT-token
     * @return de vervaltijd van het JWT-token
    */
    private LocalDateTime extractExpiration(String token) {
        return convertFromDate(extractClaim(token, Claims::getExpiration));
    }
    /**
     * Haalt de vervaltijd van het refreshtoken op.
     *
     * @param token het refreshtoken
     * @return de vervaltijd van het refreshtoken
    */
    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Claims extractAllClaimsFromRefreshToken(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKeyFromRefreshKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }

    private Key getSignInKeyFromRefreshKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(refreshTokenSecretKey));
    }

    private Date convertToDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    private LocalDateTime convertFromDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }
}
