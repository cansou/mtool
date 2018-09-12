package com.easyctrl.impl;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.easyctrl.iface.ProcesInstructImpl;
import com.easyctrl.ldy.activity.UserActivity;
import com.easyctrl.ldy.domain.ModulePortBean;

public class ProcesInstructUser implements ProcesInstructImpl {
    private static final int ENTERSETTING = 2;
    private static final int SHOWDIALOG = 3;
    public static final int UPDATEAMING = 5;
    public static final int UPDATESENDMESSAGE = 4;
    UserActivity activity;
    Bundle dataAming;
    private Handler handler;

    public ProcesInstructUser(UserActivity activity) {
        this.activity = activity;
    }

    public synchronized void lampProce(int id, int port, int type) {
        ModulePortBean bean = new ModulePortBean();
        bean.moduleID = id;
        bean.port = port;
        if (type == 0) {
            bean.isOpen = type;
            bean.progress = 0;
        } else if (type == 1) {
            bean.isOpen = type;
            bean.progress = 100;
        }
        Message msg = new Message();
        Bundle data = new Bundle();
        data.putSerializable("bean", bean);
        msg.what = 4;
        msg.setData(data);
        this.handler.sendMessage(msg);
    }

    public synchronized void ammingProce(int id, int port, int data) {
        ModulePortBean amingBean = new ModulePortBean();
        amingBean.moduleID = id;
        amingBean.port = port;
        if (data > 0) {
            amingBean.isOpen = 1;
            amingBean.progress = data;
        } else {
            amingBean.isOpen = 0;
            amingBean.progress = data;
        }
        Message msg = new Message();
        if (this.dataAming == null) {
            this.dataAming = new Bundle();
        }
        this.dataAming.putSerializable("amingBean", amingBean);
        msg.what = 5;
        msg.setData(this.dataAming);
        this.handler.sendMessage(msg);
    }

    public void loginSettingProce(int data) {
        if (data == 1) {
            this.handler.sendEmptyMessage(3);
        } else if (data == 0 && this.activity.getRunningActivityName().equals("com.easyctrl.ldy.activity.UserActivity")) {
            this.handler.sendEmptyMessage(2);
            Log.i("data", "loginSettingProce");
        }
    }

    public void loginApp(int data) {
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }
}
