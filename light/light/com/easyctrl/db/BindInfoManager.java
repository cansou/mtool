package com.easyctrl.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import com.easyctrl.iface.BaseDBImpl;
import com.easyctrl.ldy.domain.BindInfo;
import com.easyctrl.ldy.domain.BindInfo.Table;
import java.util.ArrayList;

public class BindInfoManager extends DataBaseManage implements BaseDBImpl<BindInfo> {
    private static BindInfoManager infoManager;

    private BindInfoManager(Context context) {
        super(context);
    }

    public static BindInfoManager getInstance(Context context) {
        if (infoManager == null) {
            infoManager = new BindInfoManager(context);
        }
        return infoManager;
    }

    public void deleteAll() {
        synchronized (LOCK) {
            openWriteDB();
            this.database.delete(Table.T_NAME, null, null);
            this.database.execSQL(" DELETE FROM sqlite_sequence ");
        }
    }

    public void add(BindInfo t) {
        synchronized (LOCK) {
            openWriteDB();
            try {
                this.database.insert(Table.T_NAME, null, beanToValues(t));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void addOrUpdate(BindInfo t) {
        synchronized (LOCK) {
            Cursor cursor = null;
            try {
                openWriteDB();
                cursor = this.database.query(Table.T_NAME, null, " bind_id = ? ", new String[]{String.valueOf(t.bind_id)}, null, null, null);
                if (cursor.getCount() > 0) {
                    this.database.update(Table.T_NAME, beanToValues(t), " bind_id = ? ", new String[]{String.valueOf(t.bind_id)});
                } else {
                    this.database.insert(Table.T_NAME, null, beanToValues(t));
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
        int row;
        synchronized (LOCK) {
            openWriteDB();
            row = 0;
            try {
                row = this.database.delete(Table.T_NAME, " bindInfo_id= ? ", new String[]{String.valueOf(id)});
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return row;
    }

    public int deleteByBindID(int bindId) {
        int row;
        synchronized (LOCK) {
            openWriteDB();
            row = 0;
            try {
                row = this.database.delete(Table.T_NAME, " bind_id= ? ", new String[]{String.valueOf(bindId)});
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return row;
    }

    public int update(BindInfo t, int id) {
        int row;
        synchronized (LOCK) {
            openWriteDB();
            row = this.database.update(Table.T_NAME, beanToValues(t), " bindInfo_id = ? ", new String[]{String.valueOf(id)});
        }
        return row;
    }

    public BindInfo findByTimeID(int timeID) {
        BindInfo bindInfo;
        synchronized (LOCK) {
            openWriteDB();
            Cursor cursor = this.database.query(Table.T_NAME, null, " bind_id= ? ", new String[]{String.valueOf(timeID)}, null, null, null);
            cursor.moveToNext();
            bindInfo = cursorToBean(cursor);
        }
        return bindInfo;
    }

    public ArrayList<BindInfo> findAll() {
        return null;
    }

    public ContentValues beanToValues(BindInfo bean) {
        ContentValues values = new ContentValues();
        values.put(Table.FIELD_BINDINFO_KEY, Integer.valueOf(bean.bindInfo_key));
        values.put(Table.FIELD_BINDINFO_NAME, bean.bindInfo_name);
        values.put(Table.FIELD_BINDINFO_TYPE, Integer.valueOf(bean.bindInfo_type));
        values.put(Table.FIELD_MODULEID, Integer.valueOf(bean.bindInfo_moduleID));
        values.put(Table.FIELD_INFO_BIND_ID, Integer.valueOf(bean.bind_id));
        values.put(Table.FIELD_FLOOR, bean.floor);
        values.put(Table.FIELD_ROOM, bean.room);
        values.put(Table.FIELD_DEVICE, bean.device);
        values.put(Table.FIELD_BIND_MODULE_J_ID, Integer.valueOf(bean.bind_module_J_id));
        values.put(Table.FIELD_BINDPORT, Integer.valueOf(bean.bind_bindport));
        values.put(Table.FIELD_TYPE, Integer.valueOf(bean.bind_info_type));
        values.put(Table.FIELD_BIND_INFO_SCENEID, Integer.valueOf(bean.bind_info_sceneID));
        return values;
    }

    private int getColumn(Cursor cursor, String field) {
        return cursor.getColumnIndexOrThrow(field);
    }

    public BindInfo cursorToBean(Cursor cursor) {
        BindInfo bindInfo = new BindInfo();
        bindInfo.bindInfo_id = cursor.getInt(getColumn(cursor, Table.FIELD_BIND_ID));
        bindInfo.bind_id = cursor.getInt(getColumn(cursor, Table.FIELD_INFO_BIND_ID));
        bindInfo.bindInfo_name = cursor.getString(getColumn(cursor, Table.FIELD_BINDINFO_NAME));
        bindInfo.bindInfo_type = cursor.getInt(getColumn(cursor, Table.FIELD_BINDINFO_TYPE));
        bindInfo.bindInfo_key = cursor.getInt(getColumn(cursor, Table.FIELD_BINDINFO_KEY));
        bindInfo.floor = cursor.getString(getColumn(cursor, Table.FIELD_FLOOR));
        bindInfo.room = cursor.getString(getColumn(cursor, Table.FIELD_ROOM));
        bindInfo.device = cursor.getString(getColumn(cursor, Table.FIELD_DEVICE));
        bindInfo.bind_bindport = cursor.getInt(getColumn(cursor, Table.FIELD_BINDPORT));
        bindInfo.bind_module_J_id = cursor.getInt(getColumn(cursor, Table.FIELD_BIND_MODULE_J_ID));
        bindInfo.bind_info_type = cursor.getInt(getColumn(cursor, Table.FIELD_TYPE));
        bindInfo.bind_info_sceneID = cursor.getInt(getColumn(cursor, Table.FIELD_BIND_INFO_SCENEID));
        return bindInfo;
    }

    public Cursor getCursor(String where, String[] args) {
        return null;
    }

    public void updateInfoByModulePort(BindInfo bindInfo) {
        synchronized (LOCK) {
            openWriteDB();
            try {
                ContentValues values = new ContentValues();
                values.put(Table.FIELD_FLOOR, bindInfo.floor);
                values.put(Table.FIELD_ROOM, bindInfo.room);
                values.put(Table.FIELD_DEVICE, bindInfo.device);
                this.database.update(Table.T_NAME, values, " bind_module_J_id = ? and bind_bindport = ? ", new String[]{String.valueOf(bindInfo.bind_module_J_id), String.valueOf(bindInfo.bind_bindport)});
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
