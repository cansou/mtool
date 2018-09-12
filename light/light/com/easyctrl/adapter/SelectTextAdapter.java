package com.easyctrl.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.easyctrl.ldy.activity.MainApplication;
import com.easyctrl.ldy.activity.R;
import java.util.ArrayList;

public class SelectTextAdapter extends BaseAdapter {
    private static SelectTextAdapter instance;
    private LinearLayout buttonLayout;
    private ArrayList<String> contents;
    private ViewHoleder holder;
    private boolean isTouch;
    private LayoutInflater mInflater;
    private int selectPosition;
    private View view;

    public class ViewHoleder {
        public LinearLayout buttonLayout;
        public TextView text;
    }

    private SelectTextAdapter(Context context, ArrayList<String> contents) {
        this.contents = contents;
        this.mInflater = LayoutInflater.from(context);
    }

    public static SelectTextAdapter getInstance(Context context, ArrayList<String> contents) {
        if (instance == null) {
            instance = new SelectTextAdapter(context, contents);
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
        holder.text.setTag(holder);
        holder.text.setText(content);
        if (position == this.selectPosition && this.isTouch) {
            holder.text.setBackgroundColor(MainApplication.mContext.getResources().getColor(R.color.blue));
        } else {
            holder.text.setBackgroundColor(-1);
        }
        return contentView;
    }

    public void setContents(ArrayList<String> contents) {
        this.contents = contents;
    }

    public void setSelectPosition(int selectPosition) {
        this.selectPosition = selectPosition;
    }

    public void setTouch(boolean isTouch) {
        this.isTouch = isTouch;
    }
}
