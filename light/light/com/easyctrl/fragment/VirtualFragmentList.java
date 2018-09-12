package com.easyctrl.fragment;

import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import com.easyctrl.adapter.ModulePortAdapterSimple;
import com.easyctrl.broadcast.BindBroadCast;
import com.easyctrl.broadcast.UpdateBroadCast;
import com.easyctrl.db.DeviceManager;
import com.easyctrl.dialog.BindDialog;
import com.easyctrl.iface.BaseFragmentImpl;
import com.easyctrl.iface.OnBindListenerView;
import com.easyctrl.iface.OnUpdateInterface;
import com.easyctrl.ldy.activity.MainApplication;
import com.easyctrl.ldy.activity.R;
import com.easyctrl.ldy.activity.SettingActivity;
import com.easyctrl.ldy.domain.BindBean;
import com.easyctrl.ldy.domain.ModulePortBean;
import com.easyctrl.ldy.domain.TextVirtural;
import com.easyctrl.ldy.util.SaveSingleObject;
import com.easyctrl.ldy.view.SelectPositionPopupWindow;
import com.easyctrl.ldy.view.SelectPositionPopupWindow.OnDefineListener;
import com.easyctrl.manager.OrderManage;
import com.easyctrl.ui.base.BaseActivity;
import com.easyctrl.ui.base.BaseDialog.BaseDialogOnDismissListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class VirtualFragmentList extends BaseFragment implements BaseFragmentImpl, OnDefineListener, OnUpdateInterface, OnBindListenerView {
    public static final int UPDATELIST = 1;
    private static BindBroadCast bindBroadCast;
    private static UpdateBroadCast updateBroadCast;
    private String Tag = VirtualFragmentList.class.getSimpleName();
    private ModulePortAdapterSimple adapter;
    Button allSelect;
    private ArrayList<ModulePortBean> beans;
    private BindDialog bindDialog;
    private DeviceManager deviceManager;
    private String deviceString;
    private ArrayList<String> devices;
    private String floorString;
    private ArrayList<String> floors;
    private Handler handler = new Handler(new Callback() {
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    VirtualFragmentList.this.refushAdapter();
                    break;
            }
            return false;
        }
    });
    private SelectPositionPopupWindow popupWindow;
    private String roomString;
    private ArrayList<String> rooms;
    private boolean selectFlag = false;
    private ArrayList<ModulePortBean> selectbeans;
    private String tempDevice;
    private String tempFloor;
    private String tempRoom;

    private void registerUpdateBroad() {
        updateBroadCast = new UpdateBroadCast();
        updateBroadCast.setUpdateInterface(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(UpdateBroadCast.UPDATE_ACTION);
        intentFilter.addAction(UpdateBroadCast.UPDATE_AMING_ACTION);
        getActivity().registerReceiver(updateBroadCast, intentFilter);
    }

    private void changeState(ModulePortBean bean, int isOpen) {
        ArrayList<ModulePortBean> arrys = MainApplication.modulePortAdapterSimple.getSelects();
        if (arrys != null && arrys.size() > 0) {
            for (int i = 0; i < arrys.size(); i++) {
                ModulePortBean temp = (ModulePortBean) arrys.get(i);
                if (bean.id == temp.id) {
                    temp.isOpen = isOpen;
                }
            }
        }
    }

    private void registerBindBroad() {
        bindBroadCast = new BindBroadCast();
        bindBroadCast.setOnBindListenerView(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BindBroadCast.bind_action);
        getActivity().registerReceiver(bindBroadCast, intentFilter);
    }

    public static BindFragment newInstance() {
        return new BindFragment();
    }

    public void onResume() {
        super.onResume();
        registerUpdateBroad();
        registerBindBroad();
        MainApplication.userManager.setCurrentFragment(this.Tag);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFragmentListener(this);
        setTitle("\u865a\u62df\u8bbe\u7f6e");
        setDimissBar(false);
        this.bindDialog = new BindDialog(this.settingActivity);
        this.deviceManager = DeviceManager.getInstance(MainApplication.getAppContext());
        this.popupWindow = new SelectPositionPopupWindow(this.settingActivity);
        this.popupWindow.setOnDefineListener(this);
        init();
    }

    private void init() {
        this.adapter = ModulePortAdapterSimple.getInstance(getActivity(), this.beans);
        this.tempFloor = MainApplication.userManager.getCurrentBindFloor();
        this.tempRoom = MainApplication.userManager.getCurrentBindRoom();
        this.tempDevice = MainApplication.userManager.getCurrentBindDevices();
        this.floors = MainApplication.modulePortManager.findFloor(true, MainApplication.jsonManager.getOnLineModuleID());
        boolean isFlooe = false;
        int i = 0;
        while (i < this.floors.size()) {
            if (((String) this.floors.get(i)).equals("\u8bbe\u5907")) {
                isFlooe = false;
                break;
            } else {
                isFlooe = true;
                i++;
            }
        }
        if (isFlooe) {
            this.floors.add("\u8bbe\u5907");
        }
        this.devices = MainApplication.deviceManager.findAllType();
        this.devices.add("\u5168\u90e8");
        if (this.tempDevice == null) {
            this.deviceString = (String) this.devices.get(0);
            this.tempDevice = this.deviceString;
        } else {
            this.deviceString = this.tempDevice;
        }
        this.selectbeans = new ArrayList();
        if (this.tempFloor != null) {
            this.floorString = this.tempFloor;
        } else if (this.floors != null && this.floors.size() > 0) {
            this.floorString = (String) this.floors.get(0);
            this.tempFloor = this.floorString;
        }
        this.rooms = MainApplication.modulePortManager.findRoomByFloor(this.floorString);
        if (this.tempRoom != null) {
            this.roomString = this.tempRoom;
        } else if (this.rooms != null && this.rooms.size() > 0) {
            this.roomString = (String) this.rooms.get(0);
            this.tempRoom = this.roomString;
        }
        boolean isAll = false;
        i = 0;
        while (i < this.rooms.size()) {
            if (((String) this.rooms.get(i)).equals("\u5168\u90e8")) {
                isAll = false;
                break;
            } else {
                isAll = true;
                i++;
            }
        }
        if (isAll) {
            this.rooms.add("\u5168\u90e8");
        }
        if (this.floorString.equals("\u8bbe\u5907")) {
            this.rooms = MainApplication.jsonManager.getOnLineModuleID();
            if (this.tempRoom != null) {
                this.roomString = this.tempRoom;
            } else if (this.rooms != null && this.rooms.size() > 0) {
                this.roomString = (String) this.rooms.get(0);
                this.tempRoom = this.roomString;
            }
            if (this.roomString.equals("\u7aef\u53e3")) {
                this.roomString = (String) this.rooms.get(0);
                this.tempRoom = this.roomString;
            }
            if (this.roomString != null) {
                if (this.roomString.equals("\u5168\u90e8")) {
                    this.roomString = "-1";
                }
                this.beans = MainApplication.modulePortManager.findByModuleIDAndFloor(Integer.valueOf(this.roomString).intValue(), "\u8bbe\u5907", MainApplication.jsonManager.getOnLineModuleID());
                this.adapter.setBeans(this.beans);
                this.adapter.notifyDataSetChanged();
            }
        } else {
            this.beans = MainApplication.modulePortManager.findByFloorAndRoomAndDeivce(this.floorString, this.roomString, this.deviceString);
            this.adapter.setBeans(this.beans);
            this.adapter.notifyDataSetChanged();
        }
        setButtonFloorString(this.floorString);
        setButtonRoomString(this.roomString);
        setButtonDeviceString(this.deviceString);
    }

    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(updateBroadCast);
        getActivity().unregisterReceiver(bindBroadCast);
        MainApplication.userManager.setCurrentBindFloor(this.floorString);
        MainApplication.userManager.setCurrentBindRoom(this.roomString);
        MainApplication.userManager.setCurrentBindDevices(this.deviceString);
    }

    public void onClickOperatorOne(View v) {
        SettingActivity activity = (SettingActivity) getActivity();
        Iterator it = this.beans.iterator();
        while (it.hasNext()) {
            ModulePortBean bean = (ModulePortBean) it.next();
            if (bean.type == 0) {
                try {
                    activity.sendData(OrderManage.sendLampOrder(bean, 0));
                    changeState(bean, 1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (bean.type == 1) {
                activity.sendData(OrderManage.sendAnmingOrder(bean, 0));
            }
        }
    }

    public void onClickOpreatorTwo(View v) {
        SettingActivity activity = (SettingActivity) getActivity();
        Iterator it = this.beans.iterator();
        while (it.hasNext()) {
            ModulePortBean bean = (ModulePortBean) it.next();
            if (bean.type == 0) {
                try {
                    activity.sendData(OrderManage.sendLampOrder(bean, 1));
                    changeState(bean, 0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (bean.type == 1) {
                activity.sendData(OrderManage.sendAnmingOrder(bean, 1));
            }
        }
    }

    public void onClickOpreatorThree(View v) {
        TextVirtural object = SaveSingleObject.getInstance().getTextObject();
        if (!(object instanceof TextVirtural)) {
            startVirtualList();
        } else if (object == null) {
            startVirtualList();
        } else {
            TextVirtural textVirtural = object;
            if (textVirtural.isListBack) {
                startVirtualList();
                return;
            }
            VirtualKeyGridView gridView = new VirtualKeyGridView();
            gridView.setType(textVirtural.type);
            gridView.setSelectBeans(createSelectBeans(MainApplication.modulePortAdapterSimple.getSelects()));
            gridView.setModel(textVirtural.model);
            gridView.setObject(textVirtural.object);
            this.settingActivity.pushFragments(BaseActivity.SETTING, gridView, true, true, R.id.setting_center);
        }
    }

    private void startVirtualList() {
        ArrayList<ModulePortBean> selects = MainApplication.modulePortAdapterSimple.getSelects();
        VirtualMList virtualMList = new VirtualMList();
        virtualMList.setSelectBeans(createSelectBeans(selects));
        this.settingActivity.pushFragments(BaseActivity.SETTING, virtualMList, true, true, R.id.setting_center);
        virtualMList.setFragmentType(1);
    }

    public void onClickOperatorFour(View v) {
        this.allSelect = (Button) v;
        if (this.selectFlag) {
            this.allSelect.setText("\u5168\u9009");
            this.selectFlag = false;
            this.selectbeans.clear();
        } else {
            this.allSelect.setText("\u6e05\u7a7a");
            for (int i = 0; i < this.beans.size(); i++) {
                ModulePortBean temp = (ModulePortBean) this.beans.get(i);
                if (!this.selectbeans.contains(temp)) {
                    this.selectbeans.add(temp);
                }
            }
            this.selectFlag = true;
        }
        this.adapter.setSelects(this.selectbeans);
        this.adapter.notifyDataSetChanged();
    }

    public ListAdapter onAdapter() {
        return this.adapter;
    }

    public String onClickOneString() {
        return "\u5168\u5f00";
    }

    public String onClickTwoString() {
        return "\u5168\u5173";
    }

    public String onClickThreeString() {
        return "\u865a\u62df";
    }

    public String onClickFourString() {
        return "\u5168\u9009";
    }

    public void onClickFloor(View v) {
        this.popupWindow.showAsAnchor(v, this.floors);
    }

    public void onClickRoom(View v) {
        this.popupWindow.showAsAnchor(v, this.rooms);
    }

    public void onClickDevice(View v) {
        if (this.floorString.equals("\u8bbe\u5907")) {
            setButtonDeviceString("\u5168\u90e8");
        } else {
            this.popupWindow.showAsAnchor(v, this.devices);
        }
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setButtonFloorString(this.floorString);
        if (this.roomString.equals("-1")) {
            this.roomString = "\u5168\u90e8";
        }
        setButtonRoomString(this.roomString);
        setButtonDeviceString(this.deviceString);
    }

    public void onDefine(View anchor) {
    }

    public void onRefush(String content, View anchor, View view) {
        Button button = (Button) anchor;
        if (button.getId() == R.id.floor) {
            this.floorString = content;
            if (this.floorString.equals("\u8bbe\u5907")) {
                this.rooms = MainApplication.modulePortManager.findAllModuleID();
                if (this.rooms.size() > 0) {
                    this.roomString = (String) this.rooms.get(0);
                    setButtonRoomString(this.roomString);
                    refushAdapter();
                    return;
                }
                return;
            }
            this.rooms = MainApplication.modulePortManager.findRoomByFloor(this.floorString);
            this.rooms.add("\u5168\u90e8");
            if (this.rooms.size() > 0) {
                this.roomString = (String) this.rooms.get(0);
                setButtonRoomString(this.roomString);
                refushAdapter();
            }
        } else if (button.getId() == R.id.room) {
            this.roomString = content;
            if (this.floorString.equals("\u8bbe\u5907")) {
                MainApplication.userManager.setLastSelectID(this.roomString, MainApplication.userManager.currentHost());
                if (this.roomString.equals("\u5168\u90e8")) {
                    this.roomString = "-1";
                }
                this.beans = MainApplication.modulePortManager.findByModuleIDAndFloor(Integer.valueOf(this.roomString).intValue(), "\u8bbe\u5907", MainApplication.jsonManager.getOnLineModuleID());
                refushAdapter();
                return;
            }
            refushAdapter();
        } else if (button.getId() == R.id.module) {
            this.deviceString = content;
            setButtonDeviceString(this.deviceString);
            refushAdapter();
        }
    }

    private void refushAdapter() {
        if (!this.floorString.equals("\u8bbe\u5907")) {
            this.beans = MainApplication.modulePortManager.findByFloorAndRoomAndDeivce(this.floorString, this.roomString, this.deviceString);
        } else if (!this.roomString.equals("\u7aef\u53e3")) {
            if (this.roomString.equals("\u5168\u90e8")) {
                this.roomString = "-1";
            }
            this.beans = MainApplication.modulePortManager.findByModuleIDAndFloor(Integer.valueOf(this.roomString).intValue(), "\u8bbe\u5907", MainApplication.jsonManager.getOnLineModuleID());
        }
        for (int i = 0; i < this.beans.size(); i++) {
            int j = 0;
            while (j < this.adapter.getSelects().size()) {
                if (((ModulePortBean) this.adapter.getSelects().get(j)).id == ((ModulePortBean) this.beans.get(i)).id) {
                    if (this.allSelect != null) {
                        this.allSelect.setText("\u6e05\u7a7a");
                        this.selectFlag = true;
                    }
                } else {
                    if (this.allSelect != null) {
                        this.allSelect.setText("\u5168\u9009");
                        this.selectFlag = false;
                    }
                    j++;
                }
            }
        }
        this.adapter.setBeans(this.beans);
        this.adapter.notifyDataSetChanged();
    }

    private void dataUpdate() {
        if (!this.floorString.equals("\u8bbe\u5907")) {
            this.beans = MainApplication.modulePortManager.findByFloorAndRoomAndDeivce(this.floorString, this.roomString, this.deviceString);
        } else if (!this.roomString.equals("\u7aef\u53e3")) {
            if (this.roomString.equals("\u5168\u90e8")) {
                this.roomString = "-1";
            }
            this.beans = MainApplication.modulePortManager.findByModuleIDAndFloor(Integer.valueOf(this.roomString).intValue(), "\u8bbe\u5907", MainApplication.jsonManager.getOnLineModuleID());
        }
        this.adapter.setBeans(this.beans);
    }

    public void onUpdate(ArrayList<ModulePortBean> arrayList) {
        MainApplication.threadPool.submit(new Runnable() {
            public void run() {
                VirtualFragmentList.this.dataUpdate();
                VirtualFragmentList.this.handler.post(new Runnable() {
                    public void run() {
                        VirtualFragmentList.this.adapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }

    private HashMap<Integer, ModulePortBean> listToMap(ArrayList<ModulePortBean> array) {
        HashMap<Integer, ModulePortBean> map = new HashMap();
        for (int i = 0; i < array.size(); i++) {
            ModulePortBean bean = (ModulePortBean) array.get(i);
            map.put(Integer.valueOf(bean.id), bean);
        }
        return map;
    }

    private ArrayList<ModulePortBean> createSelectBeans(ArrayList<ModulePortBean> selects) {
        HashMap<Integer, ModulePortBean> hashMap = listToMap(MainApplication.modulePortManager.findAll());
        ArrayList<ModulePortBean> arrayList = new ArrayList();
        for (int i = 0; i < selects.size(); i++) {
            ModulePortBean tempBean = (ModulePortBean) hashMap.get(Integer.valueOf(((ModulePortBean) selects.get(i)).id));
            if (tempBean != null) {
                arrayList.add(tempBean);
            } else {
                arrayList.add((ModulePortBean) selects.get(i));
            }
        }
        return arrayList;
    }

    public void onBindShowDialog(BindBean bindBean) {
        BindBean bBean = MainApplication.bindManager.findBeanByModuleIdAndKeyIp(bindBean.bind_moduleID, bindBean.keyValue);
        if (bBean != null && bindBean.bind_stat == 1) {
            this.bindDialog.setBindBean(bBean);
            ArrayList<ModulePortBean> selects = MainApplication.modulePortAdapterSimple.getSelects();
            if (selects != null && selects.size() > 0) {
                this.bindDialog.setSelectBeans(createSelectBeans(selects));
                this.bindDialog.setHandler(this.handler);
                this.bindDialog.show();
                this.bindDialog.setOnDismissListener(new BaseDialogOnDismissListener() {
                    public void onDismiss() {
                        VirtualFragmentList.this.refushAdapter();
                        if (VirtualFragmentList.this.allSelect != null) {
                            VirtualFragmentList.this.allSelect.setText("\u5168\u9009");
                            VirtualFragmentList.this.selectFlag = false;
                        }
                    }
                });
            }
        }
    }

    public String onTitle() {
        return "\u865a\u62df\u8bbe\u7f6e";
    }
}
