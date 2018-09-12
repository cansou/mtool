package com.easyctrl.ldy.util;

public class SaveSingleObject {
    private static SaveSingleObject instance;
    private Object object;
    private Object textObject;

    private SaveSingleObject() {
    }

    public static SaveSingleObject getInstance() {
        if (instance == null) {
            instance = new SaveSingleObject();
        }
        return instance;
    }

    public Object getObject() {
        return this.object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public Object getTextObject() {
        return this.textObject;
    }

    public void setTextObject(Object textObject) {
        this.textObject = textObject;
    }
}
