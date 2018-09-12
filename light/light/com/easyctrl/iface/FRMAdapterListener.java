package com.easyctrl.iface;

import com.easyctrl.adapter.FRMAdapter.ViewHolder;
import java.util.ArrayList;

public interface FRMAdapterListener<T> {
    void onGetView(ViewHolder viewHolder, int i, ArrayList<T> arrayList);
}
