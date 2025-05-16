package com.banking.application.ports.in;

import com.banking.domain.model.Account;
import com.banking.domain.model.dto.AccountCreationRequestDTO;

import java.util.UUID;

public interface AccountCreationUseCase {
    Account createAccount(UUID customerId, AccountCreationRequestDTO requestDTO);
}
