package com.mitrais.cdc.view;

import com.mitrais.cdc.model.Account;
import com.mitrais.cdc.service.AccountTransactionService;
import com.mitrais.cdc.service.AccountValidationService;
import com.mitrais.cdc.service.TransactionValidationService;
import com.mitrais.cdc.service.UserInputService;
import com.mitrais.cdc.service.impl.ServiceFactory;

import javax.xml.bind.ValidationException;
import java.util.Random;
import java.util.Scanner;

public class FundTransferScreen implements Screen {
    private Scanner userInputScanner;
    private long amount;
    private Account userAccount;
    private Account destinationAccount;
    private String referenceNumber;
    private AccountTransactionService accountTransactionService;
    private AccountValidationService accountService;
    private UserInputService userInput;
    private TransactionValidationService transactionValidate;

    public FundTransferScreen(Account account, Scanner aUserInputScanner) {
        userAccount = account;
        userInputScanner = aUserInputScanner;
        accountTransactionService = ServiceFactory.createAccountTransactionService();
        accountService = ServiceFactory.createAccountValidationService();
        userInput = ServiceFactory.createUserInputService();
        transactionValidate = ServiceFactory.createTransactionValidationService();
        generateReferenceNumber(new Random());
    }

    @Override
    public Screen display() {
        try {
            System.out.print("Please enter destination account and press enter to continue or press cancel (Esc) " +
                    "to go back to Transaction : ");
            String input = userInputScanner.nextLine();
            if ((input.equals("Esc") || input.isEmpty())) {
                return new TransactionScreen(userAccount, userInputScanner);
            }
            destinationAccount = accountService.toValidatedAccount(input);
            System.out.print("Please enter transfer amount and press enter to continue or press enter to go back " +
                    "to Transaction : ");
            input = userInputScanner.nextLine();
            if (input.isEmpty()) {
                return new TransactionScreen(userAccount, userInputScanner);
            } else {
                amount = transactionValidate.transferAmount(userAccount, userInput.toValidatedAmount(input));
            }
            generateReferenceNumber(new Random());
            System.out.print("Reference Number: " + referenceNumber +
                    "\npress enter to continue or press enter to go back to Transaction : ");
            input = userInputScanner.nextLine();
            if (input.isEmpty()) {
                return transferConfirmProcess();
            } else {
                return new TransactionScreen(userAccount, userInputScanner);
            }
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
            return new TransactionScreen(userAccount, userInputScanner);
        }
    }

    private void generateReferenceNumber(Random random) {
        StringBuffer stringBuffer = new StringBuffer(String.valueOf(random.nextInt(999999)));
        while (stringBuffer.length() < 6) {
            stringBuffer.append(0);
        }
        referenceNumber = stringBuffer.toString();
    }

    private Screen transferConfirmProcess() {
        System.out.println("Transfer Confirmation");
        System.out.println("Destination Account : " + destinationAccount.getAccountNumber());
        System.out.println("Transfer Amount : $" + amount);
        System.out.println("Reference Number : " + referenceNumber);
        System.out.println("");
        System.out.println("1. Confirm Trx");
        System.out.println("2. Cancel Trx");
        System.out.print("Choose option[2] : ");
        String input = userInputScanner.nextLine();
        int menu = userInput.toValidatedMenu(input);
        switch (menu) {
            case 1: {
                accountTransactionService.transfer(userAccount, destinationAccount, amount);
                return new FundTransferSummaryScreen(amount, userAccount, userInputScanner, destinationAccount, referenceNumber);
            }
            default: {
                return new TransactionScreen(userAccount, userInputScanner);
            }
        }
    }
}
