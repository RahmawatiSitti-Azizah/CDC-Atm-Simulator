package com.mitrais.cdc.cli;

import com.mitrais.cdc.model.dto.AccountDto;
import org.springframework.stereotype.Component;

@Component
public class SessionContext {
    private AccountDto account;

    public SessionContext() {
    }

    public boolean isAuthenticated() {
        return account != null;
    }

    public void setSession(AccountDto account) {
        this.account = account;
    }

    public void clearSession() {
        account = null;
    }

    public AccountDto getSession() {
        return account;
    }
}
