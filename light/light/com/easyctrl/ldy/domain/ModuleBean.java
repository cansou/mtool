package com.easyctrl.ldy.domain;

import java.io.Serializable;

public class ModuleBean implements Serializable {
    public static final int KEY = 2;
    public static final int ONMING = 1;
    public static final int OPEN = 0;
    private static final long serialVersionUID = 1;
    public String ext1;
    public String ext2;
    public String ext3;
    public String ext4;
    public String ext5;
    public int id;
    public String moduleDescribe;
    public String moduleFloor;
    public int moduleGatewayID;
    public int moduleId;
    public int moduleLinknum;
    public String moduleMac;
    public String moduleModel;
    public String moduleName;
    public String moduleNameExt;
    public int modulePortNum;
    public String moduleRoom;
    public String moduleVersion;
    public int type = -1;

    public static final class Table {
        public static String FIELD_DESCRIBE = "moduleDescribe";
        public static String FIELD_FLOOR = "moduleFloor";
        public static String FIELD_GATEWAYID = "moduleGatewayID";
        public static String FIELD_ID = com.easyctrl.ldy.domain.ImitateBean.Table.FIELD_ID;
        public static String FIELD_MODEL = "moduleModel";
        public static String FIELD_MODULEID = "moduleId";
        public static String FIELD_MODULELINKNUM = "moduleLinknum";
        public static String FIELD_MODULEMAC = "moduleMac";
        public static String FIELD_MODULEPORTNUM = "modulePortNum";
        public static String FIELD_NAME = "moduleName";
        public static String FIELD_NAMEExt = "moduleNameExt";
        public static String FIELD_ROOM = "moduleRoom";
        public static String FIELD_TYPE = "type";
        public static String FIELD_VERSUIB = "moduleVersuib";
        public static String T_NAME = "easy_module";

        public static String generateTableSql() {
            StringBuilder builder = new StringBuilder();
            builder.append("CREATE TABLE IF NOT EXISTS ").append(T_NAME).append("(" + FIELD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,").append(FIELD_NAME + " TEXT NULL,").append(FIELD_NAMEExt + " TEXT NULL,").append(FIELD_MODEL + " TEXT NULL,").append(FIELD_DESCRIBE + " TEXT NULL,").append(FIELD_VERSUIB + " TEXT NULL,").append(FIELD_FLOOR + " TEXT NULL,").append(FIELD_ROOM + " TEXT NULL,").append(FIELD_MODULEID + " TEXT NULL ,").append(FIELD_GATEWAYID + " TEXT NULL ,").append(FIELD_MODULEPORTNUM + " TEXT NULL,").append(FIELD_MODULELINKNUM + " TEXT NULL,").append(FIELD_MODULEMAC + " TEXT NULL, ").append(FIELD_TYPE + "  INTEGER  )");
            return builder.toString();
        }

        public static String getSaveSql() {
            StringBuilder builder = new StringBuilder();
            builder.append("INSERT INTO " + T_NAME).append(" (").append(FIELD_NAME + " ,").append(FIELD_MODEL + " ,").append(FIELD_NAMEExt + ",").append(FIELD_DESCRIBE + " ,").append(FIELD_VERSUIB + " ,").append(FIELD_ROOM + " ,").append(FIELD_MODULEID + " ,").append(FIELD_GATEWAYID + " ,").append(FIELD_MODULEPORTNUM + " ,").append(FIELD_MODULELINKNUM + " ,").append(FIELD_MODULEMAC + " ,").append(FIELD_TYPE + " ").append(" ) values (").append("?,?,?,?,?,?,?,?,?,?,?,? )");
            return builder.toString();
        }
    }

    public boolean equals(Object obj) {
        if (this.moduleId == ((ModuleBean) obj).moduleId) {
            return true;
        }
        return false;
    }
}
