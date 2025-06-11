package com.mitrais.cdc.view;

import com.mitrais.cdc.model.Account;
import com.mitrais.cdc.service.AccountValidatorService;
import com.mitrais.cdc.service.SearchAccountService;
import com.mitrais.cdc.service.impl.ServiceFactory;

import java.util.Scanner;

public class WelcomeScreen implements Screen {
    private Scanner userInputScanner;
    private Account userAccount;
    private AccountValidatorService validatorService;
    private SearchAccountService searchAccountService;

    public WelcomeScreen(Scanner aUserInputScanner) {
        this(null, aUserInputScanner);
    }

    public WelcomeScreen(Account account, Scanner aUserInputScanner) {
        userAccount = account;
        userInputScanner = aUserInputScanner;
        validatorService = ServiceFactory.createAccountValidatorService();
        searchAccountService = ServiceFactory.createSearchAccountService();
    }

    @Override
    public Screen display() {
        try {
            String accountNumber = getAccountNumberFromUserInput();
            String inputPin = getPinFromUserInput();
            userAccount = searchAccountService.get(accountNumber, inputPin);
            return new TransactionScreen(userAccount, userInputScanner);
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
