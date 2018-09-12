package com.easyctrl.manager;

import android.util.Log;
import com.easyctrl.ldy.domain.ModulePortBean;
import com.easyctrl.ldy.util.Util;
import java.util.Calendar;
import java.util.Date;

public class OrderManage {
    public static final int CLOSE = 1;
    public static final int OPEN = 0;
    public static final byte TIME_START = (byte) 1;
    public static final byte TIME_STOP = (byte) 0;
    public static final byte TUME_DELETE = (byte) 5;

    public static byte[] aiming(ModulePortBean bean) {
        int p = Integer.parseInt(Integer.toHexString(bean.progress), 16);
        byte[] data = new byte[12];
        data[0] = (byte) -2;
        data[2] = (byte) 16;
        data[3] = Util.toHexByte(bean.moduleID);
        data[4] = Util.toHexByte(bean.port);
        data[6] = (byte) 4;
        data[7] = (byte) p;
        data[8] = (byte) 1;
        data[10] = (byte) 16;
        data[11] = (byte) -54;
        return data;
    }

    public static byte[] sendAnmingOrder(ModulePortBean modulePortBean, int type) {
        byte[] data = new byte[12];
        data[0] = (byte) -2;
        data[2] = (byte) 16;
        data[3] = Util.toHexByte(modulePortBean.moduleID);
        data[4] = Util.toHexByte(modulePortBean.port);
        data[6] = (byte) 4;
        data[8] = (byte) 5;
        data[10] = (byte) 16;
        data[11] = (byte) -54;
        if (type == 0) {
            data[10] = (byte) 18;
        } else if (type == 1) {
            data[7] = (byte) 0;
        }
        return data;
    }

