package com.example.klantenportaal.jpa;

import com.example.klantenportaal.models.RefreshToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface voor het uitvoeren van databasebewerkingen op de refresh
 * token tabel.
 */
@Repository
public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Long> {

    /**
     * Zoekt een refresh token op basis van de token.
     *
     * @param token het token van de refresh token.
     * @return een optionele refresh token, leeg als deze niet gevonden wordt.
     */
    Optional<RefreshToken> findByToken(String token);
}
