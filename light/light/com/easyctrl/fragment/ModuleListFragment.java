package com.easyctrl.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import com.easyctrl.dialog.WaitViewTimer;
import com.easyctrl.event.EasyEventTypeHandler;
import com.easyctrl.ldy.activity.MainApplication;
import com.easyctrl.ldy.activity.R;
import com.easyctrl.ldy.activity.SettingActivity;
import com.easyctrl.ldy.domain.ImitateBean.Table;
import com.easyctrl.ldy.domain.ModuleBean;
import com.easyctrl.ldy.view.WaitView;
import com.easyctrl.manager.DownLoadModuleManager;
import com.easyctrl.ui.base.BaseActivity;
import com.easyctrl.ui.base.BaseEasyCtrlFragment;
import java.util.ArrayList;

public class ModuleListFragment extends BaseEasyCtrlFragment implements OnClickListener {
    public static final int BACKUPDATE = 1;
    public static final int CREATEUPDATE = 0;
    public static final int LEFTUPDATE = 2;
    private static ArrayList<ModuleBean> moduleBeans;
    private String TAG = ModuleListFragment.class.getSimpleName();
    private DownLoadModuleManager downLoadManager;
    public Handler handler = new Handler(new Callback() {
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    WaitViewTimer.getInstance(ModuleListFragment.this.settingActivity).dismiss(ModuleListFragment.this.waitview);
                    ModuleListFragment.refurbish();
                    break;
                case 1:
                    WaitViewTimer.getInstance(ModuleListFragment.this.settingActivity).show(ModuleListFragment.this.waitview);
                    break;
                case 2:
                    WaitViewTimer.getInstance(ModuleListFragment.this.settingActivity).dismiss(ModuleListFragment.this.waitview);
                    break;
            }
            return false;
        }
    });
    private boolean isSort;
    private ListView listview;
    private SettingActivity settingActivity;
    private WaitView waitview;

    public static ModuleListFragment newInstance() {
        return new ModuleListFragment();
    }

    private static void refurbish() {
        moduleBeans = MainApplication.jsonManager.getModuleByJson();
        MainApplication.moduleAdapter.setBeans(moduleBeans);
        MainApplication.moduleAdapter.notifyDataSetChanged();
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onCreateInit();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.module_layout, null);
        findViewByID(view);
        initWidget(view);
        return view;
    }

    private void proceItemClick() {
        this.listview.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                ModuleListFragment.this.startModulePortFragment(position);
            }
        });
    }

    private void startModulePortFragment(int position) {
        ModulePortListFragment listFragmet = ModulePortListFragment.newInstance();
        ModuleBean bean = (ModuleBean) moduleBeans.get(position);
        if (MainApplication.modulePortManager.findByModuleID(bean.moduleId).size() > 0) {
            listFragmet.setTitle(bean, position);
            listFragmet.setModuleID(bean.moduleId, bean.type);
            this.settingActivity.pushFragments(BaseActivity.SETTING, listFragmet, false, true, R.id.modulelayout);
            MainApplication.userManager.setCurrentFragment("ModulePortListFragment");
        }
    }

    public void onClick(View v) {
        if (v.getId() == R.id.devicemodle) {
            moduleBeans = MainApplication.moduleManager.findAllByWhereSort("moduleModel");
            this.isSort = false;
            MainApplication.moduleAdapter.setBeans(moduleBeans);
            MainApplication.moduleAdapter.notifyDataSetChanged();
        } else if (v.getId() == R.id.devicename) {
            moduleBeans = MainApplication.moduleManager.findAllByWhereSort("moduleNameExt");
            this.isSort = false;
            MainApplication.moduleAdapter.setBeans(moduleBeans);
            MainApplication.moduleAdapter.notifyDataSetChanged();
        } else if (v.getId() == R.id.deviceNum) {
            if (this.isSort) {
                moduleBeans = MainApplication.moduleManager.findAllByWhereSort("id desc");
                this.isSort = false;
            } else {
                moduleBeans = MainApplication.moduleManager.findAllByWhereSort(Table.FIELD_ID);
                this.isSort = true;
            }
            MainApplication.moduleAdapter.setBeans(moduleBeans);
            MainApplication.moduleAdapter.notifyDataSetChanged();
        } else if (v.getId() == R.id.search) {
            downData();
        }
    }

    private void downData() {
        try {
            this.downLoadManager.donwloadData();
            this.downLoadManager.setHandler(this.handler);
            this.downLoadManager.setDialogCannelUpdate(0);
            this.downLoadManager.setDialogTypeShow(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onEventMainThread(EasyEventTypeHandler event) {
        if (event.getType() == 3) {
            downData();
        } else if (event.getType() == 4) {
            downData();
        }
    }

    public void onDestroy() {
        super.onDestroy();
        this.settingActivity.mEventBus.unregister(this);
    }

    protected void findViewByID(View view) {
        this.listview = (ListView) view.findViewById(R.id.listview);
        view.findViewById(R.id.deviceNum).setOnClickListener(this);
        view.findViewById(R.id.devicemodle).setOnClickListener(this);
        view.findViewById(R.id.devicename).setOnClickListener(this);
        view.findViewById(R.id.search).setOnClickListener(this);
        this.waitview = (WaitView) view.findViewById(R.id.waitview);
        this.waitview.stopAnim();
    }

    protected void initWidget(View view) {
        this.listview.setAdapter(MainApplication.moduleAdapter);
        proceItemClick();
        this.isSort = true;
        refurbish();
    }

    protected void onCreateInit() {
        this.settingActivity = (SettingActivity) getActivity();
        moduleBeans = MainApplication.jsonManager.getModuleByJson();
        this.downLoadManager = new DownLoadModuleManager(this.settingActivity);
        this.downLoadManager.setHandler(this.handler);
    }

    public void onResume() {
        super.onResume();
        if (this.waitview != null) {
            this.waitview.stopAnim();
        }
    }
}
