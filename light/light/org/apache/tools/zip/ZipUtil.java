package org.apache.tools.zip;

public abstract class ZipUtil {
    static byte[] copy(byte[] from) {
        if (from == null) {
            return null;
        }
        byte[] to = new byte[from.length];
        System.arraycopy(from, 0, to, 0, to.length);
        return to;
    }
}
