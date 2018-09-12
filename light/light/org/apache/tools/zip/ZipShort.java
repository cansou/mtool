package org.apache.tools.zip;

public final class ZipShort implements Cloneable {
    private static final int BYTE_1_MASK = 65280;
    private static final int BYTE_1_SHIFT = 8;
    private static final int BYTE_MASK = 255;
    private int value;

    public ZipShort(int value) {
        this.value = value;
    }

    public ZipShort(byte[] bytes) {
        this(bytes, 0);
    }

    public ZipShort(byte[] bytes, int offset) {
        this.value = getValue(bytes, offset);
    }

    public byte[] getBytes() {
        return new byte[]{(byte) (this.value & 255), (byte) ((this.value & 65280) >> 8)};
    }

    public int getValue() {
        return this.value;
    }

    public static byte[] getBytes(int value) {
        return new byte[]{(byte) (value & 255), (byte) ((65280 & value) >> 8)};
    }

    public static int getValue(byte[] bytes, int offset) {
        return ((bytes[offset + 1] << 8) & 65280) + (bytes[offset] & 255);
    }

    public static int getValue(byte[] bytes) {
        return getValue(bytes, 0);
    }

    public boolean equals(Object o) {
        if (o != null && (o instanceof ZipShort) && this.value == ((ZipShort) o).getValue()) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return this.value;
    }

    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException cnfe) {
            throw new RuntimeException(cnfe);
        }
    }
}
