package com.easyctrl.fragment;

import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.easyctrl.broadcast.UpdateBroadCast;
import com.easyctrl.db.FloorManager;
import com.easyctrl.db.RoomManager;
import com.easyctrl.dialog.DevicePopupWindow;
import com.easyctrl.dialog.DevicePopupWindow.OnListItemListener;
import com.easyctrl.iface.OnBackDown;
import com.easyctrl.iface.OnUpdateInterface;
import com.easyctrl.ldy.activity.MainApplication;
import com.easyctrl.ldy.activity.R;
import com.easyctrl.ldy.activity.SettingActivity;
import com.easyctrl.ldy.domain.FloorBean;
import com.easyctrl.ldy.domain.ModuleBean;
import com.easyctrl.ldy.domain.ModulePortBean;
import com.easyctrl.ldy.domain.RoomBean;
import com.easyctrl.ldy.util.Util;
import com.easyctrl.ldy.view.SelectPositionPopupWindow;
import com.easyctrl.ldy.view.SelectPositionPopupWindow.OnDefineListener;
import com.easyctrl.ui.base.BaseActivity;
import com.easyctrl.ui.base.BaseEasyCtrlFragment;
import java.util.ArrayList;
import java.util.Iterator;

public class ModulePortListFragment extends BaseEasyCtrlFragment implements OnClickListener, OnUpdateInterface, OnBackDown {
    private static final int BROADCAST = 3;
    private static final int SHOWWINDOW = 2;
    private static final int UPDATELISTVIEW = 1;
    private static ArrayList<ModulePortBean> beans = null;
    private static UpdateBroadCast updateBroadCast;
    private TextView device;
    private Button floor;
    private FloorManager floorManager;
    private ArrayList<String> floors;
    private Handler handler = new Handler(new Callback() {
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    ModulePortListFragment.this.refurbish();
                    break;
                case 5:
                    ModulePortBean moduleBean = (ModulePortBean) msg.getData().getSerializable("lampBean");
                    MainApplication.modulePortManager.update(moduleBean, 0);
                    ModulePortListFragment.beans = MainApplication.modulePortManager.findByModuleID(moduleBean.moduleID);
                    break;
                case 6:
                    ModulePortListFragment.beans = MainApplication.modulePortManager.findByModuleID(((ModulePortBean) msg.getData().getSerializable("amingBean")).moduleID);
                    break;
            }
            return false;
        }
    });
    private boolean isBack = false;
    public boolean isUpdate = false;
    private ListView listView;
    private TextView model;
    private Button module;
    private ModuleBean moduleBean;
    private int moudleID;
    private TextView name;
    private TextView num;
    private DevicePopupWindow popupWindow;
    private int position;
    private SelectPositionPopupWindow positionPopupWindow;
    private Button room;
    private RoomManager roomManager;
    private ArrayList<String> rooms;
    private SettingActivity settingActivity;
    private int type;

    public static ModulePortListFragment newInstance() {
        return new ModulePortListFragment();
    }

    private void refurbish() {
        if (this.moudleID != 0) {
            beans = MainApplication.modulePortManager.findByModuleID(this.moudleID);
            MainApplication.modulePortAdapter.setBeans(beans);
            MainApplication.modulePortAdapter.notifyDataSetChanged();
        }
    }

    public void onResume() {
        super.onResume();
        regiest();
        this.isBack = true;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onCreateInit();
    }

    private void regiest() {
        updateBroadCast = new UpdateBroadCast();
        updateBroadCast.setUpdateInterface(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(UpdateBroadCast.UPDATE_ACTION);
        intentFilter.addAction(UpdateBroadCast.UPDATE_AMING_ACTION);
        getActivity().registerReceiver(updateBroadCast, intentFilter);
    }

    public void onPause() {
        super.onPause();
        this.settingActivity.unregisterReceiver(updateBroadCast);
        this.isBack = false;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.module_port_layout, null);
        findViewByID(view);
        initWidget(view);
        return view;
    }

    private void initPosition() {
        if (this.floors == null || this.floors.size() == 0) {
            this.floor.setText("\u8bbe\u5907");
            this.floors.add("\u81ea\u5b9a\u4e49");
            this.floors.add("\u8bbe\u5907");
            this.room.setText("\u7aef\u53e3");
            return;
        }
        FloorBean floorBean = this.floorManager.findByName(MainApplication.userManager.getCurrentFloor());
        if (MainApplication.userManager.getCurrentFloor() == null || floorBean == null) {
            setCurrentFloor((String) this.floors.get(0));
        } else {
            setCurrentFloor(MainApplication.userManager.getCurrentFloor());
        }
    }

    private void setCurrentFloor(String current) {
        this.floor.setText(current);
        this.rooms = this.roomManager.findByFloorIDAndip(this.floorManager.findByName(current).id);
        if (this.rooms != null && this.rooms.size() > 0) {
            this.room.setText((CharSequence) this.rooms.get(0));
        }
        if (MainApplication.userManager.getCurrentRoom() != null) {
            this.room.setText(MainApplication.userManager.getCurrentRoom());
        }
    }

    private void deleteAdapteSelect() {
        ArrayList<ModulePortBean> beans = MainApplication.modulePortAdapter.getSelects();
        Iterator<ModulePortBean> it = beans.iterator();
        while (it.hasNext()) {
            ModulePortBean bean = (ModulePortBean) it.next();
            it.remove();
            beans.remove(bean);
        }
    }

    private void backModuleFragment() {
        deleteAdapteSelect();
        this.settingActivity.popFragments(BaseActivity.SETTING, R.id.modulelayout, false);
    }

    public void setTitle(ModuleBean moduleBean, int position) {
        this.moduleBean = moduleBean;
        this.position = position;
    }

    private void initPopupDialog() {
        this.positionPopupWindow = new SelectPositionPopupWindow(this.settingActivity);
        this.positionPopupWindow.setOnDefineListener(new OnDefineListener() {
            public void onDefine(View anchor) {
                Button button = (Button) anchor;
                if (button.getId() == R.id.floor) {
                    FloorBean bean = ModulePortListFragment.this.floorManager.findByName(button.getText().toString());
                    if (bean != null) {
                        MainApplication.userManager.setCurrentFloor(bean.id);
                    }
                    FloorRoomFragment ff = new FloorViewModel(ModulePortListFragment.this.settingActivity).newInstance();
                    ff.setCurintSelectModuleId(ModulePortListFragment.this.moduleBean.moduleId);
                    ff.setTitle(ModulePortListFragment.this.moduleBean, ModulePortListFragment.this.position);
                    ff.setRightButton(8);
                    ModulePortListFragment.this.settingActivity.pushFragments(BaseActivity.SETTING, ff, true, true, R.id.modulelayout);
                } else if (button.getId() == R.id.room) {
                    FloorRoomFragment rf = new RoomViewModel(ModulePortListFragment.this.getActivity()).newInstance();
                    rf.setCurintSelectModuleId(ModulePortListFragment.this.moduleBean.moduleId);
                    rf.setTitle(ModulePortListFragment.this.moduleBean, ModulePortListFragment.this.position);
                    rf.setCurrentFloor(ModulePortListFragment.this.floorManager.findByName(ModulePortListFragment.this.floor.getText().toString()));
                    rf.setRightButton(0);
                    ModulePortListFragment.this.settingActivity.pushFragments(BaseActivity.SETTING, rf, true, true, R.id.modulelayout);
                    MainApplication.userManager.setCurrentRoom(ModulePortListFragment.this.roomManager.findByName(button.getText().toString(), ModulePortListFragment.this.floorManager.findByName(ModulePortListFragment.this.floor.getText().toString()).id).id);
                } else {
                    button.getId();
                }
            }

            public void onRefush(final String recontent, View anchor, View v) {
                Button button = (Button) anchor;
                if (button.getId() == R.id.floor) {
                    if (recontent.equals("\u8bbe\u5907")) {
                        ModulePortListFragment.this.room.setText("\u7aef\u53e3");
                        ModulePortListFragment.this.room.setEnabled(false);
                        return;
                    }
                    MainApplication.userManager.setCurrentFloor(ModulePortListFragment.this.floorManager.findByName(recontent).id);
                    ModulePortListFragment.this.room.setEnabled(true);
                    int floorID = ModulePortListFragment.this.floorManager.findByName(recontent).id;
                    ModulePortListFragment.this.rooms = ModulePortListFragment.this.roomManager.findByFloorIDAndip(floorID);
                    if (ModulePortListFragment.this.rooms == null || ModulePortListFragment.this.rooms.size() == 0) {
                        RoomBean t = new RoomBean();
                        t.name = "\u623f\u95f4 1";
                        t.sname = "\u623f\u95f4";
                        t.paixu = 1;
                        t.floorID = floorID;
                        ModulePortListFragment.this.roomManager.add(t);
                    }
                    ModulePortListFragment.this.rooms = ModulePortListFragment.this.roomManager.findByFloorIDAndip(floorID);
                    if (ModulePortListFragment.this.rooms != null && ModulePortListFragment.this.rooms.size() > 0) {
                        ModulePortListFragment.this.room.setText((CharSequence) ModulePortListFragment.this.rooms.get(0));
                    }
                } else if (button.getId() == R.id.room) {
                    MainApplication.userManager.setCurrentRoom(ModulePortListFragment.this.roomManager.findByName(button.getText().toString(), ModulePortListFragment.this.floorManager.findByName(ModulePortListFragment.this.floor.getText().toString()).id).id);
                } else if (button.getId() == R.id.module) {
                    WindowManager wm = (WindowManager) ModulePortListFragment.this.getActivity().getSystemService("window");
                    ModulePortListFragment.this.popupWindow.setContentValue(MainApplication.deviceManager.findByType(recontent));
                    ModulePortListFragment.this.popupWindow.setSelectWindow(ModulePortListFragment.this.positionPopupWindow);
                    v.getLocationInWindow(new int[2]);
                    v.getLocationOnScreen(new int[2]);
                    ModulePortListFragment.this.popupWindow.showAsAnchor(anchor, Util.dip2px(ModulePortListFragment.this.settingActivity, (float) Integer.parseInt(ModulePortListFragment.this.settingActivity.getResources().getString(R.string.device_level_two_weightf))), 0);
                    ModulePortListFragment.this.popupWindow.setOnListItemListener(new OnListItemListener() {
                        public void onItem(String content) {
                            if (content.equals("\u81ea\u5b9a\u4e49")) {
                                ModulePortListFragment.this.popupWindow.dimiss();
                                FloorRoomFragment df = new DeviceViewModel(ModulePortListFragment.this.getActivity()).newInstance();
                                df.setCurintSelectModuleId(ModulePortListFragment.this.moduleBean.moduleId);
                                df.setTitle(ModulePortListFragment.this.moduleBean, ModulePortListFragment.this.position);
                                df.setType(recontent);
                                ModulePortListFragment.this.settingActivity.pushFragments(BaseActivity.SETTING, df, true, true, R.id.modulelayout);
                                ModulePortListFragment.this.popupWindow.dimiss();
                                return;
                            }
                            ModulePortListFragment.this.module.setText(content);
                        }
                    });
                }
            }
        });
    }

    public void onClick(View v) {
        if (v.getId() == R.id.floor) {
            MainApplication.modulePortAdapter.setFloor((Button) v);
            this.positionPopupWindow.setListenerFrooRoom(false);
            if (!((String) this.floors.get(this.floors.size() - 1)).equals("\u8bbe\u5907")) {
                this.floors.add("\u81ea\u5b9a\u4e49");
                this.floors.add("\u8bbe\u5907");
            }
            this.positionPopupWindow.show(v, this.floors);
        } else if (v.getId() == R.id.room) {
            Button room = (Button) v;
            if (!room.getText().toString().equals("\u7aef\u53e3")) {
                MainApplication.modulePortAdapter.setRoom(room);
                this.positionPopupWindow.setListenerFrooRoom(false);
                if (this.rooms.size() > 0 && !((String) this.rooms.get(this.rooms.size() - 1)).equals("\u81ea\u5b9a\u4e49")) {
                    this.rooms.add("\u81ea\u5b9a\u4e49");
                }
                this.positionPopupWindow.show(v, this.rooms);
            }
        } else if (v.getId() == R.id.module) {
            MainApplication.modulePortAdapter.setDeivce((Button) v);
            this.positionPopupWindow.setListenerFrooRoom(true);
            this.positionPopupWindow.showAsDevice(v, MainApplication.deviceManager.findAllType());
        } else if (v.getId() == R.id.title) {
            backModuleFragment();
            FloorBean bean = this.floorManager.findByName(this.floor.getText().toString());
            if (bean != null) {
                MainApplication.userManager.setCurrentFloor(bean.id);
                FloorBean floorBean = this.floorManager.findByName(this.floor.getText().toString());
                if (floorBean != null) {
                    MainApplication.userManager.setCurrentRoom(this.roomManager.findByName(this.room.getText().toString(), floorBean.id).id);
                }
            }
        }
    }

    public void onUpdate(ArrayList<ModulePortBean> bean) {
        if (bean.size() > 0 && ((ModulePortBean) bean.get(0)).moduleID == this.moudleID) {
            beans = bean;
            this.handler.sendEmptyMessage(1);
        }
    }

    public void setModuleID(int moduleId, int type) {
        this.moudleID = moduleId;
        this.type = type;
    }

    public void setBeans(ArrayList<ModulePortBean> beans) {
        beans = beans;
    }

    public void onKeyDonw(int keyCode, KeyEvent event) {
        if (keyCode == 4 && this.isBack) {
            backModuleFragment();
            MainApplication.userManager.setCurrentFragment("ModuleListFragment");
        }
    }

    protected void findViewByID(View view) {
        view.findViewById(R.id.title).setOnClickListener(this);
        this.listView = (ListView) view.findViewById(R.id.listview);
        this.num = (TextView) view.findViewById(R.id.num);
        this.device = (TextView) view.findViewById(R.id.device);
        this.model = (TextView) view.findViewById(R.id.model);
        this.name = (TextView) view.findViewById(R.id.name);
        this.floor = (Button) view.findViewById(R.id.floor);
        this.room = (Button) view.findViewById(R.id.room);
        this.module = (Button) view.findViewById(R.id.module);
    }

    protected void initWidget(View view) {
        MainApplication.modulePortAdapter.setFloor(this.floor);
        MainApplication.modulePortAdapter.setRoom(this.room);
        MainApplication.modulePortAdapter.setDeivce(this.module);
        this.floor.setOnClickListener(this);
        this.room.setOnClickListener(this);
        this.module.setOnClickListener(this);
        initPosition();
        this.listView.setAdapter(MainApplication.modulePortAdapter);
        this.num.setText((this.position + 1));
        this.device.setText(this.moduleBean.moduleName);
        this.model.setText(this.moduleBean.moduleModel);
        this.name.setText(this.moduleBean.moduleNameExt);
        refurbish();
    }

    protected void onCreateInit() {
        this.settingActivity = (SettingActivity) getActivity();
        this.settingActivity.setOnBackDown(this);
        this.floorManager = FloorManager.getInstance(this.settingActivity);
        this.floors = this.floorManager.findAllName();
        this.roomManager = RoomManager.getInstance(this.settingActivity);
        initPopupDialog();
        MainApplication.modulePortAdapter.setCheckHind(false);
        MainApplication.modulePortAdapter.setActivity(this.settingActivity);
        this.popupWindow = new DevicePopupWindow(this.settingActivity);
    }
}
