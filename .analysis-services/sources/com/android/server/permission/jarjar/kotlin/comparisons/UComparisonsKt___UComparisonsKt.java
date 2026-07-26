package com.android.server.permission.jarjar.kotlin.comparisons;

/* JADX INFO: Access modifiers changed from: package-private */
/* JADX INFO: compiled from: _UComparisons.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000B\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0010\u001a\u001f\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0001H\u0007¢\u0006\u0004\b\u0004\u0010\u0005\u001a(\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u00012\u0006\u0010\u0006\u001a\u00020\u0001H\u0087\b¢\u0006\u0004\b\u0007\u0010\b\u001a#\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00012\n\u0010\t\u001a\u00020\n\"\u00020\u0001H\u0007¢\u0006\u0004\b\u000b\u0010\f\u001a\u001f\u0010\u0000\u001a\u00020\r2\u0006\u0010\u0002\u001a\u00020\r2\u0006\u0010\u0003\u001a\u00020\rH\u0007¢\u0006\u0004\b\u000e\u0010\u000f\u001a(\u0010\u0000\u001a\u00020\r2\u0006\u0010\u0002\u001a\u00020\r2\u0006\u0010\u0003\u001a\u00020\r2\u0006\u0010\u0006\u001a\u00020\rH\u0087\b¢\u0006\u0004\b\u0010\u0010\u0011\u001a#\u0010\u0000\u001a\u00020\r2\u0006\u0010\u0002\u001a\u00020\r2\n\u0010\t\u001a\u00020\u0012\"\u00020\rH\u0007¢\u0006\u0004\b\u0013\u0010\u0014\u001a\u001f\u0010\u0000\u001a\u00020\u00152\u0006\u0010\u0002\u001a\u00020\u00152\u0006\u0010\u0003\u001a\u00020\u0015H\u0007¢\u0006\u0004\b\u0016\u0010\u0017\u001a(\u0010\u0000\u001a\u00020\u00152\u0006\u0010\u0002\u001a\u00020\u00152\u0006\u0010\u0003\u001a\u00020\u00152\u0006\u0010\u0006\u001a\u00020\u0015H\u0087\b¢\u0006\u0004\b\u0018\u0010\u0019\u001a#\u0010\u0000\u001a\u00020\u00152\u0006\u0010\u0002\u001a\u00020\u00152\n\u0010\t\u001a\u00020\u001a\"\u00020\u0015H\u0007¢\u0006\u0004\b\u001b\u0010\u001c\u001a\u001f\u0010\u0000\u001a\u00020\u001d2\u0006\u0010\u0002\u001a\u00020\u001d2\u0006\u0010\u0003\u001a\u00020\u001dH\u0007¢\u0006\u0004\b\u001e\u0010\u001f\u001a(\u0010\u0000\u001a\u00020\u001d2\u0006\u0010\u0002\u001a\u00020\u001d2\u0006\u0010\u0003\u001a\u00020\u001d2\u0006\u0010\u0006\u001a\u00020\u001dH\u0087\b¢\u0006\u0004\b \u0010!\u001a#\u0010\u0000\u001a\u00020\u001d2\u0006\u0010\u0002\u001a\u00020\u001d2\n\u0010\t\u001a\u00020\"\"\u00020\u001dH\u0007¢\u0006\u0004\b#\u0010$\u001a\u001f\u0010%\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0001H\u0007¢\u0006\u0004\b&\u0010\u0005\u001a(\u0010%\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u00012\u0006\u0010\u0006\u001a\u00020\u0001H\u0087\b¢\u0006\u0004\b'\u0010\b\u001a#\u0010%\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00012\n\u0010\t\u001a\u00020\n\"\u00020\u0001H\u0007¢\u0006\u0004\b(\u0010\f\u001a\u001f\u0010%\u001a\u00020\r2\u0006\u0010\u0002\u001a\u00020\r2\u0006\u0010\u0003\u001a\u00020\rH\u0007¢\u0006\u0004\b)\u0010\u000f\u001a(\u0010%\u001a\u00020\r2\u0006\u0010\u0002\u001a\u00020\r2\u0006\u0010\u0003\u001a\u00020\r2\u0006\u0010\u0006\u001a\u00020\rH\u0087\b¢\u0006\u0004\b*\u0010\u0011\u001a#\u0010%\u001a\u00020\r2\u0006\u0010\u0002\u001a\u00020\r2\n\u0010\t\u001a\u00020\u0012\"\u00020\rH\u0007¢\u0006\u0004\b+\u0010\u0014\u001a\u001f\u0010%\u001a\u00020\u00152\u0006\u0010\u0002\u001a\u00020\u00152\u0006\u0010\u0003\u001a\u00020\u0015H\u0007¢\u0006\u0004\b,\u0010\u0017\u001a(\u0010%\u001a\u00020\u00152\u0006\u0010\u0002\u001a\u00020\u00152\u0006\u0010\u0003\u001a\u00020\u00152\u0006\u0010\u0006\u001a\u00020\u0015H\u0087\b¢\u0006\u0004\b-\u0010\u0019\u001a#\u0010%\u001a\u00020\u00152\u0006\u0010\u0002\u001a\u00020\u00152\n\u0010\t\u001a\u00020\u001a\"\u00020\u0015H\u0007¢\u0006\u0004\b.\u0010\u001c\u001a\u001f\u0010%\u001a\u00020\u001d2\u0006\u0010\u0002\u001a\u00020\u001d2\u0006\u0010\u0003\u001a\u00020\u001dH\u0007¢\u0006\u0004\b/\u0010\u001f\u001a(\u0010%\u001a\u00020\u001d2\u0006\u0010\u0002\u001a\u00020\u001d2\u0006\u0010\u0003\u001a\u00020\u001d2\u0006\u0010\u0006\u001a\u00020\u001dH\u0087\b¢\u0006\u0004\b0\u0010!\u001a#\u0010%\u001a\u00020\u001d2\u0006\u0010\u0002\u001a\u00020\u001d2\n\u0010\t\u001a\u00020\"\"\u00020\u001dH\u0007¢\u0006\u0004\b1\u0010$¨\u00062"}, d2 = {"maxOf", "Lkotlin/UByte;", com.android.server.wm.ActivityTaskManagerService.DUMP_ACTIVITIES_SHORT_CMD, "b", "maxOf-Kr8caGY", "(BB)B", "c", "maxOf-b33U2AM", "(BBB)B", "other", "Lkotlin/UByteArray;", "maxOf-Wr6uiD8", "(B[B)B", "Lkotlin/UInt;", "maxOf-J1ME1BU", "(II)I", "maxOf-WZ9TVnA", "(III)I", "Lkotlin/UIntArray;", "maxOf-Md2H83M", "(I[I)I", "Lkotlin/ULong;", "maxOf-eb3DHEI", "(JJ)J", "maxOf-sambcqE", "(JJJ)J", "Lkotlin/ULongArray;", "maxOf-R03FKyM", "(J[J)J", "Lkotlin/UShort;", "maxOf-5PvTz6A", "(SS)S", "maxOf-VKSA0NQ", "(SSS)S", "Lkotlin/UShortArray;", "maxOf-t1qELG4", "(S[S)S", "minOf", "minOf-Kr8caGY", "minOf-b33U2AM", "minOf-Wr6uiD8", "minOf-J1ME1BU", "minOf-WZ9TVnA", "minOf-Md2H83M", "minOf-eb3DHEI", "minOf-sambcqE", "minOf-R03FKyM", "minOf-5PvTz6A", "minOf-VKSA0NQ", "minOf-t1qELG4", "kotlin-stdlib"}, k = 5, mv = {1, 9, 0}, xi = 49, xs = "com/android/server/permission/jarjar/kotlin/comparisons/UComparisonsKt")
public class UComparisonsKt___UComparisonsKt {
    /* JADX INFO: renamed from: maxOf-J1ME1BU, reason: not valid java name */
    public static final int m7271maxOfJ1ME1BU(int a, int b) {
        return java.lang.Integer.compareUnsigned(a, b) >= 0 ? a : b;
    }

    /* JADX INFO: renamed from: maxOf-eb3DHEI, reason: not valid java name */
    public static final long m7279maxOfeb3DHEI(long a, long b) {
        return java.lang.Long.compareUnsigned(a, b) >= 0 ? a : b;
    }

    /* JADX INFO: renamed from: maxOf-Kr8caGY, reason: not valid java name */
    public static final byte m7272maxOfKr8caGY(byte a, byte b) {
        return com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.compare(a & 255, b & 255) >= 0 ? a : b;
    }

    /* JADX INFO: renamed from: maxOf-5PvTz6A, reason: not valid java name */
    public static final short m7270maxOf5PvTz6A(short a, short b) {
        return com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.compare(a & 65535, 65535 & b) >= 0 ? a : b;
    }

    /* JADX INFO: renamed from: maxOf-WZ9TVnA, reason: not valid java name */
    private static final int m7276maxOfWZ9TVnA(int a, int b, int c) {
        return com.android.server.permission.jarjar.kotlin.comparisons.UComparisonsKt.m7271maxOfJ1ME1BU(a, com.android.server.permission.jarjar.kotlin.comparisons.UComparisonsKt.m7271maxOfJ1ME1BU(b, c));
    }

    /* JADX INFO: renamed from: maxOf-sambcqE, reason: not valid java name */
    private static final long m7280maxOfsambcqE(long a, long b, long c) {
        return com.android.server.permission.jarjar.kotlin.comparisons.UComparisonsKt.m7279maxOfeb3DHEI(a, com.android.server.permission.jarjar.kotlin.comparisons.UComparisonsKt.m7279maxOfeb3DHEI(b, c));
    }

    /* JADX INFO: renamed from: maxOf-b33U2AM, reason: not valid java name */
    private static final byte m7278maxOfb33U2AM(byte a, byte b, byte c) {
        return com.android.server.permission.jarjar.kotlin.comparisons.UComparisonsKt.m7272maxOfKr8caGY(a, com.android.server.permission.jarjar.kotlin.comparisons.UComparisonsKt.m7272maxOfKr8caGY(b, c));
    }

    /* JADX INFO: renamed from: maxOf-VKSA0NQ, reason: not valid java name */
    private static final short m7275maxOfVKSA0NQ(short a, short b, short c) {
        return com.android.server.permission.jarjar.kotlin.comparisons.UComparisonsKt.m7270maxOf5PvTz6A(a, com.android.server.permission.jarjar.kotlin.comparisons.UComparisonsKt.m7270maxOf5PvTz6A(b, c));
    }

    /* JADX INFO: renamed from: maxOf-Md2H83M, reason: not valid java name */
    public static final int m7273maxOfMd2H83M(int a, int... other) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(other, "other");
        int max = a;
        int iM6245getSizeimpl = com.android.server.permission.jarjar.kotlin.UIntArray.m6245getSizeimpl(other);
        for (int i = 0; i < iM6245getSizeimpl; i++) {
            int e = com.android.server.permission.jarjar.kotlin.UIntArray.m6244getpVg5ArA(other, i);
            max = com.android.server.permission.jarjar.kotlin.comparisons.UComparisonsKt.m7271maxOfJ1ME1BU(max, e);
        }
        return max;
    }

    /* JADX INFO: renamed from: maxOf-R03FKyM, reason: not valid java name */
    public static final long m7274maxOfR03FKyM(long a, long... other) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(other, "other");
        long max = a;
        int iM6324getSizeimpl = com.android.server.permission.jarjar.kotlin.ULongArray.m6324getSizeimpl(other);
        for (int i = 0; i < iM6324getSizeimpl; i++) {
            long e = com.android.server.permission.jarjar.kotlin.ULongArray.m6323getsVKNKU(other, i);
            max = com.android.server.permission.jarjar.kotlin.comparisons.UComparisonsKt.m7279maxOfeb3DHEI(max, e);
        }
        return max;
    }

    /* JADX INFO: renamed from: maxOf-Wr6uiD8, reason: not valid java name */
    public static final byte m7277maxOfWr6uiD8(byte a, byte... other) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(other, "other");
        byte max = a;
        int iM6166getSizeimpl = com.android.server.permission.jarjar.kotlin.UByteArray.m6166getSizeimpl(other);
        for (int i = 0; i < iM6166getSizeimpl; i++) {
            byte e = com.android.server.permission.jarjar.kotlin.UByteArray.m6165getw2LRezQ(other, i);
            max = com.android.server.permission.jarjar.kotlin.comparisons.UComparisonsKt.m7272maxOfKr8caGY(max, e);
        }
        return max;
    }

    /* JADX INFO: renamed from: maxOf-t1qELG4, reason: not valid java name */
    public static final short m7281maxOft1qELG4(short a, short... other) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(other, "other");
        short max = a;
        int iM6429getSizeimpl = com.android.server.permission.jarjar.kotlin.UShortArray.m6429getSizeimpl(other);
        for (int i = 0; i < iM6429getSizeimpl; i++) {
            short e = com.android.server.permission.jarjar.kotlin.UShortArray.m6428getMh2AYeg(other, i);
            max = com.android.server.permission.jarjar.kotlin.comparisons.UComparisonsKt.m7270maxOf5PvTz6A(max, e);
        }
        return max;
    }

    /* JADX INFO: renamed from: minOf-J1ME1BU, reason: not valid java name */
    public static final int m7283minOfJ1ME1BU(int a, int b) {
        return java.lang.Integer.compareUnsigned(a, b) <= 0 ? a : b;
    }

    /* JADX INFO: renamed from: minOf-eb3DHEI, reason: not valid java name */
    public static final long m7291minOfeb3DHEI(long a, long b) {
        return java.lang.Long.compareUnsigned(a, b) <= 0 ? a : b;
    }

    /* JADX INFO: renamed from: minOf-Kr8caGY, reason: not valid java name */
    public static final byte m7284minOfKr8caGY(byte a, byte b) {
        return com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.compare(a & 255, b & 255) <= 0 ? a : b;
    }

    /* JADX INFO: renamed from: minOf-5PvTz6A, reason: not valid java name */
    public static final short m7282minOf5PvTz6A(short a, short b) {
        return com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.compare(a & 65535, 65535 & b) <= 0 ? a : b;
    }

    /* JADX INFO: renamed from: minOf-WZ9TVnA, reason: not valid java name */
    private static final int m7288minOfWZ9TVnA(int a, int b, int c) {
        return com.android.server.permission.jarjar.kotlin.comparisons.UComparisonsKt.m7283minOfJ1ME1BU(a, com.android.server.permission.jarjar.kotlin.comparisons.UComparisonsKt.m7283minOfJ1ME1BU(b, c));
    }

    /* JADX INFO: renamed from: minOf-sambcqE, reason: not valid java name */
    private static final long m7292minOfsambcqE(long a, long b, long c) {
        return com.android.server.permission.jarjar.kotlin.comparisons.UComparisonsKt.m7291minOfeb3DHEI(a, com.android.server.permission.jarjar.kotlin.comparisons.UComparisonsKt.m7291minOfeb3DHEI(b, c));
    }

    /* JADX INFO: renamed from: minOf-b33U2AM, reason: not valid java name */
    private static final byte m7290minOfb33U2AM(byte a, byte b, byte c) {
        return com.android.server.permission.jarjar.kotlin.comparisons.UComparisonsKt.m7284minOfKr8caGY(a, com.android.server.permission.jarjar.kotlin.comparisons.UComparisonsKt.m7284minOfKr8caGY(b, c));
    }

    /* JADX INFO: renamed from: minOf-VKSA0NQ, reason: not valid java name */
    private static final short m7287minOfVKSA0NQ(short a, short b, short c) {
        return com.android.server.permission.jarjar.kotlin.comparisons.UComparisonsKt.m7282minOf5PvTz6A(a, com.android.server.permission.jarjar.kotlin.comparisons.UComparisonsKt.m7282minOf5PvTz6A(b, c));
    }

    /* JADX INFO: renamed from: minOf-Md2H83M, reason: not valid java name */
    public static final int m7285minOfMd2H83M(int a, int... other) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(other, "other");
        int min = a;
        int iM6245getSizeimpl = com.android.server.permission.jarjar.kotlin.UIntArray.m6245getSizeimpl(other);
        for (int i = 0; i < iM6245getSizeimpl; i++) {
            int e = com.android.server.permission.jarjar.kotlin.UIntArray.m6244getpVg5ArA(other, i);
            min = com.android.server.permission.jarjar.kotlin.comparisons.UComparisonsKt.m7283minOfJ1ME1BU(min, e);
        }
        return min;
    }

    /* JADX INFO: renamed from: minOf-R03FKyM, reason: not valid java name */
    public static final long m7286minOfR03FKyM(long a, long... other) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(other, "other");
        long min = a;
        int iM6324getSizeimpl = com.android.server.permission.jarjar.kotlin.ULongArray.m6324getSizeimpl(other);
        for (int i = 0; i < iM6324getSizeimpl; i++) {
            long e = com.android.server.permission.jarjar.kotlin.ULongArray.m6323getsVKNKU(other, i);
            min = com.android.server.permission.jarjar.kotlin.comparisons.UComparisonsKt.m7291minOfeb3DHEI(min, e);
        }
        return min;
    }

    /* JADX INFO: renamed from: minOf-Wr6uiD8, reason: not valid java name */
    public static final byte m7289minOfWr6uiD8(byte a, byte... other) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(other, "other");
        byte min = a;
        int iM6166getSizeimpl = com.android.server.permission.jarjar.kotlin.UByteArray.m6166getSizeimpl(other);
        for (int i = 0; i < iM6166getSizeimpl; i++) {
            byte e = com.android.server.permission.jarjar.kotlin.UByteArray.m6165getw2LRezQ(other, i);
            min = com.android.server.permission.jarjar.kotlin.comparisons.UComparisonsKt.m7284minOfKr8caGY(min, e);
        }
        return min;
    }

    /* JADX INFO: renamed from: minOf-t1qELG4, reason: not valid java name */
    public static final short m7293minOft1qELG4(short a, short... other) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(other, "other");
        short min = a;
        int iM6429getSizeimpl = com.android.server.permission.jarjar.kotlin.UShortArray.m6429getSizeimpl(other);
        for (int i = 0; i < iM6429getSizeimpl; i++) {
            short e = com.android.server.permission.jarjar.kotlin.UShortArray.m6428getMh2AYeg(other, i);
            min = com.android.server.permission.jarjar.kotlin.comparisons.UComparisonsKt.m7282minOf5PvTz6A(min, e);
        }
        return min;
    }
}
