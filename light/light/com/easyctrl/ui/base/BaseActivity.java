package com.easyctrl.ui.base;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;
import com.easyctrl.ldy.activity.R;
import de.greenrobot.event.EventBus;
import java.io.File;
import java.util.HashMap;
import java.util.Stack;

public abstract class BaseActivity extends FragmentActivity {
    public static final String SETTING = "setting";
    public static final String USRT = "user";
    public File baseFile;
    public Context mContext;
    public EventBus mEventBus;
    public HashMap<String, Stack<Fragment>> mStacks;

    protected abstract void findViewByID();

    protected abstract void initWidget();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        this.mContext = this;
        this.mStacks = new HashMap();
        this.mStacks.put(SETTING, new Stack());
        this.mStacks.put(USRT, new Stack());
        this.mEventBus = EventBus.getDefault();
        this.baseFile = Environment.getExternalStorageDirectory();
    }

    public void clearStackByTag(String tag) {
        Stack<Fragment> fragments = (Stack) this.mStacks.get(tag);
        if (fragments != null) {
            fragments.clear();
        }
    }

    public void pushFragments(String tag, Fragment fragment, boolean shouldAnimate, boolean shouldAdd, int replaceID) {
        if (shouldAdd) {
            ((Stack) this.mStacks.get(tag)).push(fragment);
        }
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (shouldAnimate) {
            ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        }
        if (replaceID == 0) {
            ft.replace(R.id.setting_center, fragment);
        } else {
            ft.replace(replaceID, fragment);
        }
        ft.commitAllowingStateLoss();
    }

    public void popFragments(String tag, int replaceID, boolean isAmin) {
        int size = ((Stack) this.mStacks.get(tag)).size();
        if (size >= 2) {
            Fragment fragment = (Fragment) ((Stack) this.mStacks.get(tag)).elementAt(size - 2);
            ((Stack) this.mStacks.get(tag)).pop();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            if (isAmin) {
                ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
            }
            ft.replace(replaceID, fragment);
            ft.commit();
        }
    }

    public void showToast(String content) {
        Toast.makeText(this.mContext, content, 1).show();
    }
}
