package com.easyctrl.ldy.calendar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.easyctrl.ldy.activity.R;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.List;

public class MonthView extends LinearLayout {
    private CalendarGridView grid;
    private Listener listener;
    private TextView title;

    public interface Listener {
        void handleClick(MonthCellDescriptor monthCellDescriptor);
    }

    public static MonthView create(ViewGroup parent, LayoutInflater inflater, DateFormat weekdayNameFormat, Listener listener, Calendar today) {
        MonthView view = (MonthView) inflater.inflate(R.layout.month, parent, false);
        int originalDayOfWeek = today.get(7);
        CalendarRowView headerRow = (CalendarRowView) view.grid.getChildAt(0);
        for (int c = 1; c <= 7; c++) {
            today.set(7, c);
            ((TextView) headerRow.getChildAt(c - 1)).setText(weekdayNameFormat.format(today.getTime()));
        }
        today.set(7, originalDayOfWeek);
        view.listener = listener;
        return view;
    }

    public MonthView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    protected void onFinishInflate() {
        super.onFinishInflate();
        this.title = (TextView) findViewById(R.id.title);
        this.grid = (CalendarGridView) findViewById(R.id.calendar_grid);
    }

    public void init(MonthDescriptor month, List<List<MonthCellDescriptor>> cells) {
        Logr.d("Initializing MonthView for %s", month);
        long start = System.currentTimeMillis();
        this.title.setText(month.getLabel());
        int numRows = cells.size();
        for (int i = 0; i < 6; i++) {
            CalendarRowView weekRow = (CalendarRowView) this.grid.getChildAt(i + 1);
            weekRow.setListener(this.listener);
            if (i < numRows) {
                weekRow.setVisibility(0);
                List<MonthCellDescriptor> week = (List) cells.get(i);
                for (int c = 0; c < week.size(); c++) {
                    MonthCellDescriptor cell = (MonthCellDescriptor) week.get(c);
                    CheckedTextView cellView = (CheckedTextView) weekRow.getChildAt(c);
                    cellView.setText(Integer.toString(cell.getValue()));
                    cellView.setEnabled(cell.isCurrentMonth());
                    cellView.setChecked(!cell.isToday());
                    cellView.setSelected(cell.isSelected());
                    if (cell.isSelectable()) {
                        cellView.setTextColor(getResources().getColorStateList(R.color.calendar_text_selector));
                    } else {
                        cellView.setTextColor(getResources().getColor(R.color.calendar_text_unselectable));
                    }
                    cellView.setTag(cell);
                }
            } else {
                weekRow.setVisibility(8);
            }
        }
        Logr.d("MonthView.init took %d ms", Long.valueOf(System.currentTimeMillis() - start));
    }
}