    public static byte[] bindPhysics(ModulePortBean bean, int bid, int bkey, String model, boolean isBack) {
        byte[] data = new byte[22];
        data[0] = (byte) -2;
        data[1] = (byte) 14;
        data[2] = (byte) 16;
        data[3] = (byte) -2;
        data[4] = (byte) 10;
        data[5] = (byte) 1;
        data[6] = (byte) 14;
        data[8] = Util.toHexByte(bid);
        data[10] = Util.toHexByte(bkey);
        data[11] = (byte) -2;
        data[13] = Util.toHexByte(bean.moduleID);
        data[14] = Util.toHexByte(bean.port);
        data[15] = (byte) 1;
        data[16] = (byte) -2;
        data[21] = (byte) -54;
        if (model.equals("\u5f00\u5173\u5355\u952e\u53d6\u53cd")) {
            data[15] = (byte) 1;
            data[16] = (byte) -2;
            if (isBack) {
                data[18] = Util.toHexByte(bean.moduleID);
                data[19] = Util.toHexByte(bean.port);
            } else {
                data[18] = (byte) 0;
                data[19] = (byte) 0;
            }
        } else if (model.equals("\u5f00\u5173\u5355\u952e\u5e38\u5f00")) {
            data[15] = (byte) 3;
            data[20] = (byte) 0;
            data[16] = (byte) -2;
            if (isBack) {
                data[18] = Util.toHexByte(bean.moduleID);
                data[19] = Util.toHexByte(bean.port);
            } else {
                data[18] = (byte) 0;
                data[19] = (byte) 0;
            }
        } else if (model.equals("\u5f00\u5173\u5355\u952e\u5e38\u5173")) {
            data[15] = (byte) 2;
            data[20] = (byte) 0;
            data[16] = (byte) -2;
            if (isBack) {
                data[18] = Util.toHexByte(bean.moduleID);
                data[19] = Util.toHexByte(bean.port);
            } else {
                data[18] = (byte) 0;
                data[19] = (byte) 0;
            }
        } else if (model.equals("\u5f00\u5173\u53cc\u952e\u5f00\u542f")) {
            data[15] = (byte) 17;
            data[20] = (byte) 0;
            data[16] = (byte) -2;
            if (isBack) {
                data[18] = Util.toHexByte(bean.moduleID);
                data[19] = Util.toHexByte(bean.port);
            } else {
                data[18] = (byte) 0;
                data[19] = (byte) 0;
            }
        } else if (model.equals("\u5f00\u5173\u53cc\u952e\u5173\u95ed")) {
            data[15] = (byte) 18;
            data[20] = (byte) 0;
            data[16] = (byte) -2;
            if (isBack) {
                data[18] = Util.toHexByte(bean.moduleID);
                data[19] = Util.toHexByte(bean.port);
            } else {
                data[18] = (byte) 0;
                data[19] = (byte) 0;
            }
        } else if (model.equals("\u7a97\u5e18\u5355\u952e\u5faa\u73af")) {
            data[15] = (byte) 1;
            data[16] = (byte) -2;
            data[17] = (byte) 0;
            data[18] = (byte) 0;
            data[19] = (byte) 0;
            data[20] = (byte) 0;
        } else if (model.equals("\u7a97\u5e18\u53cc\u952e\u5f00\u542f")) {
            data[15] = (byte) 17;
            data[20] = (byte) 0;
            data[16] = (byte) -2;
            if (isBack) {
                data[18] = Util.toHexByte(bean.moduleID);
                data[19] = Util.toHexByte(bean.port);
            } else {
                data[18] = (byte) 0;
                data[19] = (byte) 0;
            }
        } else if (model.equals("\u7a97\u5e18\u53cc\u952e\u5173\u95ed")) {
            data[15] = (byte) 18;
            data[20] = (byte) 0;
            data[16] = (byte) -2;
            if (isBack) {
                data[18] = Util.toHexByte(bean.moduleID);
                data[19] = Util.toHexByte(bean.port);
            } else {
                data[18] = (byte) 0;
                data[19] = (byte) 0;
            }
        } else if (model.equals("\u7a97\u5e18\u4e09\u952e\u5f00\u542f")) {
            data[15] = (byte) 33;
            data[20] = (byte) 0;
            data[16] = (byte) -2;
            if (isBack) {
                data[18] = Util.toHexByte(bean.moduleID);
                data[19] = Util.toHexByte(bean.port);
            } else {
                data[18] = (byte) 0;
                data[19] = (byte) 0;
            }
        } else if (model.equals("\u7a97\u5e18\u4e09\u952e\u5173\u95ed")) {
            data[15] = (byte) 34;
            data[20] = (byte) 0;
            data[16] = (byte) -2;
            if (isBack) {
                data[18] = Util.toHexByte(bean.moduleID);
                data[19] = Util.toHexByte(bean.port);
            } else {
                data[18] = (byte) 0;
                data[19] = (byte) 0;
            }
        } else if (model.equals("\u7a97\u5e18\u4e09\u952e\u505c\u6b62")) {
            data[15] = (byte) 35;
            data[20] = (byte) 0;
            data[16] = (byte) -2;
            data[17] = (byte) 0;
            data[18] = (byte) 0;
            data[19] = (byte) 0;
        } else if (model.equals("\u8c03\u5149\u5355\u952e\u5faa\u73af")) {
            data[15] = (byte) 1;
            data[20] = (byte) 0;
            data[16] = (byte) -2;
            if (isBack) {
                data[18] = Util.toHexByte(bean.moduleID);
                data[19] = Util.toHexByte(bean.port);
            } else {
                data[18] = (byte) 0;
                data[19] = (byte) 0;
            }
        } else if (model.equals("\u8c03\u5149\u53cc\u952e\u5f00\u542f")) {
            data[15] = (byte) 17;
            data[20] = (byte) 0;
            data[16] = (byte) -2;
            if (isBack) {
                data[18] = Util.toHexByte(bean.moduleID);
                data[19] = Util.toHexByte(bean.port);
            } else {
                data[18] = (byte) 0;
                data[19] = (byte) 0;
            }
        } else if (model.equals("\u8c03\u5149\u53cc\u952e\u5173\u95ed")) {
            data[15] = (byte) 18;
            data[20] = (byte) 0;
            data[16] = (byte) -2;
            if (isBack) {
                data[18] = Util.toHexByte(bean.moduleID);
                data[19] = Util.toHexByte(bean.port);
            } else {
                data[18] = (byte) 0;
                data[19] = (byte) 0;
            }
        } else if (model.equals("\u8c03\u5149\u53cc\u952e\u52a010%")) {
            data[15] = (byte) 19;
            data[20] = (byte) 0;
            data[16] = (byte) -2;
            if (isBack) {
                data[18] = Util.toHexByte(bean.moduleID);
                data[19] = Util.toHexByte(bean.port);
            } else {
                data[18] = (byte) 0;
                data[19] = (byte) 0;
            }
        } else if (model.equals("\u8c03\u5149\u53cc\u952e\u51cf10%")) {
            data[15] = (byte) 20;
            data[20] = (byte) 0;
            data[16] = (byte) -2;
            if (isBack) {
                data[18] = Util.toHexByte(bean.moduleID);
                data[19] = Util.toHexByte(bean.port);
            } else {
                data[18] = (byte) 0;
                data[19] = (byte) 0;
            }
        }
        return data;
    }

