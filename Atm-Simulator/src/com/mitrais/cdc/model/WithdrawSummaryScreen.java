package com.mitrais.cdc.model;

import com.mitrais.cdc.Main;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class WithdrawSummaryScreen implements Screen {
    private double withdrawAmount;
    private WelcomeScreen welcomeScreen;
    private Scanner userInputScanner;

    public WithdrawSummaryScreen(double aWithdrawAmount, WelcomeScreen aWelcomeScreen, Scanner aUserInputScanner) {
        this.withdrawAmount = aWithdrawAmount;
        this.welcomeScreen = aWelcomeScreen;
        this.userInputScanner = aUserInputScanner;
    }

    @Override
    public Screen display() {
        Account loginAccount = welcomeScreen.getLoginAccount();
        loginAccount.setBalance((Double) (loginAccount.getBalance() - withdrawAmount));
        System.out.println("Summary");
        System.out.println("Date : " + (new SimpleDateFormat("yyyy-MM-dd hh:mm a")).format(new Date()));
        System.out.printf("Withdraw : $%.0f", Double.valueOf(withdrawAmount));
        System.out.println("");
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
