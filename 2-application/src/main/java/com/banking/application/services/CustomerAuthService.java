package com.banking.application.services;

import com.banking.application.ports.out.AccountRepositoryPort;
import com.banking.application.ports.out.CustomerRepositoryPort;
import com.banking.domain.model.Account;
import com.banking.domain.model.Customer;
import com.banking.domain.model.exceptions.AccountLockedException;
import com.banking.domain.model.exceptions.CustomerNotFoundException;
import com.banking.domain.security.JwtProvider;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CustomerAuthService {

    private final JwtProvider JwtProvider;
    private final CustomerRepositoryPort customerRepository;

    private final AccountRepositoryPort accountRepositoryPort;
    public CustomerAuthService(JwtProvider jwtProvider, CustomerRepositoryPort customerRepository, AccountRepositoryPort accountRepositoryPort) {
        JwtProvider = jwtProvider;
        this.customerRepository = customerRepository;
        this.accountRepositoryPort = accountRepositoryPort;
    }

    public Customer getCustomerFromToken(String authHeader) {
        String token = extractToken(authHeader);
        String customerUsername = JwtProvider.extractUsername(token);
        return customerRepository.findByUsername(customerUsername)
                .orElseThrow(() -> new CustomerNotFoundException("Customer with Name " + customerUsername + " not found"));
    }

    private String extractToken(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Invalid Authorization header");
        }
        return authHeader.replace("Bearer ", "");
    }

    public Customer getCustomerById(UUID id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer with ID " + id + " not found"));
    }

    public Account getCustomerAccount(Customer customer, UUID accountId) {
        Account account = customer.getAccount(accountId);
        if (account == null) {
            throw new IllegalArgumentException("Account not found or does not belong to this customer");
        }
        if (account.isLocked()) {
            throw new AccountLockedException("Account is locked");
        }
        return account;
    }

    public Account findAnyAccountById(UUID accountId) {
        return accountRepositoryPort.findById(accountId)
                .orElseThrow(() -> new CustomerNotFoundException("Account with ID " + accountId + " not found"));
    }
}
