package com.mitrais.cdc.view;

import com.mitrais.cdc.model.Account;
import com.mitrais.cdc.model.Transaction;
import com.mitrais.cdc.service.TransactionService;
import com.mitrais.cdc.service.impl.ServiceFactory;

import java.util.List;
import java.util.Scanner;

public class HistoryTransactionScreen implements Screen {
    private static HistoryTransactionScreen INSTANCE;
    private Account userAccount;
    private Scanner userInputScanner;
    private TransactionService transactionService;

    private HistoryTransactionScreen(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    public static HistoryTransactionScreen getInstance(Account account, Scanner aUserInputScanner, TransactionService transactionService) {
        if (INSTANCE == null) {
            INSTANCE = new HistoryTransactionScreen(transactionService);
        }
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
                Account destinationAccount = transaction.getDestinationAccount();
                if (destinationAccount != null && destinationAccount.getAccountNumber().equals(userAccount.getAccountNumber())) {
                    System.out.println("- " + transaction.printIncomingTransaction());
                } else {
                    System.out.println("- " + transaction);
                }
            }
        }
        return TransactionScreen.getInstance(userAccount, userInputScanner, ServiceFactory.createUserInputService());
    }
}
