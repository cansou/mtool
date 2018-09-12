package com.easyctrl.ldy.net;

import android.util.Log;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class SendDataSocket {
    private String ip;
    private OnCloseListener onCloseListener;
    private OutputStream os;
    private int port;
    private Socket socket;

    public interface OnCloseListener {
        void onClose();
    }

    public void setOnCloseListener(OnCloseListener onCloseListener) {
        this.onCloseListener = onCloseListener;
    }

    public SendDataSocket(String ip, int port) throws Exception {
        this.ip = ip;
        this.port = port;
        connect();
    }

    private void connect() throws Exception {
        this.socket = new Socket(this.ip, this.port);
    }

    public void sendDataConnect(String order) throws Exception {
        try {
            if (this.socket != null && !this.socket.isClosed()) {
                this.os = this.socket.getOutputStream();
                this.os.write(order.getBytes());
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("data", "SendDataSocket Exception:" + e.getMessage());
            throw new Exception();
        }
    }

    public void sendData(String order) throws Exception {
        Log.i("data", "scene file:" + order);
        try {
            if (!(this.socket == null || this.socket.isClosed())) {
                this.os = this.socket.getOutputStream();
                this.os.write(order.getBytes());
            }
            if (this.os != null) {
                try {
                    this.os.close();
                    if (this.onCloseListener != null) {
                        this.onCloseListener.onClose();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e2) {
            e2.printStackTrace();
            Log.i("data", "SendDataSocket Exception:" + e2.getMessage());
            throw new Exception();
        } catch (Throwable th) {
            if (this.os != null) {
                try {
                    this.os.close();
                    if (this.onCloseListener != null) {
                        this.onCloseListener.onClose();
                    }
                } catch (IOException e3) {
                    e3.printStackTrace();
                }
            }
        }
    }

    public void sendData(byte[] data) {
        try {
            if (this.socket != null && !this.socket.isClosed()) {
                this.os = this.socket.getOutputStream();
                this.os.write(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("data", "SendDataSocket Exception:" + e.getMessage());
        }
    }

    public void close() {
        if (this.os != null) {
            try {
                this.os.close();
                if (this.onCloseListener != null) {
                    this.onCloseListener.onClose();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (this.socket != null) {
            try {
                this.socket.close();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
    }
}
