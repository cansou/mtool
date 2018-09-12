package com.easyctrl.ldy.domain;

public class VirtualBean {
    public static final int TYPE_NO = 0;
    public static final int TYPE_SCENE = 2;
    public static final int TYPE_SIMPLE = 1;
    public int arrayType;
    public int bindType;
    public String deviceName;
    public String floor;
    public int key;
    public String model;
    public int moduleID;
    public String name;
    public int port;
    public String room;
    public int state;
    public int v_scene_id;
    public int virtualID;

    public static final class Table {
        public static final String ARRAYTYPE = "arrayType";
        public static final String FIELD_DEVICENAME = "deviceName";
        public static final String FIELD_FLOOR = "floor";
        public static final String FIELD_ID = "virtualID";
        public static final String FIELD_KEY = "key";
        public static final String FIELD_MODEL = "model";
        public static final String FIELD_MODULEID = "moduleID";
        public static final String FIELD_NAME = "name";
        public static final String FIELD_PORT = "port";
        public static final String FIELD_ROOM = "room";
        public static final String FIELD_SCENE_ID = "v_scene_id";
        public static final String FIELD_STATE = "state";
        public static final String FIELD_TYPE = "bindType";
        public static final String T_NAME = "easy_virtual_key";

        public static String generateTableSql() {
            StringBuilder builder = new StringBuilder();
            builder.append(" CREATE TABLE IF NOT EXISTS easy_virtual_key").append("(virtualID INTEGER PRIMARY KEY AUTOINCREMENT, ").append("name TEXT NULL ,").append("key INTEGER NULL ,").append("arrayType INTEGER NULL ,").append("floor TEXT NULL ,").append("room TEXT NULL ,").append("deviceName TEXT NULL ,").append("moduleID INTEGER NULL ,").append("port INTEGER NULL ,").append("bindType INTEGER NULL ,").append("model TEXT NULL, ").append("v_scene_id INTEGER NULL,").append("state INTEGER NULL)");
            return builder.toString();
        }

        public static String getSaveSql() {
            StringBuilder builder = new StringBuilder();
            builder.append(" INSERT INTO easy_virtual_key").append(" (").append("name ,").append("key ,").append("state").append(" ) values (").append("?,?,?,?,?)");
            return builder.toString();
        }
    }
}
