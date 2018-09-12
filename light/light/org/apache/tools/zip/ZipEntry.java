package org.apache.tools.zip;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.zip.ZipException;
import org.apache.tools.zip.ExtraFieldUtils.UnparseableExtraField;

public class ZipEntry extends java.util.zip.ZipEntry implements Cloneable {
    public static final int PLATFORM_FAT = 0;
    public static final int PLATFORM_UNIX = 3;
    private static final int SHORT_MASK = 65535;
    private static final int SHORT_SHIFT = 16;
    private long externalAttributes;
    private LinkedHashMap extraFields;
    private int internalAttributes;
    private String name;
    private int platform;
    private UnparseableExtraFieldData unparseableExtra;

    public ZipEntry(String name) {
        super(name);
        this.internalAttributes = 0;
        this.platform = 0;
        this.externalAttributes = 0;
        this.extraFields = null;
        this.unparseableExtra = null;
        this.name = null;
    }

    public ZipEntry(java.util.zip.ZipEntry entry) throws ZipException {
        super(entry);
        this.internalAttributes = 0;
        this.platform = 0;
        this.externalAttributes = 0;
        this.extraFields = null;
        this.unparseableExtra = null;
        this.name = null;
        byte[] extra = entry.getExtra();
        if (extra != null) {
            setExtraFields(ExtraFieldUtils.parse(extra, true, UnparseableExtraField.READ));
        } else {
            setExtra();
        }
    }

    public ZipEntry(ZipEntry entry) throws ZipException {
        this((java.util.zip.ZipEntry) entry);
        setInternalAttributes(entry.getInternalAttributes());
        setExternalAttributes(entry.getExternalAttributes());
        setExtraFields(entry.getExtraFields(true));
    }

    protected ZipEntry() {
        super("");
        this.internalAttributes = 0;
        this.platform = 0;
        this.externalAttributes = 0;
        this.extraFields = null;
        this.unparseableExtra = null;
        this.name = null;
    }

    public Object clone() {
        ZipEntry e = (ZipEntry) super.clone();
        e.setInternalAttributes(getInternalAttributes());
        e.setExternalAttributes(getExternalAttributes());
        e.setExtraFields(getExtraFields(true));
        return e;
    }

    public int getInternalAttributes() {
        return this.internalAttributes;
    }

    public void setInternalAttributes(int value) {
        this.internalAttributes = value;
    }

    public long getExternalAttributes() {
        return this.externalAttributes;
    }

    public void setExternalAttributes(long value) {
        this.externalAttributes = value;
    }

    public void setUnixMode(int mode) {
        int i = 0;
        int i2 = ((mode & 128) == 0 ? 1 : 0) | (mode << 16);
        if (isDirectory()) {
            i = 16;
        }
        setExternalAttributes((long) (i2 | i));
        this.platform = 3;
    }

    public int getUnixMode() {
        if (this.platform != 3) {
            return 0;
        }
        return (int) ((getExternalAttributes() >> 16) & 65535);
    }

    public int getPlatform() {
        return this.platform;
    }

    protected void setPlatform(int platform) {
        this.platform = platform;
    }

    public void setExtraFields(ZipExtraField[] fields) {
        this.extraFields = new LinkedHashMap();
        for (int i = 0; i < fields.length; i++) {
            if (fields[i] instanceof UnparseableExtraFieldData) {
                this.unparseableExtra = (UnparseableExtraFieldData) fields[i];
            } else {
                this.extraFields.put(fields[i].getHeaderId(), fields[i]);
            }
        }
        setExtra();
    }

    public ZipExtraField[] getExtraFields() {
        return getExtraFields(false);
    }

    public ZipExtraField[] getExtraFields(boolean includeUnparseable) {
        if (this.extraFields != null) {
            List result = new ArrayList(this.extraFields.values());
            if (includeUnparseable && this.unparseableExtra != null) {
                result.add(this.unparseableExtra);
            }
            return (ZipExtraField[]) result.toArray(new ZipExtraField[0]);
        } else if (!includeUnparseable || this.unparseableExtra == null) {
            return new ZipExtraField[0];
        } else {
            return new ZipExtraField[]{this.unparseableExtra};
        }
    }

