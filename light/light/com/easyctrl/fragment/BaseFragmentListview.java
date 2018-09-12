package com.easyctrl.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import com.easyctrl.iface.OnBaseFragmentList;
import com.easyctrl.ldy.activity.R;
import com.easyctrl.ldy.activity.SettingActivity;
import com.easyctrl.ldy.activity.UserActivity;
import com.easyctrl.ldy.view.BackView;
import com.easyctrl.ui.base.BaseEasyCtrlFragment;

public class BaseFragmentListview extends BaseEasyCtrlFragment implements OnClickListener {
    public SettingActivity activity;
    private BackView backView;
    private ListView listView;
    private OnBaseFragmentList onBaseFragmentList;
    private String title;
    private TextView titleView;
    public UserActivity userActivity;

    public void setOnBaseFragmentList(OnBaseFragmentList onBaseFragmentList) {
        this.onBaseFragmentList = onBaseFragmentList;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onCreateInit();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.base_fragment_listview, null);
        findViewByID(view);
        initWidget(view);
        return view;
    }

    public void onClick(View v) {
        this.onBaseFragmentList.onClick(v);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    protected void findViewByID(View view) {
        this.listView = (ListView) view.findViewById(R.id.listview);
        this.backView = (BackView) view.findViewById(R.id.backview);
        this.titleView = (TextView) this.backView.findViewById(R.id.title);
        this.backView.findViewById(R.id.leftButton).setOnClickListener(this);
    }

    protected void initWidget(View view) {
        this.listView.setAdapter(this.onBaseFragmentList.onAdapter());
        this.listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (BaseFragmentListview.this.onBaseFragmentList != null) {
                    BaseFragmentListview.this.onBaseFragmentList.onItemListviewClick(parent, view, position, id);
                }
            }
        });
        if (this.title != null) {
            this.titleView.setText(this.title);
        }
    }

    protected void onCreateInit() {
        if (getActivity() instanceof SettingActivity) {
            this.activity = (SettingActivity) getActivity();
        } else if (getActivity() instanceof UserActivity) {
            this.userActivity = (UserActivity) getActivity();
        }
    }
}
