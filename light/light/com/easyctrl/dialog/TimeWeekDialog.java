package com.easyctrl.dialog;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import com.easyctrl.iface.OnTransmitText;
import com.easyctrl.ldy.activity.R;
import com.easyctrl.ldy.activity.UserActivity;
import com.easyctrl.ldy.util.StringValues;
import com.easyctrl.ui.base.BaseDialog;
import java.util.ArrayList;

public class TimeWeekDialog extends BaseDialog implements OnClickListener {
    private CheckBox[] check = new CheckBox[this.checkid.length];
    private int[] checkid = new int[]{R.id.checksun, R.id.checkMon, R.id.checkTues, R.id.checkwed, R.id.checkthur, R.id.checkfri, R.id.checksatur};
    private OnTransmitText onTransmitText;
    private UserActivity userActivity;
    private ArrayList<String> weekList = null;

    public void setOnTransmitText(OnTransmitText onTransmitText) {
        this.onTransmitText = onTransmitText;
    }

    public TimeWeekDialog(Context context) {
        super(context);
        this.userActivity = (UserActivity) context;
        getWindow().setGravity(80);
        setContentView((int) R.layout.time_week_select);
        findViewById(R.id.layoutMonday).setOnClickListener(this);
        findViewById(R.id.layoutfriday).setOnClickListener(this);
        findViewById(R.id.layoutsaturday).setOnClickListener(this);
        findViewById(R.id.layoutsunday).setOnClickListener(this);
        findViewById(R.id.layoutthursday).setOnClickListener(this);
        findViewById(R.id.layouttuesday).setOnClickListener(this);
        findViewById(R.id.layoutwednesday).setOnClickListener(this);
        findViewById(R.id.cancel).setOnClickListener(this);
        findViewById(R.id.confirm).setOnClickListener(this);
        initCheck();
    }

    private void initCheck() {
        for (int i = 0; i < this.checkid.length; i++) {
            this.check[i] = (CheckBox) findViewById(this.checkid[i]);
            this.check[i].setTag(StringValues.week[i]);
        }
    }

    public void onClick(View v) {
        if (R.id.layoutsunday == v.getId()) {
            getCheckBox(this.checkid[0]);
        } else if (R.id.layoutfriday == v.getId()) {
            getCheckBox(this.checkid[5]);
        } else if (R.id.layoutsaturday == v.getId()) {
            getCheckBox(this.checkid[6]);
        } else if (R.id.layoutMonday == v.getId()) {
            getCheckBox(this.checkid[1]);
        } else if (R.id.layoutthursday == v.getId()) {
            getCheckBox(this.checkid[4]);
        } else if (R.id.layouttuesday == v.getId()) {
            getCheckBox(this.checkid[2]);
        } else if (R.id.layoutwednesday == v.getId()) {
            getCheckBox(this.checkid[3]);
        } else if (R.id.cancel == v.getId()) {
            dismiss();
        } else if (R.id.confirm == v.getId()) {
            checkList();
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < this.weekList.size(); i++) {
                builder.append(new StringBuilder(String.valueOf((String) this.weekList.get(i))).append(",").toString());
            }
            builder.deleteCharAt(builder.toString().length() - 1);
            this.onTransmitText.transmit(builder.toString(), 2, 4, null);
            dismiss();
        }
    }

    private void checkList() {
        this.weekList = new ArrayList();
        this.userActivity.weekArray.clear();
        for (int i = 0; i < this.check.length; i++) {
            if (this.check[i].isChecked()) {
                String value = (String) this.check[i].getTag();
                this.weekList.add(value);
                this.userActivity.weekArray.add(StringValues.getWeekNum(value), value);
            }
        }
    }

    private CheckBox getCheckBox(int id) {
        for (int i = 0; i < this.check.length; i++) {
            if (this.check[i].getId() == id) {
                if (this.check[i].isChecked()) {
                    this.check[i].setChecked(false);
                } else {
                    this.check[i].setChecked(true);
                }
                return this.check[i];
            }
        }
        return null;
    }
}
