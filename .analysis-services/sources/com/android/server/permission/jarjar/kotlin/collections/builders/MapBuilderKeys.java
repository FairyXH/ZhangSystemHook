package com.android.server.permission.jarjar.kotlin.collections.builders;

/* JADX INFO: compiled from: MapBuilder.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010#\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0010\u001e\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010)\n\u0002\b\u0004\b\u0000\u0018\u0000*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u00022\b\u0012\u0004\u0012\u0002H\u00010\u0003B\u0019\b\u0000\u0012\u0010\u0010\u0004\u001a\f\u0012\u0004\u0012\u00028\u0000\u0012\u0002\b\u00030\u0005¢\u0006\u0002\u0010\u0006J\u0015\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00028\u0000H\u0016¢\u0006\u0002\u0010\u000eJ\u0016\u0010\u000f\u001a\u00020\f2\f\u0010\u0010\u001a\b\u0012\u0004\u0012\u00028\u00000\u0011H\u0016J\b\u0010\u0012\u001a\u00020\u0013H\u0016J\u0016\u0010\u0014\u001a\u00020\f2\u0006\u0010\r\u001a\u00028\u0000H\u0096\u0002¢\u0006\u0002\u0010\u000eJ\b\u0010\u0015\u001a\u00020\fH\u0016J\u000f\u0010\u0016\u001a\b\u0012\u0004\u0012\u00028\u00000\u0017H\u0096\u0002J\u0015\u0010\u0018\u001a\u00020\f2\u0006\u0010\r\u001a\u00028\u0000H\u0016¢\u0006\u0002\u0010\u000eJ\u0016\u0010\u0019\u001a\u00020\f2\f\u0010\u0010\u001a\b\u0012\u0004\u0012\u00028\u00000\u0011H\u0016J\u0016\u0010\u001a\u001a\u00020\f2\f\u0010\u0010\u001a\b\u0012\u0004\u0012\u00028\u00000\u0011H\u0016R\u0018\u0010\u0004\u001a\f\u0012\u0004\u0012\u00028\u0000\u0012\u0002\b\u00030\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0007\u001a\u00020\b8VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\t\u0010\n¨\u0006\u001b"}, d2 = {"Lkotlin/collections/builders/MapBuilderKeys;", "E", "", "Lkotlin/collections/AbstractMutableSet;", "backing", "Lkotlin/collections/builders/MapBuilder;", "(Lkotlin/collections/builders/MapBuilder;)V", "size", "", "getSize", "()I", "add", "", "element", "(Ljava/lang/Object;)Z", "addAll", "elements", "", "clear", "", "contains", "isEmpty", "iterator", "", "remove", "removeAll", "retainAll", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class MapBuilderKeys<E> extends com.android.server.permission.jarjar.kotlin.collections.AbstractMutableSet<E> implements java.util.Set<E>, com.android.server.permission.jarjar.kotlin.jvm.internal.markers.KMutableSet {
    private final com.android.server.permission.jarjar.kotlin.collections.builders.MapBuilder<E, ?> backing;

    public MapBuilderKeys(com.android.server.permission.jarjar.kotlin.collections.builders.MapBuilder<E, ?> mapBuilder) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(mapBuilder, "backing");
        this.backing = mapBuilder;
    }

    @Override // com.android.server.permission.jarjar.kotlin.collections.AbstractMutableSet
    public int getSize() {
        return this.backing.size();
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean isEmpty() {
        return this.backing.isEmpty();
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean contains(java.lang.Object element) {
        return this.backing.containsKey(element);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public void clear() {
        this.backing.clear();
    }

    @Override // com.android.server.permission.jarjar.kotlin.collections.AbstractMutableSet, java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean add(E e) {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean addAll(java.util.Collection<? extends E> collection) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(collection, "elements");
        throw new java.lang.UnsupportedOperationException();
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean remove(java.lang.Object element) {
        return this.backing.removeKey$kotlin_stdlib(element) >= 0;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
    public java.util.Iterator<E> iterator() {
        return this.backing.keysIterator$kotlin_stdlib();
    }

    @Override // java.util.AbstractSet, java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean removeAll(java.util.Collection<? extends java.lang.Object> collection) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(collection, "elements");
        this.backing.checkIsMutable$kotlin_stdlib();
        return super.removeAll(collection);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean retainAll(java.util.Collection<? extends java.lang.Object> collection) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(collection, "elements");
        this.backing.checkIsMutable$kotlin_stdlib();
        return super.retainAll(collection);
    }
}
