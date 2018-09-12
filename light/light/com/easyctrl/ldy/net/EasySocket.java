package com.easyctrl.ldy.net;

import android.content.Context;
import android.util.Log;
import com.easyctrl.iface.OnNioSocketListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.Iterator;
import java.util.concurrent.LinkedBlockingQueue;

public class EasySocket {
    public static final int STATE_CLOSE = 2;
    public static final int STATE_CONNECT_FAILED = 16;
    public static final int STATE_CONNECT_START = 4;
    public static final int STATE_CONNECT_SUCCESS = 8;
    public static final int STATE_CONNECT_WAIT = 32;
    public static final int STATE_OPEN = 1;
    private String IP = "192.168.1.230";
    private int PORT = 5000;
    private final String TAG = "Client";
    private Thread conn = null;
    private Context context;
    private InputStream inStream = null;
    private long lastConnTime = 0;
    private final Object lock = new Object();
    private OnNioSocketListener onNioSocketListener;
    private OutputStream outStream = null;
    private Thread rec = null;
    private LinkedBlockingQueue<Packet> requestQueen = new LinkedBlockingQueue();
    private IEasySocketResponse respListener;
    private Thread send = null;
    private Socket socket = null;
    public int state = 4;

    private class Conn implements Runnable {
        private Conn() {
        }

