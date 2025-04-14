package com.banking.adapters.interfaces.controller;

import com.banking.application.services.TransactionService;
import com.banking.domain.model.Account;
import com.banking.domain.model.valueobjects.Money;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.UUID;

@RestController
@RequestMapping("/api/banking")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/deposit")
    public void deposit(@RequestParam UUID accountId, @RequestParam BigDecimal amount) {
        // Fetch account from DB (placeholder)
        Account account = getAccountById(accountId);
        transactionService.deposit(account, new Money(amount));
    }

    @PostMapping("/withdraw")
    public void withdraw(@RequestParam UUID accountId, @RequestParam double amount) {
        Account account = getAccountById(accountId);
        transactionService.withdraw(account, new Money(amount));
    }

    @PostMapping("/transfer")
    public void transfer(@RequestParam UUID fromId, @RequestParam UUID toId, @RequestParam double amount) {
        Account from = getAccountById(fromId);
        Account to = getAccountById(toId);
        transactionService.transfer(from, to, new Money(amount));
    }

    private Account getAccountById(UUID id) {
        // TODO: Get from a proper repository
        throw new UnsupportedOperationException("fetch account not implemented");
    }
}