package com.easyctrl.dialog;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import com.easyctrl.ldy.activity.R;
import com.easyctrl.ui.base.BaseDialog;

public class UpdateHostName extends BaseDialog implements OnClickListener {
    private EditText editText = ((EditText) findViewById(R.id.txtName));

    public UpdateHostName(Context context) {
        super(context);
        setContentView((int) R.layout.update_host_name);
        findViewById(R.id.cancel).setOnClickListener(this);
        findViewById(R.id.enter).setOnClickListener(this);
    }

    public void onClick(View v) {
        if (R.id.cancel == v.getId()) {
            dismiss();
        } else if (R.id.enter == v.getId()) {
            dismiss();
        }
    }
}
