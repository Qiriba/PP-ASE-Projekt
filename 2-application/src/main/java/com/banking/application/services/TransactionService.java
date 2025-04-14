package com.banking.application.services;

import com.banking.application.ports.in.TransactionUseCase;
import com.banking.application.ports.out.TransactionRepositoryPort;
import com.banking.domain.model.Account;
import com.banking.domain.model.Transactions;
import com.banking.domain.model.factory.TransactionFactory;
import com.banking.domain.model.valueobjects.Money;
import org.springframework.stereotype.Service;

@Service
public class TransactionService implements TransactionUseCase {
    private final TransactionFactory transactionFactory;
    private final TransactionRepositoryPort transactionRepository;

    public TransactionService(TransactionFactory transactionFactory,
                          TransactionRepositoryPort transactionRepository) {
        this.transactionFactory = transactionFactory;
        this.transactionRepository = transactionRepository;
    }

    public void deposit(Account account, Money amount) {
        account.deposit(amount);
        Transactions tx = transactionFactory.createDeposit(account, amount);
        transactionRepository.save(tx);
    }

    public void withdraw(Account account, Money amount) {
        account.withdraw(amount);
        Transactions tx = transactionFactory.createWithdrawal(account, amount);
        transactionRepository.save(tx);
    }

    public void transfer(Account source, Account target, Money amount) {
        source.transferTo(target, amount);
        Transactions tx = transactionFactory.createTransfer(source, target, amount);
        transactionRepository.save(tx);
    }
}
