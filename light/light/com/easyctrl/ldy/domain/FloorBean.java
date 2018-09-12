package com.easyctrl.ldy.domain;

public class FloorBean {
    public int id;
    public int isUse;
    public String name;
    public int paixu;
    public String sname;

    public static final class Table {
        public static String ID = com.easyctrl.ldy.domain.ImitateBean.Table.FIELD_ID;
        public static String NAME = "name";
        public static String PAIXU = "paixu";
        public static String SNAME = "sname";
        public static String T_NAME = "easy_floor";

        public static String generateTableSql() {
            StringBuilder builder = new StringBuilder();
            builder.append("CREATE TABLE IF NOT EXISTS ").append(T_NAME).append("(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,").append(SNAME + " TEXT NULL ,").append(PAIXU + " INTEGER NULL ,").append(NAME + " TEXT NULL)");
            return builder.toString();
        }
    }

    public boolean equals(Object o) {
        if (this.id != ((FloorBean) o).id) {
            return false;
        }
        return true;
    }
}
