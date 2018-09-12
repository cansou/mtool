package com.easyctrl.iface;

import android.content.ContentValues;
import android.database.Cursor;
import java.util.ArrayList;

public interface BaseDBImpl<T> {
    void add(T t);

    ContentValues beanToValues(T t);

    T cursorToBean(Cursor cursor);

    int delete(int i);

    ArrayList<T> findAll();

    Cursor getCursor(String str, String[] strArr);

    int update(T t, int i);
}
