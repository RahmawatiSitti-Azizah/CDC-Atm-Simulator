package com.mitrais.cdc.model;

import com.mitrais.cdc.Main;

import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Pattern;

public class FundTransferScreen implements Screen {
    private WelcomeScreen welcomeScreen;
    private Scanner userInputScanner;
    private double amount;
    private Account destinationAccount;
    private String referenceNumber;

    public FundTransferScreen(WelcomeScreen aWelcomeScreen, Scanner aUserInputScanner) {
        welcomeScreen = aWelcomeScreen;
        userInputScanner = aUserInputScanner;
        setReferenceNumber(new Random());
    }

    public FundTransferScreen(WelcomeScreen aWelcomeScreen, Scanner aUserInputScanner, long seed) {
        welcomeScreen = aWelcomeScreen;
        userInputScanner = aUserInputScanner;
        setReferenceNumber(new Random(seed));
    }

    @Override
    public Screen display() {
        destinationAccount = getDestinationAccountProcess();
        if (destinationAccount == null) {
            return new TransactionScreen(welcomeScreen, userInputScanner);
        }
        amount = getAmountProcess();
        if (amount == 0.0) {
            return new TransactionScreen(welcomeScreen, userInputScanner);
        }
        return getReferenceProcess();
    }

    private Account getDestinationAccountProcess() {
        Account account = null;
        while (account == null) {
            System.out.print("Please enter destination account and press enter to continue or press cancel (Esc) " +
                    "to go back to Transaction : ");
            String input = userInputScanner.nextLine();
            if ((input.equals("Esc") || input.isEmpty())) {
                return null;
            }
            boolean isInteger = Main.checkStringIsNumberWithLength(input, 6);
            account = validatedInputAccountNumber(isInteger, input);
        }
        return account;
    }

    private Account validatedInputAccountNumber(boolean isInteger, String input) {
        if (!isInteger) {
            System.out.println("Invalid account");
            return null;
        }
        Account destAccount = null;
        try {
            destAccount = welcomeScreen.getLoginListAccount().stream().filter((Account account) -> account.getAccountNumber().equals(input)).findFirst().get();
        } catch (NoSuchElementException exception) {
            System.out.println("Invalid account");
            return null;
        }
        return destAccount;
    }

    private double getAmountProcess() {
        double amount = 0;
        while (amount == 0) {
            System.out.print("Please enter transfer amount and press enter to continue or press enter to go back " +
                    "to Transaction : ");
            String input = userInputScanner.nextLine();
            if (input.isEmpty()) {
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
            return 0;
        }
        double amount = Double.parseDouble(input);
        if (amount < 1) {
            System.out.println("Minimum amount to transfer is $1");
            return 0;
        }
        if (amount > 1000) {
            System.out.println("Maximum amount to transfer is $1000");
            return 0;
        }
        if (amount > welcomeScreen.getLoginAccount().getBalance()) {
            System.out.println("Insufficient balance $" + amount);
            return 0;
        }
        return amount;
    }

    private Screen getReferenceProcess() {
        boolean repeat = true;
        Screen nextScreen = null;
        while (repeat) {
            System.out.print("Reference Number: " + getReferenceNumber() +
                    "\npress enter to continue or press enter to go back to Transaction : ");
            String input = userInputScanner.nextLine();
            if (input.isEmpty()) {
                repeat = false;
                nextScreen = new TransactionScreen(welcomeScreen, userInputScanner);
            } else {
                boolean isInteger = !Pattern.compile("\\D").matcher(input).find();
                if (!isInteger) {
                    System.out.println("Invalid Reference Number");
                } else if (referenceNumber.equals(input)) {
                    repeat = false;
                    nextScreen = transferConfirmProcess();
                } else {
                    System.out.println("Invalid Reference Number");
                }
            }
        }
        return nextScreen;
    }

    private void setReferenceNumber(Random random) {
        StringBuffer stringBuffer = new StringBuffer(String.valueOf(random.nextInt(999999)));
        while (stringBuffer.length() < 6) {
            stringBuffer.append(0);
        }
        referenceNumber = stringBuffer.toString();
    }

    private Screen transferConfirmProcess() {
        System.out.println("Transfer Confirmation");
        System.out.println("Destination Account : " + destinationAccount.getAccountNumber());
        System.out.printf("Transfer Amount : $%.0f", amount);
        System.out.println("");
        System.out.println("Reference Number : " + referenceNumber);
        System.out.println("");
        System.out.println("1. Confirm Trx");
        System.out.println("2. Cancel Trx");
        System.out.print("Choose option[2] : ");
        String input = userInputScanner.nextLine();
        if (input.equals("1")) {
            welcomeScreen.getLoginAccount().setBalance(welcomeScreen.getLoginAccount().getBalance() - amount);
            destinationAccount.setBalance(destinationAccount.getBalance() + amount);
            return new FundTransferSummaryScreen(amount, welcomeScreen, userInputScanner, destinationAccount, referenceNumber);
        } else {
            return new TransactionScreen(welcomeScreen, userInputScanner);
        }
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }
}
