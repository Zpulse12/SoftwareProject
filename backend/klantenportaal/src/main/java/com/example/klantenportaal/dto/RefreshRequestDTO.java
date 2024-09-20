package com.example.klantenportaal.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record RefreshRequestDTO(
                @JsonProperty("token") String token) {
}
