package com.android.server.permission.jarjar.kotlin.collections;

/* JADX INFO: compiled from: SlidingWindow.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0002\u0010\u0000\n\u0002\b\t\n\u0002\u0010\u0002\n\u0002\b\b\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010(\n\u0002\b\b\b\u0002\u0018\u0000*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u00022\u00060\u0003j\u0002`\u0004B\u000f\b\u0016\u0012\u0006\u0010\u0005\u001a\u00020\u0006¢\u0006\u0002\u0010\u0007B\u001d\u0012\u000e\u0010\b\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\n0\t\u0012\u0006\u0010\u000b\u001a\u00020\u0006¢\u0006\u0002\u0010\fJ\u0013\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00028\u0000¢\u0006\u0002\u0010\u0016J\u0014\u0010\u0017\u001a\b\u0012\u0004\u0012\u00028\u00000\u00002\u0006\u0010\u0018\u001a\u00020\u0006J\u0016\u0010\u0019\u001a\u00028\u00002\u0006\u0010\u001a\u001a\u00020\u0006H\u0096\u0002¢\u0006\u0002\u0010\u001bJ\u0006\u0010\u001c\u001a\u00020\u001dJ\u000f\u0010\u001e\u001a\b\u0012\u0004\u0012\u00028\u00000\u001fH\u0096\u0002J\u000e\u0010 \u001a\u00020\u00142\u0006\u0010!\u001a\u00020\u0006J\u0015\u0010\"\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\n0\tH\u0014¢\u0006\u0002\u0010#J'\u0010\"\u001a\b\u0012\u0004\u0012\u0002H\u00010\t\"\u0004\b\u0001\u0010\u00012\f\u0010$\u001a\b\u0012\u0004\u0012\u0002H\u00010\tH\u0014¢\u0006\u0002\u0010%J\u0015\u0010&\u001a\u00020\u0006*\u00020\u00062\u0006\u0010!\u001a\u00020\u0006H\u0082\bR\u0018\u0010\b\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\n0\tX\u0082\u0004¢\u0006\u0004\n\u0002\u0010\rR\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004¢\u0006\u0002\n\u0000R\u001e\u0010\u000f\u001a\u00020\u00062\u0006\u0010\u000e\u001a\u00020\u0006@RX\u0096\u000e¢\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u000e\u0010\u0012\u001a\u00020\u0006X\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006'"}, d2 = {"Lkotlin/collections/RingBuffer;", "T", "Lkotlin/collections/AbstractList;", "Ljava/util/RandomAccess;", "Lkotlin/collections/RandomAccess;", "capacity", "", "(I)V", "buffer", "", "", "filledSize", "([Ljava/lang/Object;I)V", "[Ljava/lang/Object;", "<set-?>", "size", "getSize", "()I", "startIndex", "add", "", "element", "(Ljava/lang/Object;)V", "expanded", "maxCapacity", "get", com.android.server.am.AssistDataRequester.KEY_RECEIVER_EXTRA_INDEX, "(I)Ljava/lang/Object;", "isFull", "", "iterator", "", "removeFirst", "n", "toArray", "()[Ljava/lang/Object;", "array", "([Ljava/lang/Object;)[Ljava/lang/Object;", "forward", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
final class RingBuffer<T> extends com.android.server.permission.jarjar.kotlin.collections.AbstractList<T> implements java.util.RandomAccess {
    private final java.lang.Object[] buffer;
    private final int capacity;
    private int size;
    private int startIndex;

    public RingBuffer(java.lang.Object[] buffer, int filledSize) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(buffer, "buffer");
        this.buffer = buffer;
        if (!(filledSize >= 0)) {
            throw new java.lang.IllegalArgumentException(("ring buffer filled size should not be negative but it is " + filledSize).toString());
        }
        if (!(filledSize <= this.buffer.length)) {
            throw new java.lang.IllegalArgumentException(("ring buffer filled size: " + filledSize + " cannot be larger than the buffer size: " + this.buffer.length).toString());
        }
        this.capacity = this.buffer.length;
        this.size = filledSize;
    }

    public RingBuffer(int capacity) {
        this(new java.lang.Object[capacity], 0);
    }

    @Override // com.android.server.permission.jarjar.kotlin.collections.AbstractList, com.android.server.permission.jarjar.kotlin.collections.AbstractCollection
    public int getSize() {
        return this.size;
    }

    @Override // com.android.server.permission.jarjar.kotlin.collections.AbstractList, java.util.List
    public T get(int i) {
        com.android.server.permission.jarjar.kotlin.collections.AbstractList.Companion.checkElementIndex$kotlin_stdlib(i, size());
        return (T) this.buffer[(this.startIndex + i) % this.capacity];
    }

    public final boolean isFull() {
        return size() == this.capacity;
    }

    @Override // com.android.server.permission.jarjar.kotlin.collections.AbstractList, com.android.server.permission.jarjar.kotlin.collections.AbstractCollection, java.util.Collection, java.lang.Iterable
    public java.util.Iterator<T> iterator() {
        return new com.android.server.permission.jarjar.kotlin.collections.AbstractIterator<T>(this) { // from class: com.android.server.permission.jarjar.kotlin.collections.RingBuffer.iterator.1
            private int count;
            private int index;
            final /* synthetic */ com.android.server.permission.jarjar.kotlin.collections.RingBuffer<T> this$0;

            {
                this.this$0 = this;
                this.count = this.size();
                this.index = ((com.android.server.permission.jarjar.kotlin.collections.RingBuffer) this).startIndex;
            }

            /* JADX WARN: Multi-variable type inference failed */
            @Override // com.android.server.permission.jarjar.kotlin.collections.AbstractIterator
            protected void computeNext() {
                if (this.count != 0) {
                    setNext(((com.android.server.permission.jarjar.kotlin.collections.RingBuffer) this.this$0).buffer[this.index]);
                    com.android.server.permission.jarjar.kotlin.collections.RingBuffer<T> ringBuffer = this.this$0;
                    int $this$forward$iv = this.index;
                    this.index = ($this$forward$iv + 1) % ((com.android.server.permission.jarjar.kotlin.collections.RingBuffer) ringBuffer).capacity;
                    this.count--;
                    return;
                }
                done();
            }
        };
    }

    @Override // com.android.server.permission.jarjar.kotlin.collections.AbstractCollection, java.util.Collection
    public <T> T[] toArray(T[] tArr) {
        java.lang.Object[] objArrCopyOf;
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(tArr, "array");
        if (tArr.length < size()) {
            objArrCopyOf = java.util.Arrays.copyOf(tArr, size());
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(objArrCopyOf, "copyOf(...)");
        } else {
            objArrCopyOf = tArr;
        }
        int size = size();
        int i = 0;
        for (int i2 = this.startIndex; i < size && i2 < this.capacity; i2++) {
            objArrCopyOf[i] = this.buffer[i2];
            i++;
        }
        int i3 = 0;
        while (i < size) {
            objArrCopyOf[i] = this.buffer[i3];
            i++;
            i3++;
        }
        return (T[]) com.android.server.permission.jarjar.kotlin.collections.CollectionsKt.terminateCollectionToArray(size, objArrCopyOf);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.android.server.permission.jarjar.kotlin.collections.AbstractCollection, java.util.Collection
    public java.lang.Object[] toArray() {
        return toArray(new java.lang.Object[size()]);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public final com.android.server.permission.jarjar.kotlin.collections.RingBuffer<T> expanded(int maxCapacity) {
        java.lang.Object[] newBuffer;
        int newCapacity = com.android.server.permission.jarjar.kotlin.ranges.RangesKt.coerceAtMost(this.capacity + (this.capacity >> 1) + 1, maxCapacity);
        if (this.startIndex == 0) {
            newBuffer = java.util.Arrays.copyOf(this.buffer, newCapacity);
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(newBuffer, "copyOf(...)");
        } else {
            newBuffer = toArray(new java.lang.Object[newCapacity]);
        }
        return new com.android.server.permission.jarjar.kotlin.collections.RingBuffer<>(newBuffer, size());
    }

    @Override // java.util.Collection, java.util.List
    public final void add(T t) {
        if (isFull()) {
            throw new java.lang.IllegalStateException("ring buffer is full");
        }
        java.lang.Object[] objArr = this.buffer;
        int $this$forward$iv = this.startIndex;
        int n$iv = size();
        objArr[($this$forward$iv + n$iv) % this.capacity] = t;
        this.size = size() + 1;
    }

    public final void removeFirst(int n) {
        if (!(n >= 0)) {
            throw new java.lang.IllegalArgumentException(("n shouldn't be negative but it is " + n).toString());
        }
        if (!(n <= size())) {
            throw new java.lang.IllegalArgumentException(("n shouldn't be greater than the buffer size: n = " + n + ", size = " + size()).toString());
        }
        if (n > 0) {
            int start = this.startIndex;
            int end = (start + n) % this.capacity;
            if (start <= end) {
                com.android.server.permission.jarjar.kotlin.collections.ArraysKt.fill(this.buffer, (java.lang.Object) null, start, end);
            } else {
                com.android.server.permission.jarjar.kotlin.collections.ArraysKt.fill(this.buffer, (java.lang.Object) null, start, this.capacity);
                com.android.server.permission.jarjar.kotlin.collections.ArraysKt.fill(this.buffer, (java.lang.Object) null, 0, end);
            }
            this.startIndex = end;
            this.size = size() - n;
        }
    }

    private final int forward(int $this$forward, int n) {
        return ($this$forward + n) % this.capacity;
    }
}
