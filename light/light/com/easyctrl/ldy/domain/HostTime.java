package com.easyctrl.ldy.domain;

import android.support.v4.view.MotionEventCompat;

public class HostTime {
    private String hour;
    private String minute;
    private int[] timeOrder;

    public HostTime(int[] timeOrder) {
        this.timeOrder = timeOrder;
    }

    private String checkDoubleNum(int num) {
        String tempNum = String.valueOf(num);
        if (tempNum.length() == 2) {
            return tempNum;
        }
        return "0" + tempNum;
    }

    public int getYear() {
        return (this.timeOrder[8] << 8) | (this.timeOrder[7] & MotionEventCompat.ACTION_MASK);
    }

    public String getMonth() {
        return checkDoubleNum(this.timeOrder[9]);
    }

    public String getDay() {
        return checkDoubleNum(this.timeOrder[10]);
    }

    public String getHour() {
        if (this.hour == null) {
            return checkDoubleNum(this.timeOrder[11]);
        }
        return this.hour;
    }

    public String getMinute() {
        if (this.minute == null) {
            return checkDoubleNum(this.timeOrder[12]);
        }
        return this.minute;
    }

    public String getSecond() {
        return checkDoubleNum(this.timeOrder[13]);
    }

    public void setMinute(String minute) {
        this.minute = minute;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }
}
