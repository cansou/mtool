package org.apache.tools.zip;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.zip.CRC32;
import java.util.zip.Deflater;
import java.util.zip.ZipException;

public class ZipOutputStream extends FilterOutputStream {
    private static final int BUFFER_SIZE = 512;
    private static final int BYTE_MASK = 255;
    protected static final byte[] CFH_SIG = ZipLong.getBytes(33639248);
    protected static final byte[] DD_SIG = ZipLong.getBytes(134695760);
    public static final int DEFAULT_COMPRESSION = -1;
    static final String DEFAULT_ENCODING = null;
    public static final int DEFLATED = 8;
    private static final int DEFLATER_BLOCK_SIZE = 8192;
    private static final byte[] DOS_TIME_MIN = ZipLong.getBytes(8448);
    public static final int EFS_FLAG = 2048;
    protected static final byte[] EOCD_SIG = ZipLong.getBytes(101010256);
    protected static final byte[] LFH_SIG = ZipLong.getBytes(67324752);
    private static final byte[] LZERO = new byte[4];
    private static final int SHORT = 2;
    public static final int STORED = 0;
    public static final int UFT8_NAMES_FLAG = 2048;
    private static final int WORD = 4;
    private static final byte[] ZERO = new byte[2];
    protected byte[] buf;
    private long cdLength;
    private long cdOffset;
    private String comment;
    private final CRC32 crc;
    private UnicodeExtraFieldPolicy createUnicodeExtraFields;
    private long dataStart;
    protected Deflater def;
    private String encoding;
    private final List entries;
    private ZipEntry entry;
    private boolean fallbackToUTF8;
    private boolean hasCompressionLevelChanged;
    private int level;
    private long localDataStart;
    private int method;
    private final Map offsets;
    private RandomAccessFile raf;
    private boolean useUTF8Flag;
    private long written;
    private ZipEncoding zipEncoding;

    public static final class UnicodeExtraFieldPolicy {
        public static final UnicodeExtraFieldPolicy ALWAYS = new UnicodeExtraFieldPolicy("always");
        public static final UnicodeExtraFieldPolicy NEVER = new UnicodeExtraFieldPolicy("never");
        public static final UnicodeExtraFieldPolicy NOT_ENCODEABLE = new UnicodeExtraFieldPolicy("not encodeable");
        private final String name;

        private UnicodeExtraFieldPolicy(String n) {
            this.name = n;
        }

        public String toString() {
            return this.name;
        }
    }

    public ZipOutputStream(OutputStream out) {
        super(out);
        this.comment = "";
        this.level = -1;
        this.hasCompressionLevelChanged = false;
        this.method = 8;
        this.entries = new LinkedList();
        this.crc = new CRC32();
        this.written = 0;
        this.dataStart = 0;
        this.localDataStart = 0;
        this.cdOffset = 0;
        this.cdLength = 0;
        this.offsets = new HashMap();
        this.encoding = null;
        this.zipEncoding = ZipEncodingHelper.getZipEncoding(DEFAULT_ENCODING);
        this.def = new Deflater(this.level, true);
        this.buf = new byte[512];
        this.raf = null;
        this.useUTF8Flag = true;
        this.fallbackToUTF8 = false;
        this.createUnicodeExtraFields = UnicodeExtraFieldPolicy.NEVER;
    }

    public ZipOutputStream(File file) throws IOException {
        super(null);
        this.comment = "";
        this.level = -1;
        this.hasCompressionLevelChanged = false;
        this.method = 8;
        this.entries = new LinkedList();
        this.crc = new CRC32();
        this.written = 0;
        this.dataStart = 0;
        this.localDataStart = 0;
        this.cdOffset = 0;
        this.cdLength = 0;
        this.offsets = new HashMap();
        this.encoding = null;
        this.zipEncoding = ZipEncodingHelper.getZipEncoding(DEFAULT_ENCODING);
        this.def = new Deflater(this.level, true);
        this.buf = new byte[512];
        this.raf = null;
        this.useUTF8Flag = true;
        this.fallbackToUTF8 = false;
        this.createUnicodeExtraFields = UnicodeExtraFieldPolicy.NEVER;
        try {
            this.raf = new RandomAccessFile(file, "rw");
            this.raf.setLength(0);
        } catch (IOException e) {
            if (this.raf != null) {
                try {
                    this.raf.close();
                } catch (IOException e2) {
                }
                this.raf = null;
            }
            this.out = new FileOutputStream(file);
        }
    }

