package kotlin.ranges;

/* JADX INFO: compiled from: UIntRange.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010(\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0003\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u001d\u0012\u0006\u0010\u0003\u001a\u00020\u0002\u0012\u0006\u0010\u0004\u001a\u00020\u0002\u0012\u0006\u0010\u0005\u001a\u00020\u0006¢\u0006\u0002\u0010\u0007J\t\u0010\n\u001a\u00020\u000bH\u0096\u0002J\u0013\u0010\f\u001a\u00020\u0002H\u0096\u0002ø\u0001\u0000¢\u0006\u0004\b\r\u0010\u000eR\u0013\u0010\b\u001a\u00020\u0002X\u0082\u0004ø\u0001\u0000¢\u0006\u0004\n\u0002\u0010\tR\u000e\u0010\n\u001a\u00020\u000bX\u0082\u000e¢\u0006\u0002\n\u0000R\u0013\u0010\f\u001a\u00020\u0002X\u0082\u000eø\u0001\u0000¢\u0006\u0004\n\u0002\u0010\tR\u0013\u0010\u0005\u001a\u00020\u0002X\u0082\u0004ø\u0001\u0000¢\u0006\u0004\n\u0002\u0010\t\u0082\u0002\u0004\n\u0002\b!¨\u0006\u000f"}, d2 = {"Lkotlin/ranges/UIntProgressionIterator;", "", "Lkotlin/UInt;", "first", "last", "step", "", "(IIILkotlin/jvm/internal/DefaultConstructorMarker;)V", "finalElement", "I", "hasNext", "", "next", "next-pVg5ArA", "()I", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
final class UIntProgressionIterator implements java.util.Iterator<kotlin.UInt>, kotlin.jvm.internal.markers.KMappedMarker {
    private final int finalElement;
    private boolean hasNext;
    private int next;
    private final int step;

    public /* synthetic */ UIntProgressionIterator(int i, int i2, int i3, kotlin.jvm.internal.DefaultConstructorMarker defaultConstructorMarker) {
        this(i, i2, i3);
    }

    @Override // java.util.Iterator
    public void remove() {
        throw new java.lang.UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    private UIntProgressionIterator(int first, int last, int step) {
        this.finalElement = last;
        boolean z = true;
        int iCompareUnsigned = java.lang.Integer.compareUnsigned(first, last);
        if (step <= 0 ? iCompareUnsigned < 0 : iCompareUnsigned > 0) {
            z = false;
        }
        this.hasNext = z;
        this.step = kotlin.UInt.m11402constructorimpl(step);
        this.next = this.hasNext ? first : this.finalElement;
    }

    @Override // java.util.Iterator
    public /* bridge */ /* synthetic */ kotlin.UInt next() {
        return kotlin.UInt.m11396boximpl(m12533nextpVg5ArA());
    }

    @Override // java.util.Iterator
    public boolean hasNext() {
        return this.hasNext;
    }

    /* JADX INFO: renamed from: next-pVg5ArA, reason: not valid java name */
    public int m12533nextpVg5ArA() {
        int value = this.next;
        if (value == this.finalElement) {
            if (!this.hasNext) {
                throw new java.util.NoSuchElementException();
            }
            this.hasNext = false;
        } else {
            this.next = kotlin.UInt.m11402constructorimpl(this.next + this.step);
        }
        return value;
    }
}
