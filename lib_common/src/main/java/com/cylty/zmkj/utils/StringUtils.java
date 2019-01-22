package com.cylty.zmkj.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class StringUtils {
    public static boolean isEmpty(String str) {
        if (str == null) {
            return true;
        }
        if (str.trim().length() == 0) {
            return true;
        }
        if (str.trim().equals("")) {
            return true;
        }
        return false;
    }


    public static String gbEncoding(final String gbString) {
        char[] utfBytes = gbString.toCharArray();
        String unicodeBytes = "";
        for (int byteIndex = 0; byteIndex < utfBytes.length; byteIndex++) {
            String hexB = Integer.toHexString(utfBytes[byteIndex]);
            if (hexB.length() <= 2) {
                hexB = "00" + hexB;
            }
            unicodeBytes = unicodeBytes + "\\u" + hexB;
        }
//		System.out.println("unicodeBytes is: " + unicodeBytes);
        return unicodeBytes;
    }


    public static String decodeUnicode(final String dataStr) {
        int start = 0;
        int end = 0;
        final StringBuffer buffer = new StringBuffer();
        while (start > -1) {
            end = dataStr.indexOf("\\u", start + 2);
            String charStr = "";
            if (end == -1) {
                charStr = dataStr.substring(start + 2, dataStr.length());
            } else {
                charStr = dataStr.substring(start + 2, end);
            }
            char letter = (char) Integer.parseInt(charStr, 16); // 16进制parse整形字符串。
            buffer.append(Character.toString(letter));
            start = end;
        }
        return buffer.toString();
    }

    public static boolean isValidTelephone(String tel) {
        String patternStr = "^1\\d{10}$";
        tel = tel.replaceAll(" ", "");
        if (!tel.matches(patternStr)) {
            return false;
        } else {
            return true;
        }
    }


    public static boolean isValidMoney(String money) {
        if (!StringUtils.isEmpty(money)) {
            try {
                Double.parseDouble(money);
            } catch (Exception e) {
                return false;
            }
        } else {
            return false;
        }
        return true;
    }

    public static String formatBankNum(String source) {
        return source.replaceAll(" ", "").replaceAll("\\d{4}(?!$|\\s)", "$0 ");
    }

    public static String formatAnonymousBankNum(String source) {
        return source.replaceAll(" ", "").replaceAll("\\S{4}(?!$|\\s)", "$0 ");
    }


    public static  String formatPhone(String phoneNum){
        if (isEmpty(phoneNum)){
            return null;
        }
        StringBuffer stringBuffer=new StringBuffer(phoneNum.substring(0, 3));
        stringBuffer.append("****").append(phoneNum.substring(7, 11));
        return stringBuffer.toString();
    }

    public static String getString(InputStream inputStream) throws IOException {
        StringBuilder sb = new StringBuilder();
        String line;

        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        return sb.toString();
    }

    public static InputStream getInputStream(String str){
        InputStream is = new ByteArrayInputStream(str.getBytes());
        return is;
    }
}
