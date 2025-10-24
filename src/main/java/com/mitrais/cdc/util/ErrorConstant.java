package com.mitrais.cdc.util;

import com.mitrais.cdc.model.Money;

public class ErrorConstant {
    public static final String FILE_FORMAT_NOT_SUPPORTED = "File format not supported. Please provide a .csv file.";
    public static final String DUPLICATE_ACCOUNT_NUMBER = "There can't be 2 different accounts with the same Account Number ";
    public static final String DUPLICATE_RECORDS = "There can't be duplicated records ";
    public static final String INVALID_AMOUNT = "Invalid amount";
    public static final String MAXIMUM_TRANSACTION_AMOUNT_EXCEED = "Maximum amount to transfer is $1000";
    public static final String INVALID_ACCOUNT_PASSWORD = "Invalid Account/Pin";
    public static final String ACCOUNT_NUMBER_SHOULD_ONLY_CONTAINS_NUMBERS = "Account Number should only contains numbers";
    public static final String ACCOUNT_NOT_FOUND = "Account not found";
    public static final String INVALID_ACCOUNT = "Invalid Account";
    public static final String DESTINATION_ACCOUNT_IS_THE_SAME_AS_SOURCE_ACCOUNT = "Destination account is the same as source account";
    private static final String INSUFFICIENT_BALANCE = "Insufficient balance ";
    public static final String ACCOUNT_NUMBER_SHOULD_HAVE_6_DIGITS_LENGTH = "Account Number should have 6 digits length";
    public static final String MINIMUM_AMOUNT_ERROR_MESSAGE = "Minimum amount to transfer is $1";

    public static String getInsufficientBalanceErrorMessage(Money withdrawCurrency) {
        return INSUFFICIENT_BALANCE + withdrawCurrency.toString();
    }
}
