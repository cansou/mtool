package com.easyctrl.fragment;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.Toast;
import com.easyctrl.dialog.BindDialog;
import com.easyctrl.dialog.SceneBindDialog;
import com.easyctrl.dialog.TimerBindDialog;
import com.easyctrl.event.EasyEventTypeHandler;
import com.easyctrl.iface.OnBackDown;
import com.easyctrl.iface.OnBaseFragmentList;
import com.easyctrl.iface.OnTouchAction;
import com.easyctrl.ldy.activity.MainApplication;
import com.easyctrl.ldy.activity.R;
import com.easyctrl.ldy.domain.BindBean;
import com.easyctrl.ldy.domain.ModuleBean;
import com.easyctrl.ldy.domain.ModulePortBean;
import com.easyctrl.ldy.domain.SceneBean;
import com.easyctrl.ldy.domain.TextDescription;
import com.easyctrl.ldy.domain.TimerBean;
import com.easyctrl.ldy.domain.UserScene;
import com.easyctrl.ldy.util.SaveSingleObject;
import com.easyctrl.manager.OrderManage;
import com.easyctrl.ui.base.BaseActivity;
import com.easyctrl.ui.base.BaseDialog.BaseDialogOnDismissListener;
import de.greenrobot.event.EventBus;
import java.util.ArrayList;
import java.util.Arrays;

public class BindKeyGridView extends BaseFragmentGrid implements OnBaseFragmentList, OnTouchAction, OnBackDown {
    private ArrayList<BindBean> beans;
    private BindDialog bindDialog;
    private boolean isBack = false;
    private ModuleBean module;
    private Object object;
    private SceneBindDialog sceneBindDialog;
    private TimerBean selectTimerBean;
    private ArrayList<ModulePortBean> selects;
    private TimerBindDialog timerBindDialog;

    public Object getObject() {
        return this.object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    private int[] createNames(ArrayList<SceneBean> scenes) {
        int[] names = new int[scenes.size()];
        for (int i = 0; i < names.length; i++) {
            names[i] = Integer.valueOf(((SceneBean) scenes.get(i)).sceneBean_jsonName).intValue();
        }
        return names;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.settingActivity.setOnBackDown(this);
        this.module = (ModuleBean) SaveSingleObject.getInstance().getObject();
        this.beans = MainApplication.bindManager.findBeansByModuleID(this.module.moduleId);
        for (int i = 0; i < this.beans.size(); i++) {
            BindBean bindBean = (BindBean) this.beans.get(i);
            ArrayList<SceneBean> scenes = MainApplication.sceneManager.findByIDandKey(bindBean.bind_moduleID, bindBean.keyValue);
            if (scenes != null) {
                bindBean.bind_name = Arrays.toString(createNames(scenes));
            }
        }
        MainApplication.bindKeyGridAdapter.setBeans(this.beans);
        setOnBaseFragmentList(this);
        MainApplication.bindKeyGridAdapter.setOnTouchAction(this);
        MainApplication.bindKeyGridAdapter.notifyDataSetChanged();
        this.isBack = true;
        this.timerBindDialog = new TimerBindDialog(this.settingActivity);
        this.bindDialog = new BindDialog(this.settingActivity);
        this.bindDialog.setStartType(1);
        this.sceneBindDialog = new SceneBindDialog(this.settingActivity);
        EventBus.getDefault().register(this);
    }

    public void onPause() {
        super.onPause();
        this.isBack = false;
    }

    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public ListAdapter onAdapter() {
        return MainApplication.bindKeyGridAdapter;
    }

    public void onItemListviewClick(AdapterView<?> adapterView, View view, int position, long id) {
    }

    public void onClick(View v) {
        if (v.getId() == R.id.leftButton) {
            this.settingActivity.popFragments(BaseActivity.SETTING, R.id.setting_center, true);
            this.settingActivity.popFragments(BaseActivity.SETTING, R.id.setting_center, true);
            ((TextDescription) SaveSingleObject.getInstance().getTextObject()).isListBack = false;
        } else if (v.getId() == R.id.titleView) {
            this.settingActivity.pushFragments(BaseActivity.SETTING, new BindMList(), true, false, R.id.setting_center);
        }
    }

    public void setModuleID(ModuleBean moduleBean) {
        this.module = moduleBean;
    }

    public void setSelectBeans(ArrayList<ModulePortBean> selects) {
        this.selects = selects;
    }

    public void onDown(View v, int position) {
        BindBean bindBean = (BindBean) this.beans.get(position);
        if (this.object != null) {
            UserScene userScene = this.object;
            this.sceneBindDialog.setObject(bindBean);
            this.sceneBindDialog.setUserScene(userScene);
            this.sceneBindDialog.show();
            this.object = null;
        } else if (this.selectTimerBean == null || MainApplication.timerAdapter.getSelects().size() <= 0) {
            v.setBackgroundResource(R.drawable.item_back_shape);
            if (this.selects == null || this.selects.size() <= 0) {
                this.settingActivity.sendData(OrderManage.bindDownAndUp(bindBean.bind_moduleID, bindBean.keyValue, true));
                return;
            }
            this.bindDialog.setBindBean(bindBean);
            this.bindDialog.setSelectBeans(this.selects);
            this.bindDialog.show();
            this.bindDialog.setOnDismissListener(new BaseDialogOnDismissListener() {
                public void onDismiss() {
                    BindKeyGridView.this.updateGridView();
                }
            });
        } else if (bindBean.bind_name == null) {
            Toast.makeText(this.settingActivity, "\u76ee\u524d\u8be5\u6309\u952e\u6ca1\u6709\u7ed1\u5b9a", 1).show();
        } else if (bindBean.type == 2) {
            TimerBean timerBean = (TimerBean) MainApplication.timerAdapter.getSelects().get(0);
            this.timerBindDialog.setObject(bindBean);
            this.timerBindDialog.setTimerBean(timerBean);
            this.timerBindDialog.show();
        } else {
            Toast.makeText(this.settingActivity, "\u5355\u63a7\u8bf7\u70b9\u51fb\u53f3\u4e0a\u89d2\u2018\u7aef\u53e3\u2019\u8fdb\u884c\u5b9a\u65f6\u64cd\u4f5c", 1).show();
        }
    }

    private void updateGridView() {
        this.beans = MainApplication.bindManager.findBeansByModuleID(this.module.moduleId);
        MainApplication.bindKeyGridAdapter.setOnTouchAction(this);
        MainApplication.bindKeyGridAdapter.notifyDataSetChanged();
    }

    public void onUp(View v, int position) {
        v.setBackgroundResource(R.drawable.item_back_shape_b);
        BindBean bindBean = (BindBean) this.beans.get(position);
        this.settingActivity.sendData(OrderManage.bindDownAndUp(bindBean.bind_moduleID, bindBean.keyValue, false));
    }

    public void setSelectTimerBean(TimerBean selectTimerBean) {
        this.selectTimerBean = selectTimerBean;
    }

    public void onResume() {
        super.onResume();
        this.isBack = true;
    }

    public void onKeyDonw(int keyCode, KeyEvent event) {
        if (keyCode == 4 && this.isBack) {
            this.settingActivity.popFragments(BaseActivity.SETTING, R.id.setting_center, true);
        }
    }

    public void onEventMainThread(EasyEventTypeHandler event) {
        if (event.getType() == 6) {
            updateGridView();
        }
    }

    public void longPress(View v, int position) {
        BindBean bindBean = (BindBean) this.beans.get(position);
        this.settingActivity.sendData(OrderManage.bindLongPress(bindBean.bind_moduleID, bindBean.keyValue));
    }
}
