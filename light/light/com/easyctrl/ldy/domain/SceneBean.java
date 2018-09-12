package com.easyctrl.ldy.domain;

public class SceneBean {
    public int sceneBean_bindBeanID;
    public int sceneBean_bindid;
    public int sceneBean_id;
    public String sceneBean_jsonName;
    public int sceneBean_mian_keyValue;
    public int sceneBean_mian_mianID;
    public int sceneBean_type;
    public String sceneModel;

    public static final class Table {
        public static final String Field_bindBeanID = "sceneBean_bindBeanID";
        public static final String Field_bindid = "sceneBean_bindid";
        public static final String Field_id = "sceneBean_id";
        public static final String Field_mian_keyValue = "sceneBean_mian_keyValue";
        public static final String Field_mian_mianID = "sceneBean_mian_mianID";
        public static final String Field_sceneBean_jsonName = "sceneBean_jsonName";
        public static final String Field_sceneModel = "sceneModel";
        public static final String Field_type = "sceneBean_type";
        public static final String T_NAME = "easy_scenebean";

        public static String generateTableSql() {
            StringBuilder builder = new StringBuilder();
            builder.append(" CREATE TABLE IF NOT EXISTS ").append(T_NAME).append("(sceneBean_id INTEGER PRIMARY KEY AUTOINCREMENT ,").append("sceneBean_type INTEGER NULL ,").append("sceneModel TEXT NULL ,").append("sceneBean_bindid INTEGER NULL ,").append("sceneBean_bindBeanID INTEGER NULL ,").append("sceneBean_mian_keyValue INTEGER NULL ,").append("sceneBean_jsonName TEXT NULL ,").append("sceneBean_mian_mianID INTEGER NULL )");
            return builder.toString();
        }
    }
}
