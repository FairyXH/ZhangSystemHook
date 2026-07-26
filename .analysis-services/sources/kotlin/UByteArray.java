package kotlin;

/* JADX INFO: compiled from: UByteArray.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000F\n\u0002\u0018\u0002\n\u0002\u0010\u001e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u0012\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0002\b\t\n\u0002\u0010\u0000\n\u0002\b\f\n\u0002\u0010(\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0002\b\u0004\b\u0087@\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001:\u00012B\u0011\b\u0016\u0012\u0006\u0010\u0003\u001a\u00020\u0004¢\u0006\u0004\b\u0005\u0010\u0006B\u0011\b\u0001\u0012\u0006\u0010\u0007\u001a\u00020\b¢\u0006\u0004\b\u0005\u0010\tJ\u0018\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0002H\u0096\u0002¢\u0006\u0004\b\u0011\u0010\u0012J\u001d\u0010\u0013\u001a\u00020\u000f2\f\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00020\u0001H\u0016¢\u0006\u0004\b\u0015\u0010\u0016J\u001a\u0010\u0017\u001a\u00020\u000f2\b\u0010\u0018\u001a\u0004\u0018\u00010\u0019HÖ\u0003¢\u0006\u0004\b\u001a\u0010\u001bJ\u001b\u0010\u001c\u001a\u00020\u00022\u0006\u0010\u001d\u001a\u00020\u0004H\u0086\u0002ø\u0001\u0000¢\u0006\u0004\b\u001e\u0010\u001fJ\u0010\u0010 \u001a\u00020\u0004HÖ\u0001¢\u0006\u0004\b!\u0010\u000bJ\u000f\u0010\"\u001a\u00020\u000fH\u0016¢\u0006\u0004\b#\u0010$J\u0016\u0010%\u001a\b\u0012\u0004\u0012\u00020\u00020&H\u0096\u0002¢\u0006\u0004\b'\u0010(J \u0010)\u001a\u00020*2\u0006\u0010\u001d\u001a\u00020\u00042\u0006\u0010+\u001a\u00020\u0002H\u0086\u0002¢\u0006\u0004\b,\u0010-J\u0010\u0010.\u001a\u00020/HÖ\u0001¢\u0006\u0004\b0\u00101R\u0014\u0010\u0003\u001a\u00020\u00048VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\n\u0010\u000bR\u0016\u0010\u0007\u001a\u00020\b8\u0000X\u0081\u0004¢\u0006\b\n\u0000\u0012\u0004\b\f\u0010\r\u0088\u0001\u0007\u0092\u0001\u00020\b\u0082\u0002\u0004\n\u0002\b!¨\u00063"}, d2 = {"Lkotlin/UByteArray;", "", "Lkotlin/UByte;", "size", "", "constructor-impl", "(I)[B", "storage", "", "([B)[B", "getSize-impl", "([B)I", "getStorage$annotations", "()V", "contains", "", "element", "contains-7apg3OU", "([BB)Z", "containsAll", "elements", "containsAll-impl", "([BLjava/util/Collection;)Z", "equals", "other", "", "equals-impl", "([BLjava/lang/Object;)Z", "get", com.android.server.am.AssistDataRequester.KEY_RECEIVER_EXTRA_INDEX, "get-w2LRezQ", "([BI)B", com.android.server.oplus.osense.OsenseConstants.KEY_INTEGER_HASH, "hashCode-impl", "isEmpty", "isEmpty-impl", "([B)Z", "iterator", "", "iterator-impl", "([B)Ljava/util/Iterator;", "set", "", "value", "set-VurrAj0", "([BIB)V", "toString", "", "toString-impl", "([B)Ljava/lang/String;", "Iterator", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
@kotlin.jvm.JvmInline
public final class UByteArray implements java.util.Collection<kotlin.UByte>, kotlin.jvm.internal.markers.KMappedMarker {
    private final byte[] storage;

    /* JADX INFO: renamed from: box-impl, reason: not valid java name */
    public static final /* synthetic */ kotlin.UByteArray m11376boximpl(byte[] bArr) {
        return new kotlin.UByteArray(bArr);
    }

    /* JADX INFO: renamed from: constructor-impl, reason: not valid java name */
    public static byte[] m11378constructorimpl(byte[] storage) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(storage, "storage");
        return storage;
    }

    /* JADX INFO: renamed from: equals-impl, reason: not valid java name */
    public static boolean m11381equalsimpl(byte[] bArr, java.lang.Object obj) {
        return (obj instanceof kotlin.UByteArray) && kotlin.jvm.internal.Intrinsics.areEqual(bArr, ((kotlin.UByteArray) obj).getStorage());
    }

    /* JADX INFO: renamed from: equals-impl0, reason: not valid java name */
    public static final boolean m11382equalsimpl0(byte[] bArr, byte[] bArr2) {
        return kotlin.jvm.internal.Intrinsics.areEqual(bArr, bArr2);
    }

    public static /* synthetic */ void getStorage$annotations() {
    }

    /* JADX INFO: renamed from: hashCode-impl, reason: not valid java name */
    public static int m11385hashCodeimpl(byte[] bArr) {
        return java.util.Arrays.hashCode(bArr);
    }

    /* JADX INFO: renamed from: toString-impl, reason: not valid java name */
    public static java.lang.String m11389toStringimpl(byte[] bArr) {
        return "UByteArray(storage=" + java.util.Arrays.toString(bArr) + ')';
    }

    @Override // java.util.Collection
    public /* bridge */ /* synthetic */ boolean add(kotlin.UByte uByte) {
        throw new java.lang.UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    /* JADX INFO: renamed from: add-7apg3OU, reason: not valid java name */
    public boolean m11390add7apg3OU(byte b) {
        throw new java.lang.UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override // java.util.Collection
    public boolean addAll(java.util.Collection<? extends kotlin.UByte> collection) {
        throw new java.lang.UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override // java.util.Collection
    public void clear() {
        throw new java.lang.UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override // java.util.Collection
    public boolean equals(java.lang.Object obj) {
        return m11381equalsimpl(this.storage, obj);
    }

    @Override // java.util.Collection
    public int hashCode() {
        return m11385hashCodeimpl(this.storage);
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
    public java.lang.Object[] toArray() {
        return kotlin.jvm.internal.CollectionToArray.toArray(this);
    }

    @Override // java.util.Collection
    public <T> T[] toArray(T[] array) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(array, "array");
        return (T[]) kotlin.jvm.internal.CollectionToArray.toArray(this, array);
    }

    public java.lang.String toString() {
        return m11389toStringimpl(this.storage);
    }

    /* JADX INFO: renamed from: unbox-impl, reason: not valid java name and from getter */
    public final /* synthetic */ byte[] getStorage() {
        return this.storage;
    }

    @Override // java.util.Collection
    public final /* bridge */ boolean contains(java.lang.Object element) {
        if (element instanceof kotlin.UByte) {
            return m11391contains7apg3OU(((kotlin.UByte) element).getData());
        }
        return false;
    }

    private /* synthetic */ UByteArray(byte[] storage) {
        this.storage = storage;
    }

    /* JADX INFO: renamed from: constructor-impl, reason: not valid java name */
    public static byte[] m11377constructorimpl(int size) {
        return m11378constructorimpl(new byte[size]);
    }

    /* JADX INFO: renamed from: get-w2LRezQ, reason: not valid java name */
    public static final byte m11383getw2LRezQ(byte[] arg0, int index) {
        return kotlin.UByte.m11325constructorimpl(arg0[index]);
    }

    /* JADX INFO: renamed from: set-VurrAj0, reason: not valid java name */
    public static final void m11388setVurrAj0(byte[] arg0, int index, byte value) {
        arg0[index] = value;
    }

    /* JADX INFO: renamed from: getSize-impl, reason: not valid java name */
    public static int m11384getSizeimpl(byte[] arg0) {
        return arg0.length;
    }

    @Override // java.util.Collection
    /* JADX INFO: renamed from: getSize, reason: merged with bridge method [inline-methods] */
    public int size() {
        return m11384getSizeimpl(this.storage);
    }

    /* JADX INFO: renamed from: iterator-impl, reason: not valid java name */
    public static java.util.Iterator<kotlin.UByte> m11387iteratorimpl(byte[] arg0) {
        return new kotlin.UByteArray.Iterator(arg0);
    }

    @Override // java.util.Collection, java.lang.Iterable
    public java.util.Iterator<kotlin.UByte> iterator() {
        return m11387iteratorimpl(this.storage);
    }

    /* JADX INFO: compiled from: UByteArray.kt */
    @kotlin.Metadata(d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010(\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0012\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\r\u0012\u0006\u0010\u0003\u001a\u00020\u0004¢\u0006\u0002\u0010\u0005J\t\u0010\b\u001a\u00020\tH\u0096\u0002J\u0013\u0010\n\u001a\u00020\u0002H\u0096\u0002ø\u0001\u0000¢\u0006\u0004\b\u000b\u0010\fR\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u000e¢\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b!¨\u0006\r"}, d2 = {"Lkotlin/UByteArray$Iterator;", "", "Lkotlin/UByte;", "array", "", "([B)V", com.android.server.am.AssistDataRequester.KEY_RECEIVER_EXTRA_INDEX, "", "hasNext", "", "next", "next-w2LRezQ", "()B", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
    private static final class Iterator implements java.util.Iterator<kotlin.UByte>, kotlin.jvm.internal.markers.KMappedMarker {
        private final byte[] array;
        private int index;

        @Override // java.util.Iterator
        public void remove() {
            throw new java.lang.UnsupportedOperationException("Operation is not supported for read-only collection");
        }

        public Iterator(byte[] array) {
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(array, "array");
            this.array = array;
        }

        @Override // java.util.Iterator
        public /* bridge */ /* synthetic */ kotlin.UByte next() {
            return kotlin.UByte.m11319boximpl(m11393nextw2LRezQ());
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.index < this.array.length;
        }

        /* JADX INFO: renamed from: next-w2LRezQ, reason: not valid java name */
        public byte m11393nextw2LRezQ() {
            if (this.index >= this.array.length) {
                throw new java.util.NoSuchElementException(java.lang.String.valueOf(this.index));
            }
            byte[] bArr = this.array;
            int i = this.index;
            this.index = i + 1;
            return kotlin.UByte.m11325constructorimpl(bArr[i]);
        }
    }

    /* JADX INFO: renamed from: contains-7apg3OU, reason: not valid java name */
    public boolean m11391contains7apg3OU(byte element) {
        return m11379contains7apg3OU(this.storage, element);
    }

    /* JADX INFO: renamed from: contains-7apg3OU, reason: not valid java name */
    public static boolean m11379contains7apg3OU(byte[] arg0, byte element) {
        return kotlin.collections.ArraysKt.contains(arg0, element);
    }

    @Override // java.util.Collection
    public boolean containsAll(java.util.Collection<? extends java.lang.Object> elements) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(elements, "elements");
        return m11380containsAllimpl(this.storage, elements);
    }

    /* JADX INFO: renamed from: containsAll-impl, reason: not valid java name */
    public static boolean m11380containsAllimpl(byte[] arg0, java.util.Collection<kotlin.UByte> elements) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(elements, "elements");
        java.util.Collection<kotlin.UByte> $this$all$iv = elements;
        if ($this$all$iv.isEmpty()) {
            return true;
        }
        for (java.lang.Object element$iv : $this$all$iv) {
            if (!((element$iv instanceof kotlin.UByte) && kotlin.collections.ArraysKt.contains(arg0, ((kotlin.UByte) element$iv).getData()))) {
                return false;
            }
        }
        return true;
    }

    /* JADX INFO: renamed from: isEmpty-impl, reason: not valid java name */
    public static boolean m11386isEmptyimpl(byte[] arg0) {
        return arg0.length == 0;
    }

    @Override // java.util.Collection
    public boolean isEmpty() {
        return m11386isEmptyimpl(this.storage);
    }
}
