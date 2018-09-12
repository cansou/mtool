package com.easyctrl.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import com.easyctrl.iface.BaseDBImpl;
import com.easyctrl.ldy.activity.MainApplication;
import com.easyctrl.ldy.domain.BindBean;
import com.easyctrl.ldy.domain.BindBean.Table;
import java.util.ArrayList;

public class BindManager extends DataBaseManage implements BaseDBImpl<BindBean> {
    private static BindManager instance;

    private BindManager(Context context) {
        super(context);
    }

    public int getVersion() {
        openWriteDB();
        return this.database.getVersion();
    }

    public static BindManager getInstance(Context context) {
        if (instance == null) {
            instance = new BindManager(context);
        }
        return instance;
    }

    public void deleteAll() {
        synchronized (LOCK) {
            openWriteDB();
            this.database.delete(Table.T_NAME, null, null);
        }
    }

    public void deleteTable() {
        synchronized (LOCK) {
            this.database.execSQL("truncate table easy_bind");
        }
    }

    public void add(BindBean t) {
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.util.ArrayList<com.easyctrl.ldy.domain.BindBean> findBeansByModuleID(int r10) {
        /*
        r9 = this;
        r5 = LOCK;
        monitor-enter(r5);
        r0 = 0;
        r9.openReadDB();	 Catch:{ all -> 0x004f }
        r2 = 0;
        r4 = " bind_moduleID = ? ";
        r6 = 1;
        r6 = new java.lang.String[r6];	 Catch:{ Exception -> 0x005f }
        r7 = 0;
        r8 = java.lang.String.valueOf(r10);	 Catch:{ Exception -> 0x005f }
        r6[r7] = r8;	 Catch:{ Exception -> 0x005f }
        r2 = r9.getCursor(r4, r6);	 Catch:{ Exception -> 0x005f }
        if (r2 == 0) goto L_0x0020;
    L_0x001a:
        r4 = r2.getCount();	 Catch:{ Exception -> 0x005f }
        if (r4 != 0) goto L_0x0028;
    L_0x0020:
        if (r2 == 0) goto L_0x0025;
    L_0x0022:
        r2.close();	 Catch:{ all -> 0x004f }
    L_0x0025:
        monitor-exit(r5);	 Catch:{ all -> 0x004f }
        r4 = 0;
    L_0x0027:
        return r4;
    L_0x0028:
        r1 = new java.util.ArrayList;	 Catch:{ Exception -> 0x005f }
        r1.<init>();	 Catch:{ Exception -> 0x005f }
    L_0x002d:
        r4 = r2.moveToNext();	 Catch:{ Exception -> 0x0044, all -> 0x005c }
        if (r4 != 0) goto L_0x003c;
    L_0x0033:
        if (r2 == 0) goto L_0x0038;
    L_0x0035:
        r2.close();	 Catch:{ all -> 0x0059 }
    L_0x0038:
        r0 = r1;
    L_0x0039:
        monitor-exit(r5);	 Catch:{ all -> 0x004f }
        r4 = r0;
        goto L_0x0027;
    L_0x003c:
        r4 = r9.cursorToBean(r2);	 Catch:{ Exception -> 0x0044, all -> 0x005c }
        r1.add(r4);	 Catch:{ Exception -> 0x0044, all -> 0x005c }
        goto L_0x002d;
    L_0x0044:
        r3 = move-exception;
        r0 = r1;
    L_0x0046:
        r3.printStackTrace();	 Catch:{ all -> 0x0052 }
        if (r2 == 0) goto L_0x0039;
    L_0x004b:
        r2.close();	 Catch:{ all -> 0x004f }
        goto L_0x0039;
    L_0x004f:
        r4 = move-exception;
    L_0x0050:
        monitor-exit(r5);	 Catch:{ all -> 0x004f }
        throw r4;
    L_0x0052:
        r4 = move-exception;
    L_0x0053:
        if (r2 == 0) goto L_0x0058;
    L_0x0055:
        r2.close();	 Catch:{ all -> 0x004f }
    L_0x0058:
        throw r4;	 Catch:{ all -> 0x004f }
    L_0x0059:
        r4 = move-exception;
        r0 = r1;
        goto L_0x0050;
    L_0x005c:
        r4 = move-exception;
        r0 = r1;
        goto L_0x0053;
    L_0x005f:
        r3 = move-exception;
        goto L_0x0046;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.easyctrl.db.BindManager.findBeansByModuleID(int):java.util.ArrayList<com.easyctrl.ldy.domain.BindBean>");
    }

    public BindBean findBeanByModuleIdAndKeyIp(int moduleID, int keyValue) {
        BindBean bindBean;
        synchronized (LOCK) {
            bindBean = null;
            openReadDB();
            Cursor cursor = null;
            try {
                cursor = getCursor(" bind_moduleID = ? and keyValue = ? ", new String[]{String.valueOf(moduleID), String.valueOf(keyValue)});
                while (cursor.moveToNext()) {
                    bindBean = cursorToBean(cursor);
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
        return bindBean;
    }

    public void batchSave(int moduleID) {
        synchronized (LOCK) {
            openReadDB();
            Cursor cursor = null;
            try {
                cursor = this.database.query(Table.T_NAME, null, " bind_moduleID = ? ", new String[]{String.valueOf(moduleID)}, null, null, null);
                if (cursor == null || cursor.getCount() != 8) {
                    this.database.delete(Table.T_NAME, " bind_moduleID = ?  ", new String[]{String.valueOf(moduleID)});
                    String saveSql = " INSERT INTO " + Table.T_NAME + " (" + " bind_moduleID,keyValue " + ") values" + " (?,?)";
                    for (int i = 1; i <= 8; i++) {
                        this.database.execSQL(saveSql, new Object[]{MainApplication.userManager.currentHost(), Integer.valueOf(moduleID), Integer.valueOf(i)});
                    }
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

    public int update(BindBean t, int id) {
        int col;
        synchronized (LOCK) {
            openWriteDB();
            col = this.database.update(Table.T_NAME, beanToValues(t), " bind_moduleID = ? and keyValue = ? ", new String[]{String.valueOf(t.bind_moduleID), String.valueOf(t.keyValue)});
        }
        return col;
    }

    public ArrayList<BindBean> findAll() {
        return null;
    }

    public ContentValues beanToValues(BindBean bean) {
        ContentValues values = new ContentValues();
        values.put(Table.NAME, bean.bind_name);
        values.put(Table.FIELD_BINDMODULEID, Integer.valueOf(bean.bindModuleID));
        values.put(Table.FIELD_BINDPORT, Integer.valueOf(bean.bindPort));
        values.put(Table.FIELD_MODEL, bean.bind_model);
        values.put(Table.FIELD_TYPE, Integer.valueOf(bean.type));
        values.put(Table.FIELD_FLOOR, bean.floor);
        values.put(Table.FIELD_ROOM, bean.room);
        values.put(Table.DEVICENAME, bean.deviceName);
        values.put(Table.STAT, Integer.valueOf(bean.bind_stat));
        return values;
    }

    public BindBean cursorToBean(Cursor cursor) {
        BindBean bindBean = new BindBean();
        bindBean.bindID = cursor.getInt(cursor.getColumnIndexOrThrow(Table.ID));
        bindBean.keyValue = cursor.getInt(cursor.getColumnIndexOrThrow(Table.KEYVALUE));
        bindBean.bind_moduleID = cursor.getInt(cursor.getColumnIndexOrThrow(Table.MOUDLEID));
        bindBean.bind_stat = cursor.getInt(cursor.getColumnIndexOrThrow(Table.STAT));
        bindBean.bind_name = cursor.getString(cursor.getColumnIndexOrThrow(Table.NAME));
        bindBean.bindModuleID = cursor.getInt(cursor.getColumnIndexOrThrow(Table.FIELD_BINDMODULEID));
        bindBean.bindPort = cursor.getInt(cursor.getColumnIndexOrThrow(Table.FIELD_BINDPORT));
        bindBean.bind_model = cursor.getString(cursor.getColumnIndexOrThrow(Table.FIELD_MODEL));
        bindBean.type = cursor.getInt(cursor.getColumnIndexOrThrow(Table.FIELD_TYPE));
        bindBean.floor = cursor.getString(cursor.getColumnIndexOrThrow(Table.FIELD_FLOOR));
        bindBean.room = cursor.getString(cursor.getColumnIndexOrThrow(Table.FIELD_ROOM));
        bindBean.deviceName = cursor.getString(cursor.getColumnIndexOrThrow(Table.DEVICENAME));
        return bindBean;
    }

    public Cursor getCursor(String where, String[] args) {
        return this.database.query(Table.T_NAME, null, where, args, null, null, null);
    }

    public BindBean findByID(int id) {
        BindBean bindBean;
        synchronized (LOCK) {
            openReadDB();
            bindBean = null;
            Cursor cursor = null;
            try {
                cursor = this.database.query(Table.T_NAME, null, " bindID = ? ", new String[]{String.valueOf(id)}, null, null, null);
                if (cursor.getCount() > 0) {
                    cursor.moveToNext();
                    bindBean = cursorToBean(cursor);
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
        return bindBean;
    }
}
