package com.easyctrl.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
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
import com.easyctrl.ldy.domain.BindInfo;
import com.easyctrl.ldy.domain.ModulePortBean;
import com.easyctrl.ldy.net.Packet;
import com.easyctrl.manager.OrderManage;
import java.util.ArrayList;
import java.util.HashMap;

public class ModulePortAdapter extends BaseAdapter {
    private static ModulePortAdapter instance;
    private Activity activity;
    private ArrayList<ModulePortBean> beans;
    private Button deivce;
    private Button floor;
    private boolean isCheckVisiable = false;
    private LayoutInflater mInflater;
    private Button room;
    private ArrayList<ModulePortBean> selects;
    private HashMap<Integer, ModulePortBean> updateed;

    public class ViewHolder {
        public Button close;
        public TextView name;
        public Button open;
        public TextView percent;
        public TextView positionPortAndID;
        public SeekBar seekbar;
        public CheckBox select;
    }

    public void setFloor(Button floor) {
        this.floor = floor;
    }

    public void setRoom(Button room) {
        this.room = room;
    }

    public void setDeivce(Button deivce) {
        this.deivce = deivce;
    }

    public ArrayList<ModulePortBean> getSelects() {
        return this.selects;
    }

    public void setBeans(ArrayList<ModulePortBean> beans) {
        this.beans = beans;
    }

    public ArrayList<ModulePortBean> getBeans() {
        return this.beans;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    private ModulePortAdapter(Context context, ArrayList<ModulePortBean> beans) {
        this.mInflater = LayoutInflater.from(context);
        this.beans = beans;
        this.updateed = new HashMap();
        this.selects = new ArrayList();
    }

    public static synchronized ModulePortAdapter getInstance(Context context, ArrayList<ModulePortBean> beans) {
        ModulePortAdapter modulePortAdapter;
        synchronized (ModulePortAdapter.class) {
            if (instance == null) {
                instance = new ModulePortAdapter(context, beans);
            }
            modulePortAdapter = instance;
        }
        return modulePortAdapter;
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

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        ModulePortBean bean = (ModulePortBean) this.beans.get(position);
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = this.mInflater.inflate(R.layout.setting_center_item, null);
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
        } else if (MainApplication.modulePortManager.singleSelect(bean) == 1) {
            holder.name.setText((position + 1) + "\u3000" + bean.floor + " / " + bean.room + " / " + bean.name);
        } else {
            holder.name.setText((position + 1) + "\u3000" + bean.floor + " / " + bean.room + " / " + bean.name + " " + bean.paixu);
        }
        holder.positionPortAndID.setText("[" + bean.moduleID + "," + bean.port + "]");
        if (this.isCheckVisiable) {
            holder.select.setVisibility(8);
            holder.positionPortAndID.setVisibility(8);
        } else {
            holder.select.setVisibility(0);
            holder.positionPortAndID.setVisibility(0);
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
                    ModulePortAdapter.this.selects.add(bean);
                    ModulePortBean b = new ModulePortBean();
                    b.floor = bean.floor;
                    b.room = bean.room;
                    b.name = bean.name;
                    b.deviveType = bean.deviveType;
                    b.moduleID = bean.moduleID;
                    b.port = bean.port;
                    b.paixu = bean.paixu;
                    ModulePortAdapter.this.updateed.put(Integer.valueOf(bean.id), b);
                    if (ModulePortAdapter.this.floor != null) {
                        bean.floor = ModulePortAdapter.this.floor.getText().toString();
                    }
                    if (ModulePortAdapter.this.room != null) {
                        bean.room = ModulePortAdapter.this.room.getText().toString();
                    }
                    if (ModulePortAdapter.this.deivce != null) {
                        String deviceName = ModulePortAdapter.this.deivce.getText().toString();
                        bean.name = deviceName;
                        bean.deviveType = MainApplication.deviceManager.findTypeByName(deviceName);
                    }
                    bean.paixu = MainApplication.modulePortManager.getNumber(bean, 1);
                    MainApplication.modulePortManager.updatePosByModuleIDandPort(bean);
                    setBindInfo(bean);
                    ModulePortAdapter.this.notifyDataSetChanged();
                    return;
                }
                ModulePortBean temp = (ModulePortBean) ModulePortAdapter.this.updateed.get(Integer.valueOf(bean.id));
                bean.floor = temp.floor;
                bean.room = temp.room;
                bean.name = temp.name;
                bean.moduleID = temp.moduleID;
                bean.port = temp.port;
                bean.deviveType = temp.deviveType;
                bean.paixu = temp.paixu;
                MainApplication.modulePortManager.updatePosByModuleIDandPort(bean);
                setBindInfo(bean);
                ModulePortAdapter.this.selects.remove(bean);
                ModulePortAdapter.this.notifyDataSetChanged();
            }

            private void setBindInfo(ModulePortBean bean) {
                BindInfo bindInfo = new BindInfo();
                bindInfo.bind_module_J_id = bean.moduleID;
                bindInfo.bind_bindport = bean.port;
                bindInfo.floor = bean.floor;
                bindInfo.room = bean.room;
                bindInfo.device = bean.name;
                MainApplication.bindInfoManager.updateInfoByModulePort(bindInfo);
            }
        });
        holder.seekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            public void onStopTrackingTouch(SeekBar seekBar) {
                ModulePortBean bean = (ModulePortBean) seekBar.getTag();
                Packet in = new Packet();
                in.pack(OrderManage.aiming(bean));
                MainApplication.easySocket.send(in);
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
