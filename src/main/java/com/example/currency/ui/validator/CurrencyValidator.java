package com.example.currency.ui.validator;

import com.example.currency.ui.dto.request.AddCurrencyRequestDto;
import com.example.currency.application.query.ICurrencyQuery;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

import java.util.Currency;

@RequiredArgsConstructor
public class CurrencyValidator implements ConstraintValidator<CurrencyConstraint, AddCurrencyRequestDto> {
    private final ICurrencyQuery currencyQuery;
    private final static String ALREADY_EXISTS = "_already_exists";
    private final static String INVALID = "_invalid";
    private final static String INVALID_LENGTH = "_invalid_length";
    private final static String REQUIRED = "_required";
    private final static String ERROR = "error.";
    private final static String NAME = "name";
    private final static String SYMBOL = "symbol";

    @Override
    public boolean isValid(AddCurrencyRequestDto dto, ConstraintValidatorContext constraintValidatorContext) {
        String fieldName = NAME;
        boolean isValid = true;
        if (dto.getName() != null && !dto.getName().isBlank()) {
            if (dto.getName().length() < 2 || dto.getName().length() > 63) {
                isValid = false;
                constraintValidatorContext.disableDefaultConstraintViolation();
                constraintValidatorContext.buildConstraintViolationWithTemplate(ERROR + fieldName + INVALID_LENGTH)
                        .addPropertyNode(fieldName)
                        .addConstraintViolation();
            } else if (currencyQuery.existsByName(dto.getName())) {
                isValid = false;
                constraintValidatorContext.disableDefaultConstraintViolation();
                constraintValidatorContext.buildConstraintViolationWithTemplate(ERROR + fieldName + ALREADY_EXISTS)
                        .addPropertyNode(fieldName)
                        .addConstraintViolation();
            }
        }
        fieldName = SYMBOL;
        if (dto.getSymbol() != null && !dto.getSymbol().isBlank()) {
            if (dto.getSymbol().length() != 3) {
                isValid = false;
                constraintValidatorContext.disableDefaultConstraintViolation();
                constraintValidatorContext.buildConstraintViolationWithTemplate(ERROR + fieldName + INVALID_LENGTH)
                        .addPropertyNode(fieldName)
                        .addConstraintViolation();
            } else {
                try {
                    Currency.getInstance(dto.getSymbol());
                    if (currencyQuery.existsBySymbol(dto.getSymbol().toUpperCase())) {
                        isValid = false;
                        constraintValidatorContext.disableDefaultConstraintViolation();
                        constraintValidatorContext.buildConstraintViolationWithTemplate(ERROR + fieldName + ALREADY_EXISTS)
                                .addPropertyNode(fieldName)
                                .addConstraintViolation();
                    }
                } catch (IllegalArgumentException e) {
                    isValid = false;
                    constraintValidatorContext.disableDefaultConstraintViolation();
                    constraintValidatorContext.buildConstraintViolationWithTemplate(ERROR + fieldName + INVALID)
                            .addPropertyNode(fieldName)
                            .addConstraintViolation();
                }
            }
        }
        return isValid;
    }
}