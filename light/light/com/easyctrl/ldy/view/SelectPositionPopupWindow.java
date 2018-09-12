package com.easyctrl.ldy.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.easyctrl.adapter.SelectTextAdapter;
import com.easyctrl.ldy.activity.R;
import com.easyctrl.ldy.util.Util;
import java.util.ArrayList;

public class SelectPositionPopupWindow {
    private SelectTextAdapter adapter;
    private View anchor;
    private int height = 200;
    private boolean isListenerFrooRoom;
    private ListView listView;
    private Context mContext;
    private LayoutInflater mInflater;
    private OnDefineListener onDefineListener;
    private PopupWindow popupWindow;
    private String selectString;
    private View view;
    private int width;

    public interface OnDefineListener {
        void onDefine(View view);

        void onRefush(String str, View view, View view2);
    }

    public void setListenerFrooRoom(boolean isListenerFrooRoom) {
        this.isListenerFrooRoom = isListenerFrooRoom;
    }

    public void setOnDefineListener(OnDefineListener onDefineListener) {
        this.onDefineListener = onDefineListener;
    }

    public void dismiss() {
        this.popupWindow.dismiss();
    }

    public SelectPositionPopupWindow(Context context) {
        this.mContext = context;
        this.popupWindow = new PopupWindow(context);
        this.mInflater = LayoutInflater.from(context);
        this.popupWindow.setFocusable(true);
        this.view = this.mInflater.inflate(R.layout.select_dialog_position, null);
        this.listView = (ListView) this.view.findViewById(R.id.listview);
        this.adapter = SelectTextAdapter.getInstance(this.mContext, null);
        this.listView.setAdapter(this.adapter);
        this.listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                SelectPositionPopupWindow.this.adapter.setSelectPosition(position);
                SelectPositionPopupWindow.this.adapter.setTouch(true);
                String content = ((TextView) view.findViewById(R.id.textname)).getText().toString();
                if (content.equals("\u81ea\u5b9a\u4e49")) {
                    SelectPositionPopupWindow.this.onDefineListener.onDefine(SelectPositionPopupWindow.this.anchor);
                    SelectPositionPopupWindow.this.dismiss();
                    return;
                }
                if (SelectPositionPopupWindow.this.anchor.getId() == R.id.floor && SelectPositionPopupWindow.this.onDefineListener != null) {
                    SelectPositionPopupWindow.this.onDefineListener.onRefush(content, SelectPositionPopupWindow.this.anchor, view);
                }
                if (SelectPositionPopupWindow.this.anchor.getId() == R.id.room && SelectPositionPopupWindow.this.onDefineListener != null) {
                    SelectPositionPopupWindow.this.onDefineListener.onRefush(content, SelectPositionPopupWindow.this.anchor, view);
                }
                if (SelectPositionPopupWindow.this.anchor.getId() != R.id.module) {
                    SelectPositionPopupWindow.this.setSelectString(content);
                    ((Button) SelectPositionPopupWindow.this.anchor).setText(SelectPositionPopupWindow.this.selectString);
                    SelectPositionPopupWindow.this.popupWindow.dismiss();
                    if (SelectPositionPopupWindow.this.isListenerFrooRoom) {
                        SelectPositionPopupWindow.this.onDefineListener.onRefush(content, SelectPositionPopupWindow.this.anchor, view);
                    }
                } else if (SelectPositionPopupWindow.this.onDefineListener != null) {
                    SelectPositionPopupWindow.this.onDefineListener.onRefush(content, SelectPositionPopupWindow.this.anchor, view);
                }
                SelectPositionPopupWindow.this.adapter.notifyDataSetChanged();
            }
        });
        this.popupWindow.setContentView(this.view);
    }

    public void show(View anchor, ArrayList<String> contents) {
        this.popupWindow.setHeight(Util.dip2px(this.mContext, (float) this.height));
        this.anchor = anchor;
        this.popupWindow.setWidth(anchor.getWidth());
        refurbish(contents);
        this.popupWindow.showAsDropDown(anchor);
    }

    public void showAsDevice(View anchor, ArrayList<String> contents) {
        this.popupWindow.setHeight(Util.dip2px(this.mContext, (float) this.height));
        this.anchor = anchor;
        this.popupWindow.setWidth(anchor.getWidth());
        refurbish(contents);
        this.popupWindow.showAsDropDown(anchor);
        this.adapter.setTouch(false);
    }

    public void showAsAnchor(View anchor, ArrayList<String> contents) {
        this.popupWindow.setHeight(Util.dip2px(this.mContext, (float) this.height));
        this.anchor = anchor;
        this.popupWindow.setWidth(anchor.getWidth());
        if (contents == null || contents.size() == 0) {
            if (contents != null) {
                contents.add("\u5168\u90e8");
            } else {
                contents = new ArrayList();
                contents.add("\u5168\u90e8");
            }
        }
        refurbish(contents);
        this.popupWindow.showAsDropDown(anchor);
        this.adapter.setTouch(false);
    }

    private void refurbish(ArrayList<String> content) {
        this.adapter.setContents(content);
        this.adapter.notifyDataSetChanged();
    }

    public void setView(View view) {
        this.view = view;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setSelectString(String selectString) {
        this.selectString = selectString;
    }
}
