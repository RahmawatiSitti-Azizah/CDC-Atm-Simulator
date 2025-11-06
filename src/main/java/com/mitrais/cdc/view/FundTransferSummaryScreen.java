package com.mitrais.cdc.view;

import com.mitrais.cdc.model.dto.AccountDto;
import com.mitrais.cdc.model.dto.TransactionDto;
import com.mitrais.cdc.service.SearchAccountService;
import com.mitrais.cdc.service.TransactionService;
import com.mitrais.cdc.service.UserInputService;
import com.mitrais.cdc.util.ErrorConstant;
import com.mitrais.cdc.util.SessionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class FundTransferSummaryScreen implements Screen {
    private final SessionContext sessionContext;
    private final ScreenManager screenManager;
    private final UserInputService userInput;
    private final TransactionService transactionService;
    private final SearchAccountService searchAccountService;
    private Scanner userInputScanner;
    private AccountDto userAccount;
    private String referenceNumber;

    @Autowired
    public FundTransferSummaryScreen(SessionContext sessionContext, ScreenManager screenManager, UserInputService userInput, Scanner userInputScanner, TransactionService transactionService, SearchAccountService searchAccountService) {
        this.sessionContext = sessionContext;
        this.screenManager = screenManager;
        this.userInput = userInput;
        this.userInputScanner = userInputScanner;
        this.transactionService = transactionService;
        this.searchAccountService = searchAccountService;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    @Override
    public Screen display() {
        if (!sessionContext.isAuthenticated()) {
            System.out.println("Unauthenticated user. Please login.");
            return screenManager.getScreen(ScreenEnum.WELCOME_SCREEN);
        }
        TransactionDto transaction = transactionService.getTransactionByReferenceNumber(referenceNumber);
        try {
            userAccount = searchAccountService.get(transaction.getSourceAccountNumber());
            if (transaction != null) {
                System.out.println("Fund Transfer Summary");
                System.out.println("Destination Account : " + transaction.getDestinationAccountNumber());
                System.out.println("Transfer Amount : " + transaction.getAmount().toString());
                System.out.println("Reference Number : " + transaction.getReferenceNumber());
                System.out.println("Balance : " + userAccount.getBalance().toString());
                System.out.println("");
            } else {
                System.out.println("Transaction not found. Please try again.");
            }
            System.out.println("1. Transaction");
            System.out.println("2. Exit");
            System.out.print("Please choose option[2] : ");
            String input = userInputScanner.nextLine();
            int menu = userInput.toValidatedMenu(input);
            switch (menu) {
                case 1: {
                    return screenManager.getScreen(ScreenEnum.TRANSACTION_SCREEN);
                }
                default: {
                    sessionContext.clearSession();
                    return screenManager.getScreen(ScreenEnum.WELCOME_SCREEN);
                }
            }
        } catch (Exception e) {
            System.out.println(ErrorConstant.INVALID_ACCOUNT);
            return screenManager.getScreen(ScreenEnum.TRANSACTION_SCREEN);
        }
    }
}
