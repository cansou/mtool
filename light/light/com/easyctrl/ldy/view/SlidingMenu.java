package com.easyctrl.ldy.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Display;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Scroller;
import com.easyctrl.ldy.activity.R;

public class SlidingMenu extends RelativeLayout {
    private static final int VELOCITY = 50;
    private RelativeLayout bgShade;
    private boolean canSlideLeft = true;
    private boolean canSlideRight = false;
    private boolean hasClickLeft = false;
    private boolean hasClickRight = false;
    private boolean isShowLeft;
    private Context mContext;
    private View mDetailView;
    private boolean mIsBeingDragged = true;
    private float mLastMotionX;
    private float mLastMotionY;
    private View mMenuView;
    private Scroller mScroller;
    private View mSlidingView;
    private int mTouchSlop;
    private VelocityTracker mVelocityTracker;
    private int screenHeight;
    private int screenWidth;
    private boolean tCanSlideLeft = true;
    private boolean tCanSlideRight = false;

    public SlidingMenu(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        this.bgShade = new RelativeLayout(context);
        this.mScroller = new Scroller(getContext());
        this.mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        Display display = ((Activity) context).getWindow().getWindowManager().getDefaultDisplay();
        this.screenWidth = display.getWidth();
        this.screenHeight = display.getHeight();
        LayoutParams bgParams = new LayoutParams(this.screenWidth, this.screenHeight);
        bgParams.addRule(13);
        this.bgShade.setLayoutParams(bgParams);
    }

