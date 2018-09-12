package org.apache.tools.zip;

import android.support.v4.internal.view.SupportMenu;
import android.support.v4.view.MotionEventCompat;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class Simple8BitZipEncoding implements ZipEncoding {
    private final char[] highChars;
    private final List reverseMapping = new ArrayList(this.highChars.length);

    private static final class Simple8BitChar implements Comparable {
        public final byte code;
        public final char unicode;

        Simple8BitChar(byte code, char unicode) {
            this.code = code;
            this.unicode = unicode;
        }

        public int compareTo(Object o) {
            return this.unicode - ((Simple8BitChar) o).unicode;
        }

        public String toString() {
            return "0x" + Integer.toHexString(SupportMenu.USER_MASK & this.unicode) + "->0x" + Integer.toHexString(this.code & MotionEventCompat.ACTION_MASK);
        }
    }

    public Simple8BitZipEncoding(char[] highChars) {
        this.highChars = highChars;
        byte code = Byte.MAX_VALUE;
        for (char simple8BitChar : this.highChars) {
            code = (byte) (code + 1);
            this.reverseMapping.add(new Simple8BitChar(code, simple8BitChar));
        }
        Collections.sort(this.reverseMapping);
    }

    public char decodeByte(byte b) {
        if (b >= (byte) 0) {
            return (char) b;
        }
        return this.highChars[b + 128];
    }

    public boolean canEncodeChar(char c) {
        if ((c < '\u0000' || c >= '\u0080') && encodeHighChar(c) == null) {
            return false;
        }
        return true;
    }

    public boolean pushEncodedChar(ByteBuffer bb, char c) {
        if (c < '\u0000' || c >= '\u0080') {
            Simple8BitChar r = encodeHighChar(c);
            if (r == null) {
                return false;
            }
            bb.put(r.code);
            return true;
        }
        bb.put((byte) c);
        return true;
    }

    private Simple8BitChar encodeHighChar(char c) {
        int i0 = 0;
        int i1 = this.reverseMapping.size();
        while (i1 > i0) {
            int i = i0 + ((i1 - i0) / 2);
            Simple8BitChar m = (Simple8BitChar) this.reverseMapping.get(i);
            if (m.unicode == c) {
                return m;
            }
            if (m.unicode < c) {
                i0 = i + 1;
            } else {
                i1 = i;
            }
        }
        if (i0 >= this.reverseMapping.size()) {
            return null;
        }
        Simple8BitChar r = (Simple8BitChar) this.reverseMapping.get(i0);
        if (r.unicode != c) {
            return null;
        }
        return r;
    }

    public boolean canEncode(String name) {
        for (int i = 0; i < name.length(); i++) {
            if (!canEncodeChar(name.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public ByteBuffer encode(String name) {
        ByteBuffer out = ByteBuffer.allocate((name.length() + 6) + ((name.length() + 1) / 2));
        for (int i = 0; i < name.length(); i++) {
            char c = name.charAt(i);
            if (out.remaining() < 6) {
                out = ZipEncodingHelper.growBuffer(out, out.position() + 6);
            }
            if (!pushEncodedChar(out, c)) {
                ZipEncodingHelper.appendSurrogate(out, c);
            }
        }
        out.limit(out.position());
        out.rewind();
        return out;
    }

    public String decode(byte[] data) throws IOException {
        char[] ret = new char[data.length];
        for (int i = 0; i < data.length; i++) {
            ret[i] = decodeByte(data[i]);
        }
        return new String(ret);
    }
}
