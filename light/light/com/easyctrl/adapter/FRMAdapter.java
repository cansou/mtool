package com.easyctrl.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import com.easyctrl.iface.FRMAdapterListener;
import com.easyctrl.ldy.activity.R;
import java.util.ArrayList;

public class FRMAdapter<T> extends BaseAdapter {
    private FRMAdapterListener adapterListener;
    private ArrayList<T> beans;
    private ArrayList<T> deletes = new ArrayList();
    private LayoutInflater mInflater;

    public class ViewHolder {
        public CheckBox delete;
        public TextView name;
        public TextView num;
        public TextView use;
    }

    public void setAdapterListener(FRMAdapterListener adapterListener) {
        this.adapterListener = adapterListener;
    }

    public ArrayList<T> getDeletes() {
        return this.deletes;
    }

    public void setBeans(ArrayList<T> beans) {
        this.beans = beans;
    }

    public FRMAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
    }

    public int getCount() {
        return this.beans == null ? 0 : this.beans.size();
    }

    public Object getItem(int position) {
        return this.beans.get(position);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = this.mInflater.inflate(R.layout.floor_room_item, parent, false);
            holder.delete = (CheckBox) convertView.findViewById(R.id.delete);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.num = (TextView) convertView.findViewById(R.id.num);
            holder.use = (TextView) convertView.findViewById(R.id.use);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        this.adapterListener.onGetView(holder, position, this.beans);
        return convertView;
    }
}
