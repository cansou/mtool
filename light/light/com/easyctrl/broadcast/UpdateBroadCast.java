package com.easyctrl.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Looper;
import android.os.Message;
import com.easyctrl.iface.OnUpdateInterface;
import com.easyctrl.ldy.activity.MainApplication;
import com.easyctrl.ldy.domain.ModulePortBean;
import java.util.ArrayList;

public class UpdateBroadCast extends BroadcastReceiver {
    public static final String UPDATE_ACTION = "com.easyctrl.broadcast.update";
    public static final String UPDATE_AMING_ACTION = "com.easyctrl.broadcast.update_aming";
    private static ArrayList<ModulePortBean> beans = null;
    private static final int update = 1;
    private Handler handler = new Handler(Looper.myLooper(), new Callback() {
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    if (UpdateBroadCast.this.updateInterface != null) {
                        UpdateBroadCast.this.updateInterface.onUpdate(UpdateBroadCast.beans);
                        break;
                    }
                    break;
            }
            return false;
        }
    });
    private OnUpdateInterface updateInterface;

    public void setUpdateInterface(OnUpdateInterface updateInterface) {
        this.updateInterface = updateInterface;
    }

    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(UPDATE_ACTION)) {
            final Bundle data = intent.getExtras();
            MainApplication.threadPool.submit(new Runnable() {
                public void run() {
                    UpdateBroadCast.beans = MainApplication.modulePortManager.findByModuleID(((ModulePortBean) data.getSerializable("lampBean")).moduleID);
                    if (UpdateBroadCast.this.updateInterface != null) {
                        UpdateBroadCast.this.updateInterface.onUpdate(UpdateBroadCast.beans);
                    }
                    UpdateBroadCast.this.handler.sendEmptyMessage(1);
                }
            });
        } else if (action.equals(UPDATE_AMING_ACTION)) {
            try {
                beans = MainApplication.modulePortManager.findByModuleID(((ModulePortBean) intent.getExtras().getSerializable("amingBean")).moduleID);
                if (this.updateInterface != null) {
                    this.updateInterface.onUpdate(beans);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
