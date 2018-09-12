package com.easyctrl.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import com.easyctrl.impl.OnOpenAndCloseListener;
import com.easyctrl.ldy.activity.MainApplication;
import com.easyctrl.ldy.activity.R;
import com.easyctrl.ldy.activity.SettingActivity;
import com.easyctrl.ldy.activity.UserActivity;
import com.easyctrl.ldy.domain.ModulePortBean;
import com.easyctrl.manager.OrderManage;
import java.util.ArrayList;
import java.util.Iterator;

public class ModulePortAdapterSimple extends BaseAdapter {
    private static ModulePortAdapterSimple instance;
    private Activity activity;
    private ArrayList<ModulePortBean> beans;
    private boolean isCheckVisiable = false;
    public ArrayList<ModulePortBean> selects;

    public class ViewHolder {
        public Button close;
        public TextView name;
        public Button open;
        public TextView percent;
        public TextView positionPortAndID;
        public SeekBar seekbar;
        public CheckBox select;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public ArrayList<ModulePortBean> getSelects() {
        return this.selects;
    }

    public void setBeans(ArrayList<ModulePortBean> beans) {
        this.beans = beans;
    }

    public void setSelects(ArrayList<ModulePortBean> selects) {
        this.selects = selects;
    }

    public ArrayList<ModulePortBean> getBeans() {
        return this.beans;
    }

    private ModulePortAdapterSimple(Context context, ArrayList<ModulePortBean> beans) {
        this.beans = beans;
        this.selects = new ArrayList();
    }

    public static synchronized ModulePortAdapterSimple getInstance(Context context, ArrayList<ModulePortBean> beans) {
        ModulePortAdapterSimple modulePortAdapterSimple;
        synchronized (ModulePortAdapterSimple.class) {
            if (instance == null) {
                instance = new ModulePortAdapterSimple(context, beans);
            }
            modulePortAdapterSimple = instance;
        }
        return modulePortAdapterSimple;
    }

    public int getCount() {
        return this.beans == null ? 0 : this.beans.size();
    }

    public Object getItem(int position) {
        return this.beans.get(position);
    }

    public long getItemId(int position) {
        return (long) ((ModulePortBean) this.beans.get(position)).id;
    }

    public void deleteList(ArrayList<ModulePortBean> list) {
        Iterator<ModulePortBean> it = list.iterator();
        while (it.hasNext()) {
            ModulePortBean i = (ModulePortBean) it.next();
            it.remove();
            list.remove(i);
        }
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        ModulePortBean bean = null;
        try {
            bean = (ModulePortBean) this.beans.get(position);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = MainApplication.mInflater.inflate(R.layout.setting_center_item, null);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.close = (Button) convertView.findViewById(R.id.close);
            holder.open = (Button) convertView.findViewById(R.id.open);
            holder.positionPortAndID = (TextView) convertView.findViewById(R.id.positionPortAndID);
            holder.percent = (TextView) convertView.findViewById(R.id.percent);
            holder.seekbar = (SeekBar) convertView.findViewById(R.id.seekbar);
            holder.select = (CheckBox) convertView.findViewById(R.id.select);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.seekbar.setTag(bean);
        if (bean.type == 1) {
            holder.seekbar.setEnabled(true);
            holder.percent.setText(bean.progress);
            holder.seekbar.setThumbOffset(5);
            holder.percent.setVisibility(0);
        } else if (bean.type == 0) {
            holder.seekbar.setEnabled(false);
            holder.percent.setVisibility(4);
            holder.seekbar.setThumbOffset(1000);
        }
        if (bean.floor.equals("\u8bbe\u5907")) {
            holder.name.setText((position + 1) + "\u3000" + bean.floor + " " + bean.moduleID + " / " + bean.room + " " + bean.port);
        } else {
            holder.name.setText((position + 1) + "\u3000" + bean.floor + " / " + bean.room + " / " + bean.name + "  " + bean.paixu);
        }
        holder.positionPortAndID.setText("[" + bean.moduleID + "," + bean.port + "]");
        if (this.isCheckVisiable) {
            holder.select.setVisibility(8);
        } else {
            holder.select.setVisibility(0);
        }
        holder.seekbar.setProgress(bean.progress);
        holder.close.setOnClickListener(new OnOpenAndCloseListener(bean));
        holder.open.setOnClickListener(new OnOpenAndCloseListener(bean));
        holder.select.setTag(bean);
        holder.select.setChecked(this.selects.contains(bean));
        holder.select.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                CheckBox check = (CheckBox) v;
                ModulePortBean bean = (ModulePortBean) check.getTag();
                if (check.isChecked()) {
                    try {
                        ModulePortBean temp = (ModulePortBean) bean.clone();
                        if (!ModulePortAdapterSimple.this.selects.contains(temp)) {
                            ModulePortAdapterSimple.this.selects.add(temp);
                            return;
                        }
                        return;
                    } catch (Exception e) {
                        e.printStackTrace();
                        return;
                    }
                }
                ModulePortAdapterSimple.this.selects.remove(bean);
            }
        });
        holder.seekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            public void onStopTrackingTouch(SeekBar seekBar) {
                final ModulePortBean bean = (ModulePortBean) seekBar.getTag();
                new Thread(new Runnable() {
                    public void run() {
                        for (int i = 0; i < ModulePortAdapterSimple.this.selects.size(); i++) {
                            ModulePortBean portBean = (ModulePortBean) ModulePortAdapterSimple.this.selects.get(i);
                            if (portBean.id == bean.id) {
                                portBean.progress = bean.progress;
                            }
                        }
                    }
                }).start();
                if (ModulePortAdapterSimple.this.activity instanceof SettingActivity) {
                    ((SettingActivity) ModulePortAdapterSimple.this.activity).sendData(OrderManage.aiming(bean));
                } else if (ModulePortAdapterSimple.this.activity instanceof UserActivity) {
                    ((UserActivity) ModulePortAdapterSimple.this.activity).sendData(OrderManage.aiming(bean));
                }
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                ModulePortBean bean = (ModulePortBean) seekBar.getTag();
                bean.progress = progress;
                if (bean.type == 1) {
                    holder.percent.setText(bean.progress);
                } else if (bean.type == 0) {
                    holder.percent.setText("");
                }
            }
        });
        return convertView;
    }

    public void setCheckHind(boolean b) {
        this.isCheckVisiable = b;
    }
}
