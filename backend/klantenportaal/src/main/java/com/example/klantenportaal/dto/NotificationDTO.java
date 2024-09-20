package com.example.klantenportaal.dto;

public record NotificationDTO(
        Long id,
        String shipName,
        String departureStatus,
        String departureTime,
        String destination,
        String referenceNumber,
        String companyCode) {
}
