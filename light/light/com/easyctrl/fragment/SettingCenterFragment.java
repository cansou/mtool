package com.easyctrl.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import com.easyctrl.dialog.WaitViewTimer;
import com.easyctrl.ldy.activity.MainApplication;
import com.easyctrl.ldy.activity.R;
import com.easyctrl.ldy.activity.SettingActivity;
import com.easyctrl.ldy.domain.DeviceBean;
import com.easyctrl.ldy.util.StringValues;
import com.easyctrl.ldy.util.Value;
import com.easyctrl.ldy.view.WaitView;
import com.easyctrl.manager.DownLoadModuleManager;
import com.easyctrl.ui.base.BaseActivity;
import com.easyctrl.ui.base.BaseEasyCtrlFragment;
import de.greenrobot.event.EventBus;
import java.util.ArrayList;

public class SettingCenterFragment extends BaseEasyCtrlFragment implements OnClickListener {
    public static final int CREATEUPDATE = 0;
    public static final int LEFTUPDATE = 2;
    private String TAG = SettingCenterFragment.class.getSimpleName();
    private DownLoadModuleManager downLoadManager;
    private Handler handler = new Handler(new Callback() {
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 9:
                    WaitViewTimer.getInstance(SettingCenterFragment.this.settingActivity).dismiss(SettingCenterFragment.this.waitview);
                    break;
                case 10:
                    WaitViewTimer.getInstance(SettingCenterFragment.this.settingActivity).show(SettingCenterFragment.this.waitview);
                    break;
                case Value.SettingCenterFragment_HANDLER_UPTATELISTVIEW /*11*/:
                    WaitViewTimer.getInstance(SettingCenterFragment.this.settingActivity).dismiss(SettingCenterFragment.this.waitview);
                    SettingCenterFragment.this.change();
                    break;
                case Value.SettingCenterFragment_HANDLER_DOWNLOAD_ERROR /*12*/:
                    SettingCenterFragment.this.settingActivity.showToast("\u7f51\u7edc\u5f02\u5e38\uff0c\u65e0\u6cd5\u4e0b\u8f7d\u6570\u636e");
                    WaitViewTimer.getInstance(SettingCenterFragment.this.settingActivity).dismiss(SettingCenterFragment.this.waitview);
                    break;
            }
            return false;
        }
    });
    private boolean isDown = false;
    private SettingActivity settingActivity;
    public int updateType = 0;
    private WaitView waitview;

    public static SettingCenterFragment newInstance() {
        return new SettingCenterFragment();
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onCreateInit();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.setting_center_layout, container, false);
        findViewByID(view);
        initWidget(view);
        return view;
    }

    private void change() {
        ModuleListFragment moduleFragment = ModuleListFragment.newInstance();
        EventBus.getDefault().register(moduleFragment);
        this.settingActivity.pushFragments(BaseActivity.SETTING, moduleFragment, false, true, R.id.modulelayout);
        MainApplication.userManager.setCurrentFragment("ModuleListFragment");
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (!this.isDown) {
            try {
                this.downLoadManager.donwloadData();
            } catch (Exception e) {
                e.printStackTrace();
            }
            MainApplication.userManager.setDownload(true);
        }
    }

    private void initModuleDevice() {
        String[] type = StringValues.moduleType;
        for (int i = 0; i < type.length; i++) {
            DeviceBean deviceBean;
            if (type[i].equals("\u7167\u660e")) {
                String[] lamps = StringValues.moduleLamp;
                for (String str : lamps) {
                    deviceBean = new DeviceBean();
                    deviceBean.device_name = str;
                    deviceBean.device_type = type[i];
                    deviceBean.device_isSystem = 1;
                    MainApplication.deviceManager.add(deviceBean);
                }
            } else if (type[i].equals("\u7a97\u5e18")) {
                curs = StringValues.curtain;
                for (String str2 : curs) {
                    deviceBean = new DeviceBean();
                    deviceBean.device_name = str2;
                    deviceBean.device_type = type[i];
                    deviceBean.device_isSystem = 1;
                    MainApplication.deviceManager.add(deviceBean);
                }
            } else if (type[i].equals("\u7535\u5668")) {
                curs = StringValues.electrical;
                for (String str22 : curs) {
                    deviceBean = new DeviceBean();
                    deviceBean.device_name = str22;
                    deviceBean.device_type = type[i];
                    deviceBean.device_isSystem = 1;
                    MainApplication.deviceManager.add(deviceBean);
                }
            } else if (type[i].equals("\u7a7a\u8c03")) {
                curs = StringValues.air;
                for (String str222 : curs) {
                    deviceBean = new DeviceBean();
                    deviceBean.device_name = str222;
                    deviceBean.device_type = type[i];
                    deviceBean.device_isSystem = 1;
                    MainApplication.deviceManager.add(deviceBean);
                }
            }
        }
    }

    public void onClick(View v) {
        if (v.getId() == R.id.leftButton) {
            ((SettingActivity) getActivity()).showLeft();
        }
    }

    public void setUpdateType(int updateType) {
        this.updateType = updateType;
    }

    public void setDownload(boolean b) {
        this.isDown = b;
    }

    protected void findViewByID(View view) {
        view.findViewById(R.id.leftButton).setOnClickListener(this);
        this.waitview = (WaitView) view.findViewById(R.id.waitview);
        this.waitview.stopAnim();
    }

    protected void initWidget(View view) {
        if (this.isDown) {
            change();
        }
    }

    protected void onCreateInit() {
        this.settingActivity = (SettingActivity) getActivity();
        this.downLoadManager = new DownLoadModuleManager(this.settingActivity);
        this.downLoadManager.setHandler(this.handler);
        this.downLoadManager.setDialogCannelUpdate(11);
        this.downLoadManager.setDialogTypeCannel(9);
        this.downLoadManager.setDialogTypeShow(10);
        this.downLoadManager.setError(12);
        ArrayList<DeviceBean> temps = MainApplication.deviceManager.findAll();
        if (temps != null && temps.size() == 0) {
            initModuleDevice();
        }
    }
}
