package com.mitrais.cdc.model;

import com.mitrais.cdc.Main;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class WithdrawSummaryScreen implements Screen {
    private Double withdrawAmount;
    private Account account;
    private Scanner userInputScanner;

    public WithdrawSummaryScreen(Double withdrawAmount, Account account, Scanner userInputScanner) {
        this.withdrawAmount = withdrawAmount;
        this.account = account;
        this.userInputScanner = userInputScanner;
    }

    @Override
    public Screen display() {
        account.setBalance(account.getBalance() - withdrawAmount);
        System.out.println("Summary");
        System.out.println("Date : " + (new SimpleDateFormat("yyyy-MM-dd hh:mm a")).format(new Date()));
        System.out.printf("Withdraw : $%.0f", withdrawAmount);
        System.out.println("");
        System.out.printf("Balance : $%.0f", account.getBalance());
        System.out.println("");
        System.out.println("");
        System.out.println("1. Transaction");
        System.out.println("2. Exit");
        System.out.print("Please choose option[2] : ");
        String input = userInputScanner.nextLine();
        Screen nextScreen;
        Integer menu = Main.checkStringIsNumberWithLength(input, 1) ? Integer.parseInt(input) : 0;
        switch (menu) {
            case 1: {
                nextScreen = new TransactionScreen(account, userInputScanner);
                break;
            }
            default: {
                nextScreen = null;
                break;
            }
        }
        return nextScreen != null ? nextScreen.display() : null;
    }
}
