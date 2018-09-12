package com.easyctrl.ldy.view;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.easyctrl.ldy.activity.LoginActivity;
import com.easyctrl.ldy.activity.R;
import com.easyctrl.manager.UserManager;

public class BackView extends RelativeLayout implements OnClickListener {
    private TextView host;
    private Button leftButton;
    private Context mContext;
    private LayoutInflater mInflater;
    private TextView titleView;
    private UserManager userManager;
    private View view = this.mInflater.inflate(R.layout.back_view, this);

    public BackView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.view.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            }
        });
        this.titleView = (TextView) this.view.findViewById(R.id.title);
        this.leftButton = (Button) this.view.findViewById(R.id.leftButton);
        this.userManager = UserManager.getInstance(context);
        this.host = (TextView) this.view.findViewById(R.id.host);
        this.host.setText(this.userManager.currentHost());
        this.view.findViewById(R.id.info).setOnClickListener(this);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.backview);
        String leftstr = array.getString(1);
        this.titleView.setText(array.getString(0));
        this.leftButton.setText(leftstr);
        array.recycle();
    }

    public void setHostViewString(String hoststr) {
        this.host.setText(hoststr);
    }

    public void setTitle(String title) {
        this.titleView.setText(title);
    }

    public void onClick(View v) {
        if (v.getId() == R.id.info) {
            this.mContext.startActivity(new Intent(this.mContext, LoginActivity.class));
        }
    }
}
