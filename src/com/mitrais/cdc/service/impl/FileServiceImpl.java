package com.mitrais.cdc.service.impl;

import com.mitrais.cdc.model.Dollar;
import com.mitrais.cdc.service.FileService;
import com.mitrais.cdc.service.SearchAccountService;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class FileServiceImpl implements FileService {

    @Override
    public void importDataFromFile(String filePath) {

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 4) {
                    throw new IOException("Invalid data format in file: " + filePath);
                }
                String accountNumber = parts[0].trim();
                String pin = parts[1].trim();
                String accountHolderName = parts[2].trim();
                String balance = parts[3].trim();
                SearchAccountService service = ServiceFactory.createSearchAccountService();
                service.addAccount(new Dollar(Long.parseLong(balance)), accountHolderName, accountNumber, pin);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
