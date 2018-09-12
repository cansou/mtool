package com.easyctrl.iface;

import android.view.View;

public interface OnTouchAction {
    void longPress(View view, int i);

    void onDown(View view, int i);

    void onUp(View view, int i);
}
