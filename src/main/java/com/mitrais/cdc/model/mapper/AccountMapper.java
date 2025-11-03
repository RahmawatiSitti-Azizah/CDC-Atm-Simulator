package com.mitrais.cdc.model.mapper;

import com.mitrais.cdc.model.Account;
import com.mitrais.cdc.model.dto.AccountDto;

public class AccountMapper {
    public static Account toEntity(AccountDto dto) {
        assert dto != null;
        return new Account(dto.getAccountNumber(), dto.getAccountHolderName(), null, dto.getBalance());
    }

    public static AccountDto toAccountDto(Account entity) {
        assert entity != null;
        return new AccountDto(entity.getAccountHolderName(), entity.getAccountNumber(), entity.getBalance());
    }
}
