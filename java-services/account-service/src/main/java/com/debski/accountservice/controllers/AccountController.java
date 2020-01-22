package com.debski.accountservice.controllers;

import com.debski.accountservice.models.AccountDTO;
import com.debski.accountservice.models.DropdownValuesDTO;
import com.debski.accountservice.models.IncomeDTO;
import com.debski.accountservice.models.OutcomeDTO;
import com.debski.accountservice.services.AccountService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashSet;

@RestController
public class AccountController {

    private AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/create")
    public AccountDTO createAccount(@RequestBody AccountDTO accountDto) {
        return accountService.save(accountDto);
    }

    @PreAuthorize("#oauth2.hasScope('server')")
    @GetMapping("/get/{username}")
    public AccountDTO getAccountByName(@PathVariable String username) {
        return accountService.findByUsername(username);
    }

    @PreAuthorize("#oauth2.hasScope('ui')")
    @PutMapping("/current/update/income")
    public AccountDTO updateAccountIncome(Principal principal, @RequestBody IncomeDTO incomeDTO) {
        AccountDTO accountDto = AccountDTO.builder()
                .username(principal.getName())
                .incomes(new HashSet<>() {{
                    add(incomeDTO);
                }})
                .build();
        return accountService.update(accountDto);
    }

    @PreAuthorize("#oauth2.hasScope('ui')")
    @PutMapping("/current/update/outcome")
    public AccountDTO updateAccountOutcome(Principal principal, @RequestBody OutcomeDTO outcomeDTO) {
        AccountDTO accountDto = AccountDTO.builder()
                .username(principal.getName())
                .outcomes(new HashSet<>() {{
                    add(outcomeDTO);
                }})
                .build();
        return accountService.update(accountDto);
    }

    @PreAuthorize("#oauth2.hasScope('ui')")
    @GetMapping("/dropdown_values")
    public DropdownValuesDTO getValueForDropdowns() {
        return accountService.provideValuesForDropdowns();
    }
}
