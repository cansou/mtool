package com.easyctrl.ldy.calendar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.widget.ExploreByTouchHelper;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import com.easyctrl.ldy.activity.R;

public class CalendarGridView extends ViewGroup {
    private final Paint dividerPaint = new Paint();

    public CalendarGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.dividerPaint.setColor(getResources().getColor(R.color.calendar_divider));
    }

    public void addView(View child, int index, LayoutParams params) {
        if (getChildCount() == 0) {
            ((CalendarRowView) child).setIsHeaderRow(true);
        }
        super.addView(child, index, params);
    }

    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        ViewGroup row = (ViewGroup) getChildAt(1);
        int top = row.getTop();
        int bottom = getBottom();
        int left = row.getChildAt(0).getLeft() + getLeft();
        canvas.drawLine((float) left, (float) top, (float) left, (float) bottom, this.dividerPaint);
        for (int c = 0; c < 7; c++) {
            int x = (row.getChildAt(c).getRight() + left) - 1;
            canvas.drawLine((float) x, (float) top, (float) x, (float) bottom, this.dividerPaint);
        }
    }

    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        boolean retVal = super.drawChild(canvas, child, drawingTime);
        int bottom = child.getBottom() - 1;
        canvas.drawLine((float) child.getLeft(), (float) bottom, (float) child.getRight(), (float) bottom, this.dividerPaint);
        return retVal;
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        long start = System.currentTimeMillis();
        int cellSize = MeasureSpec.getSize(widthMeasureSpec) / 7;
        int totalWidth = cellSize * 7;
        int totalHeight = 0;
        int rowWidthSpec = MeasureSpec.makeMeasureSpec(totalWidth, 1073741824);
        int rowHeightSpec = MeasureSpec.makeMeasureSpec(cellSize, 1073741824);
        int numChildren = getChildCount();
        for (int c = 0; c < numChildren; c++) {
            View child = getChildAt(c);
            if (child.getVisibility() == 0) {
                if (c == 0) {
                    measureChild(child, rowWidthSpec, MeasureSpec.makeMeasureSpec(cellSize, ExploreByTouchHelper.INVALID_ID));
                } else {
                    measureChild(child, rowWidthSpec, rowHeightSpec);
                }
                totalHeight += child.getMeasuredHeight();
            }
        }
        setMeasuredDimension(totalWidth + 2, totalHeight);
        Logr.d("Grid.onMeasure %d ms", Long.valueOf(System.currentTimeMillis() - start));
    }

    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        long start = System.currentTimeMillis();
        top = 0;
        int numChildren = getChildCount();
        for (int c = 0; c < numChildren; c++) {
            View child = getChildAt(c);
            int rowHeight = child.getMeasuredHeight();
            child.layout(left, top, right, top + rowHeight);
            top += rowHeight;
        }
        Logr.d("Grid.onLayout %d ms", Long.valueOf(System.currentTimeMillis() - start));
    }
}