    public boolean isSeekable() {
        return this.raf != null;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
        this.zipEncoding = ZipEncodingHelper.getZipEncoding(encoding);
        this.useUTF8Flag &= ZipEncodingHelper.isUTF8(encoding);
    }

    public String getEncoding() {
        return this.encoding;
    }

    public void setUseLanguageEncodingFlag(boolean b) {
        boolean z = b && ZipEncodingHelper.isUTF8(this.encoding);
        this.useUTF8Flag = z;
    }

    public void setCreateUnicodeExtraFields(UnicodeExtraFieldPolicy b) {
        this.createUnicodeExtraFields = b;
    }

    public void setFallbackToUTF8(boolean b) {
        this.fallbackToUTF8 = b;
    }

    public void finish() throws IOException {
        closeEntry();
        this.cdOffset = this.written;
        for (ZipEntry writeCentralFileHeader : this.entries) {
            writeCentralFileHeader(writeCentralFileHeader);
        }
        this.cdLength = this.written - this.cdOffset;
        writeCentralDirectoryEnd();
        this.offsets.clear();
        this.entries.clear();
        this.def.end();
    }

    public void closeEntry() throws IOException {
        if (this.entry != null) {
            long realCrc = this.crc.getValue();
            this.crc.reset();
            if (this.entry.getMethod() == 8) {
                this.def.finish();
                while (!this.def.finished()) {
                    deflate();
                }
                this.entry.setSize(adjustToLong(this.def.getTotalIn()));
                this.entry.setCompressedSize(adjustToLong(this.def.getTotalOut()));
                this.entry.setCrc(realCrc);
                this.def.reset();
                this.written += this.entry.getCompressedSize();
            } else if (this.raf != null) {
                long size = this.written - this.dataStart;
                this.entry.setSize(size);
                this.entry.setCompressedSize(size);
                this.entry.setCrc(realCrc);
            } else if (this.entry.getCrc() != realCrc) {
                throw new ZipException("bad CRC checksum for entry " + this.entry.getName() + ": " + Long.toHexString(this.entry.getCrc()) + " instead of " + Long.toHexString(realCrc));
            } else if (this.entry.getSize() != this.written - this.dataStart) {
                throw new ZipException("bad size for entry " + this.entry.getName() + ": " + this.entry.getSize() + " instead of " + (this.written - this.dataStart));
            }
            if (this.raf != null) {
                long save = this.raf.getFilePointer();
                this.raf.seek(this.localDataStart);
                writeOut(ZipLong.getBytes(this.entry.getCrc()));
                writeOut(ZipLong.getBytes(this.entry.getCompressedSize()));
                writeOut(ZipLong.getBytes(this.entry.getSize()));
                this.raf.seek(save);
            }
            writeDataDescriptor(this.entry);
            this.entry = null;
        }
    }

