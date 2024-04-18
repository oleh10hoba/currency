package com.example.currency.infrastructure.query;

import com.example.currency.application.query.ICurrencyQuery;
import com.example.currency.domain.Currency;
import com.example.currency.domain.GridResult;
import com.example.currency.infrastructure.repository.JpaCurrencyRepository;
import com.example.currency.ui.dto.response.CurrencyResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CurrencyQuery implements ICurrencyQuery {

    private final JpaCurrencyRepository currencyRepository;

    @Override
    public boolean existsByName(String name) {
        return currencyRepository.existsByName(name);
    }

    @Override
    public boolean existsBySymbol(String symbol) {
        return currencyRepository.existsBySymbol(symbol);
    }

    @Override
    public GridResult<CurrencyResponseDto> getCurrencyList(int pageSize, int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Currency> currencyPage = currencyRepository.findAll(pageable);

        List<CurrencyResponseDto> currencyResponseDtoList = currencyPage.getContent()
                .stream()
                .map(currency -> mapCurrency(currency))
                .collect(Collectors.toList());

        return new GridResult<>(currencyResponseDtoList, currencyPage.getNumber(), currencyPage.getSize(), currencyPage.getTotalElements());
    }

    private CurrencyResponseDto mapCurrency(Currency currency) {
        return new CurrencyResponseDto(
                currency.getCurrencyId(),
                currency.getName(),
                currency.getSymbol(),
                currency.isActive(),
                currency.getCreatedAt(),
                currency.getEditedAt(),
                currency.getDeletedAt()
        );
    }
}