        public void run() {
            while (EasySocket.this.state != 2) {
                try {
                    EasySocket.this.state = 4;
                    EasySocket.this.socket = new Socket();
                    EasySocket.this.socket.connect(new InetSocketAddress(EasySocket.this.IP, EasySocket.this.PORT), 5000);
                    EasySocket.this.socket.setKeepAlive(true);
                    EasySocket.this.state = 8;
                    EasySocket.this.onNioSocketListener.connectSuccess();
                } catch (Exception e) {
                    try {
                        EasySocket.this.onNioSocketListener.connectError();
                        EasySocket.this.state = 16;
                        e.printStackTrace();
                    } catch (Exception e2) {
                        e2.printStackTrace();
                        return;
                    }
                }
                if (EasySocket.this.state == 8) {
                    try {
                        EasySocket.this.outStream = EasySocket.this.socket.getOutputStream();
                        EasySocket.this.inStream = EasySocket.this.socket.getInputStream();
                    } catch (IOException e3) {
                        e3.printStackTrace();
                    }
                    EasySocket.this.send = new Thread(new Send());
                    EasySocket.this.rec = new Thread(new Rec());
                    EasySocket.this.send.start();
                    EasySocket.this.rec.start();
                    return;
                }
                EasySocket.this.state = 32;
                if (NetworkUtil.isNetworkAvailable(EasySocket.this.context)) {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e4) {
                        e4.printStackTrace();
                        EasySocket.this.onNioSocketListener.disconnect();
                        return;
                    }
                }
                return;
            }
        }
    }

    private class Rec implements Runnable {
        private Rec() {
        }

        public void run() {
            try {
                if (EasySocket.this.state != 2 && EasySocket.this.state == 8 && EasySocket.this.inStream != null) {
                    Log.v("Client", "Rec :---------");
                    byte[] bodyBytes = new byte[128];
                    while (true) {
                        int read = EasySocket.this.inStream.read(bodyBytes);
                        if (read <= 0) {
                            EasySocket.this.onNioSocketListener.disconnect();
                            EasySocket.this.reconn();
                            return;
                        }
                        if (EasySocket.this.respListener != null) {
                            EasySocket.this.respListener.onSocketResponse(bodyBytes, read);
                        }
                        EasySocket.this.socket.getKeepAlive();
                    }
                }
            } catch (SocketException e1) {
                e1.printStackTrace();
                EasySocket.this.onNioSocketListener.disconnect();
                Log.i("data", "onNioSocketListener.disconnect");
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    private class Send implements Runnable {
        private Send() {
        }

        public void run() {
            Log.v("Client", "Send :Start");
            while (EasySocket.this.state != 2 && EasySocket.this.state == 8 && EasySocket.this.outStream != null) {
                try {
                    while (true) {
                        Packet item = (Packet) EasySocket.this.requestQueen.poll();
                        if (item == null) {
                            break;
                        }
                        EasySocket.this.outStream.write(item.getPacket());
                        EasySocket.this.outStream.flush();
                    }
                    synchronized (EasySocket.this.lock) {
                        EasySocket.this.lock.wait();
                    }
                } catch (SocketException e1) {
                    e1.printStackTrace();
                    EasySocket.this.reconn();
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
        }
    }

    public void setOnNioSocketListener(OnNioSocketListener onNioSocketListener) {
        this.onNioSocketListener = onNioSocketListener;
    }

    public int send(Packet in) {
        this.requestQueen.add(in);
        synchronized (this.lock) {
            this.lock.notifyAll();
        }
        return in.getId();
    }

    public void cancel(int reqId) {
        Iterator<Packet> mIterator = this.requestQueen.iterator();
        while (mIterator.hasNext()) {
            if (((Packet) mIterator.next()).getId() == reqId) {
                mIterator.remove();
            }
        }
    }

    public EasySocket(Context context, IEasySocketResponse respListener) {
        this.context = context;
        this.respListener = respListener;
    }

    public boolean isNeedConn() {
        return (this.state == 8 && this.send != null && this.send.isAlive() && this.rec != null && this.rec.isAlive()) ? false : true;
    }

    public void open() {
        reconn();
    }

    public void open(String host, int port) {
        this.IP = host;
        this.PORT = port;
        reconn();
    }

    public synchronized void reconn() {
        if (System.currentTimeMillis() - this.lastConnTime >= 2000) {
            this.lastConnTime = System.currentTimeMillis();
            close();
            this.state = 1;
            this.conn = new Thread(new Conn());
            this.conn.start();
        }
    }

    public synchronized void close() {
        try {
            if (this.state != 2) {
                try {
                    if (this.socket != null) {
                        this.socket.close();
                    }
                    this.socket = null;
                } catch (Exception e) {
                    e.printStackTrace();
                    this.socket = null;
                } catch (Throwable th) {
                    this.socket = null;
                }
                try {
                    if (this.outStream != null) {
                        this.outStream.close();
                    }
                    this.outStream = null;
                } catch (Exception e2) {
                    e2.printStackTrace();
                    this.outStream = null;
                } catch (Throwable th2) {
                    this.outStream = null;
                }
                try {
                    if (this.inStream != null) {
                        this.inStream.close();
                    }
                    this.inStream = null;
                } catch (Exception e22) {
                    e22.printStackTrace();
                    this.inStream = null;
                } catch (Throwable th3) {
                    this.inStream = null;
                }
                try {
                    if (this.conn != null && this.conn.isAlive()) {
                        this.conn.interrupt();
                    }
                    this.conn = null;
                } catch (Exception e222) {
                    e222.printStackTrace();
                    this.conn = null;
                } catch (Throwable th4) {
                    this.conn = null;
                }
                try {
                    if (this.send != null && this.send.isAlive()) {
                        this.send.interrupt();
                    }
                    this.send = null;
                } catch (Exception e2222) {
                    e2222.printStackTrace();
                    this.send = null;
                } catch (Throwable th5) {
                    this.send = null;
                }
                try {
                    if (this.rec != null && this.rec.isAlive()) {
                        this.rec.interrupt();
                    }
                    this.rec = null;
                } catch (Exception e22222) {
                    e22222.printStackTrace();
                    this.rec = null;
                } catch (Throwable th6) {
                    this.rec = null;
                }
                this.state = 2;
            }
            this.requestQueen.clear();
        } catch (Exception e222222) {
            e222222.printStackTrace();
        }
    }
}
