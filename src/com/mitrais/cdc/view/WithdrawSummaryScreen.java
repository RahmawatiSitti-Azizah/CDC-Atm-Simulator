package com.mitrais.cdc.view;

import com.mitrais.cdc.model.Account;
import com.mitrais.cdc.model.Money;
import com.mitrais.cdc.service.AccountTransactionService;
import com.mitrais.cdc.service.TransactionAmountValidatorService;
import com.mitrais.cdc.service.UserInputService;
import com.mitrais.cdc.service.impl.ServiceFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class WithdrawSummaryScreen implements Screen {
    private static WithdrawSummaryScreen INSTANCE;
    private Money amount;
    private Account userAccount;
    private Scanner userInputScanner;
    private UserInputService userInput;
    private AccountTransactionService accountTransaction;
    private TransactionAmountValidatorService transactionValidate;

    public static WithdrawSummaryScreen getInstance(Money amount, Account account, Scanner aUserInputScanner, UserInputService userInputService, AccountTransactionService accountTransactionService, TransactionAmountValidatorService transactionAmountValidatorService) {
        if (INSTANCE == null) {
            INSTANCE = new WithdrawSummaryScreen(userInputService, accountTransactionService, transactionAmountValidatorService);
        }
        INSTANCE.userAccount = account;
        INSTANCE.userInputScanner = aUserInputScanner;
        INSTANCE.amount = amount;
        return INSTANCE;
    }

    private WithdrawSummaryScreen(UserInputService userInputService, AccountTransactionService accountTransactionService, TransactionAmountValidatorService transactionAmountValidatorService) {
        userInput = userInputService;
        accountTransaction = accountTransactionService;
        transactionValidate = transactionAmountValidatorService;
    }

    @Override
    public Screen display() {
        try {
            withdrawProcess();
            printSummary();
            return printMenuAndGetScreen();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return WithdrawScreen.getInstance(userAccount, userInputScanner, userInput);
        }
    }

    private void withdrawProcess() throws Exception {
        transactionValidate.validateWithdrawAmount(amount);
        accountTransaction.withdraw(userAccount, amount);
    }

    private void printSummary() {
        System.out.println("Summary");
        System.out.println("Date : " + (new SimpleDateFormat("yyyy-MM-dd hh:mm a")).format(new Date()));
        System.out.println("Withdraw : " + amount.toString());
        System.out.println("Balance : " + userAccount.getStringBalance());
        System.out.println("");
    }

    private Screen printMenuAndGetScreen() {
        System.out.println("1. Transaction");
        System.out.println("2. Exit");
        System.out.print("Please choose option[2] : ");
        String input = userInputScanner.nextLine();
        int menu;
        menu = userInput.toValidatedMenu(input);
        switch (menu) {
            case 1: {
                return TransactionScreen.getInstance(userAccount, userInputScanner, userInput);
            }
            default: {
                break;
            }
        }
        return WelcomeScreen.getInstance(null, userInputScanner, ServiceFactory.createSearchAccountService(), ServiceFactory.createAccountValidatorService());
    }
}
