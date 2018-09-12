package com.easyctrl.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListAdapter;
import com.easyctrl.adapter.UserSetAdapter;
import com.easyctrl.dialog.UserUpdateDialog;
import com.easyctrl.ldy.activity.MainApplication;
import com.easyctrl.ldy.activity.R;
import com.easyctrl.ldy.domain.FloorBean;
import com.easyctrl.ldy.domain.RoomBean;
import com.easyctrl.ldy.domain.UserScene;
import com.easyctrl.ui.base.BaseActivity;
import com.easyctrl.ui.base.BaseDialog.BaseDialogOnDismissListener;
import java.util.ArrayList;

public class UserSetFragment extends BindFragment {
    private String Tag = UserSetFragment.class.getSimpleName();
    private FloorBean floorBean;
    private RoomBean roomBean;
    private ArrayList<UserScene> scenes;
    private UserScene userScene;
    private UserSetAdapter userSetAdapter = new UserSetAdapter(null);

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFragmentListener(this);
        setDimissBar(false);
        this.floors = MainApplication.floorManager.findAllName();
        if (this.floors.size() <= 0) {
            return;
        }
        if (MainApplication.userManager.getCurrentSetFloor() == 0) {
            this.floorString = (String) this.floors.get(0);
            this.floorBean = MainApplication.floorManager.findByName(this.floorString);
            this.rooms = MainApplication.roomManager.findByFloorIDAndip(this.floorBean.id);
            if (this.rooms.size() > 0) {
                this.roomString = (String) this.rooms.get(0);
                this.roomBean = MainApplication.roomManager.findByName(this.roomString, this.floorBean.id);
                return;
            }
            return;
        }
        this.floorBean = MainApplication.floorManager.findByID(MainApplication.userManager.getCurrentSetFloor());
        this.floorString = this.floorBean.name;
        this.roomBean = MainApplication.roomManager.findByName(this.roomString, this.floorBean.id);
        if (this.roomBean == null) {
            this.floorString = (String) this.floors.get(0);
            this.floorBean = MainApplication.floorManager.findByName(this.floorString);
            this.rooms = MainApplication.roomManager.findByFloorIDAndip(this.floorBean.id);
            if (this.rooms.size() > 0) {
                this.roomString = (String) this.rooms.get(0);
                this.roomBean = MainApplication.roomManager.findByName(this.roomString, this.floorBean.id);
            }
        }
        this.roomString = this.roomBean.name;
    }

    public void onPause() {
        super.onPause();
        MainApplication.userManager.setCurrentSetFloor(this.floorBean.id);
        MainApplication.userManager.setCurrentSetRoom(this.roomBean.id);
    }

    public String onTitle() {
        return "\u5e94\u7528\u8bbe\u7f6e";
    }

    private void selectItem() {
        this.listview.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                UserSetFragment.this.userScene = (UserScene) UserSetFragment.this.scenes.get(position);
                CheckBox checkBox = (CheckBox) view.findViewById(R.id.check);
                UserSetFragment.this.userSetAdapter.selectBean = UserSetFragment.this.userScene;
                checkBox.setChecked(true);
                if (UserSetFragment.this.userScene == null) {
                    UserSetFragment.this.userScene = (UserScene) checkBox.getTag();
                }
                UserSetFragment.this.userSetAdapter.notifyDataSetChanged();
            }
        });
    }

    public ListAdapter onAdapter() {
        if (!(this.floorBean == null || this.roomBean == null)) {
            update();
            selectItem();
        }
        return this.userSetAdapter;
    }

    private void update() {
        if (MainApplication.userSceneManager != null) {
            this.scenes = MainApplication.userSceneManager.findByFloorIdAndRoomID(this.floorBean.id, this.roomBean.id);
        }
        this.userSetAdapter.setUserScenes(this.scenes);
        this.userSetAdapter.notifyDataSetChanged();
    }

    public void onClickOperatorOne(View v) {
        if (this.userScene != null) {
            UserUpdateDialog dialog = new UserUpdateDialog(this.settingActivity);
            dialog.setUserScene(this.userScene);
            dialog.show();
            dialog.setOnDismissListener(new BaseDialogOnDismissListener() {
                public void onDismiss() {
                    UserSetFragment.this.update();
                }
            });
        }
    }

    public void onClickOpreatorTwo(View v) {
        if (this.userScene != null) {
            this.userScene.sbindType = -1;
            this.userScene.pbindID = 0;
            this.userScene.vbindID = 0;
            MainApplication.userSceneManager.update(this.userScene, this.userScene.userSceneID);
            update();
        }
    }

    public void onClickOpreatorThree(View v) {
        if (this.userSetAdapter.selectBean != null) {
            BindMList bindMList = new BindMList();
            bindMList.setObject(this.userSetAdapter.selectBean);
            this.settingActivity.pushFragments(BaseActivity.SETTING, bindMList, true, true, R.id.setting_center);
            bindMList.setFragmentType(1);
        }
    }

    public void onClickOperatorFour(View v) {
        VirtualMList virtualMList = new VirtualMList();
        virtualMList.setObject(this.userSetAdapter.selectBean);
        this.settingActivity.pushFragments(BaseActivity.SETTING, virtualMList, true, true, R.id.setting_center);
    }

    public void onRefush(String content, View anchor, View view) {
        Button button = (Button) anchor;
        if (button.getId() == R.id.floor) {
            this.floorString = content;
            this.floorBean = MainApplication.floorManager.findByName(this.floorString);
            if (this.floorBean != null) {
                this.rooms = MainApplication.roomManager.findByFloorIDAndip(this.floorBean.id);
                if (this.rooms != null && this.rooms.size() > 0) {
                    this.roomString = (String) this.rooms.get(0);
                    setButtonRoomString(this.roomString);
                    this.roomBean = MainApplication.roomManager.findByName(this.roomString, this.floorBean.id);
                }
                update();
                this.userSetAdapter.notifyDataSetChanged();
            }
        } else if (button.getId() == R.id.room) {
            this.roomString = content;
            setButtonRoomString(this.roomString);
            if (this.floorBean != null) {
                this.roomBean = MainApplication.roomManager.findByName(this.roomString, this.floorBean.id);
                update();
                this.userSetAdapter.notifyDataSetChanged();
            }
        }
    }

    public void onResume() {
        super.onResume();
        MainApplication.userManager.setCurrentFragment(this.Tag);
    }

    public String onClickOneString() {
        return "\u7f16\u8f91";
    }

    public String onClickTwoString() {
        return "\u6e05\u7a7a";
    }

    public String onClickThreeString() {
        return "\u6309\u952e\u9762\u677f";
    }

    public String onClickFourString() {
        return "\u865a\u62df\u9762\u677f";
    }
}
