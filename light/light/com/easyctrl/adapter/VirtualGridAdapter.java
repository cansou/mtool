package com.easyctrl.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.easyctrl.iface.OnTouchAction;
import com.easyctrl.ldy.activity.R;
import com.easyctrl.ldy.domain.VirtualBean;
import java.util.ArrayList;

public class VirtualGridAdapter extends BaseAdapter implements OnTouchListener {
    private static VirtualGridAdapter adapter;
    private ArrayList<VirtualBean> beans;
    private LayoutInflater mInflater;
    private OnTouchAction onTouchAction;
    private long time;
    private long time_down;
    private long time_up;

    public class ViewHolder {
        public TextView bindName;
        public TextView key;
        public TextView name;
        public int position;
    }

    private VirtualGridAdapter(Context context, ArrayList<VirtualBean> beans) {
        this.beans = beans;
        this.mInflater = LayoutInflater.from(context);
    }

    public void setOnTouchAction(OnTouchAction onTouchAction) {
        this.onTouchAction = onTouchAction;
    }

    public static VirtualGridAdapter getAdapter(Context context, ArrayList<VirtualBean> beans) {
        if (adapter == null) {
            adapter = new VirtualGridAdapter(context, beans);
        }
        return adapter;
    }

    public int getCount() {
        return this.beans == null ? 0 : this.beans.size();
    }

    public Object getItem(int position) {
        return this.beans.get(position);
    }

    public long getItemId(int position) {
        return (long) ((VirtualBean) this.beans.get(position)).virtualID;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        VirtualBean bean = (VirtualBean) this.beans.get(position);
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = this.mInflater.inflate(R.layout.virtual_grid_item, parent, false);
            holder.key = (TextView) convertView.findViewById(R.id.key);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.bindName = (TextView) convertView.findViewById(R.id.bindName);
            convertView.setTag(holder);
            convertView.setOnTouchListener(this);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.key.setText(getBeanKey(bean));
        holder.position = position;
        if (bean.bindType == 0) {
            holder.name.setVisibility(4);
            holder.bindName.setVisibility(4);
        } else if (bean.bindType == 2) {
            holder.name.setVisibility(0);
            holder.bindName.setVisibility(0);
            holder.name.setText(bean.name);
            holder.bindName.setText(getScene(bean));
        } else if (bean.bindType == 1) {
            holder.name.setVisibility(0);
            holder.bindName.setVisibility(0);
            holder.name.setText(getBindInfo(bean));
            holder.bindName.setText(getModel(bean));
        }
        return convertView;
    }

    private String getBeanKey(VirtualBean bean) {
        return "\u6309\u952e " + String.valueOf(bean.key);
    }

    private String getModel(VirtualBean bean) {
        if (bean.model == null) {
            return "";
        }
        return "\u6a21\u5f0f\uff1a" + bean.model;
    }

    private String getScene(VirtualBean bean) {
        return "\u573a\u666f" + bean.virtualID;
    }

    private String getBindInfo(VirtualBean bean) {
        String f = bean.floor;
        String r = bean.room;
        String d = bean.deviceName;
        if (f == null || f.length() == 0) {
            return "\u7ed1\u5b9a ID:" + bean.moduleID + " PORT:" + bean.port;
        }
        if (f.equals("\u8bbe\u5907")) {
            return "\u7ed1\u5b9a ID:" + bean.moduleID + " PORT:" + bean.port;
        }
        return new StringBuilder(String.valueOf(f)).append(" / ").append(r).append(" / ").append(d).toString();
    }

    private String getName(VirtualBean bean) {
        String f = bean.floor;
        String r = bean.room;
        String d = bean.deviceName;
        if (f == null || f.equals("\u8bbe\u5907")) {
            return null;
        }
        return new StringBuilder(String.valueOf(f)).append(" / ").append(r).append(" / ").append(d).toString();
    }

    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();
        if (action == 0) {
            this.onTouchAction.onDown(v, ((ViewHolder) v.getTag()).position);
            this.time_down = System.currentTimeMillis();
        } else if (action == 1) {
            int position = ((ViewHolder) v.getTag()).position;
            this.onTouchAction.onUp(v, position);
            this.time_up = System.currentTimeMillis();
            this.time = this.time_up - this.time_down;
            this.time /= 1000;
            if (this.time >= 4) {
                this.onTouchAction.longPress(v, position);
            }
        } else if (action == 3) {
            Log.i("data", "ACTION_CANCEL");
        }
        return true;
    }

    public void setBeans(ArrayList<VirtualBean> beans) {
        this.beans = beans;
    }
}
