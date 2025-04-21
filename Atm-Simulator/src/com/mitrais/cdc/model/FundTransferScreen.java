package com.mitrais.cdc.model;

import com.mitrais.cdc.Main;

import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class FundTransferScreen implements Screen {
    private WelcomeScreen welcomeScreen;
    private Scanner userInputScanner;
    private List<Account> accountList = Main.listLoginAccount;
    private double amount;
    private Account destinationAccount;
    private String referenceNumber;

    public FundTransferScreen(WelcomeScreen aWelcomeScreen, Scanner aUserInputScanner) {
        welcomeScreen = aWelcomeScreen;
        userInputScanner = aUserInputScanner;
    }

    @Override
    public Screen display() {
        destinationAccount = getDestinationAccountProcess();
        if (destinationAccount == null) {
            return new TransactionScreen(welcomeScreen, userInputScanner);
        }
        amount = getAmountProcess();
        if (amount == 0) {
            return new TransactionScreen(welcomeScreen, userInputScanner);
        }
        return getReferenceProcess();
    }

    public Account getDestinationAccountProcess() {
        while (destinationAccount == null) {
            System.out.println("Please enter destination account and press enter to continue or press cancel (Esc) " +
                    "to go back to Transaction : ");
            String input = userInputScanner.nextLine();
            if (input != null && (input.equals("Esc") || input.isEmpty())) {
                return null;
            }
            boolean isInteger = Main.checkStringIsNumberWithLength(input, 6);
            return validatedInputAccountNumber(isInteger, input);
        }
        return destinationAccount;
    }

    private Account validatedInputAccountNumber(boolean isInteger, String input) {
        if (!isInteger) {
            System.out.println("Invalid account");
            return null;
        }
        Account destinationAccount = null;
        destinationAccount = accountList.stream().filter((Account account) -> account.getAccountNumber().equals(input)).findFirst().get();
        if (destinationAccount == null) {
            System.out.println("Invalid account");
        }
        return destinationAccount;
    }

    private double getAmountProcess() {
        while (amount == 0) {
            System.out.println("Please enter transfer amount and press enter to continue or press enter to go back " +
                    "to Transaction");
            String input = userInputScanner.nextLine();
            if (input == null || input.isEmpty()) {
                return 0;
            } else {
                boolean isInteger = Main.checkStringIsNumberWithRangeLength(input, 1, 4);
                amount = validatedInputAmount(isInteger, input);
            }
        }
        return amount;
    }

    private double validatedInputAmount(boolean isInteger, String input) {
        if (!isInteger) {
            System.out.println("Invalid amount");
            return (Double) 0.0;
        }
        double amount = (Double) (isInteger ? Double.parseDouble(input) : 0);
        if (amount < 1) {
            System.out.println("Minimum amount to transfer is $1");
            return (Double) 0.0;
        }
        if (amount > welcomeScreen.getLoginAccount().getBalance()) {
            System.out.println("Insufficient balance $" + amount);
            return (Double) 0.0;
        }
        if (amount > 1000) {
            System.out.println("Maximum amount to transfer is $1000");
            return (Double) 0.0;
        }
        return amount;
    }

    private Screen getReferenceProcess() {
        Random referenceGenerator = new Random();
        referenceNumber = String.valueOf(referenceGenerator.nextInt(999999));
        boolean repeat = true;
        Screen nextScreen = null;
        while (repeat) {
            System.out.println("Reference Number: " + referenceNumber +
                    "press enter to continue or press enter to go back to Transaction: ");
            String input = userInputScanner.nextLine();
            if (input == null || input.isEmpty()) {
                repeat = false;
                nextScreen = new TransactionScreen(welcomeScreen, userInputScanner);
            } else {
                boolean isInteger = Main.checkStringIsNumberWithLength(input, 6);
                if (!isInteger) {
                    System.out.println("Invalid Reference Number");
                } else if (referenceNumber.equals(input)) {
                    repeat = false;
                    nextScreen = transferConfirmProcess();11
                } else {
                    System.out.println("Invalid Reference Number");
                }
            }
        }
        return nextScreen;
    }

    private Screen transferConfirmProcess() {
        System.out.println("Transfer Confirmation");
        System.out.println("Destination Account : " + destinationAccount.getAccountNumber());
        System.out.printf("Transfer Amount : $%.0f" + amount);
        System.out.println("");
        System.out.println("Reference Number : " + referenceNumber);
        System.out.println("");
        System.out.println("1. Confirm Trx");
        System.out.println("2. Cancel Trx");
        System.out.print("Choose option[2] : ");
        String input = userInputScanner.nextLine();
        if (input.equals("1")) {
            welcomeScreen.getLoginAccount().setBalance(welcomeScreen.getLoginAccount().getBalance() - amount);
            return new FundTransferSummaryScreen(amount, welcomeScreen, userInputScanner, destinationAccount, referenceNumber);
        } else {
            return new TransactionScreen(welcomeScreen, userInputScanner);
        }
    }
}
