package com.android.server.permission.jarjar.kotlin.jvm.internal;

/* JADX INFO: compiled from: ArrayIterator.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000\u0012\n\u0000\n\u0002\u0010(\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0002\b\u0002\u001a%\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u00022\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0004¢\u0006\u0002\u0010\u0005¨\u0006\u0006"}, d2 = {"iterator", "", "T", "array", "", "([Ljava/lang/Object;)Ljava/util/Iterator;", "kotlin-stdlib"}, k = 2, mv = {1, 9, 0}, xi = 48)
public final class ArrayIteratorKt {
    public static final <T> java.util.Iterator<T> iterator(T[] tArr) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(tArr, "array");
        return new com.android.server.permission.jarjar.kotlin.jvm.internal.ArrayIterator(tArr);
    }
}
