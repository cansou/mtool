package com.easyctrl.ldy.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkUtil {
    public static boolean isNetworkAvailable(Context context) {
        NetworkInfo info = getNetworkInfo(context);
        if (info != null) {
            return info.isAvailable();
        }
        return false;
    }

    public static boolean isWifi(Context context) {
        NetworkInfo info = getNetworkInfo(context);
        if (info == null || info.getType() != 1) {
            return false;
        }
        return true;
    }

    public static boolean isMobile(Context context) {
        NetworkInfo info = getNetworkInfo(context);
        if (info == null || info.getType() != 0) {
            return false;
        }
        return true;
    }

    private static NetworkInfo getNetworkInfo(Context context) {
        return ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
    }
}
