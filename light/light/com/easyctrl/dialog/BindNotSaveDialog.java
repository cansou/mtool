package com.easyctrl.dialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
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
import com.easyctrl.dialog.SimpleDialog.OnOptionListener;
import com.easyctrl.dialog.WaitDialog.OnPostExecuteListener;
import com.easyctrl.dialog.WaitDialog.OnWorkdListener;
import com.easyctrl.event.EasyEventTypeHandler;
import com.easyctrl.fragment.BindFragment;
import com.easyctrl.ldy.activity.MainApplication;
import com.easyctrl.ldy.activity.R;
import com.easyctrl.ldy.activity.SettingActivity;
import com.easyctrl.ldy.domain.BindBean;
import com.easyctrl.ldy.domain.ModulePortBean;
import com.easyctrl.ldy.domain.SceneBean;
import com.easyctrl.ldy.net.SendDataSocket;
import com.easyctrl.ldy.net.SendDataSocket.OnCloseListener;
import com.easyctrl.ldy.util.Value;
import com.easyctrl.manager.OrderManage;
import com.easyctrl.ui.base.BaseActivity;
import com.easyctrl.ui.base.BaseDialog;
import de.greenrobot.event.EventBus;
import java.util.ArrayList;

public class BindNotSaveDialog extends BaseDialog implements OnClickListener {
    public static final int TYPE_START_GRID = 1;
    public static final int TYPE_START_Nomer = 2;
    private String TAG = BindNotSaveDialog.class.getSimpleName();
    private SettingActivity activity;
    private CheckBox autoKey;
    private BindBean bindBean;
    private CheckBox checkScene;
    private CheckBox checkStat;
    private Handler handler = new Handler(new Callback() {
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case Value.BindDialog_HANDLER_CANNEL_DIALOG /*13*/:
                    EasyProgressDialog.getInstance(BindNotSaveDialog.this.mContext).dismiss(BindNotSaveDialog.this.progressDialog);
                    BindNotSaveDialog.this.isBackStart();
                    break;
                case Value.BindDialog_HANDLER_SHOW_DIALOG /*14*/:
                    EasyProgressDialog.getInstance(BindNotSaveDialog.this.mContext).show(BindNotSaveDialog.this.progressDialog, "\u6b63\u5728\u4e0a\u4f20\u6570\u636e......", BindNotSaveDialog.this.TAG);
                    break;
            }
            return false;
        }
    });
    private boolean isBack = false;
    private boolean isScene = false;
    private int lastID;
    private LinearLayout mainLayout;
    private String modelString;
    private Spinner modle;
    private ProgressDialog progressDialog;
    private Spinner sModel;
    private SceneBean sceneBean;
    private View sceneLayout;
    private String sceneModel = "\u6309\u4e0b\u8c03\u7528";
    private EditText scenenNum;
    private ArrayList<ModulePortBean> selectBeans;
    private View simpleLayout;
    private int startType;
    private EditText textSceneName;

    public void setSelectBeans(ArrayList<ModulePortBean> selectBeans) {
        this.selectBeans = selectBeans;
    }

    public void setStartType(int startType) {
        this.startType = startType;
    }

    public BindNotSaveDialog(Context context) {
        super(context);
        this.mContext = context;
        this.progressDialog = new ProgressDialog(context);
        this.activity = (SettingActivity) this.mContext;
        this.mInflater = LayoutInflater.from(context);
        this.sceneLayout = this.mInflater.inflate(R.layout.bind_scene_dialog, null);
        this.simpleLayout = this.mInflater.inflate(R.layout.bind_simple_dialog, null);
        setContentView((int) R.layout.bind_main_dialog);
        findView();
        sceneFindView();
        simpFindView();
    }

    private void findView() {
        this.mainLayout = (LinearLayout) findViewById(R.id.content);
        findViewById(R.id.dialog_scene).setOnClickListener(this);
        findViewById(R.id.dialog_simple).setOnClickListener(this);
    }

    private void sceneFindView() {
        this.sceneLayout.findViewById(R.id.cancel).setOnClickListener(this);
        this.sceneLayout.findViewById(R.id.enter).setOnClickListener(this);
    }

    private void simpFindView() {
        this.simpleLayout.findViewById(R.id.cancel).setOnClickListener(this);
        this.simpleLayout.findViewById(R.id.enter).setOnClickListener(this);
    }

    private void simpleShow() {
        ((TextView) this.simpleLayout.findViewById(R.id.textPanleNum)).setText(this.bindBean.bind_moduleID);
        ((TextView) this.simpleLayout.findViewById(R.id.textKey)).setText(this.bindBean.keyValue);
        this.checkStat = (CheckBox) this.simpleLayout.findViewById(R.id.checkStat);
        this.modle = (Spinner) this.simpleLayout.findViewById(R.id.modle);
        TextSelectAdapter typeAdapter = null;
        if (this.selectBeans.size() == 1) {
            if (((ModulePortBean) this.selectBeans.get(0)).type == 0) {
                typeAdapter = new TextSelectAdapter(this.mContext, MainApplication.mContext.getResources().getStringArray(R.array.simpleModel));
            } else if (((ModulePortBean) this.selectBeans.get(0)).type == 1) {
                typeAdapter = new TextSelectAdapter(this.mContext, MainApplication.mContext.getResources().getStringArray(R.array.simpleAmining));
            }
        }
        this.modle.setAdapter(typeAdapter);
        this.modle.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                if (((ModulePortBean) BindNotSaveDialog.this.selectBeans.get(0)).type == 0) {
                    BindNotSaveDialog.this.modelString = MainApplication.mContext.getResources().getStringArray(R.array.simpleModel)[position];
                } else if (((ModulePortBean) BindNotSaveDialog.this.selectBeans.get(0)).type == 1) {
                    BindNotSaveDialog.this.modelString = MainApplication.mContext.getResources().getStringArray(R.array.simpleAmining)[position];
                }
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private void sceneShow() {
        ((TextView) this.sceneLayout.findViewById(R.id.textPanleNum)).setText(this.bindBean.bind_moduleID);
        ((TextView) this.sceneLayout.findViewById(R.id.textKey)).setText(this.bindBean.keyValue);
        this.scenenNum = (EditText) this.sceneLayout.findViewById(R.id.textScene);
        this.scenenNum.setText(String.valueOf(this.lastID));
        this.textSceneName = (EditText) this.sceneLayout.findViewById(R.id.textSceneName);
        this.textSceneName.setText("\u573a\u666f " + this.lastID);
        this.checkScene = (CheckBox) this.sceneLayout.findViewById(R.id.check);
        this.autoKey = (CheckBox) this.sceneLayout.findViewById(R.id.checKey);
        this.sModel = (Spinner) this.sceneLayout.findViewById(R.id.smodel);
        TextSelectAdapter typeAdapter = null;
        if (this.selectBeans.size() > 0) {
            typeAdapter = new TextSelectAdapter(this.mContext, MainApplication.mContext.getResources().getStringArray(R.array.sceneModel));
        }
        this.sModel.setAdapter(typeAdapter);
        this.sModel.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                BindNotSaveDialog.this.sceneModel = MainApplication.mContext.getResources().getStringArray(R.array.sceneModel)[position];
                if (BindNotSaveDialog.this.sceneModel.equals("\u7ed1\u5b9a\u6e05\u9664")) {
                    BindNotSaveDialog.this.scenenNum.setText("0");
                    BindNotSaveDialog.this.textSceneName.setText("\u573a\u666f 0");
                    return;
                }
                ArrayList<SceneBean> list = MainApplication.sceneManager.findByIDandKey(BindNotSaveDialog.this.bindBean.bind_moduleID, BindNotSaveDialog.this.bindBean.keyValue);
                if (list == null || list.size() <= 0) {
                    BindNotSaveDialog.this.lastID = MainApplication.sceneManager.getLastID();
                } else {
                    BindNotSaveDialog.this.sceneBean = (SceneBean) list.get(0);
                    BindNotSaveDialog.this.lastID = Integer.valueOf(BindNotSaveDialog.this.sceneBean.sceneBean_jsonName).intValue();
                }
                BindNotSaveDialog.this.scenenNum.setText(BindNotSaveDialog.this.lastID);
                BindNotSaveDialog.this.textSceneName.setText("\u573a\u666f " + BindNotSaveDialog.this.lastID);
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        if (this.checkScene.isChecked()) {
            this.textSceneName.setEnabled(false);
            this.textSceneName.setFocusable(false);
        } else {
            this.textSceneName.setEnabled(true);
            this.textSceneName.setFocusable(true);
        }
        if (this.autoKey.isChecked()) {
            this.scenenNum.setEnabled(false);
            this.scenenNum.setFocusable(false);
        } else {
            this.scenenNum.setEnabled(true);
            this.scenenNum.setFocusable(true);
        }
        this.autoKey.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    BindNotSaveDialog.this.scenenNum.setEnabled(false);
                    BindNotSaveDialog.this.scenenNum.setFocusable(false);
                    return;
                }
                BindNotSaveDialog.this.scenenNum.setEnabled(true);
                BindNotSaveDialog.this.scenenNum.setFocusable(true);
                BindNotSaveDialog.this.scenenNum.setFocusableInTouchMode(true);
            }
        });
        this.checkScene.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    BindNotSaveDialog.this.textSceneName.setEnabled(false);
                    BindNotSaveDialog.this.textSceneName.setFocusable(false);
                    BindNotSaveDialog.this.textSceneName.setText("\u573a\u666f " + BindNotSaveDialog.this.bindBean.bindID);
                    return;
                }
                BindNotSaveDialog.this.textSceneName.setEnabled(true);
                BindNotSaveDialog.this.textSceneName.setFocusable(true);
                BindNotSaveDialog.this.textSceneName.setFocusableInTouchMode(true);
            }
        });
    }

    public void show() {
        this.lastID = MainApplication.sceneManager.getLastID();
        ArrayList<SceneBean> list = MainApplication.sceneManager.findByIDandKey(this.bindBean.bind_moduleID, this.bindBean.keyValue);
        if (list != null && list.size() > 0) {
            this.lastID = Integer.valueOf(((SceneBean) list.get(0)).sceneBean_jsonName).intValue();
        }
        Log.i("data", "lastID:" + this.lastID);
        if (this.selectBeans != null && this.selectBeans.size() == 1) {
            this.mainLayout.removeAllViews();
            simpleShow();
            this.mainLayout.addView(this.simpleLayout);
            this.isScene = true;
            super.show();
        } else if (this.selectBeans != null && this.selectBeans.size() > 1) {
            this.mainLayout.removeAllViews();
            sceneShow();
            this.mainLayout.addView(this.sceneLayout);
            this.isScene = false;
            super.show();
        }
    }

    public void onClick(View v) {
        if (v.getId() == R.id.dialog_scene) {
            if (this.selectBeans != null && this.selectBeans.size() == 1) {
                this.mainLayout.removeAllViews();
                sceneShow();
                this.mainLayout.addView(this.sceneLayout);
                this.isScene = false;
            }
        } else if (v.getId() == R.id.dialog_simple) {
            if (this.selectBeans != null && this.selectBeans.size() == 1) {
                this.mainLayout.removeAllViews();
                simpleShow();
                this.mainLayout.addView(this.simpleLayout);
                this.isScene = true;
            }
        } else if (v.getId() == R.id.cancel) {
            dismiss();
        } else if (v.getId() != R.id.enter || this.isScene) {
            if (v.getId() == R.id.enter && this.isScene) {
                singleBind();
            }
        } else if (this.scenenNum.getText().toString().length() == 0) {
            Toast.makeText(this.mContext, "\u573a\u666f\u53f7\u4e0d\u80fd\u4e3a\u7a7a", 1).show();
        } else {
            new WaitDialog(this.mContext, new OnWorkdListener() {
                public void doingWork() {
                    BindNotSaveDialog.this.sendMessageToHandler(14);
                    if (BindNotSaveDialog.this.sceneModel.equals("\u6309\u4e0b\u8c03\u7528")) {
                        BindNotSaveDialog.this.isBack = true;
                    } else if (BindNotSaveDialog.this.sceneModel.equals("\u7ed1\u5b9a\u6e05\u9664")) {
                        BindNotSaveDialog.this.sendMessageToHandler(13);
                        BindNotSaveDialog.this.isBack = false;
                        return;
                    }
                    ArrayList<SceneBean> list = MainApplication.sceneManager.findByIDandKey(BindNotSaveDialog.this.bindBean.bind_moduleID, BindNotSaveDialog.this.bindBean.keyValue);
                    if (list == null || list.size() <= 0) {
                        BindNotSaveDialog.this.sceneBean = null;
                    } else {
                        BindNotSaveDialog.this.sceneBean = (SceneBean) list.get(0);
                        BindNotSaveDialog.this.lastID = Integer.valueOf(BindNotSaveDialog.this.sceneBean.sceneBean_jsonName).intValue();
                    }
                    String scene = MainApplication.jsonManager.createSenceJson(BindNotSaveDialog.this.selectBeans, BindNotSaveDialog.this.mContext);
                    try {
                        SendDataSocket sendDataSocket = new SendDataSocket(MainApplication.userManager.currentHost(), 6001);
                        sendDataSocket.setOnCloseListener(new OnCloseListener() {
                            public void onClose() {
                                try {
                                    BindNotSaveDialog.this.activity.sendData(OrderManage.bingSenceBig9(BindNotSaveDialog.this.lastID, BindNotSaveDialog.this.bindBean.bind_moduleID, BindNotSaveDialog.this.bindBean.keyValue));
                                    BindNotSaveDialog.this.refushState();
                                    BindNotSaveDialog.this.createSceneBean(BindNotSaveDialog.this.bindBean);
                                    BindNotSaveDialog.this.sendMessageToHandler(13);
                                    EasyEventTypeHandler easyEventTypeHandler = EasyEventTypeHandler.getInstance();
                                    easyEventTypeHandler.setType(6);
                                    EventBus.getDefault().post(easyEventTypeHandler);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        sendDataSocket.sendData("down /json/s" + String.valueOf(BindNotSaveDialog.this.scenenNum.getText().toString()) + ".json$ " + scene);
                    } catch (Exception e) {
                        BindNotSaveDialog.this.sendMessageToHandler(13);
                        e.printStackTrace();
                    }
                }
            }, new OnPostExecuteListener() {
                public void after() {
                    if (BindNotSaveDialog.this.sceneModel.equals("\u7ed1\u5b9a\u6e05\u9664")) {
                        BindNotSaveDialog.this.dismiss();
                        SimpleDialog dialog = new SimpleDialog(BindNotSaveDialog.this.mContext);
                        dialog.setTitle("\u786e\u5b9a\u6267\u884c\u7ed1\u5b9a\u6e05\u9664");
                        dialog.setOnOptionListener(new OnOptionListener() {
                            public void onEnter() {
                                BindNotSaveDialog.this.hostSet();
                                BindNotSaveDialog.this.clear();
                                MainApplication.bindKeyGridAdapter.notifyDataSetChanged();
                            }
                        });
                        dialog.show();
                    }
                }
            }).execute(new Integer[0]);
            dismiss();
        }
    }

    private void isBackStart() {
        if (this.startType == 1 && this.isBack) {
            this.activity.clearStackByTag(BaseActivity.SETTING);
            this.activity.pushFragments(BaseActivity.SETTING, new BindFragment(), true, true, R.id.setting_center);
        }
    }

    private void clear() {
        this.bindBean.bind_name = "";
        this.bindBean.floor = "";
        this.bindBean.room = "";
        this.bindBean.deviceName = "";
        this.bindBean.bind_model = "";
        this.bindBean.bind_stat = 0;
        this.bindBean.type = Value.BindKeyGridAdapter_Type_Chang;
        MainApplication.bindManager.update(this.bindBean, 0);
    }

    private void hostSet() {
        this.activity.sendData(OrderManage.searchCorrespondIDAndKey(this.bindBean.bind_moduleID, this.bindBean.keyValue));
        this.activity.sendData(OrderManage.removeBind(this.bindBean.bind_moduleID, this.bindBean.keyValue));
        MainApplication.sceneManager.deleteByBindID(this.bindBean.bindID);
        Toast.makeText(this.activity, "\u6e05\u9664\u5b8c\u6bd5", 0).show();
    }

    private void createSceneBean(BindBean bindBean) {
        boolean isExist;
        if (this.sceneBean == null) {
            this.sceneBean = new SceneBean();
            isExist = false;
        } else {
            isExist = true;
        }
        this.sceneBean.sceneModel = this.sceneModel;
        this.sceneBean.sceneBean_type = 2;
        this.sceneBean.sceneBean_bindBeanID = bindBean.bindID;
        this.sceneBean.sceneBean_mian_keyValue = bindBean.keyValue;
        this.sceneBean.sceneBean_mian_mianID = bindBean.bind_moduleID;
        this.sceneBean.sceneBean_jsonName = String.valueOf(this.lastID);
        if (isExist) {
            MainApplication.sceneManager.update(this.sceneBean, this.sceneBean.sceneBean_id);
        } else {
            MainApplication.sceneManager.add(this.sceneBean);
        }
    }

    private void refushState() {
        new Thread(new Runnable() {
            public void run() {
                ArrayList<ModulePortBean> beans = MainApplication.modulePortAdapterSimple.getBeans();
                for (int i = 0; i < BindNotSaveDialog.this.selectBeans.size(); i++) {
                    ModulePortBean selectBean = (ModulePortBean) BindNotSaveDialog.this.selectBeans.get(i);
                    for (int j = 0; j < beans.size(); j++) {
                        ModulePortBean bean = (ModulePortBean) beans.get(j);
                        if (bean.id == selectBean.id) {
                            selectBean.isOpen = bean.isOpen;
                        }
                    }
                }
            }
        }).start();
    }

    private void singleBind() {
        if (this.modelString.equals("\u7ed1\u5b9a\u6e05\u9664")) {
            this.activity.sendData(OrderManage.removeBind(this.bindBean.bind_moduleID, this.bindBean.keyValue));
            this.isBack = false;
            dismiss();
            clear();
            Toast.makeText(this.activity, "\u6e05\u9664\u5b8c\u6bd5", 0).show();
            return;
        }
        this.isBack = true;
        ModulePortBean modulePortBean = (ModulePortBean) this.selectBeans.get(0);
        this.activity.sendData(OrderManage.removeBind(this.bindBean.bind_moduleID, this.bindBean.keyValue));
        this.activity.sendData(OrderManage.bindPhysics(modulePortBean, this.bindBean.bind_moduleID, this.bindBean.keyValue, this.modelString, this.checkStat.isChecked()));
        dismiss();
        isBackStart();
    }

    public void setBindBean(BindBean bindBean) {
        this.bindBean = bindBean;
        Log.i("data", "id:" + bindBean.bind_moduleID);
        Log.i("data", "key:" + bindBean.keyValue);
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    private void sendMessageToHandler(int messageType) {
        if (this.handler != null) {
            this.handler.sendEmptyMessage(messageType);
        }
    }
}
