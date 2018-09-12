package com.easyctrl.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.easyctrl.dialog.UpdateFRDDialog;
import com.easyctrl.impl.FloorRoomModuleListener;
import com.easyctrl.ldy.activity.R;
import com.easyctrl.ldy.activity.SettingActivity;
import com.easyctrl.ldy.domain.FloorBean;
import com.easyctrl.ldy.domain.ModuleBean;
import com.easyctrl.ldy.view.NavigataView;
import com.easyctrl.ui.base.BaseActivity;
import com.easyctrl.ui.base.BaseDialog.BaseDialogOnDismissListener;

public class FloorRoomFragment extends Fragment implements OnClickListener {
    private int curintSelectModuleId;
    private FloorBean currentFloor;
    private int currentfirstItem;
    private UpdateFRDDialog dialog;
    public boolean isUpdate = false;
    private ListView listView;
    private ModuleBean moduleBean;
    private NavigataView navigataView;
    private FloorRoomModuleListener onFloorroomModuleListener;
    private int position;
    private Button rightButton;
    private SettingActivity settingActivity;
    private String title;
    public TextView txtTitle;
    public String type;
    private View view;
    private int visibility;

    public void onPause() {
        super.onPause();
        this.onFloorroomModuleListener.onPause();
    }

    public void onDestroy() {
        super.onDestroy();
        this.onFloorroomModuleListener.onDestroy();
    }

    public void setCurrentFloor(FloorBean currentFloor) {
        this.currentFloor = currentFloor;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }

    public void setOnFloorroomModuleListener(FloorRoomModuleListener onFloorroomModuleListener) {
        this.onFloorroomModuleListener = onFloorroomModuleListener;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.settingActivity = (SettingActivity) getActivity();
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.floor_room_manager, null);
        this.navigataView = (NavigataView) this.view.findViewById(R.id.navigaview);
        this.txtTitle = (TextView) this.navigataView.findViewById(R.id.title);
        this.txtTitle.setText(this.title);
        this.rightButton = (Button) this.navigataView.findViewById(R.id.rigthbutton);
        this.rightButton.setVisibility(this.visibility);
        if (this.currentFloor != null) {
            this.rightButton.setText(this.currentFloor.name);
        }
        this.listView = (ListView) this.view.findViewById(R.id.listview);
        this.view.findViewById(R.id.add).setOnClickListener(this);
        this.view.findViewById(R.id.delete).setOnClickListener(this);
        this.navigataView.getLeftButton().setOnClickListener(this);
        if (this.onFloorroomModuleListener != null) {
            this.listView.setAdapter(this.onFloorroomModuleListener.onFloorRoomModuleListener());
        }
        onScrool();
        this.dialog = new UpdateFRDDialog(getActivity());
        this.dialog.setOnDismissListener(new BaseDialogOnDismissListener() {
            public void onDismiss() {
                FloorRoomFragment.this.onFloorroomModuleListener.onCancel();
            }
        });
        itemListener();
        return this.view;
    }

    private void onScrool() {
        this.listView.setOnScrollListener(new OnScrollListener() {
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                FloorRoomFragment.this.currentfirstItem = firstVisibleItem;
            }
        });
    }

    private void itemListener() {
        this.listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                FloorRoomFragment.this.onFloorroomModuleListener.onSaveORUpdate(position);
            }
        });
    }

    public void onClick(View v) {
        if (v.getId() == R.id.leftbutton) {
            ModulePortListFragment fragment = ModulePortListFragment.newInstance();
            fragment.setTitle(this.moduleBean, this.position);
            fragment.setModuleID(this.curintSelectModuleId, 0);
            this.settingActivity.popFragments(BaseActivity.SETTING, R.id.modulelayout, true);
        } else if (v.getId() == R.id.rigthbutton) {
        } else {
            if (v.getId() == R.id.add) {
                this.onFloorroomModuleListener.addListener();
            } else if (v.getId() == R.id.delete) {
                this.onFloorroomModuleListener.deleteListener();
            }
        }
    }

    public void setAdapterByCurrent(BaseAdapter adapter) {
        if (adapter != null) {
            this.listView.setAdapter(adapter);
            this.listView.setSelection(this.currentfirstItem);
            adapter.notifyDataSetChanged();
        }
    }

    public void setAdapter(BaseAdapter adapter) {
        if (adapter != null) {
            this.listView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

    public void setAdapter(BaseAdapter adapter, int count) {
        if (adapter != null) {
            this.listView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            this.listView.setSelection(adapter.getCount());
        }
    }

    public void setCurintSelectModuleId(int curintSelectModuleId) {
        this.curintSelectModuleId = curintSelectModuleId;
    }

    public void setTitle(ModuleBean moduleBean, int position) {
        this.moduleBean = moduleBean;
        this.position = position;
    }

    public void setRightButton(int visibility) {
        this.visibility = visibility;
    }

    public FloorBean getCurrentFloor() {
        return this.currentFloor;
    }
}
