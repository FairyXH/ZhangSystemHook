package kotlin.collections;

/* JADX INFO: compiled from: AbstractList.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u00008\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0002\b\b\n\u0002\u0010(\n\u0002\b\u0002\n\u0002\u0010*\n\u0002\b\b\b'\u0018\u0000 \u001c*\u0006\b\u0000\u0010\u0001 \u00012\b\u0012\u0004\u0012\u0002H\u00010\u00022\b\u0012\u0004\u0012\u0002H\u00010\u0003:\u0004\u001c\u001d\u001e\u001fB\u0007\b\u0004¢\u0006\u0002\u0010\u0004J\u0013\u0010\t\u001a\u00020\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\fH\u0096\u0002J\u0016\u0010\r\u001a\u00028\u00002\u0006\u0010\u000e\u001a\u00020\u0006H¦\u0002¢\u0006\u0002\u0010\u000fJ\b\u0010\u0010\u001a\u00020\u0006H\u0016J\u0015\u0010\u0011\u001a\u00020\u00062\u0006\u0010\u0012\u001a\u00028\u0000H\u0016¢\u0006\u0002\u0010\u0013J\u000f\u0010\u0014\u001a\b\u0012\u0004\u0012\u00028\u00000\u0015H\u0096\u0002J\u0015\u0010\u0016\u001a\u00020\u00062\u0006\u0010\u0012\u001a\u00028\u0000H\u0016¢\u0006\u0002\u0010\u0013J\u000e\u0010\u0017\u001a\b\u0012\u0004\u0012\u00028\u00000\u0018H\u0016J\u0016\u0010\u0017\u001a\b\u0012\u0004\u0012\u00028\u00000\u00182\u0006\u0010\u000e\u001a\u00020\u0006H\u0016J\u001e\u0010\u0019\u001a\b\u0012\u0004\u0012\u00028\u00000\u00032\u0006\u0010\u001a\u001a\u00020\u00062\u0006\u0010\u001b\u001a\u00020\u0006H\u0016R\u0012\u0010\u0005\u001a\u00020\u0006X¦\u0004¢\u0006\u0006\u001a\u0004\b\u0007\u0010\b¨\u0006 "}, d2 = {"Lkotlin/collections/AbstractList;", "E", "Lkotlin/collections/AbstractCollection;", "", "()V", "size", "", "getSize", "()I", "equals", "", "other", "", "get", com.android.server.am.AssistDataRequester.KEY_RECEIVER_EXTRA_INDEX, "(I)Ljava/lang/Object;", com.android.server.oplus.osense.OsenseConstants.KEY_INTEGER_HASH, "indexOf", "element", "(Ljava/lang/Object;)I", "iterator", "", "lastIndexOf", "listIterator", "", "subList", "fromIndex", "toIndex", "Companion", "IteratorImpl", "ListIteratorImpl", "SubList", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
public abstract class AbstractList<E> extends kotlin.collections.AbstractCollection<E> implements java.util.List<E>, kotlin.jvm.internal.markers.KMappedMarker {

    /* JADX INFO: renamed from: Companion, reason: from kotlin metadata */
    public static final kotlin.collections.AbstractList.Companion INSTANCE = new kotlin.collections.AbstractList.Companion(null);
    private static final int maxArraySize = 2147483639;

    @Override // java.util.List
    public void add(int i, E e) {
        throw new java.lang.UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override // java.util.List
    public boolean addAll(int i, java.util.Collection<? extends E> collection) {
        throw new java.lang.UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override // java.util.List
    public abstract E get(int index);

    @Override // kotlin.collections.AbstractCollection
    /* JADX INFO: renamed from: getSize */
    public abstract int get_size();

    @Override // java.util.List
    public E remove(int i) {
        throw new java.lang.UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override // java.util.List
    public E set(int i, E e) {
        throw new java.lang.UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    protected AbstractList() {
    }

    @Override // kotlin.collections.AbstractCollection, java.util.Collection, java.lang.Iterable
    public java.util.Iterator<E> iterator() {
        return new kotlin.collections.AbstractList.IteratorImpl();
    }

    @Override // java.util.List
    public int indexOf(E element) {
        kotlin.collections.AbstractList<E> $this$indexOfFirst$iv = this;
        int index$iv = 0;
        for (java.lang.Object item$iv : $this$indexOfFirst$iv) {
            if (kotlin.jvm.internal.Intrinsics.areEqual(item$iv, element)) {
                return index$iv;
            }
            index$iv++;
        }
        return -1;
    }

    @Override // java.util.List
    public int lastIndexOf(E element) {
        kotlin.collections.AbstractList<E> $this$indexOfLast$iv = this;
        java.util.ListIterator<E> listIterator = $this$indexOfLast$iv.listIterator($this$indexOfLast$iv.size());
        while (listIterator.hasPrevious()) {
            java.lang.Object it = listIterator.previous();
            if (kotlin.jvm.internal.Intrinsics.areEqual(it, element)) {
                return listIterator.nextIndex();
            }
        }
        return -1;
    }

    @Override // java.util.List
    public java.util.ListIterator<E> listIterator() {
        return new kotlin.collections.AbstractList.ListIteratorImpl(0);
    }

    @Override // java.util.List
    public java.util.ListIterator<E> listIterator(int index) {
        return new kotlin.collections.AbstractList.ListIteratorImpl(index);
    }

    @Override // java.util.List
    public java.util.List<E> subList(int fromIndex, int toIndex) {
        return new kotlin.collections.AbstractList.SubList(this, fromIndex, toIndex);
    }

    /* JADX INFO: compiled from: AbstractList.kt */
    @kotlin.Metadata(d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\n\b\u0002\u0018\u0000*\u0006\b\u0001\u0010\u0001 \u00012\b\u0012\u0004\u0012\u0002H\u00010\u00022\u00060\u0003j\u0002`\u0004B#\u0012\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00028\u00010\u0002\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\u0007¢\u0006\u0002\u0010\tJ\u0016\u0010\u000e\u001a\u00028\u00012\u0006\u0010\u000f\u001a\u00020\u0007H\u0096\u0002¢\u0006\u0002\u0010\u0010R\u000e\u0010\n\u001a\u00020\u0007X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00028\u00010\u0002X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u000b\u001a\u00020\u00078VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\f\u0010\r¨\u0006\u0011"}, d2 = {"Lkotlin/collections/AbstractList$SubList;", "E", "Lkotlin/collections/AbstractList;", "Ljava/util/RandomAccess;", "Lkotlin/collections/RandomAccess;", "list", "fromIndex", "", "toIndex", "(Lkotlin/collections/AbstractList;II)V", "_size", "size", "getSize", "()I", "get", com.android.server.am.AssistDataRequester.KEY_RECEIVER_EXTRA_INDEX, "(I)Ljava/lang/Object;", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
    private static final class SubList<E> extends kotlin.collections.AbstractList<E> implements java.util.RandomAccess {
        private int _size;
        private final int fromIndex;
        private final kotlin.collections.AbstractList<E> list;

        /* JADX WARN: Multi-variable type inference failed */
        public SubList(kotlin.collections.AbstractList<? extends E> list, int fromIndex, int toIndex) {
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(list, "list");
            this.list = list;
            this.fromIndex = fromIndex;
            kotlin.collections.AbstractList.INSTANCE.checkRangeIndexes$kotlin_stdlib(this.fromIndex, toIndex, this.list.size());
            this._size = toIndex - this.fromIndex;
        }

        @Override // kotlin.collections.AbstractList, java.util.List
        public E get(int index) {
            kotlin.collections.AbstractList.INSTANCE.checkElementIndex$kotlin_stdlib(index, this._size);
            return this.list.get(this.fromIndex + index);
        }

        @Override // kotlin.collections.AbstractList, kotlin.collections.AbstractCollection
        /* JADX INFO: renamed from: getSize, reason: from getter */
        public int get_size() {
            return this._size;
        }
    }

    @Override // java.util.Collection, java.util.List
    public boolean equals(java.lang.Object other) {
        if (other == this) {
            return true;
        }
        if (other instanceof java.util.List) {
            return INSTANCE.orderedEquals$kotlin_stdlib(this, (java.util.Collection) other);
        }
        return false;
    }

    @Override // java.util.Collection, java.util.List
    public int hashCode() {
        return INSTANCE.orderedHashCode$kotlin_stdlib(this);
    }

    /* JADX INFO: compiled from: AbstractList.kt */
    @kotlin.Metadata(d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010(\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0003\b\u0092\u0004\u0018\u00002\b\u0012\u0004\u0012\u00028\u00000\u0001B\u0005¢\u0006\u0002\u0010\u0002J\t\u0010\t\u001a\u00020\nH\u0096\u0002J\u000e\u0010\u000b\u001a\u00028\u0000H\u0096\u0002¢\u0006\u0002\u0010\fR\u001a\u0010\u0003\u001a\u00020\u0004X\u0084\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\b¨\u0006\r"}, d2 = {"Lkotlin/collections/AbstractList$IteratorImpl;", "", "(Lkotlin/collections/AbstractList;)V", com.android.server.am.AssistDataRequester.KEY_RECEIVER_EXTRA_INDEX, "", "getIndex", "()I", "setIndex", "(I)V", "hasNext", "", "next", "()Ljava/lang/Object;", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
    private class IteratorImpl implements java.util.Iterator<E>, kotlin.jvm.internal.markers.KMappedMarker {
        private int index;

        @Override // java.util.Iterator
        public void remove() {
            throw new java.lang.UnsupportedOperationException("Operation is not supported for read-only collection");
        }

        public IteratorImpl() {
        }

        protected final int getIndex() {
            return this.index;
        }

        protected final void setIndex(int i) {
            this.index = i;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.index < kotlin.collections.AbstractList.this.size();
        }

        @Override // java.util.Iterator
        public E next() {
            if (!hasNext()) {
                throw new java.util.NoSuchElementException();
            }
            kotlin.collections.AbstractList<E> abstractList = kotlin.collections.AbstractList.this;
            int i = this.index;
            this.index = i + 1;
            return abstractList.get(i);
        }
    }

    /* JADX INFO: compiled from: AbstractList.kt */
    @kotlin.Metadata(d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010*\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0005\b\u0092\u0004\u0018\u00002\f0\u0001R\b\u0012\u0004\u0012\u00028\u00000\u00022\b\u0012\u0004\u0012\u00028\u00000\u0003B\r\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\b\u0010\u0007\u001a\u00020\bH\u0016J\b\u0010\t\u001a\u00020\u0005H\u0016J\r\u0010\n\u001a\u00028\u0000H\u0016¢\u0006\u0002\u0010\u000bJ\b\u0010\f\u001a\u00020\u0005H\u0016¨\u0006\r"}, d2 = {"Lkotlin/collections/AbstractList$ListIteratorImpl;", "Lkotlin/collections/AbstractList$IteratorImpl;", "Lkotlin/collections/AbstractList;", "", com.android.server.am.AssistDataRequester.KEY_RECEIVER_EXTRA_INDEX, "", "(Lkotlin/collections/AbstractList;I)V", "hasPrevious", "", "nextIndex", "previous", "()Ljava/lang/Object;", "previousIndex", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
    private class ListIteratorImpl extends kotlin.collections.AbstractList<E>.IteratorImpl implements java.util.ListIterator<E>, kotlin.jvm.internal.markers.KMappedMarker {
        @Override // java.util.ListIterator
        public void add(E e) {
            throw new java.lang.UnsupportedOperationException("Operation is not supported for read-only collection");
        }

        @Override // java.util.ListIterator
        public void set(E e) {
            throw new java.lang.UnsupportedOperationException("Operation is not supported for read-only collection");
        }

        public ListIteratorImpl(int index) {
            super();
            kotlin.collections.AbstractList.INSTANCE.checkPositionIndex$kotlin_stdlib(index, kotlin.collections.AbstractList.this.size());
            setIndex(index);
        }

        @Override // java.util.ListIterator
        public boolean hasPrevious() {
            return getIndex() > 0;
        }

        @Override // java.util.ListIterator
        public int nextIndex() {
            return getIndex();
        }

        @Override // java.util.ListIterator
        public E previous() {
            if (!hasPrevious()) {
                throw new java.util.NoSuchElementException();
            }
            kotlin.collections.AbstractList<E> abstractList = kotlin.collections.AbstractList.this;
            setIndex(getIndex() - 1);
            return abstractList.get(getIndex());
        }

        @Override // java.util.ListIterator
        public int previousIndex() {
            return getIndex() - 1;
        }
    }

    /* JADX INFO: compiled from: AbstractList.kt */
    @kotlin.Metadata(d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0012\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u001e\n\u0002\b\u0005\b\u0080\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J%\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u00042\u0006\u0010\b\u001a\u00020\u00042\u0006\u0010\t\u001a\u00020\u0004H\u0000¢\u0006\u0002\b\nJ\u001d\u0010\u000b\u001a\u00020\u00062\u0006\u0010\f\u001a\u00020\u00042\u0006\u0010\t\u001a\u00020\u0004H\u0000¢\u0006\u0002\b\rJ\u001d\u0010\u000e\u001a\u00020\u00062\u0006\u0010\f\u001a\u00020\u00042\u0006\u0010\t\u001a\u00020\u0004H\u0000¢\u0006\u0002\b\u000fJ%\u0010\u0010\u001a\u00020\u00062\u0006\u0010\u0011\u001a\u00020\u00042\u0006\u0010\u0012\u001a\u00020\u00042\u0006\u0010\t\u001a\u00020\u0004H\u0000¢\u0006\u0002\b\u0013J\u001d\u0010\u0014\u001a\u00020\u00042\u0006\u0010\u0015\u001a\u00020\u00042\u0006\u0010\u0016\u001a\u00020\u0004H\u0000¢\u0006\u0002\b\u0017J%\u0010\u0018\u001a\u00020\u00192\n\u0010\u001a\u001a\u0006\u0012\u0002\b\u00030\u001b2\n\u0010\u001c\u001a\u0006\u0012\u0002\b\u00030\u001bH\u0000¢\u0006\u0002\b\u001dJ\u0019\u0010\u001e\u001a\u00020\u00042\n\u0010\u001a\u001a\u0006\u0012\u0002\b\u00030\u001bH\u0000¢\u0006\u0002\b\u001fR\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000¨\u0006 "}, d2 = {"Lkotlin/collections/AbstractList$Companion;", "", "()V", "maxArraySize", "", "checkBoundsIndexes", "", "startIndex", "endIndex", "size", "checkBoundsIndexes$kotlin_stdlib", "checkElementIndex", com.android.server.am.AssistDataRequester.KEY_RECEIVER_EXTRA_INDEX, "checkElementIndex$kotlin_stdlib", "checkPositionIndex", "checkPositionIndex$kotlin_stdlib", "checkRangeIndexes", "fromIndex", "toIndex", "checkRangeIndexes$kotlin_stdlib", "newCapacity", "oldCapacity", "minCapacity", "newCapacity$kotlin_stdlib", "orderedEquals", "", "c", "", "other", "orderedEquals$kotlin_stdlib", "orderedHashCode", "orderedHashCode$kotlin_stdlib", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public static final class Companion {
        public /* synthetic */ Companion(kotlin.jvm.internal.DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final void checkElementIndex$kotlin_stdlib(int index, int size) {
            if (index < 0 || index >= size) {
                throw new java.lang.IndexOutOfBoundsException("index: " + index + ", size: " + size);
            }
        }

        public final void checkPositionIndex$kotlin_stdlib(int index, int size) {
            if (index < 0 || index > size) {
                throw new java.lang.IndexOutOfBoundsException("index: " + index + ", size: " + size);
            }
        }

        public final void checkRangeIndexes$kotlin_stdlib(int fromIndex, int toIndex, int size) {
            if (fromIndex < 0 || toIndex > size) {
                throw new java.lang.IndexOutOfBoundsException("fromIndex: " + fromIndex + ", toIndex: " + toIndex + ", size: " + size);
            }
            if (fromIndex > toIndex) {
                throw new java.lang.IllegalArgumentException("fromIndex: " + fromIndex + " > toIndex: " + toIndex);
            }
        }

        public final void checkBoundsIndexes$kotlin_stdlib(int startIndex, int endIndex, int size) {
            if (startIndex < 0 || endIndex > size) {
                throw new java.lang.IndexOutOfBoundsException("startIndex: " + startIndex + ", endIndex: " + endIndex + ", size: " + size);
            }
            if (startIndex > endIndex) {
                throw new java.lang.IllegalArgumentException("startIndex: " + startIndex + " > endIndex: " + endIndex);
            }
        }

        public final int newCapacity$kotlin_stdlib(int oldCapacity, int minCapacity) {
            int newCapacity = (oldCapacity >> 1) + oldCapacity;
            if (newCapacity - minCapacity < 0) {
                newCapacity = minCapacity;
            }
            int newCapacity2 = kotlin.collections.AbstractList.maxArraySize;
            if (newCapacity - kotlin.collections.AbstractList.maxArraySize > 0) {
                if (minCapacity > kotlin.collections.AbstractList.maxArraySize) {
                    newCapacity2 = Integer.MAX_VALUE;
                }
                return newCapacity2;
            }
            return newCapacity;
        }

        public final int orderedHashCode$kotlin_stdlib(java.util.Collection<?> c) {
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(c, "c");
            int hashCode = 1;
            java.util.Iterator<?> it = c.iterator();
            while (it.hasNext()) {
                java.lang.Object e = it.next();
                hashCode = (hashCode * 31) + (e != null ? e.hashCode() : 0);
            }
            return hashCode;
        }

        public final boolean orderedEquals$kotlin_stdlib(java.util.Collection<?> c, java.util.Collection<?> other) {
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(c, "c");
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(other, "other");
            if (c.size() != other.size()) {
                return false;
            }
            java.util.Iterator<?> it = other.iterator();
            for (java.lang.Object elem : c) {
                java.lang.Object elemOther = it.next();
                if (!kotlin.jvm.internal.Intrinsics.areEqual(elem, elemOther)) {
                    return false;
                }
            }
            return true;
        }
    }
}
