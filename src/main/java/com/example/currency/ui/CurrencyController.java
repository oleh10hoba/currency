package com.example.currency.ui;

import com.example.currency.ui.dto.request.AddCurrencyRequestDto;
import com.example.currency.application.query.ICurrencyQuery;
import com.example.currency.application.service.CurrencyService;
import com.example.currency.domain.GridResult;
import com.example.currency.ui.dto.response.CurrencyResponseDto;

import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/currency")
@RequiredArgsConstructor
@OpenAPIDefinition
public class CurrencyController {
    private final CurrencyService currencyService;
    private final ICurrencyQuery currencyQuery;

    @ApiOperation("Add currency to the project")
    @PostMapping(value = "/add")
    public ResponseEntity<Void> addCurrency(@Valid @RequestBody AddCurrencyRequestDto dto) {
        currencyService.add(dto);
        return ResponseEntity.ok().build();
    }

    @ApiOperation("Get a list of currencies used in the project")
    @GetMapping(value = "/list")
    public ResponseEntity<GridResult<CurrencyResponseDto>> currencyList(@RequestParam(defaultValue = "10") int pageSize,
                                                                        @RequestParam(defaultValue = "0") int pageNumber ) {
        return ResponseEntity.ok(currencyQuery.getCurrencyList(pageSize, pageNumber));
    }
}
