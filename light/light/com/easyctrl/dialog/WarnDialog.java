package com.easyctrl.dialog;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import com.easyctrl.ldy.activity.R;
import com.easyctrl.ui.base.BaseDialog;

public class WarnDialog extends BaseDialog {
    private OnWarnClickListener onClickListener;
    private Button reconnect = ((Button) findViewById(R.id.reconnect));

    public interface OnWarnClickListener {
        void onConnect();
    }

    public WarnDialog(Context context) {
        super(context);
        setContentView((int) R.layout.warn_layout);
        if (this.reconnect != null) {
            this.reconnect.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    WarnDialog.this.onClickListener.onConnect();
                    WarnDialog.this.dismiss();
                }
            });
        }
    }

    public void setOnClickListener(OnWarnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }
}