    public static byte[] virtyalBindPhysics(ModulePortBean bean, int bkey, String model) {
        byte[] data = new byte[22];
        data[0] = (byte) -2;
        data[1] = (byte) 14;
        data[2] = (byte) 16;
        data[3] = (byte) -2;
        data[4] = (byte) 10;
        data[5] = (byte) 1;
        data[6] = (byte) 14;
        data[7] = (byte) 14;
        data[8] = (byte) -2;
        data[10] = Util.toHexByte(bkey);
        data[11] = (byte) -2;
        data[13] = Util.toHexByte(bean.moduleID);
        data[14] = Util.toHexInt2(bean.port);
        data[15] = (byte) 1;
        data[16] = (byte) -2;
        data[18] = Util.toHexByte(bean.moduleID);
        data[19] = Util.toHexInt2(bean.port);
        data[21] = (byte) -54;
        if (model.equals("\u5f00\u5173\u5355\u952e\u53d6\u53cd")) {
            data[15] = (byte) 1;
            data[20] = (byte) 0;
        } else if (model.equals("\u5f00\u5173\u5355\u952e\u5e38\u5f00")) {
            data[15] = (byte) 3;
            data[20] = (byte) 0;
        } else if (model.equals("\u5f00\u5173\u5355\u952e\u5e38\u5173")) {
            data[15] = (byte) 2;
            data[20] = (byte) 0;
        } else if (model.equals("\u5f00\u5173\u53cc\u952e\u5f00\u542f")) {
            data[15] = (byte) 17;
            data[20] = (byte) 0;
        } else if (model.equals("\u5f00\u5173\u53cc\u952e\u5173\u95ed")) {
            data[15] = (byte) 18;
            data[20] = (byte) 0;
        } else if (model.equals("\u7a97\u5e18\u5355\u952e\u5faa\u73af")) {
            data[15] = (byte) 1;
            data[16] = (byte) -2;
            data[17] = (byte) 0;
            data[18] = (byte) 0;
            data[19] = (byte) 0;
            data[20] = (byte) 0;
        } else if (model.equals("\u7a97\u5e18\u53cc\u952e\u5f00\u542f")) {
            data[15] = (byte) 17;
            data[20] = (byte) 0;
            data[16] = (byte) -2;
        } else if (model.equals("\u7a97\u5e18\u53cc\u952e\u5173\u95ed")) {
            data[15] = (byte) 18;
            data[20] = (byte) 0;
            data[16] = (byte) -2;
        } else if (model.equals("\u7a97\u5e18\u4e09\u952e\u5f00\u542f")) {
            data[15] = (byte) 33;
            data[20] = (byte) 0;
            data[16] = (byte) -2;
        } else if (model.equals("\u7a97\u5e18\u4e09\u952e\u5173\u95ed")) {
            data[15] = (byte) 34;
            data[20] = (byte) 0;
            data[16] = (byte) -2;
        } else if (model.equals("\u7a97\u5e18\u4e09\u952e\u505c\u6b62")) {
            data[15] = (byte) 35;
            data[20] = (byte) 0;
            data[16] = (byte) -2;
            data[17] = (byte) 0;
            data[18] = (byte) 0;
            data[19] = (byte) 0;
        } else if (model.equals("\u8c03\u5149\u5355\u952e\u5faa\u73af")) {
            data[15] = (byte) 1;
            data[20] = (byte) 0;
            data[16] = (byte) -2;
        } else if (model.equals("\u8c03\u5149\u53cc\u952e\u5f00\u542f")) {
            data[15] = (byte) 17;
            data[20] = (byte) 0;
            data[16] = (byte) -2;
        } else if (model.equals("\u8c03\u5149\u53cc\u952e\u5173\u95ed")) {
            data[15] = (byte) 18;
            data[20] = (byte) 0;
            data[16] = (byte) -2;
        } else if (model.equals("\u8c03\u5149\u53cc\u952e\u52a010%")) {
            data[15] = (byte) 19;
            data[20] = (byte) 0;
            data[16] = (byte) -2;
        } else if (model.equals("\u8c03\u5149\u53cc\u952e\u51cf10%")) {
            data[15] = (byte) 20;
            data[20] = (byte) 0;
            data[16] = (byte) -2;
        }
        return data;
    }

