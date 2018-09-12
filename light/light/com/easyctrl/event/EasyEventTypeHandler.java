package com.easyctrl.event;

import com.easyctrl.ldy.domain.HostTime;

public final class EasyEventTypeHandler {
    private static EasyEventTypeHandler instance;
    private int deviceRegisterOrRemove;
    private HostTime hostTime;
    private int key;
    private int moduleID;
    private int timeID;
    private int type;

    private EasyEventTypeHandler() {
    }

    public static EasyEventTypeHandler getInstance() {
        if (instance == null) {
            instance = new EasyEventTypeHandler();
        }
        return instance;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return this.type;
    }

    public void setHostTime(HostTime hostTime) {
        this.hostTime = hostTime;
    }

    public HostTime getHostTime() {
        return this.hostTime;
    }

    public void setDeviceRegisterOrRemove(int deviceRegisterOrRemove) {
        this.deviceRegisterOrRemove = deviceRegisterOrRemove;
    }

    public int getTimeID() {
        return this.timeID;
    }

    public void setTimeID(int timeID) {
        this.timeID = timeID;
    }

    public int getModuleID() {
        return this.moduleID;
    }

    public void setModuleID(int moduleID) {
        this.moduleID = moduleID;
    }

    public int getKey() {
        return this.key;
    }

    public void setKey(int key) {
        this.key = key;
    }
}
