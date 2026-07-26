package com.android.internal.util.jobs;

/* JADX INFO: loaded from: classes.dex */
public final class BitUtils {
    private BitUtils() {
    }

    public static boolean maskedEquals(long a, long b, long mask) {
        return (a & mask) == (b & mask);
    }

    public static boolean maskedEquals(byte a, byte b, byte mask) {
        return (a & mask) == (b & mask);
    }

    public static boolean maskedEquals(byte[] a, byte[] b, byte[] mask) {
        if (a == null || b == null) {
            return a == b;
        }
        com.android.internal.util.jobs.Preconditions.checkArgument(a.length == b.length, "Inputs must be of same size");
        if (mask == null) {
            return java.util.Arrays.equals(a, b);
        }
        com.android.internal.util.jobs.Preconditions.checkArgument(a.length == mask.length, "Mask must be of same size as inputs");
        for (int i = 0; i < mask.length; i++) {
            if (!maskedEquals(a[i], b[i], mask[i])) {
                return false;
            }
        }
        return true;
    }

    public static boolean maskedEquals(java.util.UUID a, java.util.UUID b, java.util.UUID mask) {
        if (mask == null) {
            return java.util.Objects.equals(a, b);
        }
        return maskedEquals(a.getLeastSignificantBits(), b.getLeastSignificantBits(), mask.getLeastSignificantBits()) && maskedEquals(a.getMostSignificantBits(), b.getMostSignificantBits(), mask.getMostSignificantBits());
    }

    public static int[] unpackBits(long val) {
        int size = java.lang.Long.bitCount(val);
        int[] result = new int[size];
        int index = 0;
        int bitPos = 0;
        while (val != 0) {
            if ((val & 1) == 1) {
                result[index] = bitPos;
                index++;
            }
            val >>>= 1;
            bitPos++;
        }
        return result;
    }

    public static long packBits(int[] bits) {
        long packed = 0;
        for (int b : bits) {
            packed |= 1 << b;
        }
        return packed;
    }

    public static int uint8(byte b) {
        return b & 255;
    }

    public static int uint16(short s) {
        return 65535 & s;
    }

    public static int uint16(byte hi, byte lo) {
        return ((hi & 255) << 8) | (lo & 255);
    }

    public static long uint32(int i) {
        return ((long) i) & 4294967295L;
    }

    public static int bytesToBEInt(byte[] bytes) {
        return (uint8(bytes[0]) << 24) + (uint8(bytes[1]) << 16) + (uint8(bytes[2]) << 8) + uint8(bytes[3]);
    }

    public static int bytesToLEInt(byte[] bytes) {
        return java.lang.Integer.reverseBytes(bytesToBEInt(bytes));
    }

    public static int getUint8(java.nio.ByteBuffer buffer, int position) {
        return uint8(buffer.get(position));
    }

    public static int getUint16(java.nio.ByteBuffer buffer, int position) {
        return uint16(buffer.getShort(position));
    }

    public static long getUint32(java.nio.ByteBuffer buffer, int position) {
        return uint32(buffer.getInt(position));
    }

    public static void put(java.nio.ByteBuffer buffer, int position, byte[] bytes) {
        int original = buffer.position();
        buffer.position(position);
        buffer.put(bytes);
        buffer.position(original);
    }

    public static boolean isBitSet(long flags, int bitIndex) {
        return (bitAt(bitIndex) & flags) != 0;
    }

    public static long bitAt(int bitIndex) {
        return 1 << bitIndex;
    }

    public static java.lang.String flagsToString(int flags, java.util.function.IntFunction<java.lang.String> getFlagName) {
        java.lang.StringBuilder builder = new java.lang.StringBuilder();
        int count = 0;
        while (flags != 0) {
            int flag = 1 << java.lang.Integer.numberOfTrailingZeros(flags);
            flags &= ~flag;
            if (count > 0) {
                builder.append(", ");
            }
            builder.append(getFlagName.apply(flag));
            count++;
        }
        android.text.TextUtils.wrap(builder, "[", "]");
        return builder.toString();
    }

    public static byte[] toBytes(long l) {
        return java.nio.ByteBuffer.allocate(8).putLong(l).array();
    }

    public static int flagsUpTo(int lastFlag) {
        if (lastFlag <= 0) {
            return 0;
        }
        return flagsUpTo(lastFlag >> 1) | lastFlag;
    }

    public static int flagsWithin(int firstFlag, int lastFlag) {
        return (flagsUpTo(lastFlag) & (~flagsUpTo(firstFlag))) | firstFlag;
    }
}
