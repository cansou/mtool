package com.easyctrl.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.easyctrl.iface.OnScreenStatus;

public class ScreenStatusReceiver extends BroadcastReceiver {
    String SCREEN_OFF = "android.intent.action.SCREEN_OFF";
    String SCREEN_ON = "android.intent.action.SCREEN_ON";
    private OnScreenStatus onScreenStatus;

    public void setOnScreenStatus(OnScreenStatus onScreenStatus) {
        this.onScreenStatus = onScreenStatus;
    }

    public void onReceive(Context context, Intent intent) {
        if (this.SCREEN_ON.equals(intent.getAction())) {
            if (this.onScreenStatus != null) {
                this.onScreenStatus.onScreneOpen();
            }
        } else if (this.SCREEN_OFF.equals(intent.getAction()) && this.onScreenStatus != null) {
            this.onScreenStatus.onScreneClose();
        }
    }
}
