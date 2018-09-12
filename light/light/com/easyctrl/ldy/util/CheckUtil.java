package com.easyctrl.ldy.util;

public class CheckUtil {
    public static final int PASSLENTH = 8;

    public static Boolean loginCheck(byte[] result) {
        if (result.length == 9) {
            if (result[5] == (byte) 5 && result[7] == (byte) 1) {
                return Boolean.valueOf(false);
            }
            if (result[5] == (byte) 5 && result[7] == (byte) 0) {
                return Boolean.valueOf(true);
            }
        }
        return null;
    }

    public static byte[] createPassword(String strpass) {
        byte[] pwd = new byte[16];
        pwd[0] = (byte) -2;
        pwd[1] = (byte) 14;
        pwd[2] = (byte) -112;
        pwd[3] = (byte) -2;
        pwd[4] = (byte) -1;
        pwd[5] = (byte) 5;
        pwd[6] = (byte) 8;
        pwd[15] = (byte) -54;
        if (strpass.length() > 8) {
            return null;
        }
        for (int i = 0; i < strpass.length(); i++) {
            pwd[i + 7] = (byte) strpass.charAt(i);
        }
        return pwd;
    }

    public static byte[] createEnterPassword(String strpass) {
        byte[] pwd = new byte[16];
        pwd[0] = (byte) -2;
        pwd[1] = (byte) 14;
        pwd[2] = (byte) -112;
        pwd[3] = (byte) -2;
        pwd[4] = (byte) -1;
        pwd[5] = (byte) 6;
        pwd[6] = (byte) 8;
        pwd[15] = (byte) -54;
        if (strpass == null || strpass.length() > 8 || strpass.length() == 0) {
            return null;
        }
        for (int i = 0; i < strpass.length(); i++) {
            pwd[i + 7] = (byte) strpass.charAt(i);
        }
        return pwd;
    }

    public static Boolean enterSetting(byte[] result) {
        if (result.length == 9) {
            if (result[5] == (byte) 6 && result[7] == (byte) 1) {
                return Boolean.valueOf(false);
            }
            if (result[5] == (byte) 6 && result[7] == (byte) 0) {
                return Boolean.valueOf(true);
            }
        }
        return null;
    }
}
