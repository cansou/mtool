package com.easyctrl.dialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import com.easyctrl.dialog.WaitDialog.OnPostExecuteListener;
import com.easyctrl.dialog.WaitDialog.OnWorkdListener;
import com.easyctrl.ldy.activity.R;
import com.easyctrl.ldy.util.FileUtil;
import com.easyctrl.ui.base.BaseActivity;
import com.easyctrl.ui.base.BaseDialog;

public class UserExitTimeSetOrBackUpDialog extends BaseDialog implements OnClickListener {
    private String TAG = UserExitTimeSetOrBackUpDialog.class.getSimpleName();
    Handler handler = new Handler(new Callback() {
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    EasyProgressDialog.getInstance(UserExitTimeSetOrBackUpDialog.this.mContext).show(UserExitTimeSetOrBackUpDialog.this.progressDialog, "\u6b63\u5728\u4e0a\u4f20\u6570\u636e......", UserExitTimeSetOrBackUpDialog.this.TAG);
                    break;
                case 2:
                    EasyProgressDialog.getInstance(UserExitTimeSetOrBackUpDialog.this.mContext).dismiss(UserExitTimeSetOrBackUpDialog.this.progressDialog);
                    break;
            }
            return false;
        }
    });
    private ProgressDialog progressDialog;

    public UserExitTimeSetOrBackUpDialog(Context context) {
        super(context);
        setContentView((int) R.layout.user_exit_time_back);
        findViewById(R.id.cancel).setOnClickListener(this);
        findViewById(R.id.enter).setOnClickListener(this);
        this.progressDialog = new ProgressDialog(this.mContext);
    }

    public void onClick(View v) {
        if (v.getId() == R.id.cancel) {
            dismiss();
            this.mContext.popFragments(BaseActivity.USRT, R.id.center_layout, true);
        } else if (v.getId() == R.id.enter) {
            dismiss();
            new WaitDialog(this.mContext, new OnWorkdListener() {
                public void doingWork() {
                    UserExitTimeSetOrBackUpDialog.this.handler.sendEmptyMessage(1);
                    try {
                        FileUtil.backupData();
                    } catch (Exception e) {
                        e.printStackTrace();
                        UserExitTimeSetOrBackUpDialog.this.handler.sendEmptyMessage(2);
                    }
                }
            }, new OnPostExecuteListener() {
                public void after() {
                    UserExitTimeSetOrBackUpDialog.this.handler.sendEmptyMessage(2);
                    UserExitTimeSetOrBackUpDialog.this.mContext.popFragments(BaseActivity.USRT, R.id.center_layout, true);
                }
            }).execute(new Integer[0]);
        }
    }
}
