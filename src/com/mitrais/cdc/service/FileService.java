package com.mitrais.cdc.service;

public interface FileService {
    /**
     * Imports data from a file located at the specified file path.
     *
     * @param filePath a String representing the path to the file to be imported.
     */
    void importDataFromFile(String filePath);
}
