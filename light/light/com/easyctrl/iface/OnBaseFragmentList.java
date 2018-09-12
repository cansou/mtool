package com.easyctrl.iface;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;

public interface OnBaseFragmentList {
    ListAdapter onAdapter();

    void onClick(View view);

    void onItemListviewClick(AdapterView<?> adapterView, View view, int i, long j);
}
