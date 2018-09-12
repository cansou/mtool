package com.easyctrl.dialog;

import android.content.Context;
import android.os.Handler;
import com.easyctrl.ldy.view.WaitView;

public class WaitViewTimer {
    private static WaitViewTimer instance;
    private Context mContext;

    private WaitViewTimer(Context context) {
        this.mContext = context;
    }

    public static WaitViewTimer getInstance(Context context) {
        if (instance == null) {
            instance = new WaitViewTimer(context);
        }
        return instance;
    }

    public void show(final WaitView view) {
        if (view != null) {
            view.startAnim();
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    if (!view.isStop()) {
                        view.stopAnim();
                    }
                }
            }, 5000);
        }
    }

    public void dismiss(WaitView view) {
        if (view != null) {
            view.stopAnim();
        }
    }
}
