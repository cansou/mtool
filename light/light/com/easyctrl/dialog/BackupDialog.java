package com.easyctrl.dialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;
import com.easyctrl.ldy.activity.MainApplication;
import com.easyctrl.ldy.activity.R;
import com.easyctrl.ldy.activity.SettingActivity;
import com.easyctrl.ldy.util.FileUtil;
import com.easyctrl.manager.OrderManage;
import com.easyctrl.ui.base.BaseDialog;
import java.io.File;

public class BackupDialog extends BaseDialog implements OnClickListener {
    private EditText editText;
    private String fileName;
    private Handler handler = new Handler();
    private ProgressDialog progressDialog = null;
    private SettingActivity settingActivity;

    public BackupDialog(Context context) {
        super(context);
        this.settingActivity = (SettingActivity) context;
        setContentView((int) R.layout.backup_dialog);
        this.editText = (EditText) findViewById(R.id.txtName);
        findViewById(R.id.cancel).setOnClickListener(this);
        findViewById(R.id.enter).setOnClickListener(this);
    }

    public void onClick(View v) {
        if (R.id.cancel == v.getId()) {
            dismiss();
        } else if (R.id.enter != v.getId()) {
        } else {
            if (FileUtil.iSExistSDCard()) {
                this.fileName = this.editText.getText().toString();
                File file = new File(new StringBuilder(String.valueOf(this.settingActivity.baseFile.getAbsolutePath())).append("/").append(this.fileName).toString());
                if (!file.exists()) {
                    file.mkdirs();
                }
                this.progressDialog = new ProgressDialog(this.settingActivity);
                this.progressDialog.setMessage("\u6570\u636e\u6b63\u5728\u4e0a\u4f20......");
                this.progressDialog.show();
                MainApplication.threadPool.submit(new Runnable() {
                    public void run() {
                        try {
                            FileUtil.backupData();
                            Thread.sleep(3000);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        BackupDialog.this.handler.post(new Runnable() {
                            public void run() {
                                MainApplication.userManager.setCurrentBackStata(true);
                                MainApplication.userManager.setPath(BackupDialog.this.fileName);
                                BackupDialog.this.settingActivity.sendData(OrderManage.backup());
                                BackupDialog.this.progressDialog.dismiss();
                            }
                        });
                    }
                });
                dismiss();
                return;
            }
            Toast.makeText(this.settingActivity, "SD\u5361\u4e0d\u5b58\u5728", 0).show();
        }
    }
}
