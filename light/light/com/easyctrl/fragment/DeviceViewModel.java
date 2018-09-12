package com.easyctrl.fragment;

import android.content.Context;
import android.widget.ListAdapter;
import com.easyctrl.adapter.DeviceAdapter;
import com.easyctrl.db.DeviceManager;
import com.easyctrl.dialog.UpdateFRDDialog;
import com.easyctrl.dialog.UpdateFRDDialog.DialogSaveListener;
import com.easyctrl.impl.FloorRoomModuleListener;
import com.easyctrl.ldy.domain.DeviceBean;
import java.util.ArrayList;
import java.util.Iterator;

public class DeviceViewModel implements FloorRoomModuleListener {
    private DeviceAdapter adapter;
    private ArrayList<DeviceBean> beans;
    private Context context;
    private DeviceManager deviceManager;
    private UpdateFRDDialog dialog;
    private FloorRoomFragment fragment;

    public DeviceViewModel(Context context) {
        this.context = context;
        this.deviceManager = DeviceManager.getInstance(context);
        this.adapter = DeviceAdapter.getInstance(context);
        this.dialog = new UpdateFRDDialog(context);
    }

    public void onDestroy() {
        this.fragment = null;
        this.beans = null;
        this.deviceManager = null;
        this.adapter = null;
        this.context = null;
    }

    public ListAdapter onFloorRoomModuleListener() {
        this.beans = this.deviceManager.findByTypeToObj(this.fragment.getType());
        this.adapter.setBeans(this.beans);
        return this.adapter;
    }

    public void addListener() {
        this.dialog.setTitle("\u6dfb\u52a0\u8bbe\u5907\u540d\u79f0");
        this.dialog.setHint("\u8bf7\u8f93\u5165\u8bbe\u5907\u540d\u79f0");
        this.dialog.setOnSaveListener(new DialogSaveListener() {
            public void onSaveOrUpdate(String content) {
                DeviceBean bean = new DeviceBean();
                bean.device_name = content;
                bean.device_isSystem = 2;
                bean.device_type = DeviceViewModel.this.fragment.getType();
                DeviceViewModel.this.deviceManager.add(bean);
                DeviceViewModel.this.beans = DeviceViewModel.this.deviceManager.findByTypeToObj(DeviceViewModel.this.fragment.getType());
                DeviceViewModel.this.adapter.setBeans(DeviceViewModel.this.beans);
                DeviceViewModel.this.fragment.setAdapter(DeviceViewModel.this.adapter);
            }
        });
        this.dialog.show();
    }

    public void deleteListener() {
        ArrayList<DeviceBean> beans = this.adapter.getDeletes();
        Iterator<DeviceBean> it = this.adapter.getDeletes().iterator();
        while (it.hasNext()) {
            DeviceBean bean = (DeviceBean) it.next();
            this.deviceManager.delete(bean.deviceID);
            it.remove();
            beans.remove(bean);
        }
        this.adapter.setBeans(this.deviceManager.findByTypeToObj(this.fragment.getType()));
        this.fragment.setAdapterByCurrent(this.adapter);
    }

    public void onSaveORUpdate(int position) {
        final DeviceBean bean = (DeviceBean) this.beans.get(position);
        this.dialog.setContent(bean.device_name);
        this.dialog.setOnSaveListener(new DialogSaveListener() {
            public void onSaveOrUpdate(String content) {
                bean.device_name = content;
                bean.device_isSystem = 2;
                bean.device_type = DeviceViewModel.this.fragment.getType();
                DeviceViewModel.this.deviceManager.update(bean, bean.deviceID);
                DeviceViewModel.this.beans = DeviceViewModel.this.deviceManager.findByTypeToObj(DeviceViewModel.this.fragment.getType());
                DeviceViewModel.this.adapter.setBeans(DeviceViewModel.this.beans);
                DeviceViewModel.this.fragment.setAdapterByCurrent(DeviceViewModel.this.adapter);
            }
        });
        this.dialog.show();
    }

    public void onCancel() {
    }

    public FloorRoomFragment newInstance() {
        this.fragment = new FloorRoomFragment();
        this.fragment.setOnFloorroomModuleListener(this);
        this.fragment.setTitle("\u8bbe\u5907\u7ba1\u7406");
        return this.fragment;
    }

    public void onPause() {
    }
}
