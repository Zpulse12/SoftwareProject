package com.example.klantenportaal.jpa;

import com.example.klantenportaal.models.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface voor het uitvoeren van databasebewerkingen op de
 * gebruikerstabel.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    /**
     * Zoekt een gebruiker op basis van het e-mailadres.
     *
     * @param email het e-mailadres van de gebruiker.
     * @return een optionele gebruiker, leeg als deze niet gevonden wordt.
     */
    Optional<User> findByEmail(String email);

    /**
     * Zoekt de bedrijfscode van een gebruiker op basis van de gebruikers-ID.
     *
     * @param id de ID van de gebruiker.
     * @return de bedrijfscode van de gebruiker.
     */
    @Query("SELECT u.companyCode FROM User u WHERE u.id = ?1")
    String findCompanyCodeById(Integer id);
}
