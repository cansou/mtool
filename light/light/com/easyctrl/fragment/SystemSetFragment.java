package com.easyctrl.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import com.easyctrl.dialog.BackupDialog;
import com.easyctrl.dialog.ChangePassWordDialog;
import com.easyctrl.dialog.EasyProgressDialog;
import com.easyctrl.dialog.FileListDialog;
import com.easyctrl.dialog.UpdateHostIPDialog;
import com.easyctrl.event.EasyEventTypeHandler;
import com.easyctrl.ldy.activity.MainApplication;
import com.easyctrl.ldy.activity.R;
import com.easyctrl.ldy.activity.SettingActivity;
import com.easyctrl.ldy.net.SocketUtil;
import com.easyctrl.ldy.util.FileUtil;
import com.easyctrl.ldy.util.ZipUtil;
import com.easyctrl.ldy.view.ShowHostTimeView;
import com.easyctrl.manager.OrderManage;
import com.easyctrl.ui.base.BaseEasyCtrlFragment;

public class SystemSetFragment extends BaseEasyCtrlFragment implements OnClickListener {
    private String TAG = SystemSetFragment.class.getSimpleName();
    private TextView currentIP;
    private Handler handler = new Handler(new Callback() {
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    EasyProgressDialog.getInstance(SystemSetFragment.this.settingActivity).show(SystemSetFragment.this.progressDialog, "\u4e3b\u673a\u6b63\u5728\u91cd\u542f......", SystemSetFragment.this.TAG);
                    break;
                case 2:
                    EasyProgressDialog.getInstance(SystemSetFragment.this.settingActivity).dismiss(SystemSetFragment.this.progressDialog);
                    break;
                case 3:
                    EasyProgressDialog.getInstance(SystemSetFragment.this.settingActivity).show(SystemSetFragment.this.progressDialog, "\u4e3b\u673a\u6b63\u5728\u91cd\u542f......", SystemSetFragment.this.TAG);
                    break;
                case 4:
                    EasyProgressDialog.getInstance(SystemSetFragment.this.settingActivity).dismiss(SystemSetFragment.this.progressDialog);
                    break;
            }
            return false;
        }
    });
    private ShowHostTimeView hostTimeView;
    private ProgressDialog progressDialog;
    private SettingActivity settingActivity;
    private View systemView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onCreateInit();
        this.progressDialog = new ProgressDialog(this.settingActivity);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.system_set_fragment, null);
        findViewByID(view);
        initWidget(view);
        return view;
    }

    protected void findViewByID(View view) {
        view.findViewById(R.id.hostname).setOnClickListener(this);
        view.findViewById(R.id.navigaview).findViewById(R.id.leftbutton).setOnClickListener(this);
        view.findViewById(R.id.hostIP).setOnClickListener(this);
        view.findViewById(R.id.loginpass).setOnClickListener(this);
        view.findViewById(R.id.login_manager).setOnClickListener(this);
        view.findViewById(R.id.system_recover).setOnClickListener(this);
        this.currentIP = (TextView) view.findViewById(R.id.currentIP);
        this.systemView = view.findViewById(R.id.sys_time);
        view.findViewById(R.id.system_backup).setOnClickListener(this);
        view.setOnClickListener(this);
        this.hostTimeView = (ShowHostTimeView) this.systemView.findViewById(R.id.systemTimeLayout);
        this.systemView.findViewById(R.id.correct).setOnClickListener(this);
    }

    public void onDestroy() {
        super.onDestroy();
        this.settingActivity.mEventBus.unregister(this);
        this.hostTimeView.cancel();
    }

    public void onEventMainThread(EasyEventTypeHandler eventHandler) {
        if (eventHandler.getType() == 2) {
            this.hostTimeView.setHostTime(eventHandler.getHostTime());
        } else if (eventHandler.getType() == 16 && MainApplication.userManager.getCurrentBackStata()) {
            downLoad();
        }
    }

    private void downLoad() {
        this.progressDialog.show();
        this.progressDialog.setMessage("\u6b63\u5728\u5907\u4efd\uff0c\u8bf7\u8010\u5fc3\u7b49\u5f85");
        MainApplication.threadPool.submit(new Runnable() {
            public void run() {
                try {
                    FileUtil.backupData();
                    Thread.sleep(2000);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                String pathName = MainApplication.userManager.getPath();
                String path = new StringBuilder(String.valueOf(SystemSetFragment.this.settingActivity.baseFile.getAbsolutePath())).append("/").append(pathName).toString();
                String fileName = new StringBuilder(String.valueOf(SystemSetFragment.this.settingActivity.baseFile.getAbsolutePath())).append("/").append(pathName).append(".zip").toString();
                downData(path, "ET_APP_NAME.DB");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                downData(path, "ET_APP_DATA.DB");
                ZipUtil.doZip(path, fileName);
                SystemSetFragment.this.handler.post(new Runnable() {
                    public void run() {
                        SystemSetFragment.this.progressDialog.dismiss();
                    }
                });
            }

            private void downData(String path, String downName) {
                SocketUtil socketUtil = new SocketUtil(MainApplication.userManager.currentHost(), 6001, SystemSetFragment.this.settingActivity);
                try {
                    socketUtil.sendData("read /backup/" + downName + "$");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                socketUtil.downDB(path, downName);
            }
        });
    }

    public void onClick(View v) {
        if (R.id.hostname == v.getId()) {
            return;
        }
        if (R.id.leftbutton == v.getId()) {
            this.settingActivity.showLeft();
        } else if (R.id.hostIP == v.getId()) {
            UpdateHostIPDialog ipDialog = new UpdateHostIPDialog(this.settingActivity);
            ipDialog.setHandler(this.handler);
            ipDialog.show();
        } else if (R.id.loginpass == v.getId()) {
            dialog = new ChangePassWordDialog(this.settingActivity);
            dialog.setType(1);
            dialog.show();
        } else if (R.id.login_manager == v.getId()) {
            dialog = new ChangePassWordDialog(this.settingActivity);
            dialog.setType(2);
            dialog.show();
        } else if (R.id.sys_time == v.getId()) {
        } else {
            if (R.id.system_backup == v.getId()) {
                new BackupDialog(this.settingActivity).show();
            } else if (R.id.correct == v.getId()) {
                this.settingActivity.sendData(OrderManage.calibraHostTime());
                this.settingActivity.sendData(OrderManage.getHostCurrentTime());
            } else if (R.id.system_recover == v.getId()) {
                new FileListDialog(this.settingActivity).show();
            }
        }
    }

    protected void initWidget(View view) {
        this.currentIP.setText(MainApplication.userManager.currentHost());
    }

    protected void onCreateInit() {
        this.settingActivity = (SettingActivity) getActivity();
        this.settingActivity.sendData(OrderManage.getHostCurrentTime());
    }
}
