package com.easyctrl.adapter;

import com.easyctrl.sheelview.WheelAdapter;

public class MinSheelAdatper implements WheelAdapter {
    private static MinSheelAdatper adapter;
    private String[] time;

    private MinSheelAdatper(String[] time) {
        this.time = time;
    }

    public static MinSheelAdatper getAdapter(String[] time) {
        if (adapter == null) {
            adapter = new MinSheelAdatper(time);
        }
        return adapter;
    }

    public int getItemsCount() {
        return this.time.length;
    }

    public String getItmeData(int index) {
        return this.time[index];
    }

    public String getItem(int index) {
        return new StringBuilder(String.valueOf(this.time[index])).append("  \u5206").toString();
    }

    public int getMaximumLength() {
        return 0;
    }
}
