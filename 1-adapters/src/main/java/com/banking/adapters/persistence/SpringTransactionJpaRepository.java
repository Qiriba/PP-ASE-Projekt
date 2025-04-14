package com.banking.adapters.persistence;

import com.banking.domain.model.Transactions;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SpringTransactionJpaRepository extends JpaRepository<Transactions, UUID> {
}