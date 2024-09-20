package com.example.klantenportaal.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.klantenportaal.jpa.UserRepository;

/**
 * De configuratieklasse voor de applicatie.
 * Deze klasse definieert de beans die in de applicatiecontext worden
 * geregistreerd.
 */
@Configuration
public class ApplicationConfiguration {
    private final UserRepository userRepository;

    /**
     * Construeert een ApplicationConfiguration met de opgegeven userRepository.
     * 
     * @param userRepository de repository die word gebruikt voor de interactie met
     *                       de user data
     */
    public ApplicationConfiguration(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Definieert een {@link UserDetailsService} bean die de gebruikersgegevens
     * laadt op basis van de opgegeven gebruikersnaam.
     *
     * @return een instance van {@link UserDetailsService}.
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    /**
     * Definieert een {@link PasswordEncoder} bean die de BCrypt hashing functie
     * gebruikt.
     *
     * @return een instance van {@link BCryptPasswordEncoder}.
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Definieert een {@link AuthenticationManager} bean die wordt gebruikt voor
     * authenticatiebeheer.
     *
     * @param config de configuratie voor authenticatie
     * @return een instance van {@link AuthenticationManager}.
     * @throws Exception indien er een fout optreedt tijdens het verkrijgen van de
     *                   AuthenticationManager
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Definieert een {@link AuthenticationProvider} bean die wordt gebruikt voor
     * authenticatievoorziening.
     *
     * @return een instance van {@link AuthenticationProvider}.
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }
}
