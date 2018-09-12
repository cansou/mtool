package com.easyctrl.ldy.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.MulticastLock;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.easyctrl.adapter.HostAdapter;
import com.easyctrl.dialog.EasyProgressDialog;
import com.easyctrl.dialog.ExitDialog;
import com.easyctrl.dialog.HintDialog;
import com.easyctrl.dialog.WaitDialog;
import com.easyctrl.dialog.WaitDialog.OnPostExecuteListener;
import com.easyctrl.dialog.WaitDialog.OnWorkdListener;
import com.easyctrl.dialog.WarnDialog;
import com.easyctrl.dialog.WarnDialog.OnWarnClickListener;
import com.easyctrl.iface.OnLoginListener;
import com.easyctrl.iface.OnNioSocketListener;
import com.easyctrl.iface.OnReviceLoginListener;
import com.easyctrl.ldy.domain.HostBean;
import com.easyctrl.ldy.net.Packet;
import com.easyctrl.ldy.net.SocketUtil;
import com.easyctrl.ldy.util.CheckUtil;
import com.easyctrl.ldy.util.ExitApp;
import com.easyctrl.manager.HostManager;
import com.easyctrl.manager.HostManager.OnLineListenerHost;
import com.easyctrl.ui.base.BaseActivity;
import java.io.IOException;
import java.util.ArrayList;