    public static byte[] bindAmaing(ModulePortBean bean, int bid, int bkey, int model, boolean isBack) {
        byte[] data = new byte[22];
        data[0] = (byte) -2;
        data[1] = (byte) 14;
        data[2] = (byte) 16;
        data[3] = (byte) -2;
        data[4] = (byte) 10;
        data[5] = (byte) 1;
        data[6] = (byte) 14;
        data[8] = Util.toHexByte(bid);
        data[10] = Util.toHexInt2(bkey);
        data[11] = (byte) -2;
        data[13] = Util.toHexByte(bean.moduleID);
        data[14] = Util.toHexInt2(bean.port);
        data[15] = (byte) 1;
        data[16] = (byte) -2;
        data[21] = (byte) -54;
        if (model == 0) {
            data[15] = (byte) 1;
            data[20] = (byte) 0;
        } else if (model == 1) {
            data[15] = (byte) 2;
            data[20] = (byte) 0;
        } else if (model == 2) {
            data[15] = (byte) 17;
            data[20] = (byte) 0;
        } else if (model == 3) {
            data[15] = (byte) 18;
            data[20] = (byte) 0;
        }
        if (isBack) {
            data[18] = Util.toHexInt2(bean.moduleID);
            data[19] = Util.toHexInt2(bean.port);
        } else {
            data[18] = (byte) 0;
            data[19] = (byte) 0;
        }
        return data;
    }

    public static byte[] bingSenceBig9(int sameID, int bid, int bkey) {
        byte[] data = new byte[22];
        data[0] = (byte) -2;
        data[1] = (byte) 14;
        data[2] = (byte) 16;
        data[3] = (byte) -2;
        data[4] = (byte) 10;
        data[5] = (byte) 1;
        data[6] = (byte) 14;
        data[8] = Util.toHexByte(bid);
        data[10] = Util.toHexByte(bkey);
        data[11] = (byte) -2;
        data[12] = (byte) 16;
        data[14] = Util.toHexByte(sameID);
        data[15] = (byte) 17;
        data[21] = (byte) -54;
        return data;
    }

