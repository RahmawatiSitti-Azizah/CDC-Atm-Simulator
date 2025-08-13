package com.mitrais.cdc.view;

import com.mitrais.cdc.model.Account;
import com.mitrais.cdc.model.Transaction;
import com.mitrais.cdc.service.TransactionService;
import com.mitrais.cdc.service.impl.ServiceFactory;

import java.util.List;
import java.util.Scanner;

public class HistoryTransactionScreen implements Screen {
    private static final HistoryTransactionScreen INSTANCE = new HistoryTransactionScreen();
    private Account userAccount;
    private Scanner userInputScanner;
    private TransactionService transactionService;

    private HistoryTransactionScreen() {
        transactionService = ServiceFactory.createTransactionService();
    }

    public static HistoryTransactionScreen getInstance(Account account, Scanner aUserInputScanner) {
        INSTANCE.userAccount = account;
        INSTANCE.userInputScanner = aUserInputScanner;
        return INSTANCE;
    }

    @Override
    public Screen display() {
        System.out.print("Retrieving last 10 transactions for account: " + userAccount.getAccountNumber() + "\n");
        List<Transaction> transactionList = transactionService.getTransactionHistoryAccount(userAccount.getAccountNumber(), 10);
        if (transactionList.isEmpty()) {
            System.out.println("No transaction history found for account: " + userAccount.getAccountNumber());
        } else {
            for (Transaction transaction : transactionList) {
                System.out.println("- " + transaction);
            }
        }
        return TransactionScreen.getInstance(userAccount, userInputScanner);
    }
}
