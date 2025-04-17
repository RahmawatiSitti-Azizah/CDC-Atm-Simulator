package com.mitrais.cdc.model;

import com.mitrais.cdc.Main;

import java.util.List;
import java.util.Scanner;

public class WelcomeScreen implements Screen {
    private List<Account> loginListAccount;
    private Scanner userInputScanner;

    public WelcomeScreen(List<Account> aLoginListAccount, Scanner aUserInputScanner) {
        loginListAccount = aLoginListAccount;
        userInputScanner = aUserInputScanner;
    }

    @Override
    public Screen display() {
        System.out.print("Enter Account Number :");
        String inputAccountNumber = userInputScanner.nextLine();
        if (validateUser(inputAccountNumber)) {
            System.out.print("Enter Pin :");
            String inputPin = userInputScanner.nextLine();
            if (validatePassword(inputPin)) {
                for (Account loginAccount : loginListAccount) {
                    if (loginAccount.login(inputAccountNumber, inputPin)) {
                        return (new TransactionScreen(loginAccount, userInputScanner)).display();
                    }
                }
                System.out.println("Invalid Account Number/PIN");
            }
        }
        return display();
    }

    public Boolean validateUser(String username) {
        int length = username != null ? username.length() : 0;
        if (length > 6 || length < 6) {
            System.out.println("Account Number should have 6 digits length");
            return false;
        }
        if (!Main.checkStringIsNumberWithLength(username, 6)) {
            System.out.println("Account Number should only contains numbers");
            return false;
        }
        return true;
    }

    public Boolean validatePassword(String password) {
        int length = password != null ? password.length() : 0;
        if (length > 6 || length < 6) {
            System.out.println("Pin should have 6 digits length");
            return false;
        }
        if (!Main.checkStringIsNumberWithLength(password, 6)) {
            System.out.println("Pin should only contains numbers");
            return false;
        }
        return true;
    }
}
