package com.banking.adapters.persistence;

import com.banking.application.ports.out.TransactionRepositoryPort;
import com.banking.domain.model.Transactions;
import org.springframework.stereotype.Component;

@Component
public class TransactionRepository implements TransactionRepositoryPort {

    private final SpringTransactionJpaRepository jpaRepository;

    public TransactionRepository(SpringTransactionJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public void save(Transactions transaction) {
        jpaRepository.save(transaction);
    }
}