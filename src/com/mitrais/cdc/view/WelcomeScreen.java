package com.mitrais.cdc.view;

import com.mitrais.cdc.model.Account;
import com.mitrais.cdc.service.AccountValidationService;
import com.mitrais.cdc.service.SearchAccountService;
import com.mitrais.cdc.service.impl.ServiceFactory;

import javax.xml.bind.ValidationException;
import java.util.Scanner;

public class WelcomeScreen implements Screen {
    private Scanner userInputScanner;
    private Account userAccount;
    private AccountValidationService accountValidate;
    private SearchAccountService searchAccount;

    public WelcomeScreen(Scanner aUserInputScanner) {
        this(null, aUserInputScanner);
    }

    public WelcomeScreen(Account account, Scanner aUserInputScanner) {
        userAccount = account;
        userInputScanner = aUserInputScanner;
        accountValidate = ServiceFactory.createAccountValidationService();
        searchAccount = ServiceFactory.createSearchAccountValidationService();
    }

    @Override
    public Screen display() {
        System.out.print("Enter Account Number :");
        String inputAccountNumber = userInputScanner.nextLine();
        try {
            accountValidate.accountNumber(inputAccountNumber);
            System.out.print("Enter Pin :");
            String inputPin = userInputScanner.nextLine();
            accountValidate.pin(inputPin);
            userAccount = searchAccount.get(inputAccountNumber, inputPin);
            return new TransactionScreen(userAccount, userInputScanner);
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
            return this;
        }
    }
}
