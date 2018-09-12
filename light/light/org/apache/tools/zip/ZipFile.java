package org.apache.tools.zip;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.zip.CRC32;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;
import java.util.zip.ZipException;

public class ZipFile {
    private static final int BYTE_SHIFT = 8;
    private static final int CFD_LOCATOR_OFFSET = 16;
    private static final int CFH_LEN = 42;
    private static final int HASH_SIZE = 509;
    private static final long LFH_OFFSET_FOR_FILENAME_LENGTH = 26;
    private static final int MAX_EOCD_SIZE = 65557;
    private static final int MIN_EOCD_SIZE = 22;
    private static final int NIBLET_MASK = 15;
    private static final int POS_0 = 0;
    private static final int POS_1 = 1;
    private static final int POS_2 = 2;
    private static final int POS_3 = 3;
    private static final int SHORT = 2;
    private static final int WORD = 4;
    private RandomAccessFile archive;
    private String encoding;
    private final Map entries;
    private final Map nameMap;
    private final boolean useUnicodeExtraFields;
    private final ZipEncoding zipEncoding;

    private class BoundedInputStream extends InputStream {
        private boolean addDummyByte = false;
        private long loc;
        private long remaining;

        BoundedInputStream(long start, long remaining) {
            this.remaining = remaining;
            this.loc = start;
        }

        public int read() throws IOException {
            long j = this.remaining;
            this.remaining = j - 1;
            if (j > 0) {
                int read;
                synchronized (ZipFile.this.archive) {
                    RandomAccessFile access$0 = ZipFile.this.archive;
                    long j2 = this.loc;
                    this.loc = j2 + 1;
                    access$0.seek(j2);
                    read = ZipFile.this.archive.read();
                }
                return read;
            } else if (!this.addDummyByte) {
                return -1;
            } else {
                this.addDummyByte = false;
                return 0;
            }
        }

        public int read(byte[] b, int off, int len) throws IOException {
            if (this.remaining <= 0) {
                if (!this.addDummyByte) {
                    return -1;
                }
                this.addDummyByte = false;
                b[off] = (byte) 0;
                return 1;
            } else if (len <= 0) {
                return 0;
            } else {
                int ret;
                if (((long) len) > this.remaining) {
                    len = (int) this.remaining;
                }
                synchronized (ZipFile.this.archive) {
                    ZipFile.this.archive.seek(this.loc);
                    ret = ZipFile.this.archive.read(b, off, len);
                }
                if (ret <= 0) {
                    return ret;
                }
                this.loc += (long) ret;
                this.remaining -= (long) ret;
                return ret;
            }
        }

        void addDummy() {
            this.addDummyByte = true;
        }
    }

    private static final class NameAndComment {
        private final byte[] comment;
        private final byte[] name;

        private NameAndComment(byte[] name, byte[] comment) {
            this.name = name;
            this.comment = comment;
        }
    }

    private static final class OffsetEntry {
        private long dataOffset;
        private long headerOffset;

        private OffsetEntry() {
            this.headerOffset = -1;
            this.dataOffset = -1;
        }
    }

    public ZipFile(File f) throws IOException {
        this(f, null);
    }

    public ZipFile(String name) throws IOException {
        this(new File(name), null);
    }

    public ZipFile(String name, String encoding) throws IOException {
        this(new File(name), encoding, true);
    }

    public ZipFile(File f, String encoding) throws IOException {
        this(f, encoding, true);
    }

    public ZipFile(File f, String encoding, boolean useUnicodeExtraFields) throws IOException {
        this.entries = new HashMap(HASH_SIZE);
        this.nameMap = new HashMap(HASH_SIZE);
        this.encoding = null;
        this.encoding = encoding;
        this.zipEncoding = ZipEncodingHelper.getZipEncoding(encoding);
        this.useUnicodeExtraFields = useUnicodeExtraFields;
        this.archive = new RandomAccessFile(f, "r");
        try {
            resolveLocalFileHeaderData(populateFromCentralDirectory());
            if (!true) {
                try {
                    this.archive.close();
                } catch (IOException e) {
                }
            }
        } catch (Throwable th) {
            if (!false) {
                try {
                    this.archive.close();
                } catch (IOException e2) {
                }
            }
        }
    }

    public String getEncoding() {
        return this.encoding;
    }

    public void close() throws IOException {
        this.archive.close();
    }

    public static void closeQuietly(ZipFile zipfile) {
        if (zipfile != null) {
            try {
                zipfile.close();
            } catch (IOException e) {
            }
        }
    }

    public Enumeration getEntries() {
        return Collections.enumeration(this.entries.keySet());
    }

