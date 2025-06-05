package com.mitrais.cdc.view;

import com.mitrais.cdc.model.Account;
import com.mitrais.cdc.service.UserInputService;
import com.mitrais.cdc.service.impl.ServiceFactory;

import javax.xml.bind.ValidationException;
import java.util.Scanner;

public class OtherWithdrawnScreen implements Screen {
    private Account userAccount;
    private Scanner userInputScanner;
    private UserInputService userInputService;

    public OtherWithdrawnScreen(Account account, Scanner aUserInputScanner) {
        userAccount = account;
        userInputScanner = aUserInputScanner;
        userInputService = ServiceFactory.createUserInputService();
    }

    @Override
    public Screen display() {
        System.out.println("Other Withdraw");
        long amount = 0;
        System.out.print("Enter amount to withdraw : ");
        String input = userInputScanner.nextLine();
        try {
            amount = userInputService.toValidatedAmount(input);
            return new WithdrawSummaryScreen(amount, userAccount, userInputScanner);
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
            return new WithdrawScreen(userAccount, userInputScanner);
        }
    }
}
