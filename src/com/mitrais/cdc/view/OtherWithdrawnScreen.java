package com.mitrais.cdc.view;

import com.mitrais.cdc.model.Account;
import com.mitrais.cdc.model.Money;
import com.mitrais.cdc.service.UserInputService;
import com.mitrais.cdc.service.impl.ServiceFactory;

import java.util.Scanner;

public class OtherWithdrawnScreen implements Screen {
    private static OtherWithdrawnScreen INSTANCE;
    private Account userAccount;
    private Scanner userInputScanner;
    private UserInputService userInputService;

    public static OtherWithdrawnScreen getInstance(Account account, Scanner aUserInputScanner, UserInputService userInputService) {
        if (INSTANCE == null) {
            INSTANCE = new OtherWithdrawnScreen(userInputService);
        }
        INSTANCE.userAccount = account;
        INSTANCE.userInputScanner = aUserInputScanner;
        return INSTANCE;
    }

    private OtherWithdrawnScreen(UserInputService userInputService) {
        this.userInputService = userInputService;
    }

    @Override
    public Screen display() {
        System.out.println("Other Withdraw");
        Money amount;
        System.out.print("Enter amount to withdraw : ");
        String input = userInputScanner.nextLine();
        try {
            amount = userInputService.toValidatedMoney(input);
            return WithdrawSummaryScreen.getInstance(amount, userAccount, userInputScanner, ServiceFactory.createUserInputService(), ServiceFactory.createAccountTransactionService(), ServiceFactory.createTransactionAmountValidatorService());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return WithdrawScreen.getInstance(userAccount, userInputScanner, userInputService);
        }
    }
}
