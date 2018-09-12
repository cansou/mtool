package com.easyctrl.event;

public class EasyEventType {
    private int key;
    private int moduleID;
    private int timeID;
    private int type;

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getTimeID() {
        return this.timeID;
    }

    public void setTimeID(int timeID) {
        this.timeID = timeID;
    }

    public int getKey() {
        return this.key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public int getModuleID() {
        return this.moduleID;
    }

    public void setModuleID(int moduleID) {
        this.moduleID = moduleID;
    }
}
