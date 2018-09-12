package com.easyctrl.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import com.easyctrl.adapter.HourSheelAdatper;
import com.easyctrl.adapter.MinSheelAdatper;
import com.easyctrl.dialog.DateDialog;
import com.easyctrl.dialog.DescribeDialog;
import com.easyctrl.dialog.EasyProgressDialog;
import com.easyctrl.dialog.TimeTypeDialog;
import com.easyctrl.dialog.TimeWeekDialog;
import com.easyctrl.dialog.WaitDialog;
import com.easyctrl.dialog.WaitDialog.OnWorkdListener;
import com.easyctrl.event.EasyEventTypeHandler;
import com.easyctrl.iface.OnTransmitText;
import com.easyctrl.ldy.activity.MainApplication;
import com.easyctrl.ldy.activity.R;
import com.easyctrl.ldy.activity.UserActivity;
import com.easyctrl.ldy.domain.TimerBean;
import com.easyctrl.ldy.net.SendDataSocket;
import com.easyctrl.ldy.net.SendDataSocket.OnCloseListener;
import com.easyctrl.ldy.util.FileUtil;
import com.easyctrl.ldy.util.StringUtil;
import com.easyctrl.ldy.util.TimeUtil;
import com.easyctrl.ldy.view.NavigataView;
import com.easyctrl.ldy.view.ShowHostTimeView;
import com.easyctrl.manager.OrderManage;
import com.easyctrl.sheelview.OnWheelChangedListener;
import com.easyctrl.sheelview.WheelView;
import com.easyctrl.ui.base.BaseActivity;
import com.easyctrl.ui.base.BaseEasyCtrlFragment;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map.Entry;

