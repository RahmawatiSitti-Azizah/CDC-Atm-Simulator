package com.mitrais.cdc.view;

import com.mitrais.cdc.model.Account;
import com.mitrais.cdc.model.Money;
import com.mitrais.cdc.service.UserInputService;
import com.mitrais.cdc.service.impl.ServiceFactory;

import java.util.Scanner;

public class FundTransferSummaryScreen implements Screen {
    private static final FundTransferSummaryScreen INSTANCE = new FundTransferSummaryScreen();
    private Scanner userInputScanner;
    private Account userAccount;
    private Account destinationAccount;
    private UserInputService userInput;
    private String referenceNumber;
    private Money transferAmount;

    public static FundTransferSummaryScreen getInstance() {
        return INSTANCE;
    }

    private FundTransferSummaryScreen() {
        this(null, null, null, null, null);
    }

    public FundTransferSummaryScreen(Money transferAmount, Account userAccount, Scanner userInputScanner, Account destinationAccount, String referenceNumber) {
        resetData(transferAmount, userAccount, userInputScanner, destinationAccount, referenceNumber);
    }

    public void resetData(Money transferAmount, Account userAccount, Scanner userInputScanner, Account destinationAccount, String referenceNumber) {
        this.userAccount = userAccount;
        this.userInputScanner = userInputScanner;
        this.destinationAccount = destinationAccount;
        this.referenceNumber = referenceNumber;
        this.transferAmount = transferAmount;
        this.userInput = ServiceFactory.createUserInputService();
    }

    @Override
    public Screen display() {
        System.out.println("Fund Transfer Summary");
        System.out.println("Destination Account : " + destinationAccount.getAccountNumber());
        System.out.println("Transfer Amount : " + transferAmount.toString());
        System.out.println("Reference Number : " + referenceNumber);
        System.out.println("Balance : " + userAccount.getStringBalance());
        System.out.println("");
        System.out.println("1. Transaction");
        System.out.println("2. Exit");
        System.out.print("Please choose option[2] : ");
        String input = userInputScanner.nextLine();
        int menu = userInput.toValidatedMenu(input);
        switch (menu) {
            case 1: {
                return TransactionScreen.getInstance(userAccount, userInputScanner);
            }
            default: {
                return WelcomeScreen.getInstance(null, userInputScanner);
            }
        }
    }
}
