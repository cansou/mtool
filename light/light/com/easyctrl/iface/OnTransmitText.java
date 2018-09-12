package com.easyctrl.iface;

import java.util.Date;

public interface OnTransmitText {
    public static final int TIME_TYPE_DEFINE = 1;
    public static final int TIME_TYPE_EVERYDAY = 3;
    public static final int TIME_TYPE_SPECIAL = 2;
    public static final int TIME_TYPE_SPECIAL_TEXT = 6;
    public static final int TIME_TYPE_WEEK = 4;
    public static final int TIME_TYPE_WORKDAY = 5;
    public static final int TRANSMIT_D = 1;
    public static final int TRANSMIT_R = 2;

    void transmit(String str, int i, int i2, Date date);
}
