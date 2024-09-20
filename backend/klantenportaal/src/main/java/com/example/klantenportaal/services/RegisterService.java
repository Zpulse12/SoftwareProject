package com.example.klantenportaal.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.klantenportaal.dto.RegistrationUserDTO;
import com.example.klantenportaal.jpa.UserRepository;
import com.example.klantenportaal.models.User;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
/**
 * Service voor het beheren van registraties.
 */
@Service
public class RegisterService {

    private final UserRepository registerRepository;
    /**
     * Construeert een RegisterService met de opgegeven UserRepository.
     * @param registerRepository de repository die wordt gebruikt voor het beheren van registraties
     */
    public RegisterService(UserRepository registerRepository) {
        this.registerRepository = registerRepository;
    }
    /**
     * Haalt alle geregistreerde gebruikers op.
     *
     * @return een lijst van RegistrationUserDTO's die alle geregistreerde gebruikers vertegenwoordigen
    */
    public List<RegistrationUserDTO> findAll() {
        return registerRepository.findAll().stream().map(this::convertToDTO).toList();
    }

    public String getCompanyCode(int id) {
        return registerRepository.findCompanyCodeById(id);
    }
     /**
     * Werkt de status van een geregistreerde gebruiker bij.
     *
     * @param id de ID van de gebruiker waarvan de status moet worden bijgewerkt
     * @param status de nieuwe status
     * @return de bijgewerkte registratie gebruiker DTO
     */
    public RegistrationUserDTO updateStatus(int id, String status) {
        User registratie = registerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Registration not found"));
        try {
            User.Status newStatus = User.Status.valueOf(status.toUpperCase());
            registratie.setStatus(newStatus);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid status value");
        }
        return convertToDTO(registerRepository.save(registratie));
    }
    /**
     * Slaat een nieuwe registratie op.
     *
     * @param registerModel de registratie die moet worden opgeslagen
     * @return de opgeslagen registratie
    */
    @Transactional
    public RegistrationUserDTO save(User registerModel) {
        return convertToDTO(registerRepository.save(registerModel));
    }

    /**
     * Haalt een geregistreerde gebruiker op basis van het ID.
     *
     * @param id de ID van de gebruiker
     * @return de gebruiker
     * @throws EntityNotFoundException als de gebruiker niet wordt gevonden
    */
    public User findById(int id) {
        return registerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Client not found with id: " + id));
    }

    /**
     * Verwijdert een geregistreerde gebruiker op basis van het ID.
     *
     * @param id de ID van de gebruiker die moet worden verwijderd
     * @throws EntityNotFoundException als de gebruiker niet wordt gevonden
    */
    public void deleteRegistration(int id) {
        if (!registerRepository.existsById(id)) {
            throw new EntityNotFoundException("Registration not found with id " + id);
        }
        registerRepository.deleteById(id);
    }

    public RegistrationUserDTO convertToDTO(User user) {
        return new RegistrationUserDTO(
                user.getId(),
                user.getFullName(),
                user.getCompanyName(),
                user.getVat(),
                user.getEori(),
                user.getCompanyCode(),
                user.getStatus(),
                user.getRole());
    }
}
