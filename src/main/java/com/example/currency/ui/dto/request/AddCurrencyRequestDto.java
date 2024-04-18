package com.example.currency.ui.dto.request;

import com.example.currency.ui.validator.CurrencyConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;



@Setter
@Getter
@CurrencyConstraint
public class AddCurrencyRequestDto {
    @NotBlank(message = "error.name_required")
    private String name;
    @NotBlank(message = "error.symbol_required")
    private String symbol;
    private Boolean isActive;
}