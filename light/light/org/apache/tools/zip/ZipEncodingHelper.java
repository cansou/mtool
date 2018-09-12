package org.apache.tools.zip;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.HashMap;
import java.util.Map;

abstract class ZipEncodingHelper {
    private static final byte[] HEX_DIGITS = new byte[]{(byte) 48, (byte) 49, (byte) 50, (byte) 51, (byte) 52, (byte) 53, (byte) 54, (byte) 55, (byte) 56, (byte) 57, (byte) 65, (byte) 66, (byte) 67, (byte) 68, (byte) 69, (byte) 70};
    static final String UTF8 = "UTF8";
    static final ZipEncoding UTF8_ZIP_ENCODING = new FallbackZipEncoding(UTF8);
    private static final String UTF_DASH_8 = "utf-8";
    private static final Map simpleEncodings = new HashMap();

    private static class SimpleEncodingHolder {
        private Simple8BitZipEncoding encoding;
        private final char[] highChars;

        SimpleEncodingHolder(char[] highChars) {
            this.highChars = highChars;
        }

        public synchronized Simple8BitZipEncoding getEncoding() {
            if (this.encoding == null) {
                this.encoding = new Simple8BitZipEncoding(this.highChars);
            }
            return this.encoding;
        }
    }

    ZipEncodingHelper() {
    }

    static {
        SimpleEncodingHolder cp437 = new SimpleEncodingHolder(new char[]{'\u00c7', '\u00fc', '\u00e9', '\u00e2', '\u00e4', '\u00e0', '\u00e5', '\u00e7', '\u00ea', '\u00eb', '\u00e8', '\u00ef', '\u00ee', '\u00ec', '\u00c4', '\u00c5', '\u00c9', '\u00e6', '\u00c6', '\u00f4', '\u00f6', '\u00f2', '\u00fb', '\u00f9', '\u00ff', '\u00d6', '\u00dc', '\u00a2', '\u00a3', '\u00a5', '\u20a7', '\u0192', '\u00e1', '\u00ed', '\u00f3', '\u00fa', '\u00f1', '\u00d1', '\u00aa', '\u00ba', '\u00bf', '\u2310', '\u00ac', '\u00bd', '\u00bc', '\u00a1', '\u00ab', '\u00bb', '\u2591', '\u2592', '\u2593', '\u2502', '\u2524', '\u2561', '\u2562', '\u2556', '\u2555', '\u2563', '\u2551', '\u2557', '\u255d', '\u255c', '\u255b', '\u2510', '\u2514', '\u2534', '\u252c', '\u251c', '\u2500', '\u253c', '\u255e', '\u255f', '\u255a', '\u2554', '\u2569', '\u2566', '\u2560', '\u2550', '\u256c', '\u2567', '\u2568', '\u2564', '\u2565', '\u2559', '\u2558', '\u2552', '\u2553', '\u256b', '\u256a', '\u2518', '\u250c', '\u2588', '\u2584', '\u258c', '\u2590', '\u2580', '\u03b1', '\u00df', '\u0393', '\u03c0', '\u03a3', '\u03c3', '\u00b5', '\u03c4', '\u03a6', '\u0398', '\u03a9', '\u03b4', '\u221e', '\u03c6', '\u03b5', '\u2229', '\u2261', '\u00b1', '\u2265', '\u2264', '\u2320', '\u2321', '\u00f7', '\u2248', '\u00b0', '\u2219', '\u00b7', '\u221a', '\u207f', '\u00b2', '\u25a0', '\u00a0'});
        simpleEncodings.put("CP437", cp437);
        simpleEncodings.put("Cp437", cp437);
        simpleEncodings.put("cp437", cp437);
        simpleEncodings.put("IBM437", cp437);
        simpleEncodings.put("ibm437", cp437);
        SimpleEncodingHolder cp850 = new SimpleEncodingHolder(new char[]{'\u00c7', '\u00fc', '\u00e9', '\u00e2', '\u00e4', '\u00e0', '\u00e5', '\u00e7', '\u00ea', '\u00eb', '\u00e8', '\u00ef', '\u00ee', '\u00ec', '\u00c4', '\u00c5', '\u00c9', '\u00e6', '\u00c6', '\u00f4', '\u00f6', '\u00f2', '\u00fb', '\u00f9', '\u00ff', '\u00d6', '\u00dc', '\u00f8', '\u00a3', '\u00d8', '\u00d7', '\u0192', '\u00e1', '\u00ed', '\u00f3', '\u00fa', '\u00f1', '\u00d1', '\u00aa', '\u00ba', '\u00bf', '\u00ae', '\u00ac', '\u00bd', '\u00bc', '\u00a1', '\u00ab', '\u00bb', '\u2591', '\u2592', '\u2593', '\u2502', '\u2524', '\u00c1', '\u00c2', '\u00c0', '\u00a9', '\u2563', '\u2551', '\u2557', '\u255d', '\u00a2', '\u00a5', '\u2510', '\u2514', '\u2534', '\u252c', '\u251c', '\u2500', '\u253c', '\u00e3', '\u00c3', '\u255a', '\u2554', '\u2569', '\u2566', '\u2560', '\u2550', '\u256c', '\u00a4', '\u00f0', '\u00d0', '\u00ca', '\u00cb', '\u00c8', '\u0131', '\u00cd', '\u00ce', '\u00cf', '\u2518', '\u250c', '\u2588', '\u2584', '\u00a6', '\u00cc', '\u2580', '\u00d3', '\u00df', '\u00d4', '\u00d2', '\u00f5', '\u00d5', '\u00b5', '\u00fe', '\u00de', '\u00da', '\u00db', '\u00d9', '\u00fd', '\u00dd', '\u00af', '\u00b4', '\u00ad', '\u00b1', '\u2017', '\u00be', '\u00b6', '\u00a7', '\u00f7', '\u00b8', '\u00b0', '\u00a8', '\u00b7', '\u00b9', '\u00b3', '\u00b2', '\u25a0', '\u00a0'});
        simpleEncodings.put("CP850", cp850);
        simpleEncodings.put("Cp850", cp850);
        simpleEncodings.put("cp850", cp850);
        simpleEncodings.put("IBM850", cp850);
        simpleEncodings.put("ibm850", cp850);
    }

