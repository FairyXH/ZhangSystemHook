package com.android.server.permission.jarjar.kotlin.collections;

/* JADX INFO: compiled from: PrimitiveIterators.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000\u0010\n\u0002\u0018\u0002\n\u0002\u0010(\n\u0002\u0010\u0006\n\u0002\b\u0005\b&\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0003J\u000e\u0010\u0004\u001a\u00020\u0002H\u0086\u0002¢\u0006\u0002\u0010\u0005J\b\u0010\u0006\u001a\u00020\u0002H&¨\u0006\u0007"}, d2 = {"Lkotlin/collections/DoubleIterator;", "", "", "()V", "next", "()Ljava/lang/Double;", "nextDouble", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
public abstract class DoubleIterator implements java.util.Iterator<java.lang.Double>, com.android.server.permission.jarjar.kotlin.jvm.internal.markers.KMappedMarker {
    public abstract double nextDouble();

    @Override // java.util.Iterator
    public void remove() {
        throw new java.lang.UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override // java.util.Iterator
    public /* bridge */ /* synthetic */ java.lang.Double next() {
        return java.lang.Double.valueOf(nextDouble());
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.util.Iterator
    public final java.lang.Double next() {
        return java.lang.Double.valueOf(nextDouble());
    }
}
