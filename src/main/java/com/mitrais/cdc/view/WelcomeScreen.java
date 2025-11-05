package com.mitrais.cdc.view;

import com.mitrais.cdc.model.dto.AccountDto;
import com.mitrais.cdc.service.AccountValidatorService;
import com.mitrais.cdc.service.SearchAccountService;
import com.mitrais.cdc.util.SessionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class WelcomeScreen implements Screen {
    private static WelcomeScreen INSTANCE;
    private Scanner userInputScanner;
    private final SessionContext sessionContext;
    private AccountDto userAccount;
    private final AccountValidatorService validatorService;
    private final SearchAccountService searchAccountService;
    private final TransactionScreen transactionScreen;

    public static WelcomeScreen getInstance(AccountDto userAccount, Scanner userInputScanner, SearchAccountService searchAccountService, AccountValidatorService accountValidatorService) {
        if (INSTANCE == null) {
            INSTANCE = new WelcomeScreen(searchAccountService, accountValidatorService);
        }
        INSTANCE.userAccount = userAccount;
        INSTANCE.userInputScanner = userInputScanner;
        return INSTANCE;
    }

    private WelcomeScreen(SearchAccountService searchAccountService, AccountValidatorService validatorService) {
        this.searchAccountService = searchAccountService;
        this.validatorService = validatorService;
        this.sessionContext = null;
        this.transactionScreen = null;
    }

    @Autowired
    public WelcomeScreen(SearchAccountService searchAccountService, SessionContext sessionContext, AccountValidatorService validatorService, TransactionScreen transactionScreen, Scanner userInputScanner) {
        this.searchAccountService = searchAccountService;
        this.sessionContext = sessionContext;
        this.validatorService = validatorService;
        this.transactionScreen = transactionScreen;
        this.userInputScanner = userInputScanner;
    }

    @Override
    public Screen display() {
        try {
            String accountNumber = getAccountNumberFromUserInput();
            String inputPin = getPinFromUserInput();
            userAccount = searchAccountService.get(accountNumber, inputPin);
            sessionContext.setSession(userAccount);
            transactionScreen.setPreviousScreen(this);
            return transactionScreen;
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
