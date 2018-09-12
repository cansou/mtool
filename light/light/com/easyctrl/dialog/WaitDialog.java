package com.easyctrl.dialog;

import android.content.Context;
import android.os.AsyncTask;

public class WaitDialog extends AsyncTask<Integer, Integer, String> {
    private OnPostExecuteListener onPostExecuteListener;
    private OnWorkdListener onWorkdListener;

    public interface OnPostExecuteListener {
        void after();
    }

    public interface OnWorkdListener {
        void doingWork();
    }

    public WaitDialog(Context context, OnWorkdListener onWorkdListener, OnPostExecuteListener onPostExecuteListener) {
        this.onWorkdListener = onWorkdListener;
        this.onPostExecuteListener = onPostExecuteListener;
    }

    protected String doInBackground(Integer... params) {
        if (this.onWorkdListener != null) {
            this.onWorkdListener.doingWork();
        }
        return null;
    }

    protected void onPostExecute(String result) {
        if (this.onPostExecuteListener != null) {
            this.onPostExecuteListener.after();
        }
    }

    protected void onPreExecute() {
    }
}
