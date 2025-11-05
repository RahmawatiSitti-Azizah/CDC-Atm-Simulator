package com.mitrais.cdc.view;

import com.mitrais.cdc.model.Money;
import com.mitrais.cdc.model.dto.AccountDto;
import com.mitrais.cdc.service.AccountTransactionService;
import com.mitrais.cdc.service.TransactionAmountValidatorService;
import com.mitrais.cdc.service.UserInputService;
import com.mitrais.cdc.util.SessionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

@Component
public class WithdrawSummaryScreen implements Screen {
    private static WithdrawSummaryScreen INSTANCE;
    private final AccountTransactionService accountTransaction;
    private final TransactionAmountValidatorService transactionValidate;
    private final UserInputService userInput;
    private final SessionContext sessionContext;
    private final ScreenManager screenManager;
    private Scanner userInputScanner;
    private Screen previousScreen;
    private AccountDto userAccount;
    private Money amount;

    @Autowired
    public WithdrawSummaryScreen(AccountTransactionService accountTransaction, TransactionAmountValidatorService transactionValidate, UserInputService userInput, SessionContext sessionContext, Scanner userInputScanner, ScreenManager screenManager) {
        this.accountTransaction = accountTransaction;
        this.transactionValidate = transactionValidate;
        this.userInput = userInput;
        this.sessionContext = sessionContext;
        this.userInputScanner = userInputScanner;
        this.screenManager = screenManager;
    }

    public static WithdrawSummaryScreen getInstance(Money amount, AccountDto account, Scanner aUserInputScanner, UserInputService userInputService, AccountTransactionService accountTransactionService, TransactionAmountValidatorService transactionAmountValidatorService) {
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
        sessionContext = null;
        screenManager = null;
    }

    public void setTransactionAmount(Money amount) {
        this.amount = amount;
    }

    public void setPreviousScreen(Screen previousScreen) {
        this.previousScreen = previousScreen;
    }

    @Override
    public Screen display() {
        try {
            if (!sessionContext.isAuthenticated()) {
                System.out.println("Unauthenticated user. Please login.");
                return screenManager.getScreen(ScreenEnum.WELCOME_SCREEN);
            }
            userAccount = sessionContext.getSession();
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
        System.out.println("Balance : " + userAccount.getBalance().toString());
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
                return screenManager.getScreen(ScreenEnum.TRANSACTION_SCREEN);
            }
            default: {
                break;
            }
        }
        sessionContext.clearSession();
        return screenManager.getScreen(ScreenEnum.WELCOME_SCREEN);
    }
}
