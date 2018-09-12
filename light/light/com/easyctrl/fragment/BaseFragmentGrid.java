package com.easyctrl.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;
import com.easyctrl.iface.OnBaseFragmentList;
import com.easyctrl.ldy.activity.R;
import com.easyctrl.ldy.activity.SettingActivity;
import com.easyctrl.ui.base.BaseEasyCtrlFragment;

public class BaseFragmentGrid extends BaseEasyCtrlFragment implements OnClickListener {
    private GridView gridView;
    private String mianban;
    private String model;
    private String name;
    private String num;
    private OnBaseFragmentList onBaseFragmentList;
    public SettingActivity settingActivity;

    public void setOnBaseFragmentList(OnBaseFragmentList onBaseFragmentList) {
        this.onBaseFragmentList = onBaseFragmentList;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onCreateInit();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.base_fragment_gridview, null);
        findViewByID(view);
        initWidget(view);
        return view;
    }

    public void onClick(View v) {
        if (v.getId() == R.id.leftButton) {
            this.onBaseFragmentList.onClick(v);
        } else if (v.getId() == R.id.titleView) {
            this.onBaseFragmentList.onClick(v);
        }
    }

    public void setNum(String num) {
        this.num = num;
    }

    public void setMianban(String mianban) {
        this.mianban = mianban;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setName(String name) {
        this.name = name;
    }

    protected void findViewByID(View view) {
        this.gridView = (GridView) view.findViewById(R.id.gridview);
        view.findViewById(R.id.leftButton).setOnClickListener(this);
        view.findViewById(R.id.titleView).setOnClickListener(this);
        ((TextView) view.findViewById(R.id.num)).setText(this.num);
        ((TextView) view.findViewById(R.id.mianban)).setText(this.mianban);
        ((TextView) view.findViewById(R.id.model)).setText(this.model);
        ((TextView) view.findViewById(R.id.keyNum)).setText(this.name);
    }

    protected void initWidget(View view) {
        if (this.onBaseFragmentList != null) {
            this.gridView.setAdapter(this.onBaseFragmentList.onAdapter());
        }
        this.gridView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (BaseFragmentGrid.this.onBaseFragmentList != null) {
                    BaseFragmentGrid.this.onBaseFragmentList.onItemListviewClick(parent, view, position, id);
                }
            }
        });
    }

    protected void onCreateInit() {
        this.settingActivity = (SettingActivity) getActivity();
    }
}
