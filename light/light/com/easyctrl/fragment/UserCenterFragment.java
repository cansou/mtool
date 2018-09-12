package com.easyctrl.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import com.easyctrl.dialog.ExitDialog;
import com.easyctrl.dialog.WaitViewTimer;
import com.easyctrl.event.EasyEventTypeHandler;
import com.easyctrl.iface.OnBackDown;
import com.easyctrl.ldy.activity.MainApplication;
import com.easyctrl.ldy.activity.R;
import com.easyctrl.ldy.activity.UserActivity;
import com.easyctrl.ldy.domain.FloorBean;
import com.easyctrl.ldy.domain.ModulePortBean;
import com.easyctrl.ldy.view.SelectPositionPopupWindow;
import com.easyctrl.ldy.view.SelectPositionPopupWindow.OnDefineListener;
import com.easyctrl.ldy.view.WaitView;
import com.easyctrl.manager.DownLoadModuleManager;
import com.easyctrl.manager.OperatorBeanManager;
import com.easyctrl.ui.base.BaseEasyCtrlFragment;
import java.util.ArrayList;

public class UserCenterFragment extends BaseEasyCtrlFragment implements OnClickListener, OnDefineListener, OnBackDown {
    private String TAG = UserCenterFragment.class.getSimpleName();
    private ArrayList<ModulePortBean> beans;
    private Button btnFloor;
    private Button btnModule;
    private Button btnRoom;
    private int currentPage = 0;
    private ArrayList<String> devices;
    private FloorBean floorBean = null;
    private ArrayList<String> floors;
    private Handler handler = new Handler(new Callback() {
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 5:
                    UserCenterFragment.this.beans = MainApplication.modulePortManager.findByFloorAndRoomAndDeivce(UserCenterFragment.this.btnFloor.getText().toString(), UserCenterFragment.this.btnRoom.getText().toString(), UserCenterFragment.this.btnModule.getText().toString());
                    MainApplication.modulePortAdapter.setBeans(UserCenterFragment.this.beans);
                    MainApplication.modulePortAdapter.notifyDataSetChanged();
                    WaitViewTimer.getInstance(UserCenterFragment.this.userActivity).dismiss(UserCenterFragment.this.waitView);
                    break;
                case 6:
                    WaitViewTimer.getInstance(UserCenterFragment.this.userActivity).show(UserCenterFragment.this.waitView);
                    break;
                case 7:
                    WaitViewTimer.getInstance(UserCenterFragment.this.userActivity).dismiss(UserCenterFragment.this.waitView);
                    break;
                case 8:
                    WaitViewTimer.getInstance(UserCenterFragment.this.userActivity).dismiss(UserCenterFragment.this.waitView);
                    UserCenterFragment.this.userActivity.showToast("\u7f51\u7edc\u5f02\u5e38\uff0c\u65e0\u6cd5\u4e0b\u8f7d\u6570\u636e");
                    break;
            }
            return false;
        }
    });
    private ListView listView;
    private OperatorBeanManager operatorBeanManager;
    private SelectPositionPopupWindow positionPopupWindow;
    private ArrayList<String> rooms;
    private Button showMenu;
    private UserActivity userActivity;
    private String userDevice;
    private String userFloor;
    private String userRoor;
    private View view;
    private WaitView waitView;
    private String whereDevice;
    private String whereFloor;
    private String whereRoom;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onCreateInit();
    }

    public void onEventMainThread(EasyEventTypeHandler event) {
        if (event.getType() == 1) {
            update();
        }
    }

    public void onResume() {
        super.onResume();
        this.waitView.stopAnim();
    }

    private void update() {
        DownLoadModuleManager downLoadModuleManager = new DownLoadModuleManager(this.userActivity);
        downLoadModuleManager.setHandler(this.handler);
        downLoadModuleManager.setDialogCannelUpdate(5);
        downLoadModuleManager.setDialogTypeCannel(7);
        downLoadModuleManager.setDialogTypeShow(6);
        downLoadModuleManager.setError(8);
        try {
            downLoadModuleManager.donwloadData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.user_center_layou, container, false);
        findViewByID(this.view);
        initWidget(this.view);
        return this.view;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void refush() {
    }

    public void onClick(View v) {
        if (v.getId() == R.id.btnFloor) {
            this.positionPopupWindow.setListenerFrooRoom(true);
            this.positionPopupWindow.showAsAnchor(v, this.floors);
        } else if (v.getId() == R.id.btnRoom) {
            boolean isAll = true;
            for (int i = 0; i < this.rooms.size(); i++) {
                if (((String) this.rooms.get(i)).equals("\u5168\u90e8")) {
                    isAll = false;
                }
            }
            if (isAll) {
                this.rooms.add("\u5168\u90e8");
            }
            this.positionPopupWindow.setListenerFrooRoom(true);
            this.positionPopupWindow.showAsAnchor(v, this.rooms);
        } else if (v.getId() == R.id.btnModule) {
            this.positionPopupWindow.setListenerFrooRoom(true);
            this.positionPopupWindow.showAsAnchor(v, this.devices);
        }
    }

    public void onDefine(View anchor) {
    }

    public void onRefush(String content, View anchor, View v) {
        Button button = (Button) anchor;
        if (button.getId() == R.id.btnFloor) {
            this.whereFloor = button.getText().toString();
            this.rooms = MainApplication.modulePortManager.findRoomByFloor(this.whereFloor);
            this.whereRoom = (String) this.rooms.get(0);
            setFloorAndRoom();
        } else if (button.getId() == R.id.btnRoom) {
            this.whereRoom = button.getText().toString();
            setFloorAndRoom();
        } else if (button.getId() == R.id.btnModule) {
            this.whereDevice = button.getText().toString();
        }
        this.currentPage = 0;
        this.btnRoom.setText(this.whereRoom);
        this.beans = MainApplication.modulePortManager.findByFloorAndRoomAndDeivce(this.whereFloor, this.whereRoom, this.whereDevice);
        MainApplication.modulePortAdapter.setBeans(this.beans);
        MainApplication.modulePortAdapter.notifyDataSetChanged();
    }

    private void setFloorAndRoom() {
        this.floorBean = MainApplication.floorManager.findByName(this.btnFloor.getText().toString());
        if (this.floorBean != null) {
            this.userActivity.setFloorBean(this.floorBean);
            this.userActivity.setRoomBean(MainApplication.roomManager.findByName(this.btnRoom.getText().toString(), this.floorBean.id));
            if (this.userActivity.onInitLeftSceneListener != null) {
                this.userActivity.onInitLeftSceneListener.onInit(this.btnRoom.getText().toString());
            }
        }
    }

    private void updateData() {
        this.beans = MainApplication.modulePortManager.findByFloorAndRoomAndDeivce(this.btnFloor.getText().toString(), this.btnRoom.getText().toString(), this.btnModule.getText().toString());
        MainApplication.modulePortAdapter.setBeans(this.beans);
    }

    public void refush(int id) {
        MainApplication.threadPool.submit(new Runnable() {
            public void run() {
                UserCenterFragment.this.updateData();
                UserCenterFragment.this.handler.post(new Runnable() {
                    public void run() {
                        MainApplication.modulePortAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }

    public void onKeyDonw(int keyCode, KeyEvent event) {
        if (keyCode == 4) {
            new ExitDialog(this.userActivity).show();
        }
    }

    protected void findViewByID(View view) {
        this.btnFloor = (Button) view.findViewById(R.id.btnFloor);
        this.btnRoom = (Button) view.findViewById(R.id.btnRoom);
        this.btnModule = (Button) view.findViewById(R.id.btnModule);
        this.showMenu = (Button) view.findViewById(R.id.leftButton);
        this.listView = (ListView) view.findViewById(R.id.listview);
        this.waitView = (WaitView) view.findViewById(R.id.waitview);
    }

    protected void initWidget(View view) {
        this.btnFloor.setOnClickListener(this);
        this.btnRoom.setOnClickListener(this);
        this.btnModule.setOnClickListener(this);
        this.btnFloor.setText(this.whereFloor);
        this.btnRoom.setText(this.whereRoom);
        this.btnModule.setText(this.whereDevice);
        this.listView.setAdapter(MainApplication.modulePortAdapter);
        this.showMenu.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                UserActivity userActivity = (UserActivity) UserCenterFragment.this.getActivity();
                userActivity.showLeft();
                UserCenterFragment.this.operatorBeanManager.setOperatorArray(UserCenterFragment.this.beans);
                if (userActivity.mSlidingMenu.isShowLeft()) {
                    UserCenterFragment.this.setFloorAndRoom();
                }
            }
        });
        if (this.userFloor == null || this.userRoor == null || this.userDevice == null) {
            this.beans = MainApplication.modulePortManager.findByFloorAndRoomAndDeivce(this.whereFloor, this.whereRoom, this.whereDevice);
        } else {
            this.beans = MainApplication.modulePortManager.findByFloorAndRoomAndDeivce(this.userFloor, this.userRoor, this.userDevice);
            this.btnFloor.setText(this.userFloor);
            this.btnRoom.setText(this.userRoor);
            this.btnModule.setText(this.userDevice);
            if (this.beans == null || this.beans.size() == 0) {
                this.beans = MainApplication.modulePortManager.findByFloorAndRoomAndDeivce(this.whereFloor, this.whereRoom, this.whereDevice);
                this.btnFloor.setText(this.whereFloor);
                this.btnRoom.setText(this.whereRoom);
                this.btnModule.setText(this.whereDevice);
            }
        }
        MainApplication.modulePortAdapter.setBeans(this.beans);
        MainApplication.modulePortAdapter.setCheckHind(true);
        MainApplication.modulePortAdapter.notifyDataSetChanged();
    }

    protected void onCreateInit() {
        this.userActivity = (UserActivity) getActivity();
        this.userActivity.setOnBackDown(this);
        this.positionPopupWindow = new SelectPositionPopupWindow(this.userActivity);
        this.positionPopupWindow.setOnDefineListener(this);
        this.operatorBeanManager = OperatorBeanManager.getInstance();
        this.floors = MainApplication.modulePortManager.findFloor(false, MainApplication.jsonManager.getOnLineModuleID());
        this.devices = MainApplication.deviceManager.findAllType();
        if (this.floors.size() > 0) {
            this.whereFloor = (String) this.floors.get(0);
            this.rooms = MainApplication.modulePortManager.findRoomByFloor(this.whereFloor);
            this.devices.add("\u5168\u90e8");
            this.rooms.add("\u5168\u90e8");
        } else {
            this.whereRoom = "\u5168\u90e8";
            this.whereFloor = "\u5168\u90e8";
        }
        if (this.rooms == null || this.rooms.size() <= 0) {
            this.whereRoom = "\u5168\u90e8";
        } else {
            this.whereRoom = (String) this.rooms.get(0);
        }
        this.whereDevice = "\u7167\u660e";
        this.userFloor = MainApplication.userManager.getUserFloor();
        this.userRoor = MainApplication.userManager.getUserRoor();
        this.userDevice = MainApplication.userManager.getUserDevice();
    }

    public void onPause() {
        super.onPause();
        MainApplication.userManager.setUserFloor(this.btnFloor.getText().toString());
        MainApplication.userManager.setUserRoom(this.btnRoom.getText().toString());
        MainApplication.userManager.setUserDevice(this.btnModule.getText().toString());
    }
}
