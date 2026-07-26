package com.android.server.permission.jarjar.kotlin.ranges;

/* JADX INFO: compiled from: ULongRange.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010(\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0003\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u001d\u0012\u0006\u0010\u0003\u001a\u00020\u0002\u0012\u0006\u0010\u0004\u001a\u00020\u0002\u0012\u0006\u0010\u0005\u001a\u00020\u0006¢\u0006\u0002\u0010\u0007J\t\u0010\n\u001a\u00020\u000bH\u0096\u0002J\u0013\u0010\f\u001a\u00020\u0002H\u0096\u0002ø\u0001\u0000¢\u0006\u0004\b\r\u0010\u000eR\u0013\u0010\b\u001a\u00020\u0002X\u0082\u0004ø\u0001\u0000¢\u0006\u0004\n\u0002\u0010\tR\u000e\u0010\n\u001a\u00020\u000bX\u0082\u000e¢\u0006\u0002\n\u0000R\u0013\u0010\f\u001a\u00020\u0002X\u0082\u000eø\u0001\u0000¢\u0006\u0004\n\u0002\u0010\tR\u0013\u0010\u0005\u001a\u00020\u0002X\u0082\u0004ø\u0001\u0000¢\u0006\u0004\n\u0002\u0010\t\u0082\u0002\u0004\n\u0002\b!¨\u0006\u000f"}, d2 = {"Lkotlin/ranges/ULongProgressionIterator;", "", "Lkotlin/ULong;", "first", "last", "step", "", "(JJJLkotlin/jvm/internal/DefaultConstructorMarker;)V", "finalElement", "J", "hasNext", "", "next", "next-s-VKNKU", "()J", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
final class ULongProgressionIterator implements java.util.Iterator<com.android.server.permission.jarjar.kotlin.ULong>, com.android.server.permission.jarjar.kotlin.jvm.internal.markers.KMappedMarker {
    private final long finalElement;
    private boolean hasNext;
    private long next;
    private final long step;

    public /* synthetic */ ULongProgressionIterator(long j, long j2, long j3, com.android.server.permission.jarjar.kotlin.jvm.internal.DefaultConstructorMarker defaultConstructorMarker) {
        this(j, j2, j3);
    }

    @Override // java.util.Iterator
    public void remove() {
        throw new java.lang.UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    private ULongProgressionIterator(long first, long last, long step) {
        this.finalElement = last;
        boolean z = true;
        if (step <= 0 ? java.lang.Long.compareUnsigned(first, last) < 0 : java.lang.Long.compareUnsigned(first, last) > 0) {
            z = false;
        }
        this.hasNext = z;
        this.step = com.android.server.permission.jarjar.kotlin.ULong.m6263constructorimpl(step);
        this.next = this.hasNext ? first : this.finalElement;
    }

    @Override // java.util.Iterator
    public /* bridge */ /* synthetic */ com.android.server.permission.jarjar.kotlin.ULong next() {
        return com.android.server.permission.jarjar.kotlin.ULong.m6257boximpl(m7324nextsVKNKU());
    }

    @Override // java.util.Iterator
    public boolean hasNext() {
        return this.hasNext;
    }

    /* JADX INFO: renamed from: next-s-VKNKU, reason: not valid java name */
    public long m7324nextsVKNKU() {
        long value = this.next;
        if (value == this.finalElement) {
            if (!this.hasNext) {
                throw new java.util.NoSuchElementException();
            }
            this.hasNext = false;
        } else {
            this.next = com.android.server.permission.jarjar.kotlin.ULong.m6263constructorimpl(this.next + this.step);
        }
        return value;
    }
}
