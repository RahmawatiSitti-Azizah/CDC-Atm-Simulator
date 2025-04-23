package com.mitrais.cdc.model;

import com.mitrais.cdc.Main;

import java.util.Scanner;

public class WithdrawScreen implements Screen {
    private WelcomeScreen welcomeScreen;
    private Scanner userInputScanner;
    private double[] arrayWithdrawAmount = {10.0, 50.0, 100.0};

    public WithdrawScreen(WelcomeScreen aWelcomeScreen, Scanner aUserInputScanner) {
        welcomeScreen = aWelcomeScreen;
        userInputScanner = aUserInputScanner;
    }

    @Override
    public Screen display() {
        int i = 1;
        for (double amount : arrayWithdrawAmount) {
            System.out.printf(i++ + ". $%.0f", amount);
            System.out.println("");
        }
        System.out.println(i++ + ". Other");
        System.out.println(i++ + ". Back");
        Screen nextScreen = null;
        int menu = validateUserInput();
        while (!withdrawAmountIsValid(menu)) {
            menu = validateUserInput();
        }
        switch (menu) {
            case 1: {
                nextScreen = new WithdrawSummaryScreen(10.0, welcomeScreen, userInputScanner);
                break;
            }
            case 2: {
                nextScreen = new WithdrawSummaryScreen(50.0, welcomeScreen, userInputScanner);
                break;
            }
            case 3: {
                nextScreen = new WithdrawSummaryScreen(100.0, welcomeScreen, userInputScanner);
                break;
            }
            case 4: {
                nextScreen = new OtherWithdrawnScreen(welcomeScreen, userInputScanner);
                break;
            }
            default: {
                nextScreen = new TransactionScreen(welcomeScreen, userInputScanner);
                break;
            }
        }
        return nextScreen;
    }

    private int validateUserInput() {
        System.out.print("Please choose option[5] : ");
        String input = userInputScanner.nextLine();
        int menu = Main.checkStringIsNumberWithLength(input, 1) ? Integer.parseInt(input) : 0;
        return menu;
    }

    private boolean withdrawAmountIsValid(int menu) {
        Double balance = welcomeScreen.getLoginAccount().getBalance();
        int withdrawAmountIndex = menu - 1;
        if (withdrawAmountIndex >= 0 && withdrawAmountIndex <= arrayWithdrawAmount.length - 1 && balance < arrayWithdrawAmount[withdrawAmountIndex]) {
            System.out.printf("Insufficient balance $%.0f", balance);
            System.out.println("");
            return false;
        }
        return true;
    }
}
