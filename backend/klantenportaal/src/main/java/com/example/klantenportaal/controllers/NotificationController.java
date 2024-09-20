package com.example.klantenportaal.controllers;

import com.example.klantenportaal.dto.NotificationDTO;
import com.example.klantenportaal.models.User;
import com.example.klantenportaal.services.NotificationService;

import jakarta.annotation.security.RolesAllowed;

import java.util.List;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
/**
 * Controller voor het beheren van meldingen.
 */
@RestController
@RequestMapping("api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    /**
     * Construeert een NotificationController met de opgegeven NotificationService.
     *
     * @param notificationService de service die wordt gebruikt voor het beheren van meldingen
    */
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    /**
     * Haalt alle meldingen op. Dit eindpunt is alleen toegankelijk voor gebruikers met de rol "admin".
     *
     * @param authentication het authenticatietoken van de huidige gebruiker
     * @return een lijst van alle meldingen
    */
    @GetMapping()
    @RolesAllowed("admin")
    public List<NotificationDTO> getAllNotifications(Authentication authentication) {
        return notificationService.getAllNotifications();
    }
    /**
     * Haalt meldingen op voor de momenteel geauthenticeerde gebruiker.
     *
     * @param authentication het authenticatietoken van de huidige gebruiker
     * @return een lijst van meldingen voor de huidige gebruiker
    */
    @GetMapping("/user")
    public List<NotificationDTO> getNotificationsForCurrentUser(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return notificationService.getNotificationsByCompanyCode(user.getCompanyCode());
    }
    /**
     * Verwijdert een melding op basis van het ID. Verwijderen is alleen toegestaan als de melding behoort tot het bedrijf van de geauthenticeerde gebruiker.
     *
     * @param authentication het authenticatietoken van de huidige gebruiker
     * @param id het ID van de melding die verwijderd moet worden
     * @return de verwijderde melding
     * @throws AccessDeniedException als de gebruiker geen toestemming heeft om de melding te verwijderen
    */
    @DeleteMapping("/{id}")
    public NotificationDTO deleteNotification(Authentication authentication, @PathVariable long id) {
        User user = (User) authentication.getPrincipal();
        NotificationDTO notificationDTO = notificationService.getNotificationById(id);
        if (notificationDTO != null && notificationDTO.companyCode().equals(user.getCompanyCode())) {
            return notificationService.deleteNotification(id);
        } else {
            throw new AccessDeniedException("You do not have permission to delete this notification");
        }
    }
    /**
     * Maakt een nieuwe melding aan. Dit eindpunt is alleen toegankelijk voor gebruikers met de rol "admin".
     *
     * @param authentication het authenticatietoken van de huidige gebruiker
     * @param notificationDTO de melding dto met de details van de aan te maken melding
     * @return de aangemaakte melding
    */
    @PostMapping
    @RolesAllowed("admin")
    public NotificationDTO createNotification(Authentication authentication,
            @RequestBody NotificationDTO notificationDTO) {
        return notificationService.saveNotification(notificationDTO);
    }
}
