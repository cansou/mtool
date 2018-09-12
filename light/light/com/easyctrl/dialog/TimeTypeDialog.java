package com.easyctrl.dialog;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import com.easyctrl.iface.OnTransmitText;
import com.easyctrl.ldy.activity.R;
import com.easyctrl.ldy.activity.UserActivity;
import com.easyctrl.ldy.util.StringValues;
import com.easyctrl.ui.base.BaseDialog;

public class TimeTypeDialog extends BaseDialog implements OnClickListener {
    private OnTransmitText onTransmitText;
    private UserActivity userActivity;

    public void setOnTransmitText(OnTransmitText onTransmitText) {
        this.onTransmitText = onTransmitText;
    }

    public TimeTypeDialog(Context context) {
        super(context);
        this.userActivity = (UserActivity) context;
        setContentView((int) R.layout.time_repeat_type);
        getWindow().setGravity(80);
        findViewById(R.id.weekend).setOnClickListener(this);
        findViewById(R.id.everyday).setOnClickListener(this);
        findViewById(R.id.workday).setOnClickListener(this);
        findViewById(R.id.Specialdays).setOnClickListener(this);
        findViewById(R.id.userdefined).setOnClickListener(this);
    }

    private void setText(int id, int timetype) {
        if (this.onTransmitText != null) {
            this.onTransmitText.transmit(((TextView) findViewById(id)).getText().toString(), 2, timetype, null);
        }
    }

    public void onClick(View v) {
        if (v.getId() == R.id.weekend) {
            this.userActivity.weekArray.clear();
            this.userActivity.weekArray.add(6, StringValues.week[6]);
            this.userActivity.weekArray.add(0, StringValues.week[0]);
            setText(v.getId(), 4);
            dismiss();
        } else if (v.getId() == R.id.everyday) {
            this.userActivity.weekArray.clear();
            setText(v.getId(), 3);
            dismiss();
        } else if (v.getId() == R.id.workday) {
            this.userActivity.weekArray.clear();
            this.userActivity.weekArray.add(1, StringValues.week[1]);
            this.userActivity.weekArray.add(2, StringValues.week[2]);
            this.userActivity.weekArray.add(3, StringValues.week[3]);
            this.userActivity.weekArray.add(4, StringValues.week[4]);
            this.userActivity.weekArray.add(5, StringValues.week[5]);
            setText(v.getId(), 5);
            dismiss();
        } else if (v.getId() == R.id.Specialdays) {
            setText(v.getId(), 2);
            dismiss();
        } else if (v.getId() == R.id.userdefined) {
            setText(v.getId(), 1);
            dismiss();
        }
    }
}
