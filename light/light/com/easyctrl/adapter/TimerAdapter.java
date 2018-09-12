package com.easyctrl.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import com.easyctrl.ldy.activity.MainApplication;
import com.easyctrl.ldy.activity.R;
import com.easyctrl.ldy.domain.TimerBean;
import java.util.ArrayList;
import java.util.Iterator;

public class TimerAdapter extends BaseAdapter {
    private static TimerAdapter adapter;
    private ArrayList<TimerBean> beans;
    private LayoutInflater mInflater;
    public ArrayList<TimerBean> selects;
    public boolean showCheck = false;

    public class ViewHolder {
        public CheckBox check;
        public TextView descript;
        public TextView dinfo;
        public TextView name;
        public TextView num;
        public TextView time;
        public TextView week;
    }

    private TimerAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
        this.selects = new ArrayList();
    }

    public ArrayList<TimerBean> getSelects() {
        return this.selects;
    }

    public void deleteList(ArrayList<TimerBean> list) {
        Iterator<TimerBean> it = list.iterator();
        while (it.hasNext()) {
            TimerBean i = (TimerBean) it.next();
            it.remove();
            list.remove(i);
        }
    }

    public static TimerAdapter getAdapter(Context context) {
        if (adapter == null) {
            adapter = new TimerAdapter(context);
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
        return (long) ((TimerBean) this.beans.get(position)).timeID;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        TimerBean timerBean = (TimerBean) this.beans.get(position);
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = this.mInflater.inflate(R.layout.timer_item, parent, false);
            holder.num = (TextView) convertView.findViewById(R.id.num);
            holder.time = (TextView) convertView.findViewById(R.id.time);
            holder.week = (TextView) convertView.findViewById(R.id.week);
            holder.descript = (TextView) convertView.findViewById(R.id.descript);
            holder.check = (CheckBox) convertView.findViewById(R.id.check);
            holder.dinfo = (TextView) convertView.findViewById(R.id.dinfo);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.week.setText(timerBean.weekString);
        holder.check.setTag(timerBean);
        holder.num.setText(new StringBuilder(String.valueOf(position + 1)).toString());
        holder.time.setText(getTime(timerBean));
        holder.descript.setText(getDescription(timerBean));
        holder.dinfo.setText(getInfo(timerBean));
        holder.check.setChecked(this.selects.contains(timerBean));
        holder.name.setText(getArea(timerBean));
        holder.check.setTag(timerBean);
        if (this.showCheck) {
            holder.check.setVisibility(4);
        } else {
            holder.check.setVisibility(0);
        }
        holder.check.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                CheckBox check = (CheckBox) v;
                TimerBean timerBean = (TimerBean) check.getTag();
                if (check.isChecked()) {
                    try {
                        TimerAdapter.this.deleteList(TimerAdapter.this.selects);
                        TimerAdapter.this.selects.add(timerBean);
                        TimerAdapter.this.notifyDataSetChanged();
                        return;
                    } catch (Exception e) {
                        e.printStackTrace();
                        return;
                    }
                }
                TimerAdapter.this.selects.remove(timerBean);
            }
        });
        return convertView;
    }

    private String getDescription(TimerBean timerBean) {
        if (timerBean.description == null || timerBean.description.length() == 0) {
            return new StringBuilder(String.valueOf(MainApplication.mContext.getResources().getString(R.string.describe))).append("\u65e0").toString();
        }
        return "\u5907\u6ce8\uff1a" + timerBean.description;
    }

    private String getTime(TimerBean timerBean) {
        if (timerBean.hour > 0) {
            return "\u5b9a\u65f6 " + timerBean.tID + "  " + timerBean.hour + " : " + timerBean.min + " \u751f\u6548";
        }
        return "\u5b9a\u65f6 " + timerBean.tID;
    }

    private String getInfo(TimerBean timerBean) {
        if (timerBean.type == 2) {
            if (timerBean.moduleID > 0 && timerBean.isBind == 1) {
                if (timerBean.type == 2) {
                    return " KEY:" + timerBean.key + " \u573a\u666f\u53f7\uff1a" + timerBean.bind_info_sceneID;
                }
                return " KEY:" + timerBean.key + "  \u7ee7\u7535\u5668ID:" + timerBean.bindModuleID + " PORT:" + timerBean.bindPort;
            }
        } else if (timerBean.type == 1) {
            if (timerBean.isBind == 1) {
                if (timerBean.type == 2) {
                    return "\u865a\u62df\u952e  KEY:" + timerBean.key;
                }
                return "\u865a\u62df\u952e  KEY:" + timerBean.key + " \u573a\u666f\u53f7\uff1a" + timerBean.bind_info_sceneID;
            }
        } else if (timerBean.type == 3) {
            return "\u573a\u666f\u53f7\uff1a" + timerBean.bind_info_sceneID;
        }
        return "ID: \u65e0 KEY:\u65e0";
    }

    private String getArea(TimerBean timerBean) {
        if (timerBean.floor == null || timerBean.floor.equals("\u8bbe\u5907")) {
            return new StringBuilder(String.valueOf(MainApplication.mContext.getResources().getString(R.string.area))).append("\u65e0").toString();
        }
        return timerBean.floor + " / " + timerBean.room + " / " + timerBean.device;
    }

    public void setBeans(ArrayList<TimerBean> beans) {
        this.beans = beans;
    }
}
