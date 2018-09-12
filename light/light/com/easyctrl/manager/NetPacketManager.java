package com.easyctrl.manager;

import android.util.Log;
import com.easyctrl.event.EasyEventType;
import com.easyctrl.event.EasyEventTypeHandler;
import com.easyctrl.iface.OnBindListener;
import com.easyctrl.iface.OnReviceLoginListener;
import com.easyctrl.iface.ProcesInstructImpl;
import com.easyctrl.ldy.domain.HostTime;
import com.easyctrl.ldy.util.Util;
import de.greenrobot.event.EventBus;
import java.util.Arrays;
import java.util.LinkedList;

public class NetPacketManager {
    private static final int AMING = 16;
    private static final int SINGLE = 0;
    private LinkedList<byte[]> data = new LinkedList();
    private ProcesInstructImpl instructImpl;
    private OnBindListener onBindListener;
    private OnReviceLoginListener onReviceLoginListener;
    private byte[] revieceData;

    public void setInstructImpl(ProcesInstructImpl instructImpl) {
        this.instructImpl = instructImpl;
    }

    public void setOnBindListener(OnBindListener onBindListener) {
        this.onBindListener = onBindListener;
    }

    public void setData(byte[] revieceData) {
        this.revieceData = revieceData;
        this.data.offer(revieceData);
    }

    public void setOnReviceLoginListener(OnReviceLoginListener onReviceLoginListener) {
        this.onReviceLoginListener = onReviceLoginListener;
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
                        if (ss.length != 12 || ss[1] == 32) {
                            if (ss.length == 9 && ss[5] == 6) {
                                if (d[5] == (byte) 6 && d[7] == (byte) 1) {
                                    if (this.onReviceLoginListener != null) {
                                        this.onReviceLoginListener.loginApp(1);
                                    }
                                } else if (d[5] == (byte) 6 && d[7] == (byte) 0 && this.onReviceLoginListener != null) {
                                    this.onReviceLoginListener.loginApp(0);
                                }
                            } else if (ss.length == 9 && ss[5] == 5) {
                                if (d[5] == (byte) 5 && d[7] == (byte) 1) {
                                    if (this.onReviceLoginListener != null) {
                                        this.onReviceLoginListener.loginApp(1);
                                    }
                                } else if (d[5] == (byte) 5 && d[7] == (byte) 0 && this.onReviceLoginListener != null) {
                                    this.onReviceLoginListener.loginApp(0);
                                }
                            } else if (ss.length == 10 && ss[4] == 2 && ss[5] == 0 && ss[6] == 2) {
                                if (ss[8] == 0) {
                                    easyEventTypeHandler = EasyEventTypeHandler.getInstance();
                                    easyEventTypeHandler.setDeviceRegisterOrRemove(ss[7]);
                                    easyEventTypeHandler.setType(4);
                                    EventBus.getDefault().post(easyEventTypeHandler);
                                } else if (ss[8] == 1) {
                                    easyEventTypeHandler = EasyEventTypeHandler.getInstance();
                                    easyEventTypeHandler.setType(3);
                                    easyEventTypeHandler.setDeviceRegisterOrRemove(ss[7]);
                                    EventBus.getDefault().post(easyEventTypeHandler);
                                }
                            } else if (ss.length == 15 && ss[6] == 7) {
                                easyEventTypeHandler = EasyEventTypeHandler.getInstance();
                                easyEventTypeHandler.setType(2);
                                easyEventTypeHandler.setHostTime(new HostTime(ss));
                                EventBus.getDefault().post(easyEventTypeHandler);
                                EventBus.clearCaches();
                            } else if (ss.length == 12 && ss[1] == 32) {
                                EasyEventType eventType;
                                if (ss[7] == 1) {
                                    eventType = new EasyEventType();
                                    eventType.setType(7);
                                    eventType.setTimeID(ss[4]);
                                    EventBus.getDefault().post(eventType);
                                } else if (ss[7] == 0) {
                                    eventType = new EasyEventType();
                                    eventType.setType(8);
                                    eventType.setTimeID(ss[4]);
                                    EventBus.getDefault().post(eventType);
                                }
                            } else if (ss.length == 22 && ss[6] == 14 && ss[5] == 16) {
                                EasyEventType typeHandler = new EasyEventType();
                                typeHandler.setType(9);
                                typeHandler.setModuleID(ss[8]);
                                typeHandler.setKey(ss[14]);
                                EventBus.getDefault().post(typeHandler);
                            } else if (ss.length == 9 && ss[5] == -5 && ss[6] == 1 && ss[7] == 1) {
                                EasyEventTypeHandler eventType2 = EasyEventTypeHandler.getInstance();
                                eventType2.setType(16);
                                EventBus.getDefault().post(eventType2);
                            }
                        } else if (ss[10] == 16) {
                            if (this.instructImpl != null) {
                                this.instructImpl.ammingProce(ss[3], ss[4], ss[7]);
                            }
                        } else if (ss[10] == 0 && ss[4] != 0) {
                            this.instructImpl.lampProce(ss[3], ss[4], ss[7]);
                        } else if (ss[4] == 0 && ss[5] == 0) {
                            Log.i("DD", Arrays.toString(ss));
                            this.onBindListener.onBind(ss[3], ss[7], ss[8]);
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
