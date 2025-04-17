package com.mitrais.cdc;

import com.mitrais.cdc.model.Account;
import com.mitrais.cdc.model.WelcomeScreen;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Main {
    public static final List<Account> listLoginAccount = getListAccount();

    public static void main(String[] args) {
        List<Account> loginListAccount = getListAccount();
        WelcomeScreen welcomeScreen = new WelcomeScreen(loginListAccount, new Scanner(System.in));
        while (true) {
            welcomeScreen.display();
        }
    }

    public static List<Account> getListAccount() {
        List<Account> loginList = new ArrayList<>();
        loginList.add(new Account(new Double(100), "John Doe", "112233", "012108"));
        loginList.add(new Account(new Double(30), "Jane Doe", "112244", "932012"));
        return loginList;
    }

    public static boolean checkStringIsNumberWithLength(String stringData, int length) {
        return !stringData.isEmpty() && Pattern.compile("\\d{" + length + "}").matcher(stringData).find();
    }

    public static boolean checkStringIsNumberWithRangeLength(String stringData, int minimumLength, int maximumLenght) {
        return !stringData.isEmpty() && Pattern.compile("\\d{" + minimumLength + "," + maximumLenght + "}").matcher(stringData).find() && !Pattern.compile("\\D").matcher(stringData).find();
    }

    /*private static void welcomeScreen(List<Account> loginListAccount) {
        System.out.print("Enter Account Number :");
        Scanner userInputScanner = new Scanner(System.in);
        String inputAccountNumber = userInputScanner.nextLine();
        if (validateUser(inputAccountNumber)) {
            System.out.print("Enter Pin :");
            String inputPin = userInputScanner.nextLine();
            if (validatePassword(inputPin)) {
                boolean login = false;
                for (Account loginAccount : loginListAccount) {
                    if (loginAccount.login(inputAccountNumber, inputPin)) {
                        login = true;
                        int menu = 0;
                        while (menu < 1 || menu > 3) {
                            displayTransactionScreen(userInputScanner, loginAccount);
                        }
                    }
                }
                if (!login) {
                    System.out.println("Invalid Account Number/PIN");
                }
            }
        }
    }

    public static int displayTransactionScreen(Scanner userInputScanner, Account account) {
        System.out.println("1. Withdrawn");
        System.out.println("2. Fund Transfer");
        System.out.println("3. Exit");
        System.out.print("Please choose option[3]");
        int menu = userInputScanner.next();
        return processTransactionScreenMenu(menu, account);
    }

    private static int processTransactionScreenMenu(int menu, Account account) {
        switch (menu) {
            case 1:
        }
    }

    public static Boolean validateUser(String username) {
        int length = username != null ? username.length() : 0;
        if (length > 6 || length < 6) {
            System.out.println("Account Number should have 6 digits length");
            return false;
        }
        if (!Pattern.compile("\\d{6}").matcher(username).find()) {
            System.out.println("Account Number should only contains numbers");
            return false;
        }
        return true;
    }

    public static Boolean validatePassword(String password) {
        int length = password != null ? password.length() : 0;
        if (length > 6 || length < 6) {
            System.out.println("Pin should have 6 digits length");
            return false;
        }
        int n = 6;
        if (!checkStringIsNumberWithLength(password, n)) {
            System.out.println("Pin should only contains numbers");
            return false;
        }
        return true;
    }*/

}
