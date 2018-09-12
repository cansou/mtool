package com.easyctrl.fragment;

import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.Toast;
import com.easyctrl.broadcast.BindBroadCast;
import com.easyctrl.dialog.DeleteTimerDialog;
import com.easyctrl.dialog.DeleteTimerDialog.OnClickListener;
import com.easyctrl.dialog.TimerBindDialog;
import com.easyctrl.iface.BaseFragmentImpl;
import com.easyctrl.iface.OnBindListenerView;
import com.easyctrl.iface.OnUpdateInterface;
import com.easyctrl.ldy.activity.MainApplication;
import com.easyctrl.ldy.activity.R;
import com.easyctrl.ldy.domain.BindBean;
import com.easyctrl.ldy.domain.ModulePortBean;
import com.easyctrl.ldy.domain.TimerBean;
import com.easyctrl.ldy.net.Packet;
import com.easyctrl.ldy.view.SelectPositionPopupWindow.OnDefineListener;
import com.easyctrl.manager.OrderManage;
import com.easyctrl.ui.base.BaseActivity;
import com.easyctrl.ui.base.BaseDialog.BaseDialogOnDismissListener;
import java.util.ArrayList;

public class TimerFragmentList extends BaseFragment implements BaseFragmentImpl, OnDefineListener, OnUpdateInterface, OnBindListenerView, OnItemClickListener, BaseDialogOnDismissListener {
    private String Tag = TimerFragmentList.class.getSimpleName();
    private TimerBean bean;
    private ArrayList<TimerBean> beans;
    private BindBroadCast bindBroadCast;
    private TimerBindDialog timerBindDialog;

    private void registerBindBroad() {
        this.bindBroadCast = new BindBroadCast();
        this.bindBroadCast.setOnBindListenerView(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BindBroadCast.bind_action);
        getActivity().registerReceiver(this.bindBroadCast, intentFilter);
    }

    public void onResume() {
        super.onResume();
        registerBindBroad();
        MainApplication.userManager.setCurrentFragment(this.Tag);
    }

    public void onPause() {
        super.onPause();
        this.settingActivity.unregisterReceiver(this.bindBroadCast);
    }

    public void onDestroy() {
        super.onDestroy();
        this.bean = null;
        this.bindBroadCast = null;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFragmentListener(this);
        setDimissBar(true);
        MainApplication.timerManager.batchSave();
        refush();
        this.timerBindDialog = new TimerBindDialog(this.settingActivity);
    }

    private void refush() {
        this.beans = MainApplication.timerManager.findAll();
        MainApplication.timerAdapter.setBeans(this.beans);
        MainApplication.timerAdapter.showCheck = false;
        MainApplication.timerAdapter.deleteList(MainApplication.timerAdapter.getSelects());
        MainApplication.timerAdapter.notifyDataSetChanged();
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.listview.setOnItemClickListener(this);
    }

    public void onBindShowDialog(BindBean bindBean) {
        if (MainApplication.bindManager.findBeanByModuleIdAndKeyIp(bindBean.bind_moduleID, bindBean.keyValue) == null) {
            MainApplication.bindManager.batchSave(bindBean.bind_moduleID);
        }
        BindBean bBean = MainApplication.bindManager.findBeanByModuleIdAndKeyIp(bindBean.bind_moduleID, bindBean.keyValue);
        if (bBean != null && bindBean.bind_stat == 1 && MainApplication.timerAdapter.getSelects().size() > 0) {
            if (bBean.bind_name == null) {
                Toast.makeText(this.settingActivity, "\u8be5\u6309\u952e\u5c1a\u672a\u7ed1\u5b9a,\u8bf7\u7ed1\u5b9a\u540e\u4f7f\u7528", 1).show();
            } else if (bBean.type == 2) {
                TimerBean timerBean = (TimerBean) MainApplication.timerAdapter.getSelects().get(0);
                this.timerBindDialog.setObject(bBean);
                this.timerBindDialog.setTimerBean(timerBean);
                this.timerBindDialog.show();
                this.timerBindDialog.setOnDismissListener(this);
            } else {
                Toast.makeText(this.settingActivity, "\u5355\u63a7\u8bf7\u70b9\u51fb\u53f3\u4e0a\u89d2\u2018\u7aef\u53e3\u2019\u8fdb\u884c\u5b9a\u65f6\u64cd\u4f5c", 1).show();
            }
        }
    }

