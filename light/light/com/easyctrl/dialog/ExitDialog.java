package com.easyctrl.dialog;

import android.content.Context;
import android.os.Process;
import android.view.View;
import android.view.View.OnClickListener;
import com.easyctrl.db.BaseDB;
import com.easyctrl.ldy.activity.MainApplication;
import com.easyctrl.ldy.activity.R;
import com.easyctrl.ui.base.BaseDialog;

public class ExitDialog extends BaseDialog implements OnClickListener {
    private Context context;

    public ExitDialog(Context context) {
        super(context);
        this.context = context;
        setContentView((int) R.layout.exit);
        findViewById(R.id.cancel).setOnClickListener(this);
        findViewById(R.id.enter).setOnClickListener(this);
    }

    public void onClick(View v) {
        if (v.getId() == R.id.cancel) {
            dismiss();
        } else if (v.getId() == R.id.enter) {
            if (MainApplication.easySocket != null) {
                MainApplication.easySocket.close();
                MainApplication.easySocket = null;
            }
            BaseDB.getInstance(this.mContext).close();
            dismiss();
            MainApplication.exitApp.exit();
            Process.killProcess(Process.myPid());
            System.exit(0);
        }
    }
}