    public static byte[] bingSence(int sameID, int bid, int bkey, String model) {
        byte[] data = new byte[22];
        data[0] = (byte) -2;
        data[1] = (byte) 14;
        data[2] = (byte) 16;
        data[3] = (byte) -2;
        data[4] = (byte) 10;
        data[5] = (byte) 1;
        data[6] = (byte) 14;
        data[8] = Util.toHexByte(bid);
        data[10] = Util.toHexByte(bkey);
        data[11] = (byte) -2;
        data[12] = (byte) 16;
        data[14] = Util.toHexByte(sameID);
        data[15] = (byte) 17;
        data[21] = (byte) -54;
        if (model.equals("\u6309\u4e0b\u8c03\u7528")) {
            data[15] = (byte) 17;
        } else if (model.equals("\u957f\u6309\u573a\u666f")) {
            data[15] = (byte) 19;
        } else if (model.equals("\u6309\u4e0b\u573a\u666f1")) {
            data[15] = (byte) -127;
        } else if (model.equals("\u6309\u4e0b\u573a\u666f2")) {
            data[15] = (byte) -111;
        } else if (model.equals("\u6309\u4e0b\u573a\u666f3")) {
            data[15] = (byte) -95;
        } else if (model.equals("\u6309\u4e0b\u573a\u666f4")) {
            data[15] = (byte) -79;
        } else if (model.equals("\u91ca\u653e\u573a\u666f")) {
            data[15] = (byte) 18;
        }
        return data;
    }

    public static byte[] virtualBingSence(int sameID, int bkey) {
        int a = bkey;
        byte b = (byte) (a >> 8);
        byte c = (byte) a;
        byte e = (byte) (b >> 8);
        byte f = (byte) sameID;
        byte[] data = new byte[22];
        data[0] = (byte) -2;
        data[1] = (byte) 14;
        data[2] = (byte) 16;
        data[3] = (byte) -2;
        data[4] = (byte) 10;
        data[5] = (byte) 1;
        data[6] = (byte) 14;
        data[7] = (byte) 14;
        data[8] = (byte) -2;
        data[9] = b;
        data[10] = c;
        data[11] = (byte) -2;
        data[12] = (byte) 16;
        data[13] = e;
        data[14] = f;
        data[15] = (byte) 17;
        data[21] = (byte) -54;
        return data;
    }

    public static synchronized byte[] sendLampOrder(ModulePortBean bean, int type) {
        byte[] data;
        synchronized (OrderManage.class) {
            data = new byte[12];
            data[0] = (byte) -2;
            data[2] = (byte) 16;
            data[3] = Util.toHexByte(bean.moduleID);
            data[4] = Util.toHexByte(bean.port);
            data[6] = (byte) 4;
            data[7] = (byte) 1;
            data[11] = (byte) -54;
            if (type == 0) {
                data[7] = (byte) 1;
            } else if (type == 1) {
                data[7] = (byte) 0;
            }
        }
        return data;
    }

    public static byte[] removeBind(int bid, int bkey) {
        byte[] data = new byte[12];
        data[0] = (byte) -2;
        data[1] = (byte) 14;
        data[2] = (byte) 16;
        data[3] = (byte) -2;
        data[4] = (byte) 10;
        data[5] = (byte) -2;
        data[6] = (byte) 4;
        data[8] = Util.toHexByte(bid);
        data[10] = Util.toHexByte(bkey);
        data[11] = (byte) -54;
        return data;
    }

    public static byte[] bindDownAndUp(int bid, int bkey, boolean isRealse) {
        byte[] data = new byte[12];
        data[0] = (byte) -2;
        data[2] = (byte) -111;
        data[3] = Util.toHexByte(bid);
        data[6] = (byte) 4;
        data[7] = (byte) 1;
        data[8] = Util.toHexInt2(bkey);
        data[11] = (byte) -54;
        if (isRealse) {
            data[7] = (byte) 1;
        } else {
            data[7] = (byte) 2;
        }
        return data;
    }

