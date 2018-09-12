package com.easyctrl.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import com.easyctrl.iface.BaseDBImpl;
import com.easyctrl.ldy.domain.BindInfo;
import com.easyctrl.ldy.domain.TimerBean;
import com.easyctrl.ldy.domain.TimerBean.Table;
import java.util.ArrayList;

public class TimerManager extends DataBaseManage implements BaseDBImpl<TimerBean> {
    private static TimerManager instance;

    private TimerManager(Context context) {
        super(context);
    }

    public static TimerManager getInstance(Context context) {
        if (instance == null) {
            instance = new TimerManager(context);
        }
        return instance;
    }

    public void deleteAll() {
        synchronized (LOCK) {
            openWriteDB();
            this.database.delete(Table.T_NAME, null, null);
            this.database.execSQL(" DELETE FROM sqlite_sequence ");
        }
    }

    public void deleteTable() {
        synchronized (LOCK) {
            this.database.execSQL("truncate table easy_timer");
        }
    }

    public void add(TimerBean t) {
    }

    public void batchSave() {
        synchronized (LOCK) {
            openWriteDB();
            Cursor cursor = null;
            try {
                cursor = this.database.query(Table.T_NAME, null, null, null, null, null, null);
                if (cursor != null && cursor.getCount() < 50) {
                    this.database.beginTransaction();
                    for (int i = 1; i <= 50; i++) {
                        String sql = " insert into " + Table.T_NAME + "(tID) values(?) ";
                        this.database.execSQL(sql, new String[]{String.valueOf(i)});
                    }
                    this.database.setTransactionSuccessful();
                    this.database.endTransaction();
                }
                if (cursor != null) {
                    cursor.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
                if (cursor != null) {
                    cursor.close();
                }
            } catch (Throwable th) {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
    }

    public int delete(int id) {
        return 0;
    }

    public int update(TimerBean t, int id) {
        int col;
        synchronized (LOCK) {
            col = 0;
            try {
                openWriteDB();
                col = this.database.update(Table.T_NAME, beanToValues(t), " timeID = ? ", new String[]{String.valueOf(id)});
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return col;
    }

    public void updateOpenType(int timeID, int isOpen) {
        synchronized (LOCK) {
            try {
                openWriteDB();
                this.database.execSQL(" update " + Table.T_NAME + " set isOpen=? where timeID = ?", new Object[]{String.valueOf(isOpen), String.valueOf(timeID)});
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public ArrayList<TimerBean> findAll() {
        ArrayList<TimerBean> arrayList;
        Exception e;
        Throwable th;
        synchronized (LOCK) {
            arrayList = null;
            openReadDB();
            Cursor cursor = this.database.rawQuery(" select * from  easy_timer a left join easy_bindinfo b on a.timeID = b.bind_id ", null);
            if (cursor != null) {
                try {
                    if (cursor.getCount() > 0) {
                        ArrayList<TimerBean> arrayList2 = new ArrayList();
                        while (cursor.moveToNext()) {
                            try {
                                arrayList2.add(cursorToBean(cursor));
                            } catch (Exception e2) {
                                e = e2;
                                arrayList = arrayList2;
                            } catch (Throwable th2) {
                                th = th2;
                                arrayList = arrayList2;
                            }
                        }
                        arrayList = arrayList2;
                    }
                } catch (Exception e3) {
                    e = e3;
                    try {
                        e.printStackTrace();
                        if (cursor != null) {
                            cursor.close();
                        }
                        return arrayList;
                    } catch (Throwable th3) {
                        th = th3;
                        if (cursor != null) {
                            cursor.close();
                        }
                        throw th;
                    }
                }
            }
            if (cursor != null) {
                cursor.close();
            }
        }
        return arrayList;
    }

    public ContentValues beanToValues(TimerBean bean) {
        ContentValues values = new ContentValues();
        values.put(Table.FIELD_YEAR, Integer.valueOf(bean.year));
        values.put(Table.FIELD_MON, Integer.valueOf(bean.mon));
        values.put(Table.FIELD_DATE, Integer.valueOf(bean.date));
        values.put(Table.FIELD_DAY, Integer.valueOf(bean.day));
        values.put(Table.FIELD_HOUR, Integer.valueOf(bean.hour));
        values.put(Table.FIELD_MIN, Integer.valueOf(bean.min));
        values.put(Table.FIELD_BINDID, Integer.valueOf(bean.bindid));
        values.put(Table.FIELD_ISBIND, Integer.valueOf(bean.isBind));
        values.put(Table.FIELD_WEEKSTRING, bean.weekString);
        values.put(Table.FIELD_DESCRIPTION, bean.description);
        values.put(Table.FIELD_TIME_TYPE, Integer.valueOf(bean.time_type));
        values.put(Table.FIELD_CHAR_ARRAY, bean.charArray);
        values.put(Table.FIELD_BIND_TYPE, Integer.valueOf(bean.bind_type));
        values.put(Table.FIELD_ISOPEN, Integer.valueOf(bean.isOpen));
        return values;
    }

    public TimerBean cursorToBean(Cursor cursor) {
        TimerBean bean = new TimerBean();
        bean.bind_type = cursor.getInt(cursor.getColumnIndexOrThrow(BindInfo.Table.FIELD_BINDINFO_TYPE));
        bean.bindid = cursor.getInt(cursor.getColumnIndexOrThrow(BindInfo.Table.FIELD_BIND_INFO_SCENEID));
        bean.bindPort = cursor.getInt(cursor.getColumnIndexOrThrow(BindInfo.Table.FIELD_BINDPORT));
        bean.charArray = cursor.getString(cursor.getColumnIndexOrThrow(Table.FIELD_CHAR_ARRAY));
        bean.data = cursor.getInt(cursor.getColumnIndexOrThrow(Table.FIELD_DATA));
        bean.date = cursor.getInt(cursor.getColumnIndexOrThrow(Table.FIELD_DATE));
        bean.description = cursor.getString(cursor.getColumnIndexOrThrow(Table.FIELD_DESCRIPTION));
        bean.device = cursor.getString(cursor.getColumnIndexOrThrow(Table.FIELD_DEVICE));
        bean.floor = cursor.getString(cursor.getColumnIndexOrThrow(Table.FIELD_FLOOR));
        bean.hour = cursor.getInt(cursor.getColumnIndexOrThrow(Table.FIELD_HOUR));
        bean.isBind = cursor.getInt(cursor.getColumnIndexOrThrow(Table.FIELD_ISBIND));
        bean.key = cursor.getInt(cursor.getColumnIndexOrThrow(BindInfo.Table.FIELD_BINDINFO_KEY));
        bean.min = cursor.getInt(cursor.getColumnIndexOrThrow(Table.FIELD_MIN));
        bean.moduleID = cursor.getInt(cursor.getColumnIndexOrThrow(BindInfo.Table.FIELD_MODULEID));
        bean.mon = cursor.getInt(cursor.getColumnIndexOrThrow(Table.FIELD_MON));
        bean.obj = cursor.getString(cursor.getColumnIndexOrThrow(Table.FIELD_OBJECT));
        bean.room = cursor.getString(cursor.getColumnIndexOrThrow(BindInfo.Table.FIELD_ROOM));
        bean.time_type = cursor.getInt(cursor.getColumnIndexOrThrow(Table.FIELD_TIME_TYPE));
        bean.timeID = cursor.getInt(cursor.getColumnIndexOrThrow(Table.ID));
        bean.type = cursor.getInt(cursor.getColumnIndexOrThrow(BindInfo.Table.FIELD_TYPE));
        bean.weekString = cursor.getString(cursor.getColumnIndexOrThrow(Table.FIELD_WEEKSTRING));
        bean.year = cursor.getInt(cursor.getColumnIndexOrThrow(Table.FIELD_YEAR));
        bean.bindModuleID = cursor.getInt(cursor.getColumnIndexOrThrow(BindInfo.Table.FIELD_BIND_MODULE_J_ID));
        bean.day = cursor.getInt(cursor.getColumnIndexOrThrow(Table.FIELD_DAY));
        bean.isOpen = cursor.getInt(cursor.getColumnIndexOrThrow(Table.FIELD_ISOPEN));
        bean.bind_info_sceneID = cursor.getInt(cursor.getColumnIndexOrThrow(BindInfo.Table.FIELD_BIND_INFO_SCENEID));
        bean.tID = cursor.getInt(cursor.getColumnIndexOrThrow(Table.FIELD_TID));
        return bean;
    }

    public Cursor getCursor(String where, String[] args) {
        return null;
    }

    public ArrayList<TimerBean> findBinded() {
        ArrayList<TimerBean> arrayList;
        Exception e;
        Throwable th;
        synchronized (LOCK) {
            String sql = " select * from easy_bindinfo a,easy_timer b where b.timeID = a.bind_id order by b.tID asc";
            arrayList = null;
            openReadDB();
            Cursor cursor = null;
            try {
                cursor = this.database.rawQuery(sql, null);
                if (cursor != null && cursor.getCount() > 0) {
                    ArrayList<TimerBean> beans = new ArrayList();
                    while (cursor.moveToNext()) {
                        try {
                            beans.add(cursorToBean(cursor));
                        } catch (Exception e2) {
                            e = e2;
                            arrayList = beans;
                        } catch (Throwable th2) {
                            th = th2;
                            arrayList = beans;
                        }
                    }
                    arrayList = beans;
                }
                if (cursor != null) {
                    cursor.close();
                }
            } catch (Exception e3) {
                e = e3;
                try {
                    e.printStackTrace();
                    if (cursor != null) {
                        cursor.close();
                    }
                    return arrayList;
                } catch (Throwable th3) {
                    th = th3;
                    if (cursor != null) {
                        cursor.close();
                    }
                    throw th;
                }
            }
        }
        return arrayList;
    }

    public void updateOpen(TimerBean timerBean) {
        synchronized (LOCK) {
            try {
                openWriteDB();
                ContentValues values = new ContentValues();
                values.put(Table.FIELD_ISOPEN, Integer.valueOf(timerBean.isOpen));
                this.database.update(Table.T_NAME, values, " timeID = ? ", new String[]{String.valueOf(timerBean.timeID)});
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void initData(TimerBean timerBean) {
        synchronized (LOCK) {
            try {
                openWriteDB();
                ContentValues values = new ContentValues();
                values.put(Table.FIELD_ISOPEN, "");
                values.put(Table.FIELD_TIME, "");
                values.put(Table.FIELD_DESCRIPTION, "");
                values.put(Table.FIELD_BIND_TYPE, "");
                values.put(Table.FIELD_YEAR, "");
                values.put(Table.FIELD_MON, "");
                values.put(Table.FIELD_DATE, "");
                values.put(Table.FIELD_DAY, "");
                values.put(Table.FIELD_HOUR, "");
                values.put(Table.FIELD_MIN, "");
                values.put(Table.FIELD_ISBIND, "");
                values.put(Table.FIELD_BINDID, "");
                values.put(Table.FIELD_DATA, "");
                values.put(Table.FIELD_WEEKSTRING, "");
                values.put(Table.FIELD_OBJECT, "");
                values.put(Table.FIELD_CHAR_ARRAY, "");
                values.put(Table.FIELD_TIME_TYPE, "");
                this.database.update(Table.T_NAME, values, " timeID = ? ", new String[]{String.valueOf(timerBean.timeID)});
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
