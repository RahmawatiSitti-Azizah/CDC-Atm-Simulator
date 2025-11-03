package com.mitrais.cdc.service;

import com.mitrais.cdc.model.Money;
import com.mitrais.cdc.model.dto.AccountDto;
import jakarta.servlet.http.HttpServletRequest;

public interface SearchAccountService {
    public void addAccount(Money initialBalance, String accountHolderName, String accountNumber, String pin);

    public AccountDto get(String id, String otherDetail) throws Exception;

    public AccountDto get(String id) throws Exception;

    public AccountDto get(HttpServletRequest request) throws Exception;
}
