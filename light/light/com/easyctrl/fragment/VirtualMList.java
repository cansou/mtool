package com.easyctrl.fragment;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import com.easyctrl.adapter.VirtualMLIstAdapter;
import com.easyctrl.iface.OnBackDown;
import com.easyctrl.iface.OnBaseFragmentList;
import com.easyctrl.ldy.activity.MainApplication;
import com.easyctrl.ldy.activity.R;
import com.easyctrl.ldy.domain.ModulePortBean;
import com.easyctrl.ldy.domain.TextVirtural;
import com.easyctrl.ldy.domain.TimerBean;
import com.easyctrl.ldy.util.SaveSingleObject;
import com.easyctrl.ui.base.BaseActivity;
import java.util.ArrayList;

public class VirtualMList extends BaseFragmentListview implements OnBaseFragmentList, OnBackDown {
    public static final int USERTYPE = 2;
    public static final int VIRTUALTYPE = 1;
    private String Tag = VirtualMList.class.getSimpleName();
    private VirtualMLIstAdapter adapter;
    private ArrayList<String> beans;
    private int fragmentType;
    private boolean isBack = false;
    private Object object;
    private ArrayList<TimerBean> selectTimer;
    private ArrayList<ModulePortBean> selects;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setOnBaseFragmentList(this);
        this.activity.setOnBackDown(this);
        MainApplication.virtualManager.batchSave();
        this.beans = MainApplication.virtualManager.findArrayType();
        this.adapter = VirtualMLIstAdapter.getAdapter(getActivity(), this.beans);
    }

    public void setFragmentType(int fragmentType) {
        this.fragmentType = fragmentType;
    }

    public ListAdapter onAdapter() {
        return this.adapter;
    }

    public void onItemListviewClick(AdapterView<?> adapterView, View view, int position, long id) {
        TextVirtural textVirtural = new TextVirtural();
        textVirtural.beans = MainApplication.modulePortAdapterSimple.getSelects();
        String type = (String) this.beans.get(position);
        VirtualKeyGridView gridView = new VirtualKeyGridView();
        int startkey = Integer.valueOf(type).intValue();
        gridView.setType(startkey);
        textVirtural.type = startkey;
        gridView.setSelectBeans(MainApplication.modulePortAdapterSimple.getSelects());
        textVirtural.beans = this.selects;
        int start = (startkey * 10) + 1;
        String result = String.valueOf(start) + " ~ " + String.valueOf(start + 9);
        gridView.setModel(result);
        textVirtural.model = result;
        gridView.setObject(this.object);
        textVirtural.object = this.object;
        if (this.selectTimer != null && this.selectTimer.size() > 0) {
            gridView.setSelectTimerBean((TimerBean) this.selectTimer.get(0));
        }
        textVirtural.selectTimer = this.selectTimer;
        SaveSingleObject.getInstance().setTextObject(textVirtural);
        this.activity.pushFragments(BaseActivity.SETTING, gridView, true, false, R.id.setting_center);
    }

    public void onClick(View v) {
        TextVirtural obj = SaveSingleObject.getInstance().getTextObject();
        if (obj instanceof TextVirtural) {
            TextVirtural textVirtural = obj;
            if (textVirtural != null) {
                textVirtural.isListBack = true;
            }
        }
        this.activity.popFragments(BaseActivity.SETTING, R.id.setting_center, true);
    }

    public void setSelectBeans(ArrayList<ModulePortBean> selects) {
        this.selects = selects;
    }

    public void setSelectTimer(ArrayList<TimerBean> selects) {
        this.selectTimer = selects;
    }

    public void onResume() {
        super.onResume();
        this.isBack = true;
        MainApplication.userManager.setCurrentFragment(this.Tag);
    }

    public void onPause() {
        super.onPause();
        this.isBack = false;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public void onKeyDonw(int keyCode, KeyEvent event) {
        if (keyCode == 4 && this.isBack) {
            this.activity.popFragments(BaseActivity.SETTING, R.id.setting_center, true);
        }
    }
}
