package com.easyctrl.ldy.domain;

import java.io.Serializable;

public class UserScene implements Serializable {
    public static final int SCENE_BIND_TYPE_P = 1;
    public static final int SCENE_BIND_TYPE_V = 0;
    public int floorID;
    public int key;
    public String model;
    public String name;
    public int panelID;
    public int panelkey;
    public int pbindID;
    public int roomID;
    public int sbindType;
    public int userSceneID;
    public int vbindID;

    public static final class Table {
        public static final String FIELD_FLOOR = "floorID";
        public static final String FIELD_NAME = "name";
        public static final String FIELD_PBINDID = "pbindID";
        public static final String FIELD_ROOM = "roomID";
        public static final String FIELD_SBINDTYPE = "sbindType";
        public static final String FIELD_USERSCENEID = "userSceneID";
        public static final String FIELD_VBINDID = "vbindID";
        public static final String T_NAME = "easy_user_scene";

        public static String generateTableSql() {
            StringBuilder builder = new StringBuilder();
            builder.append("CREATE TABLE IF NOT EXISTS ").append(T_NAME).append("(userSceneID INTEGER PRIMARY KEY AUTOINCREMENT ,").append("name TEXT NULL ,").append("floorID INTEGER NULL ,").append("roomID INTEGER NULL ,").append("sbindType INTEGER NULL ,").append("pbindID INTEGER NULL,").append("vbindID TEXT NULL ").append(")");
            return builder.toString();
        }
    }
}
