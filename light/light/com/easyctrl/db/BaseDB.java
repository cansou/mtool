package com.easyctrl.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import com.easyctrl.ldy.domain.BindBean;
import com.easyctrl.ldy.domain.BindInfo;
import com.easyctrl.ldy.domain.DeviceBean;
import com.easyctrl.ldy.domain.FloorBean.Table;
import com.easyctrl.ldy.domain.ModuleBean;
import com.easyctrl.ldy.domain.ModulePortBean;
import com.easyctrl.ldy.domain.RoomBean;
import com.easyctrl.ldy.domain.SceneBean;
import com.easyctrl.ldy.domain.TimerBean;
import com.easyctrl.ldy.domain.UserScene;
import com.easyctrl.ldy.domain.VirtualBean;
import com.easyctrl.ldy.util.Value;

public class BaseDB extends SQLiteOpenHelper {
    private static final String dbName = "ET_APP_NAME.DB";
    private static final int dbVersion = Value.dbVersion;
    private static BaseDB instance;

    private BaseDB(Context context) {
        super(context, dbName, null, dbVersion);
    }

    public BaseDB(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Table.generateTableSql());
        db.execSQL(RoomBean.Table.generateTableSql());
        db.execSQL(DeviceBean.Table.generateTableSql());
        db.execSQL(ModuleBean.Table.generateTableSql());
        db.execSQL(ModulePortBean.Table.generateTableSql());
        db.execSQL(BindBean.Table.generateTableSql());
        db.execSQL(VirtualBean.Table.generateTableSql());
        db.execSQL(TimerBean.Table.generateTableSql());
        db.execSQL(BindInfo.Table.generateTableSql());
        db.execSQL(UserScene.Table.generateTableSql());
        db.execSQL(SceneBean.Table.generateTableSql());
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public static synchronized BaseDB getInstance(Context context) {
        BaseDB baseDB;
        synchronized (BaseDB.class) {
            if (instance == null) {
                instance = new BaseDB(context);
            }
            baseDB = instance;
        }
        return baseDB;
    }
}
