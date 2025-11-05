package com.mitrais.cdc.view;

import com.mitrais.cdc.model.dto.AccountDto;
import com.mitrais.cdc.model.dto.TransactionDto;
import com.mitrais.cdc.service.TransactionService;
import com.mitrais.cdc.util.SessionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;

@Component
public class HistoryTransactionScreen implements Screen {
    private static HistoryTransactionScreen INSTANCE;
    private final SessionContext sessionContext;
    private final ScreenManager screenManager;
    private final TransactionService transactionService;

    @Autowired
    public HistoryTransactionScreen(SessionContext sessionContext, ScreenManager screenManager, TransactionService transactionService) {
        this.sessionContext = sessionContext;
        this.screenManager = screenManager;
        this.transactionService = transactionService;
    }

    private HistoryTransactionScreen(TransactionService transactionService) {
        this.transactionService = transactionService;
        this.sessionContext = null;
        this.screenManager = null;
    }

    public static HistoryTransactionScreen getInstance(AccountDto account, Scanner aUserInputScanner, TransactionService transactionService) {
        if (INSTANCE == null) {
            INSTANCE = new HistoryTransactionScreen(transactionService);
        }
        return INSTANCE;
    }

    @Override
    public Screen display() {
        if (!sessionContext.isAuthenticated()) {
            System.out.println("Unauthenticated user. Please login.");
            return screenManager.getScreen(ScreenEnum.TRANSACTION_SCREEN);
        }
        AccountDto userAccount = sessionContext.getSession();
        System.out.print("Retrieving last 10 transactions for account: " + userAccount.getAccountNumber() + "\n");
        List<TransactionDto> transactionList = transactionService.getTransactionHistoryAccount(userAccount.getAccountNumber(), 10);
        if (transactionList.isEmpty()) {
            System.out.println("No transaction history found for account: " + userAccount.getAccountNumber());
        } else {
            for (TransactionDto transaction : transactionList) {
                String destinationAccount = transaction.getDestinationAccountNumber();
                if (destinationAccount != null && destinationAccount.equals(userAccount.getAccountNumber())) {
                    System.out.println("- " + transaction.printIncomingTransaction());
                } else {
                    System.out.println("- " + transaction);
                }
            }
        }
        return screenManager.getScreen(ScreenEnum.TRANSACTION_SCREEN);
    }
}
