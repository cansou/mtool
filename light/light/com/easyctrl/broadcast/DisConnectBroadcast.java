package com.easyctrl.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class DisConnectBroadcast extends BroadcastReceiver {
    public static final String broadCast_Disconnect_ACTION = "com.easyctrl.broadcast.disconnect";

    public void onReceive(Context context, Intent intent) {
        Log.i("data", "\u5e7f\u64ad\u63a5\u6536\u5668");
    }
}
