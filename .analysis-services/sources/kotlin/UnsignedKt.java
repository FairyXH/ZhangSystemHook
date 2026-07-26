package kotlin;

/* JADX INFO: compiled from: UnsignedUtils.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u00000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\t\n\u0002\u0010\t\n\u0002\b\u0007\n\u0002\u0010\u000e\n\u0002\b\u0002\u001a\u0015\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u0001¢\u0006\u0002\u0010\u0004\u001a\u0015\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0002\u001a\u00020\u0003H\u0001¢\u0006\u0002\u0010\u0007\u001a\u0018\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\t2\u0006\u0010\u000b\u001a\u00020\tH\u0001\u001a\u001f\u0010\f\u001a\u00020\u00012\u0006\u0010\n\u001a\u00020\u00012\u0006\u0010\u000b\u001a\u00020\u0001H\u0001¢\u0006\u0004\b\r\u0010\u000e\u001a\u001f\u0010\u000f\u001a\u00020\u00012\u0006\u0010\n\u001a\u00020\u00012\u0006\u0010\u000b\u001a\u00020\u0001H\u0001¢\u0006\u0004\b\u0010\u0010\u000e\u001a\u0010\u0010\u0011\u001a\u00020\u00032\u0006\u0010\u0002\u001a\u00020\tH\u0001\u001a\u0018\u0010\u0012\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u00132\u0006\u0010\u000b\u001a\u00020\u0013H\u0001\u001a\u001f\u0010\u0014\u001a\u00020\u00062\u0006\u0010\n\u001a\u00020\u00062\u0006\u0010\u000b\u001a\u00020\u0006H\u0001¢\u0006\u0004\b\u0015\u0010\u0016\u001a\u001f\u0010\u0017\u001a\u00020\u00062\u0006\u0010\n\u001a\u00020\u00062\u0006\u0010\u000b\u001a\u00020\u0006H\u0001¢\u0006\u0004\b\u0018\u0010\u0016\u001a\u0010\u0010\u0019\u001a\u00020\u00032\u0006\u0010\u0002\u001a\u00020\u0013H\u0001\u001a\u0010\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u0002\u001a\u00020\u0013H\u0000\u001a\u0018\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u0002\u001a\u00020\u00132\u0006\u0010\u001c\u001a\u00020\tH\u0000¨\u0006\u001d"}, d2 = {"doubleToUInt", "Lkotlin/UInt;", "v", "", "(D)I", "doubleToULong", "Lkotlin/ULong;", "(D)J", "uintCompare", "", "v1", "v2", "uintDivide", "uintDivide-J1ME1BU", "(II)I", "uintRemainder", "uintRemainder-J1ME1BU", "uintToDouble", "ulongCompare", "", "ulongDivide", "ulongDivide-eb3DHEI", "(JJ)J", "ulongRemainder", "ulongRemainder-eb3DHEI", "ulongToDouble", "ulongToString", "", "base", "kotlin-stdlib"}, k = 2, mv = {1, 9, 0}, xi = 48)
public final class UnsignedKt {
    public static final int uintCompare(int v1, int v2) {
        return kotlin.jvm.internal.Intrinsics.compare(v1 ^ Integer.MIN_VALUE, Integer.MIN_VALUE ^ v2);
    }

    public static final int ulongCompare(long v1, long v2) {
        return kotlin.jvm.internal.Intrinsics.compare(v1 ^ Long.MIN_VALUE, Long.MIN_VALUE ^ v2);
    }

    /* JADX INFO: renamed from: uintDivide-J1ME1BU, reason: not valid java name */
    public static final int m11658uintDivideJ1ME1BU(int v1, int v2) {
        return kotlin.UInt.m11402constructorimpl((int) ((((long) v1) & 4294967295L) / (4294967295L & ((long) v2))));
    }

    /* JADX INFO: renamed from: uintRemainder-J1ME1BU, reason: not valid java name */
    public static final int m11659uintRemainderJ1ME1BU(int v1, int v2) {
        return kotlin.UInt.m11402constructorimpl((int) ((((long) v1) & 4294967295L) % (4294967295L & ((long) v2))));
    }

    /* JADX INFO: renamed from: ulongDivide-eb3DHEI, reason: not valid java name */
    public static final long m11660ulongDivideeb3DHEI(long v1, long v2) {
        if (v2 < 0) {
            return kotlin.ULong.m11481constructorimpl(java.lang.Long.compareUnsigned(v1, v2) >= 0 ? 1L : 0L);
        }
        if (v1 >= 0) {
            return kotlin.ULong.m11481constructorimpl(v1 / v2);
        }
        long quotient = ((v1 >>> 1) / v2) << 1;
        long rem = v1 - (quotient * v2);
        return kotlin.ULong.m11481constructorimpl(((long) (java.lang.Long.compareUnsigned(kotlin.ULong.m11481constructorimpl(rem), kotlin.ULong.m11481constructorimpl(v2)) < 0 ? 0 : 1)) + quotient);
    }

    /* JADX INFO: renamed from: ulongRemainder-eb3DHEI, reason: not valid java name */
    public static final long m11661ulongRemaindereb3DHEI(long v1, long v2) {
        long j = 0;
        if (v2 < 0) {
            if (java.lang.Long.compareUnsigned(v1, v2) < 0) {
                return v1;
            }
            return kotlin.ULong.m11481constructorimpl(v1 - v2);
        }
        if (v1 >= 0) {
            return kotlin.ULong.m11481constructorimpl(v1 % v2);
        }
        long quotient = ((v1 >>> 1) / v2) << 1;
        long rem = v1 - (quotient * v2);
        if (java.lang.Long.compareUnsigned(kotlin.ULong.m11481constructorimpl(rem), kotlin.ULong.m11481constructorimpl(v2)) >= 0) {
            j = v2;
        }
        return kotlin.ULong.m11481constructorimpl(rem - j);
    }

    public static final int doubleToUInt(double v) {
        if (java.lang.Double.isNaN(v) || v <= uintToDouble(0)) {
            return 0;
        }
        if (v >= uintToDouble(-1)) {
            return -1;
        }
        return v <= 2.147483647E9d ? kotlin.UInt.m11402constructorimpl((int) v) : kotlin.UInt.m11402constructorimpl(kotlin.UInt.m11402constructorimpl((int) (v - ((double) Integer.MAX_VALUE))) + kotlin.UInt.m11402constructorimpl(Integer.MAX_VALUE));
    }

    public static final long doubleToULong(double v) {
        if (java.lang.Double.isNaN(v) || v <= ulongToDouble(0L)) {
            return 0L;
        }
        if (v >= ulongToDouble(-1L)) {
            return -1L;
        }
        return v < 9.223372036854776E18d ? kotlin.ULong.m11481constructorimpl((long) v) : kotlin.ULong.m11481constructorimpl(kotlin.ULong.m11481constructorimpl((long) (v - 9.223372036854776E18d)) - Long.MIN_VALUE);
    }

    public static final double uintToDouble(int v) {
        return ((double) (Integer.MAX_VALUE & v)) + (((double) ((v >>> 31) << 30)) * ((double) 2));
    }

    public static final double ulongToDouble(long v) {
        return ((v >>> 11) * ((double) 2048)) + (2047 & v);
    }

    public static final java.lang.String ulongToString(long v) {
        return ulongToString(v, 10);
    }

    public static final java.lang.String ulongToString(long v, int base) {
        if (v >= 0) {
            java.lang.String string = java.lang.Long.toString(v, kotlin.text.CharsKt.checkRadix(base));
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(string, "toString(...)");
            return string;
        }
        long quotient = ((v >>> 1) / ((long) base)) << 1;
        long rem = v - (((long) base) * quotient);
        if (rem >= base) {
            rem -= (long) base;
            quotient++;
        }
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        java.lang.String string2 = java.lang.Long.toString(quotient, kotlin.text.CharsKt.checkRadix(base));
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(string2, "toString(...)");
        java.lang.StringBuilder sbAppend = sb.append(string2);
        java.lang.String string3 = java.lang.Long.toString(rem, kotlin.text.CharsKt.checkRadix(base));
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(string3, "toString(...)");
        return sbAppend.append(string3).toString();
    }
}
