package com.android.server.permission.jarjar.kotlin;

/* JADX INFO: compiled from: UIntArray.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.jvm.JvmInline
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000F\n\u0002\u0018\u0002\n\u0002\u0010\u001e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u0015\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0002\b\t\n\u0002\u0010\u0000\n\u0002\b\f\n\u0002\u0010(\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0002\b\u0004\b\u0087@\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001:\u00012B\u0011\b\u0016\u0012\u0006\u0010\u0003\u001a\u00020\u0004¢\u0006\u0004\b\u0005\u0010\u0006B\u0011\b\u0001\u0012\u0006\u0010\u0007\u001a\u00020\b¢\u0006\u0004\b\u0005\u0010\tJ\u0018\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0002H\u0096\u0002¢\u0006\u0004\b\u0011\u0010\u0012J\u001d\u0010\u0013\u001a\u00020\u000f2\f\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00020\u0001H\u0016¢\u0006\u0004\b\u0015\u0010\u0016J\u001a\u0010\u0017\u001a\u00020\u000f2\b\u0010\u0018\u001a\u0004\u0018\u00010\u0019HÖ\u0003¢\u0006\u0004\b\u001a\u0010\u001bJ\u001b\u0010\u001c\u001a\u00020\u00022\u0006\u0010\u001d\u001a\u00020\u0004H\u0086\u0002ø\u0001\u0000¢\u0006\u0004\b\u001e\u0010\u001fJ\u0010\u0010 \u001a\u00020\u0004HÖ\u0001¢\u0006\u0004\b!\u0010\u000bJ\u000f\u0010\"\u001a\u00020\u000fH\u0016¢\u0006\u0004\b#\u0010$J\u0016\u0010%\u001a\b\u0012\u0004\u0012\u00020\u00020&H\u0096\u0002¢\u0006\u0004\b'\u0010(J \u0010)\u001a\u00020*2\u0006\u0010\u001d\u001a\u00020\u00042\u0006\u0010+\u001a\u00020\u0002H\u0086\u0002¢\u0006\u0004\b,\u0010-J\u0010\u0010.\u001a\u00020/HÖ\u0001¢\u0006\u0004\b0\u00101R\u0014\u0010\u0003\u001a\u00020\u00048VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\n\u0010\u000bR\u0016\u0010\u0007\u001a\u00020\b8\u0000X\u0081\u0004¢\u0006\b\n\u0000\u0012\u0004\b\f\u0010\r\u0088\u0001\u0007\u0092\u0001\u00020\b\u0082\u0002\u0004\n\u0002\b!¨\u00063"}, d2 = {"Lkotlin/UIntArray;", "", "Lkotlin/UInt;", "size", "", "constructor-impl", "(I)[I", "storage", "", "([I)[I", "getSize-impl", "([I)I", "getStorage$annotations", "()V", "contains", "", "element", "contains-WZ4Q5Ns", "([II)Z", "containsAll", "elements", "containsAll-impl", "([ILjava/util/Collection;)Z", "equals", "other", "", "equals-impl", "([ILjava/lang/Object;)Z", "get", com.android.server.am.AssistDataRequester.KEY_RECEIVER_EXTRA_INDEX, "get-pVg5ArA", "([II)I", com.android.server.oplus.osense.OsenseConstants.KEY_INTEGER_HASH, "hashCode-impl", "isEmpty", "isEmpty-impl", "([I)Z", "iterator", "", "iterator-impl", "([I)Ljava/util/Iterator;", "set", "", "value", "set-VXSXFK8", "([III)V", "toString", "", "toString-impl", "([I)Ljava/lang/String;", "Iterator", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class UIntArray implements java.util.Collection<com.android.server.permission.jarjar.kotlin.UInt>, com.android.server.permission.jarjar.kotlin.jvm.internal.markers.KMappedMarker {
    private final int[] storage;

    /* JADX INFO: renamed from: box-impl, reason: not valid java name */
    public static final /* synthetic */ com.android.server.permission.jarjar.kotlin.UIntArray m6237boximpl(int[] iArr) {
        return new com.android.server.permission.jarjar.kotlin.UIntArray(iArr);
    }

    /* JADX INFO: renamed from: constructor-impl, reason: not valid java name */
    public static int[] m6239constructorimpl(int[] iArr) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(iArr, "storage");
        return iArr;
    }

    /* JADX INFO: renamed from: equals-impl, reason: not valid java name */
    public static boolean m6242equalsimpl(int[] iArr, java.lang.Object obj) {
        return (obj instanceof com.android.server.permission.jarjar.kotlin.UIntArray) && com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.areEqual(iArr, ((com.android.server.permission.jarjar.kotlin.UIntArray) obj).m6253unboximpl());
    }

    /* JADX INFO: renamed from: equals-impl0, reason: not valid java name */
    public static final boolean m6243equalsimpl0(int[] iArr, int[] iArr2) {
        return com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.areEqual(iArr, iArr2);
    }

    public static /* synthetic */ void getStorage$annotations() {
    }

    /* JADX INFO: renamed from: hashCode-impl, reason: not valid java name */
    public static int m6246hashCodeimpl(int[] iArr) {
        return java.util.Arrays.hashCode(iArr);
    }

    /* JADX INFO: renamed from: toString-impl, reason: not valid java name */
    public static java.lang.String m6250toStringimpl(int[] iArr) {
        return "UIntArray(storage=" + java.util.Arrays.toString(iArr) + ')';
    }

    @Override // java.util.Collection
    public /* bridge */ /* synthetic */ boolean add(com.android.server.permission.jarjar.kotlin.UInt uInt) {
        throw new java.lang.UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    /* JADX INFO: renamed from: add-WZ4Q5Ns, reason: not valid java name */
    public boolean m6251addWZ4Q5Ns(int i) {
        throw new java.lang.UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override // java.util.Collection
    public boolean addAll(java.util.Collection<? extends com.android.server.permission.jarjar.kotlin.UInt> collection) {
        throw new java.lang.UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override // java.util.Collection
    public void clear() {
        throw new java.lang.UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override // java.util.Collection
    public boolean equals(java.lang.Object obj) {
        return m6242equalsimpl(this.storage, obj);
    }

    @Override // java.util.Collection
    public int hashCode() {
        return m6246hashCodeimpl(this.storage);
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
        return com.android.server.permission.jarjar.kotlin.jvm.internal.CollectionToArray.toArray(this);
    }

    @Override // java.util.Collection
    public <T> T[] toArray(T[] tArr) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(tArr, "array");
        return (T[]) com.android.server.permission.jarjar.kotlin.jvm.internal.CollectionToArray.toArray(this, tArr);
    }

    public java.lang.String toString() {
        return m6250toStringimpl(this.storage);
    }

    /* JADX INFO: renamed from: unbox-impl, reason: not valid java name */
    public final /* synthetic */ int[] m6253unboximpl() {
        return this.storage;
    }

    @Override // java.util.Collection
    public final /* bridge */ boolean contains(java.lang.Object element) {
        if (element instanceof com.android.server.permission.jarjar.kotlin.UInt) {
            return m6252containsWZ4Q5Ns(((com.android.server.permission.jarjar.kotlin.UInt) element).m6236unboximpl());
        }
        return false;
    }

    private /* synthetic */ UIntArray(int[] storage) {
        this.storage = storage;
    }

    /* JADX INFO: renamed from: constructor-impl, reason: not valid java name */
    public static int[] m6238constructorimpl(int size) {
        return m6239constructorimpl(new int[size]);
    }

    /* JADX INFO: renamed from: get-pVg5ArA, reason: not valid java name */
    public static final int m6244getpVg5ArA(int[] arg0, int index) {
        return com.android.server.permission.jarjar.kotlin.UInt.m6184constructorimpl(arg0[index]);
    }

    /* JADX INFO: renamed from: set-VXSXFK8, reason: not valid java name */
    public static final void m6249setVXSXFK8(int[] arg0, int index, int value) {
        arg0[index] = value;
    }

    /* JADX INFO: renamed from: getSize-impl, reason: not valid java name */
    public static int m6245getSizeimpl(int[] arg0) {
        return arg0.length;
    }

    @Override // java.util.Collection
    /* JADX INFO: renamed from: getSize, reason: merged with bridge method [inline-methods] */
    public int size() {
        return m6245getSizeimpl(this.storage);
    }

    /* JADX INFO: renamed from: iterator-impl, reason: not valid java name */
    public static java.util.Iterator<com.android.server.permission.jarjar.kotlin.UInt> m6248iteratorimpl(int[] arg0) {
        return new com.android.server.permission.jarjar.kotlin.UIntArray.Iterator(arg0);
    }

    @Override // java.util.Collection, java.lang.Iterable
    public java.util.Iterator<com.android.server.permission.jarjar.kotlin.UInt> iterator() {
        return m6248iteratorimpl(this.storage);
    }

    /* JADX INFO: compiled from: UIntArray.kt */
    @com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010(\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0015\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\r\u0012\u0006\u0010\u0003\u001a\u00020\u0004¢\u0006\u0002\u0010\u0005J\t\u0010\b\u001a\u00020\tH\u0096\u0002J\u0013\u0010\n\u001a\u00020\u0002H\u0096\u0002ø\u0001\u0000¢\u0006\u0004\b\u000b\u0010\fR\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u000e¢\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b!¨\u0006\r"}, d2 = {"Lkotlin/UIntArray$Iterator;", "", "Lkotlin/UInt;", "array", "", "([I)V", com.android.server.am.AssistDataRequester.KEY_RECEIVER_EXTRA_INDEX, "", "hasNext", "", "next", "next-pVg5ArA", "()I", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
    private static final class Iterator implements java.util.Iterator<com.android.server.permission.jarjar.kotlin.UInt>, com.android.server.permission.jarjar.kotlin.jvm.internal.markers.KMappedMarker {
        private final int[] array;
        private int index;

        @Override // java.util.Iterator
        public void remove() {
            throw new java.lang.UnsupportedOperationException("Operation is not supported for read-only collection");
        }

        public Iterator(int[] array) {
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(array, "array");
            this.array = array;
        }

        @Override // java.util.Iterator
        public /* bridge */ /* synthetic */ com.android.server.permission.jarjar.kotlin.UInt next() {
            return com.android.server.permission.jarjar.kotlin.UInt.m6178boximpl(m6254nextpVg5ArA());
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.index < this.array.length;
        }

        /* JADX INFO: renamed from: next-pVg5ArA, reason: not valid java name */
        public int m6254nextpVg5ArA() {
            if (this.index >= this.array.length) {
                throw new java.util.NoSuchElementException(java.lang.String.valueOf(this.index));
            }
            int[] iArr = this.array;
            int i = this.index;
            this.index = i + 1;
            return com.android.server.permission.jarjar.kotlin.UInt.m6184constructorimpl(iArr[i]);
        }
    }

    /* JADX INFO: renamed from: contains-WZ4Q5Ns, reason: not valid java name */
    public boolean m6252containsWZ4Q5Ns(int element) {
        return m6240containsWZ4Q5Ns(this.storage, element);
    }

    /* JADX INFO: renamed from: contains-WZ4Q5Ns, reason: not valid java name */
    public static boolean m6240containsWZ4Q5Ns(int[] arg0, int element) {
        return com.android.server.permission.jarjar.kotlin.collections.ArraysKt.contains(arg0, element);
    }

    @Override // java.util.Collection
    public boolean containsAll(java.util.Collection<? extends java.lang.Object> collection) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(collection, "elements");
        return m6241containsAllimpl(this.storage, collection);
    }

    /* JADX INFO: renamed from: containsAll-impl, reason: not valid java name */
    public static boolean m6241containsAllimpl(int[] arg0, java.util.Collection<com.android.server.permission.jarjar.kotlin.UInt> collection) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(collection, "elements");
        java.util.Collection<com.android.server.permission.jarjar.kotlin.UInt> $this$all$iv = collection;
        if ($this$all$iv.isEmpty()) {
            return true;
        }
        for (java.lang.Object element$iv : $this$all$iv) {
            if (!((element$iv instanceof com.android.server.permission.jarjar.kotlin.UInt) && com.android.server.permission.jarjar.kotlin.collections.ArraysKt.contains(arg0, ((com.android.server.permission.jarjar.kotlin.UInt) element$iv).m6236unboximpl()))) {
                return false;
            }
        }
        return true;
    }

    /* JADX INFO: renamed from: isEmpty-impl, reason: not valid java name */
    public static boolean m6247isEmptyimpl(int[] arg0) {
        return arg0.length == 0;
    }

    @Override // java.util.Collection
    public boolean isEmpty() {
        return m6247isEmptyimpl(this.storage);
    }
}
