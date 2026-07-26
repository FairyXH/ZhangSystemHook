package com.android.server.permission.jarjar.kotlin.comparisons;

/* JADX INFO: Access modifiers changed from: package-private */
/* JADX INFO: compiled from: _ComparisonsJvm.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000F\n\u0002\b\u0002\n\u0002\u0010\u000f\n\u0002\b\u0006\n\u0002\u0010\u0011\n\u0000\n\u0002\u0010\u0005\n\u0002\u0010\u0012\n\u0002\u0010\u0006\n\u0002\u0010\u0013\n\u0002\u0010\u0007\n\u0002\u0010\u0014\n\u0002\u0010\b\n\u0002\u0010\u0015\n\u0002\u0010\t\n\u0002\u0010\u0016\n\u0002\u0010\n\n\u0002\u0010\u0017\n\u0002\b\u0002\u001a-\u0010\u0000\u001a\u0002H\u0001\"\u000e\b\u0000\u0010\u0001*\b\u0012\u0004\u0012\u0002H\u00010\u00022\u0006\u0010\u0003\u001a\u0002H\u00012\u0006\u0010\u0004\u001a\u0002H\u0001H\u0007¢\u0006\u0002\u0010\u0005\u001a5\u0010\u0000\u001a\u0002H\u0001\"\u000e\b\u0000\u0010\u0001*\b\u0012\u0004\u0012\u0002H\u00010\u00022\u0006\u0010\u0003\u001a\u0002H\u00012\u0006\u0010\u0004\u001a\u0002H\u00012\u0006\u0010\u0006\u001a\u0002H\u0001H\u0007¢\u0006\u0002\u0010\u0007\u001a9\u0010\u0000\u001a\u0002H\u0001\"\u000e\b\u0000\u0010\u0001*\b\u0012\u0004\u0012\u0002H\u00010\u00022\u0006\u0010\u0003\u001a\u0002H\u00012\u0012\u0010\b\u001a\n\u0012\u0006\b\u0001\u0012\u0002H\u00010\t\"\u0002H\u0001H\u0007¢\u0006\u0002\u0010\n\u001a\u0019\u0010\u0000\u001a\u00020\u000b2\u0006\u0010\u0003\u001a\u00020\u000b2\u0006\u0010\u0004\u001a\u00020\u000bH\u0087\b\u001a!\u0010\u0000\u001a\u00020\u000b2\u0006\u0010\u0003\u001a\u00020\u000b2\u0006\u0010\u0004\u001a\u00020\u000b2\u0006\u0010\u0006\u001a\u00020\u000bH\u0087\b\u001a\u001c\u0010\u0000\u001a\u00020\u000b2\u0006\u0010\u0003\u001a\u00020\u000b2\n\u0010\b\u001a\u00020\f\"\u00020\u000bH\u0007\u001a\u0019\u0010\u0000\u001a\u00020\r2\u0006\u0010\u0003\u001a\u00020\r2\u0006\u0010\u0004\u001a\u00020\rH\u0087\b\u001a!\u0010\u0000\u001a\u00020\r2\u0006\u0010\u0003\u001a\u00020\r2\u0006\u0010\u0004\u001a\u00020\r2\u0006\u0010\u0006\u001a\u00020\rH\u0087\b\u001a\u001c\u0010\u0000\u001a\u00020\r2\u0006\u0010\u0003\u001a\u00020\r2\n\u0010\b\u001a\u00020\u000e\"\u00020\rH\u0007\u001a\u0019\u0010\u0000\u001a\u00020\u000f2\u0006\u0010\u0003\u001a\u00020\u000f2\u0006\u0010\u0004\u001a\u00020\u000fH\u0087\b\u001a!\u0010\u0000\u001a\u00020\u000f2\u0006\u0010\u0003\u001a\u00020\u000f2\u0006\u0010\u0004\u001a\u00020\u000f2\u0006\u0010\u0006\u001a\u00020\u000fH\u0087\b\u001a\u001c\u0010\u0000\u001a\u00020\u000f2\u0006\u0010\u0003\u001a\u00020\u000f2\n\u0010\b\u001a\u00020\u0010\"\u00020\u000fH\u0007\u001a\u0019\u0010\u0000\u001a\u00020\u00112\u0006\u0010\u0003\u001a\u00020\u00112\u0006\u0010\u0004\u001a\u00020\u0011H\u0087\b\u001a!\u0010\u0000\u001a\u00020\u00112\u0006\u0010\u0003\u001a\u00020\u00112\u0006\u0010\u0004\u001a\u00020\u00112\u0006\u0010\u0006\u001a\u00020\u0011H\u0087\b\u001a\u001c\u0010\u0000\u001a\u00020\u00112\u0006\u0010\u0003\u001a\u00020\u00112\n\u0010\b\u001a\u00020\u0012\"\u00020\u0011H\u0007\u001a\u0019\u0010\u0000\u001a\u00020\u00132\u0006\u0010\u0003\u001a\u00020\u00132\u0006\u0010\u0004\u001a\u00020\u0013H\u0087\b\u001a!\u0010\u0000\u001a\u00020\u00132\u0006\u0010\u0003\u001a\u00020\u00132\u0006\u0010\u0004\u001a\u00020\u00132\u0006\u0010\u0006\u001a\u00020\u0013H\u0087\b\u001a\u001c\u0010\u0000\u001a\u00020\u00132\u0006\u0010\u0003\u001a\u00020\u00132\n\u0010\b\u001a\u00020\u0014\"\u00020\u0013H\u0007\u001a\u0019\u0010\u0000\u001a\u00020\u00152\u0006\u0010\u0003\u001a\u00020\u00152\u0006\u0010\u0004\u001a\u00020\u0015H\u0087\b\u001a!\u0010\u0000\u001a\u00020\u00152\u0006\u0010\u0003\u001a\u00020\u00152\u0006\u0010\u0004\u001a\u00020\u00152\u0006\u0010\u0006\u001a\u00020\u0015H\u0087\b\u001a\u001c\u0010\u0000\u001a\u00020\u00152\u0006\u0010\u0003\u001a\u00020\u00152\n\u0010\b\u001a\u00020\u0016\"\u00020\u0015H\u0007\u001a-\u0010\u0017\u001a\u0002H\u0001\"\u000e\b\u0000\u0010\u0001*\b\u0012\u0004\u0012\u0002H\u00010\u00022\u0006\u0010\u0003\u001a\u0002H\u00012\u0006\u0010\u0004\u001a\u0002H\u0001H\u0007¢\u0006\u0002\u0010\u0005\u001a5\u0010\u0017\u001a\u0002H\u0001\"\u000e\b\u0000\u0010\u0001*\b\u0012\u0004\u0012\u0002H\u00010\u00022\u0006\u0010\u0003\u001a\u0002H\u00012\u0006\u0010\u0004\u001a\u0002H\u00012\u0006\u0010\u0006\u001a\u0002H\u0001H\u0007¢\u0006\u0002\u0010\u0007\u001a9\u0010\u0017\u001a\u0002H\u0001\"\u000e\b\u0000\u0010\u0001*\b\u0012\u0004\u0012\u0002H\u00010\u00022\u0006\u0010\u0003\u001a\u0002H\u00012\u0012\u0010\b\u001a\n\u0012\u0006\b\u0001\u0012\u0002H\u00010\t\"\u0002H\u0001H\u0007¢\u0006\u0002\u0010\n\u001a\u0019\u0010\u0017\u001a\u00020\u000b2\u0006\u0010\u0003\u001a\u00020\u000b2\u0006\u0010\u0004\u001a\u00020\u000bH\u0087\b\u001a!\u0010\u0017\u001a\u00020\u000b2\u0006\u0010\u0003\u001a\u00020\u000b2\u0006\u0010\u0004\u001a\u00020\u000b2\u0006\u0010\u0006\u001a\u00020\u000bH\u0087\b\u001a\u001c\u0010\u0017\u001a\u00020\u000b2\u0006\u0010\u0003\u001a\u00020\u000b2\n\u0010\b\u001a\u00020\f\"\u00020\u000bH\u0007\u001a\u0019\u0010\u0017\u001a\u00020\r2\u0006\u0010\u0003\u001a\u00020\r2\u0006\u0010\u0004\u001a\u00020\rH\u0087\b\u001a!\u0010\u0017\u001a\u00020\r2\u0006\u0010\u0003\u001a\u00020\r2\u0006\u0010\u0004\u001a\u00020\r2\u0006\u0010\u0006\u001a\u00020\rH\u0087\b\u001a\u001c\u0010\u0017\u001a\u00020\r2\u0006\u0010\u0003\u001a\u00020\r2\n\u0010\b\u001a\u00020\u000e\"\u00020\rH\u0007\u001a\u0019\u0010\u0017\u001a\u00020\u000f2\u0006\u0010\u0003\u001a\u00020\u000f2\u0006\u0010\u0004\u001a\u00020\u000fH\u0087\b\u001a!\u0010\u0017\u001a\u00020\u000f2\u0006\u0010\u0003\u001a\u00020\u000f2\u0006\u0010\u0004\u001a\u00020\u000f2\u0006\u0010\u0006\u001a\u00020\u000fH\u0087\b\u001a\u001c\u0010\u0017\u001a\u00020\u000f2\u0006\u0010\u0003\u001a\u00020\u000f2\n\u0010\b\u001a\u00020\u0010\"\u00020\u000fH\u0007\u001a\u0019\u0010\u0017\u001a\u00020\u00112\u0006\u0010\u0003\u001a\u00020\u00112\u0006\u0010\u0004\u001a\u00020\u0011H\u0087\b\u001a!\u0010\u0017\u001a\u00020\u00112\u0006\u0010\u0003\u001a\u00020\u00112\u0006\u0010\u0004\u001a\u00020\u00112\u0006\u0010\u0006\u001a\u00020\u0011H\u0087\b\u001a\u001c\u0010\u0017\u001a\u00020\u00112\u0006\u0010\u0003\u001a\u00020\u00112\n\u0010\b\u001a\u00020\u0012\"\u00020\u0011H\u0007\u001a\u0019\u0010\u0017\u001a\u00020\u00132\u0006\u0010\u0003\u001a\u00020\u00132\u0006\u0010\u0004\u001a\u00020\u0013H\u0087\b\u001a!\u0010\u0017\u001a\u00020\u00132\u0006\u0010\u0003\u001a\u00020\u00132\u0006\u0010\u0004\u001a\u00020\u00132\u0006\u0010\u0006\u001a\u00020\u0013H\u0087\b\u001a\u001c\u0010\u0017\u001a\u00020\u00132\u0006\u0010\u0003\u001a\u00020\u00132\n\u0010\b\u001a\u00020\u0014\"\u00020\u0013H\u0007\u001a\u0019\u0010\u0017\u001a\u00020\u00152\u0006\u0010\u0003\u001a\u00020\u00152\u0006\u0010\u0004\u001a\u00020\u0015H\u0087\b\u001a!\u0010\u0017\u001a\u00020\u00152\u0006\u0010\u0003\u001a\u00020\u00152\u0006\u0010\u0004\u001a\u00020\u00152\u0006\u0010\u0006\u001a\u00020\u0015H\u0087\b\u001a\u001c\u0010\u0017\u001a\u00020\u00152\u0006\u0010\u0003\u001a\u00020\u00152\n\u0010\b\u001a\u00020\u0016\"\u00020\u0015H\u0007¨\u0006\u0018"}, d2 = {"maxOf", "T", "", com.android.server.wm.ActivityTaskManagerService.DUMP_ACTIVITIES_SHORT_CMD, "b", "(Ljava/lang/Comparable;Ljava/lang/Comparable;)Ljava/lang/Comparable;", "c", "(Ljava/lang/Comparable;Ljava/lang/Comparable;Ljava/lang/Comparable;)Ljava/lang/Comparable;", "other", "", "(Ljava/lang/Comparable;[Ljava/lang/Comparable;)Ljava/lang/Comparable;", "", "", "", "", "", "", "", "", "", "", "", "", "minOf", "kotlin-stdlib"}, k = 5, mv = {1, 9, 0}, xi = 49, xs = "com/android/server/permission/jarjar/kotlin/comparisons/ComparisonsKt")
public class ComparisonsKt___ComparisonsJvmKt extends com.android.server.permission.jarjar.kotlin.comparisons.ComparisonsKt__ComparisonsKt {
    public static final <T extends java.lang.Comparable<? super T>> T maxOf(T t, T t2) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(t, com.android.server.wm.ActivityTaskManagerService.DUMP_ACTIVITIES_SHORT_CMD);
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(t2, "b");
        return t.compareTo(t2) >= 0 ? t : t2;
    }

    private static final byte maxOf(byte a, byte b) {
        return (byte) java.lang.Math.max((int) a, (int) b);
    }

    private static final short maxOf(short a, short b) {
        return (short) java.lang.Math.max((int) a, (int) b);
    }

    private static final int maxOf(int a, int b) {
        return java.lang.Math.max(a, b);
    }

    private static final long maxOf(long a, long b) {
        return java.lang.Math.max(a, b);
    }

    private static final float maxOf(float a, float b) {
        return java.lang.Math.max(a, b);
    }

    private static final double maxOf(double a, double b) {
        return java.lang.Math.max(a, b);
    }

    public static final <T extends java.lang.Comparable<? super T>> T maxOf(T t, T t2, T t3) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(t, com.android.server.wm.ActivityTaskManagerService.DUMP_ACTIVITIES_SHORT_CMD);
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(t2, "b");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(t3, "c");
        return (T) com.android.server.permission.jarjar.kotlin.comparisons.ComparisonsKt.maxOf(t, com.android.server.permission.jarjar.kotlin.comparisons.ComparisonsKt.maxOf(t2, t3));
    }

    private static final byte maxOf(byte a, byte b, byte c) {
        return (byte) java.lang.Math.max((int) a, java.lang.Math.max((int) b, (int) c));
    }

    private static final short maxOf(short a, short b, short c) {
        return (short) java.lang.Math.max((int) a, java.lang.Math.max((int) b, (int) c));
    }

    private static final int maxOf(int a, int b, int c) {
        return java.lang.Math.max(a, java.lang.Math.max(b, c));
    }

    private static final long maxOf(long a, long b, long c) {
        return java.lang.Math.max(a, java.lang.Math.max(b, c));
    }

    private static final float maxOf(float a, float b, float c) {
        return java.lang.Math.max(a, java.lang.Math.max(b, c));
    }

    private static final double maxOf(double a, double b, double c) {
        return java.lang.Math.max(a, java.lang.Math.max(b, c));
    }

    public static final <T extends java.lang.Comparable<? super T>> T maxOf(T t, T... tArr) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(t, com.android.server.wm.ActivityTaskManagerService.DUMP_ACTIVITIES_SHORT_CMD);
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(tArr, "other");
        T t2 = t;
        for (T t3 : tArr) {
            t2 = (T) com.android.server.permission.jarjar.kotlin.comparisons.ComparisonsKt.maxOf(t2, t3);
        }
        return t2;
    }

    public static final byte maxOf(byte a, byte... other) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(other, "other");
        byte max = a;
        for (byte e : other) {
            max = (byte) java.lang.Math.max((int) max, (int) e);
        }
        return max;
    }

    public static final short maxOf(short a, short... other) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(other, "other");
        short max = a;
        for (short e : other) {
            max = (short) java.lang.Math.max((int) max, (int) e);
        }
        return max;
    }

    public static final int maxOf(int a, int... other) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(other, "other");
        int max = a;
        for (int e : other) {
            max = java.lang.Math.max(max, e);
        }
        return max;
    }

    public static final long maxOf(long a, long... other) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(other, "other");
        long max = a;
        for (long e : other) {
            max = java.lang.Math.max(max, e);
        }
        return max;
    }

    public static final float maxOf(float a, float... other) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(other, "other");
        float max = a;
        for (float e : other) {
            max = java.lang.Math.max(max, e);
        }
        return max;
    }

    public static final double maxOf(double a, double... other) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(other, "other");
        double max = a;
        for (double e : other) {
            max = java.lang.Math.max(max, e);
        }
        return max;
    }

    public static final <T extends java.lang.Comparable<? super T>> T minOf(T t, T t2) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(t, com.android.server.wm.ActivityTaskManagerService.DUMP_ACTIVITIES_SHORT_CMD);
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(t2, "b");
        return t.compareTo(t2) <= 0 ? t : t2;
    }

    private static final byte minOf(byte a, byte b) {
        return (byte) java.lang.Math.min((int) a, (int) b);
    }

    private static final short minOf(short a, short b) {
        return (short) java.lang.Math.min((int) a, (int) b);
    }

    private static final int minOf(int a, int b) {
        return java.lang.Math.min(a, b);
    }

    private static final long minOf(long a, long b) {
        return java.lang.Math.min(a, b);
    }

    private static final float minOf(float a, float b) {
        return java.lang.Math.min(a, b);
    }

    private static final double minOf(double a, double b) {
        return java.lang.Math.min(a, b);
    }

    public static final <T extends java.lang.Comparable<? super T>> T minOf(T t, T t2, T t3) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(t, com.android.server.wm.ActivityTaskManagerService.DUMP_ACTIVITIES_SHORT_CMD);
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(t2, "b");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(t3, "c");
        return (T) com.android.server.permission.jarjar.kotlin.comparisons.ComparisonsKt.minOf(t, com.android.server.permission.jarjar.kotlin.comparisons.ComparisonsKt.minOf(t2, t3));
    }

    private static final byte minOf(byte a, byte b, byte c) {
        return (byte) java.lang.Math.min((int) a, java.lang.Math.min((int) b, (int) c));
    }

    private static final short minOf(short a, short b, short c) {
        return (short) java.lang.Math.min((int) a, java.lang.Math.min((int) b, (int) c));
    }

    private static final int minOf(int a, int b, int c) {
        return java.lang.Math.min(a, java.lang.Math.min(b, c));
    }

    private static final long minOf(long a, long b, long c) {
        return java.lang.Math.min(a, java.lang.Math.min(b, c));
    }

    private static final float minOf(float a, float b, float c) {
        return java.lang.Math.min(a, java.lang.Math.min(b, c));
    }

    private static final double minOf(double a, double b, double c) {
        return java.lang.Math.min(a, java.lang.Math.min(b, c));
    }

    public static final <T extends java.lang.Comparable<? super T>> T minOf(T t, T... tArr) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(t, com.android.server.wm.ActivityTaskManagerService.DUMP_ACTIVITIES_SHORT_CMD);
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(tArr, "other");
        T t2 = t;
        for (T t3 : tArr) {
            t2 = (T) com.android.server.permission.jarjar.kotlin.comparisons.ComparisonsKt.minOf(t2, t3);
        }
        return t2;
    }

    public static final byte minOf(byte a, byte... other) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(other, "other");
        byte min = a;
        for (byte e : other) {
            min = (byte) java.lang.Math.min((int) min, (int) e);
        }
        return min;
    }

    public static final short minOf(short a, short... other) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(other, "other");
        short min = a;
        for (short e : other) {
            min = (short) java.lang.Math.min((int) min, (int) e);
        }
        return min;
    }

    public static final int minOf(int a, int... other) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(other, "other");
        int min = a;
        for (int e : other) {
            min = java.lang.Math.min(min, e);
        }
        return min;
    }

    public static final long minOf(long a, long... other) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(other, "other");
        long min = a;
        for (long e : other) {
            min = java.lang.Math.min(min, e);
        }
        return min;
    }

    public static final float minOf(float a, float... other) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(other, "other");
        float min = a;
        for (float e : other) {
            min = java.lang.Math.min(min, e);
        }
        return min;
    }

    public static final double minOf(double a, double... other) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(other, "other");
        double min = a;
        for (double e : other) {
            min = java.lang.Math.min(min, e);
        }
        return min;
    }
}
