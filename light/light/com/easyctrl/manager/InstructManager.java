package com.easyctrl.manager;

import android.util.Log;
import com.easyctrl.iface.OnBindListener;
import com.easyctrl.iface.ProcesInstructImpl;
import com.easyctrl.iface.RegisterInteface;
import com.easyctrl.ldy.util.Util;
import java.util.LinkedList;

public class InstructManager {
    private static final int AMING = 16;
    private static final int SINGLE = 0;
    private static InstructManager instance;
    private LinkedList<byte[]> data = new LinkedList();
    private byte[] instruct;
    private ProcesInstructImpl instructImpl;
    private OnBindListener onBindListener;
    private RegisterInteface registerInteface;

    public void setRegisterInteface(RegisterInteface registerInteface) {
        this.registerInteface = registerInteface;
    }

    public void setOnBindListener(OnBindListener onBindListener) {
        this.onBindListener = onBindListener;
    }

    public void setInstructImpl(ProcesInstructImpl instructImpl) {
        this.instructImpl = instructImpl;
    }

    public static synchronized InstructManager getInstance() {
        InstructManager instructManager;
        synchronized (InstructManager.class) {
            if (instance == null) {
                instance = new InstructManager();
            }
            instructManager = instance;
        }
        return instructManager;
    }

    public void setInstruct(byte[] instruct) {
        this.instruct = instruct;
        checkOrder(instruct);
        this.data.offer(instruct);
    }

    private String[] checkOrder(byte[] instruct) {
        int i;
        String str = "";
        for (byte append : instruct) {
            str = new StringBuilder(String.valueOf(str)).append(append).append(" ").toString();
        }
        String[] qiege = str.split("-54");
        for (i = 0; i < qiege.length; i++) {
            qiege[i] = new StringBuilder(String.valueOf(qiege[i].trim())).append(" -54").toString();
        }
        return qiege;
    }

    public int getModuleID() {
        return this.instruct[3];
    }

    public int getModulePort() {
        return this.instruct[4];
    }

    public int getData() {
        return this.instruct[7];
    }

    public synchronized void proceType() {
        loop0:
        while (true) {
            byte[] d = (byte[]) this.data.poll();
            if (d != null) {
                String[] str = checkOrder(d);
                int i = 0;
                while (true) {
                    if (i < str.length) {
                        int[] ss = Util.str2Array(str[i].trim());
                        if (ss.length == 1) {
                            break loop0;
                        }
                        if (ss.length == 12) {
                            if (ss[10] == 16) {
                                if (this.instructImpl != null) {
                                    this.instructImpl.ammingProce(ss[3], ss[4], ss[7]);
                                }
                            } else if (ss[10] != 0 || ss[4] == 0) {
                                if (ss[4] == 0 && ss[5] == 0 && this.onBindListener != null) {
                                    this.onBindListener.onBind(ss[3], ss[7], ss[8]);
                                }
                            } else if (this.instructImpl != null) {
                                this.instructImpl.lampProce(ss[3], ss[4], ss[7]);
                            }
                        } else if (ss.length == 9 && ss[5] == 6) {
                            this.instructImpl.loginSettingProce(ss[7]);
                            Log.i("data", "\u8fdb\u5165\u767b\u5f55\u8bbe\u7f6e");
                        } else if (ss.length == 9 && ss[5] == 5) {
                            this.instructImpl.loginApp(ss[7]);
                        } else if (ss.length == 10 && ss[4] == 2 && ss[5] == 0 && ss[6] == 2) {
                            if (ss[8] == 0) {
                                if (this.registerInteface != null) {
                                    this.registerInteface.remove(ss[7]);
                                }
                            } else if (ss[8] == 1 && this.registerInteface != null) {
                                this.registerInteface.register(ss[7]);
                            }
                        }
                        i++;
                    }
                }
            } else {
                break;
            }
        }
    }
}
