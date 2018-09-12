package com.easyctrl.ldy.view;

import android.content.Context;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.easyctrl.ldy.activity.R;
import com.easyctrl.ldy.domain.HostTime;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class ShowHostTimeView extends RelativeLayout {
    private static final int UPDATETIME = 1;
    private String currentHostTime;
    private String currentTime;
    private Handler handler = new Handler(new Callback() {
        public boolean handleMessage(Message msg) {
            if (msg.what == 1) {
                ShowHostTimeView.this.systemTime.setText(ShowHostTimeView.this.currentTime);
                if (ShowHostTimeView.this.currentHostTime != null) {
                    ShowHostTimeView.this.hostTimeView.setText(ShowHostTimeView.this.currentHostTime);
                }
            }
            return false;
        }
    });
    private HostTime hostTime;
    private TextView hostTimeView;
    private LinearLayout hosttimeLayout;
    private boolean isPhoneTimeShow = false;
    private LayoutInflater mInflater;
    private LinearLayout phoneTimeLayout;
    private TextView systemTime;
    private Timer timer;
    private View view;

    private class Task extends TimerTask {
        private Task() {
        }

        public void run() {
            ShowHostTimeView.this.getSystemTime();
            ShowHostTimeView.this.createHostTime();
            ShowHostTimeView.this.handler.sendEmptyMessage(1);
        }
    }

    public void setPhoneTimeShow(boolean isPhoneTimeShow) {
        this.isPhoneTimeShow = isPhoneTimeShow;
    }

    public void setHostTime(HostTime hostTime) {
        this.hostTime = hostTime;
    }

    public ShowHostTimeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.isPhoneTimeShow = context.obtainStyledAttributes(attrs, R.styleable.hostShowTime).getBoolean(0, false);
        this.mInflater = LayoutInflater.from(context);
        this.view = this.mInflater.inflate(R.layout.show_host_time, this);
        this.hosttimeLayout = (LinearLayout) this.view.findViewById(R.id.hosttimeLayout);
        this.phoneTimeLayout = (LinearLayout) this.view.findViewById(R.id.phonetimeLayout);
        if (this.isPhoneTimeShow) {
            this.phoneTimeLayout.setVisibility(8);
        } else {
            this.phoneTimeLayout.setVisibility(0);
        }
        this.hostTimeView = (TextView) this.hosttimeLayout.findViewById(R.id.hostTime);
        this.systemTime = (TextView) this.phoneTimeLayout.findViewById(R.id.systemTime);
        this.timer = new Timer();
        this.timer.schedule(new Task(), 500, 1000);
    }

    private String getSystemTime() {
        this.currentTime = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date());
        return this.currentTime;
    }

    private String createHostTime() {
        if (this.hostTime == null) {
            return null;
        }
        String currentSec = this.currentTime.substring(17, this.currentTime.length());
        String currentMin = this.currentTime.substring(15, 16);
        hostMinCount(currentSec, this.hostTime);
        hostHourCount(currentMin, this.hostTime);
        this.currentHostTime = new StringBuilder(String.valueOf(this.hostTime.getYear())).append("/").append(this.hostTime.getMonth()).append("/").append(this.hostTime.getDay()).append(" ").append(this.hostTime.getHour()).append(":").append(this.hostTime.getMinute()).append(":").append(currentSec).toString();
        return this.currentHostTime;
    }

    private void hostMinCount(String currentSec, HostTime hostTime) {
        if (currentSec.equals("00")) {
            hostTime.setMinute(String.valueOf(Integer.parseInt(hostTime.getMinute()) + 1));
        }
    }

    private void hostHourCount(String currentMin, HostTime hostTime) {
        if (currentMin.equals("00")) {
            hostTime.setHour(String.valueOf(Integer.parseInt(hostTime.getHour()) + 1));
        }
    }

    public void cancel() {
        this.timer.cancel();
        this.timer = null;
    }
}
