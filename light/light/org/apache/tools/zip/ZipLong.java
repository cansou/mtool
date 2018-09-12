package org.apache.tools.zip;

public final class ZipLong implements Cloneable {
    private static final int BYTE_1 = 1;
    private static final int BYTE_1_MASK = 65280;
    private static final int BYTE_1_SHIFT = 8;
    private static final int BYTE_2 = 2;
    private static final int BYTE_2_MASK = 16711680;
    private static final int BYTE_2_SHIFT = 16;
    private static final int BYTE_3 = 3;
    private static final long BYTE_3_MASK = 4278190080L;
    private static final int BYTE_3_SHIFT = 24;
    private static final int BYTE_MASK = 255;
    private static final int WORD = 4;
    private long value;

    public ZipLong(long value) {
        this.value = value;
    }

    public ZipLong(byte[] bytes) {
        this(bytes, 0);
    }

    public ZipLong(byte[] bytes, int offset) {
        this.value = getValue(bytes, offset);
    }

    public byte[] getBytes() {
        return getBytes(this.value);
    }

    public long getValue() {
        return this.value;
    }

    public static byte[] getBytes(long value) {
        return new byte[]{(byte) ((int) (255 & value)), (byte) ((int) ((65280 & value) >> 8)), (byte) ((int) ((16711680 & value) >> 16)), (byte) ((int) ((BYTE_3_MASK & value) >> BYTE_3_SHIFT))};
    }

    public static long getValue(byte[] bytes, int offset) {
        return (((((long) (bytes[offset + 3] << BYTE_3_SHIFT)) & BYTE_3_MASK) + ((long) ((bytes[offset + 2] << 16) & BYTE_2_MASK))) + ((long) ((bytes[offset + 1] << 8) & 65280))) + ((long) (bytes[offset] & 255));
    }

    public static long getValue(byte[] bytes) {
        return getValue(bytes, 0);
    }

    public boolean equals(Object o) {
        if (o != null && (o instanceof ZipLong) && this.value == ((ZipLong) o).getValue()) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return (int) this.value;
    }

    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException cnfe) {
            throw new RuntimeException(cnfe);
        }
    }
}
