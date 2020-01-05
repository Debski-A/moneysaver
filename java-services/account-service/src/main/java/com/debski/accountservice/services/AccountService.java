package com.debski.accountservice.services;

import com.debski.accountservice.models.AccountDTO;
import com.debski.accountservice.models.DropdownValuesDTO;

public interface AccountService {
    AccountDTO save(AccountDTO accountDto);
    AccountDTO findByUsername(String username);
    DropdownValuesDTO provideValuesForDropdowns();

}
