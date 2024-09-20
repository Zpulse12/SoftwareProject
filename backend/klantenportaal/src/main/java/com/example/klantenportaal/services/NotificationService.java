package com.example.klantenportaal.services;

import com.example.klantenportaal.dto.NotificationDTO;
import com.example.klantenportaal.jpa.NotificationRepository;
import com.example.klantenportaal.models.Notification;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
/**
 * Service voor het beheren van notificaties.
 */
@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    /**
     * Construeert een NotificationService met de opgegeven NotificationRepository.
     *
     * @param notificationRepository de repository die wordt gebruikt voor het beheren van notificaties
    */
    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }
    /**
     * Haalt alle notificaties op.
     *
     * @return een lijst van alle notificaties
    */
    public List<NotificationDTO> getAllNotifications() {
        return notificationRepository.findAll().stream().map(this::convertToDTO).toList();
    }
    /**
     * Haalt notificaties op voor een bedrijf op basis van het bedrijfsnummer.
     *
     * @param companyCode het bedrijfsnummer van het bedrijf
     * @return een lijst van notificaties voor het bedrijf
    */
    public List<NotificationDTO> getNotificationsByCompanyCode(String companyCode) {
        return notificationRepository.findByCompanyCode(companyCode).stream().map(this::convertToDTO).toList();
    }
    /**
     * Controleert of een notificatie bestaat op basis van het referentienummer.
     *
     * @param referenceNumber het referentienummer van de notificatie
     * @return true als de notificatie bestaat, anders false
    */
    public boolean notificationExists(String referenceNumber) {
        return notificationRepository.findByReferenceNumber(referenceNumber).isPresent();
    }
    /**
     * Slaat een notificatie op.
     *
     * @param notificationDTO de notificatie die moet worden opgeslagen
     * @return de opgeslagen notificatie
    */
    public NotificationDTO saveNotification(NotificationDTO notificationDTO) {
        return convertToDTO(notificationRepository.save(convertFromDTO(notificationDTO)));
    }
    /**
     * Verwijdert een notificatie op basis van het ID.
     *
     * @param id het ID van de notificatie die moet worden verwijderd
     * @return de verwijderde notificatie
    */
    public NotificationDTO deleteNotification(Long id) {
        Notification notification = notificationRepository.findById(id).orElse(null);
        if (notification != null) {
            notificationRepository.delete(notification);
        }
        return convertToDTO(notification);
    }
    /**
     * Haalt een notificatie op op basis van het ID.
     *
     * @param id het ID van de notificatie
     * @return de notificatie
    */
    public NotificationDTO getNotificationById(Long id) {
        Optional<Notification> notificationOptional = notificationRepository.findById(id);
        if (notificationOptional.isPresent()) {
            return convertToDTO(notificationOptional.get());
        } else {
            return null;
        }
    }
    public NotificationDTO convertToDTO(Notification notification) {
        return new NotificationDTO(
                notification.getId(),
                notification.getShipName(),
                notification.getDepartureStatus(),
                notification.getDepartureTime(),
                notification.getDestination(),
                notification.getReferenceNumber(),
                notification.getCompanyCode());
    }

    public Notification convertFromDTO(NotificationDTO notificationDTO) {
        return new Notification(
                notificationDTO.shipName(),
                notificationDTO.departureStatus(),
                notificationDTO.departureTime(),
                notificationDTO.destination(),
                notificationDTO.referenceNumber(),
                notificationDTO.companyCode());
    }
}