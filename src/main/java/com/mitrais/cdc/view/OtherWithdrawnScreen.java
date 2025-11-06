package com.mitrais.cdc.view;

import com.mitrais.cdc.model.Money;
import com.mitrais.cdc.service.UserInputService;
import com.mitrais.cdc.util.SessionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class OtherWithdrawnScreen implements Screen {
    private final SessionContext sessionContext;
    private final ScreenManager screenManager;
    private final UserInputService userInputService;
    private Scanner userInputScanner;

    @Autowired
    public OtherWithdrawnScreen(ScreenManager screenManager, UserInputService userInputService, Scanner userInputScanner, SessionContext sessionContext) {
        this.screenManager = screenManager;
        this.userInputService = userInputService;
        this.userInputScanner = userInputScanner;
        this.sessionContext = sessionContext;
    }

    @Override
    public Screen display() {
        if (!sessionContext.isAuthenticated()) {
            System.out.println("Unauthenticated user. Please login.");
            sessionContext.clearSession();
            return screenManager.getScreen(ScreenEnum.WELCOME_SCREEN);
        }
        System.out.println("Other Withdraw");
        Money amount;
        System.out.print("Enter amount to withdraw : ");
        String input = userInputScanner.nextLine();
        try {
            amount = userInputService.toValidatedMoney(input);
            WithdrawSummaryScreen summaryScreen = (WithdrawSummaryScreen) screenManager.getScreen(ScreenEnum.WITHDRAW_SUMMARY_SCREEN);
            summaryScreen.setTransactionAmount(amount);
            return summaryScreen;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return screenManager.getScreen(ScreenEnum.WITHDRAW_SCREEN);
        }
    }
}
