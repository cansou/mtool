package com.easyctrl.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.easyctrl.ldy.activity.R;
import com.easyctrl.ui.base.BaseEasyCtrlFragment;

public class EnptyFragment extends BaseEasyCtrlFragment {
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.enpty_view, null);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    protected void findViewByID(View view) {
    }

    protected void initWidget(View view) {
    }

    protected void onCreateInit() {
    }
}
