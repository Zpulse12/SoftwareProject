package com.example.klantenportaal.dto;

import com.example.klantenportaal.models.User;

public record RegisterUserRequestDTO(
        String email,
        String password,
        String fullName,
        String companyCode,
        String companyName,
        String vat,
        String eori,
        String phoneNumber,
        String streetName,
        String houseNumber,
        String city,
        String country,
        User.Status status) {
}
