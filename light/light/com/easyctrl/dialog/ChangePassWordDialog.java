package com.easyctrl.dialog;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.easyctrl.ldy.activity.MainApplication;
import com.easyctrl.ldy.activity.R;
import com.easyctrl.ldy.activity.SettingActivity;
import com.easyctrl.manager.OrderManage;
import com.easyctrl.ui.base.BaseDialog;

public class ChangePassWordDialog extends BaseDialog implements OnClickListener {
    public static final int SHOW_LOGIN = 1;
    public static final int SHOW_SET = 2;
    private TextView hint = ((TextView) findViewById(R.id.textsave));
    private EditText newPassEdit = ((EditText) findViewById(R.id.txtName));
    private SettingActivity settingActivity;
    private EditText txtNameAgain = ((EditText) findViewById(R.id.txtNameOK));
    private int type;

    public void setType(int type) {
        this.type = type;
    }

    public ChangePassWordDialog(Context context) {
        super(context);
        this.settingActivity = (SettingActivity) context;
        setContentView((int) R.layout.update_pass_word_dialog);
        findViewById(R.id.enter).setOnClickListener(this);
        findViewById(R.id.cancel).setOnClickListener(this);
    }

    public void show() {
        if (this.type == 1) {
            this.hint.setText("\u4fee\u6539\u7528\u6237\u5bc6\u7801\uff1a");
        } else if (this.type == 2) {
            this.hint.setText("\u4fee\u6539\u7ba1\u7406\u5bc6\u7801\uff1a");
        }
        super.show();
    }

    public void onClick(View v) {
        if (R.id.enter == v.getId()) {
            String pass = this.newPassEdit.getText().toString();
            String passAgain = this.txtNameAgain.getText().toString();
            if (pass.length() > 8) {
                Toast.makeText(this.settingActivity, "\u5bc6\u7801\u4e0d\u80fd\u8d85\u8fc78\u4f4d", 1).show();
            } else if (pass.equals(passAgain)) {
                if (this.type == 1) {
                    this.settingActivity.sendData(OrderManage.updateLoginPass(MainApplication.userManager.getTempPass(), pass));
                } else if (this.type == 2) {
                    this.settingActivity.sendData(OrderManage.updateSetPass(MainApplication.userManager.getTempSetPass(), pass));
                }
                Toast.makeText(this.settingActivity, "\u5bc6\u7801\u4fee\u6539\u5b8c\u6210", 0).show();
                dismiss();
            } else {
                Toast.makeText(this.settingActivity, "\u4e24\u6b21\u5bc6\u7801\u8f93\u5165\u4e0d\u4e00\u81f4", 0).show();
            }
        } else if (R.id.cancel == v.getId()) {
            dismiss();
        }
    }
}
