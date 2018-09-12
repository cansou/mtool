package com.easyctrl.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.easyctrl.ldy.activity.R;
import java.util.ArrayList;

public class VirtualMLIstAdapter extends BaseAdapter {
    private static VirtualMLIstAdapter adapter;
    private ArrayList<String> beans;
    private LayoutInflater mInflater;

    public class ViewHolder {
        public TextView strType;
    }

    private VirtualMLIstAdapter(Context context, ArrayList<String> beans) {
        this.mInflater = LayoutInflater.from(context);
        this.beans = beans;
    }

    public static VirtualMLIstAdapter getAdapter(Context context, ArrayList<String> beans) {
        if (adapter == null) {
            adapter = new VirtualMLIstAdapter(context, beans);
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
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        String type = (String) this.beans.get(position);
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = this.mInflater.inflate(R.layout.virtual_string_type_item, parent, false);
            holder.strType = (TextView) convertView.findViewById(R.id.strType);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.strType.setText(sprit(type));
        return convertView;
    }

    private String sprit(String type) {
        int start = (Integer.valueOf(type).intValue() * 10) + 1;
        return String.valueOf(start) + " ~ " + String.valueOf(start + 9);
    }
}
