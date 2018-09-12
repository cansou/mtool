package com.easyctrl.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import com.easyctrl.ldy.activity.MainApplication;
import com.easyctrl.ldy.activity.R;
import com.easyctrl.ldy.domain.UserScene;
import java.util.ArrayList;

public class UserSetAdapter extends BaseAdapter {
    public UserScene selectBean;
    private ArrayList<UserScene> userScenes;

    public class ViewHolder {
        public CheckBox check;
        public TextView info;
        public TextView model;
    }

    public UserSetAdapter(ArrayList<UserScene> userScenes) {
        this.userScenes = userScenes;
    }

    public UserScene getSelectBean() {
        return this.selectBean;
    }

    public void setUserScenes(ArrayList<UserScene> userScenes) {
        this.userScenes = userScenes;
    }

    public int getCount() {
        return this.userScenes == null ? 0 : this.userScenes.size();
    }

    public Object getItem(int position) {
        return this.userScenes.get(position);
    }

    public long getItemId(int position) {
        return (long) ((UserScene) this.userScenes.get(position)).userSceneID;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        boolean z;
        UserScene scene = (UserScene) this.userScenes.get(position);
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(MainApplication.mContext).inflate(R.layout.user_set_item, parent, false);
            holder.model = (TextView) convertView.findViewById(R.id.model);
            holder.info = (TextView) convertView.findViewById(R.id.info);
            holder.check = (CheckBox) convertView.findViewById(R.id.check);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.model.setText(getModel(scene));
        holder.info.setText(getInfo(scene));
        holder.check.setTag(scene);
        CheckBox checkBox = holder.check;
        if (this.selectBean == scene) {
            z = true;
        } else {
            z = false;
        }
        checkBox.setChecked(z);
        holder.check.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                UserSetAdapter.this.selectBean = (UserScene) v.getTag();
                UserSetAdapter.this.notifyDataSetChanged();
            }
        });
        return convertView;
    }

    private CharSequence getModel(UserScene scene) {
        if (scene.name == null || scene.name.length() == 0) {
            return "\u540d\u79f0 :\u65e0";
        }
        return "\u540d\u79f0 :" + scene.name;
    }

    private CharSequence getInfo(UserScene scene) {
        if (scene.sbindType == 1) {
            return "\u9762\u677f\uff1a " + scene.panelID + " \u6309\u952e\uff1a" + scene.panelkey;
        }
        if (scene.sbindType == 0) {
            return "   \u865a\u62df\u952e\uff1a" + scene.key;
        }
        return "";
    }
}
