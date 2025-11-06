package com.mitrais.cdc.view;

import com.mitrais.cdc.model.Dollar;
import com.mitrais.cdc.service.UserInputService;
import com.mitrais.cdc.util.SessionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class WithdrawScreen implements Screen {
    private final SessionContext sessionContext;
    private final UserInputService userInput;
    private final ScreenManager screenManager;
    private final double[] arrayWithdrawAmount = {10.0, 50.0, 100.0};
    private Scanner userInputScanner;

    @Autowired
    public WithdrawScreen(ScreenManager screenManager, UserInputService userInput, Scanner userInputScanner, SessionContext sessionContext) {
        this.screenManager = screenManager;
        this.userInput = userInput;
        this.userInputScanner = userInputScanner;
        this.sessionContext = sessionContext;
    }

    @Override
    public Screen display() {
        if (!sessionContext.isAuthenticated()) {
            System.out.println("Unauthenticated user. Please login.");
            return screenManager.getScreen(ScreenEnum.WELCOME_SCREEN);
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
                WithdrawSummaryScreen summaryScreen = (WithdrawSummaryScreen) screenManager.getScreen(ScreenEnum.WITHDRAW_SUMMARY_SCREEN);
                summaryScreen.setTransactionAmount(new Dollar(arrayWithdrawAmount[menu - 1]));
                return summaryScreen;
            }
            case 4: {
                return screenManager.getScreen(ScreenEnum.OTHER_WITHDRAW_SCREEN);
            }
            default: {
                break;
            }
        }
        return screenManager.getScreen(ScreenEnum.TRANSACTION_SCREEN);
    }
}