    public ZipEntry getEntry(String name) {
        return (ZipEntry) this.nameMap.get(name);
    }

    public InputStream getInputStream(ZipEntry ze) throws IOException, ZipException {
        OffsetEntry offsetEntry = (OffsetEntry) this.entries.get(ze);
        if (offsetEntry == null) {
            return null;
        }
        InputStream bis = new BoundedInputStream(offsetEntry.dataOffset, ze.getCompressedSize());
        switch (ze.getMethod()) {
            case 0:
                return bis;
            case 8:
                bis.addDummy();
                final Inflater inflater = new Inflater(true);
                return new InflaterInputStream(bis, inflater) {
                    public void close() throws IOException {
                        super.close();
                        inflater.end();
                    }
                };
            default:
                throw new ZipException("Found unsupported compression method " + ze.getMethod());
        }
    }

    private Map populateFromCentralDirectory() throws IOException {
        HashMap noUTF8Flag = new HashMap();
        positionAtCentralDirectory();
        byte[] cfh = new byte[CFH_LEN];
        byte[] signatureBytes = new byte[4];
        this.archive.readFully(signatureBytes);
        long sig = ZipLong.getValue(signatureBytes);
        long cfhSig = ZipLong.getValue(ZipOutputStream.CFH_SIG);
        if (sig == cfhSig || !startsWithLocalFileHeader()) {
            while (sig == cfhSig) {
                this.archive.readFully(cfh);
                ZipEntry ze = new ZipEntry();
                int off = 0 + 2;
                ze.setPlatform((ZipShort.getValue(cfh, 0) >> 8) & 15);
                off += 2;
                boolean hasUTF8Flag = (ZipShort.getValue(cfh, off) & 2048) != 0;
                ZipEncoding entryEncoding = hasUTF8Flag ? ZipEncodingHelper.UTF8_ZIP_ENCODING : this.zipEncoding;
                off += 2;
                ze.setMethod(ZipShort.getValue(cfh, off));
                off += 2;
                ze.setTime(dosToJavaTime(ZipLong.getValue(cfh, off)));
                off += 4;
                ze.setCrc(ZipLong.getValue(cfh, off));
                off += 4;
                ze.setCompressedSize(ZipLong.getValue(cfh, off));
                off += 4;
                ze.setSize(ZipLong.getValue(cfh, off));
                off += 4;
                int fileNameLen = ZipShort.getValue(cfh, off);
                off += 2;
                int extraLen = ZipShort.getValue(cfh, off);
                off += 2;
                int commentLen = ZipShort.getValue(cfh, off);
                off = (off + 2) + 2;
                ze.setInternalAttributes(ZipShort.getValue(cfh, off));
                off += 2;
                ze.setExternalAttributes(ZipLong.getValue(cfh, off));
                off += 4;
                byte[] fileName = new byte[fileNameLen];
                this.archive.readFully(fileName);
                ze.setName(entryEncoding.decode(fileName));
                OffsetEntry offsetEntry = new OffsetEntry();
                offsetEntry.headerOffset = ZipLong.getValue(cfh, off);
                this.entries.put(ze, offsetEntry);
                this.nameMap.put(ze.getName(), ze);
                byte[] cdExtraData = new byte[extraLen];
                this.archive.readFully(cdExtraData);
                ze.setCentralDirectoryExtra(cdExtraData);
                byte[] comment = new byte[commentLen];
                this.archive.readFully(comment);
                ze.setComment(entryEncoding.decode(comment));
                this.archive.readFully(signatureBytes);
                sig = ZipLong.getValue(signatureBytes);
                if (!hasUTF8Flag && this.useUnicodeExtraFields) {
                    noUTF8Flag.put(ze, new NameAndComment(fileName, comment));
                }
            }
            return noUTF8Flag;
        }
        throw new IOException("central directory is empty, can't expand corrupt archive.");
    }

    private void positionAtCentralDirectory() throws IOException {
        boolean found = false;
        long off = this.archive.length() - 22;
        long stopSearching = Math.max(0, this.archive.length() - 65557);
        if (off >= 0) {
            byte[] sig = ZipOutputStream.EOCD_SIG;
            while (off >= stopSearching) {
                this.archive.seek(off);
                byte curr = this.archive.read();
                if (curr != (byte) -1) {
                    if (curr == sig[0] && this.archive.read() == sig[1] && this.archive.read() == sig[2] && this.archive.read() == sig[3]) {
                        found = true;
                        break;
                    }
                    off--;
                } else {
                    break;
                }
            }
        }
        if (found) {
            this.archive.seek(16 + off);
            byte[] cfdOffset = new byte[4];
            this.archive.readFully(cfdOffset);
            this.archive.seek(ZipLong.getValue(cfdOffset));
            return;
        }
        throw new ZipException("archive is not a ZIP archive");
    }

