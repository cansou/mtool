package com.easyctrl.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import com.easyctrl.iface.BaseDBImpl;
import com.easyctrl.ldy.domain.ModulePortBean;
import com.easyctrl.ldy.domain.ModulePortBean.Table;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class ModulePortManager extends DataBaseManage implements BaseDBImpl<ModulePortBean> {
    private static ModulePortManager instance;

    private ModulePortManager(Context context) {
        super(context);
    }

    public static synchronized ModulePortManager getInstance(Context context) {
        ModulePortManager modulePortManager;
        synchronized (ModulePortManager.class) {
            if (instance == null) {
                instance = new ModulePortManager(context);
            }
            modulePortManager = instance;
        }
        return modulePortManager;
    }

    public void deleteAll() {
        synchronized (LOCK) {
            openWriteDB();
            this.database.delete(Table.T_NAME, null, null);
            this.database.execSQL(" DELETE FROM sqlite_sequence ");
        }
    }

    public void add(ModulePortBean t) {
    }

    public void batchSave(ArrayList<ModulePortBean> beans, int type) {
        openWriteDB();
        try {
            ModulePortBean portbean = (ModulePortBean) beans.get(0);
            if (beans.size() != current(portbean.moduleID)) {
                deleteByModule(portbean.moduleID);
                this.database.beginTransaction();
                Iterator it = beans.iterator();
                while (it.hasNext()) {
                    ((ModulePortBean) it.next()).type = type;
                    this.database.execSQL(Table.getSaveSql(), new Object[]{Integer.valueOf(bean.moduleID), Integer.valueOf(bean.port), Integer.valueOf(bean.progress), Integer.valueOf(bean.isOpen), bean.floor, bean.room, bean.name, bean.deviveType, Integer.valueOf(bean.type)});
                }
                this.database.setTransactionSuccessful();
                this.database.endTransaction();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void batchSaveOrUpdate(ArrayList<ModulePortBean> beans, int type) {
        synchronized (LOCK) {
            if (beans != null) {
                try {
                    if (beans.size() != 0) {
                        ModulePortBean portbean = (ModulePortBean) beans.get(0);
                        Iterator it;
                        if (beans.size() != current(portbean.moduleID)) {
                            deleteByModule(portbean.moduleID);
                            openWriteDB();
                            this.database.beginTransaction();
                            it = beans.iterator();
                            while (it.hasNext()) {
                                ((ModulePortBean) it.next()).type = type;
                                this.database.execSQL(Table.getSaveSql(), new Object[]{Integer.valueOf(bean.moduleID), Integer.valueOf(bean.port), Integer.valueOf(bean.progress), Integer.valueOf(bean.isOpen), bean.floor, bean.room, bean.name, bean.deviveType, Integer.valueOf(bean.type)});
                            }
                            this.database.setTransactionSuccessful();
                            this.database.endTransaction();
                            return;
                        }
                        it = beans.iterator();
                        while (it.hasNext()) {
                            update((ModulePortBean) it.next(), 0);
                        }
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private int current(int moduleID) {
        openReadDB();
        Cursor cursor = null;
        int count = 0;
        try {
            cursor = getCursor(" moduleID = ? ", new String[]{String.valueOf(moduleID)});
            count = cursor.getCount();
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
        return count;
    }

    public int delete(int id) {
        openWriteDB();
        int count = 0;
        try {
            count = this.database.delete(Table.T_NAME, " id = ? ", new String[]{String.valueOf(id)});
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    public int deleteByModule(int moduleID) {
        openWriteDB();
        int count = 0;
        try {
            count = this.database.delete(Table.T_NAME, " moduleID = ? ", new String[]{String.valueOf(moduleID)});
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int getNumber(com.easyctrl.ldy.domain.ModulePortBean r14, int r15) {
        /*
        r13 = this;
        r12 = LOCK;
        monitor-enter(r12);
        r13.openReadDB();	 Catch:{ all -> 0x006c }
        r8 = 0;
        r10 = 0;
        r0 = r13.database;	 Catch:{ Exception -> 0x0078 }
        r1 = com.easyctrl.ldy.domain.ModulePortBean.Table.T_NAME;	 Catch:{ Exception -> 0x0078 }
        r2 = 0;
        r3 = " floor = ? and room = ? and name = ? ";
        r4 = 3;
        r4 = new java.lang.String[r4];	 Catch:{ Exception -> 0x0078 }
        r5 = 0;
        r6 = r14.floor;	 Catch:{ Exception -> 0x0078 }
        r4[r5] = r6;	 Catch:{ Exception -> 0x0078 }
        r5 = 1;
        r6 = r14.room;	 Catch:{ Exception -> 0x0078 }
        r4[r5] = r6;	 Catch:{ Exception -> 0x0078 }
        r5 = 2;
        r6 = r14.name;	 Catch:{ Exception -> 0x0078 }
        r4[r5] = r6;	 Catch:{ Exception -> 0x0078 }
        r5 = 0;
        r6 = 0;
        r7 = "paixu";
        r8 = r0.query(r1, r2, r3, r4, r5, r6, r7);	 Catch:{ Exception -> 0x0078 }
        r0 = r8.getCount();	 Catch:{ Exception -> 0x0078 }
        if (r0 <= 0) goto L_0x006f;
    L_0x002f:
        r11 = new java.util.ArrayList;	 Catch:{ Exception -> 0x0078 }
        r11.<init>();	 Catch:{ Exception -> 0x0078 }
    L_0x0034:
        r0 = r8.moveToNext();	 Catch:{ Exception -> 0x0058, all -> 0x0075 }
        if (r0 != 0) goto L_0x0046;
    L_0x003a:
        r0 = r13.getNumberSort(r11);	 Catch:{ Exception -> 0x0058, all -> 0x0075 }
        if (r8 == 0) goto L_0x0043;
    L_0x0040:
        r8.close();	 Catch:{ all -> 0x006c }
    L_0x0043:
        monitor-exit(r12);	 Catch:{ all -> 0x006c }
        r10 = r11;
    L_0x0045:
        return r0;
    L_0x0046:
        r0 = "paixu";
        r0 = r8.getColumnIndexOrThrow(r0);	 Catch:{ Exception -> 0x0058, all -> 0x0075 }
        r0 = r8.getInt(r0);	 Catch:{ Exception -> 0x0058, all -> 0x0075 }
        r0 = java.lang.Integer.valueOf(r0);	 Catch:{ Exception -> 0x0058, all -> 0x0075 }
        r11.add(r0);	 Catch:{ Exception -> 0x0058, all -> 0x0075 }
        goto L_0x0034;
    L_0x0058:
        r9 = move-exception;
        r10 = r11;
    L_0x005a:
        r9.printStackTrace();	 Catch:{ all -> 0x0065 }
        if (r8 == 0) goto L_0x0062;
    L_0x005f:
        r8.close();	 Catch:{ all -> 0x006c }
    L_0x0062:
        monitor-exit(r12);	 Catch:{ all -> 0x006c }
        r0 = 1;
        goto L_0x0045;
    L_0x0065:
        r0 = move-exception;
    L_0x0066:
        if (r8 == 0) goto L_0x006b;
    L_0x0068:
        r8.close();	 Catch:{ all -> 0x006c }
    L_0x006b:
        throw r0;	 Catch:{ all -> 0x006c }
    L_0x006c:
        r0 = move-exception;
        monitor-exit(r12);	 Catch:{ all -> 0x006c }
        throw r0;
    L_0x006f:
        if (r8 == 0) goto L_0x0062;
    L_0x0071:
        r8.close();	 Catch:{ all -> 0x006c }
        goto L_0x0062;
    L_0x0075:
        r0 = move-exception;
        r10 = r11;
        goto L_0x0066;
    L_0x0078:
        r9 = move-exception;
        goto L_0x005a;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.easyctrl.db.ModulePortManager.getNumber(com.easyctrl.ldy.domain.ModulePortBean, int):int");
    }

    private int getNumberSort(ArrayList<Integer> paixus) {
        int i = 0;
        while (i < paixus.size()) {
            try {
                if (paixus.size() == i + 1) {
                    return ((Integer) paixus.get(i)).intValue() + 1;
                }
                if (((Integer) paixus.get(i + 1)).intValue() - ((Integer) paixus.get(i)).intValue() != 1) {
                    return ((Integer) paixus.get(i)).intValue() + 1;
                }
                i++;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    public int update(ModulePortBean t, int id) {
        synchronized (LOCK) {
            openWriteDB();
            try {
                this.database.beginTransaction();
                String sql = " update " + Table.T_NAME + " set progress = ?,isOpen = ? where moduleID = ? and port = ? ";
                this.database.execSQL(sql, new Object[]{Integer.valueOf(t.progress), Integer.valueOf(t.isOpen), Integer.valueOf(t.moduleID), Integer.valueOf(t.port)});
                this.database.setTransactionSuccessful();
                this.database.endTransaction();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    public ArrayList<ModulePortBean> findAll() {
        ArrayList<ModulePortBean> beans;
        synchronized (LOCK) {
            openReadDB();
            beans = new ArrayList();
            Cursor cursor = getCursor(null, null);
            while (cursor.moveToNext()) {
                try {
                    beans.add(cursorToBean(cursor));
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
        }
        return beans;
    }

    public ContentValues beanToValues(ModulePortBean bean) {
        ContentValues values = new ContentValues();
        values.put(Table.FIELD_FLOOR, bean.floor);
        values.put(Table.FIELD_ID, Integer.valueOf(bean.id));
        values.put(Table.FIELD_ISOPEN, Integer.valueOf(bean.isOpen));
        values.put(Table.FIELD_MODULEID, Integer.valueOf(bean.moduleID));
        values.put(Table.FIELD_NANE, bean.name);
        values.put(Table.FIELD_DEVIVETYPE, bean.deviveType);
        values.put(Table.FIELD_PORT, Integer.valueOf(bean.port));
        values.put(Table.FIELD_PROGRESS, Integer.valueOf(bean.progress));
        values.put(Table.FIELD_ROOM, bean.room);
        values.put(Table.FIELD_TYPE, Integer.valueOf(bean.type));
        return values;
    }

    public ModulePortBean cursorToBean(Cursor cursor) {
        ModulePortBean bean = new ModulePortBean();
        bean.id = cursor.getInt(cursor.getColumnIndexOrThrow(Table.FIELD_ID));
        bean.floor = cursor.getString(cursor.getColumnIndexOrThrow(Table.FIELD_FLOOR));
        bean.isOpen = cursor.getInt(cursor.getColumnIndexOrThrow(Table.FIELD_ISOPEN));
        bean.moduleID = cursor.getInt(cursor.getColumnIndexOrThrow(Table.FIELD_MODULEID));
        bean.name = cursor.getString(cursor.getColumnIndexOrThrow(Table.FIELD_NANE));
        bean.deviveType = cursor.getString(cursor.getColumnIndexOrThrow(Table.FIELD_DEVIVETYPE));
        bean.port = cursor.getInt(cursor.getColumnIndexOrThrow(Table.FIELD_PORT));
        bean.progress = cursor.getInt(cursor.getColumnIndexOrThrow(Table.FIELD_PROGRESS));
        bean.room = cursor.getString(cursor.getColumnIndexOrThrow(Table.FIELD_ROOM));
        bean.type = cursor.getInt(cursor.getColumnIndexOrThrow(Table.FIELD_TYPE));
        bean.paixu = cursor.getInt(cursor.getColumnIndexOrThrow(Table.FIELD_PAIXU));
        return bean;
    }

    public Cursor getCursor(String where, String[] args) {
        return this.database.query(Table.T_NAME, null, where, args, null, null, null);
    }

    public ArrayList<ModulePortBean> findByModuleID(int moduleid) {
        ArrayList<ModulePortBean> moduleBeans;
        synchronized (LOCK) {
            openReadDB();
            Cursor cursor = null;
            moduleBeans = new ArrayList();
            try {
                cursor = getCursor(" moduleID = ? ", new String[]{String.valueOf(moduleid)});
                while (cursor.moveToNext()) {
                    moduleBeans.add(cursorToBean(cursor));
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
        return moduleBeans;
    }

    public ArrayList<ModulePortBean> findByModuleIDAndFloor(int moduleid, String floor, ArrayList<String> moduleIDs) {
        ArrayList<ModulePortBean> moduleBeans;
        synchronized (LOCK) {
            openReadDB();
            Cursor cursor = null;
            moduleBeans = new ArrayList();
            if (moduleid == -1) {
                try {
                    StringBuilder parms = new StringBuilder();
                    for (int i = 0; i < moduleIDs.size(); i++) {
                        if (((String) moduleIDs.get(i)).equals("\u5168\u90e8")) {
                            break;
                        }
                        parms.append(new StringBuilder(String.valueOf((String) moduleIDs.get(i))).append(",").toString());
                    }
                    parms.deleteCharAt(parms.length() - 1);
                    cursor = this.database.query(Table.T_NAME, null, " moduleID in( " + parms.toString() + " ) ", null, null, null, null);
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
            } else {
                cursor = getCursor(" moduleID = ? ", new String[]{String.valueOf(moduleid)});
            }
            while (cursor.moveToNext()) {
                moduleBeans.add(cursorToBean(cursor));
            }
            if (cursor != null) {
                cursor.close();
            }
        }
        return moduleBeans;
    }

    public ArrayList<String> findAllModuleID() {
        Exception e;
        Throwable th;
        openReadDB();
        Cursor cursor = null;
        ArrayList<String> arrayList = null;
        try {
            cursor = this.database.query(true, Table.T_NAME, new String[]{"moduleID"}, " floor = ? ", new String[]{"\u8bbe\u5907"}, null, null, null, null);
            if (cursor != null && cursor.getCount() > 0) {
                ArrayList<String> moduleids = new ArrayList();
                while (cursor.moveToNext()) {
                    try {
                        moduleids.add(String.valueOf(cursor.getInt(0)));
                    } catch (Exception e2) {
                        e = e2;
                        arrayList = moduleids;
                    } catch (Throwable th2) {
                        th = th2;
                        arrayList = moduleids;
                    }
                }
                arrayList = moduleids;
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
        return arrayList;
    }

    public void updatePosByModuleIDandPort(ModulePortBean bean) {
        openWriteDB();
        String sql = " update " + Table.T_NAME + " set floor = ?,room = ?,name = ?,deviveType=?,paixu=? where moduleID = ? and port = ? ";
        try {
            this.database.execSQL(sql, new Object[]{bean.floor, bean.room, bean.name, bean.deviveType, Integer.valueOf(bean.paixu), Integer.valueOf(bean.moduleID), Integer.valueOf(bean.port)});
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Cursor getCursorByString(String colums, String where, String[] args) {
        return this.database.query(true, Table.T_NAME, new String[]{colums}, where, args, null, null, null, null);
    }

    public ArrayList<ModulePortBean> onLinePortBeans(ArrayList<String> moduleIDs) {
        ArrayList<ModulePortBean> arrayList;
        Exception e;
        synchronized (LOCK) {
            arrayList = null;
            openReadDB();
            Cursor cursor = null;
            try {
                StringBuilder parms = new StringBuilder();
                for (int i = 0; i < moduleIDs.size(); i++) {
                    if (((String) moduleIDs.get(i)).equals("\u5168\u90e8")) {
                        break;
                    }
                    parms.append(new StringBuilder(String.valueOf((String) moduleIDs.get(i))).append(",").toString());
                }
                parms.deleteCharAt(parms.length() - 1);
                cursor = this.database.query(Table.T_NAME, null, " moduleID in( " + parms.toString() + " ) ", null, null, null, null);
                if (cursor != null && cursor.getCount() > 0) {
                    ArrayList<ModulePortBean> beans = new ArrayList();
                    while (cursor.moveToNext()) {
                        try {
                            beans.add(cursorToBean(cursor));
                        } catch (Exception e2) {
                            e = e2;
                            arrayList = beans;
                        } catch (Throwable th) {
                            Throwable th2 = th;
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
            }
        }
        return arrayList;
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

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.util.ArrayList<java.lang.String> findFloor(boolean r10, java.util.ArrayList<java.lang.String> r11) {
        /*
        r9 = this;
        r5 = 0;
        r6 = LOCK;
        monitor-enter(r6);
        r2 = new java.util.ArrayList;	 Catch:{ all -> 0x00a8 }
        r2.<init>();	 Catch:{ all -> 0x00a8 }
        r9.openReadDB();	 Catch:{ all -> 0x00a8 }
        r0 = 0;
        if (r10 != 0) goto L_0x0025;
    L_0x000f:
        r5 = " floor ";
        r7 = " floor not like '\u8bbe\u5907%' ";
        r8 = 0;
        r0 = r9.getCursorByString(r5, r7, r8);	 Catch:{ all -> 0x00a8 }
    L_0x0018:
        r5 = r0.moveToNext();	 Catch:{ Exception -> 0x009d }
        if (r5 != 0) goto L_0x0093;
    L_0x001e:
        if (r0 == 0) goto L_0x0023;
    L_0x0020:
        r0.close();	 Catch:{ all -> 0x00a8 }
    L_0x0023:
        monitor-exit(r6);	 Catch:{ all -> 0x00a8 }
    L_0x0024:
        return r2;
    L_0x0025:
        if (r11 == 0) goto L_0x002d;
    L_0x0027:
        r7 = r11.size();	 Catch:{ all -> 0x00a8 }
        if (r7 != 0) goto L_0x0030;
    L_0x002d:
        monitor-exit(r6);	 Catch:{ all -> 0x00a8 }
        r2 = r5;
        goto L_0x0024;
    L_0x0030:
        r4 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00a8 }
        r4.<init>();	 Catch:{ all -> 0x00a8 }
        r3 = 0;
    L_0x0036:
        r5 = r11.size();	 Catch:{ all -> 0x00a8 }
        if (r3 < r5) goto L_0x0066;
    L_0x003c:
        r5 = r4.length();	 Catch:{ all -> 0x00a8 }
        r5 = r5 + -1;
        r4.deleteCharAt(r5);	 Catch:{ all -> 0x00a8 }
        r5 = " floor ";
        r7 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00a8 }
        r8 = " moduleID in( ";
        r7.<init>(r8);	 Catch:{ all -> 0x00a8 }
        r8 = r4.toString();	 Catch:{ all -> 0x00a8 }
        r7 = r7.append(r8);	 Catch:{ all -> 0x00a8 }
        r8 = " ) ";
        r7 = r7.append(r8);	 Catch:{ all -> 0x00a8 }
        r7 = r7.toString();	 Catch:{ all -> 0x00a8 }
        r8 = 0;
        r0 = r9.getCursorByString(r5, r7, r8);	 Catch:{ all -> 0x00a8 }
        goto L_0x0018;
    L_0x0066:
        r5 = r11.get(r3);	 Catch:{ all -> 0x00a8 }
        r5 = (java.lang.String) r5;	 Catch:{ all -> 0x00a8 }
        r7 = "\u5168\u90e8";
        r5 = r5.equals(r7);	 Catch:{ all -> 0x00a8 }
        if (r5 != 0) goto L_0x003c;
    L_0x0074:
        r7 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00a8 }
        r5 = r11.get(r3);	 Catch:{ all -> 0x00a8 }
        r5 = (java.lang.String) r5;	 Catch:{ all -> 0x00a8 }
        r5 = java.lang.String.valueOf(r5);	 Catch:{ all -> 0x00a8 }
        r7.<init>(r5);	 Catch:{ all -> 0x00a8 }
        r5 = ",";
        r5 = r7.append(r5);	 Catch:{ all -> 0x00a8 }
        r5 = r5.toString();	 Catch:{ all -> 0x00a8 }
        r4.append(r5);	 Catch:{ all -> 0x00a8 }
        r3 = r3 + 1;
        goto L_0x0036;
    L_0x0093:
        r5 = 0;
        r5 = r0.getString(r5);	 Catch:{ Exception -> 0x009d }
        r2.add(r5);	 Catch:{ Exception -> 0x009d }
        goto L_0x0018;
    L_0x009d:
        r1 = move-exception;
        r1.printStackTrace();	 Catch:{ all -> 0x00ab }
        if (r0 == 0) goto L_0x0023;
    L_0x00a3:
        r0.close();	 Catch:{ all -> 0x00a8 }
        goto L_0x0023;
    L_0x00a8:
        r5 = move-exception;
        monitor-exit(r6);	 Catch:{ all -> 0x00a8 }
        throw r5;
    L_0x00ab:
        r5 = move-exception;
        if (r0 == 0) goto L_0x00b1;
    L_0x00ae:
        r0.close();	 Catch:{ all -> 0x00a8 }
    L_0x00b1:
        throw r5;	 Catch:{ all -> 0x00a8 }
        */
        throw new UnsupportedOperationException("Method not decompiled: com.easyctrl.db.ModulePortManager.findFloor(boolean, java.util.ArrayList):java.util.ArrayList<java.lang.String>");
    }

    public ArrayList<String> findRoom() {
        ArrayList<String> floors = new ArrayList();
        openReadDB();
        Cursor cursor = getCursorByString(" room ", null, null);
        while (cursor.moveToNext()) {
            try {
                floors.add(cursor.getString(0));
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
        return floors;
    }

    public ArrayList<String> findRoomByFloor(String floor) {
        ArrayList<String> rooms = new ArrayList();
        openReadDB();
        Cursor cursor = null;
        try {
            cursor = getCursorByString(" room ", " floor = ? ", new String[]{floor});
            while (cursor.moveToNext()) {
                rooms.add(cursor.getString(0));
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
        return rooms;
    }

    public ArrayList<String> findDevice() {
        ArrayList<String> floors = new ArrayList();
        openReadDB();
        Cursor cursor = getCursorByString(" name ", null, null);
        while (cursor.moveToNext()) {
            try {
                floors.add(cursor.getString(0));
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
        return floors;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.util.ArrayList<com.easyctrl.ldy.domain.ModulePortBean> findByFloorAndRoomAndDeivce(java.lang.String r18, java.lang.String r19, java.lang.String r20) {
        /*
        r17 = this;
        r16 = LOCK;
        monitor-enter(r16);
        r9 = new java.util.ArrayList;	 Catch:{ all -> 0x0057 }
        r9.<init>();	 Catch:{ all -> 0x0057 }
        r17.openReadDB();	 Catch:{ all -> 0x0057 }
        r10 = 0;
        r1 = com.easyctrl.ldy.activity.MainApplication.jsonManager;	 Catch:{ all -> 0x0057 }
        r13 = r1.getOnLineModuleID();	 Catch:{ all -> 0x0057 }
        if (r13 != 0) goto L_0x0017;
    L_0x0014:
        monitor-exit(r16);	 Catch:{ all -> 0x0057 }
        r9 = 0;
    L_0x0016:
        return r9;
    L_0x0017:
        r15 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0057 }
        r15.<init>();	 Catch:{ all -> 0x0057 }
        r12 = 0;
    L_0x001d:
        r1 = r13.size();	 Catch:{ all -> 0x0057 }
        if (r12 < r1) goto L_0x0032;
    L_0x0023:
        r14 = r15.toString();	 Catch:{ all -> 0x0057 }
        if (r14 == 0) goto L_0x002f;
    L_0x0029:
        r1 = r14.length();	 Catch:{ all -> 0x0057 }
        if (r1 != 0) goto L_0x005a;
    L_0x002f:
        monitor-exit(r16);	 Catch:{ all -> 0x0057 }
        r9 = 0;
        goto L_0x0016;
    L_0x0032:
        r1 = r13.get(r12);	 Catch:{ all -> 0x0057 }
        r1 = (java.lang.String) r1;	 Catch:{ all -> 0x0057 }
        r2 = "\u5168\u90e8";
        r1 = r1.equals(r2);	 Catch:{ all -> 0x0057 }
        if (r1 == 0) goto L_0x0048;
    L_0x0040:
        r1 = "";
        r15.append(r1);	 Catch:{ all -> 0x0057 }
    L_0x0045:
        r12 = r12 + 1;
        goto L_0x001d;
    L_0x0048:
        r1 = r13.get(r12);	 Catch:{ all -> 0x0057 }
        r1 = (java.lang.String) r1;	 Catch:{ all -> 0x0057 }
        r15.append(r1);	 Catch:{ all -> 0x0057 }
        r1 = ",";
        r15.append(r1);	 Catch:{ all -> 0x0057 }
        goto L_0x0045;
    L_0x0057:
        r1 = move-exception;
        monitor-exit(r16);	 Catch:{ all -> 0x0057 }
        throw r1;
    L_0x005a:
        r1 = 0;
        r2 = r14.length();	 Catch:{ all -> 0x0057 }
        r2 = r2 + -1;
        r14 = r14.substring(r1, r2);	 Catch:{ all -> 0x0057 }
        r1 = "\u5168\u90e8";
        r0 = r19;
        r1 = r0.equals(r1);	 Catch:{ Exception -> 0x017f }
        if (r1 == 0) goto L_0x00b6;
    L_0x006f:
        r1 = "\u5168\u90e8";
        r0 = r20;
        r1 = r0.equals(r1);	 Catch:{ Exception -> 0x017f }
        if (r1 == 0) goto L_0x00b6;
    L_0x0079:
        r0 = r17;
        r1 = r0.database;	 Catch:{ Exception -> 0x017f }
        r2 = com.easyctrl.ldy.domain.ModulePortBean.Table.T_NAME;	 Catch:{ Exception -> 0x017f }
        r3 = 0;
        r4 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x017f }
        r5 = " floor = ? and moduleID in(";
        r4.<init>(r5);	 Catch:{ Exception -> 0x017f }
        r4 = r4.append(r14);	 Catch:{ Exception -> 0x017f }
        r5 = ")";
        r4 = r4.append(r5);	 Catch:{ Exception -> 0x017f }
        r4 = r4.toString();	 Catch:{ Exception -> 0x017f }
        r5 = 1;
        r5 = new java.lang.String[r5];	 Catch:{ Exception -> 0x017f }
        r6 = 0;
        r5[r6] = r18;	 Catch:{ Exception -> 0x017f }
        r6 = 0;
        r7 = 0;
        r8 = 0;
        r10 = r1.query(r2, r3, r4, r5, r6, r7, r8);	 Catch:{ Exception -> 0x017f }
    L_0x00a2:
        r1 = r10.getCount();	 Catch:{ Exception -> 0x017f }
        if (r1 <= 0) goto L_0x00ae;
    L_0x00a8:
        r1 = r10.moveToNext();	 Catch:{ Exception -> 0x017f }
        if (r1 != 0) goto L_0x0174;
    L_0x00ae:
        if (r10 == 0) goto L_0x00b3;
    L_0x00b0:
        r10.close();	 Catch:{ all -> 0x0057 }
    L_0x00b3:
        monitor-exit(r16);	 Catch:{ all -> 0x0057 }
        goto L_0x0016;
    L_0x00b6:
        r1 = "\u5168\u90e8";
        r0 = r19;
        r1 = r0.equals(r1);	 Catch:{ Exception -> 0x017f }
        if (r1 != 0) goto L_0x00e4;
    L_0x00c0:
        r1 = "\u5168\u90e8";
        r0 = r20;
        r1 = r0.equals(r1);	 Catch:{ Exception -> 0x017f }
        if (r1 == 0) goto L_0x00e4;
    L_0x00ca:
        r0 = r17;
        r1 = r0.database;	 Catch:{ Exception -> 0x017f }
        r2 = com.easyctrl.ldy.domain.ModulePortBean.Table.T_NAME;	 Catch:{ Exception -> 0x017f }
        r3 = 0;
        r4 = " floor = ? and room = ? ";
        r5 = 2;
        r5 = new java.lang.String[r5];	 Catch:{ Exception -> 0x017f }
        r6 = 0;
        r5[r6] = r18;	 Catch:{ Exception -> 0x017f }
        r6 = 1;
        r5[r6] = r19;	 Catch:{ Exception -> 0x017f }
        r6 = 0;
        r7 = 0;
        r8 = 0;
        r10 = r1.query(r2, r3, r4, r5, r6, r7, r8);	 Catch:{ Exception -> 0x017f }
        goto L_0x00a2;
    L_0x00e4:
        r1 = "\u5168\u90e8";
        r0 = r19;
        r1 = r0.equals(r1);	 Catch:{ Exception -> 0x017f }
        if (r1 == 0) goto L_0x0126;
    L_0x00ee:
        r1 = "\u5168\u90e8";
        r0 = r20;
        r1 = r0.equals(r1);	 Catch:{ Exception -> 0x017f }
        if (r1 != 0) goto L_0x0126;
    L_0x00f8:
        r0 = r17;
        r1 = r0.database;	 Catch:{ Exception -> 0x017f }
        r2 = com.easyctrl.ldy.domain.ModulePortBean.Table.T_NAME;	 Catch:{ Exception -> 0x017f }
        r3 = 0;
        r4 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x017f }
        r5 = " floor = ? and deviveType = ? and moduleID in(";
        r4.<init>(r5);	 Catch:{ Exception -> 0x017f }
        r4 = r4.append(r14);	 Catch:{ Exception -> 0x017f }
        r5 = ")";
        r4 = r4.append(r5);	 Catch:{ Exception -> 0x017f }
        r4 = r4.toString();	 Catch:{ Exception -> 0x017f }
        r5 = 2;
        r5 = new java.lang.String[r5];	 Catch:{ Exception -> 0x017f }
        r6 = 0;
        r5[r6] = r18;	 Catch:{ Exception -> 0x017f }
        r6 = 1;
        r5[r6] = r20;	 Catch:{ Exception -> 0x017f }
        r6 = 0;
        r7 = 0;
        r8 = 0;
        r10 = r1.query(r2, r3, r4, r5, r6, r7, r8);	 Catch:{ Exception -> 0x017f }
        goto L_0x00a2;
    L_0x0126:
        r1 = "\u8bbe\u5907";
        r0 = r18;
        r1 = r0.equals(r1);	 Catch:{ Exception -> 0x017f }
        if (r1 == 0) goto L_0x014b;
    L_0x0130:
        r0 = r17;
        r1 = r0.database;	 Catch:{ Exception -> 0x017f }
        r2 = com.easyctrl.ldy.domain.ModulePortBean.Table.T_NAME;	 Catch:{ Exception -> 0x017f }
        r3 = 0;
        r4 = " floor = ? and moduleID = ? ";
        r5 = 2;
        r5 = new java.lang.String[r5];	 Catch:{ Exception -> 0x017f }
        r6 = 0;
        r5[r6] = r18;	 Catch:{ Exception -> 0x017f }
        r6 = 1;
        r5[r6] = r19;	 Catch:{ Exception -> 0x017f }
        r6 = 0;
        r7 = 0;
        r8 = 0;
        r10 = r1.query(r2, r3, r4, r5, r6, r7, r8);	 Catch:{ Exception -> 0x017f }
        goto L_0x00a2;
    L_0x014b:
        r1 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x017f }
        r2 = " floor = ? and room = ? and deviveType = ? and moduleID in(";
        r1.<init>(r2);	 Catch:{ Exception -> 0x017f }
        r1 = r1.append(r14);	 Catch:{ Exception -> 0x017f }
        r2 = ")";
        r1 = r1.append(r2);	 Catch:{ Exception -> 0x017f }
        r1 = r1.toString();	 Catch:{ Exception -> 0x017f }
        r2 = 3;
        r2 = new java.lang.String[r2];	 Catch:{ Exception -> 0x017f }
        r3 = 0;
        r2[r3] = r18;	 Catch:{ Exception -> 0x017f }
        r3 = 1;
        r2[r3] = r19;	 Catch:{ Exception -> 0x017f }
        r3 = 2;
        r2[r3] = r20;	 Catch:{ Exception -> 0x017f }
        r0 = r17;
        r10 = r0.getCursor(r1, r2);	 Catch:{ Exception -> 0x017f }
        goto L_0x00a2;
    L_0x0174:
        r0 = r17;
        r1 = r0.cursorToBean(r10);	 Catch:{ Exception -> 0x017f }
        r9.add(r1);	 Catch:{ Exception -> 0x017f }
        goto L_0x00a8;
    L_0x017f:
        r11 = move-exception;
        r11.printStackTrace();	 Catch:{ all -> 0x018a }
        if (r10 == 0) goto L_0x00b3;
    L_0x0185:
        r10.close();	 Catch:{ all -> 0x0057 }
        goto L_0x00b3;
    L_0x018a:
        r1 = move-exception;
        if (r10 == 0) goto L_0x0190;
    L_0x018d:
        r10.close();	 Catch:{ all -> 0x0057 }
    L_0x0190:
        throw r1;	 Catch:{ all -> 0x0057 }
        */
        throw new UnsupportedOperationException("Method not decompiled: com.easyctrl.db.ModulePortManager.findByFloorAndRoomAndDeivce(java.lang.String, java.lang.String, java.lang.String):java.util.ArrayList<com.easyctrl.ldy.domain.ModulePortBean>");
    }

    public HashMap<Integer, ArrayList<ModulePortBean>> findHashMap(int moduleid) {
        HashMap<Integer, ArrayList<ModulePortBean>> beans = new HashMap();
        Cursor cursor = getCursor(" moduleID = ? ", new String[]{String.valueOf(moduleid)});
        ArrayList<ModulePortBean> moduleBeans = new ArrayList();
        while (cursor.moveToNext()) {
            try {
                moduleBeans.add(cursorToBean(cursor));
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
        if (moduleBeans.size() > 0) {
            beans.put(Integer.valueOf(moduleid), moduleBeans);
        }
        if (cursor != null) {
            cursor.close();
        }
        return beans;
    }

    public int isUseName(String keyName, String where, String[] name) {
        openReadDB();
        Cursor cursor = null;
        try {
            cursor = this.database.query(Table.T_NAME, new String[]{keyName}, where, name, null, null, null);
            if (cursor.getCount() > 0) {
                if (cursor != null) {
                    cursor.close();
                }
                return 1;
            }
            if (cursor != null) {
                cursor.close();
            }
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            if (cursor != null) {
                cursor.close();
            }
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
            throw th;
        }
    }

    public void updateFloor(String before, String where) {
        openWriteDB();
        String sql = "update " + Table.T_NAME + " set floor = ? where floor =? ";
        try {
            this.database.execSQL(sql, new Object[]{before, where});
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int singleSelect(ModulePortBean bean) {
        if (this.database == null || !this.database.isOpen()) {
            openReadDB();
        }
        Cursor cursor = null;
        try {
            cursor = this.database.query(Table.T_NAME, null, " floor=? and room = ? and name = ? ", new String[]{bean.floor, bean.room, bean.name}, null, null, null);
            if (cursor != null) {
                int count = cursor.getCount();
                if (cursor == null) {
                    return count;
                }
                cursor.close();
                return count;
            }
            if (cursor != null) {
                cursor.close();
            }
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            if (cursor != null) {
                cursor.close();
            }
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
            throw th;
        }
    }

    public void updateRoom(String before, String where) {
        openWriteDB();
        String sql = " update " + Table.T_NAME + " set room = ? where room =? ";
        try {
            this.database.execSQL(sql, new Object[]{before, where});
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
