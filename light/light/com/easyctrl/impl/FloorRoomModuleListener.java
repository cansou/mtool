package com.easyctrl.impl;

import android.widget.ListAdapter;

public interface FloorRoomModuleListener {
    void addListener();

    void deleteListener();

    void onCancel();

    void onDestroy();

    ListAdapter onFloorRoomModuleListener();

    void onPause();

    void onSaveORUpdate(int i);
}
