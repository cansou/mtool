package com.easyctrl.ldy.activity;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.widget.Toast;
import com.easyctrl.broadcast.UpdateBroadCast;
import com.easyctrl.dialog.NetErrorDialog;
import com.easyctrl.event.EasyEventType;
import com.easyctrl.fragment.SettingCenterFragment;
import com.easyctrl.fragment.SettingLeftFragment;
import com.easyctrl.iface.OnBackDown;
import com.easyctrl.iface.OnDialogConnectNet;
import com.easyctrl.iface.OnNioSocketListener;
import com.easyctrl.iface.RegisterInteface;
import com.easyctrl.ldy.domain.ModulePortBean;
import com.easyctrl.ldy.net.Packet;
import com.easyctrl.ldy.util.ExitApp;
import com.easyctrl.ldy.view.SlidingMenu;
import com.easyctrl.manager.OrderManage;
import com.easyctrl.ui.base.BaseActivity;
import de.greenrobot.event.EventBus;

public class SettingActivity extends BaseActivity implements RegisterInteface, OnNioSocketListener {
    private static final String TAG = SettingActivity.class.getSimpleName();
    private NetErrorDialog errorDialog = null;
    private boolean flag = true;
    Handler handler = new Handler(new Callback() {
        public boolean handleMessage(Message msg) {
            Bundle data = msg.getData();
            switch (msg.what) {
                case 1:
                    new Bundle().putSerializable("lampBean", (ModulePortBean) data.getSerializable("lampBean"));
                    SettingActivity.this.startbroad.putExtras(data);
                    SettingActivity.this.sendBroadcast(SettingActivity.this.startbroad);
                    break;
                case 2:
                    ModulePortBean amingBean = (ModulePortBean) data.getSerializable("amingBean");
                    MainApplication.modulePortManager.update(amingBean, 0);
                    Bundle amingData = new Bundle();
                    amingData.putSerializable("amingBean", amingBean);
                    SettingActivity.this.startbroadAming.putExtras(amingData);
                    SettingActivity.this.sendBroadcast(SettingActivity.this.startbroadAming);
                    break;
                case 3:
                    if (SettingActivity.this.errorDialog == null) {
                        SettingActivity.this.errorDialog = new NetErrorDialog(SettingActivity.this, new OnDialogConnectNet() {
                            public void onExitApp() {
                            }

                            public void onDialogConnection() {
                                MainApplication.easySocket.reconn();
                            }
                        });
                    }
                    SettingActivity.this.errorDialog.show();
                    break;
                case 4:
                    Toast.makeText(SettingActivity.this, "\u8bbe\u5907 " + msg.getData().getInt("moduleid") + " \u6ce8\u518c,\u8bf7\u91cd\u65b0\u641c\u7d22", 0).show();
                    break;
                case 5:
                    Toast.makeText(SettingActivity.this, "\u8bbe\u5907 " + msg.getData().getInt("moduleid") + " \u79fb\u9664,\u8bf7\u91cd\u65b0\u641c\u7d22", 0).show();
                    break;
                case 7:
                    Toast.makeText(SettingActivity.this, "\u7f51\u7edc\u8fde\u63a5\u6210\u529f", 1).show();
                    if (SettingActivity.this.errorDialog != null) {
                        SettingActivity.this.errorDialog.dismiss();
                        break;
                    }
                    break;
            }
            return false;
        }
    });
    private LayoutInflater mInflater;
    public SlidingMenu mSlidingMenu;
    private OnBackDown onBackDown;
    Intent startbroad = new Intent(UpdateBroadCast.UPDATE_ACTION);
    Intent startbroadAming = new Intent(UpdateBroadCast.UPDATE_AMING_ACTION);

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_layout);
        MainApplication.procesInstructModulePort.setHandler(this.handler);
        MainApplication.easySocket.setOnNioSocketListener(this);
        findViewByID();
        initWidget();
        initView();
        EventBus.getDefault().register(this);
    }

    private void initView() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        this.mSlidingMenu.setLeftView(this.mInflater.inflate(R.layout.setting_menu_frame, null));
        this.mSlidingMenu.setCenterView(this.mInflater.inflate(R.layout.setting_center_frame, null));
        ft.replace(R.id.setting_left, new SettingLeftFragment());
        ft.commit();
        pushFragments(BaseActivity.SETTING, SettingCenterFragment.newInstance(), false, true, R.id.setting_center);
        MainApplication.userManager.setCurrentFragment("ModulePortListFragment");
    }

    public String getRunningActivityName() {
        return ((RunningTaskInfo) ((ActivityManager) getSystemService("activity")).getRunningTasks(1).get(0)).topActivity.getClassName();
    }

    public void sendData(byte[] data) {
        Packet in = new Packet();
        in.pack(data);
        MainApplication.easySocket.send(in);
    }

    public void showLeft() {
        this.mSlidingMenu.showLeftView();
    }

    protected void onDestroy() {
        super.onDestroy();
        MainApplication.userManager.setDownload(false);
        this.mSlidingMenu = null;
        this.mInflater = null;
        this.handler = null;
    }

    public synchronized void register(int moduleID) {
        Message message = new Message();
        message.what = 4;
        Bundle data = new Bundle();
        data.putInt("moduleid", moduleID);
        message.setData(data);
        this.handler.sendMessage(message);
    }

    public synchronized void remove(int moduleID) {
        Message message = new Message();
        message.what = 5;
        Bundle data = new Bundle();
        data.putInt("moduleid", moduleID);
        message.setData(data);
        this.handler.sendMessage(message);
    }

    public void setOnBackDown(OnBackDown onBackDown) {
        this.onBackDown = onBackDown;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 4) {
            boolean isBack;
            String fragmentName = MainApplication.userManager.getCurrentFragmentName();
            if (fragmentName.equals("ModuleListFragment") || fragmentName.equals("BindFragment") || fragmentName.equals("VirtualFragmentList") || fragmentName.equals("TimerFragmentList") || fragmentName.equals("SystemSetFragment") || fragmentName.equals("UserSetFragment")) {
                isBack = true;
            } else {
                isBack = false;
            }
            if (isBack) {
                showLeft();
            }
        }
        if (this.onBackDown != null) {
            this.onBackDown.onKeyDonw(keyCode, event);
        }
        return true;
    }

    public void disconnect() {
        if (this.errorDialog != null) {
            this.errorDialog.dismiss();
        }
        this.handler.sendEmptyMessage(3);
    }

    public void connectError() {
        this.handler.sendEmptyMessage(3);
    }

    public void connectSuccess() {
        this.handler.sendEmptyMessage(7);
    }

    protected void findViewByID() {
        this.mSlidingMenu = (SlidingMenu) findViewById(R.id.setting_slidingMenu);
    }

    protected void initWidget() {
        this.mSlidingMenu.setCanSliding(false, false);
        this.mInflater = LayoutInflater.from(this);
        ExitApp.getInstance().addActivity(this);
        MainApplication.userManager.setCurrentFragment("ModulePortListFragment");
    }

    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    public void onEventMainThread(EasyEventType event) {
        if (event.getType() == 9) {
            Log.i("data", "event.getKey():" + event.getKey());
            sendData(OrderManage.deleteScene(event.getKey()));
        }
    }
}
