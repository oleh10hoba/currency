package com.example.currency.infrastructure.repository;

import com.example.currency.domain.Currency;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaCurrencyRepository extends JpaRepository<Currency, UUID>, JpaSpecificationExecutor<Currency> {
    Optional<Currency> findById(@NotNull UUID id);

    boolean existsByName(String name);

    boolean existsBySymbol(String symbol);
}
