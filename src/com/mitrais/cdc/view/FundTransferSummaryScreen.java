package com.mitrais.cdc.view;

import com.mitrais.cdc.Main;
import com.mitrais.cdc.model.Account;

import java.util.Scanner;

public class FundTransferSummaryScreen implements Screen {
    private int withdrawAmount;
    private WelcomeScreen welcomeScreen;
    private Scanner userInputScanner;
    private Account destinationAccount;
    private String referenceNumber;

    public FundTransferSummaryScreen(int aWithdrawAmount, WelcomeScreen aWelcomeScreen, Scanner aUserInputScanner, Account aDestinationAccount, String aReferenceNumber) {
        withdrawAmount = aWithdrawAmount;
        welcomeScreen = aWelcomeScreen;
        userInputScanner = aUserInputScanner;
        destinationAccount = aDestinationAccount;
        referenceNumber = aReferenceNumber;
    }

    @Override
    public Screen display() {
        Account loginAccount = welcomeScreen.getLoginAccount();
        System.out.println("Fund Transfer Summary");
        System.out.println("Destination Account : " + destinationAccount.getAccountNumber());
        System.out.println("Transfer Amount : " + withdrawAmount);
        System.out.println("Reference Number : " + referenceNumber);
        System.out.println("Balance : " + loginAccount.getBalance());
        System.out.println("");
        System.out.println("1. Transaction");
        System.out.println("2. Exit");
        System.out.print("Please choose option[2] : ");
        String input = userInputScanner.nextLine();
        Screen nextScreen;
        int menu = Main.checkStringIsNumberWithLength(input, 1) ? Integer.parseInt(input) : 0;
        switch (menu) {
            case 1: {
                nextScreen = new TransactionScreen(welcomeScreen, userInputScanner);
                break;
            }
            default: {
                nextScreen = welcomeScreen;
                break;
            }
        }
        return nextScreen;
    }
}
