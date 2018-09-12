package com.easyctrl.dialog;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import com.easyctrl.ldy.activity.R;
import com.easyctrl.ui.base.BaseDialog;

public class HintDialog extends BaseDialog implements OnClickListener {
    private Button confirm = ((Button) findViewById(R.id.confirm));
    private TextView hint = ((TextView) findViewById(R.id.content));

    public HintDialog(Context context) {
        super(context);
        setContentView((int) R.layout.hint_dialog);
        this.confirm.setOnClickListener(this);
    }

    public void setHint(String strhint) {
        this.hint.setText(strhint);
    }

    public void onClick(View v) {
        cancel();
    }
}
