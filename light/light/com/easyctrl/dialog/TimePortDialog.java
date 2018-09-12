package com.easyctrl.dialog;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import com.easyctrl.dialog.WaitDialog.OnWorkdListener;
import com.easyctrl.fragment.TimerFragmentList;
import com.easyctrl.ldy.activity.MainApplication;
import com.easyctrl.ldy.activity.R;
import com.easyctrl.ldy.activity.SettingActivity;
import com.easyctrl.ldy.domain.BindInfo;
import com.easyctrl.ldy.domain.ModulePortBean;
import com.easyctrl.ldy.domain.TimerBean;
import com.easyctrl.ldy.net.SendDataSocket;
import com.easyctrl.ldy.net.SendDataSocket.OnCloseListener;
import com.easyctrl.ui.base.BaseActivity;
import com.easyctrl.ui.base.BaseDialog;
import java.util.ArrayList;

public class TimePortDialog extends BaseDialog implements OnClickListener {
    private Handler handler;
    private EditText sceneName = ((EditText) findViewById(R.id.editName));
    private EditText sceneNum = ((EditText) findViewById(R.id.editNum));
    private ArrayList<ModulePortBean> selectBeans;
    private SettingActivity settingActivity;
    private TimerBean timeBean;

    public TimePortDialog(Context context) {
        super(context);
        this.settingActivity = (SettingActivity) context;
        setContentView((int) R.layout.time_port_dialog);
        findViewById(R.id.cancel).setOnClickListener(this);
        findViewById(R.id.enter).setOnClickListener(this);
    }

    public void setTimeBean(TimerBean bean) {
        this.timeBean = bean;
    }

    public void show() {
        this.selectBeans = MainApplication.modulePortAdapterSimple.getSelects();
        init();
        super.show();
    }

    private String createNum() {
        return "90" + this.timeBean.tID;
    }

    private void init() {
        this.sceneNum.setText(createNum());
    }

    private BindInfo createPortBindBean() {
        BindInfo bindInfo = new BindInfo();
        if (this.selectBeans.size() == 1) {
            ModulePortBean bean = (ModulePortBean) this.selectBeans.get(0);
            bindInfo.bind_id = this.timeBean.timeID;
            bindInfo.floor = bean.floor;
            bindInfo.room = bean.room;
            bindInfo.device = bean.name;
            bindInfo.bindInfo_type = 1;
            bindInfo.bind_info_type = 3;
            bindInfo.bind_module_J_id = bean.moduleID;
            bindInfo.bind_bindport = bean.port;
            bindInfo.bind_info_sceneID = Integer.valueOf(createNum()).intValue();
            bindInfo.bindInfo_name = this.sceneName.getText().toString();
        } else if (this.selectBeans.size() > 1) {
            bindInfo.bind_id = this.timeBean.timeID;
            bindInfo.bindInfo_type = 2;
            bindInfo.bind_info_type = 3;
            bindInfo.bind_info_sceneID = Integer.valueOf(createNum()).intValue();
            bindInfo.bindInfo_name = this.sceneName.getText().toString();
        }
        return bindInfo;
    }

    private void sendDatatoServer() {
        new WaitDialog(this.mContext, new OnWorkdListener() {
            public void doingWork() {
                TimePortDialog.this.sendMessageToHandler(1);
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                }
                String scene = MainApplication.jsonManager.createSenceJson(TimePortDialog.this.selectBeans, TimePortDialog.this.mContext);
                try {
                    SendDataSocket sendDataSocket = new SendDataSocket(MainApplication.userManager.currentHost(), 6001);
                    sendDataSocket.setOnCloseListener(new OnCloseListener() {
                        public void onClose() {
                            BindInfo bindInfo = TimePortDialog.this.createPortBindBean();
                            TimePortDialog.this.timeBean.isBind = 1;
                            TimePortDialog.this.timeBean.bind_type = bindInfo.bindInfo_type;
                            TimePortDialog.this.timeBean.type = bindInfo.bind_info_type;
                            TimePortDialog.this.timeBean.description = bindInfo.bindInfo_name;
                            MainApplication.bindInfoManager.addOrUpdate(bindInfo);
                            MainApplication.timerManager.update(TimePortDialog.this.timeBean, TimePortDialog.this.timeBean.timeID);
                            MainApplication.modulePortAdapterSimple.deleteList(TimePortDialog.this.selectBeans);
                            TimePortDialog.this.sendMessageToHandler(2);
                            TimePortDialog.this.settingActivity.clearStackByTag(BaseActivity.SETTING);
                            TimePortDialog.this.settingActivity.pushFragments(BaseActivity.SETTING, new TimerFragmentList(), true, true, R.id.setting_center);
                        }
                    });
                    sendDataSocket.sendData("down /json/s" + TimePortDialog.this.sceneNum.getText().toString() + ".json$ " + scene);
                } catch (Exception e2) {
                    e2.printStackTrace();
                    TimePortDialog.this.sendMessageToHandler(2);
                }
            }
        }, null).execute(new Integer[0]);
    }

    public void onClick(View v) {
        if (v.getId() == R.id.cancel) {
            dismiss();
        } else if (v.getId() == R.id.enter) {
            sendDatatoServer();
            dismiss();
        }
    }

    public void setHander(Handler handler) {
        this.handler = handler;
    }

    private void sendMessageToHandler(int messageType) {
        if (this.handler != null) {
            this.handler.sendEmptyMessage(messageType);
        }
    }
}
