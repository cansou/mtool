package com.easyctrl.dialog;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;
import com.easyctrl.ldy.activity.MainApplication;
import com.easyctrl.ldy.activity.R;
import com.easyctrl.ldy.domain.UserScene;
import com.easyctrl.ui.base.BaseDialog;

public class UserUpdateDialog extends BaseDialog implements OnClickListener {
    private EditText editText = ((EditText) findViewById(R.id.txtName));
    private UserScene userScene;

    public void setUserScene(UserScene userScene) {
        this.userScene = userScene;
    }

    public UserUpdateDialog(Context context) {
        super(context);
        setContentView((int) R.layout.user_update_name);
        findViewById(R.id.enter).setOnClickListener(this);
        findViewById(R.id.cancel).setOnClickListener(this);
    }

    public void show() {
        super.show();
        if (this.userScene != null) {
            this.editText.setText(this.userScene.name);
        }
    }

    public void onClick(View v) {
        if (R.id.enter == v.getId()) {
            String name = this.editText.getText().toString();
            if (name.length() > 0) {
                MainApplication.userSceneManager.updateName(name, this.userScene.userSceneID);
                dismiss();
                return;
            }
            Toast.makeText(this.mContext, "\u540d\u79f0\u4e0d\u80fd\u4e3a\u7a7a", 1).show();
        } else if (R.id.cancel == v.getId()) {
            dismiss();
        }
    }
}
