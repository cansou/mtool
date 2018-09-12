package org.apache.tools.zip;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipException;

public class ExtraFieldUtils {
    private static final int WORD = 4;
    private static final Map implementations = new HashMap();

    public static final class UnparseableExtraField {
        public static final UnparseableExtraField READ = new UnparseableExtraField(2);
        public static final int READ_KEY = 2;
        public static final UnparseableExtraField SKIP = new UnparseableExtraField(1);
        public static final int SKIP_KEY = 1;
        public static final UnparseableExtraField THROW = new UnparseableExtraField(0);
        public static final int THROW_KEY = 0;
        private final int key;

        private UnparseableExtraField(int k) {
            this.key = k;
        }

        public int getKey() {
            return this.key;
        }
    }

    static {
        register(AsiExtraField.class);
        register(JarMarker.class);
        register(UnicodePathExtraField.class);
        register(UnicodeCommentExtraField.class);
    }

    public static void register(Class c) {
        try {
            implementations.put(((ZipExtraField) c.newInstance()).getHeaderId(), c);
        } catch (ClassCastException e) {
            throw new RuntimeException(c + " doesn't implement ZipExtraField");
        } catch (InstantiationException e2) {
            throw new RuntimeException(c + " is not a concrete class");
        } catch (IllegalAccessException e3) {
            throw new RuntimeException(c + "'s no-arg constructor is not public");
        }
    }

    public static ZipExtraField createExtraField(ZipShort headerId) throws InstantiationException, IllegalAccessException {
        Class c = (Class) implementations.get(headerId);
        if (c != null) {
            return (ZipExtraField) c.newInstance();
        }
        UnrecognizedExtraField u = new UnrecognizedExtraField();
        u.setHeaderId(headerId);
        return u;
    }

    public static ZipExtraField[] parse(byte[] data) throws ZipException {
        return parse(data, true, UnparseableExtraField.THROW);
    }

    public static ZipExtraField[] parse(byte[] data, boolean local) throws ZipException {
        return parse(data, local, UnparseableExtraField.THROW);
    }

    public static ZipExtraField[] parse(byte[] data, boolean local, UnparseableExtraField onUnparseableData) throws ZipException {
        List v = new ArrayList();
        int start = 0;
        while (start <= data.length - 4) {
            ZipShort headerId = new ZipShort(data, start);
            int length = new ZipShort(data, start + 2).getValue();
            if ((start + 4) + length > data.length) {
                switch (onUnparseableData.getKey()) {
                    case 0:
                        throw new ZipException("bad extra field starting at " + start + ".  Block length of " + length + " bytes exceeds remaining" + " data of " + ((data.length - start) - 4) + " bytes.");
                    case 1:
                        break;
                    case 2:
                        UnparseableExtraFieldData field = new UnparseableExtraFieldData();
                        if (local) {
                            field.parseFromLocalFileData(data, start, data.length - start);
                        } else {
                            field.parseFromCentralDirectoryData(data, start, data.length - start);
                        }
                        v.add(field);
                        break;
                    default:
                        throw new ZipException("unknown UnparseableExtraField key: " + onUnparseableData.getKey());
                }
                return (ZipExtraField[]) v.toArray(new ZipExtraField[v.size()]);
            }
            try {
                ZipExtraField ze = createExtraField(headerId);
                if (local || !(ze instanceof CentralDirectoryParsingZipExtraField)) {
                    ze.parseFromLocalFileData(data, start + 4, length);
                } else {
                    ((CentralDirectoryParsingZipExtraField) ze).parseFromCentralDirectoryData(data, start + 4, length);
                }
                v.add(ze);
                start += length + 4;
            } catch (InstantiationException ie) {
                throw new ZipException(ie.getMessage());
            } catch (IllegalAccessException iae) {
                throw new ZipException(iae.getMessage());
            }
        }
        return (ZipExtraField[]) v.toArray(new ZipExtraField[v.size()]);
    }

    public static byte[] mergeLocalFileDataData(ZipExtraField[] data) {
        boolean lastIsUnparseableHolder;
        int i;
        if (data.length <= 0 || !(data[data.length - 1] instanceof UnparseableExtraFieldData)) {
            lastIsUnparseableHolder = false;
        } else {
            lastIsUnparseableHolder = true;
        }
        int regularExtraFieldCount = lastIsUnparseableHolder ? data.length - 1 : data.length;
        int sum = regularExtraFieldCount * 4;
        for (ZipExtraField localFileDataLength : data) {
            sum += localFileDataLength.getLocalFileDataLength().getValue();
        }
        byte[] result = new byte[sum];
        int start = 0;
        for (i = 0; i < regularExtraFieldCount; i++) {
            System.arraycopy(data[i].getHeaderId().getBytes(), 0, result, start, 2);
            System.arraycopy(data[i].getLocalFileDataLength().getBytes(), 0, result, start + 2, 2);
            byte[] local = data[i].getLocalFileDataData();
            System.arraycopy(local, 0, result, start + 4, local.length);
            start += local.length + 4;
        }
        if (lastIsUnparseableHolder) {
            local = data[data.length - 1].getLocalFileDataData();
            System.arraycopy(local, 0, result, start, local.length);
        }
        return result;
    }

    public static byte[] mergeCentralDirectoryData(ZipExtraField[] data) {
        boolean lastIsUnparseableHolder;
        int i;
        if (data.length <= 0 || !(data[data.length - 1] instanceof UnparseableExtraFieldData)) {
            lastIsUnparseableHolder = false;
        } else {
            lastIsUnparseableHolder = true;
        }
        int regularExtraFieldCount = lastIsUnparseableHolder ? data.length - 1 : data.length;
        int sum = regularExtraFieldCount * 4;
        for (ZipExtraField centralDirectoryLength : data) {
            sum += centralDirectoryLength.getCentralDirectoryLength().getValue();
        }
        byte[] result = new byte[sum];
        int start = 0;
        for (i = 0; i < regularExtraFieldCount; i++) {
            System.arraycopy(data[i].getHeaderId().getBytes(), 0, result, start, 2);
            System.arraycopy(data[i].getCentralDirectoryLength().getBytes(), 0, result, start + 2, 2);
            byte[] local = data[i].getCentralDirectoryData();
            System.arraycopy(local, 0, result, start + 4, local.length);
            start += local.length + 4;
        }
        if (lastIsUnparseableHolder) {
            local = data[data.length - 1].getCentralDirectoryData();
            System.arraycopy(local, 0, result, start, local.length);
        }
        return result;
    }
}
