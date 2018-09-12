package com.easyctrl.ldy.domain;

public class ImitateBean {
    public int id;
    public int key;
    public int moduleID;
    public String name;
    public int port;
    public int state;

    public static final class Table {
        public static final String FIELD_ID = "id";
        public static final String FIELD_KEY = "key";
        public static final String FIELD_MODULEID = "moduleID";
        public static final String FIELD_NAME = "name";
        public static final String FIELD_PORT = "port";
        public static final String FIELD_STATE = "state";
        public static final String T_NAME = "easy_imitate_key";

        public static String generateTableSql() {
            StringBuilder builder = new StringBuilder();
            builder.append(" CREATE TABLE IF NOT EXISTS easy_imitate_key").append("(id INTEGER PRIMARY KEY AUTOINCREMENT, ").append("name TEXT NULL, ").append("moduleID INTEGER NULL,").append("port INTEGER NULL,").append("key INTEGER NULL,").append("state INTEGER NULL )");
            return builder.toString();
        }

        public static String getSaveSql() {
            StringBuilder builder = new StringBuilder();
            builder.append(" INSERT INTO easy_imitate_key").append(" (").append("name ,").append("moduleID ,").append("port ,").append("key ,").append("state").append(" ) values (").append("?,?,?,?,?)");
            return builder.toString();
        }
    }
}
