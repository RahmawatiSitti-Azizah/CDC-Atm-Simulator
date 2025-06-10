package com.mitrais.cdc.service;

import com.mitrais.cdc.model.Account;

public interface SearchAccountService {
    public Account get(String id, String otherDetail) throws Exception;

    public Account getByID(String id) throws Exception;
}
