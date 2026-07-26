package com.android.server.permission.jarjar.kotlin.collections;

/* JADX INFO: compiled from: ReversedViews.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0010(\n\u0000\n\u0002\u0010*\n\u0000\b\u0012\u0018\u0000*\u0006\b\u0000\u0010\u0001 \u00012\b\u0012\u0004\u0012\u0002H\u00010\u0002B\u0013\u0012\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u00028\u00000\u0004¢\u0006\u0002\u0010\u0005J\u0016\u0010\n\u001a\u00028\u00002\u0006\u0010\u000b\u001a\u00020\u0007H\u0096\u0002¢\u0006\u0002\u0010\fJ\u000f\u0010\r\u001a\b\u0012\u0004\u0012\u00028\u00000\u000eH\u0096\u0002J\u000e\u0010\u000f\u001a\b\u0012\u0004\u0012\u00028\u00000\u0010H\u0016J\u0016\u0010\u000f\u001a\b\u0012\u0004\u0012\u00028\u00000\u00102\u0006\u0010\u000b\u001a\u00020\u0007H\u0016R\u0014\u0010\u0003\u001a\b\u0012\u0004\u0012\u00028\u00000\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0006\u001a\u00020\u00078VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\b\u0010\t¨\u0006\u0011"}, d2 = {"Lkotlin/collections/ReversedListReadOnly;", "T", "Lkotlin/collections/AbstractList;", "delegate", "", "(Ljava/util/List;)V", "size", "", "getSize", "()I", "get", com.android.server.am.AssistDataRequester.KEY_RECEIVER_EXTRA_INDEX, "(I)Ljava/lang/Object;", "iterator", "", "listIterator", "", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
class ReversedListReadOnly<T> extends com.android.server.permission.jarjar.kotlin.collections.AbstractList<T> {
    private final java.util.List<T> delegate;

    /* JADX WARN: Multi-variable type inference failed */
    public ReversedListReadOnly(java.util.List<? extends T> list) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(list, "delegate");
        this.delegate = list;
    }

    @Override // com.android.server.permission.jarjar.kotlin.collections.AbstractList, com.android.server.permission.jarjar.kotlin.collections.AbstractCollection
    public int getSize() {
        return this.delegate.size();
    }

    @Override // com.android.server.permission.jarjar.kotlin.collections.AbstractList, java.util.List
    public T get(int index) {
        return this.delegate.get(com.android.server.permission.jarjar.kotlin.collections.CollectionsKt__ReversedViewsKt.reverseElementIndex$CollectionsKt__ReversedViewsKt(this, index));
    }

    @Override // com.android.server.permission.jarjar.kotlin.collections.AbstractList, com.android.server.permission.jarjar.kotlin.collections.AbstractCollection, java.util.Collection, java.lang.Iterable
    public java.util.Iterator<T> iterator() {
        return listIterator(0);
    }

    @Override // com.android.server.permission.jarjar.kotlin.collections.AbstractList, java.util.List
    public java.util.ListIterator<T> listIterator() {
        return listIterator(0);
    }

    /* JADX INFO: renamed from: com.android.server.permission.jarjar.kotlin.collections.ReversedListReadOnly$listIterator$1, reason: invalid class name */
    /* JADX INFO: compiled from: ReversedViews.kt */
    @com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000\u001d\n\u0000\n\u0002\u0010*\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\u0003*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00028\u00000\u0001J\t\u0010\u0005\u001a\u00020\u0006H\u0096\u0002J\b\u0010\u0007\u001a\u00020\u0006H\u0016J\u000e\u0010\b\u001a\u00028\u0000H\u0096\u0002¢\u0006\u0002\u0010\tJ\b\u0010\n\u001a\u00020\u000bH\u0016J\r\u0010\f\u001a\u00028\u0000H\u0016¢\u0006\u0002\u0010\tJ\b\u0010\r\u001a\u00020\u000bH\u0016R\u0017\u0010\u0002\u001a\b\u0012\u0004\u0012\u00028\u00000\u0001¢\u0006\b\n\u0000\u001a\u0004\b\u0003\u0010\u0004¨\u0006\u000e"}, d2 = {"com/android/server/permission/jarjar/kotlin/collections/ReversedListReadOnly$listIterator$1", "", "delegateIterator", "getDelegateIterator", "()Ljava/util/ListIterator;", "hasNext", "", "hasPrevious", "next", "()Ljava/lang/Object;", "nextIndex", "", "previous", "previousIndex", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public static final class AnonymousClass1 implements java.util.ListIterator<T>, com.android.server.permission.jarjar.kotlin.jvm.internal.markers.KMappedMarker {
        private final java.util.ListIterator<T> delegateIterator;
        final /* synthetic */ com.android.server.permission.jarjar.kotlin.collections.ReversedListReadOnly<T> this$0;

        @Override // java.util.ListIterator
        public void add(T t) {
            throw new java.lang.UnsupportedOperationException("Operation is not supported for read-only collection");
        }

        @Override // java.util.ListIterator, java.util.Iterator
        public void remove() {
            throw new java.lang.UnsupportedOperationException("Operation is not supported for read-only collection");
        }

        @Override // java.util.ListIterator
        public void set(T t) {
            throw new java.lang.UnsupportedOperationException("Operation is not supported for read-only collection");
        }

        /* JADX WARN: Multi-variable type inference failed */
        AnonymousClass1(com.android.server.permission.jarjar.kotlin.collections.ReversedListReadOnly<? extends T> reversedListReadOnly, int $index) {
            this.this$0 = reversedListReadOnly;
            this.delegateIterator = ((com.android.server.permission.jarjar.kotlin.collections.ReversedListReadOnly) reversedListReadOnly).delegate.listIterator(com.android.server.permission.jarjar.kotlin.collections.CollectionsKt__ReversedViewsKt.reversePositionIndex$CollectionsKt__ReversedViewsKt(reversedListReadOnly, $index));
        }

        public final java.util.ListIterator<T> getDelegateIterator() {
            return this.delegateIterator;
        }

        @Override // java.util.ListIterator, java.util.Iterator
        public boolean hasNext() {
            return this.delegateIterator.hasPrevious();
        }

        @Override // java.util.ListIterator
        public boolean hasPrevious() {
            return this.delegateIterator.hasNext();
        }

        @Override // java.util.ListIterator, java.util.Iterator
        public T next() {
            return this.delegateIterator.previous();
        }

        @Override // java.util.ListIterator
        public int nextIndex() {
            return com.android.server.permission.jarjar.kotlin.collections.CollectionsKt__ReversedViewsKt.reverseIteratorIndex$CollectionsKt__ReversedViewsKt(this.this$0, this.delegateIterator.previousIndex());
        }

        @Override // java.util.ListIterator
        public T previous() {
            return this.delegateIterator.next();
        }

        @Override // java.util.ListIterator
        public int previousIndex() {
            return com.android.server.permission.jarjar.kotlin.collections.CollectionsKt__ReversedViewsKt.reverseIteratorIndex$CollectionsKt__ReversedViewsKt(this.this$0, this.delegateIterator.nextIndex());
        }
    }

    @Override // com.android.server.permission.jarjar.kotlin.collections.AbstractList, java.util.List
    public java.util.ListIterator<T> listIterator(int index) {
        return new com.android.server.permission.jarjar.kotlin.collections.ReversedListReadOnly.AnonymousClass1(this, index);
    }
}
