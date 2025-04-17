package com.mitrais.cdc.model;

import com.mitrais.cdc.Main;

import java.util.List;
import java.util.Scanner;

public class FundTransferScreen implements Screen {
    private Account account;
    private Scanner userInputScanner;
    private List<Account> accountList = Main.listLoginAccount;

    public FundTransferScreen(Account anAccount, Scanner aUserInputScanner) {
        account = anAccount;
        userInputScanner = aUserInputScanner;
    }

    @Override
    public Screen display() {
        System.out.println("Please enter destination account and press enter to continue or press cancel (Esc) to go back to Transaction : ");
        int amount = 0;
        while (amount == 0) {
            String input = userInputScanner.nextLine();
            if (input.equals("Esc")) {
                return new TransactionScreen(account, userInputScanner).display();
            } else {

            }
        }
        return (new WithdrawSummaryScreen((double) amount, account, userInputScanner)).display();
    }

    private Account validatedInputAccountNumber(boolean isInteger, String input) {
        if (!isInteger) {
            System.out.println("Invalid account");
            return null;
        }
        int amount = isInteger ? Integer.parseInt(input) : 0;
        Account destinationAccount = null;
        destinationAccount = accountList.stream().filter((Account account) -> account.getAccountNumber().equals(input)).findFirst().get();
        if (destinationAccount == null) {
            System.out.println("Invalid account");
        }
        return destinationAccount;
    }
}
