package com.easyctrl.fragment;

import android.content.Context;
import android.widget.ListAdapter;
import com.easyctrl.adapter.RoomAdapter;
import com.easyctrl.db.RoomManager;
import com.easyctrl.dialog.UpdateFRDDialog;
import com.easyctrl.dialog.UpdateFRDDialog.DialogSaveListener;
import com.easyctrl.impl.FloorRoomModuleListener;
import com.easyctrl.ldy.activity.MainApplication;
import com.easyctrl.ldy.domain.FloorBean;
import com.easyctrl.ldy.domain.RoomBean;
import com.easyctrl.ldy.domain.UserScene;
import java.util.ArrayList;
import java.util.Iterator;

public class RoomViewModel implements FloorRoomModuleListener {
    private RoomAdapter adapter;
    private ArrayList<RoomBean> beans;
    private Context context;
    private int currentFloorId;
    private UpdateFRDDialog dialog;
    private int floorNum;
    private FloorRoomFragment fragment;
    private RoomManager roomManager;

    public void onDestroy() {
        this.beans = null;
        this.fragment = null;
    }

    public RoomViewModel(Context context) {
        this.context = context;
        this.roomManager = RoomManager.getInstance(context);
        this.adapter = RoomAdapter.getInstance(context);
        this.dialog = new UpdateFRDDialog(context);
    }

    public ListAdapter onFloorRoomModuleListener() {
        this.beans = this.roomManager.findByFloorBeanID(this.fragment.getCurrentFloor().id);
        this.adapter.setBeans(this.beans);
        return this.adapter;
    }

    public FloorRoomFragment newInstance() {
        this.fragment = new FloorRoomFragment();
        this.fragment.setTitle("\u623f\u95f4\u7ba1\u7406");
        this.fragment.setOnFloorroomModuleListener(this);
        return this.fragment;
    }

    private void addfourScene(FloorBean floorBean, RoomBean roomBean) {
        ArrayList<UserScene> userScenes = new ArrayList();
        for (int i = 0; i < 4; i++) {
            UserScene scene = new UserScene();
            scene.floorID = floorBean.id;
            scene.roomID = roomBean.id;
            userScenes.add(scene);
        }
        MainApplication.userSceneManager.batchSave(userScenes);
    }

    public void addListener() {
        this.dialog.setTitle("\u6dfb\u52a0\u623f\u95f4");
        final FloorBean floorBean = this.fragment.getCurrentFloor();
        this.currentFloorId = floorBean.id;
        this.floorNum = this.roomManager.getNumber("\u623f\u95f4", 1, this.currentFloorId);
        this.dialog.setContent("\u623f\u95f4 " + this.floorNum);
        this.dialog.setOnSaveListener(new DialogSaveListener() {
            public void onSaveOrUpdate(String content) {
                RoomBean bean = new RoomBean();
                bean.name = content;
                bean.floorID = RoomViewModel.this.currentFloorId;
                bean.paixu = RoomViewModel.this.floorNum;
                bean.sname = "\u623f\u95f4";
                RoomViewModel.this.roomManager.add(bean);
                RoomViewModel.this.beans = RoomViewModel.this.roomManager.findByFloorBeanID(RoomViewModel.this.fragment.getCurrentFloor().id);
                RoomViewModel.this.adapter.setBeans(RoomViewModel.this.beans);
                RoomViewModel.this.fragment.setAdapter(RoomViewModel.this.adapter);
                RoomViewModel.this.addfourScene(floorBean, RoomViewModel.this.roomManager.findByName(bean.name, RoomViewModel.this.currentFloorId));
            }
        });
        this.dialog.show();
    }

    public void deleteListener() {
        ArrayList<RoomBean> beans = this.adapter.getDeletes();
        Iterator<RoomBean> it = this.adapter.getDeletes().iterator();
        while (it.hasNext()) {
            RoomBean bean = (RoomBean) it.next();
            this.roomManager.delete(bean.id);
            it.remove();
            beans.remove(bean);
        }
        this.adapter.setBeans(this.roomManager.findByFloorBeanID(this.fragment.getCurrentFloor().id));
        this.fragment.setAdapterByCurrent(this.adapter);
    }

    public void onSaveORUpdate(int position) {
        final RoomBean bean = (RoomBean) this.beans.get(position);
        this.dialog.setContent(bean.name);
        this.dialog.setOnSaveListener(new DialogSaveListener() {
            public void onSaveOrUpdate(String content) {
                String before = bean.name;
                bean.name = content;
                bean.sname = content;
                RoomViewModel.this.roomManager.update(bean, bean.id);
                MainApplication.modulePortManager.updateRoom(bean.name, before);
                RoomViewModel.this.beans = RoomViewModel.this.roomManager.findByFloorBeanID(RoomViewModel.this.fragment.getCurrentFloor().id);
                RoomViewModel.this.adapter.setBeans(RoomViewModel.this.beans);
                RoomViewModel.this.fragment.setAdapterByCurrent(RoomViewModel.this.adapter);
            }
        });
        this.dialog.show();
    }

    public void onCancel() {
    }

    public void onPause() {
    }
}
