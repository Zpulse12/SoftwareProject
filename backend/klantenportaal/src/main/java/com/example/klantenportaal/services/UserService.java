package com.example.klantenportaal.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.klantenportaal.jpa.UserRepository;
import com.example.klantenportaal.models.User;
/**
 * Service voor het beheren van gebruikers.
 */
@Service
public class UserService {

    private final UserRepository userRepository;
    /**
     * Construeert een UserService met de opgegeven UserRepository.
     * @param userRepository de repository die wordt gebruikt voor het beheren van gebruikers
    */
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    /**
     * Haalt alle gebruikers op.
     *
     * @return een lijst van alle gebruikers
    */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
