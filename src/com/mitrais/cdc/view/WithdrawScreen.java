package com.mitrais.cdc.view;

import com.mitrais.cdc.model.Account;
import com.mitrais.cdc.model.Dollar;
import com.mitrais.cdc.service.UserInputService;
import com.mitrais.cdc.service.impl.ServiceFactory;

import java.util.Scanner;

public class WithdrawScreen implements Screen {
    private static final WithdrawScreen INSTANCE = new WithdrawScreen();
    private Account userAccount;
    private Scanner userInputScanner;
    private long[] arrayWithdrawAmount = {10, 50, 100};
    private UserInputService userInput;

    public static WithdrawScreen getInstance(Account account, Scanner aUserInputScanner) {
        INSTANCE.userAccount = account;
        INSTANCE.userInputScanner = aUserInputScanner;
        return INSTANCE;
    }

    private WithdrawScreen() {
        userInput = ServiceFactory.createUserInputService();
    }

    @Override
    public Screen display() {
        int menu = -1;
        while (menu < 0) {
            int i = 1;
            for (long amount : arrayWithdrawAmount) {
                System.out.println(i++ + ". $" + amount);
            }
            System.out.println(i++ + ". Other");
            System.out.println(i++ + ". Back");
            System.out.print("Please choose option[5] : ");
            String input = userInputScanner.nextLine();
            menu = userInput.toValidatedMenu(input);
        }
        switch (menu) {
            case 1:
            case 2:
            case 3: {
                return WithdrawSummaryScreen.getInstance(new Dollar(arrayWithdrawAmount[menu - 1]), userAccount, userInputScanner);
            }
            case 4: {
                return OtherWithdrawnScreen.getInstance(userAccount, userInputScanner);
            }
            default: {
                break;
            }
        }
        return TransactionScreen.getInstance(userAccount, userInputScanner);
    }
}