    public SlidingMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SlidingMenu(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public void addViews(View left, View center, View right) {
        setLeftView(left);
        setRightView(right);
        setCenterView(center);
    }

    public void setLeftView(View view) {
        addView(view, new LayoutParams(-2, -1));
        this.mMenuView = view;
    }

    public void setRightView(View view) {
        LayoutParams behindParams = new LayoutParams(-2, -1);
        behindParams.addRule(11);
        addView(view, behindParams);
        this.mDetailView = view;
    }

    public void setCenterView(View view) {
        LayoutParams aboveParams = new LayoutParams(-1, -1);
        LayoutParams bgParams = new LayoutParams(this.screenWidth, this.screenHeight);
        bgParams.addRule(13);
        View bgShadeContent = new View(this.mContext);
        bgShadeContent.setBackgroundDrawable(getResources().getDrawable(R.drawable.shade_bg));
        this.bgShade.addView(bgShadeContent, bgParams);
        addView(this.bgShade, bgParams);
        addView(view, aboveParams);
        this.mSlidingView = view;
        this.mSlidingView.bringToFront();
    }

    public void scrollTo(int x, int y) {
        super.scrollTo(x, y);
        postInvalidate();
    }

    public void computeScroll() {
        if (!this.mScroller.isFinished() && this.mScroller.computeScrollOffset()) {
            int oldX = this.mSlidingView.getScrollX();
            int oldY = this.mSlidingView.getScrollY();
            int x = this.mScroller.getCurrX();
            int y = this.mScroller.getCurrY();
            if (!((oldX == x && oldY == y) || this.mSlidingView == null)) {
                this.mSlidingView.scrollTo(x, y);
                if (x < 0) {
                    this.bgShade.scrollTo(x + 20, y);
                } else {
                    this.bgShade.scrollTo(x - 20, y);
                }
            }
            invalidate();
        }
    }

    public void setCanSliding(boolean left, boolean right) {
        this.canSlideLeft = left;
        this.canSlideRight = right;
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        float x = ev.getX();
        float y = ev.getY();
        switch (action) {
            case 0:
                this.mLastMotionX = x;
                this.mLastMotionY = y;
                this.mIsBeingDragged = false;
                if (this.canSlideLeft) {
                    this.mMenuView.setVisibility(0);
                    if (this.mDetailView != null) {
                        this.mDetailView.setVisibility(4);
                    }
                }
                if (this.canSlideRight) {
                    this.mMenuView.setVisibility(4);
                    this.mDetailView.setVisibility(0);
                    break;
                }
                break;
            case 2:
                float dx = x - this.mLastMotionX;
                float xDiff = Math.abs(dx);
                float yDiff = Math.abs(y - this.mLastMotionY);
                if (xDiff > ((float) this.mTouchSlop) && xDiff > yDiff) {
                    if (!this.canSlideLeft) {
                        if (this.canSlideRight) {
                            if (((float) this.mSlidingView.getScrollX()) <= 0.0f) {
                                if (dx < 0.0f) {
                                    this.mIsBeingDragged = true;
                                    this.mLastMotionX = x;
                                    break;
                                }
                            }
                            this.mIsBeingDragged = true;
                            this.mLastMotionX = x;
                            break;
                        }
                    } else if (((float) this.mSlidingView.getScrollX()) >= 0.0f) {
                        if (dx > 0.0f) {
                            this.mIsBeingDragged = true;
                            this.mLastMotionX = x;
                            break;
                        }
                    } else {
                        this.mIsBeingDragged = true;
                        this.mLastMotionX = x;
                        break;
                    }
                }
                break;
        }
        return this.mIsBeingDragged;
    }

    public boolean onTouchEvent(MotionEvent ev) {
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
        this.mVelocityTracker.addMovement(ev);
        int action = ev.getAction();
        float x = ev.getX();
        float y = ev.getY();
        switch (action) {
            case 0:
                if (!this.mScroller.isFinished()) {
                    this.mScroller.abortAnimation();
                }
                this.mLastMotionX = x;
                this.mLastMotionY = y;
                if (this.mSlidingView.getScrollX() == (-getMenuViewWidth()) && this.mLastMotionX < ((float) getMenuViewWidth())) {
                    return false;
                }
                if (this.mSlidingView.getScrollX() == getDetailViewWidth() && this.mLastMotionX > ((float) getMenuViewWidth())) {
                    return false;
                }
                break;
            case 1:
            case 3:
                if (this.mIsBeingDragged) {
                    VelocityTracker velocityTracker = this.mVelocityTracker;
                    velocityTracker.computeCurrentVelocity(100);
                    float xVelocity = velocityTracker.getXVelocity();
                    int oldScrollX = this.mSlidingView.getScrollX();
                    int dx = 0;
                    if (oldScrollX <= 0 && this.canSlideLeft) {
                        if (xVelocity > 50.0f) {
                            dx = (-getMenuViewWidth()) - oldScrollX;
                        } else if (xVelocity < -50.0f) {
                            dx = -oldScrollX;
                            if (this.hasClickLeft) {
                                this.hasClickLeft = false;
                                setCanSliding(this.tCanSlideLeft, this.tCanSlideRight);
                            }
                        } else if (oldScrollX < (-getMenuViewWidth()) / 2) {
                            dx = (-getMenuViewWidth()) - oldScrollX;
                        } else if (oldScrollX >= (-getMenuViewWidth()) / 2) {
                            dx = -oldScrollX;
                            if (this.hasClickLeft) {
                                this.hasClickLeft = false;
                                setCanSliding(this.tCanSlideLeft, this.tCanSlideRight);
                            }
                        }
                    }
                    if (oldScrollX >= 0 && this.canSlideRight) {
                        if (xVelocity < -50.0f) {
                            dx = getDetailViewWidth() - oldScrollX;
                        } else if (xVelocity > 50.0f) {
                            dx = -oldScrollX;
                            if (this.hasClickRight) {
                                this.hasClickRight = false;
                                setCanSliding(this.tCanSlideLeft, this.tCanSlideRight);
                            }
                        } else if (oldScrollX > getDetailViewWidth() / 2) {
                            dx = getDetailViewWidth() - oldScrollX;
                        } else if (oldScrollX <= getDetailViewWidth() / 2) {
                            dx = -oldScrollX;
                            if (this.hasClickRight) {
                                this.hasClickRight = false;
                                setCanSliding(this.tCanSlideLeft, this.tCanSlideRight);
                            }
                        }
                    }
                    smoothScrollTo(dx);
                    break;
                }
                break;
            case 2:
                if (this.mIsBeingDragged) {
                    float deltaX = this.mLastMotionX - x;
                    this.mLastMotionX = x;
                    float oldScrollX2 = (float) this.mSlidingView.getScrollX();
                    float scrollX = oldScrollX2 + deltaX;
                    if (this.canSlideLeft && scrollX > 0.0f) {
                        scrollX = 0.0f;
                    }
                    if (this.canSlideRight && scrollX < 0.0f) {
                        scrollX = 0.0f;
                    }
                    float rightBound;
                    if (deltaX < 0.0f && oldScrollX2 < 0.0f) {
                        rightBound = (float) (-getMenuViewWidth());
                        if (scrollX > 0.0f) {
                            scrollX = 0.0f;
                        } else if (scrollX < rightBound) {
                            scrollX = rightBound;
                        }
                    } else if (deltaX > 0.0f && oldScrollX2 > 0.0f) {
                        rightBound = (float) getDetailViewWidth();
                        if (scrollX < 0.0f) {
                            scrollX = 0.0f;
                        } else if (scrollX > rightBound) {
                            scrollX = rightBound;
                        }
                    }
                    if (this.mSlidingView != null) {
                        this.mSlidingView.scrollTo((int) scrollX, this.mSlidingView.getScrollY());
                        if (scrollX >= 0.0f) {
                            this.bgShade.scrollTo(((int) scrollX) - 20, this.mSlidingView.getScrollY());
                            break;
                        }
                        this.bgShade.scrollTo(((int) scrollX) + 20, this.mSlidingView.getScrollY());
                        break;
                    }
                }
                break;
        }
        return true;
    }

    private int getMenuViewWidth() {
        if (this.mMenuView == null) {
            return 0;
        }
        return this.mMenuView.getWidth();
    }

    private int getDetailViewWidth() {
        if (this.mDetailView == null) {
            return 0;
        }
        return this.mDetailView.getWidth();
    }

    void smoothScrollTo(int dx) {
        int oldScrollX = this.mSlidingView.getScrollX();
        this.mScroller.startScroll(oldScrollX, this.mSlidingView.getScrollY(), dx, this.mSlidingView.getScrollY(), 500);
        invalidate();
    }

    public boolean isShowLeft() {
        return this.isShowLeft;
    }

    public void showLeftView() {
        int menuWidth = this.mMenuView.getWidth();
        int oldScrollX = this.mSlidingView.getScrollX();
        if (oldScrollX == 0) {
            this.mMenuView.setVisibility(0);
            if (this.mDetailView != null) {
                this.mDetailView.setVisibility(4);
            }
            smoothScrollTo(-menuWidth);
            this.tCanSlideLeft = this.canSlideLeft;
            this.tCanSlideRight = this.canSlideRight;
            this.hasClickLeft = true;
            setCanSliding(true, false);
            this.isShowLeft = true;
        } else if (oldScrollX == (-menuWidth)) {
            smoothScrollTo(menuWidth);
            if (this.hasClickLeft) {
                this.hasClickLeft = false;
                this.isShowLeft = false;
                setCanSliding(this.tCanSlideLeft, this.tCanSlideRight);
            }
        }
    }

    public void showRightView() {
        int menuWidth = this.mDetailView.getWidth();
        int oldScrollX = this.mSlidingView.getScrollX();
        if (oldScrollX == 0) {
            this.mMenuView.setVisibility(4);
            this.mDetailView.setVisibility(0);
            smoothScrollTo(menuWidth);
            this.tCanSlideLeft = this.canSlideLeft;
            this.tCanSlideRight = this.canSlideRight;
            this.hasClickRight = true;
            setCanSliding(false, true);
        } else if (oldScrollX == menuWidth) {
            smoothScrollTo(-menuWidth);
            if (this.hasClickRight) {
                this.hasClickRight = false;
                setCanSliding(this.tCanSlideLeft, this.tCanSlideRight);
            }
        }
    }
}
