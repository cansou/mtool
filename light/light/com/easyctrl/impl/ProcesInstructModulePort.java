package com.easyctrl.impl;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import com.easyctrl.broadcast.UpdateBroadCast;
import com.easyctrl.iface.ProcesInstructImpl;
import com.easyctrl.ldy.activity.MainApplication;
import com.easyctrl.ldy.domain.ModulePortBean;

public class ProcesInstructModulePort implements ProcesInstructImpl {
    public static final int DEVICE_REGISTER = 4;
    public static final int DEVICE_REMOCE = 5;
    public static final int SHOW_NET_ERROR = 3;
    public static final int SettingActivity_Handler_Net_Connetion = 7;
    public static final int UPDATEAMING = 6;
    public static final int UPDATEDATA_AMIMG = 2;
    public static final int UPDATEDATA_OPEN = 1;
    public static final int UPDATELAMP = 5;
    private Context context;
    private Handler handler;
    Intent startbroad = new Intent(UpdateBroadCast.UPDATE_ACTION);
    Intent startbroadAming = new Intent(UpdateBroadCast.UPDATE_AMING_ACTION);

    public ProcesInstructModulePort(Context context) {
        this.context = context;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public void lampProce(int id, int port, int type) {
        ModulePortBean lampBean = new ModulePortBean();
        lampBean.moduleID = id;
        lampBean.port = port;
        if (type == 0) {
            lampBean.isOpen = type;
            lampBean.progress = 0;
        } else if (type == 1) {
            lampBean.isOpen = type;
            lampBean.progress = 100;
        }
        MainApplication.modulePortManager.update(lampBean, 0);
        Message message = new Message();
        Bundle data = new Bundle();
        data.putSerializable("lampBean", lampBean);
        message.setData(data);
        message.what = 1;
        this.handler.sendMessage(message);
    }

    public void ammingProce(int id, int port, int data) {
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
        Message message = new Message();
        Bundle amingData = new Bundle();
        amingData.putSerializable("amingBean", amingBean);
        message.setData(amingData);
        message.what = 2;
        if (this.handler != null) {
            this.handler.sendMessage(message);
        }
    }

    public void loginSettingProce(int data) {
    }

    public void loginApp(int data) {
    }
}
