package com.easyctrl.dialog;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import com.easyctrl.ldy.activity.MainApplication;
import com.easyctrl.ldy.activity.R;
import com.easyctrl.ldy.domain.BindBean;
import com.easyctrl.ldy.domain.BindInfo;
import com.easyctrl.ldy.domain.SceneBean;
import com.easyctrl.ldy.domain.TimerBean;
import com.easyctrl.ldy.domain.VirtualBean;
import com.easyctrl.ui.base.BaseDialog;

public class TimerBindDialog extends BaseDialog implements OnClickListener {
    private BindInfo bindInfo;
    private TextView infoView = ((TextView) findViewById(R.id.info));
    private Object object;
    private TimerBean selectTimerBean;

    private BindInfo setRelationBindBean(BindBean bindBean) {
        BindInfo bindInfo = new BindInfo();
        bindInfo.bindInfo_moduleID = bindBean.bind_moduleID;
        bindInfo.bindInfo_key = bindBean.keyValue;
        bindInfo.bindInfo_type = 2;
        bindInfo.floor = bindBean.floor;
        bindInfo.room = bindBean.room;
        bindInfo.device = bindBean.deviceName;
        bindInfo.bind_info_type = bindBean.type;
        bindInfo.bind_module_J_id = bindBean.bindModuleID;
        bindInfo.bind_bindport = bindBean.bindPort;
        SceneBean sceneBean = MainApplication.sceneManager.findByBindID(bindBean.bindID, "\u6309\u4e0b\u573a\u666f1");
        if (sceneBean == null) {
            return null;
        }
        bindInfo.bind_info_sceneID = Integer.valueOf(sceneBean.sceneBean_jsonName).intValue();
        return bindInfo;
    }

    private BindInfo setRelationVirtualBean(VirtualBean virtualBean) {
        BindInfo bindInfo = new BindInfo();
        bindInfo.bindInfo_moduleID = virtualBean.virtualID;
        bindInfo.bindInfo_key = virtualBean.key;
        bindInfo.bindInfo_type = 1;
        bindInfo.floor = virtualBean.floor;
        bindInfo.room = virtualBean.room;
        bindInfo.device = virtualBean.deviceName;
        bindInfo.bind_module_J_id = virtualBean.moduleID;
        bindInfo.bind_bindport = virtualBean.port;
        bindInfo.bind_info_type = virtualBean.bindType;
        bindInfo.bind_info_sceneID = virtualBean.v_scene_id;
        return bindInfo;
    }

    public TimerBindDialog(Context context) {
        super(context);
        setContentView((int) R.layout.timer_bind_dialog);
        findViewById(R.id.cancel).setOnClickListener(this);
        findViewById(R.id.enter).setOnClickListener(this);
    }

    public void show() {
        if (this.object instanceof VirtualBean) {
            this.bindInfo = setRelationVirtualBean(this.object);
        } else if (this.object instanceof BindBean) {
            this.bindInfo = setRelationBindBean(this.object);
        }
        if (this.bindInfo != null) {
            if (this.bindInfo.bindInfo_type == 2) {
                this.infoView.setText("\u5b9a\u65f6\u7ed1\u5b9a ID:" + this.bindInfo.bindInfo_moduleID + " KEY:" + this.bindInfo.bindInfo_key + " ?");
            } else {
                this.infoView.setText("\u5b9a\u65f6\u7ed1\u5b9a KEY:" + this.bindInfo.bindInfo_key + " ?");
            }
        }
        super.show();
    }

    public void onClick(View v) {
        if (v.getId() == R.id.cancel) {
            dismiss();
        } else if (v.getId() == R.id.enter) {
            if (this.bindInfo != null) {
                if (this.selectTimerBean.isBind == 1) {
                    this.bindInfo.bind_id = this.selectTimerBean.timeID;
                    this.selectTimerBean.isBind = 1;
                    MainApplication.bindInfoManager.update(this.bindInfo, this.selectTimerBean.timeID);
                    this.selectTimerBean.bind_type = this.bindInfo.bindInfo_type;
                    MainApplication.timerManager.update(this.selectTimerBean, this.selectTimerBean.timeID);
                } else {
                    this.bindInfo.bind_id = this.selectTimerBean.timeID;
                    this.selectTimerBean.isBind = 1;
                    this.selectTimerBean.bind_type = this.bindInfo.bindInfo_type;
                    MainApplication.bindInfoManager.add(this.bindInfo);
                    MainApplication.timerManager.update(this.selectTimerBean, this.selectTimerBean.timeID);
                }
            }
            MainApplication.timerAdapter.deleteList(MainApplication.timerAdapter.getSelects());
            dismiss();
        }
    }

    public void setTimerBean(TimerBean selectTimerBean) {
        this.selectTimerBean = selectTimerBean;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}
