package com.mitrais.cdc;

import com.mitrais.cdc.service.impl.ServiceFactory;
import com.mitrais.cdc.view.Screen;
import com.mitrais.cdc.view.WelcomeScreen;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("No file path provided. Please provide a file path as an argument.");
            return;
        }
        for (String arg : args) {
            try {
                ServiceFactory.createFileService().importDataFromFile(arg);
            } catch (java.io.IOException e) {
                System.out.println(e.getMessage());
                return;
            }
        }
        Screen nextScreen = WelcomeScreen.getInstance(null, new Scanner(System.in));
        while (nextScreen != null) {
            nextScreen = nextScreen.display();
        }
    }
}
