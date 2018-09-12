package com.easyctrl.ldy.util;

import android.app.Activity;
import java.util.LinkedList;

public class ExitApp {
    private static ExitApp instance;
    private LinkedList<Activity> activities = new LinkedList();

    private ExitApp() {
    }

    public static ExitApp getInstance() {
        if (instance == null) {
            instance = new ExitApp();
        }
        return instance;
    }

    public void addActivity(Activity activity) {
        this.activities.offer(activity);
    }

    public void exit() {
        while (true) {
            Activity activity = (Activity) this.activities.poll();
            if (activity == null) {
                System.exit(0);
                return;
            }
            activity.finish();
        }
    }
}
