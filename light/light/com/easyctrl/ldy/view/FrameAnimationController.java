package com.easyctrl.ldy.view;

import android.os.Handler;
import android.os.Message;

public class FrameAnimationController {
    public static final int ANIMATION_FRAME_DURATION = 16;
    private static final int MSG_ANIMATE = 1000;
    private static final Handler mHandler = new AnimationHandler();

    private static class AnimationHandler extends Handler {
        private AnimationHandler() {
        }

        public void handleMessage(Message m) {
            switch (m.what) {
                case FrameAnimationController.MSG_ANIMATE /*1000*/:
                    if (m.obj != null) {
                        ((Runnable) m.obj).run();
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    }

    private FrameAnimationController() {
        throw new UnsupportedOperationException();
    }

    public static void requestAnimationFrame(Runnable runnable) {
        Message message = new Message();
        message.what = MSG_ANIMATE;
        message.obj = runnable;
        mHandler.sendMessageDelayed(message, 16);
    }

    public static void requestFrameDelay(Runnable runnable, long delay) {
        Message message = new Message();
        message.what = MSG_ANIMATE;
        message.obj = runnable;
        mHandler.sendMessageDelayed(message, delay);
    }
}