    public void addExtraField(ZipExtraField ze) {
        if (ze instanceof UnparseableExtraFieldData) {
            this.unparseableExtra = (UnparseableExtraFieldData) ze;
        } else {
            if (this.extraFields == null) {
                this.extraFields = new LinkedHashMap();
            }
            this.extraFields.put(ze.getHeaderId(), ze);
        }
        setExtra();
    }

    public void addAsFirstExtraField(ZipExtraField ze) {
        if (ze instanceof UnparseableExtraFieldData) {
            this.unparseableExtra = (UnparseableExtraFieldData) ze;
        } else {
            LinkedHashMap copy = this.extraFields;
            this.extraFields = new LinkedHashMap();
            this.extraFields.put(ze.getHeaderId(), ze);
            if (copy != null) {
                copy.remove(ze.getHeaderId());
                this.extraFields.putAll(copy);
            }
        }
        setExtra();
    }

    public void removeExtraField(ZipShort type) {
        if (this.extraFields == null) {
            throw new NoSuchElementException();
        } else if (this.extraFields.remove(type) == null) {
            throw new NoSuchElementException();
        } else {
            setExtra();
        }
    }

    public void removeUnparseableExtraFieldData() {
        if (this.unparseableExtra == null) {
            throw new NoSuchElementException();
        }
        this.unparseableExtra = null;
        setExtra();
    }

    public ZipExtraField getExtraField(ZipShort type) {
        if (this.extraFields != null) {
            return (ZipExtraField) this.extraFields.get(type);
        }
        return null;
    }

    public UnparseableExtraFieldData getUnparseableExtraFieldData() {
        return this.unparseableExtra;
    }

    public void setExtra(byte[] extra) throws RuntimeException {
        try {
            mergeExtraFields(ExtraFieldUtils.parse(extra, true, UnparseableExtraField.READ), true);
        } catch (Exception e) {
            throw new RuntimeException("Error parsing extra fields for entry: " + getName() + " - " + e.getMessage(), e);
        }
    }

    protected void setExtra() {
        super.setExtra(ExtraFieldUtils.mergeLocalFileDataData(getExtraFields(true)));
    }

    public void setCentralDirectoryExtra(byte[] b) {
        try {
            mergeExtraFields(ExtraFieldUtils.parse(b, false, UnparseableExtraField.READ), false);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public byte[] getLocalFileDataExtra() {
        byte[] extra = getExtra();
        return extra != null ? extra : new byte[0];
    }

    public byte[] getCentralDirectoryExtra() {
        return ExtraFieldUtils.mergeCentralDirectoryData(getExtraFields(true));
    }

    public void setComprSize(long size) {
        setCompressedSize(size);
    }

    public String getName() {
        return this.name == null ? super.getName() : this.name;
    }

    public boolean isDirectory() {
        return getName().endsWith("/");
    }

    protected void setName(String name) {
        this.name = name;
    }

    public int hashCode() {
        return getName().hashCode();
    }

    public boolean equals(Object o) {
        return this == o;
    }

    private void mergeExtraFields(ZipExtraField[] f, boolean local) throws ZipException {
        if (this.extraFields == null) {
            setExtraFields(f);
            return;
        }
        for (int i = 0; i < f.length; i++) {
            ZipExtraField existing;
            if (f[i] instanceof UnparseableExtraFieldData) {
                existing = this.unparseableExtra;
            } else {
                existing = getExtraField(f[i].getHeaderId());
            }
            if (existing == null) {
                addExtraField(f[i]);
            } else if (local || !(existing instanceof CentralDirectoryParsingZipExtraField)) {
                b = f[i].getLocalFileDataData();
                existing.parseFromLocalFileData(b, 0, b.length);
            } else {
                b = f[i].getCentralDirectoryData();
                ((CentralDirectoryParsingZipExtraField) existing).parseFromCentralDirectoryData(b, 0, b.length);
            }
        }
        setExtra();
    }
}
