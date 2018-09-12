package com.easyctrl.dialog;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import com.easyctrl.fragment.UserSetFragment;
import com.easyctrl.ldy.activity.MainApplication;
import com.easyctrl.ldy.activity.R;
import com.easyctrl.ldy.activity.SettingActivity;
import com.easyctrl.ldy.domain.BindBean;
import com.easyctrl.ldy.domain.UserScene;
import com.easyctrl.ldy.domain.VirtualBean;
import com.easyctrl.ui.base.BaseActivity;
import com.easyctrl.ui.base.BaseDialog;

public class SceneBindDialog extends BaseDialog implements OnClickListener {
    private Object object;
    private SettingActivity settingActivity;
    private UserScene userScene;

    public SceneBindDialog(Context context) {
        super(context);
        this.settingActivity = (SettingActivity) context;
        setContentView((int) R.layout.scene_bind_dialog);
        findViewById(R.id.cancel).setOnClickListener(this);
        findViewById(R.id.enter).setOnClickListener(this);
    }

    public void onClick(View v) {
        if (R.id.cancel == v.getId()) {
            dismiss();
        } else if (R.id.enter != v.getId()) {
        } else {
            if (this.object instanceof BindBean) {
                BindBean bindBean = this.object;
                this.userScene.sbindType = 1;
                this.userScene.pbindID = bindBean.bindID;
                MainApplication.userSceneManager.update(this.userScene, this.userScene.userSceneID);
                dismiss();
                Toast.makeText(this.mContext, "\u5173\u8054\u6210\u529f", 1).show();
                this.settingActivity.clearStackByTag(BaseActivity.SETTING);
                this.settingActivity.pushFragments(BaseActivity.SETTING, new UserSetFragment(), true, true, R.id.setting_center);
            } else if (this.object instanceof VirtualBean) {
                VirtualBean virtualBean = this.object;
                this.userScene.sbindType = 0;
                this.userScene.vbindID = virtualBean.virtualID;
                MainApplication.userSceneManager.update(this.userScene, this.userScene.userSceneID);
                dismiss();
                Toast.makeText(this.mContext, "\u5173\u8054\u6210\u529f", 1).show();
                this.settingActivity.clearStackByTag(BaseActivity.SETTING);
                this.settingActivity.pushFragments(BaseActivity.SETTING, new UserSetFragment(), true, true, R.id.setting_center);
            }
        }
    }

    public void setUserScene(UserScene userScene) {
        this.userScene = userScene;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}
