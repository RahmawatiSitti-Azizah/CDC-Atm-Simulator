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
        userInput = ServiceFactory.createUserInputService();
    }

    public static TransactionScreen getInstance(Account userAccount, Scanner userInputScanner) {
        INSTANCE.userAccount = userAccount;
        INSTANCE.userInputScanner = userInputScanner;
        return INSTANCE;
    }


    @Override
    public Screen display() {
        System.out.println("1. Withdrawn");
        System.out.println("2. Fund Transfer");
        System.out.println("3. Transaction History");
        System.out.println("4. Exit");
        System.out.print("Please choose option[3] : ");
        String input = userInputScanner.nextLine();
        int menu = userInput.toValidatedMenu(input);
        switch (menu) {
            case 1: {
                return WithdrawScreen.getInstance(userAccount, userInputScanner);
            }
            case 2: {
                return FundTransferScreen.getInstance(userAccount, userInputScanner);
            }
            case 3: {
                return HistoryTransactionScreen.getInstance(userAccount, userInputScanner);
            }
            default: {
                break;
            }
        }
        return WelcomeScreen.getInstance(null, userInputScanner);
    }
}
