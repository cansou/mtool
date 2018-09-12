package com.easyctrl.adapter;

import android.content.Context;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import com.easyctrl.adapter.FRMAdapter.ViewHolder;
import com.easyctrl.iface.FRMAdapterListener;
import com.easyctrl.ldy.domain.RoomBean;
import java.util.ArrayList;

public class RoomAdapter extends FRMAdapter<RoomBean> implements FRMAdapterListener {
    private static RoomAdapter instance;
    private ArrayList<RoomBean> deletes = new ArrayList();

    private RoomAdapter(Context context) {
        super(context);
        setAdapterListener(this);
    }

    public ArrayList<RoomBean> getDeletes() {
        return this.deletes;
    }

    public static synchronized RoomAdapter getInstance(Context context) {
        RoomAdapter roomAdapter;
        synchronized (RoomAdapter.class) {
            if (instance == null) {
                instance = new RoomAdapter(context);
            }
            roomAdapter = instance;
        }
        return roomAdapter;
    }

    public void onGetView(ViewHolder holder, int position, ArrayList beans) {
        RoomBean bean = (RoomBean) beans.get(position);
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
                RoomBean bean = (RoomBean) buttonView.getTag();
                if (isChecked) {
                    RoomAdapter.this.deletes.add(bean);
                } else {
                    RoomAdapter.this.deletes.remove(bean);
                }
            }
        });
    }
}
