package com.mitrais.cdc;

import com.mitrais.cdc.view.Screen;
import com.mitrais.cdc.view.WelcomeScreen;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Screen nextScreen = WelcomeScreen.getInstance(null, new Scanner(System.in));
        while (nextScreen != null) {
            nextScreen = nextScreen.display();
        }
    }
}
