package com.easyctrl.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import com.easyctrl.db.FloorManager;
import com.easyctrl.db.RoomManager;
import com.easyctrl.ldy.domain.FloorBean;
import com.easyctrl.ldy.domain.RoomBean;
import com.easyctrl.ui.base.BaseActivity;

public class UserManager {
    private static UserManager instance;
    private Editor editor = this.preferences.edit();
    private FloorManager floorManager;
    private SharedPreferences preferences;
    private RoomManager roomManager;

    private UserManager(Context context) {
        this.preferences = context.getSharedPreferences(BaseActivity.USRT, 0);
        this.floorManager = FloorManager.getInstance(context);
        this.roomManager = RoomManager.getInstance(context);
    }

    public static synchronized UserManager getInstance(Context context) {
        UserManager userManager;
        synchronized (UserManager.class) {
            if (instance == null) {
                instance = new UserManager(context);
            }
            userManager = instance;
        }
        return userManager;
    }

    public void setDownload(boolean flag) {
        this.editor.putBoolean("isDown", flag);
        this.editor.commit();
    }

    public boolean getDownloag() {
        return this.preferences.getBoolean("isDown", false);
    }

    public void setHost(String host) {
        this.editor.putString("host", host);
        this.editor.commit();
    }

    public void setPassword(String password) {
        this.editor.putString("password", password);
        this.editor.commit();
    }

    public void setRemember(boolean isremember) {
        this.editor.putBoolean("isremember", isremember);
        this.editor.commit();
    }

    public void setAutoLogin(boolean auto) {
        this.editor.putBoolean("auto", auto);
        this.editor.commit();
    }

    public String getHost() {
        return this.preferences.getString("host", null);
    }

    public String getPassword() {
        return this.preferences.getString("password", null);
    }

    public boolean isRemember() {
        return this.preferences.getBoolean("isremember", false);
    }

    public boolean isAutoLogin() {
        return this.preferences.getBoolean("auto", false);
    }

    public void setEnterSettingPassword(String pass) {
        this.editor.putString(BaseActivity.SETTING, pass);
        this.editor.commit();
    }

    public String getEnterSettingPassWord() {
        return this.preferences.getString(BaseActivity.SETTING, null);
    }

    public void setCurrentFloor(int currentFloorId) {
        this.editor.putInt("currentFloorId", currentFloorId);
        this.editor.commit();
    }

    public FloorBean getCurrentFloorBean() {
        int currentFloorID = this.preferences.getInt("currentFloorId", -1);
        if (currentFloorID == -1) {
            return null;
        }
        FloorBean currentFloor = this.floorManager.findByID(currentFloorID);
        if (currentFloor == null) {
            return null;
        }
        return currentFloor;
    }

    public String getCurrentFloor() {
        FloorBean bean = getCurrentFloorBean();
        if (bean == null) {
            return null;
        }
        return bean.name;
    }

    public void setCurrentRoom(int currentRoomID) {
        this.editor.putInt("currentRoomID", currentRoomID);
        this.editor.commit();
    }

    public String getCurrentRoom() {
        int currentRoomid = this.preferences.getInt("currentRoomID", -1);
        if (currentRoomid == -1) {
            return null;
        }
        RoomBean bean = this.roomManager.findById(currentRoomid, currentHost());
        FloorBean fBean = getCurrentFloorBean();
        if (fBean == null || bean == null) {
            return null;
        }
        RoomBean currentRoom = this.roomManager.findByName(bean.name, fBean.id);
        if (currentRoom != null) {
            return currentRoom.name;
        }
        return null;
    }

    public void setLogin(boolean islogin) {
        this.editor.putBoolean("isLogin", islogin);
        this.editor.commit();
    }

    public boolean isLogin() {
        return this.preferences.getBoolean("isLogin", false);
    }

    public void setCurrentHost(String currentHost) {
        this.editor.putString("currentHost", currentHost);
        this.editor.commit();
    }

    public String currentHost() {
        return this.preferences.getString("currentHost", null);
    }

