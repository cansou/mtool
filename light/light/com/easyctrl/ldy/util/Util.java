package com.easyctrl.ldy.util;

import android.content.Context;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.util.List;

public class Util {
    public static String arr2String(int[] arr) {
        String str = "";
        for (int valueOf : arr) {
            str = new StringBuilder(String.valueOf(str)).append(String.valueOf(valueOf)).append("_").toString();
        }
        return str.substring(0, str.length() - 1);
    }

    public static int[] str2Array(String str) {
        String[] s = str.split(" ");
        int[] arr = new int[s.length];
        int i = 0;
        while (i < arr.length) {
            try {
                if (!s[i].trim().equals("")) {
                    arr[i] = Integer.valueOf(s[i].trim()).intValue();
                }
                i++;
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return arr;
    }

    public static int toHexInt(int decimalism) {
        return new BigInteger(Integer.toHexString(decimalism), 16).intValue();
    }

    public static byte toHexByte(int decimalism) {
        return new BigInteger(Integer.toHexString(decimalism), 16).byteValue();
    }

    public static int toInt(String hex) {
        BigInteger integer = null;
        try {
            integer = new BigInteger(hex.substring(6, 8), 16);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return integer.intValue();
    }

    public static byte toHexInt2(int dec) {
        return (byte) Integer.valueOf(Integer.toString(dec), 16).intValue();
    }

    public static String[] multilDatatoArray(String data) {
        if (data == null) {
            return null;
        }
        String[] qiege = data.split("-54");
        for (int i = 0; i < qiege.length; i++) {
            qiege[i] = new StringBuilder(String.valueOf(qiege[i].trim())).append(" -54").toString();
        }
        return qiege;
    }

    public static String[] data2Array(String data) {
        if (data == null) {
            return null;
        }
        return data.split(" ");
    }

    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) ((charToByte(hexChars[pos]) << 4) | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    private static byte charToByte(char c) {
        return (byte) "0123456789abcdef".indexOf(c);
    }

    public static int dip2px(Context context, float dipValue) {
        return (int) ((dipValue * context.getResources().getDisplayMetrics().density) + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        return (int) ((pxValue / context.getResources().getDisplayMetrics().density) + 0.5f);
    }

    public static <T> List<T> deepCopy(List<T> src) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        new ObjectOutputStream(byteOut).writeObject(src);
        return (List) new ObjectInputStream(new ByteArrayInputStream(byteOut.toByteArray())).readObject();
    }
}
