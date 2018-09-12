package com.easyctrl.ldy.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.easyctrl.ldy.activity.R;

public class WaitView extends RelativeLayout implements AnimationListener {
    private boolean isStop;
    private RotateAnimation rotate = new RotateAnimation(0.0f, 720.0f, 1, 0.5f, 1, 0.5f);
    private TextView view = ((TextView) findViewById(R.id.viewAnim));

    public WaitView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.wait_view_anim, this);
        this.rotate.setDuration(2000);
        this.rotate.setInterpolator(new LinearInterpolator());
        this.rotate.setRepeatCount(-1);
        this.rotate.setAnimationListener(this);
        this.view.startAnimation(this.rotate);
    }

    public void startAnim() {
        setVisibility(0);
        this.view.startAnimation(this.rotate);
    }

    public void stopAnim() {
        this.rotate.cancel();
        setVisibility(8);
    }

    public void onAnimationStart(Animation animation) {
        this.isStop = true;
    }

    public void onAnimationEnd(Animation animation) {
        this.isStop = true;
    }

    public void onAnimationRepeat(Animation animation) {
        this.isStop = true;
    }

    public boolean isStop() {
        return this.isStop;
    }
}
