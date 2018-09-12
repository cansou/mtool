package com.easyctrl.dialog;

import android.content.Context;
import android.os.Process;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import com.easyctrl.ldy.activity.MainApplication;
import com.easyctrl.ldy.activity.R;
import com.easyctrl.ui.base.BaseDialog;

public class AppErrorDialog extends BaseDialog implements OnClickListener {
    private static AppErrorDialog instance;
    private TextView errorlog = ((TextView) findViewById(R.id.errorlog));

    public static AppErrorDialog getInstance(Context context) {
        if (instance == null) {
            instance = new AppErrorDialog(context);
        }
        return instance;
    }

    public AppErrorDialog(Context context) {
        super(context);
        setContentView((int) R.layout.app_error_dialog);
        findViewById(R.id.confirm).setOnClickListener(this);
    }

    public void onClick(View v) {
        if (v.getId() == R.id.confirm) {
            MainApplication.exitApp.exit();
            Process.killProcess(Process.myPid());
            System.exit(10);
        }
    }

    public void setErroeMessage(String errMsg) {
        this.errorlog.setText(Html.fromHtml(errMsg));
    }

    public void show() {
        super.show();
    }
}
