package kotlin.comparisons;

/* JADX INFO: Access modifiers changed from: package-private */
/* JADX INFO: compiled from: _UComparisons.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000B\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0010\u001a\u001f\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0001H\u0007¢\u0006\u0004\b\u0004\u0010\u0005\u001a(\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u00012\u0006\u0010\u0006\u001a\u00020\u0001H\u0087\b¢\u0006\u0004\b\u0007\u0010\b\u001a#\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00012\n\u0010\t\u001a\u00020\n\"\u00020\u0001H\u0007¢\u0006\u0004\b\u000b\u0010\f\u001a\u001f\u0010\u0000\u001a\u00020\r2\u0006\u0010\u0002\u001a\u00020\r2\u0006\u0010\u0003\u001a\u00020\rH\u0007¢\u0006\u0004\b\u000e\u0010\u000f\u001a(\u0010\u0000\u001a\u00020\r2\u0006\u0010\u0002\u001a\u00020\r2\u0006\u0010\u0003\u001a\u00020\r2\u0006\u0010\u0006\u001a\u00020\rH\u0087\b¢\u0006\u0004\b\u0010\u0010\u0011\u001a#\u0010\u0000\u001a\u00020\r2\u0006\u0010\u0002\u001a\u00020\r2\n\u0010\t\u001a\u00020\u0012\"\u00020\rH\u0007¢\u0006\u0004\b\u0013\u0010\u0014\u001a\u001f\u0010\u0000\u001a\u00020\u00152\u0006\u0010\u0002\u001a\u00020\u00152\u0006\u0010\u0003\u001a\u00020\u0015H\u0007¢\u0006\u0004\b\u0016\u0010\u0017\u001a(\u0010\u0000\u001a\u00020\u00152\u0006\u0010\u0002\u001a\u00020\u00152\u0006\u0010\u0003\u001a\u00020\u00152\u0006\u0010\u0006\u001a\u00020\u0015H\u0087\b¢\u0006\u0004\b\u0018\u0010\u0019\u001a#\u0010\u0000\u001a\u00020\u00152\u0006\u0010\u0002\u001a\u00020\u00152\n\u0010\t\u001a\u00020\u001a\"\u00020\u0015H\u0007¢\u0006\u0004\b\u001b\u0010\u001c\u001a\u001f\u0010\u0000\u001a\u00020\u001d2\u0006\u0010\u0002\u001a\u00020\u001d2\u0006\u0010\u0003\u001a\u00020\u001dH\u0007¢\u0006\u0004\b\u001e\u0010\u001f\u001a(\u0010\u0000\u001a\u00020\u001d2\u0006\u0010\u0002\u001a\u00020\u001d2\u0006\u0010\u0003\u001a\u00020\u001d2\u0006\u0010\u0006\u001a\u00020\u001dH\u0087\b¢\u0006\u0004\b \u0010!\u001a#\u0010\u0000\u001a\u00020\u001d2\u0006\u0010\u0002\u001a\u00020\u001d2\n\u0010\t\u001a\u00020\"\"\u00020\u001dH\u0007¢\u0006\u0004\b#\u0010$\u001a\u001f\u0010%\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0001H\u0007¢\u0006\u0004\b&\u0010\u0005\u001a(\u0010%\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u00012\u0006\u0010\u0006\u001a\u00020\u0001H\u0087\b¢\u0006\u0004\b'\u0010\b\u001a#\u0010%\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00012\n\u0010\t\u001a\u00020\n\"\u00020\u0001H\u0007¢\u0006\u0004\b(\u0010\f\u001a\u001f\u0010%\u001a\u00020\r2\u0006\u0010\u0002\u001a\u00020\r2\u0006\u0010\u0003\u001a\u00020\rH\u0007¢\u0006\u0004\b)\u0010\u000f\u001a(\u0010%\u001a\u00020\r2\u0006\u0010\u0002\u001a\u00020\r2\u0006\u0010\u0003\u001a\u00020\r2\u0006\u0010\u0006\u001a\u00020\rH\u0087\b¢\u0006\u0004\b*\u0010\u0011\u001a#\u0010%\u001a\u00020\r2\u0006\u0010\u0002\u001a\u00020\r2\n\u0010\t\u001a\u00020\u0012\"\u00020\rH\u0007¢\u0006\u0004\b+\u0010\u0014\u001a\u001f\u0010%\u001a\u00020\u00152\u0006\u0010\u0002\u001a\u00020\u00152\u0006\u0010\u0003\u001a\u00020\u0015H\u0007¢\u0006\u0004\b,\u0010\u0017\u001a(\u0010%\u001a\u00020\u00152\u0006\u0010\u0002\u001a\u00020\u00152\u0006\u0010\u0003\u001a\u00020\u00152\u0006\u0010\u0006\u001a\u00020\u0015H\u0087\b¢\u0006\u0004\b-\u0010\u0019\u001a#\u0010%\u001a\u00020\u00152\u0006\u0010\u0002\u001a\u00020\u00152\n\u0010\t\u001a\u00020\u001a\"\u00020\u0015H\u0007¢\u0006\u0004\b.\u0010\u001c\u001a\u001f\u0010%\u001a\u00020\u001d2\u0006\u0010\u0002\u001a\u00020\u001d2\u0006\u0010\u0003\u001a\u00020\u001dH\u0007¢\u0006\u0004\b/\u0010\u001f\u001a(\u0010%\u001a\u00020\u001d2\u0006\u0010\u0002\u001a\u00020\u001d2\u0006\u0010\u0003\u001a\u00020\u001d2\u0006\u0010\u0006\u001a\u00020\u001dH\u0087\b¢\u0006\u0004\b0\u0010!\u001a#\u0010%\u001a\u00020\u001d2\u0006\u0010\u0002\u001a\u00020\u001d2\n\u0010\t\u001a\u00020\"\"\u00020\u001dH\u0007¢\u0006\u0004\b1\u0010$¨\u00062"}, d2 = {"maxOf", "Lkotlin/UByte;", com.android.server.wm.ActivityTaskManagerService.DUMP_ACTIVITIES_SHORT_CMD, "b", "maxOf-Kr8caGY", "(BB)B", "c", "maxOf-b33U2AM", "(BBB)B", "other", "Lkotlin/UByteArray;", "maxOf-Wr6uiD8", "(B[B)B", "Lkotlin/UInt;", "maxOf-J1ME1BU", "(II)I", "maxOf-WZ9TVnA", "(III)I", "Lkotlin/UIntArray;", "maxOf-Md2H83M", "(I[I)I", "Lkotlin/ULong;", "maxOf-eb3DHEI", "(JJ)J", "maxOf-sambcqE", "(JJJ)J", "Lkotlin/ULongArray;", "maxOf-R03FKyM", "(J[J)J", "Lkotlin/UShort;", "maxOf-5PvTz6A", "(SS)S", "maxOf-VKSA0NQ", "(SSS)S", "Lkotlin/UShortArray;", "maxOf-t1qELG4", "(S[S)S", "minOf", "minOf-Kr8caGY", "minOf-b33U2AM", "minOf-Wr6uiD8", "minOf-J1ME1BU", "minOf-WZ9TVnA", "minOf-Md2H83M", "minOf-eb3DHEI", "minOf-sambcqE", "minOf-R03FKyM", "minOf-5PvTz6A", "minOf-VKSA0NQ", "minOf-t1qELG4", "kotlin-stdlib"}, k = 5, mv = {1, 9, 0}, xi = 49, xs = "kotlin/comparisons/UComparisonsKt")
public class UComparisonsKt___UComparisonsKt {
    /* JADX INFO: renamed from: maxOf-J1ME1BU, reason: not valid java name */
    public static final int m12489maxOfJ1ME1BU(int a, int b) {
        return java.lang.Integer.compareUnsigned(a, b) >= 0 ? a : b;
    }

    /* JADX INFO: renamed from: maxOf-eb3DHEI, reason: not valid java name */
    public static final long m12497maxOfeb3DHEI(long a, long b) {
        return java.lang.Long.compareUnsigned(a, b) >= 0 ? a : b;
    }

    /* JADX INFO: renamed from: maxOf-Kr8caGY, reason: not valid java name */
    public static final byte m12490maxOfKr8caGY(byte a, byte b) {
        return kotlin.jvm.internal.Intrinsics.compare(a & 255, b & 255) >= 0 ? a : b;
    }

    /* JADX INFO: renamed from: maxOf-5PvTz6A, reason: not valid java name */
    public static final short m12488maxOf5PvTz6A(short a, short b) {
        return kotlin.jvm.internal.Intrinsics.compare(a & 65535, 65535 & b) >= 0 ? a : b;
    }

    /* JADX INFO: renamed from: maxOf-WZ9TVnA, reason: not valid java name */
    private static final int m12494maxOfWZ9TVnA(int a, int b, int c) {
        return kotlin.comparisons.UComparisonsKt.m12489maxOfJ1ME1BU(a, kotlin.comparisons.UComparisonsKt.m12489maxOfJ1ME1BU(b, c));
    }

    /* JADX INFO: renamed from: maxOf-sambcqE, reason: not valid java name */
    private static final long m12498maxOfsambcqE(long a, long b, long c) {
        return kotlin.comparisons.UComparisonsKt.m12497maxOfeb3DHEI(a, kotlin.comparisons.UComparisonsKt.m12497maxOfeb3DHEI(b, c));
    }

    /* JADX INFO: renamed from: maxOf-b33U2AM, reason: not valid java name */
    private static final byte m12496maxOfb33U2AM(byte a, byte b, byte c) {
        return kotlin.comparisons.UComparisonsKt.m12490maxOfKr8caGY(a, kotlin.comparisons.UComparisonsKt.m12490maxOfKr8caGY(b, c));
    }

    /* JADX INFO: renamed from: maxOf-VKSA0NQ, reason: not valid java name */
    private static final short m12493maxOfVKSA0NQ(short a, short b, short c) {
        return kotlin.comparisons.UComparisonsKt.m12488maxOf5PvTz6A(a, kotlin.comparisons.UComparisonsKt.m12488maxOf5PvTz6A(b, c));
    }

    /* JADX INFO: renamed from: maxOf-Md2H83M, reason: not valid java name */
    public static final int m12491maxOfMd2H83M(int a, int... other) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(other, "other");
        int max = a;
        int iM11463getSizeimpl = kotlin.UIntArray.m11463getSizeimpl(other);
        for (int i = 0; i < iM11463getSizeimpl; i++) {
            int e = kotlin.UIntArray.m11462getpVg5ArA(other, i);
            max = kotlin.comparisons.UComparisonsKt.m12489maxOfJ1ME1BU(max, e);
        }
        return max;
    }

    /* JADX INFO: renamed from: maxOf-R03FKyM, reason: not valid java name */
    public static final long m12492maxOfR03FKyM(long a, long... other) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(other, "other");
        long max = a;
        int iM11542getSizeimpl = kotlin.ULongArray.m11542getSizeimpl(other);
        for (int i = 0; i < iM11542getSizeimpl; i++) {
            long e = kotlin.ULongArray.m11541getsVKNKU(other, i);
            max = kotlin.comparisons.UComparisonsKt.m12497maxOfeb3DHEI(max, e);
        }
        return max;
    }

    /* JADX INFO: renamed from: maxOf-Wr6uiD8, reason: not valid java name */
    public static final byte m12495maxOfWr6uiD8(byte a, byte... other) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(other, "other");
        byte max = a;
        int iM11384getSizeimpl = kotlin.UByteArray.m11384getSizeimpl(other);
        for (int i = 0; i < iM11384getSizeimpl; i++) {
            byte e = kotlin.UByteArray.m11383getw2LRezQ(other, i);
            max = kotlin.comparisons.UComparisonsKt.m12490maxOfKr8caGY(max, e);
        }
        return max;
    }

    /* JADX INFO: renamed from: maxOf-t1qELG4, reason: not valid java name */
    public static final short m12499maxOft1qELG4(short a, short... other) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(other, "other");
        short max = a;
        int iM11647getSizeimpl = kotlin.UShortArray.m11647getSizeimpl(other);
        for (int i = 0; i < iM11647getSizeimpl; i++) {
            short e = kotlin.UShortArray.m11646getMh2AYeg(other, i);
            max = kotlin.comparisons.UComparisonsKt.m12488maxOf5PvTz6A(max, e);
        }
        return max;
    }

    /* JADX INFO: renamed from: minOf-J1ME1BU, reason: not valid java name */
    public static final int m12501minOfJ1ME1BU(int a, int b) {
        return java.lang.Integer.compareUnsigned(a, b) <= 0 ? a : b;
    }

    /* JADX INFO: renamed from: minOf-eb3DHEI, reason: not valid java name */
    public static final long m12509minOfeb3DHEI(long a, long b) {
        return java.lang.Long.compareUnsigned(a, b) <= 0 ? a : b;
    }

    /* JADX INFO: renamed from: minOf-Kr8caGY, reason: not valid java name */
    public static final byte m12502minOfKr8caGY(byte a, byte b) {
        return kotlin.jvm.internal.Intrinsics.compare(a & 255, b & 255) <= 0 ? a : b;
    }

    /* JADX INFO: renamed from: minOf-5PvTz6A, reason: not valid java name */
    public static final short m12500minOf5PvTz6A(short a, short b) {
        return kotlin.jvm.internal.Intrinsics.compare(a & 65535, 65535 & b) <= 0 ? a : b;
    }

    /* JADX INFO: renamed from: minOf-WZ9TVnA, reason: not valid java name */
    private static final int m12506minOfWZ9TVnA(int a, int b, int c) {
        return kotlin.comparisons.UComparisonsKt.m12501minOfJ1ME1BU(a, kotlin.comparisons.UComparisonsKt.m12501minOfJ1ME1BU(b, c));
    }

    /* JADX INFO: renamed from: minOf-sambcqE, reason: not valid java name */
    private static final long m12510minOfsambcqE(long a, long b, long c) {
        return kotlin.comparisons.UComparisonsKt.m12509minOfeb3DHEI(a, kotlin.comparisons.UComparisonsKt.m12509minOfeb3DHEI(b, c));
    }

    /* JADX INFO: renamed from: minOf-b33U2AM, reason: not valid java name */
    private static final byte m12508minOfb33U2AM(byte a, byte b, byte c) {
        return kotlin.comparisons.UComparisonsKt.m12502minOfKr8caGY(a, kotlin.comparisons.UComparisonsKt.m12502minOfKr8caGY(b, c));
    }

    /* JADX INFO: renamed from: minOf-VKSA0NQ, reason: not valid java name */
    private static final short m12505minOfVKSA0NQ(short a, short b, short c) {
        return kotlin.comparisons.UComparisonsKt.m12500minOf5PvTz6A(a, kotlin.comparisons.UComparisonsKt.m12500minOf5PvTz6A(b, c));
    }

    /* JADX INFO: renamed from: minOf-Md2H83M, reason: not valid java name */
    public static final int m12503minOfMd2H83M(int a, int... other) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(other, "other");
        int min = a;
        int iM11463getSizeimpl = kotlin.UIntArray.m11463getSizeimpl(other);
        for (int i = 0; i < iM11463getSizeimpl; i++) {
            int e = kotlin.UIntArray.m11462getpVg5ArA(other, i);
            min = kotlin.comparisons.UComparisonsKt.m12501minOfJ1ME1BU(min, e);
        }
        return min;
    }

    /* JADX INFO: renamed from: minOf-R03FKyM, reason: not valid java name */
    public static final long m12504minOfR03FKyM(long a, long... other) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(other, "other");
        long min = a;
        int iM11542getSizeimpl = kotlin.ULongArray.m11542getSizeimpl(other);
        for (int i = 0; i < iM11542getSizeimpl; i++) {
            long e = kotlin.ULongArray.m11541getsVKNKU(other, i);
            min = kotlin.comparisons.UComparisonsKt.m12509minOfeb3DHEI(min, e);
        }
        return min;
    }

    /* JADX INFO: renamed from: minOf-Wr6uiD8, reason: not valid java name */
    public static final byte m12507minOfWr6uiD8(byte a, byte... other) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(other, "other");
        byte min = a;
        int iM11384getSizeimpl = kotlin.UByteArray.m11384getSizeimpl(other);
        for (int i = 0; i < iM11384getSizeimpl; i++) {
            byte e = kotlin.UByteArray.m11383getw2LRezQ(other, i);
            min = kotlin.comparisons.UComparisonsKt.m12502minOfKr8caGY(min, e);
        }
        return min;
    }

    /* JADX INFO: renamed from: minOf-t1qELG4, reason: not valid java name */
    public static final short m12511minOft1qELG4(short a, short... other) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(other, "other");
        short min = a;
        int iM11647getSizeimpl = kotlin.UShortArray.m11647getSizeimpl(other);
        for (int i = 0; i < iM11647getSizeimpl; i++) {
            short e = kotlin.UShortArray.m11646getMh2AYeg(other, i);
            min = kotlin.comparisons.UComparisonsKt.m12500minOf5PvTz6A(min, e);
        }
        return min;
    }
}
