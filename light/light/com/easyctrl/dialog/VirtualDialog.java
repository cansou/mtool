package com.easyctrl.dialog;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.easyctrl.adapter.TextSelectAdapter;
import com.easyctrl.dialog.WaitDialog.OnWorkdListener;
import com.easyctrl.fragment.VirtualFragmentList;
import com.easyctrl.ldy.activity.MainApplication;
import com.easyctrl.ldy.activity.R;
import com.easyctrl.ldy.activity.SettingActivity;
import com.easyctrl.ldy.domain.ModulePortBean;
import com.easyctrl.ldy.domain.VirtualBean;
import com.easyctrl.ldy.net.SendDataSocket;
import com.easyctrl.ldy.net.SendDataSocket.OnCloseListener;
import com.easyctrl.manager.OrderManage;
import com.easyctrl.ui.base.BaseActivity;
import com.easyctrl.ui.base.BaseDialog;
import java.util.ArrayList;

public class VirtualDialog extends BaseDialog implements OnClickListener {
    public static final int TYPE_START_GRID = 1;
    public static final int TYPE_START_Nomer = 2;
    private String TAG = VirtualDialog.class.getSimpleName();
    private Handler handler;
    private boolean isScene = false;
    private LinearLayout mainLayout;
    private String modelString;
    private Spinner modle;
    private ArrayList<ModulePortBean> selectBeans;
    private SettingActivity settingActivity;
    private int startType;
    private EditText textSceneName;
    private VirtualBean virtualBean;
    private View virtualScene;
    private View virtualSimple;
    private EditText virtualText;

    public void setSelectBeans(ArrayList<ModulePortBean> selectBeans) {
        this.selectBeans = selectBeans;
    }

    public void setStartType(int startType) {
        this.startType = startType;
    }

    public VirtualDialog(Context context) {
        super(context);
        this.mContext = context;
        this.settingActivity = (SettingActivity) this.mContext;
        setContentView((int) R.layout.bind_main_dialog);
        this.mInflater = LayoutInflater.from(this.mContext);
        this.virtualScene = this.mInflater.inflate(R.layout.virtual_scene_dialog, null);
        this.virtualSimple = this.mInflater.inflate(R.layout.virtual_simple_dialog, null);
        findView();
        sceneFindView();
        simpFindView();
    }

    private void simpFindView() {
        this.virtualSimple.findViewById(R.id.cancel).setOnClickListener(this);
        this.virtualSimple.findViewById(R.id.enter).setOnClickListener(this);
    }

    private void sceneFindView() {
        this.virtualScene.findViewById(R.id.cancel).setOnClickListener(this);
        this.virtualScene.findViewById(R.id.enter).setOnClickListener(this);
    }

    private void findView() {
        this.mainLayout = (LinearLayout) findViewById(R.id.content);
        findViewById(R.id.dialog_scene).setOnClickListener(this);
        findViewById(R.id.dialog_simple).setOnClickListener(this);
    }

    public void onClick(View v) {
        if (v.getId() == R.id.dialog_scene) {
            if (this.selectBeans != null && this.selectBeans.size() == 1) {
                this.mainLayout.removeAllViews();
                sceneShow();
                this.mainLayout.addView(this.virtualScene);
                this.isScene = false;
            }
        } else if (v.getId() == R.id.dialog_simple) {
            if (this.selectBeans != null && this.selectBeans.size() == 1) {
                this.mainLayout.removeAllViews();
                simpleShow();
                this.mainLayout.addView(this.virtualSimple);
                this.isScene = true;
            }
        } else if (v.getId() == R.id.cancel) {
            dismiss();
        } else if (v.getId() != R.id.enter || this.isScene) {
            if (v.getId() == R.id.enter && this.isScene) {
                SettingActivity activity = this.mContext;
                if (this.modelString.equals("\u7ed1\u5b9a\u6e05\u9664")) {
                    clear();
                    activity.sendData(OrderManage.virtual_bind_delete(this.virtualBean.key));
                    dismiss();
                    return;
                }
                activity.sendData(OrderManage.virtyalBindPhysics((ModulePortBean) this.selectBeans.get(0), this.virtualBean.key, this.modelString));
                dismiss();
                selectBean = (ModulePortBean) this.selectBeans.get(0);
                this.virtualBean.floor = selectBean.floor;
                this.virtualBean.room = selectBean.room;
                this.virtualBean.deviceName = selectBean.name;
                this.virtualBean.moduleID = selectBean.moduleID;
                this.virtualBean.port = selectBean.port;
                this.virtualBean.bindType = 1;
                this.virtualBean.model = this.modelString;
                MainApplication.virtualManager.update(this.virtualBean, this.virtualBean.virtualID);
                isBackStart();
            }
        } else if (this.virtualText.getText().toString().length() == 0) {
            Toast.makeText(this.mContext, "\u573a\u666f\u53f7\u4e0d\u80fd\u4e3a\u7a7a", 1).show();
        } else {
            new WaitDialog(this.mContext, new OnWorkdListener() {
                public void doingWork() {
                    VirtualDialog.this.sendMessageToHandler(1);
                    Log.i("data", "key:" + VirtualDialog.this.virtualBean.key);
                    VirtualDialog.this.settingActivity.sendData(OrderManage.virtual_bind_delete(VirtualDialog.this.virtualBean.v_scene_id));
                    String scene = MainApplication.jsonManager.createSenceJson(VirtualDialog.this.selectBeans, VirtualDialog.this.mContext);
                    SendDataSocket sendDataSocket = null;
                    try {
                        sendDataSocket = new SendDataSocket(MainApplication.userManager.currentHost(), 6001);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                        VirtualDialog.this.sendMessageToHandler(2);
                    }
                    if (sendDataSocket != null) {
                        sendDataSocket.setOnCloseListener(new OnCloseListener() {
                            public void onClose() {
                                try {
                                    VirtualDialog.this.settingActivity.sendData(OrderManage.virtualBingSence(VirtualDialog.this.virtualBean.v_scene_id, VirtualDialog.this.virtualBean.key));
                                    VirtualDialog.this.sendMessageToHandler(2);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    VirtualDialog.this.handler.sendEmptyMessage(2);
                                    VirtualDialog.this.sendMessageToHandler(2);
                                }
                            }
                        });
                        try {
                            sendDataSocket.sendData("down /json/s" + String.valueOf(VirtualDialog.this.virtualBean.v_scene_id) + ".json$ " + scene);
                        } catch (Exception e) {
                            e.printStackTrace();
                            VirtualDialog.this.sendMessageToHandler(2);
                        }
                        VirtualDialog.this.sendMessageToHandler(2);
                    }
                }
            }, null).execute(new Integer[0]);
            dismiss();
            if (this.selectBeans != null && this.selectBeans.size() != 0) {
                selectBean = (ModulePortBean) this.selectBeans.get(0);
                this.virtualBean.name = this.textSceneName.getText().toString();
                this.virtualBean.floor = selectBean.floor;
                this.virtualBean.room = selectBean.room;
                this.virtualBean.deviceName = selectBean.name;
                this.virtualBean.moduleID = selectBean.moduleID;
                this.virtualBean.port = selectBean.port;
                this.virtualBean.bindType = 2;
                MainApplication.virtualManager.update(this.virtualBean, this.virtualBean.virtualID);
                isBackStart();
            }
        }
    }

