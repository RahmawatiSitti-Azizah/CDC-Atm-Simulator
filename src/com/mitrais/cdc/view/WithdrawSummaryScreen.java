package com.mitrais.cdc.view;

import com.mitrais.cdc.model.Account;
import com.mitrais.cdc.service.AccountTransactionService;
import com.mitrais.cdc.service.TransactionValidationService;
import com.mitrais.cdc.service.UserInputService;
import com.mitrais.cdc.service.impl.ServiceFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class WithdrawSummaryScreen implements Screen {
    private long withdrawAmount;
    private Account userAccount;
    private Scanner userInputScanner;
    private UserInputService userInput;
    private AccountTransactionService accountTransaction;
    private TransactionValidationService transactionValidate;

    public WithdrawSummaryScreen(long aWithdrawAmount, Account account, Scanner aUserInputScanner) {
        this.withdrawAmount = aWithdrawAmount;
        userAccount = account;
        this.userInputScanner = aUserInputScanner;
        userInput = ServiceFactory.createUserInputService();
        accountTransaction = ServiceFactory.createAccountTransactionService();
        transactionValidate = ServiceFactory.createTransactionValidationService();
    }

    @Override
    public Screen display() {
        try {
            transactionValidate.validateWithdrawAmount(withdrawAmount);
            accountTransaction.withdraw(userAccount, withdrawAmount);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new WithdrawScreen(userAccount, userInputScanner);
        }
        System.out.println("Summary");
        System.out.println("Date : " + (new SimpleDateFormat("yyyy-MM-dd hh:mm a")).format(new Date()));
        System.out.println("Withdraw : $" + withdrawAmount);
        System.out.println("Balance : $" + userAccount.getBalance());
        System.out.println("");
        System.out.println("1. Transaction");
        System.out.println("2. Exit");
        System.out.print("Please choose option[2] : ");
        String input = userInputScanner.nextLine();
        int menu;
        menu = userInput.toValidatedMenu(input);
        switch (menu) {
            case 1: {
                return new TransactionScreen(userAccount, userInputScanner);
            }
            default: {
                break;
            }
        }
        return new WelcomeScreen(userInputScanner);
    }
}
