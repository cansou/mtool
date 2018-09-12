package com.easyctrl.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.easyctrl.ldy.activity.R;

public class TextSelectAdapter extends BaseAdapter {
    private String[] list;
    private Context mContext;

    private class ViewHolder {
        public TextView textView;

        private ViewHolder() {
        }
    }

    public TextSelectAdapter(Context context, String[] list) {
        this.list = list;
        this.mContext = context;
    }

    public int getCount() {
        return this.list == null ? 0 : this.list.length;
    }

    public Object getItem(int arg0) {
        return this.list[arg0];
    }

    public long getItemId(int arg0) {
        return 0;
    }

    public View getView(int position, View converVew, ViewGroup parent) {
        ViewHolder holder;
        String text = this.list[position];
        if (converVew == null) {
            holder = new ViewHolder();
            converVew = LayoutInflater.from(this.mContext).inflate(R.layout.text_select, parent, false);
            holder.textView = (TextView) converVew.findViewById(R.id.textSelect);
            converVew.setTag(holder);
        } else {
            holder = (ViewHolder) converVew.getTag();
        }
        holder.textView.setText(text);
        return converVew;
    }
}
