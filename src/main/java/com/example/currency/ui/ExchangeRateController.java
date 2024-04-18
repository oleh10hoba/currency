package com.example.currency.ui;

import com.example.currency.ui.dto.request.SymbolRequestDto;
import com.example.currency.ui.dto.response.ExchangeRateResponseDto;
import com.example.currency.application.service.ExchangeRateService;
import io.swagger.annotations.ApiOperation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/currency")
@RequiredArgsConstructor
public class ExchangeRateController {

    private final ExchangeRateService exchangeRateService;

    @ApiOperation("Get exchange rates for a currency")
    @PostMapping(value ="/exchange-rate/list")
    public ResponseEntity<ExchangeRateResponseDto> exchangeRateList(@Valid @RequestBody SymbolRequestDto dto) {
        return ResponseEntity.ok(exchangeRateService.exchangeRateList(dto.getSymbol().toUpperCase()));
    }
}
