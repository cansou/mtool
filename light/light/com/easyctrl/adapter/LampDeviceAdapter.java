package com.easyctrl.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.easyctrl.ldy.activity.R;
import java.util.ArrayList;

public class LampDeviceAdapter extends BaseAdapter {
    private static LampDeviceAdapter instance;
    private ArrayList<String> contents;
    private LayoutInflater mInflater;

    public class ViewHoleder {
        public TextView text;
    }

    private LampDeviceAdapter(Context context, ArrayList<String> contents) {
        this.contents = contents;
        this.mInflater = LayoutInflater.from(context);
    }

    public static LampDeviceAdapter getInstance(Context context, ArrayList<String> contents) {
        if (instance == null) {
            instance = new LampDeviceAdapter(context, contents);
        }
        return instance;
    }

    public int getCount() {
        return this.contents == null ? 0 : this.contents.size();
    }

    public Object getItem(int position) {
        return this.contents.get(position);
    }

    public long getItemId(int id) {
        return 0;
    }

    public View getView(int position, View contentView, ViewGroup parent) {
        ViewHoleder holder;
        String content = (String) this.contents.get(position);
        if (contentView == null) {
            holder = new ViewHoleder();
            contentView = this.mInflater.inflate(R.layout.select_string_dialog_item, parent, false);
            holder.text = (TextView) contentView.findViewById(R.id.textname);
            contentView.setTag(holder);
        } else {
            holder = (ViewHoleder) contentView.getTag();
        }
        holder.text.setText(content);
        return contentView;
    }

    public void setContents(ArrayList<String> contents) {
        this.contents = contents;
    }
}
