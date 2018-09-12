package com.easyctrl.manager;

import android.net.wifi.WifiManager.MulticastLock;
import com.easyctrl.ldy.domain.HostBean;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;

public class HostManager {
    private static HostManager instance;
    private ArrayList<HostBean> hosts = new ArrayList();
    private OnLineListenerHost onLineListenerHost;

    public interface OnLineListenerHost {
        void onLine(String str);
    }

    public void setOnLineListenerHost(OnLineListenerHost onLineListenerHost) {
        this.onLineListenerHost = onLineListenerHost;
    }

    private HostManager() {
    }

    public static synchronized HostManager getInstance() {
        HostManager hostManager;
        synchronized (HostManager.class) {
            if (instance == null) {
                instance = new HostManager();
            }
            hostManager = instance;
        }
        return hostManager;
    }

    public void getIPinfo(MulticastLock lock) throws IOException {
        this.hosts = new ArrayList();
        DatagramSocket dgSocket = new DatagramSocket();
        dgSocket.setSoTimeout(1000);
        byte[] buf = "search all".getBytes();
        dgSocket.send(new DatagramPacket(buf, buf.length, InetAddress.getByName("255.255.255.255"), 6002));
        while (!dgSocket.isClosed()) {
            lock.acquire();
            byte[] receive = new byte[1024];
            DatagramPacket receivePacket = new DatagramPacket(receive, receive.length);
            dgSocket.receive(receivePacket);
            int len = receivePacket.getLength();
            if (this.onLineListenerHost != null) {
                this.onLineListenerHost.onLine(new String(receive, 0, len));
            }
            lock.release();
        }
    }

    public void udpateIP(byte[] update) {
        try {
            DatagramSocket dgSocket = new DatagramSocket();
            dgSocket.setSoTimeout(10000);
            dgSocket.send(new DatagramPacket(update, update.length, InetAddress.getByName("255.255.255.255"), 6002));
            while (!dgSocket.isClosed()) {
                byte[] receive = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(receive, receive.length);
                dgSocket.receive(receivePacket);
                int len = receivePacket.getLength();
                if (this.onLineListenerHost != null) {
                    this.onLineListenerHost.onLine(new String(receive, 0, len));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<HostBean> analyzeToBean(String ipInfo) {
        if (ipInfo == null) {
            return null;
        }
        String[] s = ipInfo.split(" ");
        for (int j = 0; j < s.length / 6; j++) {
            HostBean b = new HostBean();
            b.devHostName = "\u4e3b\u673a " + (j + 1);
            for (int i = j * 6; i < (j + 1) * 6; i++) {
                String content = s[i].substring(s[i].indexOf("=") + 1, s[i].length());
                if (s[i].startsWith("devIP")) {
                    b.devIP = content;
                } else if (s[i].startsWith("devMask")) {
                    b.devMask = content;
                } else if (s[i].startsWith("devRoute")) {
                    b.devRoute = content;
                } else if (s[i].startsWith("devDNS")) {
                    b.devDNS = content;
                } else if (s[i].startsWith("devMac8")) {
                    b.devMac8 = s[i];
                } else if (s[i].startsWith("devMac")) {
                    b.devMac = content;
                }
            }
            this.hosts.add(b);
        }
        return this.hosts;
    }
}
