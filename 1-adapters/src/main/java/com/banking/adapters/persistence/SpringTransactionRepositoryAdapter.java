package com.banking.adapters.persistence;

import com.banking.application.ports.out.TransactionRepositoryPort;
import com.banking.domain.model.Transactions;
import org.springframework.stereotype.Component;

@Component
public class SpringTransactionRepositoryAdapter implements TransactionRepositoryPort {

    private final TransactionRepository transactionRepository;

    public SpringTransactionRepositoryAdapter(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public Transactions save(Transactions transaction) {
        return transactionRepository.save(transaction);
    }
}
