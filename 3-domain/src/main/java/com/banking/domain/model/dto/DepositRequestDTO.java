package com.banking.domain.model.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record DepositRequestDTO(UUID accountId, BigDecimal amount) { }