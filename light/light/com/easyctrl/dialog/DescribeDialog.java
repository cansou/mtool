package com.easyctrl.dialog;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import com.easyctrl.iface.OnTransmitText;
import com.easyctrl.ldy.activity.R;
import com.easyctrl.ui.base.BaseDialog;

public class DescribeDialog extends BaseDialog implements OnClickListener {
    private OnTransmitText onTransmitText;

    public void setOnTransmitText(OnTransmitText onTransmitText) {
        this.onTransmitText = onTransmitText;
    }

    public DescribeDialog(Context context) {
        super(context);
        setContentView((int) R.layout.describe);
        getWindow().setGravity(80);
        findViewById(R.id.cancel).setOnClickListener(this);
        findViewById(R.id.confirm).setOnClickListener(this);
    }

    public void onClick(View v) {
        if (v.getId() == R.id.cancel) {
            dismiss();
        } else if (v.getId() == R.id.confirm) {
            EditText text = (EditText) findViewById(R.id.input);
            if (this.onTransmitText != null) {
                this.onTransmitText.transmit(text.getText().toString(), 1, 0, null);
            }
            dismiss();
        }
    }
}
