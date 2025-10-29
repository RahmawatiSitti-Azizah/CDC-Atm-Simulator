package com.mitrais.cdc.service.impl;

import com.mitrais.cdc.model.Account;
import com.mitrais.cdc.service.AccountValidatorService;
import com.mitrais.cdc.service.LoginService;
import com.mitrais.cdc.service.SearchAccountService;
import com.mitrais.cdc.util.ErrorConstant;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.security.auth.login.CredentialNotFoundException;

@Service
public class LoginServiceImpl implements LoginService {
    private AccountValidatorService validatorService;
    private SearchAccountService searchAccountService;

    @Autowired
    public LoginServiceImpl(SearchAccountService searchAccountService, AccountValidatorService validatorService) {
        this.searchAccountService = searchAccountService;
        this.validatorService = validatorService;
    }

    @Override
    public Account login(String account, String pin) throws Exception {
        if (account == null || pin == null) {
            throw new CredentialNotFoundException(ErrorConstant.INVALID_ACCOUNT_PASSWORD);
        }
        validatorService.validateAccountNumber(account, ErrorConstant.ACCOUNT_NUMBER_SHOULD_ONLY_CONTAINS_NUMBERS);
        validatorService.validatePin(pin);
        Account result = searchAccountService.get(account, pin);
        return new Account(null, result.getAccountHolderName(), result.getAccountNumber(), null);
    }

    @Override
    public boolean isAuthenticated(HttpServletRequest request) {
        HttpSession session = request.getSession();
        return session.getAttribute("account") != null;
    }
}
