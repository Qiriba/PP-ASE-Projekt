package com.banking.domain.model.dto;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.UUID;

public record TransferRequestDTO(UUID fromAccountId, UUID toAccountId, BigDecimal amount, Currency currency) { }
