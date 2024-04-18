package com.example.currency.application.service;

import com.example.currency.ui.dto.request.AddCurrencyRequestDto;
import com.example.currency.domain.Currency;
import com.example.currency.infrastructure.repository.JpaCurrencyRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@Log4j2
@RequiredArgsConstructor
public class CurrencyService {

    private final JpaCurrencyRepository jpaCurrencyRepository;

    @Transactional
    public void add(AddCurrencyRequestDto dto) {
        Currency currency = Currency.add(
                UUID.randomUUID(),
                dto.getName(),
                dto.getSymbol(),
                dto.getIsActive(),
                Instant.now());
        jpaCurrencyRepository.save(currency);
    }
}