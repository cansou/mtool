package com.easyctrl.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.util.Log;
import com.easyctrl.iface.BaseDBImpl;
import com.easyctrl.ldy.domain.SceneBean;
import com.easyctrl.ldy.domain.SceneBean.Table;
import java.util.ArrayList;
import java.util.Arrays;

public class SceneManager extends DataBaseManage implements BaseDBImpl<SceneBean> {
    private static SceneManager instance;

    private SceneManager(Context context) {
        super(context);
    }

    public static SceneManager getInstance(Context context) {
        if (instance == null) {
            instance = new SceneManager(context);
        }
        return instance;
    }

    public void add(SceneBean t) {
        synchronized (LOCK) {
            openWriteDB();
            this.database.insert(Table.T_NAME, null, beanToValues(t));
        }
    }

    public void deleteAll() {
        synchronized (LOCK) {
            openWriteDB();
            try {
                this.database.delete(Table.T_NAME, null, null);
                this.database.execSQL(" DELETE FROM sqlite_sequence ");
            } catch (SQLException e) {
                Log.i("data", "\u6ca1\u6709\u8be5\u8868\u5219\u53bb\u521b\u5efa");
                this.database.execSQL(Table.generateTableSql());
            }
        }
    }

    public int delete(int id) {
        int raw;
        synchronized (LOCK) {
            openWriteDB();
            raw = this.database.delete(Table.T_NAME, " id = ? ", new String[]{String.valueOf(id)});
        }
        return raw;
    }

    public int update(SceneBean t, int id) {
        int raw;
        synchronized (LOCK) {
            openWriteDB();
            raw = this.database.update(Table.T_NAME, beanToValues(t), " sceneBean_id = ? ", new String[]{String.valueOf(id)});
        }
        return raw;
    }

    public ArrayList<SceneBean> findAll() {
        ArrayList<SceneBean> arrayList;
        Exception e;
        synchronized (LOCK) {
            arrayList = null;
            openReadDB();
            Cursor cursor = null;
            try {
                cursor = this.database.query(Table.T_NAME, null, null, null, null, null, null);
                if (cursor.getCount() > 0) {
                    ArrayList<SceneBean> list = new ArrayList();
                    while (cursor.moveToNext()) {
                        try {
                            list.add(cursorToBean(cursor));
                        } catch (Exception e2) {
                            e = e2;
                            arrayList = list;
                        } catch (Throwable th) {
                            Throwable th2 = th;
                            arrayList = list;
                        }
                    }
                    arrayList = list;
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
                    th2 = th3;
                    if (cursor != null) {
                        cursor.close();
                    }
                    throw th2;
                }
            }
        }
        return arrayList;
    }

    public ContentValues beanToValues(SceneBean bean) {
        ContentValues values = new ContentValues();
        values.put(Table.Field_bindid, Integer.valueOf(bean.sceneBean_bindid));
        values.put(Table.Field_type, Integer.valueOf(bean.sceneBean_type));
        values.put(Table.Field_sceneModel, bean.sceneModel);
        values.put(Table.Field_bindBeanID, Integer.valueOf(bean.sceneBean_bindBeanID));
        values.put(Table.Field_mian_keyValue, Integer.valueOf(bean.sceneBean_mian_keyValue));
        values.put(Table.Field_mian_mianID, Integer.valueOf(bean.sceneBean_mian_mianID));
        values.put(Table.Field_sceneBean_jsonName, bean.sceneBean_jsonName);
        return values;
    }

    private int getColumn(Cursor cursor, String field) {
        return cursor.getColumnIndexOrThrow(field);
    }

    public SceneBean cursorToBean(Cursor cursor) {
        SceneBean sceneBean = new SceneBean();
        sceneBean.sceneBean_id = cursor.getInt(getColumn(cursor, Table.Field_id));
        sceneBean.sceneBean_bindid = cursor.getInt(getColumn(cursor, Table.Field_bindid));
        sceneBean.sceneModel = cursor.getString(getColumn(cursor, Table.Field_sceneModel));
        sceneBean.sceneBean_type = cursor.getInt(getColumn(cursor, Table.Field_type));
        sceneBean.sceneBean_mian_keyValue = cursor.getInt(getColumn(cursor, Table.Field_mian_keyValue));
        sceneBean.sceneBean_mian_mianID = cursor.getInt(getColumn(cursor, Table.Field_mian_mianID));
        sceneBean.sceneBean_bindBeanID = cursor.getInt(getColumn(cursor, Table.Field_bindBeanID));
        sceneBean.sceneBean_jsonName = cursor.getString(getColumn(cursor, Table.Field_sceneBean_jsonName));
        return sceneBean;
    }

    public Cursor getCursor(String where, String[] args) {
        return null;
    }