    public static byte[] virtualDownAndUp(int bkey, boolean isRealse) {
        Log.i("data", "key:" + bkey);
        byte[] data = new byte[13];
        data[0] = (byte) -2;
        data[1] = (byte) 14;
        data[2] = (byte) 16;
        data[3] = (byte) -2;
        data[4] = (byte) 5;
        data[6] = (byte) 5;
        data[7] = (byte) 14;
        data[8] = (byte) -2;
        data[10] = Util.toHexByte(bkey);
        data[11] = (byte) 1;
        data[12] = (byte) -54;
        if (isRealse) {
            data[11] = (byte) 1;
        } else {
            data[11] = (byte) 2;
        }
        return data;
    }

    public static byte[] virtual_bind_delete(int bkey) {
        byte[] data = new byte[12];
        data[0] = (byte) -2;
        data[1] = (byte) 14;
        data[2] = (byte) 16;
        data[3] = (byte) -2;
        data[4] = (byte) 10;
        data[5] = (byte) -2;
        data[6] = (byte) 4;
        data[7] = (byte) 14;
        data[8] = (byte) -2;
        data[10] = Util.toHexByte(bkey);
        data[11] = (byte) -54;
        return data;
    }

    public static byte[] bindLongPress(int bid, int bkey) {
        byte[] data = new byte[13];
        data[0] = (byte) -2;
        data[1] = (byte) 14;
        data[2] = (byte) 16;
        data[3] = (byte) -2;
        data[4] = (byte) 5;
        data[6] = (byte) 5;
        data[8] = Util.toHexByte(bid);
        data[10] = Util.toHexByte(bkey);
        data[11] = (byte) 3;
        data[12] = (byte) -54;
        return data;
    }

    public static byte[] time_order(int id, byte type) {
        byte[] data = new byte[9];
        data[0] = (byte) -2;
        data[1] = (byte) 32;
        data[2] = (byte) 16;
        data[4] = (byte) 1;
        data[6] = (byte) 1;
        data[8] = (byte) -54;
        data[4] = Util.toHexByte(id);
        if (type == (byte) 1) {
            data[7] = (byte) 1;
        } else if (type == (byte) 0) {
            data[7] = (byte) 0;
        }
        return data;
    }

    public static byte[] updateIP(String oldIP, String newIp, String gateway, String mask) {
        return ("setip " + oldIP + " " + newIp + " " + mask + " " + gateway + " " + gateway).getBytes();
    }

    public static byte[] getHostCurrentTime() {
        byte[] getTime = new byte[8];
        getTime[0] = (byte) -2;
        getTime[1] = (byte) 14;
        getTime[2] = (byte) -112;
        getTime[3] = (byte) -2;
        getTime[4] = (byte) -1;
        getTime[5] = (byte) 14;
        getTime[7] = (byte) -54;
        return getTime;
    }

