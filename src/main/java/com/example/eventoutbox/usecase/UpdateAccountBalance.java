package com.example.eventoutbox.usecase;

import com.example.eventoutbox.entity.AccountBalance;
import com.example.eventoutbox.entity.Event;
import com.example.eventoutbox.repository.AccountBalanceRepository;
import com.example.eventoutbox.repository.EventRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Component
public class UpdateAccountBalance {

    private final AccountBalanceRepository accountBalanceRepository;
    private final EventRepository eventRepository;

    public UpdateAccountBalance(
            AccountBalanceRepository accountBalanceRepository,
            EventRepository eventRepository
    ) {
        this.accountBalanceRepository = accountBalanceRepository;
        this.eventRepository = eventRepository;
    }

    @Transactional
    public AccountBalance byId(UUID id, BigDecimal balance) {
        System.out.println("Id: " + id + "\tbalance: " + balance);
        AccountBalance accountBalance = accountBalanceRepository.findById(id)
                .orElseThrow();

        accountBalance.updateBalance(balance);
        AccountBalance updatedAccountBalance = accountBalanceRepository.save(accountBalance);
        eventRepository.save(
                new Event(
                        UUID.randomUUID().toString(),
                        "BalanceUpdate",
                        UUID.randomUUID(),
                        false,
                        "deposit",
                        updatedAccountBalance.getId(),
                        AccountBalance.class.getSimpleName(),
                        Map.of("balance", updatedAccountBalance.getBalance()),
                        Instant.now(),
                        Instant.now(),
                        Instant.now()
                )
        );
        return updatedAccountBalance;
    }
}
