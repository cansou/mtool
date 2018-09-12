package com.easyctrl.ldy.calendar;

import java.util.Date;

class MonthCellDescriptor {
    private final Date date;
    private final boolean isCurrentMonth;
    private final boolean isSelectable;
    private boolean isSelected;
    private final boolean isToday;
    private final int value;

    MonthCellDescriptor(Date date, boolean currentMonth, boolean selectable, boolean selected, boolean today, int value) {
        this.date = date;
        this.isCurrentMonth = currentMonth;
        this.isSelectable = selectable;
        this.isSelected = selected;
        this.isToday = today;
        this.value = value;
    }

    public Date getDate() {
        return this.date;
    }

    public boolean isCurrentMonth() {
        return this.isCurrentMonth;
    }

    public boolean isSelectable() {
        return this.isSelectable;
    }

    public boolean isSelected() {
        return this.isSelected;
    }

    public void setSelected(boolean selected) {
        this.isSelected = selected;
    }

    public boolean isToday() {
        return this.isToday;
    }

    public int getValue() {
        return this.value;
    }

    public String toString() {
        return "MonthCellDescriptor{date=" + this.date + ", value=" + this.value + ", isCurrentMonth=" + this.isCurrentMonth + ", isSelected=" + this.isSelected + ", isToday=" + this.isToday + ", isSelectable=" + this.isSelectable + '}';
    }
}
