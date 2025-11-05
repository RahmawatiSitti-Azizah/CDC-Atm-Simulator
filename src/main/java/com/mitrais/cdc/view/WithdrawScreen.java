package com.mitrais.cdc.view;

import com.mitrais.cdc.model.Dollar;
import com.mitrais.cdc.model.dto.AccountDto;
import com.mitrais.cdc.service.UserInputService;
import com.mitrais.cdc.util.SessionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class WithdrawScreen implements Screen {
    private static WithdrawScreen INSTANCE;
    private final OtherWithdrawnScreen otherWithdrawnScreen;
    private final WithdrawSummaryScreen summaryScreen;
    private final SessionContext sessionContext;
    private final UserInputService userInput;
    private final double[] arrayWithdrawAmount = {10.0, 50.0, 100.0};
    private Scanner userInputScanner;
    private Screen previousScreen;

    @Autowired
    public WithdrawScreen(OtherWithdrawnScreen otherWithdrawnScreen, WithdrawSummaryScreen summaryScreen, UserInputService userInput, Scanner userInputScanner, SessionContext sessionContext) {
        this.otherWithdrawnScreen = otherWithdrawnScreen;
        this.summaryScreen = summaryScreen;
        this.userInput = userInput;
        this.userInputScanner = userInputScanner;
        this.sessionContext = sessionContext;
    }

    public void setPreviousScreen(Screen previousScreen) {
        this.previousScreen = previousScreen;
    }

    public static WithdrawScreen getInstance(AccountDto account, Scanner aUserInputScanner, UserInputService userInputService) {
        if (INSTANCE == null) {
            INSTANCE = new WithdrawScreen(userInputService);
        }
        INSTANCE.userInputScanner = aUserInputScanner;
        return INSTANCE;
    }

    private WithdrawScreen(UserInputService userInputService) {
        userInput = userInputService;
        otherWithdrawnScreen = null;
        summaryScreen = null;
        sessionContext = null;
    }

    @Override
    public Screen display() {
        if (!sessionContext.isAuthenticated()) {
            System.out.println("Unauthenticated user. Please login.");
            return previousScreen;
        }
        int menu = -1;
        while (menu < 0) {
            int i = 1;
            for (double amount : arrayWithdrawAmount) {
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
                summaryScreen.setTransactionAmount(new Dollar(arrayWithdrawAmount[menu - 1]));
                setPreviousScreen(this);
                return summaryScreen;
            }
            case 4: {
                otherWithdrawnScreen.setPreviousScreen(this);
                return otherWithdrawnScreen;
            }
            default: {
                break;
            }
        }
        return previousScreen;
    }
}
