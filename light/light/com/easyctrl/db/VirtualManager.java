package com.easyctrl.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import com.easyctrl.iface.BaseDBImpl;
import com.easyctrl.ldy.domain.VirtualBean;
import com.easyctrl.ldy.domain.VirtualBean.Table;
import java.util.ArrayList;

public class VirtualManager extends DataBaseManage implements BaseDBImpl<VirtualBean> {
    private static VirtualManager instance;

    private VirtualManager(Context context) {
        super(context);
    }

    public static VirtualManager getInstance(Context context) {
        if (instance == null) {
            instance = new VirtualManager(context);
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

    private ArrayList<VirtualBean> initVirtual() {
        ArrayList<VirtualBean> array = new ArrayList();
        int k = 1;
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                VirtualBean bean = new VirtualBean();
                bean.arrayType = i;
                bean.key = k;
                bean.v_scene_id = k + 200;
                k++;
                array.add(bean);
            }
        }
        return array;
    }

    public ArrayList<String> findArrayType() {
        ArrayList<String> list;
        synchronized (LOCK) {
            list = new ArrayList();
            openReadDB();
            Cursor cursor = null;
            try {
                cursor = this.database.query(true, Table.T_NAME, new String[]{Table.ARRAYTYPE}, null, null, null, null, null, null);
                while (cursor.moveToNext()) {
                    list.add(cursor.getString(0));
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
        return list;
    }

    public void batchSave() {
        synchronized (LOCK) {
            if (currentNum() < 100) {
                try {
                    openWriteDB();
                    this.database.beginTransaction();
                    ArrayList<VirtualBean> array = initVirtual();
                    for (int i = 0; i < array.size(); i++) {
                        VirtualBean bean = (VirtualBean) array.get(i);
                        this.database.execSQL(" insert into easy_virtual_key (arrayType,key ,v_scene_id) values(?,?,?)", new Object[]{Integer.valueOf(bean.arrayType), Integer.valueOf(bean.key), Integer.valueOf(bean.v_scene_id)});
                    }
                    this.database.setTransactionSuccessful();
                    this.database.endTransaction();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private int currentNum() {
        openReadDB();
        int col = 0;
        Cursor cursor = null;
        try {
            cursor = this.database.query(Table.T_NAME, null, null, null, null, null, null);
            col = cursor.getCount();
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
        return col;
    }

    public void add(VirtualBean t) {
    }

    public int delete(int id) {
        return 0;
    }

    public int update(VirtualBean t, int id) {
        int col;
        synchronized (LOCK) {
            openWriteDB();
            col = 0;
            try {
                col = this.database.update(Table.T_NAME, beanToValues(t), " virtualID = ? ", new String[]{String.valueOf(id)});
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return col;
    }

    public ArrayList<VirtualBean> findAll() {
        return null;
    }

    public ContentValues beanToValues(VirtualBean bean) {
        ContentValues values = new ContentValues();
        values.put("name", bean.name);
        values.put(Table.FIELD_DEVICENAME, bean.deviceName);
        values.put(Table.FIELD_ROOM, bean.room);
        values.put(Table.FIELD_FLOOR, bean.floor);
        values.put("moduleID", Integer.valueOf(bean.moduleID));
        values.put("port", Integer.valueOf(bean.port));
        values.put(Table.FIELD_MODEL, bean.model);
        values.put(Table.FIELD_TYPE, Integer.valueOf(bean.bindType));
        return values;
    }

    public VirtualBean cursorToBean(Cursor cursor) {
        VirtualBean bean = new VirtualBean();
        bean.arrayType = cursor.getInt(cursor.getColumnIndexOrThrow(Table.ARRAYTYPE));
        bean.virtualID = cursor.getInt(cursor.getColumnIndexOrThrow(Table.FIELD_ID));
        bean.key = cursor.getInt(cursor.getColumnIndexOrThrow("key"));
        bean.name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
        bean.state = cursor.getInt(cursor.getColumnIndexOrThrow("state"));
        bean.floor = cursor.getString(cursor.getColumnIndexOrThrow(Table.FIELD_FLOOR));
        bean.room = cursor.getString(cursor.getColumnIndexOrThrow(Table.FIELD_ROOM));
        bean.deviceName = cursor.getString(cursor.getColumnIndexOrThrow(Table.FIELD_DEVICENAME));
        bean.moduleID = cursor.getInt(cursor.getColumnIndexOrThrow("moduleID"));
        bean.port = cursor.getInt(cursor.getColumnIndexOrThrow("port"));
        bean.bindType = cursor.getInt(cursor.getColumnIndexOrThrow(Table.FIELD_TYPE));
        bean.model = cursor.getString(cursor.getColumnIndexOrThrow(Table.FIELD_MODEL));
        bean.v_scene_id = cursor.getInt(cursor.getColumnIndexOrThrow(Table.FIELD_SCENE_ID));
        return bean;
    }

    public Cursor getCursor(String where, String[] args) {
        return null;
    }

    public ArrayList<VirtualBean> findByType(int type) {
        ArrayList<VirtualBean> arrayList;
        Exception e;
        Throwable th;
        synchronized (LOCK) {
            openReadDB();
            arrayList = null;
            Cursor cursor = null;
            try {
                cursor = this.database.query(Table.T_NAME, null, " arrayType = ? ", new String[]{String.valueOf(type)}, null, null, null);
                if (cursor != null && cursor.getCount() > 0) {
                    ArrayList<VirtualBean> arrayList2 = new ArrayList();
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

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.easyctrl.ldy.domain.VirtualBean findByID(int r14) {
        /*
        r13 = this;
        r11 = 0;
        r12 = LOCK;
        monitor-enter(r12);
        r13.openReadDB();	 Catch:{ all -> 0x0053 }
        r10 = 0;
        r8 = 0;
        r0 = r13.database;	 Catch:{ Exception -> 0x0040 }
        r1 = "easy_virtual_key";
        r2 = 0;
        r3 = " virtualID = ? ";
        r4 = 1;
        r4 = new java.lang.String[r4];	 Catch:{ Exception -> 0x0040 }
        r5 = 0;
        r6 = java.lang.String.valueOf(r14);	 Catch:{ Exception -> 0x0040 }
        r4[r5] = r6;	 Catch:{ Exception -> 0x0040 }
        r5 = 0;
        r6 = 0;
        r7 = 0;
        r8 = r0.query(r1, r2, r3, r4, r5, r6, r7);	 Catch:{ Exception -> 0x0040 }
        r0 = r8.getCount();	 Catch:{ Exception -> 0x0040 }
        if (r0 != 0) goto L_0x0030;
    L_0x0027:
        if (r8 == 0) goto L_0x002d;
    L_0x0029:
        r8.close();	 Catch:{ all -> 0x0053 }
        r8 = 0;
    L_0x002d:
        monitor-exit(r12);	 Catch:{ all -> 0x0053 }
        r0 = r11;
    L_0x002f:
        return r0;
    L_0x0030:
        r8.moveToNext();	 Catch:{ Exception -> 0x0040 }
        r10 = r13.cursorToBean(r8);	 Catch:{ Exception -> 0x0040 }
        if (r8 == 0) goto L_0x003d;
    L_0x0039:
        r8.close();	 Catch:{ all -> 0x0053 }
        r8 = 0;
    L_0x003d:
        monitor-exit(r12);	 Catch:{ all -> 0x0053 }
        r0 = r10;
        goto L_0x002f;
    L_0x0040:
        r9 = move-exception;
        r9.printStackTrace();	 Catch:{ all -> 0x004b }
        if (r8 == 0) goto L_0x003d;
    L_0x0046:
        r8.close();	 Catch:{ all -> 0x0053 }
        r8 = 0;
        goto L_0x003d;
    L_0x004b:
        r0 = move-exception;
        if (r8 == 0) goto L_0x0052;
    L_0x004e:
        r8.close();	 Catch:{ all -> 0x0053 }
        r8 = 0;
    L_0x0052:
        throw r0;	 Catch:{ all -> 0x0053 }
    L_0x0053:
        r0 = move-exception;
        monitor-exit(r12);	 Catch:{ all -> 0x0053 }
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.easyctrl.db.VirtualManager.findByID(int):com.easyctrl.ldy.domain.VirtualBean");
    }
}
