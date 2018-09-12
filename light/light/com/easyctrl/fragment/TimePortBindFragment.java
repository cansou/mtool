package com.easyctrl.fragment;

import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.View;
import com.easyctrl.dialog.EasyProgressDialog;
import com.easyctrl.dialog.TimePortDialog;
import com.easyctrl.ldy.domain.TimerBean;

public class TimePortBindFragment extends BindFragment {
    private String TAG = TimePortBindFragment.class.getSimpleName();
    private TimerBean bean;
    private Handler handler = new Handler(new Callback() {
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    EasyProgressDialog.getInstance(TimePortBindFragment.this.settingActivity).show(TimePortBindFragment.this.progressDialog, "\u6570\u636e\u6b63\u5728\u4e0a\u4f20......", TimePortBindFragment.this.TAG);
                    break;
                case 2:
                    EasyProgressDialog.getInstance(TimePortBindFragment.this.settingActivity).dismiss(TimePortBindFragment.this.progressDialog);
                    break;
            }
            return false;
        }
    });
    private ProgressDialog progressDialog;

    public void setTimeBean(TimerBean bean) {
        this.bean = bean;
    }

    public String onClickThreeString() {
        this.progressDialog = new ProgressDialog(this.settingActivity);
        return "\u7ed1\u5b9a";
    }

    public void onClickOpreatorThree(View v) {
        TimePortDialog timePortDialog = new TimePortDialog(this.settingActivity);
        timePortDialog.setTimeBean(this.bean);
        timePortDialog.setHander(this.handler);
        timePortDialog.show();
    }
}
