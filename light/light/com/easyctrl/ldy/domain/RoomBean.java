package com.easyctrl.ldy.domain;

public class RoomBean {
    public int floorID;
    public int id;
    public int isUse;
    public String name;
    public int paixu;
    public String sname;

    public static final class Table {
        public static String FLOORID = com.easyctrl.ldy.domain.UserScene.Table.FIELD_FLOOR;
        public static String ID = com.easyctrl.ldy.domain.ImitateBean.Table.FIELD_ID;
        public static String NAME = "name";
        public static String PAIXU = "paixu";
        public static String SNAME = "sname";
        public static String T_NAME = "easy_room";

        public static String generateTableSql() {
            StringBuilder builder = new StringBuilder();
            builder.append("CREATE TABLE IF NOT EXISTS ").append(T_NAME).append("(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,").append(FLOORID + " INTEGER,").append(PAIXU + " INTEGER ,").append(SNAME + " TEXT NULL ,").append(NAME + " TEXT NULL )");
            return builder.toString();
        }
    }

    public boolean equals(Object o) {
        if (this.id != ((RoomBean) o).id) {
            return false;
        }
        return true;
    }
}
