package com.easyctrl.dialog;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import com.easyctrl.iface.OnDialogConnectNet;
import com.easyctrl.ldy.activity.R;
import com.easyctrl.ldy.util.ExitApp;
import com.easyctrl.ui.base.BaseDialog;

public class NetErrorDialog extends BaseDialog implements OnClickListener {
    ExitApp exitApp = ExitApp.getInstance();
    private OnDialogConnectNet onDialogConnectNet;

    public void setOnDialogConnectNet(OnDialogConnectNet onDialogConnectNet) {
        this.onDialogConnectNet = onDialogConnectNet;
    }

    public NetErrorDialog(Context context, OnDialogConnectNet onDialogConnectNet) {
        super(context);
        this.onDialogConnectNet = onDialogConnectNet;
        setContentView((int) R.layout.net_dialog);
        findViewById(R.id.cancel).setOnClickListener(this);
        findViewById(R.id.enter).setOnClickListener(this);
    }

    public void onClick(View v) {
        if (v.getId() == R.id.cancel) {
            this.exitApp.exit();
            dismiss();
        } else if (v.getId() == R.id.enter) {
            this.onDialogConnectNet.onDialogConnection();
            dismiss();
        }
    }
}
