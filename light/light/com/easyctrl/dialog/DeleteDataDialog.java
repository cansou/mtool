package com.easyctrl.dialog;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import com.easyctrl.ldy.activity.LoginActivity;
import com.easyctrl.ldy.activity.MainApplication;
import com.easyctrl.ldy.activity.R;
import com.easyctrl.ldy.activity.SettingActivity;
import com.easyctrl.ldy.util.FileUtil;
import com.easyctrl.manager.OrderManage;
import com.easyctrl.ui.base.BaseDialog;

public class DeleteDataDialog extends BaseDialog implements OnClickListener {
    private Handler handler = new Handler();
    private SettingActivity settingActivity;

    public DeleteDataDialog(Context context) {
        super(context);
        this.settingActivity = (SettingActivity) context;
        setContentView((int) R.layout.delete_dialog);
        findViewById(R.id.cancel).setOnClickListener(this);
        findViewById(R.id.enter).setOnClickListener(this);
    }

    public void onClick(View v) {
        if (R.id.cancel == v.getId()) {
            dismiss();
        } else if (R.id.enter == v.getId()) {
            init();
        }
    }

    private void init() {
        MainApplication.sceneManager.deleteAll();
        MainApplication.virtualManager.deleteAll();
        MainApplication.threadPool.submit(new Runnable() {
            public void run() {
                try {
                    FileUtil.backupData();
                    DeleteDataDialog.this.handler.post(new Runnable() {
                        public void run() {
                            DeleteDataDialog.this.dismiss();
                            DeleteDataDialog.this.settingActivity.sendData(OrderManage.deleteAll());
                            DeleteDataDialog.this.settingActivity.startActivity(new Intent(DeleteDataDialog.this.settingActivity, LoginActivity.class));
                            DeleteDataDialog.this.settingActivity.finish();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
