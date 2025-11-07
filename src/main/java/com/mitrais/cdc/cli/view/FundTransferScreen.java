package com.mitrais.cdc.cli.view;

import com.mitrais.cdc.cli.SessionContext;
import com.mitrais.cdc.model.Money;
import com.mitrais.cdc.model.dto.AccountDto;
import com.mitrais.cdc.service.*;
import com.mitrais.cdc.util.ReferenceNumberGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.Scanner;

@Component
public class FundTransferScreen implements Screen {
    private final SessionContext sessionContext;
    private final AccountTransactionService accountTransactionService;
    private final AccountValidatorService accountService;
    private final UserInputService userInput;
    private final TransactionAmountValidatorService transactionValidate;
    private final SearchAccountService searchService;
    private final ScreenManager screenManager;
    private Scanner userInputScanner;
    private Money transferAmount;
    private AccountDto destinationAccount;
    private String referenceNumber;

    @Autowired
    public FundTransferScreen(AccountValidatorService accountService, SessionContext sessionContext, AccountTransactionService accountTransactionService, UserInputService userInput, TransactionAmountValidatorService transactionValidate, SearchAccountService searchService, ScreenManager screenManager, Scanner userInputScanner) {
        this.accountService = accountService;
        this.sessionContext = sessionContext;
        this.accountTransactionService = accountTransactionService;
        this.userInput = userInput;
        this.transactionValidate = transactionValidate;
        this.searchService = searchService;
        this.screenManager = screenManager;
        this.userInputScanner = userInputScanner;
    }

    @Override
    public Screen display() {
        if (!sessionContext.isAuthenticated()) {
            System.out.println("Unauthenticated user. Please login.");
            sessionContext.clearSession();
            return screenManager.getScreen(ScreenEnum.WELCOME_SCREEN);
        }
        try {
            setDestinationAccountFromUserInput();
            System.out.print("Please enter transfer amount and press enter to continue or press enter to go back " +
                    "to Transaction : ");
            setTransferAmountFromUserInput();
            printReferenceNumberAndProceed();
            return transferConfirmationProcessAndGetNextScreen();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return screenManager.getScreen(ScreenEnum.TRANSACTION_SCREEN);
        }
    }

    private void printReferenceNumberAndProceed() throws Exception {
        referenceNumber = ReferenceNumberGenerator.generateTransferRefnumber(new Random());
        System.out.print("Reference Number: " + referenceNumber +
                "\npress enter to continue or press enter to go back to Transaction : ");
        String input = userInputScanner.nextLine();
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
        destinationAccount = searchService.get(input);
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
                    AccountDto session = sessionContext.getSession();
                    accountTransactionService.transfer(session, destinationAccount, transferAmount, referenceNumber);
                    FundTransferSummaryScreen summaryScreen = (FundTransferSummaryScreen) screenManager.getScreen(ScreenEnum.FUND_TRANSFER_SUMMARY);
                    summaryScreen.setReferenceNumber(referenceNumber);
                    return summaryScreen;
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    return screenManager.getScreen(ScreenEnum.TRANSACTION_SCREEN);
                }
            }
            default: {
                return screenManager.getScreen(ScreenEnum.TRANSACTION_SCREEN);
            }
        }
    }

}
