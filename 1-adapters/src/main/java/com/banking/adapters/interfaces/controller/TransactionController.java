package com.banking.adapters.interfaces.controller;

import com.banking.adapters.persistence.CustomerRepository;
import com.banking.application.services.CustomerAuthService;
import com.banking.application.services.TransactionService;
import com.banking.domain.model.Account;
import com.banking.domain.model.Customer;
import com.banking.domain.model.dto.DepositRequestDTO;
import com.banking.domain.model.dto.TransferRequestDTO;
import com.banking.domain.model.dto.WithdrawRequestDTO;
import com.banking.domain.model.exceptions.CustomerNotFoundException;
import com.banking.domain.model.exceptions.AccountLockedException;
import com.banking.domain.model.valueobjects.Money;
import com.banking.adapters.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/banking")
public class TransactionController {

    private final TransactionService transactionService;
    private final CustomerRepository customerRepository;
    private final JwtUtil jwtUtil;

    private final CustomerAuthService customerAuthService;
    public TransactionController(TransactionService transactionService,
                                 CustomerRepository customerRepository,
                                 JwtUtil jwtUtil, CustomerAuthService customerAuthService) {
        this.transactionService = transactionService;
        this.customerRepository = customerRepository;
        this.jwtUtil = jwtUtil;
        this.customerAuthService = customerAuthService;
    }

    @PostMapping("/deposit")
    public ResponseEntity<?> deposit(@RequestHeader("Authorization") String authHeader,
                                     @RequestBody DepositRequestDTO request) {
        Customer customer = getCustomerFromToken(authHeader);
        Account account = getCustomerAccount(customer, request.accountId());
        transactionService.deposit(account, new Money(request.amount(), request.currency()));
        return ResponseEntity.ok().build();
    }

    @PostMapping("/withdraw")
    public ResponseEntity<?> withdraw(@RequestHeader("Authorization") String authHeader,
                                      @RequestBody WithdrawRequestDTO request) {
        Customer customer = getCustomerFromToken(authHeader);
        Account account = getCustomerAccount(customer, request.accountId());
        transactionService.withdraw(account, new Money(request.amount(), request.currency()));
        return ResponseEntity.ok().build();
    }

    @PostMapping("/transfer")
    public ResponseEntity<?> transfer(@RequestHeader("Authorization") String authHeader,
                                      @RequestBody TransferRequestDTO request) {
        Customer customer = getCustomerFromToken(authHeader);
        Account from = getCustomerAccount(customer, request.fromAccountId());
        Account to = customerAuthService.findAnyAccountById(request.toAccountId());
        transactionService.transfer(from, to, new Money(request.amount(), request.currency()));
        return ResponseEntity.ok().build();
    }

    // Helpers
    private Customer getCustomerFromToken(String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        UUID customerId = jwtUtil.extractCustomerId(token);
        return customerAuthService.getCustomerById(customerId);
    }

    private Account getCustomerAccount(Customer customer, UUID accountId) {
        Account account = customer.getAccount(accountId);
        if (account == null) {
            throw new IllegalArgumentException("Account not found or does not belong to this customer");
        }
        if (account.isLocked()) {
            throw new AccountLockedException("Account is locked");
        }
        return account;
    }
}
