package com.easyctrl.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import com.easyctrl.iface.BaseDBImpl;
import com.easyctrl.ldy.activity.MainApplication;
import com.easyctrl.ldy.domain.FloorBean;
import com.easyctrl.ldy.domain.FloorBean.Table;
import com.easyctrl.ldy.domain.VirtualBean;
import java.util.ArrayList;

public class FloorManager extends DataBaseManage implements BaseDBImpl<FloorBean> {
    private static FloorManager instance;

    private FloorManager(Context context) {
        super(context);
        openWriteDB();
    }

    public static synchronized FloorManager getInstance(Context context) {
        FloorManager floorManager;
        synchronized (FloorManager.class) {
            if (instance == null) {
                instance = new FloorManager(context);
            }
            floorManager = instance;
        }
        return floorManager;
    }

    public void deleteAll() {
        synchronized (LOCK) {
            openWriteDB();
            this.database.delete(Table.T_NAME, null, null);
            this.database.execSQL(" DELETE FROM sqlite_sequence ");
        }
    }

    public void add(FloorBean t) {
        synchronized (LOCK) {
            String sql = " insert into " + Table.T_NAME + "(name,sname,paixu) values(?,?,?)";
            try {
                if (findByName(t.name) == null) {
                    openWriteDB();
                    this.database.execSQL(sql, new Object[]{t.name, t.sname, Integer.valueOf(t.paixu)});
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.easyctrl.ldy.domain.FloorBean findByID(int r10) {
        /*
        r9 = this;
        r5 = LOCK;
        monitor-enter(r5);
        r9.openReadDB();	 Catch:{ all -> 0x0046 }
        r2 = 0;
        r0 = 0;
        r4 = " id = ? ";
        r6 = 1;
        r6 = new java.lang.String[r6];	 Catch:{ Exception -> 0x003c }
        r7 = 0;
        r8 = java.lang.String.valueOf(r10);	 Catch:{ Exception -> 0x003c }
        r6[r7] = r8;	 Catch:{ Exception -> 0x003c }
        r2 = r9.getCursor(r4, r6);	 Catch:{ Exception -> 0x003c }
        if (r2 == 0) goto L_0x0020;
    L_0x001a:
        r4 = r2.getCount();	 Catch:{ Exception -> 0x003c }
        if (r4 != 0) goto L_0x0028;
    L_0x0020:
        if (r2 == 0) goto L_0x0025;
    L_0x0022:
        r2.close();	 Catch:{ all -> 0x0046 }
    L_0x0025:
        monitor-exit(r5);	 Catch:{ all -> 0x0046 }
        r4 = 0;
    L_0x0027:
        return r4;
    L_0x0028:
        r1 = new com.easyctrl.ldy.domain.FloorBean;	 Catch:{ Exception -> 0x003c }
        r1.<init>();	 Catch:{ Exception -> 0x003c }
        r2.moveToNext();	 Catch:{ Exception -> 0x0053, all -> 0x0050 }
        r0 = r9.cursorToBean(r2);	 Catch:{ Exception -> 0x0053, all -> 0x0050 }
        if (r2 == 0) goto L_0x0039;
    L_0x0036:
        r2.close();	 Catch:{ all -> 0x0046 }
    L_0x0039:
        monitor-exit(r5);	 Catch:{ all -> 0x0046 }
        r4 = r0;
        goto L_0x0027;
    L_0x003c:
        r3 = move-exception;
    L_0x003d:
        r3.printStackTrace();	 Catch:{ all -> 0x0049 }
        if (r2 == 0) goto L_0x0039;
    L_0x0042:
        r2.close();	 Catch:{ all -> 0x0046 }
        goto L_0x0039;
    L_0x0046:
        r4 = move-exception;
        monitor-exit(r5);	 Catch:{ all -> 0x0046 }
        throw r4;
    L_0x0049:
        r4 = move-exception;
    L_0x004a:
        if (r2 == 0) goto L_0x004f;
    L_0x004c:
        r2.close();	 Catch:{ all -> 0x0046 }
    L_0x004f:
        throw r4;	 Catch:{ all -> 0x0046 }
    L_0x0050:
        r4 = move-exception;
        r0 = r1;
        goto L_0x004a;
    L_0x0053:
        r3 = move-exception;
        r0 = r1;
        goto L_0x003d;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.easyctrl.db.FloorManager.findByID(int):com.easyctrl.ldy.domain.FloorBean");
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.easyctrl.ldy.domain.FloorBean findByName(java.lang.String r10) {
        /*
        r9 = this;
        r4 = 0;
        if (r10 != 0) goto L_0x0005;
    L_0x0003:
        r0 = r4;
    L_0x0004:
        return r0;
    L_0x0005:
        r5 = LOCK;
        monitor-enter(r5);
        r9.openReadDB();	 Catch:{ all -> 0x003b }
        r6 = " name = ? ";
        r7 = 1;
        r7 = new java.lang.String[r7];	 Catch:{ all -> 0x003b }
        r8 = 0;
        r7[r8] = r10;	 Catch:{ all -> 0x003b }
        r2 = r9.getCursor(r6, r7);	 Catch:{ all -> 0x003b }
        r0 = 0;
        if (r2 == 0) goto L_0x0020;
    L_0x001a:
        r6 = r2.getCount();	 Catch:{ Exception -> 0x003e }
        if (r6 != 0) goto L_0x0028;
    L_0x0020:
        if (r2 == 0) goto L_0x0025;
    L_0x0022:
        r2.close();	 Catch:{ all -> 0x003b }
    L_0x0025:
        monitor-exit(r5);	 Catch:{ all -> 0x003b }
        r0 = r4;
        goto L_0x0004;
    L_0x0028:
        r1 = new com.easyctrl.ldy.domain.FloorBean;	 Catch:{ Exception -> 0x003e }
        r1.<init>();	 Catch:{ Exception -> 0x003e }
        r2.moveToNext();	 Catch:{ Exception -> 0x0052, all -> 0x004f }
        r0 = r9.cursorToBean(r2);	 Catch:{ Exception -> 0x0052, all -> 0x004f }
        if (r2 == 0) goto L_0x0039;
    L_0x0036:
        r2.close();	 Catch:{ all -> 0x003b }
    L_0x0039:
        monitor-exit(r5);	 Catch:{ all -> 0x003b }
        goto L_0x0004;
    L_0x003b:
        r4 = move-exception;
        monitor-exit(r5);	 Catch:{ all -> 0x003b }
        throw r4;
    L_0x003e:
        r3 = move-exception;
    L_0x003f:
        r3.printStackTrace();	 Catch:{ all -> 0x0048 }
        if (r2 == 0) goto L_0x0039;
    L_0x0044:
        r2.close();	 Catch:{ all -> 0x003b }
        goto L_0x0039;
    L_0x0048:
        r4 = move-exception;
    L_0x0049:
        if (r2 == 0) goto L_0x004e;
    L_0x004b:
        r2.close();	 Catch:{ all -> 0x003b }
    L_0x004e:
        throw r4;	 Catch:{ all -> 0x003b }
    L_0x004f:
        r4 = move-exception;
        r0 = r1;
        goto L_0x0049;
    L_0x0052:
        r3 = move-exception;
        r0 = r1;
        goto L_0x003f;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.easyctrl.db.FloorManager.findByName(java.lang.String):com.easyctrl.ldy.domain.FloorBean");
    }

    public int delete(int id) {
        openWriteDB();
        int col = 0;
        try {
            this.database.beginTransaction();
            col = this.database.delete(Table.T_NAME, " id = ? ", new String[]{String.valueOf(id)});
            this.database.setTransactionSuccessful();
            this.database.endTransaction();
            return col;
        } catch (Exception e) {
            e.printStackTrace();
            return col;
        }
    }

    public int update(FloorBean t, int id) {
        openWriteDB();
        int col = 0;
        try {
            col = this.database.update(Table.T_NAME, beanToValues(t), " id = ?  ", new String[]{String.valueOf(id)});
        } catch (Exception e) {
            e.printStackTrace();
        }
        return col;
    }

    public ContentValues beanToValues(FloorBean bean) {
        ContentValues values = new ContentValues();
        values.put(Table.NAME, bean.name);
        values.put(Table.SNAME, bean.sname);
        return values;
    }

    public ArrayList<FloorBean> findBeanAll() {
        openReadDB();
        ArrayList<FloorBean> beans = new ArrayList();
        Cursor cursor = this.database.query(Table.T_NAME, null, null, null, null, null, "paixu");
        while (cursor.moveToNext()) {
            try {
                FloorBean bean = cursorToBean(cursor);
                bean.isUse = MainApplication.modulePortManager.isUseName(VirtualBean.Table.FIELD_FLOOR, " floor = ?  ", new String[]{bean.name});
                beans.add(bean);
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
        if (cursor != null) {
            cursor.close();
        }
        return beans;
    }

    public ArrayList<String> findAllName() {
        openReadDB();
        ArrayList<String> beans = new ArrayList();
        Cursor cursor = getCursor(null, null);
        while (cursor.moveToNext()) {
            try {
                beans.add(cursorToBean(cursor).name);
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
        if (cursor != null) {
            cursor.close();
        }
        return beans;
    }

    public FloorBean cursorToBean(Cursor cursor) {
        FloorBean bean = new FloorBean();
        bean.id = cursor.getInt(cursor.getColumnIndexOrThrow(Table.ID));
        bean.name = cursor.getString(cursor.getColumnIndexOrThrow(Table.NAME));
        bean.paixu = cursor.getInt(cursor.getColumnIndexOrThrow(Table.PAIXU));
        bean.sname = cursor.getString(cursor.getColumnIndexOrThrow(Table.SNAME));
        return bean;
    }

    public Cursor getCursor(String where, String[] args) {
        Cursor cursor = null;
        try {
            cursor = this.database.query(Table.T_NAME, null, where, args, null, null, "paixu");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cursor;
    }

    public int getNumber(String roomName, int number) {
        openReadDB();
        Cursor cursor = this.database.query(Table.T_NAME, new String[]{Table.PAIXU}, " sname = ? and paixu = ?  ", new String[]{roomName, String.valueOf(number)}, null, null, null);
        int resultNum = number;
        if (cursor.getCount() > 0) {
            resultNum++;
            cursor.close();
            return getNumber(roomName, resultNum);
        }
        cursor.close();
        return resultNum;
    }

    public ArrayList<FloorBean> findAll() {
        return null;
    }
}