    static ByteBuffer growBuffer(ByteBuffer b, int newCapacity) {
        b.limit(b.position());
        b.rewind();
        int c2 = b.capacity() * 2;
        if (c2 >= newCapacity) {
            newCapacity = c2;
        }
        ByteBuffer on = ByteBuffer.allocate(newCapacity);
        on.put(b);
        return on;
    }

    static void appendSurrogate(ByteBuffer bb, char c) {
        bb.put((byte) 37);
        bb.put((byte) 85);
        bb.put(HEX_DIGITS[(c >> 12) & 15]);
        bb.put(HEX_DIGITS[(c >> 8) & 15]);
        bb.put(HEX_DIGITS[(c >> 4) & 15]);
        bb.put(HEX_DIGITS[c & 15]);
    }

    static ZipEncoding getZipEncoding(String name) {
        if (isUTF8(name)) {
            return UTF8_ZIP_ENCODING;
        }
        if (name == null) {
            return new FallbackZipEncoding();
        }
        SimpleEncodingHolder h = (SimpleEncodingHolder) simpleEncodings.get(name);
        if (h != null) {
            return h.getEncoding();
        }
        try {
            return new NioZipEncoding(Charset.forName(name));
        } catch (UnsupportedCharsetException e) {
            return new FallbackZipEncoding(name);
        }
    }

    static boolean isUTF8(String encoding) {
        if (encoding == null) {
            encoding = System.getProperty("file.encoding");
        }
        return UTF8.equalsIgnoreCase(encoding) || UTF_DASH_8.equalsIgnoreCase(encoding);
    }
}
