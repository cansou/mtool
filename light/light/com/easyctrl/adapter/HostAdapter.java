package com.easyctrl.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.easyctrl.ldy.activity.MainApplication;
import com.easyctrl.ldy.activity.R;
import com.easyctrl.ldy.domain.HostBean;
import java.util.ArrayList;

public class HostAdapter extends BaseAdapter {
    private ArrayList<HostBean> beans;

    public class ViewHolder {
        public TextView describe;
        public TextView host;
        public TextView num;
    }

    public void setBeans(ArrayList<HostBean> beans) {
        this.beans = beans;
    }

    public HostAdapter(ArrayList<HostBean> beans) {
        this.beans = beans;
    }

    public int getCount() {
        return this.beans == null ? 0 : this.beans.size();
    }

    public Object getItem(int position) {
        return this.beans.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        HostBean bean = (HostBean) this.beans.get(position);
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(MainApplication.mContext).inflate(R.layout.online_host_item, parent, false);
            holder.num = (TextView) convertView.findViewById(R.id.num);
            holder.describe = (TextView) convertView.findViewById(R.id.describe);
            holder.host = (TextView) convertView.findViewById(R.id.host);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.num.setText((position + 1));
        holder.describe.setText("\u4e3b\u673a" + (position + 1));
        holder.host.setText(bean.devIP);
        return convertView;
    }
}
