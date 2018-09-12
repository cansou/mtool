package com.easyctrl.ldy.domain;

public class DeviceBean {
    public static final int ORDER = 2;
    public static final int SYSTEM = 1;
    public int deviceID;
    public int device_isSystem;
    public String device_name;
    public String device_type;
    public int isUse;

    public static final class Table {
        public static String ID = "deviceID";
        public static String ISSYSTEM = "device_isSystem";
        public static String NAME = "device_name";
        public static String TYPE = "device_type";
        public static String T_NAME = "easy_device";

        public static String generateTableSql() {
            StringBuilder builder = new StringBuilder();
            builder.append("CREATE TABLE IF NOT EXISTS ").append(T_NAME).append("(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,").append(TYPE + " TEXT NULL ,").append(ISSYSTEM + " INTEGER NULL ,").append(NAME + " TEXT NULL )");
            return builder.toString();
        }
    }

    public boolean equals(Object o) {
        if (this.deviceID != ((DeviceBean) o).deviceID) {
            return false;
        }
        return true;
    }
}
