package com.easyctrl.ldy.activity;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.widget.Toast;
import com.easyctrl.dialog.EnterShowPassDialog;
import com.easyctrl.dialog.NetErrorDialog;
import com.easyctrl.event.EasyEventTypeHandler;
import com.easyctrl.fragment.UserCenterFragment;
import com.easyctrl.fragment.UserLeftFragment;
import com.easyctrl.iface.OnBackDown;
import com.easyctrl.iface.OnDialogConnectNet;
import com.easyctrl.iface.OnInitLeftSceneListener;
import com.easyctrl.iface.OnLoginListener;
import com.easyctrl.iface.OnNioSocketListener;
import com.easyctrl.iface.OnReviceLoginListener;
import com.easyctrl.impl.ProcesInstructUser;
import com.easyctrl.ldy.domain.FloorBean;
import com.easyctrl.ldy.domain.ModulePortBean;
import com.easyctrl.ldy.domain.RoomBean;
import com.easyctrl.ldy.net.Packet;
import com.easyctrl.ldy.util.ExitApp;
import com.easyctrl.ldy.util.WeekArray;
import com.easyctrl.ldy.view.SlidingMenu;
import com.easyctrl.ui.base.BaseActivity;

public class UserActivity extends BaseActivity implements OnNioSocketListener, OnReviceLoginListener {
    private static final int ENTERSETTING = 7;
    private static final int SHOWDIALOG = 6;
    private static final int SHOW_ERROR_NET = 8;
    private static final int SHOW_NET_CONNECTION = 9;
    private static OnLoginListener onLoginListener;
    private String TAG = UserActivity.class.getSimpleName();
    public EnterShowPassDialog enterShowPassDialog;
    private NetErrorDialog errorDialog;
    private FloorBean floorBean;
    private Handler handler = new Handler(new Callback() {
        public boolean handleMessage(Message msg) {
            Bundle data = msg.getData();
            switch (msg.what) {
                case 1:
                    ModulePortBean moduleBean = (ModulePortBean) data.getSerializable("lampBean");
                    MainApplication.modulePortManager.update(moduleBean, 0);
                    UserActivity.this.userCenterFragment.refush(moduleBean.port);
                    break;
                case 2:
                    ModulePortBean amingBean = (ModulePortBean) msg.getData().getSerializable("amingBean");
                    MainApplication.modulePortManager.update(amingBean, 0);
                    UserActivity.this.userCenterFragment.refush(amingBean.port);
                    break;
                case 6:
                    UserActivity.this.enterShowPassDialog.show();
                    Toast.makeText(UserActivity.this, "\u5bc6\u7801\u9519\u8bef\u8bf7\u91cd\u65b0\u8f93\u5165", 1).show();
                    break;
                case 7:
                    if (!MainApplication.userManager.isLogin()) {
                        UserActivity.this.startActivity(new Intent(UserActivity.this, SettingActivity.class));
                        UserActivity.this.finish();
                        MainApplication.userManager.setLogin(true);
                        break;
                    }
                    break;
                case 8:
                    UserActivity.this.errorDialog = new NetErrorDialog(UserActivity.this, new OnDialogConnectNet() {
                        public void onExitApp() {
                        }

                        public void onDialogConnection() {
                            MainApplication.easySocket.reconn();
                        }
                    });
                    UserActivity.this.errorDialog.show();
                    break;
                case 9:
                    Toast.makeText(UserActivity.this, "\u7f51\u7edc\u8fde\u63a5\u6210\u529f", 1).show();
                    if (UserActivity.this.errorDialog != null) {
                        UserActivity.this.errorDialog.dismiss();
                        break;
                    }
                    break;
            }
            return false;
        }
    });
    private ProcesInstructUser instruct;
    private LayoutInflater mInflater;
    public SlidingMenu mSlidingMenu;
    private OnBackDown onBackDown;
    public OnInitLeftSceneListener onInitLeftSceneListener;
    private RoomBean roomBean;
    private UserCenterFragment userCenterFragment;
    public WeekArray weekArray;

    public void setOnInitLeftSceneListener(OnInitLeftSceneListener onInitLeftSceneListener) {
        this.onInitLeftSceneListener = onInitLeftSceneListener;
    }

    public void setOnBackDown(OnBackDown onBackDown) {
        this.onBackDown = onBackDown;
    }

    public FloorBean getFloorBean() {
        return this.floorBean;
    }

    public void setFloorBean(FloorBean floorBean) {
        this.floorBean = floorBean;
    }

    public RoomBean getRoomBean() {
        return this.roomBean;
    }

    public void setRoomBean(RoomBean roomBean) {
        this.roomBean = roomBean;
    }

    public static void setOnLoginListener(OnLoginListener onLoginListener) {
        onLoginListener = onLoginListener;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_layout);
        this.mInflater = LayoutInflater.from(MainApplication.getAppContext());
        findViewByID();
        MainApplication.procesInstructModulePort.setHandler(this.handler);
        this.enterShowPassDialog = new EnterShowPassDialog(this);
        init();
        initWidget();
    }

    private void init() {
        this.mSlidingMenu.setCanSliding(false, false);
        this.mSlidingMenu.setLeftView(this.mInflater.inflate(R.layout.left_frame, null));
        this.mSlidingMenu.setCenterView(this.mInflater.inflate(R.layout.center_layout, null));
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.left_frame, new UserLeftFragment());
        ft.commit();
        this.userCenterFragment = new UserCenterFragment();
        this.mEventBus.register(this.userCenterFragment);
        pushFragments(BaseActivity.USRT, this.userCenterFragment, false, true, R.id.center_layout);
    }

    public void showLeft() {
        this.mSlidingMenu.showLeftView();
    }

    public void sendData(byte[] content) {
        Packet in = new Packet();
        in.pack(content);
        if (MainApplication.easySocket == null) {
            onLoginListener.onReconnect();
        }
        MainApplication.easySocket.send(in);
    }

    public String getRunningActivityName() {
        return ((RunningTaskInfo) ((ActivityManager) getSystemService("activity")).getRunningTasks(1).get(0)).topActivity.getClassName();
    }

    protected void onResume() {
        super.onResume();
        EasyEventTypeHandler easyEventTypeHandler = EasyEventTypeHandler.getInstance();
        easyEventTypeHandler.setType(1);
        this.mEventBus.post(easyEventTypeHandler);
    }

    protected void onDestroy() {
        super.onDestroy();
        this.mEventBus.unregister(this.userCenterFragment);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (this.onBackDown != null) {
            this.onBackDown.onKeyDonw(keyCode, event);
        }
        return false;
    }

    public void disconnect() {
        if (this.errorDialog != null) {
            this.errorDialog.dismiss();
        }
    }

    public void connectError() {
        this.handler.sendEmptyMessage(8);
    }

    public void connectSuccess() {
        this.handler.sendEmptyMessage(9);
    }

    public void loginApp(int type) {
        if (type == 0) {
            this.handler.sendEmptyMessage(7);
        } else if (type == 1) {
            this.handler.sendEmptyMessage(6);
        }
    }

    protected void findViewByID() {
        this.mSlidingMenu = (SlidingMenu) findViewById(R.id.slidingMenu);
    }

    protected void initWidget() {
        this.instruct = new ProcesInstructUser(this);
        this.instruct.setHandler(this.handler);
        ExitApp.getInstance().addActivity(this);
        this.weekArray = WeekArray.getInatance();
        if (MainApplication.easySocket != null) {
            MainApplication.easySocket.setOnNioSocketListener(this);
        }
        MainApplication.netPacketManager.setOnReviceLoginListener(this);
    }
}
