package com.easyctrl.ldy.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;

public class ZipUtil {
    public static final String ENCODING_TYPE = "UTF-8";

    public static void doZip(String srcFile, String destFile) {
        IOException ioe;
        Throwable th;
        ZipOutputStream zipOut = null;
        File zipDir = new File(srcFile);
        String dirName = zipDir.getName();
        try {
            ZipOutputStream zipOut2 = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(destFile)));
            try {
                zipOut2.setComment("comment");
                zipOut2.setEncoding(ENCODING_TYPE);
                zipOut2.setMethod(8);
                zipOut2.setLevel(9);
                handleDir(zipDir, zipOut2, dirName);
                if (zipOut2 != null) {
                    try {
                        zipOut2.close();
                        zipOut = zipOut2;
                        return;
                    } catch (Exception e) {
                        zipOut = zipOut2;
                        return;
                    }
                }
            } catch (IOException e2) {
                ioe = e2;
                zipOut = zipOut2;
                try {
                    ioe.printStackTrace();
                    if (zipOut != null) {
                        try {
                            zipOut.close();
                        } catch (Exception e3) {
                        }
                    }
                } catch (Throwable th2) {
                    th = th2;
                    if (zipOut != null) {
                        try {
                            zipOut.close();
                        } catch (Exception e4) {
                        }
                    }
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                zipOut = zipOut2;
                if (zipOut != null) {
                    zipOut.close();
                }
                throw th;
            }
        } catch (IOException e5) {
            ioe = e5;
            ioe.printStackTrace();
            if (zipOut != null) {
                zipOut.close();
            }
        }
    }

    private static void handleDir(File dir, ZipOutputStream zipOut, String dirName) throws IOException {
        byte[] buffer = new byte[1024];
        File[] files = dir.listFiles();
        if (files.length == 0) {
            zipOut.putNextEntry(new ZipEntry(dirName));
            zipOut.closeEntry();
            return;
        }
        for (File fileName : files) {
            if (fileName.isDirectory()) {
                handleDir(fileName, zipOut, new StringBuilder(String.valueOf(dirName)).append(File.separator).append(fileName.getName()).append(File.separator).toString());
            } else {
                FileInputStream fileIn = new FileInputStream(fileName);
                zipOut.putNextEntry(new ZipEntry(new StringBuilder(String.valueOf(dirName)).append(File.separator).append(fileName.getName()).toString()));
                while (true) {
                    int length = fileIn.read(buffer);
                    if (length <= 0) {
                        break;
                    }
                    zipOut.write(buffer, 0, length);
                }
                zipOut.closeEntry();
            }
        }
    }

    public static void unZip(String srcZipfilePath, String destFolderPath) {
        IOException ioe;
        ZipFile zipFile;
        Throwable th;
        byte[] buffer = new byte[1024];
        FileOutputStream fileOut = null;
        InputStream inputStream = null;
        try {
            ZipFile mZipFile = new ZipFile(srcZipfilePath);
            try {
                Enumeration entries = mZipFile.getEntries();
                FileOutputStream fileOut2 = null;
                while (entries.hasMoreElements()) {
                    try {
                        ZipEntry entry = (ZipEntry) entries.nextElement();
                        File file = new File(new StringBuilder(String.valueOf(destFolderPath)).append(File.separator).append(entry.getName()).toString());
                        if (entry.isDirectory()) {
                            file.mkdirs();
                        } else {
                            File parent = file.getParentFile();
                            if (!parent.exists()) {
                                parent.mkdirs();
                            }
                            inputStream = mZipFile.getInputStream(entry);
                            fileOut = new FileOutputStream(file);
                            while (true) {
                                int length = inputStream.read(buffer);
                                if (length <= 0) {
                                    break;
                                }
                                fileOut.write(buffer, 0, length);
                            }
                            fileOut2 = fileOut;
                        }
                    } catch (IOException e) {
                        ioe = e;
                        fileOut = fileOut2;
                        zipFile = mZipFile;
                    } catch (Throwable th2) {
                        th = th2;
                        fileOut = fileOut2;
                        zipFile = mZipFile;
                    }
                }
                mZipFile.close();
                if (fileOut2 != null) {
                    try {
                        fileOut2.close();
                    } catch (IOException e2) {
                        e2.printStackTrace();
                    }
                }
                if (inputStream != null) {
                    inputStream.close();
                    fileOut = fileOut2;
                    zipFile = mZipFile;
                    return;
                }
                fileOut = fileOut2;
                zipFile = mZipFile;
            } catch (IOException e3) {
                ioe = e3;
                zipFile = mZipFile;
            } catch (Throwable th3) {
                th = th3;
                zipFile = mZipFile;
            }
        } catch (IOException e4) {
            ioe = e4;
            try {
                ioe.printStackTrace();
                if (fileOut != null) {
                    try {
                        fileOut.close();
                    } catch (IOException e22) {
                        e22.printStackTrace();
                        return;
                    }
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (Throwable th4) {
                th = th4;
                if (fileOut != null) {
                    try {
                        fileOut.close();
                    } catch (IOException e222) {
                        e222.printStackTrace();
                        throw th;
                    }
                }
                if (inputStream != null) {
                    inputStream.close();
                }
                throw th;
            }
        }
    }
}
