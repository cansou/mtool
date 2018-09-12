package com.easyctrl.dialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.Toast;
import com.easyctrl.dialog.WaitDialog.OnPostExecuteListener;
import com.easyctrl.dialog.WaitDialog.OnWorkdListener;
import com.easyctrl.ldy.activity.MainApplication;
import com.easyctrl.ldy.activity.R;
import com.easyctrl.ldy.activity.UserActivity;
import com.easyctrl.ldy.util.FileUtil;
import com.easyctrl.ldy.util.Value;
import com.easyctrl.ui.base.BaseDialog;

public class BackDialog extends BaseDialog implements OnClickListener {
    private String TAG = BackDialog.class.getSimpleName();
    Handler handler = new Handler(new Callback() {
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    EasyProgressDialog.getInstance(BackDialog.this.mContext).show(BackDialog.this.mProgressDialog, "\u6b63\u5728\u4e0a\u4f20\u6570\u636e......", BackDialog.this.TAG);
                    break;
                case 2:
                    BackDialog.this.mProgressDialog.dismiss();
                    EasyProgressDialog.getInstance(BackDialog.this.mContext).dismiss(BackDialog.this.mProgressDialog);
                    break;
            }
            return false;
        }
    });
    private CheckBox isSave;
    private Context mContext;
    private ProgressDialog mProgressDialog;

    public BackDialog(Context context) {
        super(context);
        this.mContext = context;
        setContentView((int) R.layout.back_dialog);
        this.isSave = (CheckBox) findViewById(R.id.isSave);
        this.isSave.setChecked(true);
        findViewById(R.id.cancel).setOnClickListener(this);
        findViewById(R.id.enter).setOnClickListener(this);
        this.mProgressDialog = new ProgressDialog(this.mContext);
    }

    public void onClick(View v) {
        if (v.getId() == R.id.cancel) {
            dismiss();
        } else if (v.getId() == R.id.enter) {
            dismiss();
            if (!this.isSave.isChecked()) {
                this.mContext.startActivity(new Intent(this.mContext, UserActivity.class));
                MainApplication.userManager.setDownload(false);
            } else if (MainApplication.bindManager.getVersion() == Value.dbVersion) {
                backup();
            } else {
                Toast.makeText(this.mContext, "\u6570\u636e\u4e0d\u517c\u5bb9\uff0c\u8bf7\u5220\u9664\u4e3b\u673a\u548c\u624b\u673a\u6570\u636e\u518d\u91cd\u65b0\u64cd\u4f5c", 1).show();
            }
        }
    }

    private void backup() {
        new WaitDialog(this.mContext, new OnWorkdListener() {
            public void doingWork() {
                BackDialog.this.handler.sendEmptyMessage(1);
                try {
                    FileUtil.backupData();
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.i("data", new StringBuilder(String.valueOf(BackDialog.this.TAG)).append(" backDialog :").append(e.getMessage()).toString());
                    BackDialog.this.handler.sendEmptyMessage(2);
                }
            }
        }, new OnPostExecuteListener() {
            public void after() {
                BackDialog.this.handler.sendEmptyMessage(2);
                BackDialog.this.mContext.startActivity(new Intent(BackDialog.this.mContext, UserActivity.class));
                MainApplication.userManager.setDownload(false);
            }
        }).execute(new Integer[0]);
    }
}
