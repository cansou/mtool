package com.easyctrl.ldy.calendar;

import android.content.Context;
import android.support.v4.widget.ExploreByTouchHelper;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import com.easyctrl.ldy.calendar.MonthView.Listener;

public class CalendarRowView extends ViewGroup implements OnClickListener {
    private int cellSize;
    private boolean isHeaderRow;
    private Listener listener;
    private int oldHeightMeasureSpec;
    private int oldWidthMeasureSpec;

    public CalendarRowView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void addView(View child, int index, LayoutParams params) {
        child.setOnClickListener(this);
        super.addView(child, index, params);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (this.oldWidthMeasureSpec == widthMeasureSpec && this.oldHeightMeasureSpec == heightMeasureSpec) {
            Logr.d("SKIP Row.onMeasure");
            setMeasuredDimension(getMeasuredWidth(), getMeasuredHeight());
            return;
        }
        int cellHeightSpec;
        long start = System.currentTimeMillis();
        int totalWidth = MeasureSpec.getSize(widthMeasureSpec);
        this.cellSize = totalWidth / 7;
        int cellWidthSpec = MeasureSpec.makeMeasureSpec(this.cellSize, 1073741824);
        if (this.isHeaderRow) {
            cellHeightSpec = MeasureSpec.makeMeasureSpec(this.cellSize, ExploreByTouchHelper.INVALID_ID);
        } else {
            cellHeightSpec = cellWidthSpec;
        }
        int rowHeight = 0;
        int numChildren = getChildCount();
        for (int c = 0; c < numChildren; c++) {
            View child = getChildAt(c);
            child.measure(cellWidthSpec, cellHeightSpec);
            if (child.getMeasuredHeight() > rowHeight) {
                rowHeight = child.getMeasuredHeight();
            }
        }
        setMeasuredDimension((getPaddingLeft() + totalWidth) + getPaddingRight(), (getPaddingTop() + rowHeight) + getPaddingBottom());
        Logr.d("Row.onMeasure %d ms", Long.valueOf(System.currentTimeMillis() - start));
        this.oldWidthMeasureSpec = widthMeasureSpec;
        this.oldHeightMeasureSpec = heightMeasureSpec;
    }

    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        long start = System.currentTimeMillis();
        int cellHeight = bottom - top;
        int numChildren = getChildCount();
        for (int c = 0; c < numChildren; c++) {
            getChildAt(c).layout(this.cellSize * c, 0, (c + 1) * this.cellSize, cellHeight);
        }
        Logr.d("Row.onLayout %d ms", Long.valueOf(System.currentTimeMillis() - start));
    }

    public void setIsHeaderRow(boolean isHeaderRow) {
        this.isHeaderRow = isHeaderRow;
    }

    public void onClick(View v) {
        if (this.listener != null) {
            this.listener.handleClick((MonthCellDescriptor) v.getTag());
        }
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }
}
