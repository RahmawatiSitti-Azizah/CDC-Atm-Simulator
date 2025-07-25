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
    private static final WithdrawSummaryScreen INSTANCE = new WithdrawSummaryScreen();
    private Money amount;
    private Account userAccount;
    private Scanner userInputScanner;
    private UserInputService userInput;
    private AccountTransactionService accountTransaction;
    private TransactionAmountValidatorService transactionValidate;

    public static WithdrawSummaryScreen getInstance() {
        return INSTANCE;
    }

    private WithdrawSummaryScreen() {
        this(null, null, null);
    }

    public WithdrawSummaryScreen(Money amount, Account account, Scanner aUserInputScanner) {
        resetData(amount, account, aUserInputScanner);
    }

    public void resetData(Money amount, Account account, Scanner aUserInputScanner) {
        userAccount = account;
        userInputScanner = aUserInputScanner;
        userInput = ServiceFactory.createUserInputService();
        accountTransaction = ServiceFactory.createAccountTransactionService();
        transactionValidate = ServiceFactory.createTransactionAmountValidatorService();
        this.amount = amount;
    }

    @Override
    public Screen display() {
        try {
            withdrawProcess();
            printSummary();
            return printMenuAndGetScreen();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new WithdrawScreen(userAccount, userInputScanner);
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
                return TransactionScreen.getInstance(userAccount, userInputScanner);
            }
            default: {
                break;
            }
        }
        return WelcomeScreen.getInstance(null, userInputScanner);
    }
}
