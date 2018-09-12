package com.easyctrl.iface;

import android.view.View;
import android.widget.ListAdapter;

public interface BaseFragmentImpl {
    ListAdapter onAdapter();

    void onClickDevice(View view);

    void onClickFloor(View view);

    String onClickFourString();

    String onClickOneString();

    void onClickOperatorFour(View view);

    void onClickOperatorOne(View view);

    void onClickOpreatorThree(View view);

    void onClickOpreatorTwo(View view);

    void onClickRoom(View view);

    String onClickThreeString();

    String onClickTwoString();

    String onTitle();
}
