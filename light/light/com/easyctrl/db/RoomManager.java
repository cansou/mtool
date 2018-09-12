package com.easyctrl.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import com.easyctrl.iface.BaseDBImpl;
import com.easyctrl.ldy.domain.FloorBean;
import com.easyctrl.ldy.domain.RoomBean;
import com.easyctrl.ldy.domain.RoomBean.Table;
import com.easyctrl.ldy.domain.VirtualBean;
import java.util.ArrayList;

public class RoomManager extends DataBaseManage implements BaseDBImpl<RoomBean> {
    private static RoomManager instance;
    private FloorManager floorManager;
    private ModulePortManager modulePortManager;

    private RoomManager(Context context) {
        super(context);
        openWriteDB();
        this.modulePortManager = ModulePortManager.getInstance(context);
        this.floorManager = FloorManager.getInstance(context);
    }

    public static synchronized RoomManager getInstance(Context context) {
        RoomManager roomManager;
        synchronized (RoomManager.class) {
            if (instance == null) {
                instance = new RoomManager(context);
            }
            roomManager = instance;
        }
        return roomManager;
    }

    public void deleteAll() {
        synchronized (LOCK) {
            openWriteDB();
            this.database.delete(Table.T_NAME, null, null);
            this.database.execSQL(" DELETE FROM sqlite_sequence ");
        }
    }

