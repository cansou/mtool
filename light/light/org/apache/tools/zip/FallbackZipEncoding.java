package org.apache.tools.zip;

import java.io.IOException;
import java.nio.ByteBuffer;

class FallbackZipEncoding implements ZipEncoding {
    private final String charset;

    public FallbackZipEncoding() {
        this.charset = null;
    }

    public FallbackZipEncoding(String charset) {
        this.charset = charset;
    }

    public boolean canEncode(String name) {
        return true;
    }

    public ByteBuffer encode(String name) throws IOException {
        if (this.charset == null) {
            return ByteBuffer.wrap(name.getBytes());
        }
        return ByteBuffer.wrap(name.getBytes(this.charset));
    }

    public String decode(byte[] data) throws IOException {
        if (this.charset == null) {
            return new String(data);
        }
        return new String(data, this.charset);
    }
}
