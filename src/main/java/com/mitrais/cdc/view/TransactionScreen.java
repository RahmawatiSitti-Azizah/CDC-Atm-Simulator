package com.mitrais.cdc.view;

import com.mitrais.cdc.model.dto.AccountDto;
import com.mitrais.cdc.service.UserInputService;
import com.mitrais.cdc.util.SessionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class TransactionScreen implements Screen {
    private static TransactionScreen INSTANCE;
    private final SessionContext sessionContext;
    private final UserInputService userInput;
    private final WithdrawScreen withdrawScreen;
    private final FundTransferScreen fundTransferScreen;
    private final HistoryTransactionScreen historyTransactionScreen;
    private Scanner userInputScanner;
    private Screen previousScreen;

    @Autowired
    public TransactionScreen(SessionContext sessionContext, Scanner userInputScanner, UserInputService userInput, WithdrawScreen withdrawScreen, FundTransferScreen fundTransferScreen, HistoryTransactionScreen historyTransactionScreen) {
        this.sessionContext = sessionContext;
        this.userInputScanner = userInputScanner;
        this.userInput = userInput;
        this.withdrawScreen = withdrawScreen;
        this.fundTransferScreen = fundTransferScreen;
        this.historyTransactionScreen = historyTransactionScreen;
    }

    private TransactionScreen(UserInputService userInputService) {
        userInput = userInputService;
        sessionContext = null;
        withdrawScreen = null;
        fundTransferScreen = null;
        historyTransactionScreen = null;
    }

    public static TransactionScreen getInstance(AccountDto userAccount, Scanner userInputScanner, UserInputService userInputService) {
        if (INSTANCE == null) {
            INSTANCE = new TransactionScreen(userInputService);
        }
        INSTANCE.userInputScanner = userInputScanner;
        return INSTANCE;
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
        System.out.println("1. Withdrawn");
        System.out.println("2. Fund Transfer");
        System.out.println("3. Transaction History");
        System.out.println("4. Exit");
        System.out.print("Please choose option[3] : ");
        String input = userInputScanner.nextLine();
        int menu = userInput.toValidatedMenu(input);
        switch (menu) {
            case 1: {
                withdrawScreen.setPreviousScreen(this);
                return withdrawScreen;
            }
            case 2: {
                fundTransferScreen.setPreviousScreen(this);
                return fundTransferScreen;
            }
            case 3: {
                return historyTransactionScreen;
            }
            default: {
                break;
            }
        }
        sessionContext.clearSession();
        return previousScreen;
    }
}
