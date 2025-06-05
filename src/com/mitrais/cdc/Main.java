package com.mitrais.cdc;

import com.mitrais.cdc.model.Account;
import com.mitrais.cdc.view.Screen;
import com.mitrais.cdc.view.WelcomeScreen;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Main {
    public static final List<Account> listLoginAccount = getListAccount();

    public static void main(String[] args) {
        Screen nextScreen = new WelcomeScreen(new Scanner(System.in));
        while (nextScreen != null) {
            nextScreen = nextScreen.display();
        }
    }

    public static List<Account> getListAccount() {
        List<Account> loginList = new ArrayList<>();
        loginList.add(new Account(100, "John Doe", "112233", "012108"));
        loginList.add(new Account(30, "Jane Doe", "112244", "932012"));
        return loginList;
    }

    public static boolean checkStringIsNumberWithRangeLength(String stringData, int minimumLength, int maximumLenght) {
        return Pattern.compile("\\d{" + minimumLength + "," + maximumLenght + "}").matcher(stringData).find() && !Pattern.compile("\\D").matcher(stringData).find();
    }
}
