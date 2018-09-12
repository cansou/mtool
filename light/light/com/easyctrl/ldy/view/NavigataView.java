package com.easyctrl.ldy.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.easyctrl.ldy.activity.R;

public class NavigataView extends RelativeLayout {
    private Button leftButton = ((Button) this.view.findViewById(R.id.leftbutton));
    private LayoutInflater mInflater;
    private Button rightButton = ((Button) this.view.findViewById(R.id.rigthbutton));
    private TextView title = ((TextView) this.view.findViewById(R.id.title));
    private View view = this.mInflater.inflate(R.layout.navagta_view, this);

    public Button getLeftButton() {
        return this.leftButton;
    }

    public Button getRightButton() {
        return this.rightButton;
    }

    public NavigataView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mInflater = LayoutInflater.from(context);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.navation);
        String left = array.getString(1);
        String t = array.getString(0);
        String right = array.getString(2);
        if (left != null && left.length() > 0) {
            this.leftButton.setText(left);
        }
        if (t != null && t.length() > 0) {
            this.title.setText(t);
        }
        if (right != null && right.length() > 0) {
            this.rightButton.setText(right);
            this.rightButton.setVisibility(0);
        }
    }
}
