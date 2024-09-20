package com.example.klantenportaal.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * Beveiligingsconfiguratie voor de applicatie.
 * Deze klasse configureert de algemene beveiligingsinstellingen zoals
 * authenticatie, autorisatie en CORS.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(jsr250Enabled = true)
public class SecurityConfiguration {
    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * Construeert een SecurityConfiguration met de opgegeven filters en providers.
     *
     * @param jwtAuthenticationFilter de JWT authenticatiefilter
     * @param authenticationProvider  de authenticatieprovider
     */
    public SecurityConfiguration(
            JwtAuthenticationFilter jwtAuthenticationFilter,
            AuthenticationProvider authenticationProvider) {
        this.authenticationProvider = authenticationProvider;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    /**
     * Definieert de {@link SecurityFilterChain} bean die de beveiligingsfilterketen
     * configureert.
     *
     * @param http de HttpSecurity configuratie
     * @return een instance van {@link SecurityFilterChain}.
     * @throws Exception indien er een fout optreedt bij de configuratie van de
     *                   beveiligingsfilterketen
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);

        http.authorizeHttpRequests(request -> {
            request.requestMatchers("/api/auth/**", "/api/register").permitAll();
            request.anyRequest().authenticated();
        });

        http.sessionManagement(sessionAuthenticationStrategy -> sessionAuthenticationStrategy
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.authenticationProvider(authenticationProvider);

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        http.cors(Customizer.withDefaults());

        return http.build();
    }

    /**
     * Definieert de {@link CorsConfigurationSource} bean die de CORS configuratie
     * specificeert.
     *
     * @return een instance van {@link CorsConfigurationSource}.
     */
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(
                List.of("http://localhost:4200", "https://sw02.devops-ap.be", "http://127.0.0.1:4200",
                        "http://localhost", "https://localhost"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

}
