package com.mitrais.cdc.service;

import com.mitrais.cdc.model.Account;
import jakarta.servlet.http.HttpServletRequest;

public interface LoginService {
    /**
     * Return the account based on account number and pin provided.
     *
     * @param account account number used to get the account object (not null)
     * @param pin     pin used to get the account object
     * @return Account that contain information that will
     * @throws Exception
     */
    public Account login(String account, String pin) throws Exception;

    public boolean isAuthenticated(HttpServletRequest request);
}
