package com.easyctrl.ldy.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public class WeekArray {
    private static WeekArray instance;
    private HashMap<Integer, String> weekMap = new HashMap();

    private WeekArray() {
    }

    public int size() {
        return this.weekMap.size();
    }

    public HashMap<Integer, String> getWeekMap() {
        return this.weekMap;
    }

    public static WeekArray getInatance() {
        if (instance == null) {
            instance = new WeekArray();
        }
        return instance;
    }

    public ArrayList<Integer> getKeys() {
        ArrayList<Integer> integers = new ArrayList();
        for (Entry entry : getWeekMap().entrySet()) {
            integers.add(Integer.valueOf(entry.getKey().toString()));
        }
        return integers;
    }

    public void add(int key, String value) {
        this.weekMap.put(Integer.valueOf(key), value);
    }

    public void clear() {
        this.weekMap.clear();
    }
}
