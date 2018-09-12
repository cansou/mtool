package com.easyctrl.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import com.easyctrl.event.EasyEventType;
import com.easyctrl.iface.OnBackDown;
import com.easyctrl.iface.OnBaseFragmentList;
import com.easyctrl.ldy.activity.MainApplication;
import com.easyctrl.ldy.activity.R;
import com.easyctrl.ldy.activity.UserActivity;
import com.easyctrl.ldy.domain.TimerBean;
import com.easyctrl.manager.OrderManage;
import com.easyctrl.ui.base.BaseActivity;
import de.greenrobot.event.EventBus;
import java.util.ArrayList;

public class TimerFragmentUser extends BaseFragmentListview implements OnBaseFragmentList, OnBackDown {
    private static final int Update = 1;
    private ArrayList<TimerBean> beans;
    private Handler handler = new Handler(Looper.getMainLooper(), new Callback() {
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    TimerFragmentUser.this.update();
                    break;
            }
            return false;
        }
    });

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setOnBaseFragmentList(this);
        this.userActivity.setOnBackDown(this);
        setTitle("\u5b9a\u65f6");
        ((UserActivity) getActivity()).sendData(OrderManage.searchAllTimer());
    }

    public ListAdapter onAdapter() {
        update();
        return MainApplication.timeUserAdapter;
    }

    private void update() {
        this.beans = MainApplication.timerManager.findBinded();
        MainApplication.timeUserAdapter.setBeans(this.beans);
        MainApplication.timeUserAdapter.notifyDataSetChanged();
    }

    public void onEventBackgroundThread(EasyEventType event) {
        if (event.getType() == 7) {
            MainApplication.timerManager.updateOpenType(event.getTimeID(), 1);
            this.handler.sendEmptyMessage(1);
            Log.i("data", "start");
        } else if (event.getType() == 8) {
            MainApplication.timerManager.updateOpenType(event.getTimeID(), 0);
            this.handler.sendEmptyMessage(1);
            Log.i("data", "stop");
        }
    }

    public void onItemListviewClick(AdapterView<?> adapterView, View view, int position, long id) {
        TimerBean bean = (TimerBean) this.beans.get(position);
        TimerBindSet fragment = new TimerBindSet();
        fragment.setTimeBean(bean);
        this.userActivity.mEventBus.register(fragment);
        this.userActivity.pushFragments(BaseActivity.USRT, fragment, true, true, R.id.center_layout);
    }

    public void onClick(View v) {
        this.userActivity.popFragments(BaseActivity.USRT, R.id.center_layout, true);
    }

    public void onKeyDonw(int keyCode, KeyEvent event) {
        if (keyCode == 4) {
            this.userActivity.popFragments(BaseActivity.USRT, R.id.center_layout, true);
        }
    }

    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
