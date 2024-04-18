package com.example.currency.ui.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = SymbolValidator.class)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface SymbolConstraint {
    String message() default "Invalid symbol";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}