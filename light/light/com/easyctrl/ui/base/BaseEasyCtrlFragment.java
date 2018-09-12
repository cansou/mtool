package com.easyctrl.ui.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ListView;

public abstract class BaseEasyCtrlFragment extends Fragment {
    public ListView listView;

    protected abstract void findViewByID(View view);

    protected abstract void initWidget(View view);

    protected abstract void onCreateInit();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
