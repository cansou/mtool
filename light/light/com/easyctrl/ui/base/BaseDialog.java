package com.easyctrl.ui.base;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import com.easyctrl.ldy.activity.R;

public class BaseDialog {
    private Dialog baseDialog;
    public Context mContext;
    public LayoutInflater mInflater;
    private BaseDialogOnDismissListener onDismissListener;
    private Window window = this.baseDialog.getWindow();

    public interface BaseDialogOnDismissListener {
        void onDismiss();
    }

    public void setOnDismissListener(BaseDialogOnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
    }

    public BaseDialog(Context context) {
        this.mInflater = LayoutInflater.from(context);
        this.mContext = context;
        this.baseDialog = new Dialog(context, R.style.BaseDialog);
        this.baseDialog.setCanceledOnTouchOutside(false);
        this.baseDialog.setCancelable(true);
        this.baseDialog.setOnDismissListener(new OnDismissListener() {
            public void onDismiss(DialogInterface dialog) {
                if (BaseDialog.this.onDismissListener != null) {
                    BaseDialog.this.onDismissListener.onDismiss();
                }
            }
        });
    }

    public void setContentView(int layoutResID) {
        this.baseDialog.setContentView(layoutResID);
    }

    public void setContentView(View view) {
        this.baseDialog.setContentView(view);
    }

    public View findViewById(int viewId) {
        return this.baseDialog.findViewById(viewId);
    }

    public void show() {
        this.baseDialog.show();
    }

    public void dismiss() {
        this.baseDialog.dismiss();
    }

    public void cancel() {
        this.baseDialog.cancel();
    }

    public Window getWindow() {
        return this.window;
    }

    public boolean isShowing() {
        return this.baseDialog.isShowing();
    }
}
