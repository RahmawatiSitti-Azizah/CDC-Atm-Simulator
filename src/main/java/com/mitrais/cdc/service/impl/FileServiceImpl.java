package com.mitrais.cdc.service.impl;

import com.mitrais.cdc.model.Dollar;
import com.mitrais.cdc.service.FileService;
import com.mitrais.cdc.service.SearchAccountService;
import com.mitrais.cdc.util.ErrorConstant;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class FileServiceImpl implements FileService {

    private SearchAccountService searchAccountService;

    public FileServiceImpl(SearchAccountService searchAccountService) {
        this.searchAccountService = searchAccountService;
    }

    @Override
    public void importDataFromFile(String filePath) throws IOException {
        if (!filePath.contains(".csv")) {
            System.out.println(ErrorConstant.FILE_FORMAT_NOT_SUPPORTED);
            throw new IOException(ErrorConstant.FILE_FORMAT_NOT_SUPPORTED);
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            reader.lines().forEach(s -> {
                String[] parts = s.split(",");
                if (parts.length < 4) {
                    try {
                        throw new IOException("Invalid data format in file " + filePath + " file should contain accountHolderName, pin, balance, accountNumber");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                String accountHolderName = parts[0].trim();
                String pin = parts[1].trim();
                String balance = parts[2].trim();
                String accountNumber = parts[3].trim();
                try {
                    searchAccountService.addAccount(new Dollar(Double.parseDouble(balance)), accountHolderName, accountNumber, pin);
                } catch (NumberFormatException exception) {
                    System.out.println("skip row, no valid balance found -> potentially a header");
                }
            });
        } catch (IOException e) {
            throw new IOException("Error reading file: " + e.getMessage(), e);
        }
    }
}
