package com.easyctrl.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.easyctrl.iface.BaseFragmentImpl;
import com.easyctrl.ldy.activity.R;
import com.easyctrl.ldy.activity.SettingActivity;
import com.easyctrl.ldy.activity.UserActivity;
import com.easyctrl.ldy.view.BackView;
import com.easyctrl.ui.base.BaseEasyCtrlFragment;

public class BaseFragment extends BaseEasyCtrlFragment implements OnClickListener {
    private BackView backView;
    private View barView;
    private Button btnDevice;
    private Button btnFloor;
    private Button btnRoom;
    private boolean dimissBar;
    private BaseFragmentImpl fragmentListener;
    public ListView listview;
    private Button menuButton;
    private Button operator_four;
    private Button operator_one;
    private Button operator_three;
    private Button operator_two;
    public SettingActivity settingActivity;
    private String title;
    private TextView titleView;
    public UserActivity userActivity;

    public void setTitle(String title) {
        this.title = title;
    }

    public void setViewTile(String title) {
        if (this.titleView != null) {
            this.titleView.setText(title);
        }
    }

    public void setEnabledOne(boolean isEnable) {
        if (this.operator_one != null) {
            this.operator_one.setEnabled(isEnable);
        }
    }

    public void setEnabledTwo(boolean isEnable) {
        if (this.operator_two != null) {
            this.operator_two.setEnabled(isEnable);
        }
    }

    public void setFragmentListener(BaseFragmentImpl fragmentListener) {
        this.fragmentListener = fragmentListener;
    }

    public void setButtonFloorString(String str) {
        if (this.btnFloor != null) {
            this.btnFloor.setText(str);
        }
    }

    public void setButtonRoomString(String str) {
        if (this.btnRoom != null) {
            this.btnRoom.setText(str);
        }
    }

    public void setButtonDeviceString(String str) {
        if (this.btnDevice != null) {
            this.btnDevice.setText(str);
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onCreateInit();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.base_fragment_layout, null);
        findViewByID(view);
        initWidget(view);
        return view;
    }

    public void setDimissBar(boolean dimissBar) {
        this.dimissBar = dimissBar;
    }

    public void onClick(View v) {
        if (R.id.operator_one == v.getId()) {
            this.fragmentListener.onClickOperatorOne(v);
        } else if (R.id.operator_two == v.getId()) {
            this.fragmentListener.onClickOpreatorTwo(v);
        } else if (R.id.operator_three == v.getId()) {
            this.fragmentListener.onClickOpreatorThree(v);
        } else if (R.id.operator_four == v.getId()) {
            this.fragmentListener.onClickOperatorFour(v);
        } else if (R.id.floor == v.getId()) {
            this.fragmentListener.onClickFloor(v);
        } else if (R.id.room == v.getId()) {
            this.fragmentListener.onClickRoom(v);
        } else if (R.id.module == v.getId()) {
            this.fragmentListener.onClickDevice(v);
        } else if (R.id.leftButton == v.getId()) {
            ((SettingActivity) getActivity()).showLeft();
        }
    }

    protected void findViewByID(View view) {
        this.menuButton = (Button) view.findViewById(R.id.leftButton);
        this.menuButton.setOnClickListener(this);
        this.operator_one = (Button) view.findViewById(R.id.operator_one);
        this.operator_two = (Button) view.findViewById(R.id.operator_two);
        this.operator_three = (Button) view.findViewById(R.id.operator_three);
        this.operator_four = (Button) view.findViewById(R.id.operator_four);
        this.operator_one.setText(this.fragmentListener.onClickOneString());
        this.operator_three.setText(this.fragmentListener.onClickThreeString());
        this.operator_four.setText(this.fragmentListener.onClickFourString());
        this.operator_two.setText(this.fragmentListener.onClickTwoString());
        this.listview = (ListView) view.findViewById(R.id.listview);
        this.btnFloor = (Button) view.findViewById(R.id.floor);
        this.btnRoom = (Button) view.findViewById(R.id.room);
        this.btnDevice = (Button) view.findViewById(R.id.module);
        this.barView = view.findViewById(R.id.arealayout);
        this.backView = (BackView) view.findViewById(R.id.backview);
        this.titleView = (TextView) this.backView.findViewById(R.id.title);
        this.titleView.setText(this.fragmentListener.onTitle());
    }

    protected void initWidget(View view) {
        this.btnFloor.setOnClickListener(this);
        this.btnRoom.setOnClickListener(this);
        this.btnDevice.setOnClickListener(this);
        this.operator_one.setOnClickListener(this);
        this.operator_two.setOnClickListener(this);
        this.operator_three.setOnClickListener(this);
        this.operator_four.setOnClickListener(this);
        this.listview.setAdapter(this.fragmentListener.onAdapter());
        if (this.dimissBar) {
            this.barView.setVisibility(8);
        } else {
            this.barView.setVisibility(0);
        }
    }

    protected void onCreateInit() {
        if (getActivity() instanceof SettingActivity) {
            this.settingActivity = (SettingActivity) getActivity();
        } else if (getActivity() instanceof UserActivity) {
            this.userActivity = (UserActivity) getActivity();
        }
    }
}