    private int getNumberSort(ArrayList<Integer> paixus) {
        int i;
        int[] sort = new int[paixus.size()];
        for (i = 0; i < paixus.size(); i++) {
            sort[i] = ((Integer) paixus.get(i)).intValue();
        }
        Arrays.sort(sort);
        i = 0;
        while (i < sort.length) {
            try {
                if (sort[0] != 1) {
                    return 1;
                }
                if (sort.length == i + 1) {
                    return sort[i] + 1;
                }
                if (sort[i + 1] - sort[i] != 1) {
                    return sort[i] + 1;
                }
                i++;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int getLastID() {
        /*
        r15 = this;
        r14 = LOCK;
        monitor-enter(r14);
        r15.openReadDB();	 Catch:{ all -> 0x005f }
        r8 = 0;
        r12 = 0;
        r0 = r15.database;	 Catch:{ Exception -> 0x006c }
        r1 = "easy_scenebean";
        r2 = 1;
        r2 = new java.lang.String[r2];	 Catch:{ Exception -> 0x006c }
        r3 = 0;
        r4 = "sceneBean_jsonName";
        r2[r3] = r4;	 Catch:{ Exception -> 0x006c }
        r3 = 0;
        r4 = 0;
        r5 = 0;
        r6 = 0;
        r7 = "sceneBean_jsonName";
        r8 = r0.query(r1, r2, r3, r4, r5, r6, r7);	 Catch:{ Exception -> 0x006c }
        r0 = r8.getCount();	 Catch:{ Exception -> 0x006c }
        if (r0 <= 0) goto L_0x0062;
    L_0x0024:
        r13 = new java.util.ArrayList;	 Catch:{ Exception -> 0x006c }
        r13.<init>();	 Catch:{ Exception -> 0x006c }
    L_0x0029:
        r0 = r8.moveToNext();	 Catch:{ Exception -> 0x0049, all -> 0x0069 }
        if (r0 != 0) goto L_0x003c;
    L_0x002f:
        r0 = r15.getNumberSort(r13);	 Catch:{ Exception -> 0x0049, all -> 0x0069 }
        if (r8 == 0) goto L_0x0039;
    L_0x0035:
        r8.close();	 Catch:{ all -> 0x005f }
        r8 = 0;
    L_0x0039:
        monitor-exit(r14);	 Catch:{ all -> 0x005f }
        r12 = r13;
    L_0x003b:
        return r0;
    L_0x003c:
        r0 = 0;
        r11 = r8.getString(r0);	 Catch:{ Exception -> 0x0049, all -> 0x0069 }
        r10 = java.lang.Integer.valueOf(r11);	 Catch:{ Exception -> 0x0049, all -> 0x0069 }
        r13.add(r10);	 Catch:{ Exception -> 0x0049, all -> 0x0069 }
        goto L_0x0029;
    L_0x0049:
        r9 = move-exception;
        r12 = r13;
    L_0x004b:
        r9.printStackTrace();	 Catch:{ all -> 0x0057 }
        if (r8 == 0) goto L_0x0054;
    L_0x0050:
        r8.close();	 Catch:{ all -> 0x005f }
        r8 = 0;
    L_0x0054:
        monitor-exit(r14);	 Catch:{ all -> 0x005f }
        r0 = 1;
        goto L_0x003b;
    L_0x0057:
        r0 = move-exception;
    L_0x0058:
        if (r8 == 0) goto L_0x005e;
    L_0x005a:
        r8.close();	 Catch:{ all -> 0x005f }
        r8 = 0;
    L_0x005e:
        throw r0;	 Catch:{ all -> 0x005f }
    L_0x005f:
        r0 = move-exception;
        monitor-exit(r14);	 Catch:{ all -> 0x005f }
        throw r0;
    L_0x0062:
        if (r8 == 0) goto L_0x0054;
    L_0x0064:
        r8.close();	 Catch:{ all -> 0x005f }
        r8 = 0;
        goto L_0x0054;
    L_0x0069:
        r0 = move-exception;
        r12 = r13;
        goto L_0x0058;
    L_0x006c:
        r9 = move-exception;
        goto L_0x004b;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.easyctrl.db.SceneManager.getLastID():int");
    }

    public void deleteByBindID(int bindID) {
        synchronized (LOCK) {
            openWriteDB();
            this.database.delete(Table.T_NAME, " sceneBean_bindBeanID = ? ", new String[]{String.valueOf(bindID)});
        }
    }

    public ArrayList<Integer> findDeleteByBindID(int bindID) {
        ArrayList<Integer> arrayList;
        Exception e;
        Throwable th;
        synchronized (LOCK) {
            openReadDB();
            Cursor cursor = null;
            arrayList = null;
            try {
                cursor = this.database.query(Table.T_NAME, new String[]{Table.Field_sceneBean_jsonName}, " sceneBean_bindBeanID = ? ", new String[]{String.valueOf(bindID)}, null, null, null);
                if (cursor.getCount() > 0) {
                    ArrayList<Integer> deletes = new ArrayList();
                    while (cursor.moveToNext()) {
                        try {
                            deletes.add(Integer.valueOf(cursor.getString(0)));
                        } catch (Exception e2) {
                            e = e2;
                            arrayList = deletes;
                        } catch (Throwable th2) {
                            th = th2;
                            arrayList = deletes;
                        }
                    }
                    arrayList = deletes;
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

    public ArrayList<SceneBean> findByIDandKey(int id, int key) {
        ArrayList<SceneBean> arrayList;
        Exception e;
        Throwable th;
        synchronized (LOCK) {
            openReadDB();
            Cursor cursor = null;
            arrayList = null;
            try {
                cursor = this.database.query(Table.T_NAME, null, " sceneBean_mian_mianID = ? and sceneBean_mian_keyValue = ? ", new String[]{String.valueOf(id), String.valueOf(key)}, null, null, null);
                if (cursor.getCount() > 0) {
                    ArrayList<SceneBean> arrays = new ArrayList();
                    while (cursor.moveToNext()) {
                        try {
                            arrays.add(cursorToBean(cursor));
                        } catch (Exception e2) {
                            e = e2;
                            arrayList = arrays;
                        } catch (Throwable th2) {
                            th = th2;
                            arrayList = arrays;
                        }
                    }
                    arrayList = arrays;
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

    public SceneBean findByBindID(int bindID, String model) {
        SceneBean sceneBean;
        synchronized (LOCK) {
            openReadDB();
            Cursor cursor = null;
            sceneBean = null;
            try {
                cursor = this.database.query(Table.T_NAME, null, " sceneBean_bindBeanID = ? and sceneModel = ? ", new String[]{String.valueOf(bindID), model}, null, null, null);
                if (cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        sceneBean = cursorToBean(cursor);
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
        return sceneBean;
    }
}
