package com.banking.application.ports.out;

import com.banking.domain.model.Account;

public interface AccountRepositoryPort {
    Account save(Account account);
}
