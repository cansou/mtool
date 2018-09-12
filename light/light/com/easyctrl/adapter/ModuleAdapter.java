package com.easyctrl.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.easyctrl.ldy.activity.R;
import com.easyctrl.ldy.domain.ModuleBean;
import java.util.ArrayList;

public class ModuleAdapter extends BaseAdapter {
    private static ModuleAdapter instance;
    private ArrayList<ModuleBean> beans;
    private LayoutInflater mInflater;

    public class ViewHolder {
        public TextView device;
        public ImageView imageView;
        public TextView model;
        public TextView name;
        public TextView num;
    }

    public void setBeans(ArrayList<ModuleBean> beans) {
        this.beans = beans;
    }

    private ModuleAdapter(Context context, ArrayList<ModuleBean> beans) {
        this.beans = beans;
        this.mInflater = LayoutInflater.from(context);
    }

    public static ModuleAdapter getInstance(Context context, ArrayList<ModuleBean> beans) {
        if (instance == null) {
            instance = new ModuleAdapter(context, beans);
        }
        return instance;
    }

    public int getCount() {
        return this.beans == null ? 0 : this.beans.size();
    }

    public Object getItem(int position) {
        return this.beans.get(position);
    }

    public long getItemId(int position) {
        return (long) ((ModuleBean) this.beans.get(position)).id;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        ModuleBean bean = (ModuleBean) this.beans.get(position);
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = this.mInflater.inflate(R.layout.module_item, null);
            holder.num = (TextView) convertView.findViewById(R.id.num);
            holder.device = (TextView) convertView.findViewById(R.id.device);
            holder.model = (TextView) convertView.findViewById(R.id.model);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.imageView = (ImageView) convertView.findViewById(R.id.downImage);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (bean.modulePortNum > 2) {
            holder.imageView.setVisibility(0);
        } else {
            holder.imageView.setVisibility(4);
        }
        holder.num.setText((position + 1));
        holder.device.setText("\u8bbe\u5907 " + bean.moduleId);
        holder.model.setText(bean.moduleModel);
        holder.name.setText(bean.moduleNameExt);
        return convertView;
    }
}
