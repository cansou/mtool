package com.easyctrl.ldy.calendar;

import android.util.Log;

final class Logr {
    Logr() {
    }

    public static void d(String message) {
        Log.d("TimesSquare", message);
    }

    public static void d(String message, Object... args) {
        d(String.format(message, args));
    }
}
