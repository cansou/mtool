package com.easyctrl.dialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;

public class EasyProgressDialog {
    private static EasyProgressDialog instance;
    private Handler handler;
    private Context mContext;
    private int showType;

    public void setShowType(int showType) {
        this.showType = showType;
    }

    private EasyProgressDialog(Context context) {
        this.mContext = context;
    }

    public static EasyProgressDialog getInstance(Context context) {
        if (instance == null) {
            instance = new EasyProgressDialog(context);
        }
        return instance;
    }

    public void show(final ProgressDialog progressDialog, String message, String tag) {
        if (progressDialog != null) {
            progressDialog.setMessage(message);
            progressDialog.show();
            if (!tag.equals("SystemSetFragment")) {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    }
                }, 10000);
            }
        }
    }

    public void dismiss(ProgressDialog progressDialog) {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}
