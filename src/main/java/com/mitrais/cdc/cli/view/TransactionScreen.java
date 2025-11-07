package com.mitrais.cdc.cli.view;

import com.mitrais.cdc.cli.SessionContext;
import com.mitrais.cdc.service.UserInputService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class TransactionScreen implements Screen {
    private final SessionContext sessionContext;
    private final UserInputService userInput;
    private final ScreenManager screenManager;
    private Scanner userInputScanner;

    @Autowired
    public TransactionScreen(SessionContext sessionContext, Scanner userInputScanner, UserInputService userInput, ScreenManager screenManager) {
        this.sessionContext = sessionContext;
        this.userInputScanner = userInputScanner;
        this.userInput = userInput;
        this.screenManager = screenManager;
    }

    @Override
    public Screen display() {
        if (!sessionContext.isAuthenticated()) {
            System.out.println("Unauthenticated user. Please login.");
            sessionContext.clearSession();
            return screenManager.getScreen(ScreenEnum.WELCOME_SCREEN);
        }
        System.out.println("1. Withdrawn");
        System.out.println("2. Fund Transfer");
        System.out.println("3. Transaction History");
        System.out.println("4. Exit");
        System.out.print("Please choose option[3] : ");
        String input = userInputScanner.nextLine();
        int menu = userInput.toValidatedMenu(input);
        switch (menu) {
            case 1: {
                return screenManager.getScreen(ScreenEnum.WITHDRAW_SCREEN);
            }
            case 2: {
                return screenManager.getScreen(ScreenEnum.FUND_TRANSFER_SCREEN);
            }
            case 3: {
                return screenManager.getScreen(ScreenEnum.HISTORY_TRANSACTION_SCREEN);
            }
            default: {
                break;
            }
        }
        sessionContext.clearSession();
        return screenManager.getScreen(ScreenEnum.WELCOME_SCREEN);
    }
}