public class LoginActivity extends BaseActivity implements OnNioSocketListener, OnClickListener, OnReviceLoginListener {
    private static final int DELETEANDOPENDB = 10;
    private static final int LOGIN = 5;
    private static final int LOGINERROR = 6;
    private static final int NETERROR = 3;
    private static final int OPENDATATE = 11;
    private static final int PASSERROR = 4;
    private static final int UPDATE = 1;
    private static final int WARN = 2;
    private static OnLoginListener onLoginListener;
    public String TAG = LoginActivity.class.getSimpleName();
    private HostAdapter adapter;
    private ArrayList<HostBean> beans;
    private CheckBox checkRemember;
    private CheckBox checkauto;
    private EasyProgressDialog easyProgressDialog;
    private Handler handler = new Handler(new Callback() {
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    LoginActivity.this.refush();
                    break;
                case 2:
                    LoginActivity.this.warnDialog.show();
                    LoginActivity.this.warnDialog.setOnClickListener(new OnWarnClickListener() {
                        public void onConnect() {
                        }
                    });
                    break;
                case 3:
                    LoginActivity.this.hint.setHint(LoginActivity.this.getResources().getString(R.string.hintstr));
                    LoginActivity.this.hint.show();
                    LoginActivity.this.easyProgressDialog.dismiss(LoginActivity.this.progressDialog);
                    break;
                case 4:
                    LoginActivity.this.hint.setHint(LoginActivity.this.getResources().getString(R.string.reinptPass));
                    LoginActivity.this.hint.show();
                    LoginActivity.this.easyProgressDialog.dismiss(LoginActivity.this.progressDialog);
                    break;
                case 5:
                    LoginActivity.this.startUserActivity();
                    break;
                case 6:
                    LoginActivity.this.easyProgressDialog.dismiss(LoginActivity.this.progressDialog);
                    break;
                case 10:
                    MainApplication.createDB();
                    MainApplication.deleteAllDB();
                    LoginActivity.this.startUserActivity();
                    break;
                case 11:
                    MainApplication.createDB();
                    LoginActivity.this.startUserActivity();
                    break;
            }
            return false;
        }
    });
    private HintDialog hint;
    private HostManager hostManager;
    private TextView inputaccount;
    private EditText inputpass;
    private ListView listView;
    private MulticastLock lock;
    private ProgressDialog progressDialog;
    private WaitDialog waitDialog;
    private WarnDialog warnDialog;

    public static void setOnLoginListener(OnLoginListener LoginListener) {
        onLoginListener = LoginListener;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.lock = ((WifiManager) getSystemService("wifi")).createMulticastLock(this.TAG);
        setContentView(R.layout.login_layout);
        if (MainApplication.easySocket != null) {
            MainApplication.easySocket.setOnNioSocketListener(this);
        }
        MainApplication.netPacketManager.setOnReviceLoginListener(this);
        this.warnDialog = new WarnDialog(this);
        this.hint = new HintDialog(this);
        findViewByID();
        initWidget();
        this.easyProgressDialog = EasyProgressDialog.getInstance(this.mContext);
    }

    private void refush() {
        if (this.beans != null && this.beans.size() > 0) {
            HostBean bean = (HostBean) this.beans.get(0);
            this.inputaccount.setText(bean.devIP);
            MainApplication.userManager.setHost(bean.devIP);
            MainApplication.userManager.setCurrentHost(bean.devIP);
            MainApplication.userManager.setCurrentMask(bean.devMask);
            MainApplication.userManager.setCurrentGateway(bean.devRoute);
        }
        this.adapter.setBeans(this.beans);
        this.adapter.notifyDataSetChanged();
    }

    private void initWaitDialog() {
        this.waitDialog = new WaitDialog(this, new OnWorkdListener() {
            public void doingWork() {
                LoginActivity.this.hostManager = HostManager.getInstance();
                try {
                    LoginActivity.this.hostManager.setOnLineListenerHost(new OnLineListenerHost() {
                        public void onLine(String host) {
                            LoginActivity.this.beans = LoginActivity.this.hostManager.analyzeToBean(host);
                            LoginActivity.this.handler.sendEmptyMessage(1);
                        }
                    });
                    LoginActivity.this.hostManager.getIPinfo(LoginActivity.this.lock);
                } catch (IOException e) {
                }
                if (LoginActivity.this.beans == null || LoginActivity.this.beans.isEmpty()) {
                    LoginActivity.this.handler.sendEmptyMessage(3);
                }
            }
        }, new OnPostExecuteListener() {
            public void after() {
                LoginActivity.this.handler.sendEmptyMessage(1);
            }
        });
        this.waitDialog.execute(new Integer[0]);
    }

    private void search() {
        initWaitDialog();
    }

    private void startUserActivity() {
        if (this.progressDialog != null) {
            this.easyProgressDialog.dismiss(this.progressDialog);
            startActivity(new Intent(this, UserActivity.class));
            overridePendingTransition(R.anim.push_left_out, R.anim.push_left_in);
            finish();
        }
    }

    public void disconnect() {
    }

    public void connectError() {
        this.handler.sendEmptyMessage(6);
    }

    public void connectSuccess() {
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 4) {
            new ExitDialog(this).show();
        }
        return false;
    }

    public void onClick(View v) {
        if (v.getId() == R.id.login) {
            String account = this.inputaccount.getText().toString();
            String pass = this.inputpass.getText().toString();
            if (this.beans == null || this.beans.isEmpty()) {
                this.hint.show();
            } else if (pass.length() == 0) {
                this.inputpass.setHint("\u8bf7\u8f93\u5165\u5bc6\u7801");
            } else {
                MainApplication.userManager.setTempLoginPws(pass);
                checkUser(account, pass);
                if (onLoginListener != null) {
                    onLoginListener.onLogin(MainApplication.userManager.currentHost(), null);
                }
                if (MainApplication.easySocket != null) {
                    MainApplication.easySocket.setOnNioSocketListener(this);
                }
                this.progressDialog = new ProgressDialog(this);
                this.easyProgressDialog.show(this.progressDialog, "\u6b63\u5728\u8bf7\u6c42\u767b\u5f55......", this.TAG);
                new Handler().post(new Runnable() {
                    public void run() {
                        byte[] pass = CheckUtil.createPassword(LoginActivity.this.inputpass.getText().toString());
                        if (pass == null) {
                            Toast.makeText(LoginActivity.this, LoginActivity.this.getResources().getString(R.string.errLoginPassEight), 1).show();
                            LoginActivity.this.progressDialog.dismiss();
                            return;
                        }
                        Packet packet = new Packet();
                        packet.pack(pass);
                        MainApplication.easySocket.send(packet);
                    }
                });
            }
        } else if (R.id.research == v.getId()) {
            search();
        }
    }

    private void checkUser(String account, String pass) {
        if (this.checkRemember.isChecked()) {
            MainApplication.userManager.setHost(account);
            MainApplication.userManager.setPassword(pass);
            MainApplication.userManager.setRemember(true);
            return;
        }
        MainApplication.userManager.setHost("");
        MainApplication.userManager.setPassword(null);
        MainApplication.userManager.setRemember(false);
    }

    private void downLoad() {
        MainApplication.threadPool.submit(new Runnable() {
            public void run() {
                SocketUtil socketUtil = new SocketUtil(MainApplication.userManager.currentHost(), 6001, LoginActivity.this);
                try {
                    socketUtil.sendData("read /backup/ET_APP_NAME.DB$");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (socketUtil.saveDB()) {
                    LoginActivity.this.handler.sendEmptyMessage(10);
                } else {
                    LoginActivity.this.handler.sendEmptyMessage(11);
                }
            }
        });
    }

    public void loginApp(int type) {
        if (type == 0) {
            downLoad();
            if (this.checkauto.isChecked()) {
                MainApplication.userManager.setRemember(true);
                MainApplication.userManager.setAutoLogin(true);
            } else {
                MainApplication.userManager.setAutoLogin(false);
            }
            if (this.checkauto.isChecked()) {
                MainApplication.userManager.setRemember(true);
                MainApplication.userManager.setAutoLogin(true);
                return;
            }
            MainApplication.userManager.setAutoLogin(false);
        } else if (1 == type) {
            this.handler.sendEmptyMessage(4);
        }
    }

    protected void findViewByID() {
        this.listView = (ListView) findViewById(R.id.listhost);
        findViewById(R.id.login).setOnClickListener(this);
        findViewById(R.id.research).setOnClickListener(this);
        this.checkRemember = (CheckBox) findViewById(R.id.checkRemember);
        this.checkauto = (CheckBox) findViewById(R.id.checkauto);
        this.inputaccount = (TextView) findViewById(R.id.inputaccount);
        this.inputpass = (EditText) findViewById(R.id.inputpass);
    }

    protected void initWidget() {
        this.adapter = new HostAdapter(null);
        initWaitDialog();
        this.listView.setAdapter(this.adapter);
        this.listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                HostBean bean = (HostBean) LoginActivity.this.beans.get(position);
                LoginActivity.this.inputaccount.setText(bean.devIP);
                MainApplication.userManager.setHost(bean.devIP);
                MainApplication.userManager.setCurrentHost(bean.devIP);
            }
        });
        if (MainApplication.userManager.isRemember()) {
            this.checkRemember.setChecked(true);
            this.inputaccount.setText(MainApplication.userManager.getHost());
            this.inputpass.setText(MainApplication.userManager.getPassword());
        }
        ExitApp.getInstance().addActivity(this);
    }
}
