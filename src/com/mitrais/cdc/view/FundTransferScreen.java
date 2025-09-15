package com.mitrais.cdc.view;

import com.mitrais.cdc.model.Account;
import com.mitrais.cdc.model.Money;
import com.mitrais.cdc.service.*;
import com.mitrais.cdc.service.impl.ServiceFactory;
import com.mitrais.cdc.util.ReferenceNumberGenerator;

import java.util.Random;
import java.util.Scanner;

public class FundTransferScreen implements Screen {
    private static final FundTransferScreen INSTANCE = new FundTransferScreen();
    private Scanner userInputScanner;
    private Money transferAmount;
    private Account userAccount;
    private Account destinationAccount;
    private String referenceNumber;
    private AccountTransactionService accountTransactionService;
    private AccountValidatorService accountService;
    private UserInputService userInput;
    private TransactionAmountValidatorService transactionValidate;
    private SearchAccountService searchService;

    public static FundTransferScreen getInstance(Account account, Scanner aUserInputScanner) {
        INSTANCE.userAccount = account;
        INSTANCE.userInputScanner = aUserInputScanner;
        INSTANCE.referenceNumber = ReferenceNumberGenerator.generateTransferRefnumber(new Random());
        return INSTANCE;
    }

    private FundTransferScreen() {
        accountTransactionService = ServiceFactory.createAccountTransactionService();
        accountService = ServiceFactory.createAccountValidatorService();
        userInput = ServiceFactory.createUserInputService();
        transactionValidate = ServiceFactory.createTransactionAmountValidatorService();
        searchService = ServiceFactory.createSearchAccountService();
    }

    @Override
    public Screen display() {
        try {
            setDestinationAccountFromUserInput();
            System.out.print("Please enter transfer amount and press enter to continue or press enter to go back " +
                    "to Transaction : ");
            setTransferAmountFromUserInput();
            printReferenceNumberAndProceed();
            return transferConfirmationProcessAndGetNextScreen();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return TransactionScreen.getInstance(userAccount, userInputScanner);
        }
    }

    private void printReferenceNumberAndProceed() throws Exception {
        String input;
        System.out.print("Reference Number: " + referenceNumber +
                "\npress enter to continue or press enter to go back to Transaction : ");
        input = userInputScanner.nextLine();
        if (!input.isEmpty()) {
            throw new Exception("");

        }
    }

    private void setTransferAmountFromUserInput() throws Exception {
        String input = userInputScanner.nextLine();
        if (input.isEmpty()) {
            throw new Exception("");
        } else {
            transferAmount = userInput.toValidatedMoney(input);
            transactionValidate.validateTransferAmount(transferAmount);
        }
    }

    private void setDestinationAccountFromUserInput() throws Exception {
        System.out.print("Please enter destination account and press enter to continue or press cancel (Esc) " +
                "to go back to Transaction : ");
        String input = userInputScanner.nextLine();
        if ((input.equals("Esc") || input.isEmpty())) {
            throw new Exception("");
        }
        accountService.validateAccountNumber(input, "Invalid Account");
        destinationAccount = searchService.getByID(input);
    }

    private Screen transferConfirmationProcessAndGetNextScreen() {
        System.out.println("Transfer Confirmation");
        System.out.println("Destination Account : " + destinationAccount.getAccountNumber());
        System.out.println("Transfer Amount : " + transferAmount.toString());
        System.out.println("Reference Number : " + referenceNumber);
        System.out.println("");
        System.out.println("1. Confirm Trx");
        System.out.println("2. Cancel Trx");
        System.out.print("Choose option[2] : ");
        String input = userInputScanner.nextLine();
        int menu = userInput.toValidatedMenu(input);
        switch (menu) {
            case 1: {
                try {
                    accountTransactionService.transfer(userAccount, destinationAccount, transferAmount, referenceNumber);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    return TransactionScreen.getInstance(userAccount, userInputScanner);
                }
                return FundTransferSummaryScreen.getInstance(transferAmount, userAccount, userInputScanner, destinationAccount, referenceNumber);
            }
            default: {
                return TransactionScreen.getInstance(userAccount, userInputScanner);
            }
        }
    }

}
