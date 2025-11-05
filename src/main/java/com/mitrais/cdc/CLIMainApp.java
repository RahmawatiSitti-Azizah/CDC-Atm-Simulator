package com.mitrais.cdc;

import com.mitrais.cdc.service.FileService;
import com.mitrais.cdc.view.Screen;
import com.mitrais.cdc.view.WelcomeScreen;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CLIMainApp implements CommandLineRunner {
    private final FileService fileService;
    private final WelcomeScreen welcomeScreen;

    public CLIMainApp(FileService fileService, WelcomeScreen welcomeScreen) {
        this.fileService = fileService;
        this.welcomeScreen = welcomeScreen;
    }


    @Override
    public void run(String... args) throws Exception {
        if (args.length == 0) {
            System.out.println("No file path provided. Please provide a file path as an argument.");
            return;
        }
        for (String arg : args) {
            try {
                fileService.importDataFromFile(arg);
            } catch (java.io.IOException e) {
                System.out.println(e.getMessage());
                return;
            }
        }
        Screen nextScreen = welcomeScreen;
        while (nextScreen != null) {
            nextScreen = nextScreen.display();
        }
    }
}
