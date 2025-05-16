package com.banking.adapters.persistence;

import com.banking.application.ports.out.AccountRepositoryPort;
import com.banking.domain.model.Account;
import org.springframework.stereotype.Repository;

@Repository
public class SpringAccountRepositoryAdapter implements AccountRepositoryPort {

    private final AccountRepository accountRepository;

    public SpringAccountRepositoryAdapter(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Account save(Account account) {
        return accountRepository.save(account);
    }
}
