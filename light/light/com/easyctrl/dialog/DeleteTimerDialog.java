package com.easyctrl.dialog;

import android.content.Context;
import android.view.View;
import com.easyctrl.ldy.activity.R;
import com.easyctrl.ui.base.BaseDialog;

public class DeleteTimerDialog extends BaseDialog implements android.view.View.OnClickListener {
    private OnClickListener onClickListener;

    public interface OnClickListener {
        void onCancel();

        void onEnter();
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public DeleteTimerDialog(Context context, OnClickListener onClickListener) {
        super(context);
        this.onClickListener = onClickListener;
        setContentView((int) R.layout.dialog_delete_time);
        findViewById(R.id.cancel).setOnClickListener(this);
        findViewById(R.id.enter).setOnClickListener(this);
    }

    public void onClick(View v) {
        if (v.getId() == R.id.cancel) {
            this.onClickListener.onCancel();
            dismiss();
        } else if (v.getId() == R.id.enter) {
            this.onClickListener.onEnter();
            dismiss();
        }
    }
}