    public void putNextEntry(ZipEntry ze) throws IOException {
        closeEntry();
        this.entry = ze;
        this.entries.add(this.entry);
        if (this.entry.getMethod() == -1) {
            this.entry.setMethod(this.method);
        }
        if (this.entry.getTime() == -1) {
            this.entry.setTime(System.currentTimeMillis());
        }
        if (this.entry.getMethod() == 0 && this.raf == null) {
            if (this.entry.getSize() == -1) {
                throw new ZipException("uncompressed size is required for STORED method when not writing to a file");
            } else if (this.entry.getCrc() == -1) {
                throw new ZipException("crc checksum is required for STORED method when not writing to a file");
            } else {
                this.entry.setCompressedSize(this.entry.getSize());
            }
        }
        if (this.entry.getMethod() == 8 && this.hasCompressionLevelChanged) {
            this.def.setLevel(this.level);
            this.hasCompressionLevelChanged = false;
        }
        writeLocalFileHeader(this.entry);
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setLevel(int level) {
        if (level < -1 || level > 9) {
            throw new IllegalArgumentException("Invalid compression level: " + level);
        }
        this.hasCompressionLevelChanged = this.level != level;
        this.level = level;
    }

    public void setMethod(int method) {
        this.method = method;
    }

    public void write(byte[] b, int offset, int length) throws IOException {
        if (this.entry.getMethod() != 8) {
            writeOut(b, offset, length);
            this.written += (long) length;
        } else if (length > 0 && !this.def.finished()) {
            if (length <= 8192) {
                this.def.setInput(b, offset, length);
                deflateUntilInputIsNeeded();
            } else {
                int fullblocks = length / 8192;
                for (int i = 0; i < fullblocks; i++) {
                    this.def.setInput(b, (i * 8192) + offset, 8192);
                    deflateUntilInputIsNeeded();
                }
                int done = fullblocks * 8192;
                if (done < length) {
                    this.def.setInput(b, offset + done, length - done);
                    deflateUntilInputIsNeeded();
                }
            }
        }
        this.crc.update(b, offset, length);
    }

    public void write(int b) throws IOException {
        write(new byte[]{(byte) (b & 255)}, 0, 1);
    }

    public void close() throws IOException {
        finish();
        if (this.raf != null) {
            this.raf.close();
        }
        if (this.out != null) {
            this.out.close();
        }
    }

    public void flush() throws IOException {
        if (this.out != null) {
            this.out.flush();
        }
    }

    protected final void deflate() throws IOException {
        int len = this.def.deflate(this.buf, 0, this.buf.length);
        if (len > 0) {
            writeOut(this.buf, 0, len);
        }
    }

    protected void writeLocalFileHeader(ZipEntry ze) throws IOException {
        ZipEncoding entryEncoding;
        boolean encodable = this.zipEncoding.canEncode(ze.getName());
        if (encodable || !this.fallbackToUTF8) {
            entryEncoding = this.zipEncoding;
        } else {
            entryEncoding = ZipEncodingHelper.UTF8_ZIP_ENCODING;
        }
        ByteBuffer name = entryEncoding.encode(ze.getName());
        if (this.createUnicodeExtraFields != UnicodeExtraFieldPolicy.NEVER) {
            if (this.createUnicodeExtraFields == UnicodeExtraFieldPolicy.ALWAYS || !encodable) {
                ze.addExtraField(new UnicodePathExtraField(ze.getName(), name.array(), name.arrayOffset(), name.limit()));
            }
            String comm = ze.getComment();
            if (!(comm == null || "".equals(comm))) {
                boolean commentEncodable = this.zipEncoding.canEncode(comm);
                if (this.createUnicodeExtraFields == UnicodeExtraFieldPolicy.ALWAYS || !commentEncodable) {
                    ByteBuffer commentB = entryEncoding.encode(comm);
                    ze.addExtraField(new UnicodeCommentExtraField(comm, commentB.array(), commentB.arrayOffset(), commentB.limit()));
                }
            }
        }
        this.offsets.put(ze, ZipLong.getBytes(this.written));
        writeOut(LFH_SIG);
        this.written += 4;
        int zipMethod = ze.getMethod();
        boolean z = !encodable && this.fallbackToUTF8;
        writeVersionNeededToExtractAndGeneralPurposeBits(zipMethod, z);
        this.written += 4;
        writeOut(ZipShort.getBytes(zipMethod));
        this.written += 2;
        writeOut(toDosTime(ze.getTime()));
        this.written += 4;
        this.localDataStart = this.written;
        if (zipMethod == 8 || this.raf != null) {
            writeOut(LZERO);
            writeOut(LZERO);
            writeOut(LZERO);
        } else {
            writeOut(ZipLong.getBytes(ze.getCrc()));
            writeOut(ZipLong.getBytes(ze.getSize()));
            writeOut(ZipLong.getBytes(ze.getSize()));
        }
        this.written += 12;
        writeOut(ZipShort.getBytes(name.limit()));
        this.written += 2;
        byte[] extra = ze.getLocalFileDataExtra();
        writeOut(ZipShort.getBytes(extra.length));
        this.written += 2;
        writeOut(name.array(), name.arrayOffset(), name.limit());
        this.written += (long) name.limit();
        writeOut(extra);
        this.written += (long) extra.length;
        this.dataStart = this.written;
    }

    protected void writeDataDescriptor(ZipEntry ze) throws IOException {
        if (ze.getMethod() == 8 && this.raf == null) {
            writeOut(DD_SIG);
            writeOut(ZipLong.getBytes(this.entry.getCrc()));
            writeOut(ZipLong.getBytes(this.entry.getCompressedSize()));
            writeOut(ZipLong.getBytes(this.entry.getSize()));
            this.written += 16;
        }
    }

    protected void writeCentralFileHeader(ZipEntry ze) throws IOException {
        ZipEncoding entryEncoding;
        writeOut(CFH_SIG);
        this.written += 4;
        writeOut(ZipShort.getBytes((ze.getPlatform() << 8) | 20));
        this.written += 2;
        int zipMethod = ze.getMethod();
        boolean encodable = this.zipEncoding.canEncode(ze.getName());
        boolean z = !encodable && this.fallbackToUTF8;
        writeVersionNeededToExtractAndGeneralPurposeBits(zipMethod, z);
        this.written += 4;
        writeOut(ZipShort.getBytes(zipMethod));
        this.written += 2;
        writeOut(toDosTime(ze.getTime()));
        this.written += 4;
        writeOut(ZipLong.getBytes(ze.getCrc()));
        writeOut(ZipLong.getBytes(ze.getCompressedSize()));
        writeOut(ZipLong.getBytes(ze.getSize()));
        this.written += 12;
        if (encodable || !this.fallbackToUTF8) {
            entryEncoding = this.zipEncoding;
        } else {
            entryEncoding = ZipEncodingHelper.UTF8_ZIP_ENCODING;
        }
        ByteBuffer name = entryEncoding.encode(ze.getName());
        writeOut(ZipShort.getBytes(name.limit()));
        this.written += 2;
        byte[] extra = ze.getCentralDirectoryExtra();
        writeOut(ZipShort.getBytes(extra.length));
        this.written += 2;
        String comm = ze.getComment();
        if (comm == null) {
            comm = "";
        }
        ByteBuffer commentB = entryEncoding.encode(comm);
        writeOut(ZipShort.getBytes(commentB.limit()));
        this.written += 2;
        writeOut(ZERO);
        this.written += 2;
        writeOut(ZipShort.getBytes(ze.getInternalAttributes()));
        this.written += 2;
        writeOut(ZipLong.getBytes(ze.getExternalAttributes()));
        this.written += 4;
        writeOut((byte[]) this.offsets.get(ze));
        this.written += 4;
        writeOut(name.array(), name.arrayOffset(), name.limit());
        this.written += (long) name.limit();
        writeOut(extra);
        this.written += (long) extra.length;
        writeOut(commentB.array(), commentB.arrayOffset(), commentB.limit());
        this.written += (long) commentB.limit();
    }

    protected void writeCentralDirectoryEnd() throws IOException {
        writeOut(EOCD_SIG);
        writeOut(ZERO);
        writeOut(ZERO);
        byte[] num = ZipShort.getBytes(this.entries.size());
        writeOut(num);
        writeOut(num);
        writeOut(ZipLong.getBytes(this.cdLength));
        writeOut(ZipLong.getBytes(this.cdOffset));
        ByteBuffer data = this.zipEncoding.encode(this.comment);
        writeOut(ZipShort.getBytes(data.limit()));
        writeOut(data.array(), data.arrayOffset(), data.limit());
    }

    protected static ZipLong toDosTime(Date time) {
        return new ZipLong(toDosTime(time.getTime()));
    }

    protected static byte[] toDosTime(long t) {
        Date time = new Date(t);
        int year = time.getYear() + 1900;
        if (year < 1980) {
            return DOS_TIME_MIN;
        }
        return ZipLong.getBytes((long) (((((((year - 1980) << 25) | ((time.getMonth() + 1) << 21)) | (time.getDate() << 16)) | (time.getHours() << 11)) | (time.getMinutes() << 5)) | (time.getSeconds() >> 1)));
    }

    protected byte[] getBytes(String name) throws ZipException {
        try {
            ByteBuffer b = ZipEncodingHelper.getZipEncoding(this.encoding).encode(name);
            byte[] result = new byte[b.limit()];
            System.arraycopy(b.array(), b.arrayOffset(), result, 0, result.length);
            return result;
        } catch (IOException ex) {
            throw new ZipException("Failed to encode name: " + ex.getMessage());
        }
    }

    protected final void writeOut(byte[] data) throws IOException {
        writeOut(data, 0, data.length);
    }

    protected final void writeOut(byte[] data, int offset, int length) throws IOException {
        if (this.raf != null) {
            this.raf.write(data, offset, length);
        } else {
            this.out.write(data, offset, length);
        }
    }

    protected static long adjustToLong(int i) {
        if (i < 0) {
            return 4294967296L + ((long) i);
        }
        return (long) i;
    }

    private void deflateUntilInputIsNeeded() throws IOException {
        while (!this.def.needsInput()) {
            deflate();
        }
    }

    private void writeVersionNeededToExtractAndGeneralPurposeBits(int zipMethod, boolean utfFallback) throws IOException {
        int versionNeededToExtract = 10;
        int generalPurposeFlag = (this.useUTF8Flag || utfFallback) ? 2048 : 0;
        if (zipMethod == 8 && this.raf == null) {
            versionNeededToExtract = 20;
            generalPurposeFlag |= 8;
        }
        writeOut(ZipShort.getBytes(versionNeededToExtract));
        writeOut(ZipShort.getBytes(generalPurposeFlag));
    }
}
