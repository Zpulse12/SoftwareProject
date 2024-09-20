package com.example.klantenportaal.dto;

import com.example.klantenportaal.models.User;

public record RegistrationUserDTO(
        Integer id,
        String fullName,
        String companyName,
        String vat,
        String eori,
        String companyCode,
        User.Status status,
        String role) {
}
