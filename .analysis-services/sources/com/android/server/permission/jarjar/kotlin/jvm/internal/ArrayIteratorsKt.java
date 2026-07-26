package com.android.server.permission.jarjar.kotlin.jvm.internal;

/* JADX INFO: compiled from: ArrayIterators.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000F\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0019\n\u0002\u0018\u0002\n\u0002\u0010\u0013\n\u0002\u0018\u0002\n\u0002\u0010\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0015\n\u0002\u0018\u0002\n\u0002\u0010\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0017\n\u0000\u001a\u000e\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003\u001a\u000e\u0010\u0000\u001a\u00020\u00042\u0006\u0010\u0002\u001a\u00020\u0005\u001a\u000e\u0010\u0000\u001a\u00020\u00062\u0006\u0010\u0002\u001a\u00020\u0007\u001a\u000e\u0010\u0000\u001a\u00020\b2\u0006\u0010\u0002\u001a\u00020\t\u001a\u000e\u0010\u0000\u001a\u00020\n2\u0006\u0010\u0002\u001a\u00020\u000b\u001a\u000e\u0010\u0000\u001a\u00020\f2\u0006\u0010\u0002\u001a\u00020\r\u001a\u000e\u0010\u0000\u001a\u00020\u000e2\u0006\u0010\u0002\u001a\u00020\u000f\u001a\u000e\u0010\u0000\u001a\u00020\u00102\u0006\u0010\u0002\u001a\u00020\u0011¨\u0006\u0012"}, d2 = {"iterator", "Lkotlin/collections/BooleanIterator;", "array", "", "Lkotlin/collections/ByteIterator;", "", "Lkotlin/collections/CharIterator;", "", "Lkotlin/collections/DoubleIterator;", "", "Lkotlin/collections/FloatIterator;", "", "Lkotlin/collections/IntIterator;", "", "Lkotlin/collections/LongIterator;", "", "Lkotlin/collections/ShortIterator;", "", "kotlin-stdlib"}, k = 2, mv = {1, 9, 0}, xi = 48)
public final class ArrayIteratorsKt {
    public static final com.android.server.permission.jarjar.kotlin.collections.ByteIterator iterator(byte[] array) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(array, "array");
        return new com.android.server.permission.jarjar.kotlin.jvm.internal.ArrayByteIterator(array);
    }

    public static final com.android.server.permission.jarjar.kotlin.collections.CharIterator iterator(char[] array) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(array, "array");
        return new com.android.server.permission.jarjar.kotlin.jvm.internal.ArrayCharIterator(array);
    }

    public static final com.android.server.permission.jarjar.kotlin.collections.ShortIterator iterator(short[] array) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(array, "array");
        return new com.android.server.permission.jarjar.kotlin.jvm.internal.ArrayShortIterator(array);
    }

    public static final com.android.server.permission.jarjar.kotlin.collections.IntIterator iterator(int[] array) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(array, "array");
        return new com.android.server.permission.jarjar.kotlin.jvm.internal.ArrayIntIterator(array);
    }

    public static final com.android.server.permission.jarjar.kotlin.collections.LongIterator iterator(long[] array) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(array, "array");
        return new com.android.server.permission.jarjar.kotlin.jvm.internal.ArrayLongIterator(array);
    }

    public static final com.android.server.permission.jarjar.kotlin.collections.FloatIterator iterator(float[] array) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(array, "array");
        return new com.android.server.permission.jarjar.kotlin.jvm.internal.ArrayFloatIterator(array);
    }

    public static final com.android.server.permission.jarjar.kotlin.collections.DoubleIterator iterator(double[] array) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(array, "array");
        return new com.android.server.permission.jarjar.kotlin.jvm.internal.ArrayDoubleIterator(array);
    }

    public static final com.android.server.permission.jarjar.kotlin.collections.BooleanIterator iterator(boolean[] array) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(array, "array");
        return new com.android.server.permission.jarjar.kotlin.jvm.internal.ArrayBooleanIterator(array);
    }
}
