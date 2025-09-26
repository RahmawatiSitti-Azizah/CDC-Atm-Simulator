package com.mitrais.cdc.util;

import java.util.Random;

public class ReferenceNumberGenerator {

    public static String generateTransferRefnumber(Random random) {
        StringBuffer stringBuffer = new StringBuffer("T" + String.valueOf(random.nextInt(999999)));
        while (stringBuffer.length() < 7) {
            stringBuffer.append(0);
        }
        return stringBuffer.toString();
    }

    public static String generateWithdrawRefnumber(Random random) {
        StringBuffer stringBuffer = new StringBuffer("W" + String.valueOf(random.nextInt(999999)));
        while (stringBuffer.length() < 7) {
            stringBuffer.append(0);
        }
        return stringBuffer.toString();
    }
}
