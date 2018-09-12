package com.easyctrl.ldy.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import com.easyctrl.db.DeviceManager;
import com.easyctrl.db.FloorManager;
import com.easyctrl.db.RoomManager;
import com.easyctrl.ldy.activity.R;
import com.easyctrl.ldy.domain.DeviceBean;
import com.easyctrl.ldy.domain.FloorBean;
import com.easyctrl.ldy.domain.RoomBean;
import java.util.ArrayList;

public class BottomPositionView extends LinearLayout {
    private Button btnFloor = ((Button) this.layout.findViewById(R.id.floor));
    private Button btnModule = ((Button) this.layout.findViewById(R.id.module));
    private Button btnRoom = ((Button) this.layout.findViewById(R.id.room));
    private DeviceManager deviceManager;
    private FloorManager floorManager;
    private LinearLayout layout = ((LinearLayout) this.mInflater.inflate(R.layout.bottom_position_select, this));
    private LayoutInflater mInflater = LayoutInflater.from(getContext());
    private SelectPositionPopupWindow popupWindow;
    private RoomManager roomManager;

    private class BottomOnClickListener implements OnClickListener {
        private BottomOnClickListener() {
        }

        public void onClick(View v) {
            if (R.id.floor == v.getId()) {
                BottomPositionView.this.popupWindow.show(v, BottomPositionView.this.getContentByFloor(BottomPositionView.this.floorManager.findAll()));
            } else if (R.id.room == v.getId()) {
                BottomPositionView.this.popupWindow.show(v, BottomPositionView.this.getContentByRoom(BottomPositionView.this.roomManager.findAll()));
            } else if (R.id.module == v.getId()) {
                BottomPositionView.this.popupWindow.show(v, BottomPositionView.this.getContentByDevice(BottomPositionView.this.deviceManager.findAll()));
            }
        }
    }

    public SelectPositionPopupWindow getPopupWindow() {
        return this.popupWindow;
    }

    public BottomPositionView(Context context) {
        super(context);
        this.floorManager = FloorManager.getInstance(context);
        this.roomManager = RoomManager.getInstance(context);
        this.deviceManager = DeviceManager.getInstance(context);
        this.btnFloor.setOnClickListener(new BottomOnClickListener());
        this.btnRoom.setOnClickListener(new BottomOnClickListener());
        this.btnModule.setOnClickListener(new BottomOnClickListener());
        this.popupWindow = new SelectPositionPopupWindow(context);
    }

    public ArrayList<String> getContentByFloor(ArrayList<FloorBean> list) {
        ArrayList<String> arrs = new ArrayList();
        for (int i = 0; i < list.size(); i++) {
            arrs.add(((FloorBean) list.get(i)).name);
        }
        return arrs;
    }

    public ArrayList<String> getContentByRoom(ArrayList<RoomBean> list) {
        ArrayList<String> arrs = new ArrayList();
        for (int i = 0; i < list.size(); i++) {
            arrs.add(((RoomBean) list.get(i)).name);
        }
        return arrs;
    }

    public ArrayList<String> getContentByDevice(ArrayList<DeviceBean> list) {
        ArrayList<String> arrs = new ArrayList();
        for (int i = 0; i < list.size(); i++) {
            arrs.add(((DeviceBean) list.get(i)).device_name);
        }
        return arrs;
    }
}
