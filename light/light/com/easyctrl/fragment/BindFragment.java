package com.easyctrl.fragment;

import android.app.ProgressDialog;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import com.easyctrl.broadcast.BindBroadCast;
import com.easyctrl.broadcast.UpdateBroadCast;
import com.easyctrl.dialog.BindDialog;
import com.easyctrl.dialog.BindNotSaveDialog;
import com.easyctrl.dialog.EasyProgressDialog;
import com.easyctrl.event.EasyEventTypeHandler;
import com.easyctrl.iface.BaseFragmentImpl;
import com.easyctrl.iface.OnBackDown;
import com.easyctrl.iface.OnBindListenerView;
import com.easyctrl.iface.OnUpdateInterface;
import com.easyctrl.ldy.activity.MainApplication;
import com.easyctrl.ldy.activity.R;
import com.easyctrl.ldy.domain.BindBean;
import com.easyctrl.ldy.domain.ModuleBean;
import com.easyctrl.ldy.domain.ModulePortBean;
import com.easyctrl.ldy.domain.TextDescription;
import com.easyctrl.ldy.util.SaveSingleObject;
import com.easyctrl.ldy.view.SelectPositionPopupWindow;
import com.easyctrl.ldy.view.SelectPositionPopupWindow.OnDefineListener;
import com.easyctrl.manager.OrderManage;
import com.easyctrl.ui.base.BaseActivity;
import com.easyctrl.ui.base.BaseDialog.BaseDialogOnDismissListener;
import de.greenrobot.event.EventBus;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class BindFragment extends BaseFragment implements BaseFragmentImpl, OnDefineListener, OnUpdateInterface, OnBindListenerView, OnBackDown {
    public static final int TIME_PORT_TYPE = 1;
    private static BindBroadCast bindBroadCast;
    private static UpdateBroadCast updateBroadCast;
    private String TAG = BindFragment.class.getSimpleName();
    Button allSelect;
    private ArrayList<ModulePortBean> beans;
    private BindDialog bindDialog;
    public String deviceString;
    private ArrayList<String> devices;
    public String floorString;
    public ArrayList<String> floors;
    private int fragmentType;
    private Handler handler = new Handler(new Callback() {
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    BindFragment.this.refushAdapter();
                    break;
                case 1:
                    EasyProgressDialog.getInstance(BindFragment.this.settingActivity).show(BindFragment.this.progressDialog, BindFragment.this.TAG, BindFragment.this.TAG);
                    break;
                case 2:
                    EasyProgressDialog.getInstance(BindFragment.this.settingActivity).dismiss(BindFragment.this.progressDialog);
                    break;
            }
            return false;
        }
    });
    private boolean isBack = false;
    private boolean isOpenBack = false;
    private BindNotSaveDialog notSaveDialog;
    private SelectPositionPopupWindow popupWindow;
    private ProgressDialog progressDialog;
    public String roomString;
    public ArrayList<String> rooms;
    private boolean selectFlag = false;
    private ArrayList<ModulePortBean> selectbeans;
    private String tempDevice;
    private String tempFloor;
    private String tempRoom;

    public void setFragmentType(int fragmentType) {
        this.fragmentType = fragmentType;
    }

    private void registerUpdateBroad() {
        updateBroadCast = new UpdateBroadCast();
        updateBroadCast.setUpdateInterface(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(UpdateBroadCast.UPDATE_ACTION);
        intentFilter.addAction(UpdateBroadCast.UPDATE_AMING_ACTION);
        getActivity().registerReceiver(updateBroadCast, intentFilter);
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
        EventBus.getDefault().register(this);
        this.isBack = true;
        MainApplication.userManager.setCurrentFragment(this.TAG);
    }

    public void isOpenBackDown(boolean isOpenBack) {
        this.isOpenBack = isOpenBack;
    }

    private void init() {
        this.tempFloor = MainApplication.userManager.getCurrentBindFloor();
        this.tempRoom = MainApplication.userManager.getCurrentBindRoom();
        this.tempDevice = MainApplication.userManager.getCurrentBindDevices();
        this.floors = MainApplication.modulePortManager.findFloor(true, MainApplication.jsonManager.getOnLineModuleID());
        if (this.floors != null) {
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
                boolean isAdd = false;
                i = 0;
                while (i < this.rooms.size()) {
                    if (((String) this.rooms.get(i)).equals("\u5168\u90e8")) {
                        isAdd = false;
                        break;
                    } else {
                        isAdd = true;
                        i++;
                    }
                }
                if (isAdd) {
                    this.rooms.add("\u5168\u90e8");
                }
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
                    MainApplication.modulePortAdapterSimple.setBeans(this.beans);
                    MainApplication.modulePortAdapterSimple.notifyDataSetChanged();
                }
            } else {
                this.beans = MainApplication.modulePortManager.findByFloorAndRoomAndDeivce(this.floorString, this.roomString, this.deviceString);
                MainApplication.modulePortAdapterSimple.setBeans(this.beans);
                MainApplication.modulePortAdapterSimple.notifyDataSetChanged();
            }
            setButtonFloorString(this.floorString);
            setButtonRoomString(this.roomString);
            setButtonDeviceString(this.deviceString);
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFragmentListener(this);
        setDimissBar(false);
        if (this.isOpenBack) {
            this.settingActivity.setOnBackDown(this);
        }
        this.progressDialog = new ProgressDialog(this.settingActivity);
        this.bindDialog = new BindDialog(this.settingActivity);
        this.notSaveDialog = new BindNotSaveDialog(this.settingActivity);
        MainApplication.modulePortAdapterSimple.setActivity(this.settingActivity);
        init();
    }

    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(updateBroadCast);
        getActivity().unregisterReceiver(bindBroadCast);
        MainApplication.userManager.setCurrentBindFloor(this.floorString);
        MainApplication.userManager.setCurrentBindRoom(this.roomString);
        MainApplication.userManager.setCurrentBindDevices(this.deviceString);
        EventBus.getDefault().unregister(this);
        this.isBack = false;
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

    public void onClickOperatorOne(View v) {
        Iterator it = this.beans.iterator();
        while (it.hasNext()) {
            ModulePortBean bean = (ModulePortBean) it.next();
            if (bean.type == 0) {
                try {
                    this.settingActivity.sendData(OrderManage.sendLampOrder(bean, 0));
                    changeState(bean, 1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (bean.type == 1) {
                this.settingActivity.sendData(OrderManage.sendAnmingOrder(bean, 0));
            }
        }
    }

    public void onClickOpreatorTwo(View v) {
        Iterator it = this.beans.iterator();
        while (it.hasNext()) {
            ModulePortBean bean = (ModulePortBean) it.next();
            if (bean.type == 0) {
                try {
                    this.settingActivity.sendData(OrderManage.sendLampOrder(bean, 1));
                    changeState(bean, 0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (bean.type == 1) {
                this.settingActivity.sendData(OrderManage.sendAnmingOrder(bean, 1));
            }
        }
    }

    public void onClickOpreatorThree(View v) {
        Object object = SaveSingleObject.getInstance().getObject();
        if (!(object instanceof ModuleBean)) {
            startMList();
        } else if (object != null) {
            TextDescription description = (TextDescription) SaveSingleObject.getInstance().getTextObject();
            if (description.isListBack) {
                startMList();
                return;
            }
            ArrayList<ModulePortBean> selects = MainApplication.modulePortAdapterSimple.getSelects();
            BindKeyGridView bindKeyGridView = new BindKeyGridView();
            bindKeyGridView.setNum(description.num);
            bindKeyGridView.setName(description.keyNum);
            bindKeyGridView.setModel(description.model);
            bindKeyGridView.setMianban(description.mianban);
            bindKeyGridView.setSelectBeans(createSelectBeans(selects));
            this.settingActivity.pushFragments(BaseActivity.SETTING, bindKeyGridView, true, true, R.id.setting_center);
        }
    }

    private void startMList() {
        BindMList bindMList = new BindMList();
        bindMList.setSelectBeans(createSelectBeans(MainApplication.modulePortAdapterSimple.getSelects()));
        this.settingActivity.pushFragments(BaseActivity.SETTING, bindMList, true, true, R.id.setting_center);
        bindMList.setFragmentType(1);
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
        MainApplication.modulePortAdapterSimple.setSelects(this.selectbeans);
        MainApplication.modulePortAdapterSimple.notifyDataSetChanged();
    }

    public ListAdapter onAdapter() {
        return MainApplication.modulePortAdapterSimple;
    }

    public String onClickOneString() {
        return "\u5168\u5f00";
    }

    public String onClickTwoString() {
        return "\u5168\u5173";
    }

    public String onClickThreeString() {
        return "\u9762\u677f";
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
        if (this.roomString != null && this.roomString.equals("-1")) {
            this.roomString = "\u5168\u90e8";
        }
        setButtonRoomString(this.roomString);
        setButtonDeviceString(this.deviceString);
        this.popupWindow = new SelectPositionPopupWindow(this.settingActivity);
        this.popupWindow.setOnDefineListener(this);
    }

    public void onDefine(View anchor) {
    }

    public void onRefush(String content, View anchor, View view) {
        Button button = (Button) anchor;
        if (button.getId() == R.id.floor) {
            this.floorString = content;
            if (this.floorString.equals("\u8bbe\u5907")) {
                this.rooms = MainApplication.jsonManager.getOnLineModuleID();
                if (this.rooms.size() > 0) {
                    if (!((String) this.rooms.get(this.rooms.size() - 1)).equals("\u5168\u90e8")) {
                        this.rooms.add("\u5168\u90e8");
                    }
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

    private void dataUpdate() {
        if (!this.floorString.equals("\u8bbe\u5907")) {
            this.beans = MainApplication.modulePortManager.findByFloorAndRoomAndDeivce(this.floorString, this.roomString, this.deviceString);
        } else if (!this.roomString.equals("\u7aef\u53e3")) {
            if (this.roomString.equals("\u5168\u90e8")) {
                this.roomString = "-1";
            }
            this.beans = MainApplication.modulePortManager.findByModuleIDAndFloor(Integer.valueOf(this.roomString).intValue(), "\u8bbe\u5907", MainApplication.jsonManager.getOnLineModuleID());
        }
        MainApplication.modulePortAdapterSimple.setBeans(this.beans);
    }

    public void refushAdapter() {
        if (!this.floorString.equals("\u8bbe\u5907")) {
            this.beans = MainApplication.modulePortManager.findByFloorAndRoomAndDeivce(this.floorString, this.roomString, this.deviceString);
        } else if (!this.roomString.equals("\u7aef\u53e3")) {
            if (this.roomString.equals("\u5168\u90e8")) {
                this.roomString = "-1";
            }
            this.beans = MainApplication.modulePortManager.findByModuleIDAndFloor(Integer.valueOf(this.roomString).intValue(), "\u8bbe\u5907", MainApplication.jsonManager.getOnLineModuleID());
        }
        MainApplication.modulePortAdapterSimple.setBeans(this.beans);
        ArrayList<ModulePortBean> selects = MainApplication.modulePortAdapterSimple.getSelects();
        for (int i = 0; i < this.beans.size(); i++) {
            int j = 0;
            while (j < selects.size()) {
                if (((ModulePortBean) selects.get(j)).id == ((ModulePortBean) this.beans.get(i)).id) {
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
        MainApplication.modulePortAdapterSimple.notifyDataSetChanged();
    }

    public void onUpdate(ArrayList<ModulePortBean> arrayList) {
        MainApplication.threadPool.submit(new Runnable() {
            public void run() {
                BindFragment.this.dataUpdate();
                BindFragment.this.handler.post(new Runnable() {
                    public void run() {
                        MainApplication.modulePortAdapterSimple.notifyDataSetChanged();
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
        if (MainApplication.bindManager.findBeanByModuleIdAndKeyIp(bindBean.bind_moduleID, bindBean.keyValue) == null) {
            MainApplication.bindManager.batchSave(bindBean.bind_moduleID);
        }
        BindBean bBean = MainApplication.bindManager.findBeanByModuleIdAndKeyIp(bindBean.bind_moduleID, bindBean.keyValue);
        ArrayList<ModulePortBean> selects = MainApplication.modulePortAdapterSimple.getSelects();
        if (bBean != null && bindBean.bind_stat == 1) {
            if (selects != null && selects.size() != 0) {
                if (bindBean.keyValue <= 8) {
                    if (this.notSaveDialog != null && this.notSaveDialog.isShowing()) {
                        this.notSaveDialog.dismiss();
                    }
                    this.bindDialog.setBindBean(bBean);
                    this.bindDialog.setSelectBeans(createSelectBeans(selects));
                    this.bindDialog.setHandler(this.handler);
                    this.bindDialog.show();
                    this.bindDialog.setOnDismissListener(new BaseDialogOnDismissListener() {
                        public void onDismiss() {
                            BindFragment.this.refushAdapter();
                            if (BindFragment.this.allSelect != null) {
                                BindFragment.this.allSelect.setText("\u5168\u9009");
                                BindFragment.this.selectFlag = false;
                            }
                        }
                    });
                }
            } else {
                return;
            }
        }
        if (bindBean.keyValue > 8 && bindBean.bind_stat == 1) {
            if (this.bindDialog != null && this.bindDialog.isShowing()) {
                this.bindDialog.dismiss();
            }
            this.notSaveDialog.setBindBean(bindBean);
            this.notSaveDialog.setSelectBeans(createSelectBeans(selects));
            this.notSaveDialog.setHandler(this.handler);
            this.notSaveDialog.show();
            this.notSaveDialog.setOnDismissListener(new BaseDialogOnDismissListener() {
                public void onDismiss() {
                    BindFragment.this.refushAdapter();
                    if (BindFragment.this.allSelect != null) {
                        BindFragment.this.allSelect.setText("\u5168\u9009");
                        BindFragment.this.selectFlag = false;
                    }
                }
            });
        }
    }

    public String onTitle() {
        return "\u6309\u952e\u7ed1\u5b9a";
    }

    public void onKeyDonw(int keyCode, KeyEvent event) {
        if (keyCode == 4 && this.isBack) {
            this.settingActivity.popFragments(BaseActivity.SETTING, R.id.setting_center, true);
        }
    }

    public void onEventMainThread(EasyEventTypeHandler event) {
        if (event.getType() == 1) {
            Log.i("data", "fffffffffffff");
        }
    }
}
