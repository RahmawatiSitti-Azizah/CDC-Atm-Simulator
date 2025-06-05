package com.mitrais.cdc.service;

import com.mitrais.cdc.model.Account;

import javax.xml.bind.ValidationException;

public interface SearchAccountService {
    public Account get(String id, String otherDetail) throws ValidationException;

    public Account getByID(String id) throws ValidationException;
}
