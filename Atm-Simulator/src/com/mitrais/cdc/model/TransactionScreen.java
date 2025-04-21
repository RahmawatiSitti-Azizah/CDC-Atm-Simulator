package com.mitrais.cdc.model;

import com.mitrais.cdc.Main;

import java.util.Scanner;

public class TransactionScreen implements Screen {
    private WelcomeScreen welcomeScreen;
    private Scanner userInputScanner;

    public TransactionScreen(WelcomeScreen aWelcomeScreen, Scanner aUserInputScanner) {
        welcomeScreen = aWelcomeScreen;
        userInputScanner = aUserInputScanner;
    }

    @Override
    public Screen display() {
        System.out.println("1. Withdrawn");
        System.out.println("2. Fund Transfer");
        System.out.println("3. Exit");
        System.out.print("Please choose option[3] : ");
        String input = userInputScanner.nextLine();
        Screen nextScreen = null;
        int menu = Main.checkStringIsNumberWithLength(input, 1) ? Integer.parseInt(input) : 0;
        switch (menu) {
            case 1: {
                nextScreen = new WithdrawScreen(welcomeScreen, userInputScanner);
                break;
            }
            case 2: {
                nextScreen = new FundTransferScreen(welcomeScreen, userInputScanner);
                break;
            }
            default:
                nextScreen = welcomeScreen;
                break;
        }
        return nextScreen;
    }
}
