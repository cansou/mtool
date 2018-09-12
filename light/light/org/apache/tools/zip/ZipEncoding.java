package org.apache.tools.zip;

import java.io.IOException;
import java.nio.ByteBuffer;

interface ZipEncoding {
    boolean canEncode(String str);

    String decode(byte[] bArr) throws IOException;

    ByteBuffer encode(String str) throws IOException;
}
