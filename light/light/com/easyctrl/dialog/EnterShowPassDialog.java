package com.easyctrl.dialog;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import com.easyctrl.ldy.activity.MainApplication;
import com.easyctrl.ldy.activity.R;
import com.easyctrl.ui.base.BaseDialog;

public class EnterShowPassDialog extends BaseDialog implements OnClickListener {
    private Button cancel = ((Button) findViewById(R.id.cancel));
    private CheckBox checkBox = ((CheckBox) findViewById(R.id.check));
    private EditText editpass = ((EditText) findViewById(R.id.editpass));
    private Button enter = ((Button) findViewById(R.id.enter));
    private OnEnterlistener onEnterlistener;

    public interface OnEnterlistener {
        void onClick(View view, String str);
    }

    public EnterShowPassDialog(Context context) {
        super(context);
        setContentView((int) R.layout.enter_setting_layout_dialog);
        this.cancel.setOnClickListener(this);
        this.enter.setOnClickListener(this);
    }

    public void show() {
        super.show();
        String pass = MainApplication.userManager.getEnterSettingPassWord();
        if (pass == null) {
            this.editpass.setText("");
        } else {
            this.editpass.setText(pass);
        }
        this.checkBox.setChecked(pass != null);
    }

    public void onClick(View v) {
        if (v.getId() == R.id.cancel) {
            dismiss();
        } else if (v.getId() == R.id.enter) {
            if (this.onEnterlistener != null) {
                this.onEnterlistener.onClick(v, this.editpass.getText().toString());
            }
            checkPassWord();
            dismiss();
        }
    }

    public void setOnEnterlistener(OnEnterlistener onEnterlistener) {
        this.onEnterlistener = onEnterlistener;
    }

    private void checkPassWord() {
        if (this.checkBox.isChecked()) {
            MainApplication.userManager.setEnterSettingPassword(this.editpass.getText().toString());
        } else {
            MainApplication.userManager.setEnterSettingPassword(null);
        }
    }
}
