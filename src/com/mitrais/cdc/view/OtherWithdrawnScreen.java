package com.mitrais.cdc.view;

import com.mitrais.cdc.Main;

import java.util.Scanner;

public class OtherWithdrawnScreen implements Screen {
    private WelcomeScreen welcomeScreen;
    private Scanner userInputScanner;

    public OtherWithdrawnScreen(WelcomeScreen aWelcomeScreen, Scanner aUserInputScanner) {
        welcomeScreen = aWelcomeScreen;
        userInputScanner = aUserInputScanner;
    }

    @Override
    public Screen display() {
        System.out.println("Other Withdraw");
        double amount = 0;
        while (amount == 0) {
            amount = getAmount();
        }
        return new WithdrawSummaryScreen(amount, welcomeScreen, userInputScanner);
    }

    public double getAmount() {
        double amount;
        System.out.print("Enter amount to withdraw : ");
        String input = userInputScanner.nextLine();
        boolean isInteger = Main.checkStringIsNumberWithRangeLength(input, 1, 4);
        amount = validatedInputAmount(isInteger, input);
        return amount;
    }

    private double validatedInputAmount(boolean isInteger, String input) {
        if (!isInteger) {
            System.out.println("Invalid amount");
            return 0;
        }
        double amount = Double.parseDouble(input);
        if (amount % 10 != 0) {
            System.out.println("Invalid amount");
            return 0;
        }
        if (amount > 1000) {
            System.out.println("Maximum amount to transfer is $1000");
            return 0;
        }
        if (amount > welcomeScreen.getLoginAccount().getBalance()) {
            System.out.printf("Insufficient balance $%.0f", amount);
            System.out.println("");
            return 0;
        }
        return amount;
    }
}
