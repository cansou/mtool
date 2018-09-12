package com.easyctrl.impl;

import android.widget.TextView;
import com.easyctrl.iface.OnConnectedProcess;

public class ConnectedPrccess implements OnConnectedProcess {
    private TextView textView;

    public ConnectedPrccess(TextView textView) {
        this.textView = textView;
    }

    public void onDisconnect() {
    }

    public void connectSuccess() {
    }
}
