package com.mitrais.cdc.view;

import com.mitrais.cdc.Main;
import com.mitrais.cdc.model.Account;

import javax.xml.bind.ValidationException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class WithdrawSummaryScreen implements Screen {
    private int withdrawAmount;
    private WelcomeScreen welcomeScreen;
    private Scanner userInputScanner;

    public WithdrawSummaryScreen(int aWithdrawAmount, WelcomeScreen aWelcomeScreen, Scanner aUserInputScanner) {
        this.withdrawAmount = aWithdrawAmount;
        this.welcomeScreen = aWelcomeScreen;
        this.userInputScanner = aUserInputScanner;
    }

    @Override
    public Screen display() {
        Account loginAccount = welcomeScreen.getLoginAccount();
        try {
            loginAccount.decreaseBalance(withdrawAmount);
        } catch (ValidationException e) {
            throw new RuntimeException(e);
        }
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
        int menu = Main.checkStringIsNumberWithRangeLength(input, 1, 1) ? Integer.parseInt(input) : 0;
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
