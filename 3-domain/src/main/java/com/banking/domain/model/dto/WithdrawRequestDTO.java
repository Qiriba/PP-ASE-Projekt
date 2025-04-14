package com.banking.domain.model.dto;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.UUID;

public record WithdrawRequestDTO(UUID accountId, BigDecimal amount, Currency currency) { }
