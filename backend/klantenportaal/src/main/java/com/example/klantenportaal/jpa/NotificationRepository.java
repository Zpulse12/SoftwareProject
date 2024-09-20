package com.example.klantenportaal.jpa;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.klantenportaal.models.Notification;

/**
 * Repository interface voor het uitvoeren van databasebewerkingen op de
 * notificatietabel.
 */
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    /**
     * Zoekt een notificatie op basis van het referentienummer.
     *
     * @param referenceNumber het referentienummer van de notificatie.
     * @return een optionele notificatie, leeg als deze niet gevonden wordt.
     */
    Optional<Notification> findByReferenceNumber(String referenceNumber);

    /**
     * Zoekt notificaties op basis van de companyCode.
     *
     * @param companyCode de companyCode.
     * @return een lijst van notificaties.
     */
    List<Notification> findByCompanyCode(String companyCode);
}