    private void resolveLocalFileHeaderData(Map entriesWithoutUTF8Flag) throws IOException {
        Enumeration e = Collections.enumeration(new HashSet(this.entries.keySet()));
        while (e.hasMoreElements()) {
            ZipEntry ze = (ZipEntry) e.nextElement();
            OffsetEntry offsetEntry = (OffsetEntry) this.entries.get(ze);
            long offset = offsetEntry.headerOffset;
            this.archive.seek(LFH_OFFSET_FOR_FILENAME_LENGTH + offset);
            byte[] b = new byte[2];
            this.archive.readFully(b);
            int fileNameLen = ZipShort.getValue(b);
            this.archive.readFully(b);
            int extraFieldLen = ZipShort.getValue(b);
            int lenToSkip = fileNameLen;
            while (lenToSkip > 0) {
                int skipped = this.archive.skipBytes(lenToSkip);
                if (skipped <= 0) {
                    throw new RuntimeException("failed to skip file name in local file header");
                }
                lenToSkip -= skipped;
            }
            byte[] localExtraData = new byte[extraFieldLen];
            this.archive.readFully(localExtraData);
            ze.setExtra(localExtraData);
            offsetEntry.dataOffset = ((((LFH_OFFSET_FOR_FILENAME_LENGTH + offset) + 2) + 2) + ((long) fileNameLen)) + ((long) extraFieldLen);
            if (entriesWithoutUTF8Flag.containsKey(ze)) {
                this.entries.remove(ze);
                setNameAndCommentFromExtraFields(ze, (NameAndComment) entriesWithoutUTF8Flag.get(ze));
                this.entries.put(ze, offsetEntry);
            }
        }
    }

    protected static Date fromDosTime(ZipLong zipDosTime) {
        return new Date(dosToJavaTime(zipDosTime.getValue()));
    }

    private static long dosToJavaTime(long dosTime) {
        Calendar cal = Calendar.getInstance();
        cal.set(1, ((int) ((dosTime >> 25) & 127)) + 1980);
        cal.set(2, ((int) ((dosTime >> 21) & 15)) - 1);
        cal.set(5, ((int) (dosTime >> 16)) & 31);
        cal.set(11, ((int) (dosTime >> 11)) & 31);
        cal.set(12, ((int) (dosTime >> 5)) & 63);
        cal.set(13, ((int) (dosTime << 1)) & 62);
        return cal.getTime().getTime();
    }

    protected String getString(byte[] bytes) throws ZipException {
        try {
            return ZipEncodingHelper.getZipEncoding(this.encoding).decode(bytes);
        } catch (IOException ex) {
            throw new ZipException("Failed to decode name: " + ex.getMessage());
        }
    }

    private boolean startsWithLocalFileHeader() throws IOException {
        this.archive.seek(0);
        byte[] start = new byte[4];
        this.archive.readFully(start);
        for (int i = 0; i < start.length; i++) {
            if (start[i] != ZipOutputStream.LFH_SIG[i]) {
                return false;
            }
        }
        return true;
    }

    private void setNameAndCommentFromExtraFields(ZipEntry ze, NameAndComment nc) {
        UnicodePathExtraField name = (UnicodePathExtraField) ze.getExtraField(UnicodePathExtraField.UPATH_ID);
        String originalName = ze.getName();
        String newName = getUnicodeStringIfOriginalMatches(name, nc.name);
        if (!(newName == null || originalName.equals(newName))) {
            ze.setName(newName);
            this.nameMap.remove(originalName);
            this.nameMap.put(newName, ze);
        }
        if (nc.comment != null && nc.comment.length > 0) {
            String newComment = getUnicodeStringIfOriginalMatches((UnicodeCommentExtraField) ze.getExtraField(UnicodeCommentExtraField.UCOM_ID), nc.comment);
            if (newComment != null) {
                ze.setComment(newComment);
            }
        }
    }

    private String getUnicodeStringIfOriginalMatches(AbstractUnicodeExtraField f, byte[] orig) {
        String str = null;
        if (f != null) {
            CRC32 crc32 = new CRC32();
            crc32.update(orig);
            if (crc32.getValue() == f.getNameCRC32()) {
                try {
                    str = ZipEncodingHelper.UTF8_ZIP_ENCODING.decode(f.getUnicodeName());
                } catch (IOException e) {
                }
            }
        }
        return str;
    }
}
