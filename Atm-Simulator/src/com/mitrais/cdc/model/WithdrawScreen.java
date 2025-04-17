package com.mitrais.cdc.model;

import com.mitrais.cdc.Main;

import java.util.Scanner;

public class WithdrawScreen implements Screen {
    private Account account;
    private Scanner userInputScanner;
    private Double[] arrayWithdrawAmount = {10.0, 50.0, 100.0};

    public WithdrawScreen(Account anAccount, Scanner aUserInputScanner) {
        account = anAccount;
        userInputScanner = aUserInputScanner;
    }

    @Override
    public Screen display() {
        int i = 1;
        for (Double amount : arrayWithdrawAmount) {
            System.out.println(i++ + ". $" + amount);
        }
        System.out.println(i++ + ". Other");
        System.out.println(i++ + ". Back");
        Screen nextScreen = null;
        Integer menu = validateUserInput();
        while (!withdrawAmountIsValid(menu)) {
            menu = validateUserInput();
        }
        switch (menu) {
            case 1: {
                nextScreen = new WithdrawSummaryScreen(10.0, account, userInputScanner);
                break;
            }
            case 2: {
                nextScreen = new WithdrawSummaryScreen(50.0, account, userInputScanner);
                break;
            }
            case 3: {
                nextScreen = new WithdrawSummaryScreen(100.0, account, userInputScanner);
                break;
            }
            case 4: {
                nextScreen = new OtherWithdrawnScreen(account, userInputScanner);
                break;
            }
            default: {
                nextScreen = new TransactionScreen(account, userInputScanner);
                break;
            }
        }
        return nextScreen != null ? nextScreen.display() : null;
    }

    private Integer validateUserInput() {
        System.out.print("Please choose option[5] : ");
        String input = userInputScanner.nextLine();
        Integer menu = Main.checkStringIsNumberWithLength(input, 1) ? Integer.parseInt(input) : 0;
        return menu;
    }

    private boolean withdrawAmountIsValid(Integer menu) {
        Double balance = account.getBalance();
        int withdrawAmountIndex = menu - 1;
        if (withdrawAmountIndex >= 0 && withdrawAmountIndex <= arrayWithdrawAmount.length - 1 && balance < arrayWithdrawAmount[withdrawAmountIndex]) {
            System.out.printf("Insufficient balance $%f.0", balance);
            System.out.println("");
            return false;
        }
        return true;
    }
}
