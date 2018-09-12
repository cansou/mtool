package com.easyctrl.ldy.net;

import android.content.Context;
import android.util.Log;
import com.easyctrl.iface.OnNioSocketListener;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.ArrayList;
import java.util.Iterator;

public class NioSocket {
    public static final int STATE_CONNECT_FAILED = 16;
    public static final int STATE_CONNECT_SUCCESS = 8;
    private String IP;
    private int PORT;
    private final int STATE_CLOSE = 2;
    private final int STATE_CONNECT_START = 4;
    private final int STATE_CONNECT_WAIT = 32;
    private final int STATE_OPEN = 1;
    private final String TAG = "NioClient";
    private Thread conn = null;
    private Context context;
    private long lastConnTime = 0;
    private final Object lock = new Object();
    private OnNioSocketListener onNioSocketListener;
    ByteBuffer readBuffer = ByteBuffer.allocate(8192);
    public Thread rec = null;
    private ArrayList<Packet> requestQueen = new ArrayList();
    private ISocketResponse respListener;
    Selector selector;
    SocketChannel socketChannel;
    public int state = 4;

    private class Conn implements Runnable {
        private Conn() {
        }

        public void run() {
            SelectionKey selectionKey;
            Log.v("NioClient", "Conn :Start");
            NioSocket.this.state = 4;
            NioSocket.this.selector = SelectorProvider.provider().openSelector();
            NioSocket.this.socketChannel = SocketChannel.open();
            NioSocket.this.socketChannel.configureBlocking(false);
            NioSocket.this.socketChannel.connect(new InetSocketAddress(NioSocket.this.IP, NioSocket.this.PORT));
            NioSocket.this.socketChannel.register(NioSocket.this.selector, 8);
            while (NioSocket.this.state != 2) {
                try {
                    NioSocket.this.selector.select();
                    Iterator<SelectionKey> selectedKeys = NioSocket.this.selector.selectedKeys().iterator();
                    while (selectedKeys.hasNext()) {
                        SelectionKey key = (SelectionKey) selectedKeys.next();
                        selectedKeys.remove();
                        if (key.isValid()) {
                            if (key.isConnectable()) {
                                finishConnection(key);
                            } else if (key.isReadable()) {
                                read(key);
                            } else if (key.isWritable()) {
                                write(key);
                                Log.i("data", "fafafaafeeeeeeeeeee");
                            }
                        }
                    }
                    synchronized (NioSocket.this.lock) {
                        if (NioSocket.this.requestQueen.size() > 0) {
                            NioSocket.this.socketChannel.keyFor(NioSocket.this.selector).interestOps(4);
                        }
                    }
                } catch (Exception e) {
                    try {
                        e.printStackTrace();
                        if (NioSocket.this.socketChannel != null) {
                            selectionKey = NioSocket.this.socketChannel.keyFor(NioSocket.this.selector);
                            if (selectionKey != null) {
                                selectionKey.cancel();
                            }
                            try {
                                NioSocket.this.socketChannel.close();
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                        }
                    } catch (Throwable th) {
                        if (NioSocket.this.socketChannel != null) {
                            selectionKey = NioSocket.this.socketChannel.keyFor(NioSocket.this.selector);
                            if (selectionKey != null) {
                                selectionKey.cancel();
                            }
                            try {
                                NioSocket.this.socketChannel.close();
                            } catch (IOException e12) {
                                e12.printStackTrace();
                            }
                        }
                    }
                }
            }
            if (NioSocket.this.socketChannel != null) {
                selectionKey = NioSocket.this.socketChannel.keyFor(NioSocket.this.selector);
                if (selectionKey != null) {
                    selectionKey.cancel();
                }
                try {
                    NioSocket.this.socketChannel.close();
                } catch (IOException e122) {
                    e122.printStackTrace();
                }
            }
            Log.v("NioClient", "Conn :End");
            NioSocket.this.state = 16;
            if (NioSocket.this.onNioSocketListener != null) {
                NioSocket.this.onNioSocketListener.disconnect();
            }
        }

        private boolean finishConnection(SelectionKey key) throws IOException {
            boolean result = ((SocketChannel) key.channel()).finishConnect();
            if (result) {
                key.interestOps(1);
                NioSocket.this.state = 8;
            }
            Log.v("NioClient", "finishConnection :" + result);
            if (NioSocket.this.onNioSocketListener != null) {
                NioSocket.this.onNioSocketListener.connectSuccess();
            }
            return result;
        }

        private void read(SelectionKey key) throws IOException {
            SocketChannel socketChannel = (SocketChannel) key.channel();
            NioSocket.this.readBuffer.clear();
            int numRead = socketChannel.read(NioSocket.this.readBuffer);
            if (numRead == -1) {
                key.channel().close();
                key.cancel();
                return;
            }
            NioSocket.this.respListener.onSocketResponse(NioSocket.this.readBuffer.array(), numRead);
        }

        private void write(SelectionKey key) throws IOException {
            try {
                SocketChannel socketChannel = (SocketChannel) key.channel();
                synchronized (NioSocket.this.lock) {
                    Iterator<Packet> iter = NioSocket.this.requestQueen.iterator();
                    while (iter.hasNext()) {
                        socketChannel.write(ByteBuffer.wrap(((Packet) iter.next()).getPacket()));
                        iter.remove();
                        Log.i("data", "fafafafaa");
                    }
                }
                key.interestOps(1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setOnNioSocketListener(OnNioSocketListener onNioSocketListener) {
        this.onNioSocketListener = onNioSocketListener;
    }

    public int send(Packet in) {
        synchronized (this.lock) {
            this.requestQueen.add(in);
        }
        this.selector.wakeup();
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

    public NioSocket(Context context, ISocketResponse respListener) {
        this.context = context;
        this.respListener = respListener;
    }

    public boolean isSocketConnected() {
        return this.state == 8 && this.rec != null && this.rec.isAlive();
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
        if (System.currentTimeMillis() - this.lastConnTime >= 300) {
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
                    if (this.conn != null && this.conn.isAlive()) {
                        this.conn.interrupt();
                    }
                    this.conn = null;
                } catch (Exception e) {
                    e.printStackTrace();
                    this.conn = null;
                } catch (Throwable th) {
                    this.conn = null;
                }
                try {
                    if (this.rec != null && this.rec.isAlive()) {
                        this.rec.interrupt();
                    }
                    this.rec = null;
                } catch (Exception e2) {
                    e2.printStackTrace();
                    this.rec = null;
                } catch (Throwable th2) {
                    this.rec = null;
                }
                this.state = 2;
            }
            this.requestQueen.clear();
        } catch (Exception e22) {
            e22.printStackTrace();
        }
    }
}
