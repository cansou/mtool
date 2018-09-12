package com.easyctrl.adapter;

import android.content.Context;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import com.easyctrl.adapter.FRMAdapter.ViewHolder;
import com.easyctrl.iface.FRMAdapterListener;
import com.easyctrl.ldy.domain.DeviceBean;
import java.util.ArrayList;

public class DeviceAdapter extends FRMAdapter<DeviceBean> implements FRMAdapterListener {
    private static DeviceAdapter instance;
    private ArrayList<DeviceBean> deletes = new ArrayList();

    private DeviceAdapter(Context context) {
        super(context);
        setAdapterListener(this);
    }

    public static synchronized DeviceAdapter getInstance(Context context) {
        DeviceAdapter deviceAdapter;
        synchronized (DeviceAdapter.class) {
            if (instance == null) {
                instance = new DeviceAdapter(context);
            }
            deviceAdapter = instance;
        }
        return deviceAdapter;
    }

    public ArrayList<DeviceBean> getDeletes() {
        return this.deletes;
    }

    public void onGetView(ViewHolder holder, int position, ArrayList beans) {
        DeviceBean bean = (DeviceBean) beans.get(position);
        holder.name.setTag(bean);
        holder.delete.setTag(bean);
        holder.delete.setChecked(this.deletes.contains(bean));
        holder.name.setText(bean.device_name);
        holder.num.setText((position + 1));
        if (bean.device_isSystem == 1) {
            holder.delete.setVisibility(8);
            holder.use.setVisibility(8);
        } else {
            holder.delete.setVisibility(0);
            holder.use.setVisibility(8);
        }
        holder.delete.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                DeviceBean bean = (DeviceBean) buttonView.getTag();
                if (isChecked) {
                    DeviceAdapter.this.deletes.add(bean);
                } else {
                    DeviceAdapter.this.deletes.remove(bean);
                }
            }
        });
    }
}
