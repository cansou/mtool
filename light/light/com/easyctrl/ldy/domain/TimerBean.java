package com.easyctrl.ldy.domain;

public class TimerBean {
    public static final int BIND_NO = 0;
    public static final int BIND_YES = 1;
    public static final int TIME_SCENE = 2;
    public static final int TIME_SINPLE = 1;
    public int bindModuleID;
    public int bindPort;
    public int bind_info_sceneID;
    public int bind_type;
    public int bindid;
    public String charArray;
    public int data;
    public int date;
    public int day;
    public String description;
    public String device;
    public String floor;
    public int hour;
    public int isBind;
    public int isOpen;
    public int key;
    public int min;
    public int moduleID;
    public int mon;
    public String obj;
    public String room;
    public int tID;
    public int timeID;
    public int time_type;
    public int type;
    public String weekString;
    public int year;

    public static final class Table {
        public static String FIELD_BINDID = "bindid";
        public static String FIELD_BIND_TYPE = "bind_type";
        public static String FIELD_CHAR_ARRAY = "charArray";
        public static String FIELD_DATA = "data";
        public static String FIELD_DATE = "date";
        public static String FIELD_DAY = "day";
        public static String FIELD_DESCRIPTION = "description";
        public static String FIELD_DEVICE = "device";
        public static String FIELD_FLOOR = com.easyctrl.ldy.domain.VirtualBean.Table.FIELD_FLOOR;
        public static String FIELD_HOUR = "hour";
        public static String FIELD_ISBIND = "isBind";
        public static String FIELD_ISOPEN = "isOpen";
        public static String FIELD_MIN = "min";
        public static String FIELD_MON = "mon";
        public static String FIELD_OBJECT = "obj";
        public static String FIELD_ROOM = com.easyctrl.ldy.domain.VirtualBean.Table.FIELD_ROOM;
        public static String FIELD_TID = "tID";
        public static String FIELD_TIME = "time";
        public static String FIELD_TIME_TYPE = "time_type";
        public static String FIELD_WEEKSTRING = "weekString";
        public static String FIELD_YEAR = "year";
        public static String ID = "timeID";
        public static String T_NAME = "easy_timer";

        public static String generateTableSql() {
            StringBuilder builder = new StringBuilder();
            builder.append("CREATE TABLE IF NOT EXISTS ").append(T_NAME).append("(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,").append(FIELD_TIME + " TEXT NULL ,").append(FIELD_DESCRIPTION + " TEXT NULL ,").append(FIELD_BIND_TYPE + " INTEGER NULL ,").append(FIELD_YEAR + " INTEGER NULL ,").append(FIELD_MON + " INTEGER NULL ,").append(FIELD_DATE + " INTEGER NULL ,").append(FIELD_DAY + " INTEGER NULL ,").append(FIELD_HOUR + " INTEGER NULL ,").append(FIELD_MIN + " INTEGER NULL ,").append(FIELD_ISBIND + " INTEGER NULL ,").append(FIELD_BINDID + " INTEGER NULL ,").append(FIELD_DATA + " TEXT NULL ,").append(FIELD_WEEKSTRING + " TEXT NULL ,").append(FIELD_OBJECT + " TEXT NULL ,").append(FIELD_CHAR_ARRAY + " TEXT NULL,").append(FIELD_ISOPEN + " INTEGER NULL,").append(FIELD_TIME_TYPE + " INTEGER NULL ,").append(FIELD_TID + " INTEGER NULL").append(")");
            return builder.toString();
        }
    }

    public boolean equals(Object o) {
        if (this.timeID == ((TimerBean) o).timeID) {
            return true;
        }
        return false;
    }
}
