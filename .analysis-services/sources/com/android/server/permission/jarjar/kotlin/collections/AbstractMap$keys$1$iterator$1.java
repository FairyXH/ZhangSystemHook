package com.android.server.permission.jarjar.kotlin.collections;

/* JADX INFO: Add missing generic type declarations: [K] */
/* JADX INFO: compiled from: AbstractMap.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000\u0013\n\u0000\n\u0002\u0010(\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00028\u00000\u0001J\t\u0010\u0002\u001a\u00020\u0003H\u0096\u0002J\u000e\u0010\u0004\u001a\u00028\u0000H\u0096\u0002¢\u0006\u0002\u0010\u0005¨\u0006\u0006"}, d2 = {"com/android/server/permission/jarjar/kotlin/collections/AbstractMap$keys$1$iterator$1", "", "hasNext", "", "next", "()Ljava/lang/Object;", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class AbstractMap$keys$1$iterator$1<K> implements java.util.Iterator<K>, com.android.server.permission.jarjar.kotlin.jvm.internal.markers.KMappedMarker {
    final /* synthetic */ java.util.Iterator<java.util.Map.Entry<K, V>> $entryIterator;

    @Override // java.util.Iterator
    public void remove() {
        throw new java.lang.UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    /* JADX WARN: Multi-variable type inference failed */
    AbstractMap$keys$1$iterator$1(java.util.Iterator<? extends java.util.Map.Entry<? extends K, ? extends V>> it) {
        this.$entryIterator = it;
    }

    @Override // java.util.Iterator
    public boolean hasNext() {
        return this.$entryIterator.hasNext();
    }

    @Override // java.util.Iterator
    public K next() {
        return (K) ((java.util.Map.Entry) this.$entryIterator.next()).getKey();
    }
}
