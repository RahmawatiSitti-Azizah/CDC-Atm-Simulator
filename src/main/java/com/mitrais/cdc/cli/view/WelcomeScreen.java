package com.mitrais.cdc.cli.view;

import com.mitrais.cdc.cli.SessionContext;
import com.mitrais.cdc.model.dto.AccountDto;
import com.mitrais.cdc.service.AccountValidatorService;
import com.mitrais.cdc.service.SearchAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class WelcomeScreen implements Screen {
    private final AccountValidatorService validatorService;
    private final SearchAccountService searchAccountService;
    private final ScreenManager screenManager;
    private Scanner userInputScanner;
    private final SessionContext sessionContext;
    private AccountDto userAccount;

    @Autowired
    public WelcomeScreen(SearchAccountService searchAccountService, SessionContext sessionContext, AccountValidatorService validatorService, ScreenManager screenManager, Scanner userInputScanner) {
        this.searchAccountService = searchAccountService;
        this.sessionContext = sessionContext;
        this.validatorService = validatorService;
        this.screenManager = screenManager;
        this.userInputScanner = userInputScanner;
    }

    @Override
    public Screen display() {
        try {
            String accountNumber = getAccountNumberFromUserInput();
            String inputPin = getPinFromUserInput();
            userAccount = searchAccountService.get(accountNumber, inputPin);
            sessionContext.setSession(userAccount);
            return screenManager.getScreen(ScreenEnum.TRANSACTION_SCREEN);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return this;
        }
    }

    private String getPinFromUserInput() throws Exception {
        System.out.print("Enter Pin :");
        String inputPin = userInputScanner.nextLine();
        validatorService.validatePin(inputPin);
        return inputPin;
    }

    private String getAccountNumberFromUserInput() throws Exception {
        return inputAccountNumber();
    }

    private String inputAccountNumber() throws Exception {
        System.out.print("Enter Account Number :");
        String inputAccountNumber = userInputScanner.nextLine();
        validatorService.validateAccountNumber(inputAccountNumber, "Account Number should only contains numbers");
        return inputAccountNumber;
    }
}
