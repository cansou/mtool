package org.apache.tools.zip;

public final class UnparseableExtraFieldData implements CentralDirectoryParsingZipExtraField {
    private static final ZipShort HEADER_ID = new ZipShort(44225);
    private byte[] centralDirectoryData;
    private byte[] localFileData;

    public ZipShort getHeaderId() {
        return HEADER_ID;
    }

    public ZipShort getLocalFileDataLength() {
        return new ZipShort(this.localFileData == null ? 0 : this.localFileData.length);
    }

    public ZipShort getCentralDirectoryLength() {
        if (this.centralDirectoryData == null) {
            return getLocalFileDataLength();
        }
        return new ZipShort(this.centralDirectoryData.length);
    }

    public byte[] getLocalFileDataData() {
        return ZipUtil.copy(this.localFileData);
    }

    public byte[] getCentralDirectoryData() {
        return this.centralDirectoryData == null ? getLocalFileDataData() : ZipUtil.copy(this.centralDirectoryData);
    }

    public void parseFromLocalFileData(byte[] buffer, int offset, int length) {
        this.localFileData = new byte[length];
        System.arraycopy(buffer, offset, this.localFileData, 0, length);
    }

    public void parseFromCentralDirectoryData(byte[] buffer, int offset, int length) {
        this.centralDirectoryData = new byte[length];
        System.arraycopy(buffer, offset, this.centralDirectoryData, 0, length);
        if (this.localFileData == null) {
            parseFromLocalFileData(buffer, offset, length);
        }
    }
}
