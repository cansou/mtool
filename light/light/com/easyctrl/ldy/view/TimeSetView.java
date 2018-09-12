package com.easyctrl.ldy.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import com.easyctrl.ldy.activity.R;

public class TimeSetView extends RelativeLayout {
    private static TimeSetView instance;
    private LayoutInflater mInflater;
    private View view = this.mInflater.inflate(R.layout.timer_bind_set, this);

    public static TimeSetView getInstance(Context context) {
        if (instance == null) {
            instance = new TimeSetView(context);
        }
        return instance;
    }

    private TimeSetView(Context context) {
        super(context);
        this.mInflater = LayoutInflater.from(context);
    }
}
