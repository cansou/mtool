package com.easyctrl.ldy.calendar;

class MonthDescriptor {
    private final String label;
    private final int month;
    private final int year;

    public MonthDescriptor(int month, int year, String label) {
        this.month = month;
        this.year = year;
        this.label = label;
    }

    public int getMonth() {
        return this.month;
    }

    public int getYear() {
        return this.year;
    }

    public String getLabel() {
        return this.label;
    }

    public String toString() {
        return "MonthDescriptor{label='" + this.label + '\'' + ", month=" + this.month + ", year=" + this.year + '}';
    }
}
