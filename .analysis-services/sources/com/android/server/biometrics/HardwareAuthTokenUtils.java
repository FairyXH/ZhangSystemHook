package com.android.server.biometrics;

/* JADX INFO: loaded from: classes.dex */
public class HardwareAuthTokenUtils {
    public static byte[] toByteArray(android.hardware.keymaster.HardwareAuthToken hat) {
        byte[] array = new byte[69];
        array[0] = 0;
        writeLong(hat.challenge, array, 1);
        writeLong(hat.userId, array, 9);
        writeLong(hat.authenticatorId, array, 17);
        writeInt(flipIfNativelyLittle(hat.authenticatorType), array, 25);
        writeLong(flipIfNativelyLittle(hat.timestamp.milliSeconds), array, 29);
        java.lang.System.arraycopy(hat.mac, 0, array, 37, hat.mac.length);
        return array;
    }

    public static android.hardware.keymaster.HardwareAuthToken toHardwareAuthToken(byte[] array) {
        android.hardware.keymaster.HardwareAuthToken hardwareAuthToken = new android.hardware.keymaster.HardwareAuthToken();
        hardwareAuthToken.challenge = getLong(array, 1);
        hardwareAuthToken.userId = getLong(array, 9);
        hardwareAuthToken.authenticatorId = getLong(array, 17);
        hardwareAuthToken.authenticatorType = flipIfNativelyLittle(getInt(array, 25));
        android.hardware.keymaster.Timestamp timestamp = new android.hardware.keymaster.Timestamp();
        timestamp.milliSeconds = flipIfNativelyLittle(getLong(array, 29));
        hardwareAuthToken.timestamp = timestamp;
        hardwareAuthToken.mac = new byte[32];
        java.lang.System.arraycopy(array, 37, hardwareAuthToken.mac, 0, 32);
        return hardwareAuthToken;
    }

    private static long flipIfNativelyLittle(long l) {
        if (java.nio.ByteOrder.LITTLE_ENDIAN == java.nio.ByteOrder.nativeOrder()) {
            return java.lang.Long.reverseBytes(l);
        }
        return l;
    }

    private static int flipIfNativelyLittle(int i) {
        if (java.nio.ByteOrder.LITTLE_ENDIAN == java.nio.ByteOrder.nativeOrder()) {
            return java.lang.Integer.reverseBytes(i);
        }
        return i;
    }

    private static void writeLong(long l, byte[] dest, int offset) {
        dest[offset + 0] = (byte) l;
        dest[offset + 1] = (byte) (l >> 8);
        dest[offset + 2] = (byte) (l >> 16);
        dest[offset + 3] = (byte) (l >> 24);
        dest[offset + 4] = (byte) (l >> 32);
        dest[offset + 5] = (byte) (l >> 40);
        dest[offset + 6] = (byte) (l >> 48);
        dest[offset + 7] = (byte) (l >> 56);
    }

    private static void writeInt(int i, byte[] dest, int offset) {
        dest[offset + 0] = (byte) i;
        dest[offset + 1] = (byte) (i >> 8);
        dest[offset + 2] = (byte) (i >> 16);
        dest[offset + 3] = (byte) (i >> 24);
    }

    private static long getLong(byte[] array, int offset) {
        long result = 0;
        for (int i = 0; i < 8; i++) {
            result += (((long) array[i + offset]) & 255) << (i * 8);
        }
        return result;
    }

    private static int getInt(byte[] array, int offset) {
        int result = 0;
        for (int i = 0; i < 4; i++) {
            result += (array[i + offset] & 255) << (i * 8);
        }
        return result;
    }
}