    public static byte[] calibraHostTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int year = calendar.get(1);
        return new byte[]{(byte) -2, (byte) 14, (byte) 16, (byte) -2, (byte) -1, (byte) 14, (byte) 7, Util.toHexByte((byte) year), Util.toHexByte((byte) (year >> 8)), Util.toHexByte(calendar.get(2) + 1), Util.toHexByte(calendar.get(5)), Util.toHexByte(calendar.get(11)), Util.toHexByte(calendar.get(12)), Util.toHexByte(calendar.get(13)), (byte) -54};
    }

    public static byte[] deleteTime(int deleteID) {
        byte[] delete = new byte[9];
        delete[0] = (byte) -2;
        delete[1] = (byte) 32;
        delete[2] = (byte) 16;
        delete[4] = Util.toHexByte(deleteID);
        delete[6] = (byte) 1;
        delete[7] = (byte) 5;
        delete[8] = (byte) -54;
        return delete;
    }

    public static byte[] deleteScene(int sceneNam) {
        int a = sceneNam;
        byte b = (byte) (a >> 8);
        byte c = (byte) a;
        byte[] deletescene = new byte[9];
        deletescene[0] = (byte) -2;
        deletescene[1] = (byte) 16;
        deletescene[2] = (byte) 16;
        deletescene[3] = b;
        deletescene[4] = c;
        deletescene[6] = (byte) 1;
        deletescene[7] = (byte) 5;
        deletescene[8] = (byte) -54;
        return deletescene;
    }

    public static byte[] searchCorrespondIDAndKey(int id, int key) {
        byte[] data = new byte[12];
        data[0] = (byte) -2;
        data[1] = (byte) 14;
        data[2] = (byte) -112;
        data[3] = (byte) -2;
        data[4] = (byte) 10;
        data[5] = (byte) 16;
        data[6] = (byte) 4;
        data[8] = Util.toHexByte(id);
        data[10] = Util.toHexByte(key);
        data[11] = (byte) -54;
        return data;
    }

    public static byte[] updateLoginPass(String oldPass, String newPass) {
        int i;
        byte[] old = oldPass.getBytes();
        byte[] newpass = newPass.getBytes();
        byte[] b = new byte[24];
        b[0] = (byte) -2;
        b[1] = (byte) 14;
        b[2] = (byte) 16;
        b[3] = (byte) -2;
        b[4] = (byte) -1;
        b[5] = (byte) 5;
        b[6] = (byte) 16;
        b[7] = (byte) 56;
        b[8] = (byte) 56;
        b[9] = (byte) 56;
        b[10] = (byte) 56;
        b[11] = (byte) 56;
        b[12] = (byte) 56;
        b[15] = (byte) 57;
        b[16] = (byte) 48;
        b[23] = (byte) -54;
        for (i = 0; i < old.length; i++) {
            b[i + 7] = Util.toHexByte(old[i]);
        }
        for (i = 0; i < newpass.length; i++) {
            b[i + 15] = Util.toHexByte(newpass[i]);
        }
        return b;
    }

    public static byte[] updateSetPass(String oldPass, String newPass) {
        int i;
        byte[] old = oldPass.getBytes();
        byte[] newpass = newPass.getBytes();
        byte[] b = new byte[24];
        b[0] = (byte) -2;
        b[1] = (byte) 14;
        b[2] = (byte) 16;
        b[3] = (byte) -2;
        b[4] = (byte) -1;
        b[5] = (byte) 6;
        b[6] = (byte) 16;
        b[7] = (byte) 56;
        b[8] = (byte) 56;
        b[9] = (byte) 56;
        b[10] = (byte) 56;
        b[11] = (byte) 56;
        b[12] = (byte) 56;
        b[15] = (byte) 57;
        b[16] = (byte) 48;
        b[23] = (byte) -54;
        for (i = 0; i < old.length; i++) {
            b[i + 7] = Util.toHexByte(old[i]);
        }
        for (i = 0; i < newpass.length; i++) {
            b[i + 15] = Util.toHexByte(newpass[i]);
        }
        return b;
    }

    public static byte[] searchAllTimer() {
        byte[] timers = new byte[12];
        timers[0] = (byte) -2;
        timers[1] = (byte) 14;
        timers[2] = (byte) -112;
        timers[3] = (byte) -2;
        timers[4] = (byte) 3;
        timers[6] = (byte) 4;
        timers[7] = (byte) -2;
        timers[8] = (byte) 32;
        timers[9] = (byte) -1;
        timers[10] = (byte) -1;
        timers[11] = (byte) -54;
        return timers;
    }

    public static byte[] backup() {
        return new byte[]{(byte) -2, (byte) 14, (byte) 16, (byte) -2, (byte) -1, (byte) -5, (byte) 1, (byte) 1, (byte) -54};
    }

    public static byte[] recovery() {
        return new byte[]{(byte) -2, (byte) 14, (byte) 16, (byte) -2, (byte) -1, (byte) -5, (byte) 1, (byte) 2, (byte) -54};
    }

    public static byte[] deleteAll() {
        return new byte[]{(byte) -2, (byte) 14, (byte) 16, (byte) -2, (byte) 10, (byte) -1, (byte) 2, (byte) -84, (byte) -84, (byte) -54};
    }
}
