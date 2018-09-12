package com.easyctrl.dialog;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import com.easyctrl.iface.OnDateListener;
import com.easyctrl.iface.OnTransmitText;
import com.easyctrl.ldy.activity.R;
import com.easyctrl.ldy.calendar.CalendarPickerView;
import com.easyctrl.ui.base.BaseDialog;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateDialog extends BaseDialog implements OnDateListener, OnClickListener {
    private Date date;
    private String dateStr;
    private OnTransmitText onTransmitText;
    private TextView textView = ((TextView) findViewById(R.id.selectDate));

    public void setOnTransmitText(OnTransmitText onTransmitText) {
        this.onTransmitText = onTransmitText;
    }

    public DateDialog(Context context) {
        super(context);
        setContentView((int) R.layout.layout_date);
        findViewById(R.id.cancel).setOnClickListener(this);
        findViewById(R.id.confirm).setOnClickListener(this);
        Calendar nextYear = Calendar.getInstance();
        nextYear.add(1, 1);
        CalendarPickerView calendar = (CalendarPickerView) findViewById(R.id.calendar_view);
        calendar.setOnDateListener(this);
        calendar.init(new Date(), new Date(), nextYear.getTime());
    }

    public void onSelectDate(Date time) {
        this.date = time;
        this.dateStr = new StringBuilder(String.valueOf(this.mContext.getResources().getString(R.string.dateSelect))).append(DateFormat.getDateInstance().format(this.date)).toString();
        this.textView.setText(this.dateStr);
    }

    public void onClick(View v) {
        if (v.getId() == R.id.confirm) {
            Date d = new Date(System.currentTimeMillis());
            if (this.date == null) {
                this.date = d;
                this.dateStr = new StringBuilder(String.valueOf(this.mContext.getResources().getString(R.string.dateSelect))).append(DateFormat.getDateInstance().format(this.date)).toString();
            }
            this.onTransmitText.transmit(this.dateStr, 2, 6, this.date);
            dismiss();
        } else if (v.getId() == R.id.cancel) {
            dismiss();
        }
    }
}
