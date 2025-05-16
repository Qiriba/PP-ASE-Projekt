package com.banking.application.services;

import com.banking.application.ports.in.AccountCreationUseCase;
import com.banking.application.ports.out.AccountRepositoryPort;
import com.banking.application.ports.out.CustomerRepositoryPort;
import com.banking.domain.model.Account;
import com.banking.domain.model.Customer;
import com.banking.domain.model.dto.AccountCreationRequestDTO;
import com.banking.domain.model.valueobjects.AccountNumber;
import com.banking.domain.model.valueobjects.Money;

import org.springframework.stereotype.Service;

import java.util.Currency;
import java.util.UUID;

@Service
public class AccountCreationService implements AccountCreationUseCase {

    private final CustomerRepositoryPort customerRepository;
    private final AccountRepositoryPort accountRepository;

    public AccountCreationService(CustomerRepositoryPort customerRepository,
                                  AccountRepositoryPort accountRepository) {
        this.customerRepository = customerRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    public Account createAccount(UUID customerId, AccountCreationRequestDTO request) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Kunde nicht gefunden"));

        Currency currency = Currency.getInstance(request.currencyCode());
        Money initialBalance = new Money(request.initialDeposit(), currency);

        Account account = new Account(
                AccountNumber.generate(),
                initialBalance,
                customer
        );

        return accountRepository.save(account);
    }
}
