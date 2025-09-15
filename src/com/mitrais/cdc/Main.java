package com.mitrais.cdc;

import com.mitrais.cdc.service.FileService;
import com.mitrais.cdc.service.impl.ServiceFactory;
import com.mitrais.cdc.view.Screen;
import com.mitrais.cdc.view.WelcomeScreen;

import java.util.Scanner;

public class Main {

    private static FileService fileService;

    public static void main(String[] args) {

        if (args.length == 0) {
            System.out.println("No file path provided. Please provide a file path as an argument.");
            return;
        }
        for (String arg : args) {
            try {
                fileService = ServiceFactory.createFileService(ServiceFactory.createSearchAccountService());
                fileService.importDataFromFile(arg);
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
