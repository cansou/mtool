package com.easyctrl.ldy.domain;

import java.io.Serializable;

public class ModulePortBean implements Serializable, Cloneable {
    public static final int CLOSE = 2;
    public static final int OPEN = 1;
    public String deviveType;
    public String floor;
    public int id;
    public int isOpen;
    public int moduleID;
    public String name;
    public int paixu;
    public int port;
    public int progress;
    public String room;
    public int type;

    public static final class Table {
        public static String FIELD_DEVIVETYPE = "deviveType";
        public static String FIELD_FLOOR = com.easyctrl.ldy.domain.VirtualBean.Table.FIELD_FLOOR;
        public static String FIELD_ID = com.easyctrl.ldy.domain.ImitateBean.Table.FIELD_ID;
        public static String FIELD_ISOPEN = "isOpen";
        public static String FIELD_MODULEID = "moduleID";
        public static String FIELD_NANE = "name";
        public static String FIELD_PAIXU = "paixu";
        public static String FIELD_PORT = "port";
        public static String FIELD_PROGRESS = "progress";
        public static String FIELD_ROOM = com.easyctrl.ldy.domain.VirtualBean.Table.FIELD_ROOM;
        public static String FIELD_TYPE = "type";
        public static String T_NAME = "easy_moduleport";

        public static String generateTableSql() {
            StringBuilder builder = new StringBuilder();
            builder.append("CREATE TABLE IF NOT EXISTS ").append(T_NAME).append("(").append(FIELD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,").append(FIELD_MODULEID + " INTEGER NUll, ").append(FIELD_PORT + " INTEGER NULL, ").append(FIELD_PROGRESS + " INTEGER NULL, ").append(FIELD_ISOPEN + " INTEGER NULL, ").append(FIELD_FLOOR + " TEXT NULL, ").append(FIELD_ROOM + " TEXT NULL , ").append(FIELD_NANE + " TEXT NULL,").append(FIELD_DEVIVETYPE + " TEXT NULL,").append(FIELD_TYPE + " INTEGER NULL ,").append(FIELD_PAIXU + " INTEGER NULL ").append(")");
            return builder.toString();
        }

        public static String getSaveSql() {
            StringBuilder builder = new StringBuilder();
            builder.append("INSERT INTO " + T_NAME).append("(").append(FIELD_MODULEID + ",").append(FIELD_PORT + ",").append(FIELD_PROGRESS + " ,").append(FIELD_ISOPEN + " ,").append(FIELD_FLOOR + ",").append(FIELD_ROOM + ",").append(FIELD_NANE + " ,").append(FIELD_DEVIVETYPE + " ,").append(FIELD_TYPE + " ").append(") values (?,?,?,?,?,?,?,?,? )");
            return builder.toString();
        }
    }

    public boolean equals(Object o) {
        if (this.id == ((ModulePortBean) o).id) {
            return true;
        }
        return false;
    }

    public Object clone() throws CloneNotSupportedException {
        Object obj = null;
        try {
            return (ModulePortBean) super.clone();
        } catch (Exception e) {
            System.out.println(e.toString());
            return obj;
        }
    }
}
