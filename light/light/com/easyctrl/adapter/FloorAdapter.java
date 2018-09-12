package com.easyctrl.adapter;

import android.content.Context;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import com.easyctrl.adapter.FRMAdapter.ViewHolder;
import com.easyctrl.iface.FRMAdapterListener;
import com.easyctrl.ldy.domain.FloorBean;
import java.util.ArrayList;

public class FloorAdapter extends FRMAdapter<FloorBean> implements FRMAdapterListener {
    private static FloorAdapter instance;
    private ArrayList<FloorBean> deletes = new ArrayList();

    private FloorAdapter(Context context) {
        super(context);
        setAdapterListener(this);
    }

    public static synchronized FloorAdapter getInstance(Context context) {
        FloorAdapter floorAdapter;
        synchronized (FloorAdapter.class) {
            if (instance == null) {
                instance = new FloorAdapter(context);
            }
            floorAdapter = instance;
        }
        return floorAdapter;
    }

    public ArrayList<FloorBean> getDeletes() {
        return this.deletes;
    }

    public void onGetView(ViewHolder holder, int position, ArrayList beans) {
        FloorBean bean = (FloorBean) beans.get(position);
        holder.name.setTag(bean);
        holder.delete.setTag(bean);
        holder.delete.setChecked(this.deletes.contains(bean));
        holder.name.setText(bean.name);
        holder.num.setText((position + 1));
        if (bean.isUse == 1) {
            holder.delete.setVisibility(8);
            holder.use.setVisibility(0);
        } else {
            holder.delete.setVisibility(0);
            holder.use.setVisibility(8);
        }
        holder.delete.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                FloorBean bean = (FloorBean) buttonView.getTag();
                if (isChecked) {
                    FloorAdapter.this.deletes.add(bean);
                } else {
                    FloorAdapter.this.deletes.remove(bean);
                }
            }
        });
    }
}
