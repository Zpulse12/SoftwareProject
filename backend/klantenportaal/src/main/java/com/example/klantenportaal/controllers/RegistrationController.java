package com.example.klantenportaal.controllers;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.example.klantenportaal.dto.RegistrationUserDTO;
import com.example.klantenportaal.models.User;
import com.example.klantenportaal.services.RegisterService;

import jakarta.annotation.security.RolesAllowed;
/**
 * Controller voor het beheren van registraties.
 */
@RestController
@RequestMapping("api/registraties")
public class RegistrationController {

  private RegisterService registerService;
  /**
   * Construeert een RegistrationController met de opgegeven RegisterService.
   *
   * @param registerService de service die wordt gebruikt voor het beheren van registraties
  */
  public RegistrationController(RegisterService registerService) {
    this.registerService = registerService;
  }
  /**
   * Haalt alle geregistreerde gebruikers op.
   *
   * @param authentication het authenticatietoken van de huidige gebruiker
   * @return een lijst van geregistreerde gebruikers
  */
  @GetMapping
  @RolesAllowed("admin")
  public List<RegistrationUserDTO> getAllUsers(Authentication authentication) {
    return this.registerService.findAll();
  }
  /**
   * Werkt de registratie status van een gebruiker bij.
   *
   * @param authentication het authenticatietoken van de huidige gebruiker
   * @param id de ID van de gebruiker waarvan de status moet worden bijgewerkt
   * @param status de nieuwe status
   * @return de bijgewerkte registratie gebruiker DTO
  */
  @PutMapping("/{id}/status")
  @RolesAllowed("admin")
  public RegistrationUserDTO updateRegistratieStatus(Authentication authentication, @PathVariable int id,
      @RequestBody String status) {
    return registerService.updateStatus(id, status);
  }
  /**
   * Werkt de details van een geregistreerde gebruiker bij.
   *
   * @param authentication het authenticatietoken van de huidige gebruiker
   * @param id de ID van de gebruiker waarvan de details moeten worden bijgewerkt
   * @param clientData het dto met de nieuwe gegevens van de gebruiker
   * @return de bijgewerkte registratie gebruiker DTO
  */
  @PutMapping("/{id}/details")
  @RolesAllowed("admin")
  public RegistrationUserDTO updateClient(Authentication authentication, @PathVariable int id,
      @RequestBody RegistrationUserDTO clientData) {
    User existingClient = registerService.findById(id);
    existingClient.setVat(clientData.vat());
    existingClient.setEori(clientData.eori());
    existingClient.setCompanyCode(clientData.companyCode());
    return registerService.save(existingClient);
  }
  /**
   * Verwijdert een geregistreerde gebruiker.
   *
   * @param authentication het authenticatietoken van de huidige gebruiker
   * @param id de ID van de gebruiker die moet worden verwijderd
  */
  @DeleteMapping("/{id}")
  @RolesAllowed("admin")
  public void deleteRegistration(Authentication authentication, @PathVariable int id) {

    registerService.deleteRegistration(id);

  }
}
