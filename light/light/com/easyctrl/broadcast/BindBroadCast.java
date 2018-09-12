package com.easyctrl.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.easyctrl.iface.OnBindListenerView;
import com.easyctrl.ldy.domain.BindBean;

public class BindBroadCast extends BroadcastReceiver {
    public static final String bind_action = "com.easyctrl.broadcast.bind";
    private OnBindListenerView onBindListenerView;

    public void setOnBindListenerView(OnBindListenerView onBindListenerView) {
        this.onBindListenerView = onBindListenerView;
    }

    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(bind_action)) {
            Bundle data = intent.getExtras();
            if (data != null) {
                this.onBindListenerView.onBindShowDialog((BindBean) data.getSerializable("bindBean"));
            }
        }
    }
}
