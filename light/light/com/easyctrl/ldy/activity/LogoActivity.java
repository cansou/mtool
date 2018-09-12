package com.easyctrl.ldy.activity;

import android.content.Intent;
import android.net.wifi.WifiManager.MulticastLock;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import com.easyctrl.dialog.WaitDialog;
import com.easyctrl.iface.OnNioSocketListener;
import com.easyctrl.iface.OnReviceLoginListener;
import com.easyctrl.ldy.domain.HostBean;
import com.easyctrl.ldy.net.Packet;
import com.easyctrl.ldy.util.CheckUtil;
import com.easyctrl.ldy.util.ExitApp;
import com.easyctrl.manager.HostManager;
import com.easyctrl.ui.base.BaseActivity;
import java.util.ArrayList;

public class LogoActivity extends BaseActivity implements OnReviceLoginListener, OnNioSocketListener {
    private static final int NETERROR = 4;
    private static final int START_LOGIN = 1;
    private static final int StART_USER = 2;
    private static final int UPDATE = 3;
    private String TAG = LogoActivity.class.getSimpleName();
    private ArrayList<HostBean> beans;
    private Handler handler = new Handler(new Callback() {
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    LogoActivity.this.start();
                    break;
                case 2:
                    if (MainApplication.easySocket != null) {
                        if (MainApplication.easySocket.state != 8) {
                            LogoActivity.this.start();
                            break;
                        }
                        MainApplication.createDB();
                        LogoActivity.this.startUserActivity();
                        break;
                    }
                    LogoActivity.this.start();
                    break;
                case 3:
                    boolean isLogin = false;
                    for (int i = 0; i < LogoActivity.this.beans.size(); i++) {
                        if (((HostBean) LogoActivity.this.beans.get(i)).equals(MainApplication.userManager.currentHost())) {
                            isLogin = true;
                        }
                    }
                    if (isLogin) {
                        LogoActivity.this.start();
                        break;
                    }
                    break;
                case 4:
                    LogoActivity.this.start();
                    break;
            }
            return false;
        }
    });
    private HostManager hostManager;
    private boolean isAuto;
    private MulticastLock lock;
    private WaitDialog waitDialog;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logo);
        MainApplication.netPacketManager.setOnReviceLoginListener(this);
        MainApplication.easySocket.setOnNioSocketListener(this);
        initWidget();
    }

    private void start() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    private void startUserActivity() {
        startActivity(new Intent(this, UserActivity.class));
        finish();
    }

    private void startLoginActivity() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    protected void findViewByID() {
    }

    protected void initWidget() {
        this.isAuto = MainApplication.userManager.isAutoLogin();
        if (!this.isAuto) {
            MainApplication.threadPool.submit(new Runnable() {
                public void run() {
                    try {
                        Thread.sleep(1500);
                        LogoActivity.this.handler.sendEmptyMessage(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        } else if (this.isAuto) {
            MainApplication.threadPool.submit(new Runnable() {
                public void run() {
                    try {
                        Thread.sleep(1500);
                        if (MainApplication.easySocket.state == 8) {
                            Packet packet = new Packet();
                            packet.pack(CheckUtil.createPassword(MainApplication.userManager.getTempPass()));
                            MainApplication.easySocket.send(packet);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        ExitApp.getInstance().addActivity(this);
    }

    public void loginApp(int type) {
        if (type == 0) {
            this.handler.sendEmptyMessage(2);
        } else if (1 == type) {
            this.handler.sendEmptyMessage(1);
        }
    }

    public void disconnect() {
    }

    public void connectError() {
        MainApplication.easySocket.close();
        start();
    }

    public void connectSuccess() {
    }
}
