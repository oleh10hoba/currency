package com.example.currency.application.query;

import com.example.currency.domain.GridResult;
import com.example.currency.ui.dto.response.CurrencyResponseDto;

public interface ICurrencyQuery {

    boolean existsByName(String name);

    boolean existsBySymbol(String symbol);

    GridResult<CurrencyResponseDto> getCurrencyList(int pageSize, int pageNumber);
}