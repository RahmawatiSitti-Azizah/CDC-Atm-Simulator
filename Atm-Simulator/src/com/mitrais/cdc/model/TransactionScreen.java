package com.mitrais.cdc.model;

import com.mitrais.cdc.Main;

import java.util.Scanner;

public class TransactionScreen implements Screen {
    private Account account;
    private Scanner userInputScanner;

    public TransactionScreen(Account anAccount, Scanner aUserInputScanner) {
        account = anAccount;
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
        Integer menu = Main.checkStringIsNumberWithLength(input, 1) ? Integer.parseInt(input) : 0;
        switch (menu) {
            case 1: {
                nextScreen = new WithdrawScreen(account, userInputScanner);
                break;
            }
            case 2: {
                nextScreen = new FundTransferScreen(account, userInputScanner);
                break;
            }
            default:
                break;
        }
        return nextScreen != null ? nextScreen.display() : null;
    }
}
