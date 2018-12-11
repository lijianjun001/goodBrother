package com.antelope.goodbrother.utils;

import java.text.DecimalFormat;

public class DecimalUtil {
    public static String format(long amount) {
        DecimalFormat decimalFormat = new DecimalFormat(".00");
        return decimalFormat.format(amount);
    }

    public static String format(double amount) {
        DecimalFormat decimalFormat = new DecimalFormat(".00");
        return decimalFormat.format(amount);
    }
}
