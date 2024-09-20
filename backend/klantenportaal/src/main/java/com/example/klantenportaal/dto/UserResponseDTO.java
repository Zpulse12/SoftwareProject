package com.example.klantenportaal.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UserResponseDTO(
                @JsonProperty("id") Integer id,
                @JsonProperty("fullName") String fullName,
                @JsonProperty("email") String email) {
}
