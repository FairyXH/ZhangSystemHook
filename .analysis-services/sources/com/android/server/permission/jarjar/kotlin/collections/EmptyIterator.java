package com.android.server.permission.jarjar.kotlin.collections;

/* JADX INFO: compiled from: Collections.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010*\n\u0002\u0010\u0001\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0003\bÀ\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0003J\t\u0010\u0004\u001a\u00020\u0005H\u0096\u0002J\b\u0010\u0006\u001a\u00020\u0005H\u0016J\t\u0010\u0007\u001a\u00020\u0002H\u0096\u0002J\b\u0010\b\u001a\u00020\tH\u0016J\b\u0010\n\u001a\u00020\u0002H\u0016J\b\u0010\u000b\u001a\u00020\tH\u0016¨\u0006\f"}, d2 = {"Lkotlin/collections/EmptyIterator;", "", "", "()V", "hasNext", "", "hasPrevious", "next", "nextIndex", "", "previous", "previousIndex", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class EmptyIterator implements java.util.ListIterator, com.android.server.permission.jarjar.kotlin.jvm.internal.markers.KMappedMarker {
    public static final com.android.server.permission.jarjar.kotlin.collections.EmptyIterator INSTANCE = new com.android.server.permission.jarjar.kotlin.collections.EmptyIterator();

    @Override // java.util.ListIterator
    public /* bridge */ /* synthetic */ void add(java.lang.Object obj) {
        throw new java.lang.UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    public void add(java.lang.Void r3) {
        throw new java.lang.UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override // java.util.ListIterator, java.util.Iterator
    public void remove() {
        throw new java.lang.UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override // java.util.ListIterator
    public /* bridge */ /* synthetic */ void set(java.lang.Object obj) {
        throw new java.lang.UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    public void set(java.lang.Void r3) {
        throw new java.lang.UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    private EmptyIterator() {
    }

    @Override // java.util.ListIterator, java.util.Iterator
    public boolean hasNext() {
        return false;
    }

    @Override // java.util.ListIterator
    public boolean hasPrevious() {
        return false;
    }

    @Override // java.util.ListIterator
    public int nextIndex() {
        return 0;
    }

    @Override // java.util.ListIterator
    public int previousIndex() {
        return -1;
    }

    @Override // java.util.ListIterator, java.util.Iterator
    public java.lang.Void next() {
        throw new java.util.NoSuchElementException();
    }

    @Override // java.util.ListIterator
    public java.lang.Void previous() {
        throw new java.util.NoSuchElementException();
    }
}
