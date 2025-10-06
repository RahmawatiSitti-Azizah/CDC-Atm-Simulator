package com.mitrais.cdc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);

        /*if (args.length == 0) {
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
        Screen nextScreen = WelcomeScreen.getInstance(null, new Scanner(System.in),
                ServiceFactory.createSearchAccountService(),
                ServiceFactory.createAccountValidatorService());
        while (nextScreen != null) {
            nextScreen = nextScreen.display();
        }*/
    }
}
