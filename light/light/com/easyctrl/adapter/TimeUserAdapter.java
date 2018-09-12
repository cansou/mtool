package com.easyctrl.adapter;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.easyctrl.ldy.activity.MainApplication;
import com.easyctrl.ldy.activity.R;
import com.easyctrl.ldy.domain.TimerBean;
import com.easyctrl.ldy.net.Packet;
import com.easyctrl.ldy.view.SwitchView;
import com.easyctrl.ldy.view.SwitchView.OnSwitchChangeListener;
import com.easyctrl.manager.OrderManage;
import java.util.ArrayList;

public class TimeUserAdapter extends BaseAdapter {
    private static TimeUserAdapter timeUserAdapter;
    private ArrayList<TimerBean> beans;

    public class ViewHolder {
        public LinearLayout changeLayout;
        public TextView date;
        public TextView desprt;
        public SwitchView switchButton;
        public TextView time;
    }

    private TimeUserAdapter(Context context) {
    }

    public void setBeans(ArrayList<TimerBean> beans) {
        this.beans = beans;
    }

    public static TimeUserAdapter getInstance(Context context) {
        if (timeUserAdapter == null) {
            timeUserAdapter = new TimeUserAdapter(context);
        }
        return timeUserAdapter;
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
        boolean z;
        TimerBean bean = (TimerBean) this.beans.get(position);
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = MainApplication.mInflater.inflate(R.layout.time_list_user_item, parent, false);
            holder.time = (TextView) convertView.findViewById(R.id.time);
            holder.date = (TextView) convertView.findViewById(R.id.date);
            holder.desprt = (TextView) convertView.findViewById(R.id.desprt);
            holder.switchButton = (SwitchView) convertView.findViewById(R.id.switchbutton);
            holder.changeLayout = (LinearLayout) convertView.findViewById(R.id.changeLayout);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.time.setText(Html.fromHtml(getTime(bean)));
        holder.date.setText(bean.weekString);
        holder.desprt.setText(getDesprt(bean));
        holder.switchButton.setTag(bean);
        TimerBean timebean = (TimerBean) holder.switchButton.getTag();
        SwitchView switchView = holder.switchButton;
        if (timebean.isOpen == 1) {
            z = true;
        } else {
            z = false;
        }
        switchView.setSwitchStatus(z);
        holder.switchButton.setOnSwitchChangeListener(new OnSwitchChangeListener() {
            public void onSwitchChanged(boolean isChecked) {
                TimerBean bean = (TimerBean) holder.switchButton.getTag();
                if (isChecked) {
                    bean.isOpen = 1;
                    MainApplication.timerManager.updateOpenType(bean.timeID, 1);
                    TimeUserAdapter.this.sendData(OrderManage.time_order(bean.timeID, (byte) 1));
                    TimeUserAdapter.this.sendData(OrderManage.searchAllTimer());
                    return;
                }
                bean.isOpen = 0;
                MainApplication.timerManager.updateOpenType(bean.timeID, 0);
                TimeUserAdapter.this.sendData(OrderManage.time_order(bean.timeID, (byte) 0));
                TimeUserAdapter.this.sendData(OrderManage.searchAllTimer());
            }
        });
        holder.changeLayout.setTag(bean);
        holder.changeLayout.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                TimerBean bean = (TimerBean) holder.changeLayout.getTag();
                if (holder.switchButton.getSwitchStatus()) {
                    holder.switchButton.setSwitchStatus(false);
                    bean.isOpen = 0;
                    MainApplication.timerManager.updateOpen(bean);
                    TimeUserAdapter.this.sendData(OrderManage.time_order(bean.timeID, (byte) 0));
                    return;
                }
                holder.switchButton.setSwitchStatus(true);
                bean.isOpen = 1;
                MainApplication.timerManager.updateOpen(bean);
                TimeUserAdapter.this.sendData(OrderManage.time_order(bean.timeID, (byte) 1));
            }
        });
        return convertView;
    }

    private void sendData(byte[] data) {
        Packet in = new Packet();
        in.pack(data);
        MainApplication.easySocket.send(in);
    }

    private String getDesprt(TimerBean bean) {
        if (bean.description == null || bean.description.length() < 1) {
            return "\u5907\u6ce8";
        }
        return "\u5907\u6ce8\uff1a" + bean.description;
    }

    private String getTime(TimerBean bean) {
        if (bean.hour == 0 && bean.min == 0) {
            return "\u5b9a\u65f6 " + bean.tID;
        }
        int min = bean.min;
        String strMin = "";
        if (min < 10) {
            strMin = "0" + min;
        } else {
            strMin = min;
        }
        return "\u5b9a\u65f6 " + bean.tID + "   <span>&nbsp;&nbsp;&nbsp;</span> <font color=#7284C3>" + bean.hour + " : " + strMin + "</font>";
    }
}
