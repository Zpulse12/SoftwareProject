package com.example.klantenportaal.seeders;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.klantenportaal.jpa.UserRepository;
import com.example.klantenportaal.models.User;

/**
 * Seeder component voor het initialiseren van een admin en user account in de
 * database.
 */
@Component
public class UserSeeder implements CommandLineRunner {
    private static final String ADMIN_NAME = "admin";
    private static final String ADMIN_EMAIL = "admin@gmail.com";
    private static final String ADMIN_PASSWORD = "user123";
    private static final String ADMIN_COMPANY_CODE = "Steelduxx";

    private static final String USER_NAME = "user";
    private static final String USER_EMAIL = "user@gmail.com";
    private static final String USER_PASSWORD = "user123";
    private static final String USER_COMPANY_CODE = "SOF2";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Construeert een nieuwe UserSeeder met de opgegeven UserRepository en
     * PasswordEncoder.
     *
     * @param userRepository  de repository voor gebruikersgegevens
     * @param passwordEncoder de encoder voor het coderen van wachtwoorden
     */
    public UserSeeder(UserRepository userRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Voert de seeder uit om gebruikersgegevens te initialiseren.
     *
     * @param args de opdrachtregelargumenten
     * @throws Exception indien er een fout optreedt tijdens het uitvoeren van de
     *                   seeder
     */
    @Override
    public void run(String... args) throws Exception {
        if (userRepository.findByEmail(ADMIN_EMAIL).isEmpty()) {
            userRepository.save(new User(ADMIN_NAME, ADMIN_EMAIL, passwordEncoder.encode(ADMIN_PASSWORD),
                    ADMIN_COMPANY_CODE, "BE 0425.069.935", "BE156535846732", "+32 3 234 64 40", "Steelduxx",
                    "Duboisstraat", "50", "Antwerpen", "Belgium", User.Status.APPROVED, "admin"));
        }
        if (userRepository.findByEmail(USER_EMAIL).isEmpty()) {
            userRepository.save(new User(USER_NAME, USER_EMAIL, passwordEncoder.encode(USER_PASSWORD),
                    USER_COMPANY_CODE, "BE 2691.036.159", "BE953658154836", "+32 499 36 95 35", "BobSteel",
                    "Voetbalstraat", "80", "Antwerpen", "Belgium", User.Status.APPROVED, "user"));
        }
    }
}
