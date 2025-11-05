package com.mitrais.cdc.view;

import com.mitrais.cdc.model.Money;
import com.mitrais.cdc.model.dto.AccountDto;
import com.mitrais.cdc.service.UserInputService;
import com.mitrais.cdc.util.SessionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class OtherWithdrawnScreen implements Screen {
    private static OtherWithdrawnScreen INSTANCE;
    private final SessionContext sessionContext;
    private final WithdrawSummaryScreen summaryScreen;
    private final UserInputService userInputService;
    private Scanner userInputScanner;
    private Screen previousScreen;

    @Autowired
    public OtherWithdrawnScreen(WithdrawSummaryScreen summaryScreen, UserInputService userInputService, Scanner userInputScanner, SessionContext sessionContext) {
        this.summaryScreen = summaryScreen;
        this.userInputService = userInputService;
        this.userInputScanner = userInputScanner;
        this.sessionContext = sessionContext;
    }

    public static OtherWithdrawnScreen getInstance(AccountDto account, Scanner aUserInputScanner, UserInputService userInputService) {
        if (INSTANCE == null) {
            INSTANCE = new OtherWithdrawnScreen(userInputService);
        }
        INSTANCE.userInputScanner = aUserInputScanner;
        return INSTANCE;
    }

    private OtherWithdrawnScreen(UserInputService userInputService) {
        this.userInputService = userInputService;
        this.summaryScreen = null;
        this.sessionContext = null;
    }

    public void setPreviousScreen(Screen previousScreen) {
        this.previousScreen = previousScreen;
    }

    @Override
    public Screen display() {
        if (!sessionContext.isAuthenticated()) {
            System.out.println("Unauthenticated user. Please login.");
            return previousScreen;
        }
        System.out.println("Other Withdraw");
        Money amount;
        System.out.print("Enter amount to withdraw : ");
        String input = userInputScanner.nextLine();
        try {
            amount = userInputService.toValidatedMoney(input);
            summaryScreen.setTransactionAmount(amount);
            summaryScreen.setPreviousScreen(previousScreen);
            return summaryScreen;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return previousScreen;
        }
    }
}
