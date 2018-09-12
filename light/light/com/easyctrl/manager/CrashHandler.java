package com.easyctrl.manager;

import android.content.Context;
import android.os.Looper;
import android.util.Log;
import com.easyctrl.dialog.AppErrorDialog;
import java.lang.Thread.UncaughtExceptionHandler;

public final class CrashHandler implements UncaughtExceptionHandler {
    private static CrashHandler instance;
    private Context mContext;
    private UncaughtExceptionHandler mDefaultHandler;

    public static CrashHandler getInstance() {
        if (instance == null) {
            instance = new CrashHandler();
        }
        return instance;
    }

    public void init(Context ctx) {
        this.mContext = ctx;
        this.mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    public void uncaughtException(Thread thread, Throwable ex) {
        StringBuilder builder = new StringBuilder();
        StackTraceElement[] err = ex.getStackTrace();
        for (int i = 0; i < err.length; i++) {
            builder.append("<font color=#7284C3> errNumber:" + err[i].getLineNumber() + "</font><br>");
            builder.append("<font color=#ff0000>" + err[i].getClassName() + "</font>" + "<br>");
            builder.append("errMethod:" + err[i].getMethodName() + "<br>");
            builder.append("err:" + ex.getMessage() + "<br>");
            builder.append("<p> </p> ");
        }
        final String msg = builder.toString();
        Log.i("data", "ex:" + msg);
        new Thread(new Runnable() {
            public void run() {
                Looper.prepare();
                AppErrorDialog errorDialog = AppErrorDialog.getInstance(CrashHandler.this.mContext);
                errorDialog.setErroeMessage(msg);
                errorDialog.show();
                Looper.loop();
            }
        }).start();
    }
}
