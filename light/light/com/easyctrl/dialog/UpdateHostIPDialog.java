package com.easyctrl.dialog;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;
import com.easyctrl.dialog.WaitDialog.OnPostExecuteListener;
import com.easyctrl.dialog.WaitDialog.OnWorkdListener;
import com.easyctrl.ldy.activity.LoginActivity;
import com.easyctrl.ldy.activity.MainApplication;
import com.easyctrl.ldy.activity.R;
import com.easyctrl.ldy.activity.SettingActivity;
import com.easyctrl.manager.HostManager;
import com.easyctrl.manager.OrderManage;
import com.easyctrl.ui.base.BaseDialog;

public class UpdateHostIPDialog extends BaseDialog implements OnClickListener {
    private EditText editGateway;
    private EditText editIP = ((EditText) findViewById(R.id.editpass));
    private EditText editMask;
    private Handler handler;
    private HostManager hostManager;
    private SettingActivity settingActivity;

    public UpdateHostIPDialog(Context context) {
        super(context);
        this.settingActivity = (SettingActivity) context;
        setContentView((int) R.layout.updateip);
        findViewById(R.id.cancel).setOnClickListener(this);
        findViewById(R.id.enter).setOnClickListener(this);
        this.editIP.setText(MainApplication.userManager.currentHost());
        this.editGateway = (EditText) findViewById(R.id.editgateway);
        this.editGateway.setText(MainApplication.userManager.currentGateway());
        this.editMask = (EditText) findViewById(R.id.editDNS);
        this.editMask.setText(MainApplication.userManager.currentMask());
        this.hostManager = HostManager.getInstance();
    }

    public void onClick(View v) {
        if (R.id.cancel == v.getId()) {
            dismiss();
        } else if (R.id.enter == v.getId()) {
            new WaitDialog(this.settingActivity, new OnWorkdListener() {
                public void doingWork() {
                    UpdateHostIPDialog.this.sendMessageToHandler(1);
                    String newIP = UpdateHostIPDialog.this.editIP.getText().toString();
                    String gateway = UpdateHostIPDialog.this.editGateway.getText().toString();
                    String mask = UpdateHostIPDialog.this.editMask.getText().toString();
                    if (newIP.length() == 0 || gateway.length() == 0 || mask.length() == 0) {
                        new Handler().post(new Runnable() {
                            public void run() {
                                Toast.makeText(UpdateHostIPDialog.this.settingActivity, "\u53c2\u6570\u4e0d\u5408\u6cd5", 0).show();
                            }
                        });
                    } else {
                        UpdateHostIPDialog.this.hostManager.udpateIP(OrderManage.updateIP(MainApplication.userManager.currentHost(), newIP, gateway, mask));
                    }
                }
            }, new OnPostExecuteListener() {
                public void after() {
                    UpdateHostIPDialog.this.sendMessageToHandler(2);
                    if (MainApplication.easySocket != null) {
                        MainApplication.easySocket.close();
                        MainApplication.easySocket = null;
                    }
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    UpdateHostIPDialog.this.settingActivity.startActivity(new Intent(UpdateHostIPDialog.this.settingActivity, LoginActivity.class));
                    UpdateHostIPDialog.this.settingActivity.finish();
                }
            }).execute(new Integer[0]);
            dismiss();
        }
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    private void sendMessageToHandler(int messageType) {
        if (this.handler != null) {
            this.handler.sendEmptyMessage(messageType);
        }
    }
}
