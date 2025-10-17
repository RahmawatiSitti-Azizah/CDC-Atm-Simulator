package com.mitrais.cdc.service;

import com.mitrais.cdc.model.Account;

public interface LoginService {
    public Account login(String account, String pin) throws Exception;
}
