package com.mitrais.cdc.util;

import java.util.regex.Pattern;

public class StringMatcherUtil {

    public static boolean checkStringIsNumberWithLength(String stringData, int length) {
        return !stringData.isEmpty() && stringData.length() == length && Pattern.compile("\\d{" + length + "}").matcher(stringData).find();
    }

    public static boolean checkStringIsNumberOnly(String stringData) {
        return !Pattern.compile("\\D").matcher(stringData).find();
    }
}
