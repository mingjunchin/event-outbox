package com.example.eventoutbox.usecase;

import com.example.eventoutbox.controller.dto.CreateAccountDto;
import com.example.eventoutbox.entity.AccountBalance;
import com.example.eventoutbox.repository.AccountBalanceRepository;
import org.springframework.stereotype.Component;

@Component
public class CreateAccount {

    private final AccountBalanceRepository accountBalanceRepository;

    public CreateAccount(AccountBalanceRepository accountBalanceRepository) {
        this.accountBalanceRepository = accountBalanceRepository;
    }

    public AccountBalance create(CreateAccountDto createAccountDto) {

        AccountBalance accountBalance = new AccountBalance(
                createAccountDto.getId(),
                createAccountDto.getAccountNumber(),
                createAccountDto.getUserId(),
                createAccountDto.getBalance(),
                null,
                null
        );
        return accountBalanceRepository.save(accountBalance);
    }
}
