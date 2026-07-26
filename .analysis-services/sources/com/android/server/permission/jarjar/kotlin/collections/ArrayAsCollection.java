package com.android.server.permission.jarjar.kotlin.collections;

/* JADX INFO: compiled from: Collections.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u001e\n\u0000\n\u0002\u0010\u0011\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\f\n\u0002\u0010(\n\u0000\n\u0002\u0010\u0000\n\u0000\b\u0002\u0018\u0000*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u0002B\u001d\u0012\u000e\u0010\u0003\u001a\n\u0012\u0006\b\u0001\u0012\u00028\u00000\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0006¢\u0006\u0002\u0010\u0007J\u0016\u0010\u0010\u001a\u00020\u00062\u0006\u0010\u0011\u001a\u00028\u0000H\u0096\u0002¢\u0006\u0002\u0010\u0012J\u0016\u0010\u0013\u001a\u00020\u00062\f\u0010\u0014\u001a\b\u0012\u0004\u0012\u00028\u00000\u0002H\u0016J\b\u0010\u0015\u001a\u00020\u0006H\u0016J\u000f\u0010\u0016\u001a\b\u0012\u0004\u0012\u00028\u00000\u0017H\u0096\u0002J\u0015\u0010\u0018\u001a\f\u0012\b\b\u0001\u0012\u0004\u0018\u00010\u00190\u0004¢\u0006\u0002\u0010\u000eR\u0011\u0010\u0005\u001a\u00020\u0006¢\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\bR\u0014\u0010\t\u001a\u00020\n8VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\u000b\u0010\fR\u001b\u0010\u0003\u001a\n\u0012\u0006\b\u0001\u0012\u00028\u00000\u0004¢\u0006\n\n\u0002\u0010\u000f\u001a\u0004\b\r\u0010\u000e¨\u0006\u001a"}, d2 = {"Lkotlin/collections/ArrayAsCollection;", "T", "", "values", "", "isVarargs", "", "([Ljava/lang/Object;Z)V", "()Z", "size", "", "getSize", "()I", "getValues", "()[Ljava/lang/Object;", "[Ljava/lang/Object;", "contains", "element", "(Ljava/lang/Object;)Z", "containsAll", "elements", "isEmpty", "iterator", "", "toArray", "", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
final class ArrayAsCollection<T> implements java.util.Collection<T>, com.android.server.permission.jarjar.kotlin.jvm.internal.markers.KMappedMarker {
    private final boolean isVarargs;
    private final T[] values;

    @Override // java.util.Collection
    public boolean add(T t) {
        throw new java.lang.UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override // java.util.Collection
    public boolean addAll(java.util.Collection<? extends T> collection) {
        throw new java.lang.UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override // java.util.Collection
    public void clear() {
        throw new java.lang.UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override // java.util.Collection
    public boolean remove(java.lang.Object obj) {
        throw new java.lang.UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override // java.util.Collection
    public boolean removeAll(java.util.Collection<? extends java.lang.Object> collection) {
        throw new java.lang.UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override // java.util.Collection
    public boolean retainAll(java.util.Collection<? extends java.lang.Object> collection) {
        throw new java.lang.UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override // java.util.Collection
    public <T> T[] toArray(T[] tArr) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(tArr, "array");
        return (T[]) com.android.server.permission.jarjar.kotlin.jvm.internal.CollectionToArray.toArray(this, tArr);
    }

    public ArrayAsCollection(T[] tArr, boolean isVarargs) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(tArr, "values");
        this.values = tArr;
        this.isVarargs = isVarargs;
    }

    public final T[] getValues() {
        return this.values;
    }

    public final boolean isVarargs() {
        return this.isVarargs;
    }

    @Override // java.util.Collection
    public final /* bridge */ int size() {
        return getSize();
    }

    public int getSize() {
        return this.values.length;
    }

    @Override // java.util.Collection
    public boolean isEmpty() {
        return this.values.length == 0;
    }

    @Override // java.util.Collection
    public boolean contains(java.lang.Object element) {
        return com.android.server.permission.jarjar.kotlin.collections.ArraysKt.contains(this.values, element);
    }

    @Override // java.util.Collection
    public boolean containsAll(java.util.Collection<? extends java.lang.Object> collection) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(collection, "elements");
        java.util.Collection<? extends java.lang.Object> $this$all$iv = collection;
        if ($this$all$iv.isEmpty()) {
            return true;
        }
        for (java.lang.Object element$iv : $this$all$iv) {
            if (!contains(element$iv)) {
                return false;
            }
        }
        return true;
    }

    @Override // java.util.Collection, java.lang.Iterable
    public java.util.Iterator<T> iterator() {
        return com.android.server.permission.jarjar.kotlin.jvm.internal.ArrayIteratorKt.iterator(this.values);
    }

    @Override // java.util.Collection
    public final java.lang.Object[] toArray() {
        return com.android.server.permission.jarjar.kotlin.collections.CollectionsKt.copyToArrayOfAny(this.values, this.isVarargs);
    }
}
