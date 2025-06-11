package com.mitrais.cdc.view;

import com.mitrais.cdc.model.Account;
import com.mitrais.cdc.model.Dollar;
import com.mitrais.cdc.service.UserInputService;
import com.mitrais.cdc.service.impl.ServiceFactory;

import java.util.Scanner;

public class WithdrawScreen implements Screen {
    private Account userAccount;
    private Scanner userInputScanner;
    private long[] arrayWithdrawAmount = {10, 50, 100};
    private UserInputService userInput;

    public WithdrawScreen(Account account, Scanner aUserInputScanner) {
        userAccount = account;
        userInputScanner = aUserInputScanner;
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
                return new WithdrawSummaryScreen(new Dollar(arrayWithdrawAmount[menu - 1]), userAccount, userInputScanner);
            }
            case 4: {
                return new OtherWithdrawnScreen(userAccount, userInputScanner);
            }
            default: {
                break;
            }
        }
        return new TransactionScreen(userAccount, userInputScanner);
    }
}