    public void onUpdate(ArrayList<ModulePortBean> arrayList) {
    }

    public void onDefine(View anchor) {
    }

    public void onRefush(String content, View anchor, View view) {
    }

    public void onClickOperatorOne(View v) {
        new DeleteTimerDialog(getActivity(), new OnClickListener() {
            public void onEnter() {
                if (TimerFragmentList.this.bean != null) {
                    TimerFragmentList.this.deleteTimeInfo(TimerFragmentList.this.bean.timeID);
                    TimerFragmentList.this.sendDeleteTime(TimerFragmentList.this.bean);
                    TimerFragmentList.this.updateTimerData();
                    TimerFragmentList.this.refush();
                }
            }

            public void onCancel() {
            }
        }).show();
    }

    private void updateTimerData() {
        MainApplication.timerManager.initData(this.bean);
    }

    private void sendDeleteTime(TimerBean bean) {
        Packet deleteTimeJson = new Packet();
        deleteTimeJson.pack(OrderManage.deleteTime(bean.timeID));
        MainApplication.easySocket.send(deleteTimeJson);
        Packet deleteScene = new Packet();
        deleteScene.pack(OrderManage.deleteScene(bean.bindid));
        MainApplication.easySocket.send(deleteScene);
    }

    private void deleteTimeInfo(int id) {
        MainApplication.bindInfoManager.deleteByBindID(id);
    }

    public void onClickOpreatorTwo(View v) {
        if (MainApplication.timerAdapter.getSelects().size() > 0) {
            BindMList bindMList = new BindMList();
            bindMList.setSelectTimer(MainApplication.timerAdapter.getSelects());
            this.settingActivity.pushFragments(BaseActivity.SETTING, bindMList, true, true, R.id.setting_center);
            bindMList.setFragmentType(1);
        }
    }

    public void onClickOpreatorThree(View v) {
        if (MainApplication.timerAdapter.getSelects().size() > 0) {
            VirtualMList virtualMList = new VirtualMList();
            virtualMList.setSelectTimer(MainApplication.timerAdapter.getSelects());
            this.settingActivity.pushFragments(BaseActivity.SETTING, virtualMList, true, true, R.id.setting_center);
            virtualMList.setFragmentType(1);
        }
    }

    public void onClickOperatorFour(View v) {
        if (MainApplication.timerAdapter.getSelects().size() > 0) {
            TimePortBindFragment portBindFragment = new TimePortBindFragment();
            portBindFragment.isOpenBackDown(true);
            portBindFragment.setTimeBean((TimerBean) MainApplication.timerAdapter.getSelects().get(0));
            this.settingActivity.pushFragments(BaseActivity.SETTING, portBindFragment, true, true, R.id.setting_center);
            portBindFragment.setFragmentType(1);
        }
    }

    public ListAdapter onAdapter() {
        return MainApplication.timerAdapter;
    }

    public String onClickOneString() {
        return "\u6e05\u7a7a";
    }

    public String onClickTwoString() {
        return "\u7269\u7406";
    }

    public String onClickThreeString() {
        return "\u865a\u62df";
    }

    public String onClickFourString() {
        return "\u7aef\u53e3";
    }

    public String onTitle() {
        return "\u5b9a\u65f6\u8bbe\u7f6e";
    }

    public void onClickFloor(View v) {
    }

    public void onClickRoom(View v) {
    }

    public void onClickDevice(View v) {
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        this.bean = (TimerBean) this.beans.get(position);
        MainApplication.timerAdapter.deleteList(MainApplication.timerAdapter.getSelects());
        MainApplication.timerAdapter.selects.add(this.bean);
        MainApplication.timerAdapter.notifyDataSetChanged();
    }

    public void onDismiss() {
        this.beans = MainApplication.timerManager.findAll();
        MainApplication.timerAdapter.setBeans(this.beans);
        MainApplication.timerAdapter.notifyDataSetChanged();
    }
}
