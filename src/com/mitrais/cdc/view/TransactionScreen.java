package com.mitrais.cdc.view;

import com.mitrais.cdc.model.Account;
import com.mitrais.cdc.service.UserInputService;
import com.mitrais.cdc.service.impl.ServiceFactory;

import java.util.Scanner;

public class TransactionScreen implements Screen {
    private static final TransactionScreen INSTANCE = new TransactionScreen();
    private Scanner userInputScanner;
    private Account userAccount;
    private UserInputService userInput;

    private TransactionScreen() {
        userAccount = null;
        userInputScanner = null;
        userInput = ServiceFactory.createUserInputService();
    }

    public static TransactionScreen getInstance(Account userAccount, Scanner userInputScanner) {
        TransactionScreen transactionScreen = INSTANCE;
        transactionScreen.userAccount = userAccount;
        transactionScreen.userInputScanner = userInputScanner;
        transactionScreen.userInput = ServiceFactory.createUserInputService();
        return transactionScreen;
    }


    @Override
    public Screen display() {
        System.out.println("1. Withdrawn");
        System.out.println("2. Fund Transfer");
        System.out.println("3. Exit");
        System.out.print("Please choose option[3] : ");
        String input = userInputScanner.nextLine();
        int menu = userInput.toValidatedMenu(input);
        switch (menu) {
            case 1: {
                return new WithdrawScreen(userAccount, userInputScanner);
            }
            case 2: {
                return new FundTransferScreen(userAccount, userInputScanner);
            }
            default: {
                break;
            }
        }
        return WelcomeScreen.getInstance(null, userInputScanner);
    }
}
