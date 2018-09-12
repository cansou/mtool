package com.easyctrl.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import com.easyctrl.iface.BaseDBImpl;
import com.easyctrl.ldy.domain.DeviceBean;
import com.easyctrl.ldy.domain.DeviceBean.Table;
import java.util.ArrayList;

public class DeviceManager extends DataBaseManage implements BaseDBImpl<DeviceBean> {
    private static DeviceManager instance;

    private DeviceManager(Context context) {
        super(context);
    }

    public static synchronized DeviceManager getInstance(Context context) {
        DeviceManager deviceManager;
        synchronized (DeviceManager.class) {
            if (instance == null) {
                instance = new DeviceManager(context);
            }
            deviceManager = instance;
        }
        return deviceManager;
    }

    public void deleteAll() {
        synchronized (LOCK) {
            openWriteDB();
            this.database.delete(Table.T_NAME, null, null);
            this.database.execSQL(" DELETE FROM sqlite_sequence ");
        }
    }

    public void add(DeviceBean t) {
        openWriteDB();
        String sql = " insert into " + Table.T_NAME + " (device_name,device_type,device_isSystem) values(?,?,?) ";
        try {
            this.database.execSQL(sql, new Object[]{t.device_name, t.device_type, Integer.valueOf(t.device_isSystem)});
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int delete(int id) {
        openWriteDB();
        int colunms = 0;
        try {
            colunms = this.database.delete(Table.T_NAME, " deviceID = ? ", new String[]{String.valueOf(id)});
        } catch (Exception e) {
            e.printStackTrace();
        }
        return colunms;
    }

    public int update(DeviceBean t, int id) {
        openWriteDB();
        try {
            this.database.update(Table.T_NAME, beanToValues(t), " deviceID = ? ", new String[]{String.valueOf(id)});
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public ArrayList<DeviceBean> findAll() {
        openReadDB();
        ArrayList<DeviceBean> beans = new ArrayList();
        Cursor cursor = null;
        try {
            cursor = getCursor(null, null);
            while (cursor.moveToNext()) {
                beans.add(cursorToBean(cursor));
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
        return beans;
    }

    public ArrayList<DeviceBean> findByTypeToObj(String type) {
        ArrayList<DeviceBean> beans;
        synchronized (LOCK) {
            openReadDB();
            beans = new ArrayList();
            Cursor cursor = null;
            try {
                cursor = getCursor(" device_type = ? ", new String[]{type});
                while (cursor.moveToNext()) {
                    beans.add(cursorToBean(cursor));
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
        return beans;
    }

    public ContentValues beanToValues(DeviceBean bean) {
        ContentValues values = new ContentValues();
        values.put(Table.NAME, bean.device_name);
        values.put(Table.TYPE, bean.device_type);
        return values;
    }

    public DeviceBean cursorToBean(Cursor cursor) {
        DeviceBean bean = new DeviceBean();
        bean.device_name = cursor.getString(cursor.getColumnIndexOrThrow(Table.NAME));
        bean.deviceID = cursor.getInt(cursor.getColumnIndexOrThrow(Table.ID));
        bean.device_type = cursor.getString(cursor.getColumnIndexOrThrow(Table.TYPE));
        bean.device_isSystem = cursor.getInt(cursor.getColumnIndexOrThrow(Table.ISSYSTEM));
        return bean;
    }

    public Cursor getCursor(String where, String[] args) {
        return this.database.query(Table.T_NAME, null, where, args, null, null, null);
    }

    public ArrayList<String> findAllType() {
        ArrayList<String> beans;
        synchronized (LOCK) {
            beans = new ArrayList();
            openReadDB();
            Cursor cursor = null;
            try {
                cursor = this.database.query(true, Table.T_NAME, new String[]{"device_type"}, null, null, null, null, null, null);
                while (cursor.moveToNext()) {
                    beans.add(cursor.getString(0));
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
        return beans;
    }

    public ArrayList<String> findAllName() {
        openReadDB();
        ArrayList<String> beans = new ArrayList();
        Cursor cursor = null;
        try {
            cursor = getCursor(null, null);
            while (cursor.moveToNext()) {
                beans.add(cursorToBean(cursor).device_name);
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
        return beans;
    }

    public ArrayList<String> findByType(String content) {
        ArrayList<String> beans;
        synchronized (LOCK) {
            openReadDB();
            beans = new ArrayList();
            Cursor cursor = null;
            try {
                cursor = getCursor(" device_type = ? ", new String[]{content});
                while (cursor.moveToNext()) {
                    beans.add(cursorToBean(cursor).device_name);
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
        return beans;
    }

    public String findTypeByName(String deviceName) {
        openWriteDB();
        String name = null;
        Cursor cursor = null;
        try {
            cursor = this.database.query(Table.T_NAME, new String[]{"device_type"}, " device_name = ? ", new String[]{deviceName}, null, null, null);
            if (cursor != null) {
                cursor.moveToNext();
                name = cursor.getString(0);
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
        return name;
    }
}
