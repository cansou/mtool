package com.easyctrl.impl;

import android.content.Intent;
import android.os.Bundle;
import com.easyctrl.broadcast.BindBroadCast;
import com.easyctrl.iface.OnBindListener;
import com.easyctrl.ldy.activity.MainApplication;
import com.easyctrl.ldy.domain.BindBean;

public class ProcesBind implements OnBindListener {
    public void onBind(int moduleID, int downOrRelese, int keyValue) {
        BindBean bean = new BindBean();
        bean.bind_moduleID = moduleID;
        bean.bind_stat = downOrRelese;
        bean.keyValue = keyValue;
        Intent intent = new Intent(BindBroadCast.bind_action);
        Bundle data = new Bundle();
        data.putSerializable("bindBean", bean);
        intent.putExtras(data);
        MainApplication.getAppContext().sendBroadcast(intent);
    }
}