    public void add(RoomBean t) {
        String sql = " insert into " + Table.T_NAME + "(name,floorID,paixu,sname) values(?,?,?,?)";
        try {
            if (findByName(t.name, t.floorID) == null) {
                openWriteDB();
                this.database.execSQL(sql, new Object[]{t.name, String.valueOf(t.floorID), Integer.valueOf(t.paixu), t.sname});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
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

    public RoomBean findByName(String name, int floorID) {
        Exception e;
        Throwable th;
        openReadDB();
        Cursor cursor = getCursor(" name = ? and floorID = ? ", new String[]{name, String.valueOf(floorID)});
        RoomBean bean = null;
        try {
            if (cursor.getCount() == 0) {
                if (cursor != null) {
                    cursor.close();
                }
                return null;
            }
            RoomBean bean2 = new RoomBean();
            try {
                cursor.moveToNext();
                bean = cursorToBean(cursor);
                if (cursor != null) {
                    cursor.close();
                }
            } catch (Exception e2) {
                e = e2;
                bean = bean2;
                try {
                    e.printStackTrace();
                    if (cursor != null) {
                        cursor.close();
                    }
                    return bean;
                } catch (Throwable th2) {
                    th = th2;
                    if (cursor != null) {
                        cursor.close();
                    }
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                bean = bean2;
                if (cursor != null) {
                    cursor.close();
                }
                throw th;
            }
            return bean;
        } catch (Exception e3) {
            e = e3;
            e.printStackTrace();
            if (cursor != null) {
                cursor.close();
            }
            return bean;
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.easyctrl.ldy.domain.RoomBean findById(int r10, java.lang.String r11) {
        /*
        r9 = this;
        r5 = LOCK;
        monitor-enter(r5);
        r9.openReadDB();	 Catch:{ all -> 0x0043 }
        r4 = " id = ? ";
        r6 = 1;
        r6 = new java.lang.String[r6];	 Catch:{ all -> 0x0043 }
        r7 = 0;
        r8 = java.lang.String.valueOf(r10);	 Catch:{ all -> 0x0043 }
        r6[r7] = r8;	 Catch:{ all -> 0x0043 }
        r2 = r9.getCursor(r4, r6);	 Catch:{ all -> 0x0043 }
        r0 = 0;
        r4 = r2.getCount();	 Catch:{ Exception -> 0x0039 }
        if (r4 != 0) goto L_0x0025;
    L_0x001d:
        if (r2 == 0) goto L_0x0022;
    L_0x001f:
        r2.close();	 Catch:{ all -> 0x0043 }
    L_0x0022:
        monitor-exit(r5);	 Catch:{ all -> 0x0043 }
        r4 = 0;
    L_0x0024:
        return r4;
    L_0x0025:
        r1 = new com.easyctrl.ldy.domain.RoomBean;	 Catch:{ Exception -> 0x0039 }
        r1.<init>();	 Catch:{ Exception -> 0x0039 }
        r2.moveToNext();	 Catch:{ Exception -> 0x0050, all -> 0x004d }
        r0 = r9.cursorToBean(r2);	 Catch:{ Exception -> 0x0050, all -> 0x004d }
        if (r2 == 0) goto L_0x0036;
    L_0x0033:
        r2.close();	 Catch:{ all -> 0x0043 }
    L_0x0036:
        monitor-exit(r5);	 Catch:{ all -> 0x0043 }
        r4 = r0;
        goto L_0x0024;
    L_0x0039:
        r3 = move-exception;
    L_0x003a:
        r3.printStackTrace();	 Catch:{ all -> 0x0046 }
        if (r2 == 0) goto L_0x0036;
    L_0x003f:
        r2.close();	 Catch:{ all -> 0x0043 }
        goto L_0x0036;
    L_0x0043:
        r4 = move-exception;
        monitor-exit(r5);	 Catch:{ all -> 0x0043 }
        throw r4;
    L_0x0046:
        r4 = move-exception;
    L_0x0047:
        if (r2 == 0) goto L_0x004c;
    L_0x0049:
        r2.close();	 Catch:{ all -> 0x0043 }
    L_0x004c:
        throw r4;	 Catch:{ all -> 0x0043 }
    L_0x004d:
        r4 = move-exception;
        r0 = r1;
        goto L_0x0047;
    L_0x0050:
        r3 = move-exception;
        r0 = r1;
        goto L_0x003a;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.easyctrl.db.RoomManager.findById(int, java.lang.String):com.easyctrl.ldy.domain.RoomBean");
    }

    public int update(RoomBean t, int id) {
        openWriteDB();
        int col = 0;
        try {
            col = this.database.update(Table.T_NAME, beanToValues(t), " id = ? ", new String[]{String.valueOf(id)});
        } catch (Exception e) {
            e.printStackTrace();
        }
        return col;
    }

    public ContentValues beanToValues(RoomBean bean) {
        ContentValues values = new ContentValues();
        values.put(Table.NAME, bean.name);
        values.put(Table.SNAME, bean.sname);
        return values;
    }

    public ArrayList<RoomBean> findAll() {
        openReadDB();
        ArrayList<RoomBean> beans = new ArrayList();
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
        return beans;
    }

    public RoomBean cursorToBean(Cursor cursor) {
        RoomBean bean = new RoomBean();
        bean.id = cursor.getInt(cursor.getColumnIndexOrThrow(Table.ID));
        bean.name = cursor.getString(cursor.getColumnIndexOrThrow(Table.NAME));
        bean.floorID = cursor.getInt(cursor.getColumnIndexOrThrow(Table.FLOORID));
        return bean;
    }

    public Cursor getCursor(String where, String[] args) {
        Cursor cursor = null;
        try {
            cursor = this.database.query(Table.T_NAME, null, where, args, null, null, "paixu asc");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cursor;
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

    public ArrayList<String> findByFloorIDAndip(int id) {
        openReadDB();
        ArrayList<String> beans = new ArrayList();
        Cursor cursor = getCursor(" floorID = ? ", new String[]{String.valueOf(id)});
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

    public ArrayList<RoomBean> findByFloorBeanID(int id) {
        openReadDB();
        ArrayList<RoomBean> beans = new ArrayList();
        Cursor cursor = getCursor(" floorID = ? ", new String[]{String.valueOf(id)});
        while (cursor.moveToNext()) {
            try {
                RoomBean bean = cursorToBean(cursor);
                FloorBean floorBean = this.floorManager.findByID(bean.floorID);
                bean.isUse = this.modulePortManager.isUseName(VirtualBean.Table.FIELD_ROOM, " room = ? and floor = ? ", new String[]{bean.name, floorBean.name});
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

    public int getNumber(String roomName, int number, int floorID) {
        openReadDB();
        Cursor cursor = this.database.query(Table.T_NAME, new String[]{Table.PAIXU}, " sname= ? and paixu= ? and floorID = ?  ", new String[]{roomName, String.valueOf(number), String.valueOf(floorID)}, null, null, null);
        int resultNum = number;
        if (cursor.getCount() > 0) {
            resultNum++;
            cursor.close();
            return getNumber(roomName, resultNum, floorID);
        }
        cursor.close();
        return resultNum;
    }
}
