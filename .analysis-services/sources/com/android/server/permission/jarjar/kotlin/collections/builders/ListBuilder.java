package com.android.server.permission.jarjar.kotlin.collections.builders;

/* JADX INFO: compiled from: ListBuilder.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000r\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\f\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0010\u001e\n\u0002\b\u0005\n\u0002\u0010 \n\u0002\b\t\n\u0002\u0010\u0000\n\u0002\b\b\n\u0002\u0010)\n\u0002\b\u0002\n\u0002\u0010+\n\u0002\b\u0016\n\u0002\u0010\u000e\n\u0002\b\u0004\b\u0000\u0018\u0000 X*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u00022\u00060\u0003j\u0002`\u00042\b\u0012\u0004\u0012\u0002H\u00010\u00052\u00060\u0006j\u0002`\u0007:\u0002XYB\u0007\b\u0016¢\u0006\u0002\u0010\bB\u000f\b\u0016\u0012\u0006\u0010\t\u001a\u00020\n¢\u0006\u0002\u0010\u000bBM\b\u0002\u0012\f\u0010\f\u001a\b\u0012\u0004\u0012\u00028\u00000\r\u0012\u0006\u0010\u000e\u001a\u00020\n\u0012\u0006\u0010\u000f\u001a\u00020\n\u0012\u0006\u0010\u0010\u001a\u00020\u0011\u0012\u000e\u0010\u0012\u001a\n\u0012\u0004\u0012\u00028\u0000\u0018\u00010\u0000\u0012\u000e\u0010\u0013\u001a\n\u0012\u0004\u0012\u00028\u0000\u0018\u00010\u0000¢\u0006\u0002\u0010\u0014J\u0015\u0010\u001b\u001a\u00020\u00112\u0006\u0010\u001c\u001a\u00028\u0000H\u0016¢\u0006\u0002\u0010\u001dJ\u001d\u0010\u001b\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020\n2\u0006\u0010\u001c\u001a\u00028\u0000H\u0016¢\u0006\u0002\u0010 J\u001e\u0010!\u001a\u00020\u00112\u0006\u0010\u001f\u001a\u00020\n2\f\u0010\"\u001a\b\u0012\u0004\u0012\u00028\u00000#H\u0016J\u0016\u0010!\u001a\u00020\u00112\f\u0010\"\u001a\b\u0012\u0004\u0012\u00028\u00000#H\u0016J&\u0010$\u001a\u00020\u001e2\u0006\u0010%\u001a\u00020\n2\f\u0010\"\u001a\b\u0012\u0004\u0012\u00028\u00000#2\u0006\u0010&\u001a\u00020\nH\u0002J\u001d\u0010'\u001a\u00020\u001e2\u0006\u0010%\u001a\u00020\n2\u0006\u0010\u001c\u001a\u00028\u0000H\u0002¢\u0006\u0002\u0010 J\f\u0010(\u001a\b\u0012\u0004\u0012\u00028\u00000)J\b\u0010*\u001a\u00020\u001eH\u0002J\b\u0010+\u001a\u00020\u001eH\u0002J\b\u0010,\u001a\u00020\u001eH\u0016J\u0014\u0010-\u001a\u00020\u00112\n\u0010.\u001a\u0006\u0012\u0002\b\u00030)H\u0002J\u0010\u0010/\u001a\u00020\u001e2\u0006\u00100\u001a\u00020\nH\u0002J\u0010\u00101\u001a\u00020\u001e2\u0006\u0010&\u001a\u00020\nH\u0002J\u0013\u00102\u001a\u00020\u00112\b\u0010.\u001a\u0004\u0018\u000103H\u0096\u0002J\u0016\u00104\u001a\u00028\u00002\u0006\u0010\u001f\u001a\u00020\nH\u0096\u0002¢\u0006\u0002\u00105J\b\u00106\u001a\u00020\nH\u0016J\u0015\u00107\u001a\u00020\n2\u0006\u0010\u001c\u001a\u00028\u0000H\u0016¢\u0006\u0002\u00108J\u0018\u00109\u001a\u00020\u001e2\u0006\u0010%\u001a\u00020\n2\u0006\u0010&\u001a\u00020\nH\u0002J\b\u0010:\u001a\u00020\u0011H\u0016J\u000f\u0010;\u001a\b\u0012\u0004\u0012\u00028\u00000<H\u0096\u0002J\u0015\u0010=\u001a\u00020\n2\u0006\u0010\u001c\u001a\u00028\u0000H\u0016¢\u0006\u0002\u00108J\u000e\u0010>\u001a\b\u0012\u0004\u0012\u00028\u00000?H\u0016J\u0016\u0010>\u001a\b\u0012\u0004\u0012\u00028\u00000?2\u0006\u0010\u001f\u001a\u00020\nH\u0016J\b\u0010@\u001a\u00020\u001eH\u0002J\u0015\u0010A\u001a\u00020\u00112\u0006\u0010\u001c\u001a\u00028\u0000H\u0016¢\u0006\u0002\u0010\u001dJ\u0016\u0010B\u001a\u00020\u00112\f\u0010\"\u001a\b\u0012\u0004\u0012\u00028\u00000#H\u0016J\u0015\u0010C\u001a\u00028\u00002\u0006\u0010\u001f\u001a\u00020\nH\u0016¢\u0006\u0002\u00105J\u0015\u0010D\u001a\u00028\u00002\u0006\u0010%\u001a\u00020\nH\u0002¢\u0006\u0002\u00105J\u0018\u0010E\u001a\u00020\u001e2\u0006\u0010F\u001a\u00020\n2\u0006\u0010G\u001a\u00020\nH\u0002J\u0016\u0010H\u001a\u00020\u00112\f\u0010\"\u001a\b\u0012\u0004\u0012\u00028\u00000#H\u0016J.\u0010I\u001a\u00020\n2\u0006\u0010F\u001a\u00020\n2\u0006\u0010G\u001a\u00020\n2\f\u0010\"\u001a\b\u0012\u0004\u0012\u00028\u00000#2\u0006\u0010J\u001a\u00020\u0011H\u0002J\u001e\u0010K\u001a\u00028\u00002\u0006\u0010\u001f\u001a\u00020\n2\u0006\u0010\u001c\u001a\u00028\u0000H\u0096\u0002¢\u0006\u0002\u0010LJ\u001e\u0010M\u001a\b\u0012\u0004\u0012\u00028\u00000\u00022\u0006\u0010N\u001a\u00020\n2\u0006\u0010O\u001a\u00020\nH\u0016J\u0015\u0010P\u001a\n\u0012\u0006\u0012\u0004\u0018\u0001030\rH\u0016¢\u0006\u0002\u0010QJ'\u0010P\u001a\b\u0012\u0004\u0012\u0002HR0\r\"\u0004\b\u0001\u0010R2\f\u0010S\u001a\b\u0012\u0004\u0012\u0002HR0\rH\u0016¢\u0006\u0002\u0010TJ\b\u0010U\u001a\u00020VH\u0016J\b\u0010W\u001a\u000203H\u0002R\u0016\u0010\f\u001a\b\u0012\u0004\u0012\u00028\u00000\rX\u0082\u000e¢\u0006\u0004\n\u0002\u0010\u0015R\u0016\u0010\u0012\u001a\n\u0012\u0004\u0012\u00028\u0000\u0018\u00010\u0000X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0016\u001a\u00020\u00118BX\u0082\u0004¢\u0006\u0006\u001a\u0004\b\u0016\u0010\u0017R\u000e\u0010\u0010\u001a\u00020\u0011X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\nX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\nX\u0082\u000e¢\u0006\u0002\n\u0000R\u0016\u0010\u0013\u001a\n\u0012\u0004\u0012\u00028\u0000\u0018\u00010\u0000X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0018\u001a\u00020\n8VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\u0019\u0010\u001a¨\u0006Z"}, d2 = {"Lkotlin/collections/builders/ListBuilder;", "E", "", "Ljava/util/RandomAccess;", "Lkotlin/collections/RandomAccess;", "Lkotlin/collections/AbstractMutableList;", "Ljava/io/Serializable;", "Lkotlin/io/Serializable;", "()V", "initialCapacity", "", "(I)V", "array", "", "offset", "length", "isReadOnly", "", "backing", "root", "([Ljava/lang/Object;IIZLkotlin/collections/builders/ListBuilder;Lkotlin/collections/builders/ListBuilder;)V", "[Ljava/lang/Object;", "isEffectivelyReadOnly", "()Z", "size", "getSize", "()I", "add", "element", "(Ljava/lang/Object;)Z", "", com.android.server.am.AssistDataRequester.KEY_RECEIVER_EXTRA_INDEX, "(ILjava/lang/Object;)V", "addAll", "elements", "", "addAllInternal", "i", "n", "addAtInternal", "build", "", "checkForComodification", "checkIsMutable", "clear", "contentEquals", "other", "ensureCapacityInternal", "minCapacity", "ensureExtraCapacity", "equals", "", "get", "(I)Ljava/lang/Object;", com.android.server.oplus.osense.OsenseConstants.KEY_INTEGER_HASH, "indexOf", "(Ljava/lang/Object;)I", "insertAtInternal", "isEmpty", "iterator", "", "lastIndexOf", "listIterator", "", "registerModification", "remove", "removeAll", "removeAt", "removeAtInternal", "removeRangeInternal", "rangeOffset", "rangeLength", "retainAll", "retainOrRemoveAllInternal", "retain", "set", "(ILjava/lang/Object;)Ljava/lang/Object;", "subList", "fromIndex", "toIndex", "toArray", "()[Ljava/lang/Object;", "T", "destination", "([Ljava/lang/Object;)[Ljava/lang/Object;", "toString", "", "writeReplace", "Companion", "Itr", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class ListBuilder<E> extends com.android.server.permission.jarjar.kotlin.collections.AbstractMutableList<E> implements java.util.List<E>, java.util.RandomAccess, java.io.Serializable, com.android.server.permission.jarjar.kotlin.jvm.internal.markers.KMutableList {
    private static final com.android.server.permission.jarjar.kotlin.collections.builders.ListBuilder.Companion Companion = new com.android.server.permission.jarjar.kotlin.collections.builders.ListBuilder.Companion(null);
    private static final com.android.server.permission.jarjar.kotlin.collections.builders.ListBuilder Empty;
    private E[] array;
    private final com.android.server.permission.jarjar.kotlin.collections.builders.ListBuilder<E> backing;
    private boolean isReadOnly;
    private int length;
    private int offset;
    private final com.android.server.permission.jarjar.kotlin.collections.builders.ListBuilder<E> root;

    /* JADX INFO: compiled from: ListBuilder.kt */
    @com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0001\n\u0000\b\u0082\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u0014\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0006"}, d2 = {"Lkotlin/collections/builders/ListBuilder$Companion;", "", "()V", "Empty", "Lkotlin/collections/builders/ListBuilder;", "", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
    private static final class Companion {
        public /* synthetic */ Companion(com.android.server.permission.jarjar.kotlin.jvm.internal.DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    private ListBuilder(E[] eArr, int offset, int length, boolean isReadOnly, com.android.server.permission.jarjar.kotlin.collections.builders.ListBuilder<E> listBuilder, com.android.server.permission.jarjar.kotlin.collections.builders.ListBuilder<E> listBuilder2) {
        this.array = eArr;
        this.offset = offset;
        this.length = length;
        this.isReadOnly = isReadOnly;
        this.backing = listBuilder;
        this.root = listBuilder2;
        if (this.backing != null) {
            this.modCount = this.backing.modCount;
        }
    }

    static {
        com.android.server.permission.jarjar.kotlin.collections.builders.ListBuilder it = new com.android.server.permission.jarjar.kotlin.collections.builders.ListBuilder(0);
        it.isReadOnly = true;
        Empty = it;
    }

    public ListBuilder() {
        this(10);
    }

    public ListBuilder(int initialCapacity) {
        this(com.android.server.permission.jarjar.kotlin.collections.builders.ListBuilderKt.arrayOfUninitializedElements(initialCapacity), 0, 0, false, null, null);
    }

    public final java.util.List<E> build() {
        if (this.backing != null) {
            throw new java.lang.IllegalStateException();
        }
        checkIsMutable();
        this.isReadOnly = true;
        return this.length > 0 ? this : Empty;
    }

    private final java.lang.Object writeReplace() throws java.io.NotSerializableException {
        if (isEffectivelyReadOnly()) {
            return new com.android.server.permission.jarjar.kotlin.collections.builders.SerializedCollection(this, 0);
        }
        throw new java.io.NotSerializableException("The list cannot be serialized while it is being built.");
    }

    @Override // com.android.server.permission.jarjar.kotlin.collections.AbstractMutableList
    public int getSize() {
        checkForComodification();
        return this.length;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public boolean isEmpty() {
        checkForComodification();
        return this.length == 0;
    }

    @Override // java.util.AbstractList, java.util.List
    public E get(int index) {
        checkForComodification();
        com.android.server.permission.jarjar.kotlin.collections.AbstractList.Companion.checkElementIndex$kotlin_stdlib(index, this.length);
        return this.array[this.offset + index];
    }

    @Override // com.android.server.permission.jarjar.kotlin.collections.AbstractMutableList, java.util.AbstractList, java.util.List
    public E set(int index, E e) {
        checkIsMutable();
        checkForComodification();
        com.android.server.permission.jarjar.kotlin.collections.AbstractList.Companion.checkElementIndex$kotlin_stdlib(index, this.length);
        E e2 = this.array[this.offset + index];
        this.array[this.offset + index] = e;
        return e2;
    }

    @Override // java.util.AbstractList, java.util.List
    public int indexOf(java.lang.Object element) {
        checkForComodification();
        for (int i = 0; i < this.length; i++) {
            if (com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.areEqual(this.array[this.offset + i], element)) {
                return i;
            }
        }
        return -1;
    }

    @Override // java.util.AbstractList, java.util.List
    public int lastIndexOf(java.lang.Object element) {
        checkForComodification();
        for (int i = this.length - 1; i >= 0; i--) {
            if (com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.areEqual(this.array[this.offset + i], element)) {
                return i;
            }
        }
        return -1;
    }

    @Override // java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.List
    public java.util.Iterator<E> iterator() {
        return listIterator(0);
    }

    @Override // java.util.AbstractList, java.util.List
    public java.util.ListIterator<E> listIterator() {
        return listIterator(0);
    }

    @Override // java.util.AbstractList, java.util.List
    public java.util.ListIterator<E> listIterator(int index) {
        checkForComodification();
        com.android.server.permission.jarjar.kotlin.collections.AbstractList.Companion.checkPositionIndex$kotlin_stdlib(index, this.length);
        return new com.android.server.permission.jarjar.kotlin.collections.builders.ListBuilder.Itr(this, index);
    }

    @Override // java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.util.List
    public boolean add(E e) {
        checkIsMutable();
        checkForComodification();
        addAtInternal(this.offset + this.length, e);
        return true;
    }

    @Override // com.android.server.permission.jarjar.kotlin.collections.AbstractMutableList, java.util.AbstractList, java.util.List
    public void add(int index, E e) {
        checkIsMutable();
        checkForComodification();
        com.android.server.permission.jarjar.kotlin.collections.AbstractList.Companion.checkPositionIndex$kotlin_stdlib(index, this.length);
        addAtInternal(this.offset + index, e);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public boolean addAll(java.util.Collection<? extends E> collection) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(collection, "elements");
        checkIsMutable();
        checkForComodification();
        int n = collection.size();
        addAllInternal(this.offset + this.length, collection, n);
        return n > 0;
    }

    @Override // java.util.AbstractList, java.util.List
    public boolean addAll(int index, java.util.Collection<? extends E> collection) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(collection, "elements");
        checkIsMutable();
        checkForComodification();
        com.android.server.permission.jarjar.kotlin.collections.AbstractList.Companion.checkPositionIndex$kotlin_stdlib(index, this.length);
        int n = collection.size();
        addAllInternal(this.offset + index, collection, n);
        return n > 0;
    }

    @Override // java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.util.List
    public void clear() {
        checkIsMutable();
        checkForComodification();
        removeRangeInternal(this.offset, this.length);
    }

    @Override // com.android.server.permission.jarjar.kotlin.collections.AbstractMutableList
    public E removeAt(int index) {
        checkIsMutable();
        checkForComodification();
        com.android.server.permission.jarjar.kotlin.collections.AbstractList.Companion.checkElementIndex$kotlin_stdlib(index, this.length);
        return removeAtInternal(this.offset + index);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public boolean remove(java.lang.Object element) {
        checkIsMutable();
        checkForComodification();
        int i = indexOf(element);
        if (i >= 0) {
            remove(i);
        }
        return i >= 0;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public boolean removeAll(java.util.Collection<? extends java.lang.Object> collection) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(collection, "elements");
        checkIsMutable();
        checkForComodification();
        return retainOrRemoveAllInternal(this.offset, this.length, collection, false) > 0;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public boolean retainAll(java.util.Collection<? extends java.lang.Object> collection) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(collection, "elements");
        checkIsMutable();
        checkForComodification();
        return retainOrRemoveAllInternal(this.offset, this.length, collection, true) > 0;
    }

    @Override // java.util.AbstractList, java.util.List
    public java.util.List<E> subList(int fromIndex, int toIndex) {
        com.android.server.permission.jarjar.kotlin.collections.AbstractList.Companion.checkRangeIndexes$kotlin_stdlib(fromIndex, toIndex, this.length);
        E[] eArr = this.array;
        int i = this.offset + fromIndex;
        int i2 = toIndex - fromIndex;
        boolean z = this.isReadOnly;
        com.android.server.permission.jarjar.kotlin.collections.builders.ListBuilder<E> listBuilder = this.root;
        return new com.android.server.permission.jarjar.kotlin.collections.builders.ListBuilder(eArr, i, i2, z, this, listBuilder == null ? this : listBuilder);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public <T> T[] toArray(T[] tArr) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(tArr, "destination");
        checkForComodification();
        if (tArr.length < this.length) {
            T[] tArr2 = (T[]) java.util.Arrays.copyOfRange(this.array, this.offset, this.offset + this.length, tArr.getClass());
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(tArr2, "copyOfRange(...)");
            return tArr2;
        }
        com.android.server.permission.jarjar.kotlin.collections.ArraysKt.copyInto(this.array, tArr, 0, this.offset, this.offset + this.length);
        return (T[]) com.android.server.permission.jarjar.kotlin.collections.CollectionsKt.terminateCollectionToArray(this.length, tArr);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public java.lang.Object[] toArray() {
        checkForComodification();
        return com.android.server.permission.jarjar.kotlin.collections.ArraysKt.copyOfRange(this.array, this.offset, this.offset + this.length);
    }

    @Override // java.util.AbstractList, java.util.Collection, java.util.List
    public boolean equals(java.lang.Object other) {
        checkForComodification();
        return other == this || ((other instanceof java.util.List) && contentEquals((java.util.List) other));
    }

    @Override // java.util.AbstractList, java.util.Collection, java.util.List
    public int hashCode() {
        checkForComodification();
        return com.android.server.permission.jarjar.kotlin.collections.builders.ListBuilderKt.subarrayContentHashCode(this.array, this.offset, this.length);
    }

    @Override // java.util.AbstractCollection
    public java.lang.String toString() {
        checkForComodification();
        return com.android.server.permission.jarjar.kotlin.collections.builders.ListBuilderKt.subarrayContentToString(this.array, this.offset, this.length, this);
    }

    private final void registerModification() {
        this.modCount++;
    }

    private final void checkForComodification() {
        if (this.root != null && this.root.modCount != this.modCount) {
            throw new java.util.ConcurrentModificationException();
        }
    }

    private final void checkIsMutable() {
        if (isEffectivelyReadOnly()) {
            throw new java.lang.UnsupportedOperationException();
        }
    }

    private final boolean isEffectivelyReadOnly() {
        return this.isReadOnly || (this.root != null && this.root.isReadOnly);
    }

    private final void ensureExtraCapacity(int n) {
        ensureCapacityInternal(this.length + n);
    }

    private final void ensureCapacityInternal(int i) {
        if (i < 0) {
            throw new java.lang.OutOfMemoryError();
        }
        if (i > this.array.length) {
            this.array = (E[]) com.android.server.permission.jarjar.kotlin.collections.builders.ListBuilderKt.copyOfUninitializedElements(this.array, com.android.server.permission.jarjar.kotlin.collections.AbstractList.Companion.newCapacity$kotlin_stdlib(this.array.length, i));
        }
    }

    private final boolean contentEquals(java.util.List<?> list) {
        return com.android.server.permission.jarjar.kotlin.collections.builders.ListBuilderKt.subarrayContentEquals(this.array, this.offset, this.length, list);
    }

    private final void insertAtInternal(int i, int n) {
        ensureExtraCapacity(n);
        com.android.server.permission.jarjar.kotlin.collections.ArraysKt.copyInto(this.array, this.array, i + n, i, this.offset + this.length);
        this.length += n;
    }

    private final void addAtInternal(int i, E e) {
        registerModification();
        if (this.backing != null) {
            this.backing.addAtInternal(i, e);
            this.array = this.backing.array;
            this.length++;
        } else {
            insertAtInternal(i, 1);
            this.array[i] = e;
        }
    }

    private final void addAllInternal(int i, java.util.Collection<? extends E> collection, int n) {
        registerModification();
        if (this.backing != null) {
            this.backing.addAllInternal(i, collection, n);
            this.array = this.backing.array;
            this.length += n;
        } else {
            insertAtInternal(i, n);
            java.util.Iterator<? extends E> it = collection.iterator();
            for (int j = 0; j < n; j++) {
                this.array[i + j] = it.next();
            }
        }
    }

    private final E removeAtInternal(int i) {
        registerModification();
        if (this.backing != null) {
            this.length--;
            return this.backing.removeAtInternal(i);
        }
        E e = this.array[i];
        com.android.server.permission.jarjar.kotlin.collections.ArraysKt.copyInto(this.array, this.array, i, i + 1, this.offset + this.length);
        com.android.server.permission.jarjar.kotlin.collections.builders.ListBuilderKt.resetAt(this.array, (this.offset + this.length) - 1);
        this.length--;
        return e;
    }

    private final void removeRangeInternal(int rangeOffset, int rangeLength) {
        if (rangeLength > 0) {
            registerModification();
        }
        if (this.backing != null) {
            this.backing.removeRangeInternal(rangeOffset, rangeLength);
        } else {
            com.android.server.permission.jarjar.kotlin.collections.ArraysKt.copyInto(this.array, this.array, rangeOffset, rangeOffset + rangeLength, this.length);
            com.android.server.permission.jarjar.kotlin.collections.builders.ListBuilderKt.resetRange(this.array, this.length - rangeLength, this.length);
        }
        this.length -= rangeLength;
    }

    private final int retainOrRemoveAllInternal(int rangeOffset, int rangeLength, java.util.Collection<? extends E> collection, boolean retain) {
        int removed;
        if (this.backing != null) {
            removed = this.backing.retainOrRemoveAllInternal(rangeOffset, rangeLength, collection, retain);
        } else {
            int i = 0;
            int j = 0;
            while (i < rangeLength) {
                if (collection.contains(this.array[rangeOffset + i]) == retain) {
                    this.array[j + rangeOffset] = this.array[i + rangeOffset];
                    j++;
                    i++;
                } else {
                    i++;
                }
            }
            int removed2 = rangeLength - j;
            com.android.server.permission.jarjar.kotlin.collections.ArraysKt.copyInto(this.array, this.array, rangeOffset + j, rangeOffset + rangeLength, this.length);
            com.android.server.permission.jarjar.kotlin.collections.builders.ListBuilderKt.resetRange(this.array, this.length - removed2, this.length);
            removed = removed2;
        }
        if (removed > 0) {
            registerModification();
        }
        this.length -= removed;
        return removed;
    }

    /* JADX INFO: compiled from: ListBuilder.kt */
    @com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010+\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\t\b\u0002\u0018\u0000*\u0004\b\u0001\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u0002B\u001d\b\u0016\u0012\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u00028\u00010\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0006¢\u0006\u0002\u0010\u0007J\u0015\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00028\u0001H\u0016¢\u0006\u0002\u0010\rJ\b\u0010\u000e\u001a\u00020\u000bH\u0002J\t\u0010\u000f\u001a\u00020\u0010H\u0096\u0002J\b\u0010\u0011\u001a\u00020\u0010H\u0016J\u000e\u0010\u0012\u001a\u00028\u0001H\u0096\u0002¢\u0006\u0002\u0010\u0013J\b\u0010\u0014\u001a\u00020\u0006H\u0016J\r\u0010\u0015\u001a\u00028\u0001H\u0016¢\u0006\u0002\u0010\u0013J\b\u0010\u0016\u001a\u00020\u0006H\u0016J\b\u0010\u0017\u001a\u00020\u000bH\u0016J\u0015\u0010\u0018\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00028\u0001H\u0016¢\u0006\u0002\u0010\rR\u000e\u0010\b\u001a\u00020\u0006X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0006X\u0082\u000e¢\u0006\u0002\n\u0000R\u0014\u0010\u0003\u001a\b\u0012\u0004\u0012\u00028\u00010\u0004X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0019"}, d2 = {"Lkotlin/collections/builders/ListBuilder$Itr;", "E", "", "list", "Lkotlin/collections/builders/ListBuilder;", com.android.server.am.AssistDataRequester.KEY_RECEIVER_EXTRA_INDEX, "", "(Lkotlin/collections/builders/ListBuilder;I)V", "expectedModCount", "lastIndex", "add", "", "element", "(Ljava/lang/Object;)V", "checkForComodification", "hasNext", "", "hasPrevious", "next", "()Ljava/lang/Object;", "nextIndex", "previous", "previousIndex", "remove", "set", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
    private static final class Itr<E> implements java.util.ListIterator<E>, com.android.server.permission.jarjar.kotlin.jvm.internal.markers.KMutableListIterator {
        private int expectedModCount;
        private int index;
        private int lastIndex;
        private final com.android.server.permission.jarjar.kotlin.collections.builders.ListBuilder<E> list;

        public Itr(com.android.server.permission.jarjar.kotlin.collections.builders.ListBuilder<E> listBuilder, int index) {
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(listBuilder, "list");
            this.list = listBuilder;
            this.index = index;
            this.lastIndex = -1;
            this.expectedModCount = listBuilder.modCount;
        }

        @Override // java.util.ListIterator
        public boolean hasPrevious() {
            return this.index > 0;
        }

        @Override // java.util.ListIterator, java.util.Iterator
        public boolean hasNext() {
            return this.index < ((com.android.server.permission.jarjar.kotlin.collections.builders.ListBuilder) this.list).length;
        }

        @Override // java.util.ListIterator
        public int previousIndex() {
            return this.index - 1;
        }

        @Override // java.util.ListIterator
        public int nextIndex() {
            return this.index;
        }

        @Override // java.util.ListIterator
        public E previous() {
            checkForComodification();
            if (this.index <= 0) {
                throw new java.util.NoSuchElementException();
            }
            this.index--;
            this.lastIndex = this.index;
            return (E) ((com.android.server.permission.jarjar.kotlin.collections.builders.ListBuilder) this.list).array[((com.android.server.permission.jarjar.kotlin.collections.builders.ListBuilder) this.list).offset + this.lastIndex];
        }

        @Override // java.util.ListIterator, java.util.Iterator
        public E next() {
            checkForComodification();
            if (this.index >= ((com.android.server.permission.jarjar.kotlin.collections.builders.ListBuilder) this.list).length) {
                throw new java.util.NoSuchElementException();
            }
            int i = this.index;
            this.index = i + 1;
            this.lastIndex = i;
            return (E) ((com.android.server.permission.jarjar.kotlin.collections.builders.ListBuilder) this.list).array[((com.android.server.permission.jarjar.kotlin.collections.builders.ListBuilder) this.list).offset + this.lastIndex];
        }

        @Override // java.util.ListIterator
        public void set(E e) {
            checkForComodification();
            if (!(this.lastIndex != -1)) {
                throw new java.lang.IllegalStateException("Call next() or previous() before replacing element from the iterator.".toString());
            }
            this.list.set(this.lastIndex, e);
        }

        @Override // java.util.ListIterator
        public void add(E e) {
            checkForComodification();
            com.android.server.permission.jarjar.kotlin.collections.builders.ListBuilder<E> listBuilder = this.list;
            int i = this.index;
            this.index = i + 1;
            listBuilder.add(i, e);
            this.lastIndex = -1;
            this.expectedModCount = this.list.modCount;
        }

        @Override // java.util.ListIterator, java.util.Iterator
        public void remove() {
            checkForComodification();
            if (!(this.lastIndex != -1)) {
                throw new java.lang.IllegalStateException("Call next() or previous() before removing element from the iterator.".toString());
            }
            this.list.remove(this.lastIndex);
            this.index = this.lastIndex;
            this.lastIndex = -1;
            this.expectedModCount = this.list.modCount;
        }

        private final void checkForComodification() {
            if (this.list.modCount != this.expectedModCount) {
                throw new java.util.ConcurrentModificationException();
            }
        }
    }
}
