package com.example.klantenportaal.dto;

public record ProductDetailDTO(
        String hsCode,
        String name,
        int quantity,
        long weight,
        String containerNumber,
        String containerSize,
        String containerType) {
}
