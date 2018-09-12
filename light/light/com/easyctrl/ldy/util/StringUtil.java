package com.easyctrl.ldy.util;

import java.util.ArrayList;
import java.util.Arrays;

public class StringUtil {
    public static byte[] getByteArray(byte[] buf, int start, int len) {
        byte[] b = new byte[len];
        for (int i = 0; i < len; i++) {
            b[i] = buf[i];
        }
        return b;
    }

    public static String[] checkOrder(byte[] instruct) {
        String str = "";
        for (byte append : instruct) {
            str = new StringBuilder(String.valueOf(str)).append(append).append(" ").toString();
        }
        return str.split("-54");
    }

    public static String listToArray(ArrayList<Integer> integers) {
        int[] ins = new int[integers.size()];
        for (int i = 0; i < integers.size(); i++) {
            ins[i] = ((Integer) integers.get(i)).intValue();
        }
        return Arrays.toString(ins).replace("[", "").replace("]", "");
    }
}
