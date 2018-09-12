package com.easyctrl.ldy.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonUtil {
    private static final String RIGHT = "/";
    private static final String WRONG = "\\/";

    public static Map<String, Object> getMap(String json) {
        try {
            return getMap(new JSONObject(json));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Map<String, Object> getMap(JSONObject item) {
        JSONException e;
        if (item == null) {
            return null;
        }
        Map<String, Object> map = null;
        try {
            Map<String, Object> map2 = new HashMap();
            try {
                Iterator<String> iterator = item.keys();
                while (iterator.hasNext()) {
                    String key = (String) iterator.next();
                    map2.put(key, item.get(key));
                }
                return map2;
            } catch (JSONException e2) {
                e = e2;
                map = map2;
                e.printStackTrace();
                return map;
            }
        } catch (JSONException e3) {
            e = e3;
            e.printStackTrace();
            return map;
        }
    }

    public static List<Map<String, Object>> getList(JSONArray array) {
        if (array == null) {
            return null;
        }
        List<Map<String, Object>> list = new ArrayList();
        for (int i = 0; i < array.length(); i++) {
            try {
                list.add(getMap(array.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    public static <T> List<T> jsonToObjList(String json, Class<T> class1) {
        Exception e;
        ArrayList<T> listItem = null;
        try {
            Constructor<T> cts = class1.getConstructor(new Class[]{JSONObject.class});
            ArrayList<T> listItem2;
            if (json.indexOf("[") >= 0) {
                String str = json;
                JSONArray array = new JSONArray(str.substring(json.indexOf("["), json.lastIndexOf("]") + 1));
                listItem2 = new ArrayList();
                int i = 0;
                while (i < array.length()) {
                    try {
                        listItem2.add(cts.newInstance(new Object[]{(JSONObject) array.opt(i)}));
                        i++;
                    } catch (Exception e2) {
                        e = e2;
                        listItem = listItem2;
                    }
                }
                return listItem2;
            }
            JSONObject jsonObject = new JSONObject(json);
            listItem2 = new ArrayList();
            listItem2.add(cts.newInstance(new Object[]{jsonObject}));
            return listItem2;
        } catch (Exception e3) {
            e = e3;
            e.printStackTrace();
            return listItem;
        }
    }

    public static boolean equals(JSONObject j1, JSONObject j2) {
        if (j1 != null && j2 != null) {
            try {
                Iterator<String> iterator = j1.keys();
                while (iterator.hasNext()) {
                    String type = (String) iterator.next();
                    if (!correct(j1.get(type).toString()).equals(correct(j2.get(type).toString()))) {
                        return false;
                    }
                }
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        } else if (j1 == null && j2 == null) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean equals(Map j1, JSONObject j2) {
        if (j1 != null && j2 != null) {
            try {
                for (String type : j1.keySet()) {
                    if (!correct(j1.get(type).toString()).equals(correct(j2.get(type).toString()))) {
                        return false;
                    }
                }
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        } else if (j1 == null && j2 == null) {
            return true;
        } else {
            return false;
        }
    }

    public static int getInt(JSONObject obj, String key, int defaultValue) {
        if (!(obj == null || obj.isNull(key))) {
            try {
                defaultValue = Integer.parseInt(obj.getString(key));
            } catch (Exception e) {
            }
        }
        return defaultValue;
    }

    public static int getInt(Map map, String key, int defaultValue) {
        if (map == null || !map.containsKey(key)) {
            return defaultValue;
        }
        try {
            Object obj = map.get(key);
            return Integer.parseInt(obj == null ? null : obj.toString());
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static String getString(Map map, String key, String defaultValue) {
        if (map == null) {
            return defaultValue;
        }
        Object value = map.get(key);
        return value == null ? defaultValue : correct(value.toString());
    }

    public static String getString(JSONObject obj, String key, String defaultValue) {
        if (obj == null || obj.isNull(key)) {
            return defaultValue;
        }
        try {
            return correct(obj.getString(key));
        } catch (JSONException e) {
            return null;
        }
    }

    public static boolean getBoolean(JSONObject obj, String key, boolean defaultValue) {
        if (!(obj == null || obj.isNull(key))) {
            try {
                defaultValue = obj.getBoolean(key);
            } catch (JSONException e) {
            }
        }
        return defaultValue;
    }

    public static String correct(Object o) {
        if (o instanceof String) {
            return o.toString().replace(WRONG, RIGHT);
        }
        return null;
    }

    private static int obj2int(Object obj) {
        if (obj2Str(obj).trim() == "") {
            return 0;
        }
        return Integer.valueOf(obj2Str(obj).trim()).intValue();
    }

    private static String obj2Str(Object obj) {
        if (obj == null) {
            return "";
        }
        return obj.toString();
    }

    public static ArrayList<HashMap<String, Object>> objToMap(ArrayList<Object> array) {
        ArrayList<HashMap<String, Object>> listItem = new ArrayList();
        for (int i = 0; i < array.size(); i++) {
            Object obj = array.get(i);
            Field[] fields = obj.getClass().getDeclaredFields();
            Map<String, Object> map = new HashMap();
            for (Field f : fields) {
                f.setAccessible(true);
                try {
                    map.put(f.getName(), f.get(obj));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            listItem.add((HashMap) map);
        }
        System.out.println("tomap\u4e4b\u540e\u6570\u636e" + listItem.toString());
        return listItem;
    }

    public static Object objToJson(Map<String, Object> map) {
        return null;
    }
}
