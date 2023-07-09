package com.example.eventoutbox.repository;

import com.example.eventoutbox.entity.AccountBalance;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface AccountBalanceRepository extends CrudRepository<AccountBalance, UUID> {

    @Query(value = """
            INSERT INTO account_balance (id, account_number, user_id, balance, created_at, updated_at)
            VALUES (:#{#accountBalance.id}, :#{#accountBalance.accountNumber}, :#{#accountBalance.userId}, :#{#accountBalance.balance}, :#{#accountBalance.createdAt}, :#{#accountBalance.updatedAt})
            ON CONFLICT (id) DO UPDATE
                SET
                    account_number = :#{#accountBalance.accountNumber},
                    user_id = :#{#accountBalance.userId},
                    balance = :#{#accountBalance.balance},
                    updated_at = :#{#accountBalance.updatedAt}
            RETURNING *
            """, nativeQuery = true)
    AccountBalance save(AccountBalance accountBalance);
}