public class TimerBindSet extends BaseEasyCtrlFragment implements OnClickListener, OnTransmitText, OnWheelChangedListener {
    private String TAG = TimerBindSet.class.getSimpleName();
    private NavigataView backview;
    private DateDialog dateDialog;
    private DescribeDialog describeDialog;
    private TextView descript;
    private TimeTypeDialog dialog;
    Handler handler = new Handler(new Callback() {
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    EasyProgressDialog.getInstance(TimerBindSet.this.userActivity).show(TimerBindSet.this.progressDialog, "\u6b63\u5728\u4e0a\u4f20\u6570\u636e......", TimerBindSet.this.TAG);
                    break;
                case 2:
                    EasyProgressDialog.getInstance(TimerBindSet.this.userActivity).dismiss(TimerBindSet.this.progressDialog);
                    break;
            }
            return false;
        }
    });
    private HourSheelAdatper hourSheelAdatper;
    private MinSheelAdatper minSheelAdatper;
    private ProgressDialog progressDialog;
    private TextView repeat;
    private int saveType = 3;
    private ShowHostTimeView showTimeView;
    private ArrayList<TimerBean> timeBeans;
    private TimeUtil timeUtil;
    private TimeWeekDialog timeWeekDialog;
    private TimerBean timerBean;
    private UserActivity userActivity;
    private WheelView wheelHour;
    private WheelView wheelMin;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.userActivity = (UserActivity) getActivity();
        this.dialog = new TimeTypeDialog(this.userActivity);
        this.describeDialog = new DescribeDialog(this.userActivity);
        this.timeWeekDialog = new TimeWeekDialog(this.userActivity);
        this.userActivity.sendData(OrderManage.getHostCurrentTime());
        this.progressDialog = new ProgressDialog(this.userActivity);
    }

    public void onDestroy() {
        super.onDestroy();
        this.showTimeView.cancel();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.timeUtil = TimeUtil.getTimeUtil();
        View view = inflater.inflate(R.layout.timer_bind_set, null);
        findViewByID(view);
        initWidget(view);
        return view;
    }

    public void onEventMainThread(EasyEventTypeHandler eventHandler) {
        if (eventHandler.getType() == 2) {
            this.showTimeView.setHostTime(eventHandler.getHostTime());
        }
    }

    private String getArea(TimerBean timerBean) {
        if (timerBean.floor == null || timerBean.floor.equals("\u8bbe\u5907")) {
            return "\u672a\u5206\u914d";
        }
        return timerBean.floor + " / " + timerBean.room + " / " + timerBean.device;
    }

    private String getInfo(TimerBean timerBean) {
        if (timerBean.type == 2) {
            if (timerBean.moduleID > 0 && timerBean.isBind == 1) {
                if (timerBean.type == 2) {
                    return "\u9762\u677fID:" + timerBean.moduleID + " KEY:" + timerBean.key + "  \u573a\u666f";
                }
                return "\u9762\u677fID:" + timerBean.moduleID + " KEY:" + timerBean.key + "  \u7ee7\u7535\u5668ID:" + timerBean.bindModuleID + " PORT:" + timerBean.bindPort;
            }
        } else if (timerBean.type == 1) {
            if (timerBean.isBind == 1) {
                if (timerBean.type == 2) {
                    return "\u865a\u62df\u952e  KEY:" + timerBean.key;
                }
                return "\u865a\u62df\u952e  KEY:" + timerBean.key + " \u573a\u666f";
            }
        } else if (timerBean.type == 3) {
            return "\u573a\u666f\u53f7\uff1a" + timerBean.bind_info_sceneID;
        }
        return null;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.hourSheelAdatper = HourSheelAdatper.getAdapter(MainApplication.mContext.getResources().getStringArray(R.array.hours));
        this.wheelHour.setAdapter(this.hourSheelAdatper);
        this.wheelHour.setCurrentItem(this.timeUtil.getHour());
        this.minSheelAdatper = MinSheelAdatper.getAdapter(MainApplication.mContext.getResources().getStringArray(R.array.mins));
        this.wheelMin.setAdapter(this.minSheelAdatper);
        this.wheelMin.setCurrentItem(this.timeUtil.getMinute());
        if (this.timerBean.weekString == null || this.timerBean.weekString.length() < 1 || this.timerBean.weekString.equals("\u6bcf\u5929")) {
            this.timerBean.year = MotionEventCompat.ACTION_MASK;
            this.timerBean.mon = MotionEventCompat.ACTION_MASK;
            this.timerBean.date = MotionEventCompat.ACTION_MASK;
            this.timerBean.day = MotionEventCompat.ACTION_MASK;
        }
        this.repeat.setText(this.timerBean.weekString);
        this.descript.setText(this.timerBean.description);
        this.userActivity.weekArray.clear();
        if (this.timerBean.charArray != null) {
            String[] arr = this.timerBean.charArray.split(",");
            for (int i = 0; i < arr.length; i++) {
                if (!arr[i].trim().equals("")) {
                    this.userActivity.weekArray.add(Integer.valueOf(arr[i].trim()).intValue(), "");
                }
            }
            return;
        }
        this.repeat.setText("\u6bcf\u5929");
    }

    public void onClick(View v) {
        if (v.getId() == R.id.infor) {
            this.dialog.setOnTransmitText(this);
            this.dialog.show();
        } else if (v.getId() == R.id.discript) {
            this.describeDialog.setOnTransmitText(this);
            this.describeDialog.show();
        } else if (v.getId() == R.id.leftbutton) {
            this.userActivity.popFragments(BaseActivity.USRT, R.id.center_layout, true);
        } else if (v.getId() == R.id.rigthbutton) {
            new WaitDialog(this.userActivity, new OnWorkdListener() {
                public void doingWork() {
                    TimerBindSet.this.handler.sendEmptyMessage(1);
                    TimerBindSet.this.createTime();
                    String strTime = MainApplication.jsonManager.createTimerJson(TimerBindSet.this.timeBeans);
                    if (strTime != null) {
                        Log.i("data", strTime);
                    } else {
                        Log.i("data", "strTime is null");
                    }
                    SendDataSocket sendDataSocket = null;
                    try {
                        sendDataSocket = new SendDataSocket(MainApplication.userManager.currentHost(), 6001);
                    } catch (Exception e) {
                        e.printStackTrace();
                        TimerBindSet.this.handler.sendEmptyMessage(2);
                    }
                    sendDataSocket.setOnCloseListener(new OnCloseListener() {
                        public void onClose() {
                            TimerBindSet.this.userActivity.sendData(OrderManage.time_order(TimerBindSet.this.timerBean.timeID, (byte) 0));
                            TimerBindSet.this.timerBean.weekString = TimerBindSet.this.repeat.getText().toString();
                            TimerBindSet.this.timerBean.description = TimerBindSet.this.descript.getText().toString();
                            TimerBindSet.this.timerBean.time_type = TimerBindSet.this.saveType;
                            TimerBindSet.this.timerBean.charArray = StringUtil.listToArray(TimerBindSet.this.userActivity.weekArray.getKeys());
                            TimerBindSet.this.timerBean.isOpen = 0;
                            MainApplication.timerManager.update(TimerBindSet.this.timerBean, TimerBindSet.this.timerBean.timeID);
                            TimerBindSet.this.handler.sendEmptyMessage(2);
                            TimerBindSet.this.userActivity.popFragments(BaseActivity.USRT, R.id.center_layout, true);
                        }
                    });
                    try {
                        FileUtil.backupData();
                        sendDataSocket.sendData("down /json/t" + String.valueOf(TimerBindSet.this.timerBean.timeID) + ".json$ " + strTime);
                    } catch (Exception e2) {
                        e2.printStackTrace();
                        TimerBindSet.this.handler.sendEmptyMessage(2);
                    }
                }
            }, null).execute(new Integer[0]);
        }
    }

    private void createTime() {
        this.timeBeans = new ArrayList();
        String obj;
        String bindID;
        if (this.userActivity.weekArray.size() == 0) {
            this.timerBean.min = Integer.valueOf(this.minSheelAdatper.getItmeData(this.wheelMin.getCurrentItem())).intValue();
            this.timerBean.hour = Integer.valueOf(this.hourSheelAdatper.getItmeData(this.wheelHour.getCurrentItem())).intValue();
            obj = null;
            this.timerBean.data = 2;
            bindID = Integer.toHexString(this.timerBean.bindid);
            if (bindID.length() == 1) {
                obj = "fe10000" + bindID;
            } else if (bindID.length() == 2) {
                obj = "fe1000" + bindID;
            } else if (bindID.length() == 3) {
                obj = "fe100" + bindID;
            }
            this.timerBean.obj = obj;
            this.timeBeans.add(this.timerBean);
            return;
        }
        for (Entry entry : this.userActivity.weekArray.getWeekMap().entrySet()) {
            Object key = entry.getKey();
            this.timerBean.year = MotionEventCompat.ACTION_MASK;
            this.timerBean.mon = MotionEventCompat.ACTION_MASK;
            this.timerBean.date = MotionEventCompat.ACTION_MASK;
            this.timerBean.min = Integer.valueOf(this.minSheelAdatper.getItmeData(this.wheelMin.getCurrentItem())).intValue();
            this.timerBean.hour = Integer.valueOf(this.hourSheelAdatper.getItmeData(this.wheelHour.getCurrentItem())).intValue();
            TimerBean bean = new TimerBean();
            bean.year = this.timerBean.year;
            bean.mon = this.timerBean.mon;
            bean.date = this.timerBean.date;
            bean.day = Integer.valueOf(key.toString()).intValue();
            bean.min = this.timerBean.min;
            bean.hour = this.timerBean.hour;
            obj = null;
            bean.data = 2;
            bindID = Integer.toHexString(this.timerBean.bindid);
            if (bindID.length() == 1) {
                obj = "fe10000" + bindID;
            } else if (bindID.length() == 2) {
                obj = "fe1000" + bindID;
            } else if (bindID.length() == 3) {
                obj = "fe100" + bindID;
            }
            bean.obj = obj;
            this.timeBeans.add(bean);
        }
    }

    public void transmit(String text, int type, int timetype, Date date) {
        if (type == 1) {
            this.descript.setText(text);
        } else if (type == 2) {
            this.saveType = timetype;
            if (timetype == 1) {
                this.timeWeekDialog.setOnTransmitText(this);
                this.timeWeekDialog.show();
            } else if (timetype == 2) {
                this.dateDialog = new DateDialog(this.userActivity);
                this.dateDialog.setOnTransmitText(this);
                this.dateDialog.show();
            } else if (timetype == 6) {
                this.repeat.setText(text);
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                this.userActivity.weekArray.clear();
                this.timerBean.year = cal.get(1);
                this.timerBean.mon = cal.get(2) + 1;
                this.timerBean.date = cal.get(5);
                this.timerBean.day = MotionEventCompat.ACTION_MASK;
            } else if (timetype == 3) {
                this.timerBean.year = MotionEventCompat.ACTION_MASK;
                this.timerBean.mon = MotionEventCompat.ACTION_MASK;
                this.timerBean.date = MotionEventCompat.ACTION_MASK;
                this.timerBean.day = MotionEventCompat.ACTION_MASK;
                this.repeat.setText(text);
            } else {
                this.repeat.setText(text);
            }
        }
    }

    public void setTimeBean(TimerBean bean) {
        this.timerBean = bean;
    }

    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        if (this.hourSheelAdatper != null && this.minSheelAdatper != null) {
            byte effectHour = Byte.valueOf(this.hourSheelAdatper.getItmeData(this.wheelHour.getCurrentItem())).byteValue();
            Byte.valueOf(this.minSheelAdatper.getItmeData(this.wheelMin.getCurrentItem())).byteValue();
        }
    }

    protected void findViewByID(View view) {
        this.showTimeView = (ShowHostTimeView) view.findViewById(R.id.start);
        this.wheelHour = (WheelView) view.findViewById(R.id.hour);
        this.wheelMin = (WheelView) view.findViewById(R.id.min);
        this.descript = (TextView) view.findViewById(R.id.distext);
        this.repeat = (TextView) view.findViewById(R.id.onlytext);
        this.backview = (NavigataView) view.findViewById(R.id.backview);
        TextView cinfo = (TextView) view.findViewById(R.id.cinfo);
        ((TextView) view.findViewById(R.id.carea)).setText(getArea(this.timerBean));
        cinfo.setText(getInfo(this.timerBean));
    }

    protected void initWidget(View view) {
        this.wheelHour.addChangingListener(this);
        this.wheelMin.addChangingListener(this);
        this.backview.findViewById(R.id.leftbutton).setOnClickListener(this);
        this.backview.findViewById(R.id.rigthbutton).setOnClickListener(this);
        view.findViewById(R.id.infor).setOnClickListener(this);
        view.findViewById(R.id.discript).setOnClickListener(this);
    }

    protected void onCreateInit() {
    }
}
