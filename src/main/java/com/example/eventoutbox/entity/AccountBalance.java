package com.example.eventoutbox.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
public class AccountBalance {
    private Instant createdAt;
    @Id
    private UUID id;
    private String accountNumber;
    private String userId;
    private BigDecimal balance;
    private Instant updatedAt;

    public AccountBalance(UUID id, String accountNumber, String userId, BigDecimal balance, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.userId = userId;
        this.balance = balance;
        this.createdAt = createdAt == null ? Instant.now() : createdAt;
        this.updatedAt = updatedAt == null ? Instant.now() : updatedAt;
    }

    public AccountBalance() {
    }

    public void updateBalance(BigDecimal balance) {
        if (balance.scale() > 2) {
            System.out.println("scale: " + balance.scale());
            throw new IllegalArgumentException("Balance precision must be equals or less than 2");
        }

        this.balance = balance;
        this.updatedAt = Instant.now();
    }

    public UUID getId() {
        return id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getUserId() {
        return userId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
