package com.mitrais.cdc.model;

import com.mitrais.cdc.Main;

import java.util.Scanner;

public class FundTransferSummaryScreen implements Screen {
    private double withdrawAmount;
    private WelcomeScreen welcomeScreen;
    private Scanner userInputScanner;
    private Account destinationAccount;
    private String referenceNumber;

    public FundTransferSummaryScreen(double aWithdrawAmount, WelcomeScreen aWelcomeScreen, Scanner aUserInputScanner, Account aDestinationAccount, String aReferenceNumber) {
        withdrawAmount = aWithdrawAmount;
        welcomeScreen = aWelcomeScreen;
        userInputScanner = aUserInputScanner;
        destinationAccount = aDestinationAccount;
        referenceNumber = aReferenceNumber;
    }

    @Override
    public Screen display() {
        Account loginAccount = welcomeScreen.getLoginAccount();
        loginAccount.setBalance(Double.valueOf(loginAccount.getBalance() - withdrawAmount));
        System.out.println("Fund Transfer Summary");
        System.out.println("Destination Account : " + destinationAccount.getAccountNumber());
        System.out.printf("Transfer Amount : $%.0f", Double.valueOf(withdrawAmount));
        System.out.println("");
        System.out.println("Reference Number : " + referenceNumber);
        System.out.printf("Balance : $%.0f", Double.valueOf(loginAccount.getBalance()));
        System.out.println("");
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
