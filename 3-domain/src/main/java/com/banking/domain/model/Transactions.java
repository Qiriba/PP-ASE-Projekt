package com.banking.domain.model;

import com.banking.domain.model.enums.TransactionType;
import com.banking.domain.model.valueobjects.Money;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
public class Transactions {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private LocalDateTime timestamp;

    @Embedded
    private Money amount;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    @ManyToOne
    @JoinColumn(name = "source_account_id", nullable = false)
    private Account sourceAccount;

    @ManyToOne
    @JoinColumn(name = "target_account_id", nullable = true)
    private Account targetAccount;

    public Transactions(UUID id, LocalDateTime timestamp, Money amount, TransactionType type, Account sourceAccount, Account targetAccount) {
        this.id = id;
        this.timestamp = timestamp;
        this.amount = amount;
        this.type = type;
        this.sourceAccount = sourceAccount;
        this.targetAccount = targetAccount;
    }

    // Standardkonstruktor für JPA
    public Transactions() {
    }

    public UUID getId() {
        return id;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public Money getAmount() {
        return amount;
    }

    public TransactionType getType() {
        return type;
    }

    public Account getSourceAccount() {
        return sourceAccount;
    }

    public Account getTargetAccount() {
        return targetAccount;
    }

    @Override
    public String toString() {
        return type + " " + amount + " am " + timestamp + " (von " + sourceAccount.getId() + " zu " + targetAccount.getId() + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Transactions that)) return false;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
