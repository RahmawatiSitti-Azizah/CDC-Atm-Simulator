package com.mitrais.cdc.service.impl;

import com.mitrais.cdc.service.*;

public class ServiceFactory {
    private static AccountTransactionService accountTransaction;
    private static AccountValidationService accountValidation;
    private static SearchAccountService searchAccount;
    private static UserInputService userInput;
    private static TransactionValidationService transactionValidation;

    public static AccountTransactionService createAccountTransactionService() {
        if (accountTransaction == null) {
            accountTransaction = new AccountTransactionServiceImpl();
        }
        return accountTransaction;
    }

    public static AccountValidationService createAccountValidationService() {
        if (accountValidation == null) {
            accountValidation = new AccountValidationServiceImpl();
        }
        return accountValidation;
    }

    public static SearchAccountService createSearchAccountValidationService() {
        if (searchAccount == null) {
            searchAccount = new SearchAccountServiceImpl();
        }
        return searchAccount;
    }

    public static UserInputService createUserInputService() {
        if (userInput == null) {
            userInput = new UserInputServiceImpl();
        }
        return userInput;
    }

    public static TransactionValidationService createTransactionValidationService() {
        if (transactionValidation == null) {
            transactionValidation = new TransactionValidationServiceImpl();
        }
        return transactionValidation;
    }
}
