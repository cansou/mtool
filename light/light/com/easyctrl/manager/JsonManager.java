package com.easyctrl.manager;

import android.content.Context;
import com.easyctrl.ldy.activity.MainApplication;
import com.easyctrl.ldy.domain.ImitateBean.Table;
import com.easyctrl.ldy.domain.ModuleBean;
import com.easyctrl.ldy.domain.ModulePortBean;
import com.easyctrl.ldy.domain.TimerBean;
import com.easyctrl.ldy.net.SocketUtil;
import com.easyctrl.ldy.util.FileUtil;
import com.easyctrl.ldy.util.JsonUtil;
import com.easyctrl.ldy.util.Util;
import com.easyctrl.ldy.util.Value;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonManager {
    private static JsonManager manager;
    private HashMap<Integer, ArrayList<ModulePortBean>> hashMap = new HashMap();
    private ArrayList<ModuleBean> list = new ArrayList();
    private ArrayList<ModulePortBean> listItem = new ArrayList();
    private Context mContext;
    private ArrayList<String> onLineModuleID;

    public ArrayList<String> getOnLineModuleID() {
        return this.onLineModuleID;
    }

    private void getJsonData(String ip, int port, String name, String saveName, Context context) throws Exception {
        SocketUtil getData = new SocketUtil(ip, port, context);
        getData.sendData(name);
        getData.readData(saveName);
    }

    public void downloadData() throws Exception {
        getJsonData(MainApplication.userManager.currentHost(), 6001, "read data.json$", "data.json", this.mContext);
        Thread.sleep(100);
        getJsonData(MainApplication.userManager.currentHost(), 6001, "read serial.json$", "serial.json", this.mContext);
    }

    public void deleteModule() {
        Iterator<ModuleBean> iterator = this.list.iterator();
        while (iterator.hasNext()) {
            ModuleBean module = (ModuleBean) iterator.next();
            iterator.remove();
            this.list.remove(module);
        }
    }

    public void deleteObject() {
        Iterator<ModulePortBean> iterator = this.listItem.iterator();
        while (iterator.hasNext()) {
            ModulePortBean bean = (ModulePortBean) iterator.next();
            iterator.remove();
            this.listItem.remove(bean);
        }
    }

    public List<ModulePortBean> getListItem() {
        return this.listItem;
    }

    public ArrayList<ModuleBean> getList() {
        return this.list;
    }

    private JsonManager(Context context) {
        this.mContext = context;
    }

    public static synchronized JsonManager getInstance(Context context) {
        JsonManager jsonManager;
        synchronized (JsonManager.class) {
            if (manager == null) {
                manager = new JsonManager(context);
            }
            jsonManager = manager;
        }
        return jsonManager;
    }

    public ArrayList<ModulePortBean> getModulePortByModuleID(Integer moduleid) {
        return (ArrayList) this.hashMap.get(moduleid);
    }

    public HashMap<Integer, ArrayList<ModulePortBean>> getHashMap() {
        return this.hashMap;
    }

    public ArrayList<ModulePortBean> getModulePortByJson() {
        JSONException e;
        JSONObject jSONObject;
        String json = FileUtil.readData("data.json", this.mContext);
        if (this.listItem.size() > 0) {
            this.listItem = null;
            this.listItem = new ArrayList();
        }
        try {
            JSONObject jSONObject2 = new JSONObject(json);
            List<Map<String, Object>> list = JsonUtil.getList(jSONObject2.getJSONArray("data"));
            int i = 0;
            while (i < list.size()) {
                try {
                    Map<String, Object> map = (Map) list.get(i);
                    Integer deviceID = (Integer) map.get(Table.FIELD_ID);
                    Object obj = map.get("ports");
                    if (obj != null) {
                        List<Map<String, Object>> listPorts = JsonUtil.getList(new JSONArray(obj.toString()));
                        ArrayList<ModulePortBean> temp = new ArrayList();
                        for (int k = 0; k < listPorts.size(); k++) {
                            Map<String, Object> port = (Map) listPorts.get(k);
                            int p = ((Integer) port.get("port")).intValue();
                            if (p != 0) {
                                int type;
                                ModulePortBean modulePort = new ModulePortBean();
                                modulePort.floor = "\u8bbe\u5907";
                                modulePort.room = "\u7aef\u53e3";
                                modulePort.name = "\u7aef\u53e3 ";
                                modulePort.port = p;
                                String strNum = (String) port.get("val");
                                if (strNum.equalsIgnoreCase("ffffffff")) {
                                    type = 1;
                                } else {
                                    type = Integer.valueOf(strNum.substring(0, 1)).intValue();
                                }
                                int progress = Util.toInt(strNum);
                                if (type == 0) {
                                    modulePort.isOpen = Integer.valueOf(strNum.substring(7, 8)).intValue();
                                    if (modulePort.isOpen == 1) {
                                        modulePort.progress = 100;
                                    } else {
                                        modulePort.progress = 0;
                                    }
                                    modulePort.type = 0;
                                } else if (type == 1) {
                                    modulePort.progress = progress;
                                    modulePort.type = 1;
                                }
                                modulePort.moduleID = deviceID.intValue();
                                temp.add(modulePort);
                                this.listItem.add(modulePort);
                            }
                        }
                        this.hashMap.put(deviceID, temp);
                    }
                    i++;
                } catch (JSONException e2) {
                    e = e2;
                    jSONObject = jSONObject2;
                }
            }
            jSONObject = jSONObject2;
        } catch (JSONException e3) {
            e = e3;
            e.printStackTrace();
            return this.listItem;
        }
        return this.listItem;
    }

    private void addOnline(ModuleBean module) {
        if (module.modulePortNum > 1) {
            this.onLineModuleID.add(String.valueOf(module.moduleId));
        }
    }

    public ArrayList<ModuleBean> getModuleByJson() {
        JSONException e;
        JSONObject jSONObject;
        JSONObject jSONObject2;
        if (this.list.size() > 0) {
            this.list = null;
            this.list = new ArrayList();
        }
        String jsonData = FileUtil.readData("data.json", this.mContext);
        String jsonSerial = FileUtil.readData("serial.json", this.mContext);
        try {
            JSONObject objectData = new JSONObject(jsonData);
            try {
                JSONObject jSONObject3 = new JSONObject(jsonSerial);
                JSONArray array = objectData.getJSONArray("data");
                JSONArray serilaArray = jSONObject3.getJSONArray("serial");
                List<Map<String, Object>> listMap = JsonUtil.getList(array);
                List<Map<String, Object>> listSerial = JsonUtil.getList(serilaArray);
                this.onLineModuleID = new ArrayList();
                for (int i = 0; i < listMap.size(); i++) {
                    Map<String, Object> map = (Map) listMap.get(i);
                    ModuleBean module = new ModuleBean();
                    int id = ((Integer) map.get(Table.FIELD_ID)).intValue();
                    module.moduleName = "\u8bbe\u5907 " + id;
                    module.moduleLinknum = ((Integer) map.get("lid")).intValue();
                    module.moduleGatewayID = ((Integer) map.get("nid")).intValue();
                    module.moduleId = id;
                    module.moduleFloor = "";
                    module.moduleRoom = "";
                    Map<String, Object> serialObj = null;
                    try {
                        serialObj = (Map) listSerial.get(i);
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                    if (serialObj != null) {
                        try {
                            String serial = (String) serialObj.get("serial");
                            String version = (String) serialObj.get("version");
                            String mac = (String) serialObj.get("mac8");
                            module.moduleModel = serial.trim();
                            module.moduleVersion = version.trim();
                            module.moduleMac = mac;
                            module.modulePortNum = ((Integer) serialObj.get("portnum")).intValue();
                            if (serial.trim().equals(Value.MODEL_R0816)) {
                                module.moduleNameExt = "8\u8def16A\u5f00\u5173";
                                module.type = 4;
                                addOnline(module);
                            } else if (serial.equals(Value.MODEL_MI0808A)) {
                                module.moduleNameExt = "8\u8defIO\u6a21\u5757";
                                module.type = 7;
                                addOnline(module);
                            } else if (serial.trim().equals(Value.MODEL_D0201A)) {
                                module.moduleNameExt = "2\u8def1A\u540e\u65a9\u8c03\u5149";
                                module.type = 5;
                                addOnline(module);
                            } else if (serial.trim().equals(Value.MODEL_R0420A)) {
                                module.moduleNameExt = "4\u8def\u8c03\u5149";
                                module.type = 5;
                                addOnline(module);
                            } else if (serial.trim().equals(Value.MODEL_D0403A)) {
                                module.moduleNameExt = "4\u8def3A\u524d\u65a9\u8c03\u5149";
                                module.type = 5;
                                addOnline(module);
                            } else if (serial.trim().equals(Value.MODEL_KP06A)) {
                                module.moduleNameExt = "\u667a\u80fd6\u952e\u9762\u677f";
                                module.type = 6;
                                addOnline(module);
                            } else if (serial.trim().equals(Value.MODEL_KP06B)) {
                                module.moduleNameExt = "\u667a\u80fd6\u952e\u9762\u677f";
                                module.type = 7;
                                addOnline(module);
                            } else if (serial.trim().equals(Value.MODEL_D0102A)) {
                                module.type = 5;
                                addOnline(module);
                            } else if (serial.trim().equals(Value.MODEL_ET_DMX_08A)) {
                                module.moduleNameExt = "8\u8def\u8c03\u5149";
                                module.type = 5;
                                addOnline(module);
                            } else if (serial.trim().equals(Value.MODEL_ET_DMX_12A)) {
                                module.moduleNameExt = "12\u8def\u8c03\u5149";
                                module.type = 5;
                                addOnline(module);
                            } else if (serial.trim().equals(Value.MODEL_ET_DAX_06A)) {
                                module.moduleNameExt = "6\u8def\u8c03\u5149";
                                module.type = 5;
                                addOnline(module);
                            } else if (serial.trim().equals(Value.MODEL_ET_DMX_16A)) {
                                module.moduleNameExt = "16\u8def\u8c03\u5149";
                                module.type = 5;
                                addOnline(module);
                            } else if (serial.trim().equals(Value.MODEL_R0416)) {
                                module.moduleNameExt = "4\u8def16A\u5f00\u5173";
                                module.type = 4;
                                addOnline(module);
                            } else if (serial.trim().equals(Value.MODEL_R0416D4I)) {
                                module.moduleNameExt = "4\u8def0-10V\u8c03\u5149";
                                module.type = 5;
                                addOnline(module);
                            } else {
                                addOnline(module);
                            }
                        } catch (JSONException e3) {
                            e = e3;
                            jSONObject = jSONObject3;
                            jSONObject2 = objectData;
                        }
                    }
                    this.list.add(module);
                }
                jSONObject = jSONObject3;
                jSONObject2 = objectData;
            } catch (JSONException e4) {
                e = e4;
                jSONObject2 = objectData;
                e.printStackTrace();
                return this.list;
            }
        } catch (JSONException e5) {
            e = e5;
            e.printStackTrace();
            return this.list;
        }
        return this.list;
    }

    public String createSenceJson(ArrayList<ModulePortBean> bindBeans, Context context) {
        Object list = new ArrayList();
        Map<String, Object> map = new HashMap();
        map.put("type", "action");
        map.put("describe", "\u63cf\u8ff0");
        List<Map<String, Object>> listSence = new ArrayList();
        for (int j = 0; j < bindBeans.size(); j++) {
            ModulePortBean bean = (ModulePortBean) bindBeans.get(j);
            Map<String, Object> secen = new HashMap();
            secen.put("nid", Integer.valueOf(0));
            secen.put("lid", Integer.valueOf(0));
            secen.put(Table.FIELD_ID, Integer.valueOf(bean.moduleID));
            secen.put("port", Integer.valueOf(bean.port));
            if (bean.type == 0) {
                if (bean.isOpen == 0) {
                    secen.put("val", "00000000");
                } else {
                    secen.put("val", "00000001");
                }
            } else if (bean.type == 1) {
                String hex = Integer.toHexString(bean.progress);
                if (hex.length() == 1) {
                    hex = "0" + hex;
                }
                secen.put("val", "100005" + hex);
            }
            secen.put("delay", Integer.valueOf(0));
            listSence.add(secen);
        }
        map.put("action", listSence);
        list.add(map);
        StringBuffer buf = new StringBuffer(new Gson().toJson(list));
        buf.deleteCharAt(buf.indexOf("["));
        buf.deleteCharAt(buf.lastIndexOf("]"));
        return buf.toString();
    }

    public String createTimerJson(ArrayList<TimerBean> timerBeans) {
        if (timerBeans == null || timerBeans.size() <= 0) {
            return null;
        }
        Object list = new ArrayList();
        HashMap<String, Object> map = new HashMap();
        List<Map<String, Object>> listTimers = new ArrayList();
        map.put("type", "timer");
        map.put("describe", "\u63cf\u8ff0");
        for (int i = 0; i < timerBeans.size(); i++) {
            Map<String, Object> timer = new HashMap();
            TimerBean timerbean = (TimerBean) timerBeans.get(i);
            timer.put("mon", Integer.valueOf(timerbean.mon));
            timer.put("year", Integer.valueOf(timerbean.year));
            timer.put("date", Integer.valueOf(timerbean.date));
            timer.put("day", Integer.valueOf(timerbean.day));
            timer.put("hour", Integer.valueOf(timerbean.hour));
            timer.put("min", Integer.valueOf(timerbean.min));
            timer.put("obj", timerbean.obj);
            timer.put("data", "00000001");
            listTimers.add(timer);
        }
        map.put("timer", listTimers);
        list.add(map);
        StringBuffer buf = new StringBuffer(new Gson().toJson(list));
        buf.deleteCharAt(buf.indexOf("["));
        buf.deleteCharAt(buf.lastIndexOf("]"));
        return buf.toString();
    }

    public static String[] datatoArray(String data) {
        if (data == null) {
            return null;
        }
        return data.split(" ");
    }
}