    public void setCurrentMask(String dns) {
        this.editor.putString("mask", dns);
        this.editor.commit();
    }

    public String currentMask() {
        return this.preferences.getString("mask", null);
    }

    public void setCurrentGateway(String gateway) {
        this.editor.putString("gateway", gateway);
        this.editor.commit();
    }

    public String currentGateway() {
        return this.preferences.getString("gateway", null);
    }

    public void setCurrentFragment(String fragmentName) {
        this.editor.putString("fragmentName", fragmentName);
        this.editor.commit();
    }

    public String getCurrentFragmentName() {
        return this.preferences.getString("fragmentName", null);
    }

    public void setLastSelectID(String roomString, String lastIP) {
        this.editor.putString("lastIP", lastIP);
        this.editor.putString("roomString", roomString);
        this.editor.commit();
    }

    public String getLastSelectID() {
        String lastIP = this.preferences.getString("lastIP", null);
        if (lastIP != null && lastIP.equals(currentHost())) {
            return this.preferences.getString("roomString", null);
        }
        return null;
    }

    public void setTempLoginPws(String tempPass) {
        this.editor.putString("tempPass", tempPass);
        this.editor.commit();
    }

    public String getTempPass() {
        return this.preferences.getString("tempPass", "");
    }

    public void setTempSetPws(String tempPass) {
        this.editor.putString("tempSetPass", tempPass);
        this.editor.commit();
    }

    public String getTempSetPass() {
        return this.preferences.getString("tempSetPass", "");
    }

    public void setUserFloor(String userFloor) {
        this.editor.putString("userFloor", userFloor);
        this.editor.commit();
    }

    public String getUserFloor() {
        return this.preferences.getString("userFloor", null);
    }

    public void setUserRoom(String userRoor) {
        this.editor.putString("userRoor", userRoor);
        this.editor.commit();
    }

    public String getUserRoor() {
        return this.preferences.getString("userRoor", null);
    }

    public void setUserDevice(String userDevice) {
        this.editor.putString("userDevice", userDevice);
        this.editor.commit();
    }

    public String getUserDevice() {
        return this.preferences.getString("userDevice", null);
    }

    public void setCurrentBindFloor(String floor) {
        this.editor.putString("CurrentBindFloor", floor);
        this.editor.commit();
    }

    public String getCurrentBindFloor() {
        return this.preferences.getString("CurrentBindFloor", null);
    }

    public void setCurrentBindRoom(String room) {
        this.editor.putString("CurrentBindRoom", room);
        this.editor.commit();
    }

    public String getCurrentBindRoom() {
        return this.preferences.getString("CurrentBindRoom", null);
    }

    public void setCurrentBindDevices(String device) {
        this.editor.putString("CurrentBindDevices", device);
        this.editor.commit();
    }

    public String getCurrentBindDevices() {
        return this.preferences.getString("CurrentBindDevices", null);
    }

    public void setPath(String fileName) {
        this.editor.putString("fileName", fileName);
        this.editor.commit();
    }

    public String getPath() {
        return this.preferences.getString("fileName", null);
    }

    public void setCurrentBackStata(boolean b) {
        this.editor.putBoolean("CurrentBackStata", b);
        this.editor.commit();
    }

    public boolean getCurrentBackStata() {
        return this.preferences.getBoolean("CurrentBackStata", false);
    }

    public void setCurrentSetRoom(int room) {
        this.editor.putInt("CurrentSetRoom", room);
        this.editor.commit();
    }

    public int getCurrentSetRoom() {
        return this.preferences.getInt("CurrentSetRoom", 0);
    }

    public void setCurrentSetDevice(String device) {
        this.editor.putString("CurrentSetDevice", device);
        this.editor.commit();
    }

    public String getCurrentSetDevice() {
        return this.preferences.getString("CurrentSetDevice", null);
    }

    public void setCurrentSetFloor(int floor) {
        this.editor.putInt("CurrentSetFloor", floor);
        this.editor.commit();
    }

    public int getCurrentSetFloor() {
        return this.preferences.getInt("CurrentSetFloor", 0);
    }
}
