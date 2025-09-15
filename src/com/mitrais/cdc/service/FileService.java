package com.mitrais.cdc.service;

import java.io.IOException;

public interface FileService {
    /**
     * Imports data from a file located at the specified file path.
     * The file only accept .csv format and doesn't have header on the file otherwise it will throw exception.
     *
     * @param filePath a String representing the path to the file to be imported.
     * @throws IOException if and error occurs while reading the file,
     */
    void importDataFromFile(String filePath) throws IOException;
}
