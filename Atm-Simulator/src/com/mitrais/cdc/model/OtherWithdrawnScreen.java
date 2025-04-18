package com.mitrais.cdc.model;

import com.mitrais.cdc.Main;

import java.util.Scanner;

public class OtherWithdrawnScreen implements Screen {
    private Account account;
    private Scanner userInputScanner;

    public OtherWithdrawnScreen(Account anAccount, Scanner aUserInputScanner) {
        account = anAccount;
        userInputScanner = aUserInputScanner;
    }

    @Override
    public Screen display() {
        System.out.println("Other Withdraw");
        int amount = 0;
        while (amount == 0) {
            System.out.println("Enter amount to withdraw : ");
            String input = userInputScanner.nextLine();
            boolean isInteger = Main.checkStringIsNumberWithRangeLength(input, 1, 4);
            amount = validatedInputAmount(isInteger, input);
        }
        return (new WithdrawSummaryScreen((double) amount, account, userInputScanner)).display();
    }

    private int validatedInputAmount(boolean isInteger, String input) {
        if (!isInteger) {
            System.out.println("Invalid amount");
            return 0;
        }
        int amount = isInteger ? Integer.parseInt(input) : 0;
        if (amount % 10 != 0) {
            System.out.println("Invalid amount");
            return 0;
        }
        if (amount > account.getBalance()) {
            System.out.println("Insufficient balance $" + amount);
            return 0;
        }
        if (amount > 1000) {
            System.out.println("Maximum amount to transfer is $1000");
            return 0;
        }
        return amount;
    }
}
