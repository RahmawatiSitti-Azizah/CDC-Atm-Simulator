package com.mitrais.cdc.view;

import com.mitrais.cdc.model.Account;
import com.mitrais.cdc.service.UserInputService;
import com.mitrais.cdc.service.impl.ServiceFactory;

import javax.xml.bind.ValidationException;
import java.util.Scanner;

public class TransactionScreen implements Screen {
    private Scanner userInputScanner;
    private Account userAccount;
    private UserInputService userInput;

    public TransactionScreen(Account account, Scanner aUserInputScanner) {
        userAccount = account;
        userInputScanner = aUserInputScanner;
        userInput = ServiceFactory.createUserInputService();
    }


    @Override
    public Screen display() {
        System.out.println("1. Withdrawn");
        System.out.println("2. Fund Transfer");
        System.out.println("3. Exit");
        System.out.print("Please choose option[3] : ");
        String input = userInputScanner.nextLine();
        try {
            int menu = userInput.toValidatedMenu(input);
            switch (menu) {
                case 1: {
                    return new WithdrawScreen(userAccount, userInputScanner);
                }
                case 2: {
                    return new FundTransferScreen(userAccount, userInputScanner);
                }
                default: {
                    break;
                }
            }
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
        }
        return new WelcomeScreen(userInputScanner);
    }
}
