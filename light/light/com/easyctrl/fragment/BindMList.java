package com.easyctrl.fragment;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.TextView;
import com.easyctrl.iface.OnBackDown;
import com.easyctrl.iface.OnBaseFragmentList;
import com.easyctrl.ldy.activity.MainApplication;
import com.easyctrl.ldy.activity.R;
import com.easyctrl.ldy.domain.ModuleBean;
import com.easyctrl.ldy.domain.ModulePortBean;
import com.easyctrl.ldy.domain.TextDescription;
import com.easyctrl.ldy.domain.TimerBean;
import com.easyctrl.ldy.util.SaveSingleObject;
import com.easyctrl.ui.base.BaseActivity;
import java.util.ArrayList;

public class BindMList extends BaseFragmentListview implements OnBaseFragmentList, OnBackDown {
    public static final int TIMETYPE = 1;
    private String Tag = BindMList.class.getSimpleName();
    private ArrayList<ModuleBean> beans;
    private int fragmentType;
    private boolean isBack = false;
    private Object object;
    private ArrayList<TimerBean> selectTimer;
    private ArrayList<ModulePortBean> selects;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setOnBaseFragmentList(this);
        this.activity.setOnBackDown(this);
        this.beans = MainApplication.moduleManager.findByType();
        MainApplication.bindMListAdapter.setModuls(this.beans);
        this.isBack = true;
    }

    public void setFragmentType(int fragmentType) {
        this.fragmentType = fragmentType;
    }

    public ListAdapter onAdapter() {
        return MainApplication.bindMListAdapter;
    }

    public void onItemListviewClick(AdapterView<?> adapterView, View view, int position, long id) {
        TextView num = (TextView) view.findViewById(R.id.num);
        TextView mianban = (TextView) view.findViewById(R.id.mianban);
        TextView model = (TextView) view.findViewById(R.id.model);
        TextView keyNum = (TextView) view.findViewById(R.id.keyNum);
        TextDescription description = new TextDescription();
        description.num = num.getText().toString();
        description.mianban = mianban.getText().toString();
        description.model = model.getText().toString();
        description.keyNum = keyNum.getText().toString();
        ModuleBean module = (ModuleBean) this.beans.get(position);
        SaveSingleObject.getInstance().setObject(module);
        SaveSingleObject.getInstance().setTextObject(description);
        BindKeyGridView bindKeyGridView = new BindKeyGridView();
        bindKeyGridView.setModuleID(module);
        bindKeyGridView.setNum(description.num);
        bindKeyGridView.setName(description.keyNum);
        bindKeyGridView.setModel(description.model);
        bindKeyGridView.setMianban(description.mianban);
        bindKeyGridView.setSelectBeans(MainApplication.modulePortAdapterSimple.getSelects());
        bindKeyGridView.setObject(this.object);
        if (this.selectTimer != null && this.selectTimer.size() > 0) {
            bindKeyGridView.setSelectTimerBean((TimerBean) this.selectTimer.get(0));
        }
        this.activity.pushFragments(BaseActivity.SETTING, bindKeyGridView, true, false, R.id.setting_center);
    }

    public void onClick(View v) {
        try {
            TextDescription description = (TextDescription) SaveSingleObject.getInstance().getTextObject();
            if (description != null) {
                description.isListBack = true;
            }
            this.activity.popFragments(BaseActivity.SETTING, R.id.setting_center, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setSelectBeans(ArrayList<ModulePortBean> selects) {
        this.selects = selects;
    }

    public void setSelectTimer(ArrayList<TimerBean> selectTimer) {
        this.selectTimer = selectTimer;
    }

    public void onPause() {
        super.onPause();
        this.isBack = false;
    }

    public void onResume() {
        super.onResume();
        this.isBack = true;
        MainApplication.userManager.setCurrentFragment(this.Tag);
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public Object getObject() {
        return this.object;
    }

    public void onKeyDonw(int keyCode, KeyEvent event) {
        if (keyCode == 4 && this.isBack) {
            this.activity.popFragments(BaseActivity.SETTING, R.id.setting_center, true);
        }
    }
}
