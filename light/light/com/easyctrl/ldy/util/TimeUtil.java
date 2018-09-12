package com.easyctrl.ldy.util;

import android.text.format.Time;

public class TimeUtil {
    private static TimeUtil instance;
    private static Time t;
    int date;
    int hour;
    int minute;
    int month;
    int second;
    int year;

    private TimeUtil() {
        t = new Time();
    }

    public static TimeUtil getTimeUtil() {
        if (instance == null) {
            instance = new TimeUtil();
        }
        t.setToNow();
        return instance;
    }

    public int getYear() {
        return t.year;
    }

    public int getMonth() {
        return t.month;
    }

    public int getDate() {
        return t.monthDay;
    }

    public int getHour() {
        return t.hour;
    }

    public int getMinute() {
        return t.minute;
    }

    public int getSecond() {
        return t.second;
    }

    public String effectTime(byte effectHour, byte effectMin) {
        byte currentHour = (byte) getHour();
        byte currentMin = (byte) getMinute();
        if (effectHour == (byte) 0) {
            effectHour = (byte) 24;
        }
        if (currentMin <= effectMin) {
            effectMin = (byte) (effectMin - currentMin);
        } else {
            effectHour = (byte) (effectHour - 1);
            effectMin = (byte) (((byte) (effectMin + 60)) - currentMin);
        }
        if (currentHour <= effectHour) {
            effectHour = (byte) (effectHour - currentHour);
        } else {
            effectHour = (byte) (((byte) (effectHour + 24)) - currentHour);
        }
        if (effectHour <= (byte) 24) {
            return new StringBuilder(String.valueOf(effectHour)).append("\u5c0f\u65f6").append(effectMin).append("\u5206\u949f\u540e\u751f\u6548").toString();
        }
        effectHour = (byte) (effectHour % 24);
        return (effectHour / 24) + "\u5929" + effectHour + "\u65f6" + effectMin + "\u540e\u6267\u884c";
    }
}
