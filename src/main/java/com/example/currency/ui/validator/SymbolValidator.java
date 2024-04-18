package com.example.currency.ui.validator;

import com.example.currency.application.query.ICurrencyQuery;
import com.example.currency.ui.dto.request.SymbolRequestDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

import java.util.Currency;

@RequiredArgsConstructor
public class SymbolValidator implements ConstraintValidator<SymbolConstraint, SymbolRequestDto> {
    private final ICurrencyQuery currencyQuery;
    private final static String INVALID = "_invalid";
    private final static String NOT_AVAILABLE = "_not_available";
    private final static String INVALID_LENGTH = "_invalid_length";
    private final static String ERROR = "error.";
    private final static String SYMBOL = "symbol";

    @Override
    public boolean isValid(SymbolRequestDto dto, ConstraintValidatorContext constraintValidatorContext) {
        if (dto.getSymbol() != null && !dto.getSymbol().isBlank()) {
            if (dto.getSymbol().length() != 3) {
                constraintValidatorContext.disableDefaultConstraintViolation();
                constraintValidatorContext.buildConstraintViolationWithTemplate(ERROR + SYMBOL + INVALID_LENGTH)
                        .addPropertyNode(SYMBOL)
                        .addConstraintViolation();
                return false;
            } else {
                try {
                    Currency.getInstance(dto.getSymbol());
                    if (!currencyQuery.existsBySymbol(dto.getSymbol().toUpperCase())) {
                        constraintValidatorContext.disableDefaultConstraintViolation();
                        constraintValidatorContext.buildConstraintViolationWithTemplate(ERROR + SYMBOL + NOT_AVAILABLE)
                                .addPropertyNode(SYMBOL)
                                .addConstraintViolation();
                        return false;
                    }
                } catch (IllegalArgumentException e) {
                    constraintValidatorContext.disableDefaultConstraintViolation();
                    constraintValidatorContext.buildConstraintViolationWithTemplate(ERROR + SYMBOL + INVALID)
                            .addPropertyNode(SYMBOL)
                            .addConstraintViolation();
                    return false;
                }
            }
        }
        return true;
    }
}