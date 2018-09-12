package com.easyctrl.impl;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import com.easyctrl.ldy.activity.MainApplication;
import com.easyctrl.ldy.activity.R;
import com.easyctrl.ldy.activity.SettingActivity;
import com.easyctrl.ldy.activity.UserActivity;
import com.easyctrl.ldy.domain.ModulePortBean;
import com.easyctrl.ldy.net.Packet;
import com.easyctrl.ldy.util.Util;
import java.util.ArrayList;

public class OnOpenAndCloseListener implements OnClickListener {
    private static final int CLOSE = 1;
    private static final int OPEN = 0;
    private ModulePortBean modulePortBean;

    public OnOpenAndCloseListener(ModulePortBean modulePortBean) {
        this.modulePortBean = modulePortBean;
    }

    private void changeState(int isOpen) {
        ArrayList<ModulePortBean> selects = MainApplication.modulePortAdapterSimple.getSelects();
        for (int i = 0; i < selects.size(); i++) {
            ModulePortBean portBean = (ModulePortBean) selects.get(i);
            if (portBean.id == this.modulePortBean.id) {
                portBean.isOpen = isOpen;
            }
        }
    }

    public void onClick(View v) {
        if (R.id.open == v.getId()) {
            if (this.modulePortBean.type == 0) {
                sendLampOrder(0);
                changeState(1);
            } else if (this.modulePortBean.type == 1) {
                sendAnmingOrder(0);
            }
        } else if (R.id.close != v.getId()) {
        } else {
            if (this.modulePortBean.type == 0) {
                sendLampOrder(1);
                changeState(0);
            } else if (this.modulePortBean.type == 1) {
                sendAnmingOrder(1);
            }
        }
    }

    public void sendAnmingOrder(int type) {
        byte[] data = new byte[12];
        data[0] = (byte) -2;
        data[2] = (byte) 16;
        data[3] = Util.toHexByte(this.modulePortBean.moduleID);
        data[4] = Util.toHexByte(this.modulePortBean.port);
        data[6] = (byte) 4;
        data[8] = (byte) 5;
        data[10] = (byte) 16;
        data[11] = (byte) -54;
        if (type == 0) {
            data[10] = (byte) 18;
        } else if (type == 1) {
            data[7] = (byte) 0;
        }
        sendData(data);
    }

    private void sendData(byte[] data) {
        Packet in = new Packet();
        in.pack(data);
        MainApplication.easySocket.send(in);
    }

    public void sendLampOrder(int type) {
        byte[] data = new byte[12];
        data[0] = (byte) -2;
        data[2] = (byte) 16;
        data[3] = Util.toHexByte(this.modulePortBean.moduleID);
        data[4] = Util.toHexByte(this.modulePortBean.port);
        data[6] = (byte) 4;
        data[7] = (byte) 1;
        data[11] = (byte) -54;
        if (type == 0) {
            data[7] = (byte) 1;
        } else if (type == 1) {
            data[7] = (byte) 0;
        }
        sendData(data);
    }

    public void send(Activity activity, byte[] data) {
        if (activity instanceof SettingActivity) {
            ((SettingActivity) activity).sendData(data);
        } else if (activity instanceof UserActivity) {
            ((UserActivity) activity).sendData(data);
        }
    }
}
