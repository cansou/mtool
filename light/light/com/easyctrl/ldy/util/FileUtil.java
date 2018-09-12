package com.easyctrl.ldy.util;

import android.content.Context;
import android.os.Environment;
import com.easyctrl.ldy.activity.MainApplication;
import com.easyctrl.ldy.net.SendDataSocket;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.http.util.EncodingUtils;

public final class FileUtil {
    public static final void writerData(String fileName, String content, Context context) {
        try {
            FileOutputStream fos = context.openFileOutput(fileName, 0);
            fos.write(content.getBytes());
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static final void writeData(byte[] content, String path, String saveName) {
        Exception e;
        Throwable th;
        File file = new File(new StringBuilder(String.valueOf(path)).append("/").append(saveName).toString());
        try {
            file.createNewFile();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        FileOutputStream fos = null;
        try {
            FileOutputStream fos2 = new FileOutputStream(file);
            try {
                fos2.write(content);
                fos2.flush();
                if (fos2 != null) {
                    try {
                        fos2.close();
                        fos = fos2;
                        return;
                    } catch (IOException e2) {
                        e2.printStackTrace();
                    }
                }
                fos = fos2;
            } catch (Exception e3) {
                e = e3;
                fos = fos2;
                try {
                    e.printStackTrace();
                    if (fos != null) {
                        try {
                            fos.close();
                        } catch (IOException e22) {
                            e22.printStackTrace();
                        }
                    }
                } catch (Throwable th2) {
                    th = th2;
                    if (fos != null) {
                        try {
                            fos.close();
                        } catch (IOException e222) {
                            e222.printStackTrace();
                        }
                    }
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                fos = fos2;
                if (fos != null) {
                    fos.close();
                }
                throw th;
            }
        } catch (Exception e4) {
            e = e4;
            e.printStackTrace();
            if (fos != null) {
                fos.close();
            }
        }
    }

    public static final void writeDataDB(byte[] content) {
        Exception e;
        Throwable th;
        File file = new File("/data/data/com.easyctrl.ldy.activity/databases/ET_APP_NAME.DB");
        try {
            file.createNewFile();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        FileOutputStream fos = null;
        try {
            FileOutputStream fos2 = new FileOutputStream(file);
            try {
                fos2.write(content);
                fos2.flush();
                if (fos2 != null) {
                    try {
                        fos2.close();
                        fos = fos2;
                        return;
                    } catch (IOException e2) {
                        e2.printStackTrace();
                    }
                }
                fos = fos2;
            } catch (Exception e3) {
                e = e3;
                fos = fos2;
                try {
                    e.printStackTrace();
                    if (fos != null) {
                        try {
                            fos.close();
                        } catch (IOException e22) {
                            e22.printStackTrace();
                        }
                    }
                } catch (Throwable th2) {
                    th = th2;
                    if (fos != null) {
                        try {
                            fos.close();
                        } catch (IOException e222) {
                            e222.printStackTrace();
                        }
                    }
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                fos = fos2;
                if (fos != null) {
                    fos.close();
                }
                throw th;
            }
        } catch (Exception e4) {
            e = e4;
            e.printStackTrace();
            if (fos != null) {
                fos.close();
            }
        }
    }

    public static final void writeDataDBRoot(byte[] content) {
        Exception e;
        Throwable th;
        File file = new File("/data/data/com.easyctrl.ldy.activity/databases/ET_APP_NAME.DB");
        try {
            file.createNewFile();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        FileOutputStream fos = null;
        try {
            FileOutputStream fos2 = new FileOutputStream(file);
            try {
                fos2.write(content);
                fos2.flush();
                if (fos2 != null) {
                    try {
                        fos2.close();
                        fos = fos2;
                        return;
                    } catch (IOException e2) {
                        e2.printStackTrace();
                    }
                }
                fos = fos2;
            } catch (Exception e3) {
                e = e3;
                fos = fos2;
                try {
                    e.printStackTrace();
                    if (fos != null) {
                        try {
                            fos.close();
                        } catch (IOException e22) {
                            e22.printStackTrace();
                        }
                    }
                } catch (Throwable th2) {
                    th = th2;
                    if (fos != null) {
                        try {
                            fos.close();
                        } catch (IOException e222) {
                            e222.printStackTrace();
                        }
                    }
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                fos = fos2;
                if (fos != null) {
                    fos.close();
                }
                throw th;
            }
        } catch (Exception e4) {
            e = e4;
            e.printStackTrace();
            if (fos != null) {
                fos.close();
            }
        }
    }

    public static final String readData(String fileName, Context context) {
        String res = "";
        try {
            FileInputStream fis = context.openFileInput(fileName);
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            res = EncodingUtils.getString(buffer, ZipUtil.ENCODING_TYPE);
            fis.close();
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            return res;
        }
    }

    public static boolean checkFileisExists(String filaName) {
        if (new File("/data/data/com.easyctrl.areademo/files/" + filaName).exists()) {
            return true;
        }
        return false;
    }

    public static boolean deleteFile(String fileName) {
        File file = new File("/data/data/com.easyctrl.areademo/files/" + fileName);
        if (file.exists()) {
            return file.delete();
        }
        return false;
    }

    public static boolean deleteFileByPath(String path) {
        if (new File(path).exists()) {
            deleteDirectory(path);
        }
        return false;
    }

    public static boolean delFileByPath(String sPath) {
        File file = new File(sPath);
        if (!file.isFile() || !file.exists()) {
            return false;
        }
        file.delete();
        return true;
    }

    public static boolean deleteDirectory(String sPath) {
        if (!sPath.endsWith(File.separator)) {
            sPath = new StringBuilder(String.valueOf(sPath)).append(File.separator).toString();
        }
        File dirFile = new File(sPath);
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
        boolean flag = true;
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) {
                flag = delFileByPath(files[i].getAbsolutePath());
                if (!flag) {
                    break;
                }
            } else {
                flag = deleteDirectory(files[i].getAbsolutePath());
                if (!flag) {
                    break;
                }
            }
        }
        if (flag && dirFile.delete()) {
            return true;
        }
        return false;
    }

    public static byte[] loadDB(String path) {
        Exception e;
        Throwable th;
        byte[] data = null;
        File file = new File(path);
        InputStream fis = null;
        try {
            InputStream fis2 = new FileInputStream(file);
            int offset = 0;
            try {
                data = new byte[((int) file.length())];
                while (data.length > 0 && fis2.read(data, offset, data.length + 0) >= 0) {
                    offset = 0 + 0;
                }
                int length = data.length;
                if (fis2 != null) {
                    try {
                        fis2.close();
                        fis = fis2;
                    } catch (IOException e2) {
                        e2.printStackTrace();
                    }
                    return data;
                }
                fis = fis2;
            } catch (Exception e3) {
                e = e3;
                fis = fis2;
                try {
                    e.printStackTrace();
                    if (fis != null) {
                        try {
                            fis.close();
                        } catch (IOException e22) {
                            e22.printStackTrace();
                        }
                    }
                    return data;
                } catch (Throwable th2) {
                    th = th2;
                    if (fis != null) {
                        try {
                            fis.close();
                        } catch (IOException e222) {
                            e222.printStackTrace();
                        }
                    }
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                fis = fis2;
                if (fis != null) {
                    fis.close();
                }
                throw th;
            }
        } catch (Exception e4) {
            e = e4;
            e.printStackTrace();
            if (fis != null) {
                fis.close();
            }
            return data;
        }
        return data;
    }

    public static void recoveryDBss(String path, byte[] data) {
        Exception e;
        Throwable th;
        FileOutputStream fis = null;
        try {
            FileOutputStream fis2 = new FileOutputStream(path);
            try {
                fis2.write(data);
                if (fis2 != null) {
                    try {
                        fis2.close();
                        fis = fis2;
                        return;
                    } catch (IOException e2) {
                        e2.printStackTrace();
                    }
                }
                fis = fis2;
            } catch (Exception e3) {
                e = e3;
                fis = fis2;
                try {
                    e.printStackTrace();
                    if (fis != null) {
                        try {
                            fis.close();
                        } catch (IOException e22) {
                            e22.printStackTrace();
                        }
                    }
                } catch (Throwable th2) {
                    th = th2;
                    if (fis != null) {
                        try {
                            fis.close();
                        } catch (IOException e222) {
                            e222.printStackTrace();
                        }
                    }
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                fis = fis2;
                if (fis != null) {
                    fis.close();
                }
                throw th;
            }
        } catch (Exception e4) {
            e = e4;
            e.printStackTrace();
            if (fis != null) {
                fis.close();
            }
        }
    }

    public static void backupData() throws Exception {
        byte[] data = loadDB("/data/data/com.easyctrl.ldy.activity/databases/ET_APP_NAME.DB");
        SendDataSocket sendDataSocket = new SendDataSocket(MainApplication.userManager.currentHost(), 6001);
        sendDataSocket.sendData("down /backup/ET_APP_NAME.DB$".getBytes());
        sendDataSocket.sendData(data);
        sendDataSocket.close();
    }

    public static void backupDatatoServerName(String path, String name) throws Exception {
        byte[] data = loadDB(new StringBuilder(String.valueOf(path)).append("/").append(name).toString());
        SendDataSocket sendDataSocket = new SendDataSocket(MainApplication.userManager.currentHost(), 6001);
        sendDataSocket.sendData("down /backup/ET_APP_NAME.DB$".getBytes());
        sendDataSocket.sendData(data);
        sendDataSocket.close();
    }

    public static void backupDatatoServerDATA(String path, String name) throws Exception {
        byte[] data = loadDB(new StringBuilder(String.valueOf(path)).append("/").append(name).toString());
        SendDataSocket sendDataSocket = new SendDataSocket(MainApplication.userManager.currentHost(), 6001);
        sendDataSocket.sendData("down /backup/ET_APP_DATA.DB$".getBytes());
        sendDataSocket.sendData(data);
        sendDataSocket.close();
    }

    public static boolean iSExistSDCard() {
        if (Environment.getExternalStorageState().equals("mounted")) {
            return true;
        }
        return false;
    }
}
