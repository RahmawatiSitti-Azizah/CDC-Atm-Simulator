package com.mitrais.cdc.service.impl;

import com.mitrais.cdc.service.*;

public class ServiceFactory {
    private static AccountTransactionService accountTransaction;
    private static AccountValidatorService accountValidator;
    private static SearchAccountService searchAccount;
    private static UserInputService userInput;
    private static TransactionAmountValidatorService transactionAmountValidator;

    public static AccountTransactionService createAccountTransactionService() {
        if (accountTransaction == null) {
            accountTransaction = new AccountTransactionServiceImpl();
        }
        return accountTransaction;
    }

    public static AccountValidatorService createAccountValidatorService() {
        if (accountValidator == null) {
            accountValidator = new AccountValidatorServiceImpl();
        }
        return accountValidator;
    }

    public static SearchAccountService createSearchAccountService() {
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

    public static TransactionAmountValidatorService createTransactionAmountValidatorService() {
        if (transactionAmountValidator == null) {
            transactionAmountValidator = new TransactionAmountValidatorServiceImpl();
        }
        return transactionAmountValidator;
    }
}
