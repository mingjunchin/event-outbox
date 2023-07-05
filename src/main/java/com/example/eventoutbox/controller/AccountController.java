package com.example.eventoutbox.controller;

import com.example.eventoutbox.controller.dto.CreateAccountDto;
import com.example.eventoutbox.entity.AccountBalance;
import com.example.eventoutbox.usecase.CreateAccount;
import com.example.eventoutbox.usecase.UpdateAccountBalance;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
public class AccountController {

    private final UpdateAccountBalance updateAccountBalance;
    private final CreateAccount createAccount;

    public AccountController(
            UpdateAccountBalance updateAccountBalance,
            CreateAccount createAccount
    ) {
        this.updateAccountBalance = updateAccountBalance;
        this.createAccount = createAccount;
    }

    @PostMapping("/account")
    public AccountBalance createAccount(
            @RequestBody CreateAccountDto createAccountDto) {
        return createAccount.create(createAccountDto);
    }

    @PatchMapping("/accounts/{id}/balance")
    public AccountBalance updateAccountBalance(
            @PathVariable("id") UUID id,
            @RequestBody BigDecimal balance
    ) {
        return updateAccountBalance.byId(id, balance);
    }
}
