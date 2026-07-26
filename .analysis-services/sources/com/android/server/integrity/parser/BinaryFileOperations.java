package com.android.server.integrity.parser;

/* JADX INFO: loaded from: classes2.dex */
public class BinaryFileOperations {
    public static java.lang.String getStringValue(com.android.server.integrity.model.BitInputStream bitInputStream) throws java.io.IOException {
        boolean isHashedValue = bitInputStream.getNext(1) == 1;
        int valueSize = bitInputStream.getNext(8);
        return getStringValue(bitInputStream, valueSize, isHashedValue);
    }

    public static java.lang.String getStringValue(com.android.server.integrity.model.BitInputStream bitInputStream, int valueSize, boolean isHashedValue) throws java.io.IOException {
        if (!isHashedValue) {
            java.lang.StringBuilder value = new java.lang.StringBuilder();
            while (true) {
                int valueSize2 = valueSize - 1;
                if (valueSize > 0) {
                    value.append((char) bitInputStream.getNext(8));
                    valueSize = valueSize2;
                } else {
                    return value.toString();
                }
            }
        } else {
            java.nio.ByteBuffer byteBuffer = java.nio.ByteBuffer.allocate(valueSize);
            while (true) {
                int valueSize3 = valueSize - 1;
                if (valueSize > 0) {
                    byteBuffer.put((byte) (bitInputStream.getNext(8) & 255));
                    valueSize = valueSize3;
                } else {
                    return android.content.integrity.IntegrityUtils.getHexDigest(byteBuffer.array());
                }
            }
        }
    }

    public static int getIntValue(com.android.server.integrity.model.BitInputStream bitInputStream) throws java.io.IOException {
        return bitInputStream.getNext(32);
    }

    public static boolean getBooleanValue(com.android.server.integrity.model.BitInputStream bitInputStream) throws java.io.IOException {
        return bitInputStream.getNext(1) == 1;
    }
}
