package com.easyctrl.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import com.easyctrl.iface.BaseDBImpl;
import com.easyctrl.ldy.domain.BindBean;
import com.easyctrl.ldy.domain.ModuleBean;
import com.easyctrl.ldy.domain.ModuleBean.Table;
import com.easyctrl.ldy.domain.ModulePortBean;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class ModuleManager extends DataBaseManage implements BaseDBImpl<ModuleBean> {
    private static ModuleManager instance;
    private HashMap<Integer, ArrayList<ModulePortBean>> modulePortBeans;

    public void setModulePortBeans(HashMap<Integer, ArrayList<ModulePortBean>> modulePortBeans) {
        this.modulePortBeans = modulePortBeans;
    }

    public ModuleManager(Context context) {
        super(context);
        openWriteDB();
    }

    public static ModuleManager getInstance(Context context) {
        if (instance == null) {
            instance = new ModuleManager(context);
        }
        return instance;
    }

    public void add(ModuleBean t) {
    }

    public void deleteAll() {
        synchronized (LOCK) {
            openWriteDB();
            this.database.delete(Table.T_NAME, null, null);
            this.database.execSQL(" DELETE FROM sqlite_sequence ");
        }
    }

    private int current(int moduleID) {
        Cursor cursor = null;
        int count = 0;
        try {
            cursor = this.database.query(ModulePortBean.Table.T_NAME, null, " moduleID = ? ", new String[]{String.valueOf(moduleID)}, null, null, null);
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

    private Cursor getCursorByModuleID(ModuleBean bean) {
        return this.database.query(BindBean.Table.T_NAME, null, " bind_moduleID = ? ", new String[]{String.valueOf(bean.moduleId)}, null, null, null);
    }

    public void batchSave(ArrayList<ModuleBean> beans) {
        synchronized (LOCK) {
            openWriteDB();
            this.database.delete(Table.T_NAME, null, null);
            String sql = Table.getSaveSql();
            this.database.beginTransaction();
            Iterator it = beans.iterator();
            while (it.hasNext()) {
                ModuleBean bean = (ModuleBean) it.next();
                this.database.execSQL(sql, new Object[]{bean.moduleName, bean.moduleModel, bean.moduleNameExt, bean.moduleDescribe, bean.moduleVersion, bean.moduleRoom, Integer.valueOf(bean.moduleId), Integer.valueOf(bean.moduleGatewayID), Integer.valueOf(bean.modulePortNum), Integer.valueOf(bean.moduleLinknum), bean.moduleMac, Integer.valueOf(bean.type)});
                ArrayList<ModulePortBean> b = (ArrayList) this.modulePortBeans.get(Integer.valueOf(bean.moduleId));
                if (bean.moduleModel.startsWith("ET")) {
                    insertDB(bean, b);
                } else if (bean.moduleModel.startsWith("KP")) {
                    insertBindDB(null, bean);
                    insertDB(bean, b);
                } else if (bean.moduleModel.startsWith("MI")) {
                    insertBindDB(null, bean);
                    insertDB(bean, b);
                }
            }
            this.database.setTransactionSuccessful();
            this.database.endTransaction();
        }
    }

    private Cursor insertBindDB(Cursor cursor, ModuleBean bean) {
        try {
            cursor = getCursorByModuleID(bean);
            if (cursor.getCount() != 8) {
                this.database.delete(BindBean.Table.T_NAME, " bind_moduleID = ?   ", new String[]{String.valueOf(bean.moduleId)});
                String saveSql = " INSERT INTO " + BindBean.Table.T_NAME + " (" + "  bind_moduleID,keyValue " + ") values" + " (?,? )";
                for (int i = 1; i <= 8; i++) {
                    this.database.execSQL(saveSql, new Object[]{Integer.valueOf(bean.moduleId), Integer.valueOf(i)});
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
        return cursor;
    }

    private void insertDB(ModuleBean bean, ArrayList<ModulePortBean> b) {
        if (bean.modulePortNum != 1) {
            int i;
            ModulePortBean beanPort;
            if (current(bean.moduleId) != b.size()) {
                this.database.delete(ModulePortBean.Table.T_NAME, " moduleID = ? ", new String[]{String.valueOf(bean.moduleId)});
                for (i = 0; i < b.size(); i++) {
                    beanPort = (ModulePortBean) b.get(i);
                    this.database.execSQL(ModulePortBean.Table.getSaveSql(), new Object[]{Integer.valueOf(beanPort.moduleID), Integer.valueOf(beanPort.port), Integer.valueOf(beanPort.progress), Integer.valueOf(beanPort.isOpen), beanPort.floor, beanPort.room, beanPort.name, beanPort.deviveType, Integer.valueOf(beanPort.type)});
                }
                return;
            }
            for (i = 0; i < b.size(); i++) {
                beanPort = (ModulePortBean) b.get(i);
                String updateSql = " update " + ModulePortBean.Table.T_NAME + " set progress = ?,isOpen = ?, type = ?  where moduleID = ? and port = ? ";
                this.database.execSQL(updateSql, new Object[]{Integer.valueOf(beanPort.progress), Integer.valueOf(beanPort.isOpen), Integer.valueOf(beanPort.type), Integer.valueOf(beanPort.moduleID), Integer.valueOf(beanPort.port)});
            }
        }
    }

    public void batchDelete() {
        synchronized (LOCK) {
            openWriteDB();
            try {
                this.database.delete(Table.T_NAME, null, null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public int delete(int id) {
        openWriteDB();
        int coloum = 0;
        try {
            coloum = this.database.delete(Table.T_NAME, " id = ? ", new String[]{String.valueOf(id)});
        } catch (Exception e) {
            e.printStackTrace();
        }
        return coloum;
    }

    public int update(ModuleBean t, int id) {
        return 0;
    }

    public ArrayList<ModuleBean> findAll() {
        ArrayList<ModuleBean> beans;
        synchronized (LOCK) {
            beans = new ArrayList();
            openReadDB();
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

    public ArrayList<ModuleBean> findAllByWhereSort(String where) {
        ArrayList<ModuleBean> beans;
        synchronized (LOCK) {
            openReadDB();
            beans = new ArrayList();
            Cursor cursor = null;
            try {
                cursor = this.database.query(Table.T_NAME, null, null, null, null, null, where);
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

    public ArrayList<ModuleBean> findByType() {
        ArrayList<ModuleBean> arrayList;
        Exception e;
        Throwable th;
        synchronized (LOCK) {
            openReadDB();
            arrayList = null;
            Cursor cursor = null;
            try {
                cursor = getCursor(" type in(6,7) ", null);
                if (cursor != null && cursor.getCount() > 0) {
                    ArrayList<ModuleBean> array = new ArrayList();
                    while (cursor.moveToNext()) {
                        try {
                            array.add(cursorToBean(cursor));
                        } catch (Exception e2) {
                            e = e2;
                            arrayList = array;
                        } catch (Throwable th2) {
                            th = th2;
                            arrayList = array;
                        }
                    }
                    arrayList = array;
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

    public ContentValues beanToValues(ModuleBean bean) {
        return null;
    }

    public ModuleBean cursorToBean(Cursor cursor) {
        ModuleBean bean = new ModuleBean();
        bean.id = cursor.getInt(cursor.getColumnIndexOrThrow(Table.FIELD_ID));
        bean.moduleDescribe = cursor.getString(cursor.getColumnIndexOrThrow(Table.FIELD_DESCRIBE));
        bean.moduleFloor = cursor.getString(cursor.getColumnIndexOrThrow(Table.FIELD_FLOOR));
        bean.moduleGatewayID = cursor.getInt(cursor.getColumnIndexOrThrow(Table.FIELD_GATEWAYID));
        bean.moduleId = cursor.getInt(cursor.getColumnIndexOrThrow(Table.FIELD_MODULEID));
        bean.moduleLinknum = cursor.getInt(cursor.getColumnIndexOrThrow(Table.FIELD_MODULELINKNUM));
        bean.moduleMac = cursor.getString(cursor.getColumnIndexOrThrow(Table.FIELD_MODULEMAC));
        bean.moduleModel = cursor.getString(cursor.getColumnIndexOrThrow(Table.FIELD_MODEL));
        bean.moduleName = cursor.getString(cursor.getColumnIndexOrThrow(Table.FIELD_NAME));
        bean.moduleNameExt = cursor.getString(cursor.getColumnIndexOrThrow(Table.FIELD_NAMEExt));
        bean.modulePortNum = cursor.getInt(cursor.getColumnIndexOrThrow(Table.FIELD_MODULEPORTNUM));
        bean.moduleRoom = cursor.getString(cursor.getColumnIndexOrThrow(Table.FIELD_ROOM));
        bean.moduleVersion = cursor.getString(cursor.getColumnIndexOrThrow(Table.FIELD_VERSUIB));
        bean.type = cursor.getInt(cursor.getColumnIndexOrThrow(Table.FIELD_TYPE));
        return bean;
    }

    public Cursor getCursor(String where, String[] args) {
        return this.database.query(Table.T_NAME, null, where, args, null, null, null);
    }
}
