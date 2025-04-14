package com.banking.adapters.persistence;

import com.banking.application.ports.out.TransactionRepositoryPort;
import com.banking.domain.model.Transactions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.UUID;


public interface TransactionRepository extends JpaRepository<Transactions, UUID> {
}