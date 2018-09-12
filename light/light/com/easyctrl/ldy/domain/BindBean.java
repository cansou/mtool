package com.easyctrl.ldy.domain;

import java.io.Serializable;

public class BindBean implements Serializable {
    public static final int BIND_TYPE_BIND_NO = 0;
    public static final int BIND_TYPE_BIND_YES = 1;
    public static final int TYPE_SCENE = 2;
    public static final int TYPE_SIMPLE = 1;
    private static final long serialVersionUID = -1890059283377256599L;
    public int bindID;
    public int bindModuleID;
    public int bindPort;
    public String bind_model;
    public int bind_moduleID;
    public String bind_name;
    public int bind_stat;
    public String deviceName;
    public String floor;
    public int keyValue;
    public String room;
    public int type;

    public static final class Table {
        public static String DEVICENAME = com.easyctrl.ldy.domain.VirtualBean.Table.FIELD_DEVICENAME;
        public static String FIELD_BINDMODULEID = "bindModuleID";
        public static String FIELD_BINDPORT = "bindPort";
        public static String FIELD_FLOOR = com.easyctrl.ldy.domain.VirtualBean.Table.FIELD_FLOOR;
        public static String FIELD_MODEL = "bind_model";
        public static String FIELD_ROOM = com.easyctrl.ldy.domain.VirtualBean.Table.FIELD_ROOM;
        public static String FIELD_TYPE = "type";
        public static String ID = "bindID";
        public static String KEYVALUE = "keyValue";
        public static String MOUDLEID = "bind_moduleID";
        public static String NAME = "bind_name";
        public static String STAT = "bind_stat";
        public static String T_NAME = "easy_bind";

        public static String generateTableSql() {
            StringBuilder builder = new StringBuilder();
            builder.append("CREATE TABLE IF NOT EXISTS ").append(T_NAME).append("(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,").append(NAME + " TEXT NULL ,").append(KEYVALUE + " TEXT NULL ,").append(STAT + " INTEGER NULL ,").append(FIELD_BINDMODULEID + " INTEGER NULL ,").append(FIELD_BINDPORT + " INTEGER NULL,").append(FIELD_MODEL + " TEXT NULL ,").append(FIELD_TYPE + " INTEGER NULL ,").append(FIELD_FLOOR + " TEXT NULL ,").append(FIELD_ROOM + " TEXT NULL ,").append(DEVICENAME + " TEXT NULL ,").append(MOUDLEID + " INTEGER NULL ").append(")");
            return builder.toString();
        }
    }

    public boolean equals(Object o) {
        if (this.bindID != ((BindBean) o).bindID) {
            return false;
        }
        return true;
    }

    public Object clone() throws CloneNotSupportedException {
        Object obj = null;
        try {
            return (BindBean) super.clone();
        } catch (Exception e) {
            e.printStackTrace();
            return obj;
        }
    }
}
