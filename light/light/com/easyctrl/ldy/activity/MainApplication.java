package com.easyctrl.ldy.activity;

import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import com.easyctrl.adapter.BindKeyGridAdapter;
import com.easyctrl.adapter.BindMListAdapter;
import com.easyctrl.adapter.ModuleAdapter;
import com.easyctrl.adapter.ModulePortAdapter;
import com.easyctrl.adapter.ModulePortAdapterSimple;
import com.easyctrl.adapter.TimeUserAdapter;
import com.easyctrl.adapter.TimerAdapter;
import com.easyctrl.adapter.VirtualGridAdapter;
import com.easyctrl.broadcast.ScreenStatusReceiver;
import com.easyctrl.db.BindInfoManager;
import com.easyctrl.db.BindManager;
import com.easyctrl.db.DeviceManager;
import com.easyctrl.db.FloorManager;
import com.easyctrl.db.ModuleManager;
import com.easyctrl.db.ModulePortManager;
import com.easyctrl.db.RoomManager;
import com.easyctrl.db.SceneManager;
import com.easyctrl.db.TimerManager;
import com.easyctrl.db.UserSceneManager;
import com.easyctrl.db.VirtualManager;
import com.easyctrl.iface.OnBindListener;
import com.easyctrl.iface.OnLoginListener;
import com.easyctrl.iface.OnNioSocketListener;
import com.easyctrl.iface.OnScreenStatus;
import com.easyctrl.impl.ProcesBind;
import com.easyctrl.impl.ProcesInstructModulePort;
import com.easyctrl.ldy.net.EasySocket;
import com.easyctrl.ldy.net.IEasySocketResponse;
import com.easyctrl.ldy.util.ExitApp;
import com.easyctrl.ldy.util.FileUtil;
import com.easyctrl.ldy.util.StringUtil;
import com.easyctrl.manager.CrashHandler;
import com.easyctrl.manager.JsonManager;
import com.easyctrl.manager.NetPacketManager;
import com.easyctrl.manager.UserManager;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainApplication extends Application implements IEasySocketResponse, OnNioSocketListener, OnLoginListener, OnScreenStatus {
    public static BindInfoManager bindInfoManager;
    public static BindKeyGridAdapter bindKeyGridAdapter;
    public static BindMListAdapter bindMListAdapter;
    public static BindManager bindManager;
    public static CrashHandler crashHandler;
    public static DeviceManager deviceManager;
    public static EasySocket easySocket;
    public static ExitApp exitApp;
    public static FloorManager floorManager;
    public static JsonManager jsonManager;
    public static MainApplication mContext;
    public static LayoutInflater mInflater;
    public static ModuleAdapter moduleAdapter;
    public static ModuleManager moduleManager;
    public static ModulePortAdapter modulePortAdapter;
    public static ModulePortAdapterSimple modulePortAdapterSimple;
    public static ModulePortManager modulePortManager;
    public static NetPacketManager netPacketManager;
    public static OnBindListener onBindListener;
    public static ProcesInstructModulePort procesInstructModulePort;
    public static RoomManager roomManager;
    public static SceneManager sceneManager;
    public static ExecutorService threadPool = Executors.newFixedThreadPool(5);
    public static TimeUserAdapter timeUserAdapter;
    public static TimerAdapter timerAdapter;
    public static TimerManager timerManager;
    public static UserManager userManager;
    public static UserSceneManager userSceneManager;
    public static VirtualGridAdapter virtualGridAdapter;
    public static VirtualManager virtualManager;
    private String TAG = MainApplication.class.getSimpleName();
    private String ip;
    private boolean isAppLogin = false;

    public static boolean deleteDB() {
        boolean flag = FileUtil.deleteFileByPath("/data/data/com.easyctrl.ldy.activity/databases/ET_APP_NAME.DB");
        FileUtil.deleteFileByPath("/data/data/com.easyctrl.ldy.activity/databases/ET_APP_NAME.DB-journal");
        return flag;
    }

    public static boolean delete_Journal() {
        return FileUtil.deleteFileByPath("/data/data/com.easyctrl.ldy.activity/databases/ET_APP_NAME.DB-journal");
    }

    public static void deleteAllDB() {
        moduleManager.deleteAll();
        bindManager.deleteAll();
        timerManager.deleteAll();
        bindInfoManager.deleteAll();
        modulePortManager.deleteAll();
        virtualManager.deleteAll();
        deviceManager.deleteAll();
        userSceneManager.deleteAll();
        floorManager.deleteAll();
        roomManager.deleteAll();
        sceneManager.deleteAll();
    }

    public static void createDB() {
        moduleManager = ModuleManager.getInstance(mContext);
        bindManager = BindManager.getInstance(mContext);
        timerManager = TimerManager.getInstance(mContext);
        bindInfoManager = BindInfoManager.getInstance(mContext);
        modulePortManager = ModulePortManager.getInstance(mContext);
        virtualManager = VirtualManager.getInstance(mContext);
        deviceManager = DeviceManager.getInstance(mContext);
        userSceneManager = UserSceneManager.getInstance(mContext);
        floorManager = FloorManager.getInstance(mContext);
        roomManager = RoomManager.getInstance(mContext);
        sceneManager = SceneManager.getInstance(mContext);
    }

    public void onCreate() {
        super.onCreate();
        mContext = this;
        mInflater = LayoutInflater.from(mContext);
        userManager = UserManager.getInstance(mContext);
        jsonManager = JsonManager.getInstance(mContext);
        crashHandler = CrashHandler.getInstance();
        regist();
        timerAdapter = TimerAdapter.getAdapter(mContext);
        moduleAdapter = ModuleAdapter.getInstance(mContext, null);
        modulePortAdapter = ModulePortAdapter.getInstance(mContext, null);
        modulePortAdapterSimple = ModulePortAdapterSimple.getInstance(mContext, null);
        bindMListAdapter = BindMListAdapter.getAdapter(mContext, null);
        bindKeyGridAdapter = BindKeyGridAdapter.getAdapter(mContext, null);
        virtualGridAdapter = VirtualGridAdapter.getAdapter(mContext, null);
        timeUserAdapter = TimeUserAdapter.getInstance(mContext);
        exitApp = ExitApp.getInstance();
        UserActivity.setOnLoginListener(this);
        LoginActivity.setOnLoginListener(this);
        procesInstructModulePort = new ProcesInstructModulePort(mContext);
        netPacketManager = new NetPacketManager();
        netPacketManager.setInstructImpl(procesInstructModulePort);
        netPacketManager.setOnBindListener(new ProcesBind());
        easySocket = new EasySocket(mContext, this);
        easySocket.setOnNioSocketListener(this);
        if (userManager.isAutoLogin()) {
            this.ip = userManager.currentHost();
            connectNet();
        }
        userManager.setCurrentBindDevices(null);
        userManager.setCurrentBindFloor(null);
        userManager.setCurrentBindRoom(null);
    }

    public static Context getAppContext() {
        return mContext;
    }

    public static Resources getAppResources() {
        return getAppResources();
    }

    public void connectNet() {
        easySocket.open(this.ip, 5000);
        this.isAppLogin = true;
        Log.i("data", this.TAG + "\u7f51\u7edc\u6253\u5f00");
    }

    public void disconnect() {
        Log.i("DD", "disconnect");
    }

    public void connectError() {
        Log.i("DD", this.TAG + " connectError");
    }

    public void connectSuccess() {
        Log.i("DD", this.TAG + " connectSuccess");
    }

    public void onLogin(String ip, String pass) {
        this.ip = ip;
        connectNet();
    }

    public void onReconnect() {
        if (easySocket == null) {
            easySocket = new EasySocket(mContext, this);
            easySocket.setOnNioSocketListener(this);
            easySocket.open(userManager.currentHost(), 5000);
            this.isAppLogin = true;
            Log.i("data", this.TAG + " connect onReconnect");
        } else if (easySocket != null && easySocket.state != 8) {
            easySocket.reconn();
            Log.i("data", this.TAG + " AA connect onReconnect");
        }
    }

    private void regist() {
        ScreenStatusReceiver screenStatusReceiver = new ScreenStatusReceiver();
        screenStatusReceiver.setOnScreenStatus(this);
        IntentFilter screenStatusIF = new IntentFilter();
        screenStatusIF.addAction("android.intent.action.SCREEN_ON");
        screenStatusIF.addAction("android.intent.action.SCREEN_OFF");
        registerReceiver(screenStatusReceiver, screenStatusIF);
    }

    public void onScreneOpen() {
        Log.i("data", "\u5c4f\u5e55\u6253\u5f00");
        if (!this.isAppLogin) {
            return;
        }
        if (easySocket == null) {
            easySocket.reconn();
        } else if (easySocket.state != 8) {
            easySocket.reconn();
        }
    }

    public void onScreneClose() {
        Log.i("data", "\u5c4f\u5e55\u5173\u95ed");
        if (this.isAppLogin && easySocket != null) {
            easySocket.close();
        }
    }

    public void onSocketResponse(byte[] data, int size) {
        netPacketManager.setData(StringUtil.getByteArray(data, 0, size));
        netPacketManager.proceType();
    }
}
