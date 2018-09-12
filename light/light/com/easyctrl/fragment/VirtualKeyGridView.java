package com.easyctrl.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.Toast;
import com.easyctrl.dialog.EasyProgressDialog;
import com.easyctrl.dialog.SceneBindDialog;
import com.easyctrl.dialog.TimerBindDialog;
import com.easyctrl.dialog.VirtualDialog;
import com.easyctrl.iface.OnBackDown;
import com.easyctrl.iface.OnBaseFragmentList;
import com.easyctrl.iface.OnTouchAction;
import com.easyctrl.ldy.activity.MainApplication;
import com.easyctrl.ldy.activity.R;
import com.easyctrl.ldy.domain.ModulePortBean;
import com.easyctrl.ldy.domain.TextVirtural;
import com.easyctrl.ldy.domain.TimerBean;
import com.easyctrl.ldy.domain.UserScene;
import com.easyctrl.ldy.domain.VirtualBean;
import com.easyctrl.ldy.util.SaveSingleObject;
import com.easyctrl.manager.OrderManage;
import com.easyctrl.ui.base.BaseActivity;
import com.easyctrl.ui.base.BaseDialog.BaseDialogOnDismissListener;
import java.util.ArrayList;

public class VirtualKeyGridView extends BaseFragmentGrid implements OnBaseFragmentList, OnTouchAction, OnBackDown {
    private String TAG = VirtualKeyGridView.class.getSimpleName();
    private ArrayList<VirtualBean> beans;
    private EasyProgressDialog easyProgressDialog;
    private Handler handler = new Handler(new Callback() {
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    VirtualKeyGridView.this.easyProgressDialog.show(VirtualKeyGridView.this.mProgressDialog, "\u6b63\u5728\u4e0a\u4f20\u6570\u636e......", VirtualKeyGridView.this.TAG);
                    break;
                case 2:
                    VirtualKeyGridView.this.easyProgressDialog.dismiss(VirtualKeyGridView.this.mProgressDialog);
                    break;
            }
            return false;
        }
    });
    private boolean isBack = false;
    private ProgressDialog mProgressDialog;
    private Object object;
    private SceneBindDialog sceneBindDialog;
    private TimerBean selectTimerBean;
    private ArrayList<ModulePortBean> selects;
    private TimerBindDialog timerBindDialog;
    private int type;
    private VirtualDialog virtualDialog;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setOnBaseFragmentList(this);
        this.settingActivity.setOnBackDown(this);
        TextVirtural textVirtural = (TextVirtural) SaveSingleObject.getInstance().getTextObject();
        if (textVirtural != null) {
            this.type = textVirtural.type;
        }
        this.beans = MainApplication.virtualManager.findByType(this.type);
        MainApplication.virtualGridAdapter.setBeans(this.beans);
        MainApplication.virtualGridAdapter.notifyDataSetChanged();
        MainApplication.virtualGridAdapter.setOnTouchAction(this);
        this.timerBindDialog = new TimerBindDialog(this.settingActivity);
        this.virtualDialog = new VirtualDialog(this.settingActivity);
        this.virtualDialog.setStartType(1);
        this.virtualDialog.setHandler(this.handler);
        this.sceneBindDialog = new SceneBindDialog(this.settingActivity);
        this.mProgressDialog = new ProgressDialog(this.settingActivity);
        this.easyProgressDialog = EasyProgressDialog.getInstance(this.settingActivity);
    }

    public void onPause() {
        super.onPause();
        this.isBack = false;
    }

    public void onClick(View v) {
        if (v.getId() == R.id.leftButton) {
            this.settingActivity.popFragments(BaseActivity.SETTING, R.id.setting_center, true);
            this.settingActivity.popFragments(BaseActivity.SETTING, R.id.setting_center, true);
            ((TextVirtural) SaveSingleObject.getInstance().getTextObject()).isListBack = false;
        } else if (v.getId() == R.id.titleView) {
            this.settingActivity.pushFragments(BaseActivity.SETTING, new VirtualMList(), true, false, R.id.setting_center);
        }
    }

    public void onDown(View v, int position) {
        VirtualBean virtualBean = (VirtualBean) this.beans.get(position);
        if (this.object != null) {
            UserScene userScene = this.object;
            this.sceneBindDialog.setObject(virtualBean);
            this.sceneBindDialog.setUserScene(userScene);
            this.sceneBindDialog.show();
            this.object = null;
        } else if (this.selectTimerBean == null || MainApplication.timerAdapter.getSelects().size() <= 0) {
            v.setBackgroundResource(R.drawable.item_back_shape);
            if (this.selects == null || this.selects.size() <= 0) {
                this.settingActivity.sendData(OrderManage.virtualDownAndUp(virtualBean.key, true));
                return;
            }
            this.virtualDialog.setVirtualBean(virtualBean);
            this.virtualDialog.setSelectBeans(this.selects);
            this.virtualDialog.show();
            this.virtualDialog.setOnDismissListener(new BaseDialogOnDismissListener() {
                public void onDismiss() {
                    VirtualKeyGridView.this.beans = MainApplication.virtualManager.findByType(VirtualKeyGridView.this.type);
                    MainApplication.virtualGridAdapter.setBeans(VirtualKeyGridView.this.beans);
                    MainApplication.virtualGridAdapter.notifyDataSetChanged();
                }
            });
        } else if (virtualBean.floor == null) {
            Toast.makeText(this.settingActivity, "\u76ee\u524d\u8be5\u6309\u952e\u6ca1\u6709\u7ed1\u5b9a", 1).show();
        } else {
            this.timerBindDialog.setTimerBean((TimerBean) MainApplication.timerAdapter.getSelects().get(0));
            this.timerBindDialog.setObject(virtualBean);
            this.timerBindDialog.show();
        }
    }

    public void setSelectBeans(ArrayList<ModulePortBean> selects) {
        this.selects = selects;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public void onUp(View v, int position) {
        v.setBackgroundResource(R.drawable.item_back_shape_b);
        this.settingActivity.sendData(OrderManage.virtualDownAndUp(((VirtualBean) this.beans.get(position)).key, false));
    }

    public ListAdapter onAdapter() {
        return MainApplication.virtualGridAdapter;
    }

    public void onItemListviewClick(AdapterView<?> adapterView, View view, int position, long id) {
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setSelectTimerBean(TimerBean timerBean) {
        this.selectTimerBean = timerBean;
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

    public void longPress(View v, int position) {
    }
}
