package com.mitrais.cdc.view;

import com.mitrais.cdc.model.Account;
import com.mitrais.cdc.service.UserInputService;
import com.mitrais.cdc.service.impl.ServiceFactory;

import javax.xml.bind.ValidationException;
import java.util.Scanner;

public class FundTransferSummaryScreen implements Screen {
    private long withdrawAmount;
    private Scanner userInputScanner;
    private Account userAccount;
    private Account destinationAccount;
    private UserInputService userInput;
    private String referenceNumber;

    public FundTransferSummaryScreen(long aWithdrawAmount, Account account, Scanner aUserInputScanner, Account aDestinationAccount, String aReferenceNumber) {
        withdrawAmount = aWithdrawAmount;
        userAccount = account;
        userInputScanner = aUserInputScanner;
        destinationAccount = aDestinationAccount;
        referenceNumber = aReferenceNumber;
        userInput = ServiceFactory.createUserInputService();
    }

    @Override
    public Screen display() {
        System.out.println("Fund Transfer Summary");
        System.out.println("Destination Account : " + destinationAccount.getAccountNumber());
        System.out.println("Transfer Amount : " + withdrawAmount);
        System.out.println("Reference Number : " + referenceNumber);
        System.out.println("Balance : " + userAccount.getBalance());
        System.out.println("");
        System.out.println("1. Transaction");
        System.out.println("2. Exit");
        System.out.print("Please choose option[2] : ");
        String input = userInputScanner.nextLine();
        try {
            int menu = userInput.toValidatedMenu(input);
            switch (menu) {
                case 1: {
                    return new TransactionScreen(userAccount, userInputScanner);
                }
                default: {
                    break;
                }
            }
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
        }
        return new WelcomeScreen(userInputScanner);
    }
}
