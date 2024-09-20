package com.example.klantenportaal.config;

import com.example.klantenportaal.services.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

/**
 * Filter die JWT authenticatie verzorgt voor inkomende HTTP verzoeken.
 * Deze filter wordt één keer per verzoek uitgevoerd.
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final HandlerExceptionResolver handlerExceptionResolver;

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    /**
     * Construeert een JwtAuthenticationFilter met de opgegeven services.
     *
     * @param jwtService               de service die JWT tokens beheert
     * @param userDetailsService       de service die gebruikersdetails laadt
     * @param handlerExceptionResolver de handler voor het afhandelen van
     *                                 uitzonderingen
     */
    public JwtAuthenticationFilter(
            JwtService jwtService,
            UserDetailsService userDetailsService,
            HandlerExceptionResolver handlerExceptionResolver) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.handlerExceptionResolver = handlerExceptionResolver;
    }

    /**
     * Filtert inkomende verzoeken en voert JWT authenticatie uit.
     *
     * @param request     het HTTP verzoek
     * @param response    het HTTP antwoord
     * @param filterChain de filterketen
     * @throws ServletException indien een servlet-specifieke fout optreedt
     * @throws IOException      indien een I/O fout optreedt
     */
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");

        String jwt = null;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            if (request.getCookies() != null) {
                for (var cookie : request.getCookies()) {
                    if (cookie.getName().equals("JWT_TOKEN"))
                        jwt = cookie.getValue();
                }
            }
            if (jwt == null) {
                filterChain.doFilter(request, response);
                return;
            }
        } else {
            jwt = authHeader.substring(7);
        }

        try {
            final String userEmail = jwtService.extractUsername(jwt);

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (userEmail != null && authentication == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

                if (jwtService.isTokenValid(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities());

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }

            filterChain.doFilter(request, response);
        } catch (Exception exception) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            handlerExceptionResolver.resolveException(request, response, null, exception);
        }
    }
}
