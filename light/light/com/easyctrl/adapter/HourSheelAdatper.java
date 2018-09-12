package com.easyctrl.adapter;

import com.easyctrl.sheelview.WheelAdapter;

public class HourSheelAdatper implements WheelAdapter {
    private static HourSheelAdatper adapter;
    private String[] time;

    public String getItmeData(int index) {
        return this.time[index];
    }

    private HourSheelAdatper(String[] time) {
        this.time = time;
    }

    public static HourSheelAdatper getAdapter(String[] time) {
        if (adapter == null) {
            adapter = new HourSheelAdatper(time);
        }
        return adapter;
    }

    public int getItemsCount() {
        return this.time.length;
    }

    public String getItem(int index) {
        return new StringBuilder(String.valueOf(this.time[index])).append("  \u65f6").toString();
    }

    public int getMaximumLength() {
        return 0;
    }
}
