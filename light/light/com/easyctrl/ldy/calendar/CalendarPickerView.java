package com.easyctrl.ldy.calendar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;
import com.easyctrl.iface.OnDateListener;
import com.easyctrl.ldy.activity.R;
import com.easyctrl.ldy.calendar.MonthView.Listener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CalendarPickerView extends ListView {
    private final MonthAdapter adapter = new MonthAdapter();
    final List<List<List<MonthCellDescriptor>>> cells = new ArrayList();
    private final DateFormat fullDateFormat;
    private final Listener listener = new CellClickedListener();
    private final Calendar maxCal = Calendar.getInstance();
    private final Calendar minCal = Calendar.getInstance();
    private final Calendar monthCounter = Calendar.getInstance();
    private final DateFormat monthNameFormat;
    final List<MonthDescriptor> months = new ArrayList();
    private OnDateListener onDateListener;
    private final Calendar selectedCal = Calendar.getInstance();
    private MonthCellDescriptor selectedCell;
    final Calendar today = Calendar.getInstance();
    private final DateFormat weekdayNameFormat;

    private class MonthAdapter extends BaseAdapter {
        private final LayoutInflater inflater;

        private MonthAdapter() {
            this.inflater = LayoutInflater.from(CalendarPickerView.this.getContext());
        }

        public boolean isEnabled(int position) {
            return false;
        }

        public int getCount() {
            return CalendarPickerView.this.months.size();
        }

        public Object getItem(int position) {
            return CalendarPickerView.this.months.get(position);
        }

        public long getItemId(int position) {
            return (long) position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            MonthView monthView = (MonthView) convertView;
            if (monthView == null) {
                monthView = MonthView.create(parent, this.inflater, CalendarPickerView.this.weekdayNameFormat, CalendarPickerView.this.listener, CalendarPickerView.this.today);
            }
            monthView.init((MonthDescriptor) CalendarPickerView.this.months.get(position), (List) CalendarPickerView.this.cells.get(position));
            return monthView;
        }
    }

    private class CellClickedListener implements Listener {
        private CellClickedListener() {
        }

        public void handleClick(MonthCellDescriptor cell) {
            if (CalendarPickerView.betweenDates(cell.getDate(), CalendarPickerView.this.minCal, CalendarPickerView.this.maxCal)) {
                CalendarPickerView.this.selectedCell.setSelected(false);
                CalendarPickerView.this.selectedCell = cell;
                CalendarPickerView.this.selectedCell.setSelected(true);
                CalendarPickerView.this.selectedCal.setTime(cell.getDate());
                CalendarPickerView.this.adapter.notifyDataSetChanged();
                CalendarPickerView.this.onDateListener.onSelectDate(CalendarPickerView.this.selectedCell.getDate());
                return;
            }
            Toast.makeText(CalendarPickerView.this.getContext(), CalendarPickerView.this.getResources().getString(R.string.invalid_date, new Object[]{CalendarPickerView.this.fullDateFormat.format(CalendarPickerView.this.minCal.getTime()), CalendarPickerView.this.fullDateFormat.format(CalendarPickerView.this.maxCal.getTime())}), 0).show();
        }
    }

    public void setOnDateListener(OnDateListener onDateListener) {
        this.onDateListener = onDateListener;
    }

    public CalendarPickerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setDivider(null);
        setDividerHeight(0);
        setAdapter(this.adapter);
        int bg = getResources().getColor(R.color.calendar_bg);
        setBackgroundColor(bg);
        setCacheColorHint(bg);
        this.monthNameFormat = new SimpleDateFormat(context.getString(R.string.month_name_format));
        this.weekdayNameFormat = new SimpleDateFormat(context.getString(R.string.day_name_format));
        this.fullDateFormat = DateFormat.getDateInstance(2);
    }

    public void init(Date selectedDate, Date minDate, Date maxDate) {
        if (selectedDate == null || minDate == null || maxDate == null) {
            throw new IllegalArgumentException("All dates must be non-null.  " + dbg(selectedDate, minDate, maxDate));
        } else if (selectedDate.getTime() == 0 || minDate.getTime() == 0 || maxDate.getTime() == 0) {
            throw new IllegalArgumentException("All dates must be non-zero.  " + dbg(selectedDate, minDate, maxDate));
        } else if (minDate.after(maxDate)) {
            throw new IllegalArgumentException("Min date must be before max date.  " + dbg(selectedDate, minDate, maxDate));
        } else if (selectedDate.before(minDate) || selectedDate.after(maxDate)) {
            throw new IllegalArgumentException("selectedDate must be between minDate and maxDate.  " + dbg(selectedDate, minDate, maxDate));
        } else {
            this.cells.clear();
            this.months.clear();
            this.selectedCal.setTime(selectedDate);
            this.minCal.setTime(minDate);
            this.maxCal.setTime(maxDate);
            setMidnight(this.selectedCal);
            setMidnight(this.minCal);
            setMidnight(this.maxCal);
            this.maxCal.add(12, -1);
            this.monthCounter.setTime(this.minCal.getTime());
            int maxMonth = this.maxCal.get(2);
            int maxYear = this.maxCal.get(1);
            int selectedYear = this.selectedCal.get(1);
            int selectedMonth = this.selectedCal.get(2);
            int selectedIndex = 0;
            while (true) {
                if ((this.monthCounter.get(2) <= maxMonth || this.monthCounter.get(1) < maxYear) && this.monthCounter.get(1) < maxYear + 1) {
                    MonthDescriptor month = new MonthDescriptor(this.monthCounter.get(2), this.monthCounter.get(1), this.monthNameFormat.format(this.monthCounter.getTime()));
                    this.cells.add(getMonthCells(month, this.monthCounter, this.selectedCal));
                    Logr.d("Adding month %s", month);
                    if (selectedMonth == month.getMonth() && selectedYear == month.getYear()) {
                        selectedIndex = this.months.size();
                    }
                    this.months.add(month);
                    this.monthCounter.add(2, 1);
                }
            }
            this.adapter.notifyDataSetChanged();
            if (selectedIndex != 0) {
                scrollToSelectedMonth(selectedIndex);
            }
        }
    }

    private void scrollToSelectedMonth(final int selectedIndex) {
        post(new Runnable() {
            public void run() {
                CalendarPickerView.this.smoothScrollToPosition(selectedIndex);
            }
        });
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (this.months.isEmpty()) {
            throw new IllegalStateException("Must have at least one month to display.  Did you forget to call init()?");
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public Date getSelectedDate() {
        return this.selectedCal.getTime();
    }

    private static String dbg(Date startDate, Date minDate, Date maxDate) {
        return "startDate: " + startDate + "\nminDate: " + minDate + "\nmaxDate: " + maxDate;
    }

    private static void setMidnight(Calendar cal) {
        cal.set(11, 0);
        cal.set(12, 0);
        cal.set(13, 0);
        cal.set(14, 0);
    }

    List<List<MonthCellDescriptor>> getMonthCells(MonthDescriptor month, Calendar startCal, Calendar selectedDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(startCal.getTime());
        List<List<MonthCellDescriptor>> cells = new ArrayList();
        cal.set(5, 1);
        cal.add(5, 1 - cal.get(7));
        while (true) {
            if ((cal.get(2) < month.getMonth() + 1 || cal.get(1) < month.getYear()) && cal.get(1) <= month.getYear()) {
                Logr.d("Building week row starting at %s", cal.getTime());
                List<MonthCellDescriptor> weekCells = new ArrayList();
                cells.add(weekCells);
                for (int c = 0; c < 7; c++) {
                    Date date = cal.getTime();
                    boolean isCurrentMonth = cal.get(2) == month.getMonth();
                    boolean isSelected = isCurrentMonth && sameDate(cal, selectedDate);
                    boolean isSelectable = isCurrentMonth && betweenDates(cal, this.minCal, this.maxCal);
                    MonthCellDescriptor cell = new MonthCellDescriptor(date, isCurrentMonth, isSelectable, isSelected, sameDate(cal, this.today), cal.get(5));
                    if (isSelected) {
                        this.selectedCell = cell;
                    }
                    weekCells.add(cell);
                    cal.add(5, 1);
                }
            }
        }
        return cells;
    }

    private static boolean sameDate(Calendar cal, Calendar selectedDate) {
        return cal.get(2) == selectedDate.get(2) && cal.get(1) == selectedDate.get(1) && cal.get(5) == selectedDate.get(5);
    }

    private static boolean betweenDates(Calendar cal, Calendar minCal, Calendar maxCal) {
        return betweenDates(cal.getTime(), minCal, maxCal);
    }

    static boolean betweenDates(Date date, Calendar minCal, Calendar maxCal) {
        Date min = minCal.getTime();
        return (date.equals(min) || date.after(min)) && date.before(maxCal.getTime());
    }
}
