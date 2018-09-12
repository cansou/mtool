package com.easyctrl.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class DataBaseManage {
    protected static byte[] LOCK = new byte[1];
    protected SQLiteDatabase database;
    protected BaseDB dbHelper;

    public DataBaseManage(Context context) {
        this.dbHelper = BaseDB.getInstance(context);
    }

    protected void openWriteDB() {
        if (this.dbHelper != null) {
            this.database = this.dbHelper.getWritableDatabase();
        }
    }

    protected void openReadDB() {
        if (this.dbHelper != null) {
            this.database = this.dbHelper.getReadableDatabase();
        }
    }

    protected void closeDB() {
        if (this.database != null && this.database.isOpen()) {
            this.database.close();
            this.database = null;
        }
    }
}
