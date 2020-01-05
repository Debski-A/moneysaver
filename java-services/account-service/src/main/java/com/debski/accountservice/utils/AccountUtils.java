package com.debski.accountservice.utils;

import com.debski.accountservice.entities.Account;
import com.debski.accountservice.entities.enums.Role;
import com.debski.accountservice.models.AccountDTO;
import org.apache.commons.lang.CharUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.function.IntPredicate;

@Component
public class AccountUtils {

    private PasswordEncoder encoder;

    public AccountUtils(PasswordEncoder encoder) {
        this.encoder = encoder;
    }

    public AccountDTO entityToDto(Account entity) {
        AccountDTO dto = entity != null ? AccountDTO.builder()
                .username(entity.getUsername())
                .email(entity.getEmail())
                .incomes(entity.getIncomes())
                .outcomes(entity.getOutcomes())
                .build() : null;
        return dto;
    }

    public Account dtoToEntity(AccountDTO dto) {
        Account entity = Account.builder()
                .username(dto.getUsername())
                .password(encoder.encode(dto.getRawPassword()))
                .email(dto.getEmail())
                .role(Role.USER) // TODO Role.PREMIUM will be available by account upgrade
                .enabled(true)
                .build();
        if (dto.getIncomes() != null) dto.getIncomes().forEach(income -> income.setAccount(entity));
        if (dto.getOutcomes() != null) dto.getOutcomes().forEach(outcome -> outcome.setAccount(entity));
        entity.setIncomes(dto.getIncomes());
        entity.setOutcomes(dto.getOutcomes());
        return entity;
    }

    public boolean isToWeak(String password) {
        IntPredicate isUpperCase =  (singleChar -> CharUtils.isAsciiAlphaUpper((char) singleChar));
        IntPredicate isDigit = (singleChar -> CharUtils.isAsciiNumeric((char) singleChar));

        if (password.length() < 8 || password.chars().noneMatch(isUpperCase) || password.chars().noneMatch(isDigit)) return true;
        return false;
    }

}
