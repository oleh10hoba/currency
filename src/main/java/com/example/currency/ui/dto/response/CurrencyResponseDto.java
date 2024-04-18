package com.example.currency.ui.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CurrencyResponseDto {
    private UUID currencyId;
    private String name;
    private String symbol;
    private boolean isActive;
    private Instant createdAt;
    private Instant editedAt;
    private Instant deletedAt;
}
