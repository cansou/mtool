package com.easyctrl.dialog;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import com.easyctrl.ldy.activity.R;
import com.easyctrl.ui.base.BaseDialog;

public class SimpleDialog extends BaseDialog implements OnClickListener {
    private OnOptionListener onOptionListener;
    private TextView textView = ((TextView) findViewById(R.id.textisExit));
    private String title;

    public interface OnOptionListener {
        void onEnter();
    }

    public SimpleDialog(Context context) {
        super(context);
        setContentView((int) R.layout.simple_dialog);
        findViewById(R.id.cancel).setOnClickListener(this);
        findViewById(R.id.enter).setOnClickListener(this);
    }

    public void show() {
        if (this.title != null) {
            this.textView.setText(this.title);
        }
        super.show();
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void onClick(View v) {
        if (R.id.cancel == v.getId()) {
            dismiss();
        } else if (R.id.enter == v.getId()) {
            dismiss();
            if (this.onOptionListener != null) {
                this.onOptionListener.onEnter();
            }
        }
    }

    public void setOnOptionListener(OnOptionListener onOptionListener) {
        this.onOptionListener = onOptionListener;
    }
}
