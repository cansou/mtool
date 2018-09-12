package com.easyctrl.dialog;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.PopupWindow;
import com.easyctrl.adapter.LampDeviceAdapter;
import com.easyctrl.ldy.activity.R;
import com.easyctrl.ldy.util.Util;
import com.easyctrl.ldy.view.SelectPositionPopupWindow;
import java.util.ArrayList;

public class DevicePopupWindow {
    private LampDeviceAdapter adapter;
    private ArrayList<String> content;
    private int height = 200;
    private ListView listView;
    private Context mContext;
    private OnListItemListener onListItemListener;
    private PopupWindow popupWindow;
    private SelectPositionPopupWindow positionPopupWindow;
    private View view;

    public interface OnListItemListener {
        void onItem(String str);
    }

    public void setOnListItemListener(OnListItemListener onListItemListener) {
        this.onListItemListener = onListItemListener;
    }

    public DevicePopupWindow(Context context) {
        this.mContext = context;
        this.popupWindow = new PopupWindow(context, null, R.style.BaseDialog);
        this.view = LayoutInflater.from(context).inflate(R.layout.ex_device_listview, null);
        this.listView = (ListView) this.view.findViewById(R.id.listview);
        this.popupWindow.setContentView(this.view);
        this.popupWindow.setFocusable(true);
        this.popupWindow.setBackgroundDrawable(new BitmapDrawable());
        this.adapter = LampDeviceAdapter.getInstance(context, this.content);
        this.adapter.setContents(this.content);
        this.adapter.notifyDataSetChanged();
        this.listView.setAdapter(this.adapter);
        this.listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String string = (String) DevicePopupWindow.this.content.get(position);
                if (DevicePopupWindow.this.onListItemListener != null) {
                    DevicePopupWindow.this.onListItemListener.onItem(string);
                    DevicePopupWindow.this.dimiss();
                    DevicePopupWindow.this.positionPopupWindow.dismiss();
                }
            }
        });
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void dimiss() {
        this.popupWindow.dismiss();
    }

    public void setContentValue(ArrayList<String> content) {
        if (content == null) {
            content = new ArrayList();
            content.add("\u81ea\u5b9a\u4e49");
        } else {
            content.add("\u81ea\u5b9a\u4e49");
        }
        this.content = content;
    }

    public void showAsAnchor(View anchor, int xoff, int yoff) {
        this.adapter.setContents(this.content);
        this.adapter.notifyDataSetChanged();
        this.popupWindow.setWidth(anchor.getWidth());
        this.popupWindow.setHeight(Util.dip2px(this.mContext, (float) this.height));
        this.popupWindow.showAsDropDown(anchor, Integer.valueOf("-" + anchor.getWidth()).intValue(), Util.dip2px(this.mContext, (float) yoff));
    }

    public void show(View parent, int x, int y) {
        this.popupWindow.showAtLocation(parent, 0, x, y);
    }

    public void setSelectWindow(SelectPositionPopupWindow positionPopupWindow) {
        this.positionPopupWindow = positionPopupWindow;
    }
}
