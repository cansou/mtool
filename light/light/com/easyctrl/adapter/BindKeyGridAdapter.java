package com.easyctrl.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.easyctrl.iface.OnTouchAction;
import com.easyctrl.ldy.activity.MainApplication;
import com.easyctrl.ldy.activity.R;
import com.easyctrl.ldy.domain.BindBean;
import java.util.ArrayList;

public class BindKeyGridAdapter extends BaseAdapter implements OnTouchListener {
    private static BindKeyGridAdapter adapter = null;
    private static final int longpress = 1;
    private ArrayList<BindBean> beans;
    private Handler handler = new Handler(new Callback() {
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    BindKeyGridAdapter.this.onTouchAction.longPress(BindKeyGridAdapter.this.v, BindKeyGridAdapter.this.position);
                    break;
            }
            return false;
        }
    });
    private boolean isDonw = false;
    private OnTouchAction onTouchAction;
    private int position;
    private long time;
    private long time_down;
    private long time_up;
    private View v;

    public class ViewHolder {
        public TextView bisoshi;
        public TextView model;
        public int position;
        public TextView textKey;
    }

    public void setOnTouchAction(OnTouchAction onTouchAction) {
        this.onTouchAction = onTouchAction;
    }

    private BindKeyGridAdapter(Context context, ArrayList<BindBean> beans) {
        this.beans = beans;
    }

    public static BindKeyGridAdapter getAdapter(Context context, ArrayList<BindBean> beans) {
        if (adapter == null) {
            adapter = new BindKeyGridAdapter(context, beans);
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
        return (long) ((BindBean) this.beans.get(position)).bindID;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        BindBean bindBean = (BindBean) this.beans.get(position);
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = MainApplication.mInflater.inflate(R.layout.bind_grid_key_item, parent, false);
            holder.textKey = (TextView) convertView.findViewById(R.id.textKey);
            holder.bisoshi = (TextView) convertView.findViewById(R.id.bisoshi);
            holder.model = (TextView) convertView.findViewById(R.id.model);
            convertView.setTag(holder);
            convertView.setOnTouchListener(this);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.position = position;
        holder.textKey.setText("\u6309\u952e " + bindBean.keyValue);
        if (bindBean.bind_stat == 1) {
            if (bindBean.bindModuleID == 0) {
                holder.bisoshi.setVisibility(4);
                holder.model.setVisibility(4);
            } else {
                holder.bisoshi.setVisibility(0);
                holder.model.setVisibility(0);
                holder.bisoshi.setText(getTager(bindBean));
                holder.model.setText(getModel(bindBean));
            }
            if (bindBean.type == 2) {
                holder.bisoshi.setVisibility(0);
                holder.model.setVisibility(0);
                holder.bisoshi.setText("\u573a\u666f" + bindBean.bind_name);
            } else {
                int i = bindBean.type;
            }
        } else {
            holder.bisoshi.setVisibility(4);
            holder.model.setVisibility(4);
        }
        return convertView;
    }

    private String getTager(BindBean bindBean) {
        String f = bindBean.floor;
        String r = bindBean.room;
        String d = bindBean.deviceName;
        if (f == null || f.length() == 0) {
            return "\u7ed1\u5b9a ID:" + bindBean.bind_moduleID + " PORT:" + bindBean.bindPort;
        }
        if (f.equals("\u8bbe\u5907")) {
            return "\u7ed1\u5b9a ID:" + bindBean.bind_moduleID + " PORT:" + bindBean.bindPort;
        }
        return new StringBuilder(String.valueOf(f)).append(" / ").append(r).append(" / ").append(d).toString();
    }

    private String getModel(BindBean bindBean) {
        return "\u6a21\u5f0f\uff1a" + bindBean.bind_model;
    }

    public void setBeans(ArrayList<BindBean> beans) {
        this.beans = beans;
    }

    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();
        if (action == 0) {
            int position = ((ViewHolder) v.getTag()).position;
            this.v = v;
            this.position = position;
            this.onTouchAction.onDown(v, position);
            this.time_down = System.currentTimeMillis();
            this.isDonw = true;
            MainApplication.threadPool.submit(new Runnable() {
                public void run() {
                    while (BindKeyGridAdapter.this.isDonw) {
                        try {
                            if ((System.currentTimeMillis() - BindKeyGridAdapter.this.time_down) / 1000 >= 4) {
                                BindKeyGridAdapter.this.handler.sendEmptyMessage(1);
                                BindKeyGridAdapter.this.isDonw = false;
                            }
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        } else if (action == 1) {
            this.onTouchAction.onUp(v, ((ViewHolder) v.getTag()).position);
            this.isDonw = false;
        }
        return true;
    }
}
