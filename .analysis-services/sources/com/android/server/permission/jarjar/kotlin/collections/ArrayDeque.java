package com.android.server.permission.jarjar.kotlin.collections;

/* JADX INFO: compiled from: ArrayDeque.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000L\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u001e\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0002\u0010\u0000\n\u0002\b\u0007\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0011\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u001b\b\u0007\u0018\u0000 P*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u0002:\u0001PB\u000f\b\u0016\u0012\u0006\u0010\u0003\u001a\u00020\u0004¢\u0006\u0002\u0010\u0005B\u0007\b\u0016¢\u0006\u0002\u0010\u0006B\u0015\b\u0016\u0012\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00028\u00000\b¢\u0006\u0002\u0010\tJ\u0015\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00028\u0000H\u0016¢\u0006\u0002\u0010\u0016J\u001d\u0010\u0013\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u00042\u0006\u0010\u0015\u001a\u00028\u0000H\u0016¢\u0006\u0002\u0010\u0019J\u001e\u0010\u001a\u001a\u00020\u00142\u0006\u0010\u0018\u001a\u00020\u00042\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00028\u00000\bH\u0016J\u0016\u0010\u001a\u001a\u00020\u00142\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00028\u00000\bH\u0016J\u0013\u0010\u001b\u001a\u00020\u00172\u0006\u0010\u0015\u001a\u00028\u0000¢\u0006\u0002\u0010\u001cJ\u0013\u0010\u001d\u001a\u00020\u00172\u0006\u0010\u0015\u001a\u00028\u0000¢\u0006\u0002\u0010\u001cJ\b\u0010\u001e\u001a\u00020\u0017H\u0016J\u0016\u0010\u001f\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00028\u0000H\u0096\u0002¢\u0006\u0002\u0010\u0016J\u001e\u0010 \u001a\u00020\u00172\u0006\u0010!\u001a\u00020\u00042\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00028\u00000\bH\u0002J\u0010\u0010\"\u001a\u00020\u00172\u0006\u0010#\u001a\u00020\u0004H\u0002J\u0010\u0010$\u001a\u00020\u00042\u0006\u0010\u0018\u001a\u00020\u0004H\u0002J\u0010\u0010%\u001a\u00020\u00172\u0006\u0010&\u001a\u00020\u0004H\u0002J\u001d\u0010'\u001a\u00020\u00142\u0012\u0010(\u001a\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00020\u00140)H\u0082\bJ\u000b\u0010*\u001a\u00028\u0000¢\u0006\u0002\u0010+J\r\u0010,\u001a\u0004\u0018\u00018\u0000¢\u0006\u0002\u0010+J\u0016\u0010-\u001a\u00028\u00002\u0006\u0010\u0018\u001a\u00020\u0004H\u0096\u0002¢\u0006\u0002\u0010.J\u0010\u0010/\u001a\u00020\u00042\u0006\u0010\u0018\u001a\u00020\u0004H\u0002J\u0015\u00100\u001a\u00020\u00042\u0006\u0010\u0015\u001a\u00028\u0000H\u0016¢\u0006\u0002\u00101J\u0016\u00102\u001a\u00028\u00002\u0006\u0010!\u001a\u00020\u0004H\u0083\b¢\u0006\u0002\u0010.J\u0011\u0010!\u001a\u00020\u00042\u0006\u0010\u0018\u001a\u00020\u0004H\u0083\bJM\u00103\u001a\u00020\u00172>\u00104\u001a:\u0012\u0013\u0012\u00110\u0004¢\u0006\f\b6\u0012\b\b7\u0012\u0004\b\b(\u000e\u0012\u001b\u0012\u0019\u0012\u0006\u0012\u0004\u0018\u00010\f0\u000b¢\u0006\f\b6\u0012\b\b7\u0012\u0004\b\b(\u0007\u0012\u0004\u0012\u00020\u001705H\u0000¢\u0006\u0002\b8J\b\u00109\u001a\u00020\u0014H\u0016J\u000b\u0010:\u001a\u00028\u0000¢\u0006\u0002\u0010+J\u0015\u0010;\u001a\u00020\u00042\u0006\u0010\u0015\u001a\u00028\u0000H\u0016¢\u0006\u0002\u00101J\r\u0010<\u001a\u0004\u0018\u00018\u0000¢\u0006\u0002\u0010+J\u0010\u0010=\u001a\u00020\u00042\u0006\u0010\u0018\u001a\u00020\u0004H\u0002J\u0010\u0010>\u001a\u00020\u00042\u0006\u0010\u0018\u001a\u00020\u0004H\u0002J\u0015\u0010?\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00028\u0000H\u0016¢\u0006\u0002\u0010\u0016J\u0016\u0010@\u001a\u00020\u00142\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00028\u00000\bH\u0016J\u0015\u0010A\u001a\u00028\u00002\u0006\u0010\u0018\u001a\u00020\u0004H\u0016¢\u0006\u0002\u0010.J\u000b\u0010B\u001a\u00028\u0000¢\u0006\u0002\u0010+J\r\u0010C\u001a\u0004\u0018\u00018\u0000¢\u0006\u0002\u0010+J\u000b\u0010D\u001a\u00028\u0000¢\u0006\u0002\u0010+J\r\u0010E\u001a\u0004\u0018\u00018\u0000¢\u0006\u0002\u0010+J\u0016\u0010F\u001a\u00020\u00142\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00028\u00000\bH\u0016J\u001e\u0010G\u001a\u00028\u00002\u0006\u0010\u0018\u001a\u00020\u00042\u0006\u0010\u0015\u001a\u00028\u0000H\u0096\u0002¢\u0006\u0002\u0010HJ\u0017\u0010I\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\f0\u000bH\u0000¢\u0006\u0004\bJ\u0010KJ)\u0010I\u001a\b\u0012\u0004\u0012\u0002HL0\u000b\"\u0004\b\u0001\u0010L2\f\u0010M\u001a\b\u0012\u0004\u0012\u0002HL0\u000bH\u0000¢\u0006\u0004\bJ\u0010NJ\u0015\u0010O\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\f0\u000bH\u0016¢\u0006\u0002\u0010KJ'\u0010O\u001a\b\u0012\u0004\u0012\u0002HL0\u000b\"\u0004\b\u0001\u0010L2\f\u0010M\u001a\b\u0012\u0004\u0012\u0002HL0\u000bH\u0016¢\u0006\u0002\u0010NR\u0018\u0010\n\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\f0\u000bX\u0082\u000e¢\u0006\u0004\n\u0002\u0010\rR\u000e\u0010\u000e\u001a\u00020\u0004X\u0082\u000e¢\u0006\u0002\n\u0000R\u001e\u0010\u0010\u001a\u00020\u00042\u0006\u0010\u000f\u001a\u00020\u0004@RX\u0096\u000e¢\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012¨\u0006Q"}, d2 = {"Lkotlin/collections/ArrayDeque;", "E", "Lkotlin/collections/AbstractMutableList;", "initialCapacity", "", "(I)V", "()V", "elements", "", "(Ljava/util/Collection;)V", "elementData", "", "", "[Ljava/lang/Object;", "head", "<set-?>", "size", "getSize", "()I", "add", "", "element", "(Ljava/lang/Object;)Z", "", com.android.server.am.AssistDataRequester.KEY_RECEIVER_EXTRA_INDEX, "(ILjava/lang/Object;)V", "addAll", "addFirst", "(Ljava/lang/Object;)V", "addLast", "clear", "contains", "copyCollectionElements", "internalIndex", "copyElements", "newCapacity", "decremented", "ensureCapacity", "minCapacity", "filterInPlace", "predicate", "Lkotlin/Function1;", "first", "()Ljava/lang/Object;", "firstOrNull", "get", "(I)Ljava/lang/Object;", "incremented", "indexOf", "(Ljava/lang/Object;)I", "internalGet", "internalStructure", com.android.server.wm.ActivityTaskManagerInternal.ASSIST_KEY_STRUCTURE, "Lkotlin/Function2;", "Lkotlin/ParameterName;", "name", "internalStructure$kotlin_stdlib", "isEmpty", "last", "lastIndexOf", "lastOrNull", "negativeMod", "positiveMod", "remove", "removeAll", "removeAt", "removeFirst", "removeFirstOrNull", "removeLast", "removeLastOrNull", "retainAll", "set", "(ILjava/lang/Object;)Ljava/lang/Object;", "testToArray", "testToArray$kotlin_stdlib", "()[Ljava/lang/Object;", "T", "array", "([Ljava/lang/Object;)[Ljava/lang/Object;", "toArray", "Companion", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class ArrayDeque<E> extends com.android.server.permission.jarjar.kotlin.collections.AbstractMutableList<E> {
    private static final int defaultMinCapacity = 10;
    private java.lang.Object[] elementData;
    private int head;
    private int size;
    public static final com.android.server.permission.jarjar.kotlin.collections.ArrayDeque.Companion Companion = new com.android.server.permission.jarjar.kotlin.collections.ArrayDeque.Companion(null);
    private static final java.lang.Object[] emptyElementData = new java.lang.Object[0];

    @Override // com.android.server.permission.jarjar.kotlin.collections.AbstractMutableList
    public int getSize() {
        return this.size;
    }

    public ArrayDeque(int initialCapacity) {
        java.lang.Object[] objArr;
        if (initialCapacity == 0) {
            objArr = emptyElementData;
        } else {
            if (initialCapacity <= 0) {
                throw new java.lang.IllegalArgumentException("Illegal Capacity: " + initialCapacity);
            }
            objArr = new java.lang.Object[initialCapacity];
        }
        this.elementData = objArr;
    }

    public ArrayDeque() {
        this.elementData = emptyElementData;
    }

    public ArrayDeque(java.util.Collection<? extends E> collection) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(collection, "elements");
        this.elementData = collection.toArray(new java.lang.Object[0]);
        this.size = this.elementData.length;
        if (this.elementData.length == 0) {
            this.elementData = emptyElementData;
        }
    }

    private final void ensureCapacity(int minCapacity) {
        if (minCapacity < 0) {
            throw new java.lang.IllegalStateException("Deque is too big.");
        }
        if (minCapacity <= this.elementData.length) {
            return;
        }
        if (this.elementData == emptyElementData) {
            this.elementData = new java.lang.Object[com.android.server.permission.jarjar.kotlin.ranges.RangesKt.coerceAtLeast(minCapacity, 10)];
        } else {
            int newCapacity = com.android.server.permission.jarjar.kotlin.collections.AbstractList.Companion.newCapacity$kotlin_stdlib(this.elementData.length, minCapacity);
            copyElements(newCapacity);
        }
    }

    private final void copyElements(int newCapacity) {
        java.lang.Object[] newElements = new java.lang.Object[newCapacity];
        com.android.server.permission.jarjar.kotlin.collections.ArraysKt.copyInto(this.elementData, newElements, 0, this.head, this.elementData.length);
        com.android.server.permission.jarjar.kotlin.collections.ArraysKt.copyInto(this.elementData, newElements, this.elementData.length - this.head, 0, this.head);
        this.head = 0;
        this.elementData = newElements;
    }

    private final E internalGet(int i) {
        return (E) this.elementData[i];
    }

    private final int positiveMod(int index) {
        return index >= this.elementData.length ? index - this.elementData.length : index;
    }

    private final int negativeMod(int index) {
        return index < 0 ? this.elementData.length + index : index;
    }

    private final int internalIndex(int index) {
        return positiveMod(this.head + index);
    }

    private final int incremented(int index) {
        if (index == com.android.server.permission.jarjar.kotlin.collections.ArraysKt.getLastIndex(this.elementData)) {
            return 0;
        }
        return index + 1;
    }

    private final int decremented(int index) {
        return index == 0 ? com.android.server.permission.jarjar.kotlin.collections.ArraysKt.getLastIndex(this.elementData) : index - 1;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public boolean isEmpty() {
        return size() == 0;
    }

    public final E first() {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException("ArrayDeque is empty.");
        }
        return (E) this.elementData[this.head];
    }

    public final E firstOrNull() {
        if (isEmpty()) {
            return null;
        }
        return (E) this.elementData[this.head];
    }

    public final E last() {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException("ArrayDeque is empty.");
        }
        return (E) this.elementData[positiveMod(this.head + com.android.server.permission.jarjar.kotlin.collections.CollectionsKt.getLastIndex(this))];
    }

    public final E lastOrNull() {
        if (isEmpty()) {
            return null;
        }
        return (E) this.elementData[positiveMod(this.head + com.android.server.permission.jarjar.kotlin.collections.CollectionsKt.getLastIndex(this))];
    }

    public final void addFirst(E e) {
        ensureCapacity(size() + 1);
        this.head = decremented(this.head);
        this.elementData[this.head] = e;
        this.size = size() + 1;
    }

    public final void addLast(E e) {
        ensureCapacity(size() + 1);
        this.elementData[positiveMod(this.head + size())] = e;
        this.size = size() + 1;
    }

    public final E removeFirst() {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException("ArrayDeque is empty.");
        }
        E e = (E) this.elementData[this.head];
        this.elementData[this.head] = null;
        this.head = incremented(this.head);
        this.size = size() - 1;
        return e;
    }

    public final E removeFirstOrNull() {
        if (isEmpty()) {
            return null;
        }
        return removeFirst();
    }

    public final E removeLast() {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException("ArrayDeque is empty.");
        }
        int iPositiveMod = positiveMod(this.head + com.android.server.permission.jarjar.kotlin.collections.CollectionsKt.getLastIndex(this));
        E e = (E) this.elementData[iPositiveMod];
        this.elementData[iPositiveMod] = null;
        this.size = size() - 1;
        return e;
    }

    public final E removeLastOrNull() {
        if (isEmpty()) {
            return null;
        }
        return removeLast();
    }

    @Override // java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.util.List
    public boolean add(E e) {
        addLast(e);
        return true;
    }

    @Override // com.android.server.permission.jarjar.kotlin.collections.AbstractMutableList, java.util.AbstractList, java.util.List
    public void add(int index, E e) {
        com.android.server.permission.jarjar.kotlin.collections.AbstractList.Companion.checkPositionIndex$kotlin_stdlib(index, size());
        if (index == size()) {
            addLast(e);
            return;
        }
        if (index == 0) {
            addFirst(e);
            return;
        }
        ensureCapacity(size() + 1);
        int internalIndex = positiveMod(this.head + index);
        if (index < ((size() + 1) >> 1)) {
            int decrementedInternalIndex = decremented(internalIndex);
            int decrementedHead = decremented(this.head);
            if (decrementedInternalIndex >= this.head) {
                this.elementData[decrementedHead] = this.elementData[this.head];
                com.android.server.permission.jarjar.kotlin.collections.ArraysKt.copyInto(this.elementData, this.elementData, this.head, this.head + 1, decrementedInternalIndex + 1);
            } else {
                com.android.server.permission.jarjar.kotlin.collections.ArraysKt.copyInto(this.elementData, this.elementData, this.head - 1, this.head, this.elementData.length);
                this.elementData[this.elementData.length - 1] = this.elementData[0];
                com.android.server.permission.jarjar.kotlin.collections.ArraysKt.copyInto(this.elementData, this.elementData, 0, 1, decrementedInternalIndex + 1);
            }
            this.elementData[decrementedInternalIndex] = e;
            this.head = decrementedHead;
        } else {
            int tail = positiveMod(this.head + size());
            if (internalIndex < tail) {
                com.android.server.permission.jarjar.kotlin.collections.ArraysKt.copyInto(this.elementData, this.elementData, internalIndex + 1, internalIndex, tail);
            } else {
                com.android.server.permission.jarjar.kotlin.collections.ArraysKt.copyInto(this.elementData, this.elementData, 1, 0, tail);
                this.elementData[0] = this.elementData[this.elementData.length - 1];
                com.android.server.permission.jarjar.kotlin.collections.ArraysKt.copyInto(this.elementData, this.elementData, internalIndex + 1, internalIndex, this.elementData.length - 1);
            }
            this.elementData[internalIndex] = e;
        }
        this.size = size() + 1;
    }

    private final void copyCollectionElements(int internalIndex, java.util.Collection<? extends E> collection) {
        java.util.Iterator<? extends E> it = collection.iterator();
        int length = this.elementData.length;
        for (int index = internalIndex; index < length && it.hasNext(); index++) {
            this.elementData[index] = it.next();
        }
        int i = this.head;
        for (int index2 = 0; index2 < i && it.hasNext(); index2++) {
            this.elementData[index2] = it.next();
        }
        int index3 = size();
        this.size = index3 + collection.size();
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public boolean addAll(java.util.Collection<? extends E> collection) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(collection, "elements");
        if (collection.isEmpty()) {
            return false;
        }
        ensureCapacity(size() + collection.size());
        copyCollectionElements(positiveMod(this.head + size()), collection);
        return true;
    }

    @Override // java.util.AbstractList, java.util.List
    public boolean addAll(int index, java.util.Collection<? extends E> collection) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(collection, "elements");
        com.android.server.permission.jarjar.kotlin.collections.AbstractList.Companion.checkPositionIndex$kotlin_stdlib(index, size());
        if (collection.isEmpty()) {
            return false;
        }
        if (index == size()) {
            return addAll(collection);
        }
        ensureCapacity(size() + collection.size());
        int tail = positiveMod(this.head + size());
        int internalIndex = positiveMod(this.head + index);
        int elementsSize = collection.size();
        if (index < ((size() + 1) >> 1)) {
            int shiftedHead = this.head - elementsSize;
            if (internalIndex >= this.head) {
                if (shiftedHead >= 0) {
                    com.android.server.permission.jarjar.kotlin.collections.ArraysKt.copyInto(this.elementData, this.elementData, shiftedHead, this.head, internalIndex);
                } else {
                    shiftedHead += this.elementData.length;
                    int elementsToShift = internalIndex - this.head;
                    int shiftToBack = this.elementData.length - shiftedHead;
                    if (shiftToBack >= elementsToShift) {
                        com.android.server.permission.jarjar.kotlin.collections.ArraysKt.copyInto(this.elementData, this.elementData, shiftedHead, this.head, internalIndex);
                    } else {
                        com.android.server.permission.jarjar.kotlin.collections.ArraysKt.copyInto(this.elementData, this.elementData, shiftedHead, this.head, this.head + shiftToBack);
                        com.android.server.permission.jarjar.kotlin.collections.ArraysKt.copyInto(this.elementData, this.elementData, 0, this.head + shiftToBack, internalIndex);
                    }
                }
            } else {
                com.android.server.permission.jarjar.kotlin.collections.ArraysKt.copyInto(this.elementData, this.elementData, shiftedHead, this.head, this.elementData.length);
                if (elementsSize >= internalIndex) {
                    com.android.server.permission.jarjar.kotlin.collections.ArraysKt.copyInto(this.elementData, this.elementData, this.elementData.length - elementsSize, 0, internalIndex);
                } else {
                    com.android.server.permission.jarjar.kotlin.collections.ArraysKt.copyInto(this.elementData, this.elementData, this.elementData.length - elementsSize, 0, elementsSize);
                    com.android.server.permission.jarjar.kotlin.collections.ArraysKt.copyInto(this.elementData, this.elementData, 0, elementsSize, internalIndex);
                }
            }
            this.head = shiftedHead;
            copyCollectionElements(negativeMod(internalIndex - elementsSize), collection);
        } else {
            int shiftedInternalIndex = internalIndex + elementsSize;
            if (internalIndex < tail) {
                if (tail + elementsSize <= this.elementData.length) {
                    com.android.server.permission.jarjar.kotlin.collections.ArraysKt.copyInto(this.elementData, this.elementData, shiftedInternalIndex, internalIndex, tail);
                } else if (shiftedInternalIndex >= this.elementData.length) {
                    com.android.server.permission.jarjar.kotlin.collections.ArraysKt.copyInto(this.elementData, this.elementData, shiftedInternalIndex - this.elementData.length, internalIndex, tail);
                } else {
                    int shiftToFront = (tail + elementsSize) - this.elementData.length;
                    com.android.server.permission.jarjar.kotlin.collections.ArraysKt.copyInto(this.elementData, this.elementData, 0, tail - shiftToFront, tail);
                    com.android.server.permission.jarjar.kotlin.collections.ArraysKt.copyInto(this.elementData, this.elementData, shiftedInternalIndex, internalIndex, tail - shiftToFront);
                }
            } else {
                com.android.server.permission.jarjar.kotlin.collections.ArraysKt.copyInto(this.elementData, this.elementData, elementsSize, 0, tail);
                if (shiftedInternalIndex >= this.elementData.length) {
                    com.android.server.permission.jarjar.kotlin.collections.ArraysKt.copyInto(this.elementData, this.elementData, shiftedInternalIndex - this.elementData.length, internalIndex, this.elementData.length);
                } else {
                    com.android.server.permission.jarjar.kotlin.collections.ArraysKt.copyInto(this.elementData, this.elementData, 0, this.elementData.length - elementsSize, this.elementData.length);
                    com.android.server.permission.jarjar.kotlin.collections.ArraysKt.copyInto(this.elementData, this.elementData, shiftedInternalIndex, internalIndex, this.elementData.length - elementsSize);
                }
            }
            copyCollectionElements(internalIndex, collection);
        }
        return true;
    }

    @Override // java.util.AbstractList, java.util.List
    public E get(int i) {
        com.android.server.permission.jarjar.kotlin.collections.AbstractList.Companion.checkElementIndex$kotlin_stdlib(i, size());
        return (E) this.elementData[positiveMod(this.head + i)];
    }

    @Override // com.android.server.permission.jarjar.kotlin.collections.AbstractMutableList, java.util.AbstractList, java.util.List
    public E set(int i, E e) {
        com.android.server.permission.jarjar.kotlin.collections.AbstractList.Companion.checkElementIndex$kotlin_stdlib(i, size());
        int iPositiveMod = positiveMod(this.head + i);
        E e2 = (E) this.elementData[iPositiveMod];
        this.elementData[iPositiveMod] = e;
        return e2;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public boolean contains(java.lang.Object element) {
        return indexOf(element) != -1;
    }

    @Override // java.util.AbstractList, java.util.List
    public int indexOf(java.lang.Object element) {
        int tail = positiveMod(this.head + size());
        if (this.head < tail) {
            for (int index = this.head; index < tail; index++) {
                if (com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.areEqual(element, this.elementData[index])) {
                    return index - this.head;
                }
            }
            return -1;
        }
        int index2 = this.head;
        if (index2 >= tail) {
            int length = this.elementData.length;
            for (int index3 = this.head; index3 < length; index3++) {
                if (com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.areEqual(element, this.elementData[index3])) {
                    return index3 - this.head;
                }
            }
            for (int index4 = 0; index4 < tail; index4++) {
                if (com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.areEqual(element, this.elementData[index4])) {
                    return (this.elementData.length + index4) - this.head;
                }
            }
            return -1;
        }
        return -1;
    }

    @Override // java.util.AbstractList, java.util.List
    public int lastIndexOf(java.lang.Object element) {
        int tail = positiveMod(this.head + size());
        if (this.head < tail) {
            int index = tail - 1;
            int i = this.head;
            if (i <= index) {
                while (!com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.areEqual(element, this.elementData[index])) {
                    if (index != i) {
                        index--;
                    }
                }
                return index - this.head;
            }
        } else {
            int index2 = this.head;
            if (index2 > tail) {
                for (int index3 = tail - 1; -1 < index3; index3--) {
                    if (com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.areEqual(element, this.elementData[index3])) {
                        return (this.elementData.length + index3) - this.head;
                    }
                }
                int index4 = com.android.server.permission.jarjar.kotlin.collections.ArraysKt.getLastIndex(this.elementData);
                int i2 = this.head;
                if (i2 <= index4) {
                    while (!com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.areEqual(element, this.elementData[index4])) {
                        if (index4 != i2) {
                            index4--;
                        }
                    }
                    return index4 - this.head;
                }
            }
        }
        return -1;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public boolean remove(java.lang.Object element) {
        int index = indexOf(element);
        if (index == -1) {
            return false;
        }
        remove(index);
        return true;
    }

    @Override // com.android.server.permission.jarjar.kotlin.collections.AbstractMutableList
    public E removeAt(int i) {
        com.android.server.permission.jarjar.kotlin.collections.AbstractList.Companion.checkElementIndex$kotlin_stdlib(i, size());
        if (i == com.android.server.permission.jarjar.kotlin.collections.CollectionsKt.getLastIndex(this)) {
            return removeLast();
        }
        if (i == 0) {
            return removeFirst();
        }
        int iPositiveMod = positiveMod(this.head + i);
        E e = (E) this.elementData[iPositiveMod];
        if (i < (size() >> 1)) {
            if (iPositiveMod >= this.head) {
                com.android.server.permission.jarjar.kotlin.collections.ArraysKt.copyInto(this.elementData, this.elementData, this.head + 1, this.head, iPositiveMod);
            } else {
                com.android.server.permission.jarjar.kotlin.collections.ArraysKt.copyInto(this.elementData, this.elementData, 1, 0, iPositiveMod);
                this.elementData[0] = this.elementData[this.elementData.length - 1];
                com.android.server.permission.jarjar.kotlin.collections.ArraysKt.copyInto(this.elementData, this.elementData, this.head + 1, this.head, this.elementData.length - 1);
            }
            this.elementData[this.head] = null;
            this.head = incremented(this.head);
        } else {
            int iPositiveMod2 = positiveMod(this.head + com.android.server.permission.jarjar.kotlin.collections.CollectionsKt.getLastIndex(this));
            if (iPositiveMod <= iPositiveMod2) {
                com.android.server.permission.jarjar.kotlin.collections.ArraysKt.copyInto(this.elementData, this.elementData, iPositiveMod, iPositiveMod + 1, iPositiveMod2 + 1);
            } else {
                com.android.server.permission.jarjar.kotlin.collections.ArraysKt.copyInto(this.elementData, this.elementData, iPositiveMod, iPositiveMod + 1, this.elementData.length);
                this.elementData[this.elementData.length - 1] = this.elementData[0];
                com.android.server.permission.jarjar.kotlin.collections.ArraysKt.copyInto(this.elementData, this.elementData, 0, 1, iPositiveMod2 + 1);
            }
            this.elementData[iPositiveMod2] = null;
        }
        this.size = size() - 1;
        return e;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public boolean removeAll(java.util.Collection<? extends java.lang.Object> collection) {
        int newTail$iv;
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(collection, "elements");
        boolean modified$iv = false;
        if (!isEmpty()) {
            if (!(this.elementData.length == 0)) {
                int tail$iv = positiveMod(this.head + size());
                int newTail$iv2 = this.head;
                boolean modified$iv2 = false;
                if (this.head < tail$iv) {
                    for (int index$iv = this.head; index$iv < tail$iv; index$iv++) {
                        java.lang.Object element$iv = this.elementData[index$iv];
                        if (!collection.contains(element$iv)) {
                            this.elementData[newTail$iv2] = element$iv;
                            newTail$iv2++;
                        } else {
                            modified$iv2 = true;
                        }
                    }
                    com.android.server.permission.jarjar.kotlin.collections.ArraysKt.fill(this.elementData, (java.lang.Object) null, newTail$iv2, tail$iv);
                    newTail$iv = newTail$iv2;
                    modified$iv = modified$iv2;
                } else {
                    int length = this.elementData.length;
                    for (int index$iv2 = this.head; index$iv2 < length; index$iv2++) {
                        java.lang.Object element$iv2 = this.elementData[index$iv2];
                        this.elementData[index$iv2] = null;
                        if (!collection.contains(element$iv2)) {
                            this.elementData[newTail$iv2] = element$iv2;
                            newTail$iv2++;
                        } else {
                            modified$iv2 = true;
                        }
                    }
                    int newTail$iv3 = positiveMod(newTail$iv2);
                    for (int index$iv3 = 0; index$iv3 < tail$iv; index$iv3++) {
                        java.lang.Object element$iv3 = this.elementData[index$iv3];
                        this.elementData[index$iv3] = null;
                        if (!collection.contains(element$iv3)) {
                            this.elementData[newTail$iv3] = element$iv3;
                            newTail$iv3 = incremented(newTail$iv3);
                        } else {
                            modified$iv2 = true;
                        }
                    }
                    newTail$iv = newTail$iv3;
                    modified$iv = modified$iv2;
                }
                if (modified$iv) {
                    this.size = negativeMod(newTail$iv - this.head);
                }
            }
        }
        return modified$iv;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public boolean retainAll(java.util.Collection<? extends java.lang.Object> collection) {
        int newTail$iv;
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(collection, "elements");
        boolean modified$iv = false;
        if (!isEmpty()) {
            if (!(this.elementData.length == 0)) {
                int tail$iv = positiveMod(this.head + size());
                int newTail$iv2 = this.head;
                boolean modified$iv2 = false;
                if (this.head < tail$iv) {
                    for (int index$iv = this.head; index$iv < tail$iv; index$iv++) {
                        java.lang.Object element$iv = this.elementData[index$iv];
                        if (collection.contains(element$iv)) {
                            this.elementData[newTail$iv2] = element$iv;
                            newTail$iv2++;
                        } else {
                            modified$iv2 = true;
                        }
                    }
                    com.android.server.permission.jarjar.kotlin.collections.ArraysKt.fill(this.elementData, (java.lang.Object) null, newTail$iv2, tail$iv);
                    boolean z = modified$iv2;
                    newTail$iv = newTail$iv2;
                    modified$iv = z;
                } else {
                    int length = this.elementData.length;
                    for (int index$iv2 = this.head; index$iv2 < length; index$iv2++) {
                        java.lang.Object element$iv2 = this.elementData[index$iv2];
                        this.elementData[index$iv2] = null;
                        if (collection.contains(element$iv2)) {
                            this.elementData[newTail$iv2] = element$iv2;
                            newTail$iv2++;
                        } else {
                            modified$iv2 = true;
                        }
                    }
                    int newTail$iv3 = positiveMod(newTail$iv2);
                    for (int index$iv3 = 0; index$iv3 < tail$iv; index$iv3++) {
                        java.lang.Object element$iv3 = this.elementData[index$iv3];
                        this.elementData[index$iv3] = null;
                        if (collection.contains(element$iv3)) {
                            this.elementData[newTail$iv3] = element$iv3;
                            newTail$iv3 = incremented(newTail$iv3);
                        } else {
                            modified$iv2 = true;
                        }
                    }
                    boolean z2 = modified$iv2;
                    newTail$iv = newTail$iv3;
                    modified$iv = z2;
                }
                if (modified$iv) {
                    this.size = negativeMod(newTail$iv - this.head);
                }
            }
        }
        return modified$iv;
    }

    private final boolean filterInPlace(com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<? super E, java.lang.Boolean> function1) {
        if (!isEmpty()) {
            if (!(this.elementData.length == 0)) {
                int tail = positiveMod(this.head + size());
                int newTail = this.head;
                boolean modified = false;
                if (this.head < tail) {
                    for (int index = this.head; index < tail; index++) {
                        java.lang.Object element = this.elementData[index];
                        if (function1.invoke(element).booleanValue()) {
                            this.elementData[newTail] = element;
                            newTail++;
                        } else {
                            modified = true;
                        }
                    }
                    com.android.server.permission.jarjar.kotlin.collections.ArraysKt.fill(this.elementData, (java.lang.Object) null, newTail, tail);
                } else {
                    int length = this.elementData.length;
                    for (int index2 = this.head; index2 < length; index2++) {
                        java.lang.Object element2 = this.elementData[index2];
                        this.elementData[index2] = null;
                        if (function1.invoke(element2).booleanValue()) {
                            this.elementData[newTail] = element2;
                            newTail++;
                        } else {
                            modified = true;
                        }
                    }
                    newTail = positiveMod(newTail);
                    for (int index3 = 0; index3 < tail; index3++) {
                        java.lang.Object element3 = this.elementData[index3];
                        this.elementData[index3] = null;
                        if (function1.invoke(element3).booleanValue()) {
                            this.elementData[newTail] = element3;
                            newTail = incremented(newTail);
                        } else {
                            modified = true;
                        }
                    }
                }
                if (modified) {
                    this.size = negativeMod(newTail - this.head);
                }
                return modified;
            }
        }
        return false;
    }

    @Override // java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.util.List
    public void clear() {
        int tail = positiveMod(this.head + size());
        if (this.head < tail) {
            com.android.server.permission.jarjar.kotlin.collections.ArraysKt.fill(this.elementData, (java.lang.Object) null, this.head, tail);
        } else if (!isEmpty()) {
            com.android.server.permission.jarjar.kotlin.collections.ArraysKt.fill(this.elementData, (java.lang.Object) null, this.head, this.elementData.length);
            com.android.server.permission.jarjar.kotlin.collections.ArraysKt.fill(this.elementData, (java.lang.Object) null, 0, tail);
        }
        this.head = 0;
        this.size = 0;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public <T> T[] toArray(T[] tArr) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(tArr, "array");
        java.lang.Object[] objArrArrayOfNulls = tArr.length >= size() ? tArr : com.android.server.permission.jarjar.kotlin.collections.ArraysKt.arrayOfNulls(tArr, size());
        int iPositiveMod = positiveMod(this.head + size());
        if (this.head < iPositiveMod) {
            com.android.server.permission.jarjar.kotlin.collections.ArraysKt.copyInto$default(this.elementData, objArrArrayOfNulls, 0, this.head, iPositiveMod, 2, (java.lang.Object) null);
        } else if (!isEmpty()) {
            com.android.server.permission.jarjar.kotlin.collections.ArraysKt.copyInto(this.elementData, objArrArrayOfNulls, 0, this.head, this.elementData.length);
            com.android.server.permission.jarjar.kotlin.collections.ArraysKt.copyInto(this.elementData, objArrArrayOfNulls, this.elementData.length - this.head, 0, iPositiveMod);
        }
        return (T[]) com.android.server.permission.jarjar.kotlin.collections.CollectionsKt.terminateCollectionToArray(size(), objArrArrayOfNulls);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public java.lang.Object[] toArray() {
        return toArray(new java.lang.Object[size()]);
    }

    public final <T> T[] testToArray$kotlin_stdlib(T[] tArr) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(tArr, "array");
        return (T[]) toArray(tArr);
    }

    public final java.lang.Object[] testToArray$kotlin_stdlib() {
        return toArray();
    }

    /* JADX INFO: compiled from: ArrayDeque.kt */
    @com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0011\n\u0002\b\u0002\b\u0080\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u0018\u0010\u0005\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00010\u0006X\u0082\u0004¢\u0006\u0004\n\u0002\u0010\u0007¨\u0006\b"}, d2 = {"Lkotlin/collections/ArrayDeque$Companion;", "", "()V", "defaultMinCapacity", "", "emptyElementData", "", "[Ljava/lang/Object;", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public static final class Companion {
        public /* synthetic */ Companion(com.android.server.permission.jarjar.kotlin.jvm.internal.DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    public final void internalStructure$kotlin_stdlib(com.android.server.permission.jarjar.kotlin.jvm.functions.Function2<? super java.lang.Integer, ? super java.lang.Object[], com.android.server.permission.jarjar.kotlin.Unit> function2) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function2, com.android.server.wm.ActivityTaskManagerInternal.ASSIST_KEY_STRUCTURE);
        int tail = positiveMod(this.head + size());
        int head = (isEmpty() || this.head < tail) ? this.head : this.head - this.elementData.length;
        function2.invoke(java.lang.Integer.valueOf(head), toArray());
    }
}
