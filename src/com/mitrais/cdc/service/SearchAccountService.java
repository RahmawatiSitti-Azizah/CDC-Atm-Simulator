package com.mitrais.cdc.service;

import com.mitrais.cdc.model.Account;
import com.mitrais.cdc.model.Money;

public interface SearchAccountService {
    public void addAccount(Money initialBalance, String accountHolderName, String accountNumber, String pin) throws Exception;

    public Account get(String id, String otherDetail) throws Exception;

    public Account getByID(String id) throws Exception;
}
