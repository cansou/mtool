package com.easyctrl.manager;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import com.easyctrl.dialog.WaitDialog;
import com.easyctrl.dialog.WaitDialog.OnWorkdListener;
import com.easyctrl.ldy.activity.MainApplication;
import com.easyctrl.ldy.domain.ModuleBean;
import com.easyctrl.ldy.domain.ModulePortBean;
import java.util.ArrayList;
import java.util.HashMap;

public class DownLoadModuleManager {
    public Context context;
    private int dialogCannelUpdate;
    private int dialogTypeCannel;
    private int dialogTypeShow;
    private int error;
    private Handler handler;
    private ArrayList<ModuleBean> moduleBeans;

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public DownLoadModuleManager(Context context) {
        this.context = context;
    }

    private void sendMessageToHandler(int messageType) {
        if (this.handler != null) {
            this.handler.sendEmptyMessage(messageType);
        }
    }

    public void setError(int error) {
        this.error = error;
    }

    public void setDialogCannelUpdate(int dialogCannelUpdate) {
        this.dialogCannelUpdate = dialogCannelUpdate;
    }

    public void setDialogTypeShow(int dialogTypeShow) {
        this.dialogTypeShow = dialogTypeShow;
    }

    public void setDialogTypeCannel(int dialogTypeCannel) {
        this.dialogTypeCannel = dialogTypeCannel;
    }

    public void donwloadData() throws Exception {
        new WaitDialog(this.context, new OnWorkdListener() {
            public void doingWork() {
                try {
                    DownLoadModuleManager.this.sendMessageToHandler(DownLoadModuleManager.this.dialogTypeShow);
                    MainApplication.jsonManager.downloadData();
                    DownLoadModuleManager.this.moduleBeans = MainApplication.jsonManager.getModuleByJson();
                    MainApplication.jsonManager.getModulePortByJson();
                    HashMap<Integer, ArrayList<ModulePortBean>> hashMap = MainApplication.jsonManager.getHashMap();
                    if (DownLoadModuleManager.this.moduleBeans != null && DownLoadModuleManager.this.moduleBeans.size() > 0) {
                        MainApplication.moduleManager.setModulePortBeans(hashMap);
                        MainApplication.moduleManager.batchSave(DownLoadModuleManager.this.moduleBeans);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.i("data", "\u4e0b\u8f7d\u5931\u8d25");
                    DownLoadModuleManager.this.sendMessageToHandler(DownLoadModuleManager.this.error);
                }
                DownLoadModuleManager.this.sendMessageToHandler(DownLoadModuleManager.this.dialogCannelUpdate);
            }
        }, null).execute(new Integer[0]);
    }
}
