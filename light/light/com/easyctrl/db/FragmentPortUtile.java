package com.easyctrl.db;

import android.app.Activity;
import android.widget.ListAdapter;
import com.easyctrl.adapter.FloorAdapter;
import com.easyctrl.adapter.RoomAdapter;
import com.easyctrl.fragment.FloorRoomFragment;
import com.easyctrl.impl.FloorRoomModuleListener;
import com.easyctrl.ldy.domain.FloorBean;
import com.easyctrl.ldy.domain.RoomBean;
import java.util.ArrayList;

public class FragmentPortUtile {

    /* renamed from: com.easyctrl.db.FragmentPortUtile$1 */
    class AnonymousClass1 implements FloorRoomModuleListener {
        private final /* synthetic */ Activity val$activity;
        private final /* synthetic */ ArrayList val$beans;

        AnonymousClass1(Activity activity, ArrayList arrayList) {
            this.val$activity = activity;
            this.val$beans = arrayList;
        }

        public ListAdapter onFloorRoomModuleListener() {
            FloorAdapter adapter = FloorAdapter.getInstance(this.val$activity);
            adapter.setBeans(this.val$beans);
            return adapter;
        }

        public void addListener() {
        }

        public void deleteListener() {
        }

        public void onSaveORUpdate(int position) {
        }

        public void onCancel() {
        }

        public void onPause() {
        }

        public void onDestroy() {
        }
    }

    /* renamed from: com.easyctrl.db.FragmentPortUtile$2 */
    class AnonymousClass2 implements FloorRoomModuleListener {
        private final /* synthetic */ Activity val$activity;
        private final /* synthetic */ ArrayList val$beans;

        AnonymousClass2(Activity activity, ArrayList arrayList) {
            this.val$activity = activity;
            this.val$beans = arrayList;
        }

        public ListAdapter onFloorRoomModuleListener() {
            RoomAdapter adapter = RoomAdapter.getInstance(this.val$activity);
            adapter.setBeans(this.val$beans);
            return adapter;
        }

        public void addListener() {
        }

        public void deleteListener() {
        }

        public void onSaveORUpdate(int position) {
        }

        public void onCancel() {
        }

        public void onPause() {
        }

        public void onDestroy() {
        }
    }

    public static FloorRoomFragment toFloorRoomFragmentbyFloor(Activity activity, ArrayList<FloorBean> beans) {
        FloorRoomFragment frf = new FloorRoomFragment();
        frf.setOnFloorroomModuleListener(new AnonymousClass1(activity, beans));
        return frf;
    }

    public static FloorRoomFragment toFloorRoomFragmentbyRoom(Activity activity, ArrayList<RoomBean> beans) {
        FloorRoomFragment frf = new FloorRoomFragment();
        frf.setOnFloorroomModuleListener(new AnonymousClass2(activity, beans));
        return frf;
    }
}
