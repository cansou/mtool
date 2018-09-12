package com.easyctrl.ldy.net;

import android.content.Context;
import android.util.Log;
import com.easyctrl.ldy.util.FileUtil;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class SocketUtil {
    private Context context;
    private InputStream in;
    private String ip;
    private OutputStream out;
    private int port;
    private Socket socket;

    public void connect() throws Exception {
        this.socket = new Socket();
        this.socket.connect(new InetSocketAddress(this.ip, this.port), 2000);
    }

    public SocketUtil(String ip, int port, Context context) {
        this.ip = ip;
        this.port = port;
        this.context = context;
        try {
            connect();
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("data", "SocketUtil socket = " + e.getMessage());
        }
        try {
            if (this.socket != null && !this.socket.isClosed()) {
                this.in = this.socket.getInputStream();
            }
        } catch (IOException e2) {
            e2.printStackTrace();
            Log.i("data", "SocketUtil socket = " + e2.getMessage());
        }
    }

    public void sendData(String order) throws Exception {
        this.out = this.socket.getOutputStream();
        this.out.write(order.getBytes());
    }

    public byte[] readStream(InputStream in) throws IOException {
        int count = 0;
        while (count == 0) {
            count = in.available();
        }
        byte[] buf = new byte[count];
        in.available();
        in.read(buf);
        return buf;
    }

    public void readData(String saveName) {
        byte[] buf = new byte[1024];
        try {
            String str = "";
            while (true) {
                int len = this.in.read(buf);
                if (len == -1) {
                    break;
                }
                str = new StringBuilder(String.valueOf(str)).append(new String(buf, 0, len)).toString();
            }
            FileUtil.writerData(saveName, str, this.context);
            try {
                if (this.in != null) {
                    this.in.close();
                }
                if (this.socket != null) {
                    this.socket.close();
                }
                this.in = null;
                this.socket = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e2) {
            e2.printStackTrace();
            try {
                if (this.in != null) {
                    this.in.close();
                }
                if (this.socket != null) {
                    this.socket.close();
                }
                this.in = null;
                this.socket = null;
            } catch (IOException e22) {
                e22.printStackTrace();
            }
        } catch (Throwable th) {
            try {
                if (this.in != null) {
                    this.in.close();
                }
                if (this.socket != null) {
                    this.socket.close();
                }
                this.in = null;
                this.socket = null;
            } catch (IOException e222) {
                e222.printStackTrace();
            }
            throw th;
        }
    }

    public boolean downDB(String path, String savename) {
        boolean isSave = false;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        while (true) {
            int len = this.in.read(buffer);
            if (len == -1) {
                break;
            }
            try {
                bos.write(buffer, 0, len);
            } catch (IOException e) {
                e.printStackTrace();
                if (bos != null) {
                    try {
                        bos.close();
                    } catch (IOException e2) {
                        e2.printStackTrace();
                    }
                }
                if (this.in != null) {
                    this.in.close();
                }
                if (this.socket != null) {
                    this.socket.close();
                }
            } catch (Throwable th) {
                if (bos != null) {
                    try {
                        bos.close();
                    } catch (IOException e22) {
                        e22.printStackTrace();
                    }
                }
                if (this.in != null) {
                    this.in.close();
                }
                if (this.socket != null) {
                    this.socket.close();
                }
            }
        }
        byte[] data = bos.toByteArray();
        if (data.length == 0) {
            isSave = true;
        } else {
            FileUtil.writeData(data, path, savename);
            isSave = false;
        }
        if (bos != null) {
            try {
                bos.close();
            } catch (IOException e222) {
                e222.printStackTrace();
            }
        }
        if (this.in != null) {
            this.in.close();
        }
        if (this.socket != null) {
            this.socket.close();
        }
        return isSave;
    }

    public boolean saveDB() {
        boolean isSave = false;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        while (true) {
            int len = this.in.read(buffer);
            if (len == -1) {
                break;
            }
            try {
                bos.write(buffer, 0, len);
            } catch (IOException e) {
                e.printStackTrace();
                if (bos != null) {
                    try {
                        bos.close();
                    } catch (IOException e2) {
                        e2.printStackTrace();
                    }
                }
                if (this.in != null) {
                    this.in.close();
                }
                if (this.socket != null) {
                    this.socket.close();
                }
            } catch (Throwable th) {
                if (bos != null) {
                    try {
                        bos.close();
                    } catch (IOException e22) {
                        e22.printStackTrace();
                    }
                }
                if (this.in != null) {
                    this.in.close();
                }
                if (this.socket != null) {
                    this.socket.close();
                }
            }
        }
        byte[] data = bos.toByteArray();
        if (data.length == 0) {
            isSave = true;
        } else {
            FileUtil.writeDataDB(data);
            isSave = false;
        }
        if (bos != null) {
            try {
                bos.close();
            } catch (IOException e222) {
                e222.printStackTrace();
            }
        }
        if (this.in != null) {
            this.in.close();
        }
        if (this.socket != null) {
            this.socket.close();
        }
        return isSave;
    }
}