    private void clear() {
        this.virtualBean.bindType = 0;
        MainApplication.virtualManager.update(this.virtualBean, this.virtualBean.virtualID);
    }

    private void isBackStart() {
        if (this.startType == 1) {
            this.settingActivity.clearStackByTag(BaseActivity.SETTING);
            this.settingActivity.pushFragments(BaseActivity.SETTING, new VirtualFragmentList(), true, true, R.id.setting_center);
        }
    }

    public void setBindBean(VirtualBean virtualBean) {
        this.virtualBean = virtualBean;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    private void sendMessageToHandler(int messageType) {
        if (this.handler != null) {
            this.handler.sendEmptyMessage(messageType);
        }
    }

    public void show() {
        if (this.selectBeans != null && this.selectBeans.size() == 1) {
            this.mainLayout.removeAllViews();
            simpleShow();
            this.mainLayout.addView(this.virtualSimple);
            this.isScene = true;
            super.show();
        } else if (this.selectBeans != null && this.selectBeans.size() > 1) {
            this.mainLayout.removeAllViews();
            sceneShow();
            this.mainLayout.addView(this.virtualScene);
            this.isScene = false;
            super.show();
        }
    }

    private void sceneShow() {
        ((TextView) this.virtualScene.findViewById(R.id.textPanleNum)).setText(this.virtualBean.key);
        this.virtualText = (EditText) this.virtualScene.findViewById(R.id.textScene);
        this.virtualText.setText(this.virtualBean.v_scene_id);
        ((EditText) this.virtualScene.findViewById(R.id.textSceneName)).setText("\u573a\u666f " + this.virtualBean.v_scene_id);
        this.textSceneName = (EditText) this.virtualScene.findViewById(R.id.textSceneName);
        ((CheckBox) this.virtualScene.findViewById(R.id.checKey)).setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    VirtualDialog.this.virtualText.setEnabled(false);
                } else {
                    VirtualDialog.this.virtualText.setEnabled(true);
                }
            }
        });
        ((CheckBox) this.virtualScene.findViewById(R.id.check)).setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    VirtualDialog.this.textSceneName.setEnabled(false);
                } else {
                    VirtualDialog.this.textSceneName.setEnabled(true);
                }
            }
        });
    }

    private void simpleShow() {
        ((TextView) this.virtualSimple.findViewById(R.id.textPanleNum)).setText(this.virtualBean.key);
        this.modle = (Spinner) this.virtualSimple.findViewById(R.id.modle);
        TextSelectAdapter typeAdapter = null;
        if (this.selectBeans.size() == 1) {
            if (((ModulePortBean) this.selectBeans.get(0)).type == 0) {
                typeAdapter = new TextSelectAdapter(this.mContext, this.mContext.getResources().getStringArray(R.array.simpleModel));
            } else if (((ModulePortBean) this.selectBeans.get(0)).type == 1) {
                typeAdapter = new TextSelectAdapter(this.mContext, this.mContext.getResources().getStringArray(R.array.simpleAmining));
            }
        }
        this.modle.setAdapter(typeAdapter);
        this.modle.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                if (((ModulePortBean) VirtualDialog.this.selectBeans.get(0)).type == 0) {
                    VirtualDialog.this.modelString = MainApplication.mContext.getResources().getStringArray(R.array.simpleModel)[position];
                } else if (((ModulePortBean) VirtualDialog.this.selectBeans.get(0)).type == 1) {
                    VirtualDialog.this.modelString = MainApplication.mContext.getResources().getStringArray(R.array.simpleAmining)[position];
                }
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    public void setVirtualBean(VirtualBean virtualBean) {
        this.virtualBean = virtualBean;
    }
}
