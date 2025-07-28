package com.mitrais.cdc.view;

import com.mitrais.cdc.model.Account;
import com.mitrais.cdc.service.AccountValidatorService;
import com.mitrais.cdc.service.SearchAccountService;
import com.mitrais.cdc.service.impl.ServiceFactory;

import java.util.Scanner;

public class WelcomeScreen implements Screen {
    private static final WelcomeScreen INSTANCE = new WelcomeScreen();
    private Scanner userInputScanner;
    private Account userAccount;
    private AccountValidatorService validatorService;
    private SearchAccountService searchAccountService;

    public static WelcomeScreen getInstance(Account userAccount, Scanner userInputScanner) {
        INSTANCE.userAccount = userAccount;
        INSTANCE.userInputScanner = userInputScanner;
        return INSTANCE;
    }

    private WelcomeScreen() {
        validatorService = ServiceFactory.createAccountValidatorService();
        searchAccountService = ServiceFactory.createSearchAccountService();
    }

    @Override
    public Screen display() {
        try {
            String accountNumber = getAccountNumberFromUserInput();
            String inputPin = getPinFromUserInput();
            userAccount = searchAccountService.get(accountNumber, inputPin);
            return TransactionScreen.getInstance(userAccount, userInputScanner);
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
