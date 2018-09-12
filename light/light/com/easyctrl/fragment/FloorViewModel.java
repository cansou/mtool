package com.easyctrl.fragment;

import android.content.Context;
import android.widget.ListAdapter;
import com.easyctrl.adapter.FloorAdapter;
import com.easyctrl.db.FloorManager;
import com.easyctrl.dialog.UpdateFRDDialog;
import com.easyctrl.dialog.UpdateFRDDialog.DialogSaveListener;
import com.easyctrl.impl.FloorRoomModuleListener;
import com.easyctrl.ldy.activity.MainApplication;
import com.easyctrl.ldy.domain.FloorBean;
import com.easyctrl.ldy.domain.RoomBean;
import com.easyctrl.ldy.domain.UserScene;
import com.easyctrl.ui.base.BaseDialog.BaseDialogOnDismissListener;
import java.util.ArrayList;
import java.util.Iterator;

public class FloorViewModel implements FloorRoomModuleListener {
    private FloorAdapter adapter;
    private ArrayList<FloorBean> beans;
    private Context context;
    private UpdateFRDDialog dialog;
    private FloorManager floorManager;
    private int floorNum;
    private FloorRoomFragment fragment = null;

    public FloorViewModel(Context context) {
        this.context = context;
        this.floorManager = FloorManager.getInstance(context);
    }

    public FloorRoomFragment newInstance() {
        this.fragment = new FloorRoomFragment();
        this.fragment.setTitle("\u697c\u5c42\u7ba1\u7406");
        this.fragment.setOnFloorroomModuleListener(this);
        this.dialog = new UpdateFRDDialog(this.context);
        return this.fragment;
    }

    public ListAdapter onFloorRoomModuleListener() {
        this.beans = this.floorManager.findBeanAll();
        this.adapter = FloorAdapter.getInstance(this.context);
        this.adapter.setBeans(this.beans);
        return this.adapter;
    }

    public void addListener() {
        this.dialog.setTitle("\u6dfb\u52a0\u697c\u5c42");
        this.floorNum = this.floorManager.getNumber("\u697c", 1);
        this.dialog.setContent(this.floorNum + " " + "\u697c");
        this.dialog.setOnSaveListener(new DialogSaveListener() {
            public void onSaveOrUpdate(String content) {
                FloorBean bean = new FloorBean();
                bean.name = content;
                bean.paixu = FloorViewModel.this.floorNum;
                bean.sname = "\u697c";
                FloorViewModel.this.floorManager.add(bean);
                bean = FloorViewModel.this.floorManager.findByName(bean.name);
                RoomBean t = new RoomBean();
                t.name = "\u623f\u95f4 1";
                t.sname = "\u623f\u95f4";
                t.paixu = 1;
                t.floorID = bean.id;
                MainApplication.roomManager.add(t);
                FloorViewModel.this.beans = FloorViewModel.this.floorManager.findBeanAll();
                FloorViewModel.this.adapter.setBeans(FloorViewModel.this.beans);
                FloorViewModel.this.fragment.setAdapter(FloorViewModel.this.adapter);
                FloorViewModel.this.addfourScene(bean, MainApplication.roomManager.findByName(t.name, bean.id));
            }
        });
        this.dialog.show();
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

    public void deleteListener() {
        ArrayList<FloorBean> beans = this.adapter.getDeletes();
        Iterator<FloorBean> it = this.adapter.getDeletes().iterator();
        while (it.hasNext()) {
            FloorBean bean = (FloorBean) it.next();
            this.floorManager.delete(bean.id);
            it.remove();
            beans.remove(bean);
        }
        this.adapter.setBeans(this.floorManager.findBeanAll());
        this.fragment.setAdapterByCurrent(this.adapter);
    }

    public void onSaveORUpdate(int position) {
        final FloorBean bean = (FloorBean) this.beans.get(position);
        this.dialog.setContent(bean.name);
        this.dialog.setOnSaveListener(new DialogSaveListener() {
            public void onSaveOrUpdate(String content) {
                String before = bean.name;
                bean.name = content;
                bean.sname = content;
                FloorViewModel.this.floorManager.update(bean, bean.id);
                MainApplication.modulePortManager.updateFloor(bean.name, before);
                FloorViewModel.this.beans = FloorViewModel.this.floorManager.findBeanAll();
                FloorViewModel.this.adapter.setBeans(FloorViewModel.this.beans);
                FloorViewModel.this.fragment.setAdapterByCurrent(FloorViewModel.this.adapter);
            }
        });
        this.dialog.show();
    }

    public void onCancel() {
        this.dialog.setOnDismissListener(new BaseDialogOnDismissListener() {
            public void onDismiss() {
            }
        });
    }

    public void onDestroy() {
    }

    public void onPause() {
    }
}
