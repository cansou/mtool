package com.easyctrl.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import com.easyctrl.iface.BaseDBImpl;
import com.easyctrl.ldy.domain.UserScene;
import com.easyctrl.ldy.domain.UserScene.Table;
import java.util.ArrayList;

public class UserSceneManager extends DataBaseManage implements BaseDBImpl<UserScene> {
    private static UserSceneManager instance;

    public static UserSceneManager getInstance(Context context) {
        if (instance == null) {
            instance = new UserSceneManager(context);
        }
        return instance;
    }

    private UserSceneManager(Context context) {
        super(context);
    }

    public void deleteAll() {
        synchronized (LOCK) {
            openWriteDB();
            this.database.delete(Table.T_NAME, null, null);
            this.database.execSQL(" DELETE FROM sqlite_sequence ");
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void batchSave(java.util.ArrayList<com.easyctrl.ldy.domain.UserScene> r14) {
        /*
        r13 = this;
        r12 = LOCK;
        monitor-enter(r12);
        r13.openWriteDB();	 Catch:{ all -> 0x005f }
        r0 = 0;
        r11 = r14.get(r0);	 Catch:{ all -> 0x005f }
        r11 = (com.easyctrl.ldy.domain.UserScene) r11;	 Catch:{ all -> 0x005f }
        r8 = 0;
        r0 = r13.database;	 Catch:{ Exception -> 0x003e }
        r1 = "easy_user_scene";
        r2 = 0;
        r3 = " floorID = ? and roomID = ? ";
        r4 = 2;
        r4 = new java.lang.String[r4];	 Catch:{ Exception -> 0x003e }
        r5 = 0;
        r6 = r11.floorID;	 Catch:{ Exception -> 0x003e }
        r6 = java.lang.String.valueOf(r6);	 Catch:{ Exception -> 0x003e }
        r4[r5] = r6;	 Catch:{ Exception -> 0x003e }
        r5 = 1;
        r6 = r11.roomID;	 Catch:{ Exception -> 0x003e }
        r6 = java.lang.String.valueOf(r6);	 Catch:{ Exception -> 0x003e }
        r4[r5] = r6;	 Catch:{ Exception -> 0x003e }
        r5 = 0;
        r6 = 0;
        r7 = 0;
        r8 = r0.query(r1, r2, r3, r4, r5, r6, r7);	 Catch:{ Exception -> 0x003e }
        r0 = r8.getCount();	 Catch:{ Exception -> 0x003e }
        if (r0 <= 0) goto L_0x0069;
    L_0x0037:
        if (r8 == 0) goto L_0x003c;
    L_0x0039:
        r8.close();	 Catch:{ all -> 0x005f }
    L_0x003c:
        monitor-exit(r12);	 Catch:{ all -> 0x005f }
    L_0x003d:
        return;
    L_0x003e:
        r9 = move-exception;
        r9.printStackTrace();	 Catch:{ all -> 0x0062 }
        if (r8 == 0) goto L_0x0047;
    L_0x0044:
        r8.close();	 Catch:{ all -> 0x005f }
    L_0x0047:
        r0 = r13.database;	 Catch:{ all -> 0x005f }
        r0.beginTransaction();	 Catch:{ all -> 0x005f }
        r10 = 0;
    L_0x004d:
        r0 = r14.size();	 Catch:{ all -> 0x005f }
        if (r10 < r0) goto L_0x006f;
    L_0x0053:
        r0 = r13.database;	 Catch:{ all -> 0x005f }
        r0.setTransactionSuccessful();	 Catch:{ all -> 0x005f }
        r0 = r13.database;	 Catch:{ all -> 0x005f }
        r0.endTransaction();	 Catch:{ all -> 0x005f }
        monitor-exit(r12);	 Catch:{ all -> 0x005f }
        goto L_0x003d;
    L_0x005f:
        r0 = move-exception;
        monitor-exit(r12);	 Catch:{ all -> 0x005f }
        throw r0;
    L_0x0062:
        r0 = move-exception;
        if (r8 == 0) goto L_0x0068;
    L_0x0065:
        r8.close();	 Catch:{ all -> 0x005f }
    L_0x0068:
        throw r0;	 Catch:{ all -> 0x005f }
    L_0x0069:
        if (r8 == 0) goto L_0x0047;
    L_0x006b:
        r8.close();	 Catch:{ all -> 0x005f }
        goto L_0x0047;
    L_0x006f:
        r1 = r13.database;	 Catch:{ all -> 0x005f }
        r2 = "easy_user_scene";
        r3 = 0;
        r0 = r14.get(r10);	 Catch:{ all -> 0x005f }
        r0 = (com.easyctrl.ldy.domain.UserScene) r0;	 Catch:{ all -> 0x005f }
        r0 = r13.beanToValues(r0);	 Catch:{ all -> 0x005f }
        r1.insert(r2, r3, r0);	 Catch:{ all -> 0x005f }
        r10 = r10 + 1;
        goto L_0x004d;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.easyctrl.db.UserSceneManager.batchSave(java.util.ArrayList):void");
    }

    public ArrayList<UserScene> findByFloorIdAndRoomID(int floor, int room) {
        ArrayList<UserScene> arrayUser;
        synchronized (LOCK) {
            openReadDB();
            arrayUser = new ArrayList();
            Cursor cursor = null;
            try {
                cursor = this.database.rawQuery("select a.userSceneID,a.name,a.pbindID,a.vbindID,a.sbindType,b.keyValue,  b.bind_moduleID ,c.key from easy_user_scene a  left join easy_bind b on a.pbindID = b.bindID  left join easy_virtual_key c on a.vbindID = c.virtualID  where floorID = ? and roomID = ? ", new String[]{String.valueOf(floor), String.valueOf(room)});
                while (cursor.moveToNext()) {
                    arrayUser.add(cursorToBean(cursor));
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
        return arrayUser;
    }

    public void add(UserScene t) {
    }

    public int delete(int id) {
        return 0;
    }

    public int update(UserScene t, int id) {
        int update;
        synchronized (LOCK) {
            openWriteDB();
            update = this.database.update(Table.T_NAME, updateValues(t), " userSceneID = ? ", new String[]{String.valueOf(t.userSceneID)});
        }
        return update;
    }

    public void updateName(String newName, int id) {
        synchronized (LOCK) {
            openWriteDB();
            this.database.execSQL(" update easy_user_scene set name = ? where userSceneID = ? ", new String[]{newName, String.valueOf(id)});
        }
    }

    private ContentValues updateValues(UserScene t) {
        ContentValues values = new ContentValues();
        values.put(Table.FIELD_SBINDTYPE, Integer.valueOf(t.sbindType));
        values.put(Table.FIELD_VBINDID, Integer.valueOf(t.vbindID));
        values.put(Table.FIELD_PBINDID, Integer.valueOf(t.pbindID));
        return values;
    }

    public ArrayList<UserScene> findAll() {
        return null;
    }

    public ContentValues beanToValues(UserScene bean) {
        ContentValues values = new ContentValues();
        values.put(Table.FIELD_FLOOR, Integer.valueOf(bean.floorID));
        values.put(Table.FIELD_ROOM, Integer.valueOf(bean.roomID));
        values.put(Table.FIELD_SBINDTYPE, Integer.valueOf(-1));
        return values;
    }

    public UserScene cursorToBean(Cursor cursor) {
        UserScene scene = new UserScene();
        scene.userSceneID = cursor.getInt(getColunmName(cursor, Table.FIELD_USERSCENEID));
        scene.sbindType = cursor.getInt(getColunmName(cursor, Table.FIELD_SBINDTYPE));
        scene.name = cursor.getString(getColunmName(cursor, "name"));
        scene.panelID = cursor.getInt(getColunmName(cursor, "bind_moduleID"));
        scene.panelkey = cursor.getInt(getColunmName(cursor, "keyValue"));
        scene.key = cursor.getInt(getColunmName(cursor, "key"));
        scene.pbindID = cursor.getInt(getColunmName(cursor, Table.FIELD_PBINDID));
        scene.vbindID = cursor.getInt(getColunmName(cursor, Table.FIELD_VBINDID));
        return scene;
    }

    private int getColunmName(Cursor cursor, String colunmName) {
        return cursor.getColumnIndexOrThrow(colunmName);
    }

    public Cursor getCursor(String where, String[] args) {
        return null;
    }
}
