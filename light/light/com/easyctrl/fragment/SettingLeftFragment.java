package com.easyctrl.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import com.easyctrl.dialog.BackDialog;
import com.easyctrl.ldy.activity.MainApplication;
import com.easyctrl.ldy.activity.R;
import com.easyctrl.ldy.activity.SettingActivity;
import com.easyctrl.manager.UserManager;
import com.easyctrl.ui.base.BaseActivity;
import com.easyctrl.ui.base.BaseEasyCtrlFragment;
import de.greenrobot.event.EventBus;
import java.util.Stack;

public class SettingLeftFragment extends BaseEasyCtrlFragment implements OnClickListener {
    private SettingActivity activity;
    private TextView areaposition;
    private TextView bindkey;
    private UserManager userManager;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onCreateInit();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.setting_menu_layout, container, false);
        findViewByID(view);
        initWidget(view);
        return view;
    }

    public void onClick(View v) {
        if (R.id.areaposition == v.getId()) {
            ((Stack) this.activity.mStacks.get(BaseActivity.SETTING)).clear();
            SettingCenterFragment setting = SettingCenterFragment.newInstance();
            setting.setDownload(this.userManager.getDownloag());
            setting.setUpdateType(2);
            this.activity.pushFragments(BaseActivity.SETTING, setting, false, true, R.id.setting_center);
            this.activity.showLeft();
            MainApplication.userManager.setCurrentFragment("SettingCenterFragment");
        } else if (R.id.bindkey == v.getId()) {
            ((Stack) this.activity.mStacks.get(BaseActivity.SETTING)).clear();
            this.activity.pushFragments(BaseActivity.SETTING, BindFragment.newInstance(), false, true, R.id.setting_center);
            this.activity.showLeft();
            MainApplication.userManager.setCurrentFragment("BindFragment");
        } else if (R.id.virtualsetting == v.getId()) {
            ((Stack) this.activity.mStacks.get(BaseActivity.SETTING)).clear();
            this.activity.pushFragments(BaseActivity.SETTING, new VirtualFragmentList(), false, true, R.id.setting_center);
            this.activity.showLeft();
            MainApplication.userManager.setCurrentFragment("VirtualFragmentList");
        } else if (R.id.timersetting == v.getId()) {
            ((Stack) this.activity.mStacks.get(BaseActivity.SETTING)).clear();
            this.activity.pushFragments(BaseActivity.SETTING, new TimerFragmentList(), false, true, R.id.setting_center);
            this.activity.showLeft();
            MainApplication.userManager.setCurrentFragment("TimerFragmentList");
        } else if (R.id.user == v.getId()) {
            new BackDialog(this.activity).show();
        } else if (R.id.appsetting == v.getId()) {
            ((Stack) this.activity.mStacks.get(BaseActivity.SETTING)).clear();
            this.activity.pushFragments(BaseActivity.SETTING, new UserSetFragment(), false, true, R.id.setting_center);
            this.activity.showLeft();
            MainApplication.userManager.setCurrentFragment("UserSetFragment");
        } else if (R.id.systemsetting == v.getId()) {
            ((Stack) this.activity.mStacks.get(BaseActivity.SETTING)).clear();
            SystemSetFragment systemSetFragment = new SystemSetFragment();
            this.activity.pushFragments(BaseActivity.SETTING, systemSetFragment, false, true, R.id.setting_center);
            this.activity.showLeft();
            EventBus.getDefault().register(systemSetFragment);
            MainApplication.userManager.setCurrentFragment("SystemSetFragment");
        }
    }

    public void changeView(int id, Fragment fragment) {
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.setting_center, fragment);
        ft.commit();
        this.activity.showLeft();
    }

    protected void findViewByID(View view) {
        this.areaposition = (TextView) view.findViewById(R.id.areaposition);
        this.bindkey = (TextView) view.findViewById(R.id.bindkey);
        view.findViewById(R.id.virtualsetting).setOnClickListener(this);
        view.findViewById(R.id.timersetting).setOnClickListener(this);
        view.findViewById(R.id.user).setOnClickListener(this);
        view.findViewById(R.id.appsetting).setOnClickListener(this);
        view.findViewById(R.id.systemsetting).setOnClickListener(this);
    }

    protected void initWidget(View view) {
        this.areaposition.setOnClickListener(this);
        this.bindkey.setOnClickListener(this);
    }

    protected void onCreateInit() {
        this.activity = (SettingActivity) getActivity();
        this.userManager = UserManager.getInstance(this.activity);
    }
}
