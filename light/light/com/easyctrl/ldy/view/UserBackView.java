package com.easyctrl.ldy.view;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.easyctrl.fragment.TimerFragmentUser;
import com.easyctrl.ldy.activity.LoginActivity;
import com.easyctrl.ldy.activity.MainApplication;
import com.easyctrl.ldy.activity.R;
import com.easyctrl.ldy.activity.UserActivity;
import com.easyctrl.ui.base.BaseActivity;
import de.greenrobot.event.EventBus;

public class UserBackView extends RelativeLayout implements OnClickListener {
    private TextView host;
    private Context mContext;
    private LayoutInflater mInflater;
    private UserActivity userActivity = ((UserActivity) this.mContext);
    private View view;

    public UserBackView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.view = this.mInflater.inflate(R.layout.user_back_view, this);
        this.view.findViewById(R.id.info).setOnClickListener(this);
        this.view.findViewById(R.id.leftButton).setOnClickListener(this);
        this.view.findViewById(R.id.rightButton).setOnClickListener(this);
        this.host = (TextView) this.view.findViewById(R.id.host);
        this.host.setText(MainApplication.userManager.currentHost());
    }

    public void onClick(View v) {
        if (v.getId() == R.id.info) {
            this.mContext.startActivity(new Intent(this.mContext, LoginActivity.class));
        } else if (v.getId() == R.id.leftButton) {
            this.userActivity.popFragments(BaseActivity.USRT, R.id.center_layout, true);
        } else if (v.getId() == R.id.rightButton) {
            TimerFragmentUser timerFragmentUser = new TimerFragmentUser();
            EventBus.getDefault().register(timerFragmentUser);
            this.userActivity.pushFragments(BaseActivity.USRT, timerFragmentUser, true, true, R.id.center_layout);
        }
    }
}
