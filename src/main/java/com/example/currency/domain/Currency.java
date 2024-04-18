package com.example.currency.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@Table(name = "currency")
public class Currency {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID currencyId;
    private String name;
    private String symbol;
    private boolean isActive;
    private Instant createdAt;
    private Instant activatedAt;
    private Instant editedAt;
    private Instant deletedAt;

    public Currency() {}

    private Currency(UUID currencyId,
                     String name,
                     String symbol,
                     boolean isActive,
                     Instant createdAt,
                     Instant activatedAt) {
        this.currencyId = currencyId;
        this.name = name;
        this.isActive = isActive;
        this.symbol = symbol.toUpperCase()  ;
        this.createdAt = createdAt;
        this.activatedAt = activatedAt;
    }



    public static Currency add(UUID currencyId,
                               String name,
                               String symbol,
                               boolean isActive,
                               Instant createdAt) {
        Instant activatedAt = null;
        if (isActive) {
            activatedAt = createdAt;
        }
        return new Currency(
                currencyId,
                name,
                symbol,
                isActive,
                createdAt,
                activatedAt);
    }
}