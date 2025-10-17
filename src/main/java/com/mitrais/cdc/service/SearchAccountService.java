package com.mitrais.cdc.service;

import com.mitrais.cdc.model.Account;
import com.mitrais.cdc.model.Money;
import jakarta.servlet.http.HttpServletRequest;

public interface SearchAccountService {
    public void addAccount(Money initialBalance, String accountHolderName, String accountNumber, String pin);

    public Account get(String id, String otherDetail) throws Exception;

    public Account get(String id) throws Exception;

    public Account get(HttpServletRequest request) throws Exception;
}
