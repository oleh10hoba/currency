package com.example.currency.ui.dto.request;

import com.example.currency.ui.validator.SymbolConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
@SymbolConstraint
public class SymbolRequestDto {
    @NotBlank(message = "error.symbol_required")
    private String symbol;
}