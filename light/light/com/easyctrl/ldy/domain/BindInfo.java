package com.easyctrl.ldy.domain;

public class BindInfo {
    public static final int BIND_MORE = 2;
    public static final int BIND_ONE = 1;
    public static final int BIND_TYPE_PHYSICS = 2;
    public static final int BIND_TYPE_PORT = 3;
    public static final int BIND_TYPE_VIRTUAL = 1;
    public int bindInfo_id;
    public int bindInfo_key;
    public int bindInfo_moduleID;
    public String bindInfo_name;
    public int bindInfo_type;
    public int bind_bindport;
    public int bind_id;
    public int bind_info_sceneID;
    public int bind_info_type;
    public int bind_module_J_id;
    public String device;
    public String floor;
    public String room;

    public static final class Table {
        public static String FIELD_BINDINFO_KEY = "bindInfo_key";
        public static String FIELD_BINDINFO_NAME = "bindInfo_name";
        public static String FIELD_BINDINFO_TYPE = "bindInfo_type";
        public static String FIELD_BINDPORT = "bind_bindport";
        public static String FIELD_BIND_ID = "bindInfo_id";
        public static String FIELD_BIND_INFO_SCENEID = "bind_info_sceneID";
        public static String FIELD_BIND_MODULE_J_ID = "bind_module_J_id";
        public static String FIELD_DEVICE = "device";
        public static String FIELD_FLOOR = com.easyctrl.ldy.domain.VirtualBean.Table.FIELD_FLOOR;
        public static String FIELD_INFO_BIND_ID = "bind_id";
        public static String FIELD_MODULEID = "bindInfo_moduleID";
        public static String FIELD_ROOM = com.easyctrl.ldy.domain.VirtualBean.Table.FIELD_ROOM;
        public static String FIELD_TYPE = "bind_info_type";
        public static String T_NAME = "easy_bindinfo";

        public static String generateTableSql() {
            StringBuilder builder = new StringBuilder();
            builder.append("CREATE TABLE IF NOT EXISTS ").append(T_NAME).append("(" + FIELD_BIND_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,").append(FIELD_INFO_BIND_ID + " INTEGER NULL ,").append(FIELD_BINDINFO_NAME + " TEXT NULL ,").append(FIELD_BINDINFO_TYPE + " INTEGER NULL ,").append(FIELD_BINDINFO_KEY + " INTEGER NULL ,").append(FIELD_FLOOR + " TEXT NULL ,").append(FIELD_ROOM + " TEXT NULL ,").append(FIELD_DEVICE + " TEXT NULL ,").append(FIELD_BINDPORT + " INTEGER NULL ,").append(FIELD_BIND_MODULE_J_ID + " INTEGER NULL ,").append(FIELD_TYPE + " INTEGER NULL , ").append(FIELD_BIND_INFO_SCENEID + " INTEGER NULL ,").append(FIELD_MODULEID + " INTEGER NULL)");
            return builder.toString();
        }
    }
}
