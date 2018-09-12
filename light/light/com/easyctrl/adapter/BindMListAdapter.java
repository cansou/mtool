package com.easyctrl.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.easyctrl.ldy.activity.MainApplication;
import com.easyctrl.ldy.activity.R;
import com.easyctrl.ldy.domain.ModuleBean;
import com.easyctrl.ldy.util.Value;
import java.util.ArrayList;

public class BindMListAdapter extends BaseAdapter {
    private static BindMListAdapter adapter;
    private ArrayList<ModuleBean> moduls;

    public class ViewHolder {
        public TextView keyNum;
        public TextView mianban;
        public TextView model;
        public TextView num;
    }

    private BindMListAdapter(Context context, ArrayList<ModuleBean> moduls) {
        this.moduls = moduls;
    }

    public void setModuls(ArrayList<ModuleBean> moduls) {
        this.moduls = moduls;
    }

    public static BindMListAdapter getAdapter(Context context, ArrayList<ModuleBean> moduls) {
        if (adapter == null) {
            adapter = new BindMListAdapter(context, moduls);
        }
        return adapter;
    }

    public int getCount() {
        return this.moduls == null ? 0 : this.moduls.size();
    }

    public Object getItem(int position) {
        return this.moduls.get(position);
    }

    public long getItemId(int position) {
        return (long) ((ModuleBean) this.moduls.get(position)).id;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        ModuleBean bean = (ModuleBean) this.moduls.get(position);
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = MainApplication.mInflater.inflate(R.layout.bindm_list_item, parent, false);
            holder.num = (TextView) convertView.findViewById(R.id.num);
            holder.mianban = (TextView) convertView.findViewById(R.id.mianban);
            holder.model = (TextView) convertView.findViewById(R.id.model);
            holder.keyNum = (TextView) convertView.findViewById(R.id.keyNum);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.num.setText((position + 1));
        holder.mianban.setText("\u9762\u677f " + bean.moduleId);
        holder.model.setText(bean.moduleModel);
        if (bean.moduleModel.equals(Value.MODEL_MI0808A)) {
            holder.keyNum.setText(MainApplication.mContext.getResources().getString(R.string.eightMB));
        } else if (bean.moduleModel.equals(Value.MODEL_KP06B)) {
            holder.keyNum.setText(MainApplication.mContext.getResources().getString(R.string.sixMB));
        }
        return convertView;
    }
}
