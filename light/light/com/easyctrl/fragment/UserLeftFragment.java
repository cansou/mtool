package com.easyctrl.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import com.easyctrl.dialog.EnterShowPassDialog.OnEnterlistener;
import com.easyctrl.iface.OnInitLeftSceneListener;
import com.easyctrl.ldy.activity.MainApplication;
import com.easyctrl.ldy.activity.R;
import com.easyctrl.ldy.activity.UserActivity;
import com.easyctrl.ldy.domain.BindBean;
import com.easyctrl.ldy.domain.FloorBean;
import com.easyctrl.ldy.domain.ModulePortBean;
import com.easyctrl.ldy.domain.RoomBean;
import com.easyctrl.ldy.domain.UserScene;
import com.easyctrl.ldy.domain.VirtualBean;
import com.easyctrl.ldy.util.CheckUtil;
import com.easyctrl.manager.OperatorBeanManager;
import com.easyctrl.manager.OrderManage;
import com.easyctrl.ui.base.BaseEasyCtrlFragment;
import java.util.ArrayList;
import java.util.Iterator;

public class UserLeftFragment extends BaseEasyCtrlFragment implements OnClickListener, OnInitLeftSceneListener {
    private static final int UPDATE = 1;
    private long downTime;
    private FloorBean floorBean;
    private Handler handler = new Handler(new Callback() {
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    UserScene userScene = (UserScene) msg.getData().getSerializable("userScene");
                    if (userScene.sbindType == 1) {
                        BindBean bindBean = MainApplication.bindManager.findByID(userScene.pbindID);
                        if (!(bindBean == null || bindBean.keyValue == 0)) {
                            UserLeftFragment.this.userActivity.sendData(OrderManage.bindLongPress(bindBean.bind_moduleID, bindBean.keyValue));
                            break;
                        }
                    }
                    break;
            }
            return false;
        }
    });
    private boolean isDown = false;
    private OperatorBeanManager operatorBeanManager;
    private RoomBean roomBean;
    private String[] sceneName = new String[]{"\u4e00", "\u4e8c", "\u4e09", "\u56db"};
    private Button[] scenes = new Button[4];
    private long upTime;
    private UserActivity userActivity;
    private UserScene userScene;
    private ArrayList<UserScene> userScenes;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onCreateInit();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_left_menu_fragment, container, false);
        findViewByID(view);
        initWidget(view);
        return view;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void sceneInvoke(UserScene userScene, boolean isDown) {
        if (userScene.sbindType == 1) {
            BindBean bindBean = MainApplication.bindManager.findByID(userScene.pbindID);
            if (bindBean != null && bindBean.keyValue != 0) {
                this.userActivity.sendData(OrderManage.bindDownAndUp(bindBean.bind_moduleID, bindBean.keyValue, isDown));
            }
        } else if (userScene.sbindType == 0) {
            VirtualBean virtualBean = MainApplication.virtualManager.findByID(userScene.vbindID);
            if (virtualBean != null && virtualBean.key != 0) {
                this.userActivity.sendData(OrderManage.virtualDownAndUp(virtualBean.key, isDown));
            }
        }
    }

    public void onClick(View v) {
        Button btn = (Button) v;
        Iterator it;
        ModulePortBean bean;
        if (R.id.allopen == v.getId()) {
            ArrayList<ModulePortBean> beans = this.operatorBeanManager.getOperatorArray();
            if (beans != null) {
                it = beans.iterator();
                while (it.hasNext()) {
                    bean = (ModulePortBean) it.next();
                    if (bean != null && bean.type == 0) {
                        try {
                            this.userActivity.sendData(OrderManage.sendLampOrder(bean, 0));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else if (bean.type == 1) {
                        this.userActivity.sendData(OrderManage.sendAnmingOrder(bean, 0));
                    }
                }
            }
        } else if (R.id.allclose == v.getId()) {
            it = this.operatorBeanManager.getOperatorArray().iterator();
            while (it.hasNext()) {
                bean = (ModulePortBean) it.next();
                if (bean.type == 0) {
                    try {
                        this.userActivity.sendData(OrderManage.sendLampOrder(bean, 1));
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                } else if (bean.type == 1) {
                    this.userActivity.sendData(OrderManage.sendAnmingOrder(bean, 1));
                }
            }
        } else if (R.id.btnSet == v.getId()) {
            MainApplication.userManager.setLogin(false);
            this.userActivity.enterShowPassDialog.show();
            this.userActivity.enterShowPassDialog.setOnEnterlistener(new OnEnterlistener() {
                public void onClick(View v, String password) {
                    MainApplication.userManager.setTempSetPws(password);
                    byte[] enterPass = CheckUtil.createEnterPassword(password);
                    if (enterPass == null) {
                        Toast.makeText(UserLeftFragment.this.userActivity, UserLeftFragment.this.userActivity.getResources().getString(R.string.errLoginPassEight), 1).show();
                    } else {
                        UserLeftFragment.this.userActivity.sendData(enterPass);
                    }
                }
            });
        }
    }

    public void onInit(String room) {
        update(room);
    }

    private void update(String room) {
        int i;
        if (room == null || !room.equals("\u5168\u90e8")) {
            this.floorBean = this.userActivity.getFloorBean();
            this.roomBean = this.userActivity.getRoomBean();
            Log.i("data", "fffffff");
            if (this.floorBean != null && this.roomBean != null) {
                this.userScenes = MainApplication.userSceneManager.findByFloorIdAndRoomID(this.floorBean.id, this.roomBean.id);
                for (i = 0; i < this.userScenes.size(); i++) {
                    UserScene scene = (UserScene) this.userScenes.get(i);
                    Log.i("data", "fffffff:" + scene.name);
                    if (scene.name == null || scene.name.length() <= 0) {
                        this.scenes[i].setText("\u573a\u666f" + this.sceneName[i]);
                    } else {
                        this.scenes[i].setText(scene.name);
                    }
                }
                return;
            }
            return;
        }
        for (i = 0; i < 4; i++) {
            this.scenes[i].setText("");
            this.scenes[i].setEnabled(false);
        }
    }

    private void sendDataByUserScene(UserScene userScene) {
        if (this.userScenes != null) {
            sceneInvoke(userScene, true);
        }
    }

    private void longpress() {
        MainApplication.threadPool.submit(new Runnable() {
            public void run() {
                while (UserLeftFragment.this.isDown) {
                    try {
                        UserLeftFragment.this.upTime = System.currentTimeMillis();
                        UserLeftFragment.this.upTime = (UserLeftFragment.this.upTime - UserLeftFragment.this.downTime) / 1000;
                        if (UserLeftFragment.this.upTime >= 3) {
                            Message message = UserLeftFragment.this.handler.obtainMessage();
                            Bundle data = new Bundle();
                            data.putSerializable("userScene", UserLeftFragment.this.userScene);
                            message.setData(data);
                            message.what = 1;
                            UserLeftFragment.this.handler.sendMessage(message);
                            UserLeftFragment.this.isDown = false;
                        }
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    protected void findViewByID(View view) {
        view.findViewById(R.id.btnSet).setOnClickListener(this);
        view.findViewById(R.id.allopen).setOnClickListener(this);
        view.findViewById(R.id.allclose).setOnClickListener(this);
        this.scenes[0] = (Button) view.findViewById(R.id.sence_one);
        this.scenes[0].setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                if (action == 0) {
                    UserLeftFragment.this.downTime = System.currentTimeMillis();
                    UserLeftFragment.this.isDown = true;
                    UserLeftFragment.this.userScene = (UserScene) UserLeftFragment.this.userScenes.get(0);
                    UserLeftFragment.this.longpress();
                    UserLeftFragment.this.sendDataByUserScene(UserLeftFragment.this.userScene);
                } else if (action == 1) {
                    if (UserLeftFragment.this.userScenes != null) {
                        UserLeftFragment.this.sceneInvoke((UserScene) UserLeftFragment.this.userScenes.get(0), false);
                    }
                    UserLeftFragment.this.isDown = false;
                }
                return false;
            }
        });
        this.scenes[1] = (Button) view.findViewById(R.id.sence_two);
        this.scenes[1].setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                if (action == 0) {
                    UserLeftFragment.this.downTime = System.currentTimeMillis();
                    UserLeftFragment.this.isDown = true;
                    UserLeftFragment.this.userScene = (UserScene) UserLeftFragment.this.userScenes.get(1);
                    UserLeftFragment.this.sendDataByUserScene(UserLeftFragment.this.userScene);
                    UserLeftFragment.this.longpress();
                } else if (action == 1) {
                    if (UserLeftFragment.this.userScenes != null) {
                        UserLeftFragment.this.sceneInvoke((UserScene) UserLeftFragment.this.userScenes.get(1), false);
                    }
                    UserLeftFragment.this.isDown = false;
                }
                return false;
            }
        });
        this.scenes[2] = (Button) view.findViewById(R.id.sence_three);
        this.scenes[2].setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                if (action == 0) {
                    UserLeftFragment.this.downTime = System.currentTimeMillis();
                    UserLeftFragment.this.isDown = true;
                    UserLeftFragment.this.userScene = (UserScene) UserLeftFragment.this.userScenes.get(2);
                    UserLeftFragment.this.sendDataByUserScene(UserLeftFragment.this.userScene);
                    UserLeftFragment.this.longpress();
                } else if (action == 1) {
                    if (UserLeftFragment.this.userScenes != null) {
                        UserLeftFragment.this.sceneInvoke((UserScene) UserLeftFragment.this.userScenes.get(2), false);
                    }
                    UserLeftFragment.this.isDown = false;
                }
                return false;
            }
        });
        this.scenes[3] = (Button) view.findViewById(R.id.sence_four);
        this.scenes[3].setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                if (action == 0) {
                    UserLeftFragment.this.downTime = System.currentTimeMillis();
                    UserLeftFragment.this.isDown = true;
                    UserLeftFragment.this.userScene = (UserScene) UserLeftFragment.this.userScenes.get(3);
                    UserLeftFragment.this.longpress();
                    UserLeftFragment.this.sendDataByUserScene(UserLeftFragment.this.userScene);
                } else if (action == 1) {
                    if (UserLeftFragment.this.userScenes != null) {
                        UserLeftFragment.this.sceneInvoke((UserScene) UserLeftFragment.this.userScenes.get(3), false);
                    }
                    UserLeftFragment.this.isDown = false;
                }
                return false;
            }
        });
    }

    protected void initWidget(View view) {
        update(null);
    }

    protected void onCreateInit() {
        this.userActivity = (UserActivity) getActivity();
        this.operatorBeanManager = OperatorBeanManager.getInstance();
        this.userActivity.setOnInitLeftSceneListener(this);
    }
}
