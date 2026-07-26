package kotlin.ranges;

/* JADX INFO: compiled from: Ranges.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000f\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0012\u0018\u0000*\u000e\b\u0000\u0010\u0001*\b\u0012\u0004\u0012\u0002H\u00010\u00022\b\u0012\u0004\u0012\u0002H\u00010\u0003B\u0015\u0012\u0006\u0010\u0004\u001a\u00028\u0000\u0012\u0006\u0010\u0005\u001a\u00028\u0000¢\u0006\u0002\u0010\u0006J\u0013\u0010\u000b\u001a\u00020\f2\b\u0010\r\u001a\u0004\u0018\u00010\u000eH\u0096\u0002J\b\u0010\u000f\u001a\u00020\u0010H\u0016J\b\u0010\u0011\u001a\u00020\u0012H\u0016R\u0016\u0010\u0005\u001a\u00028\u0000X\u0096\u0004¢\u0006\n\n\u0002\u0010\t\u001a\u0004\b\u0007\u0010\bR\u0016\u0010\u0004\u001a\u00028\u0000X\u0096\u0004¢\u0006\n\n\u0002\u0010\t\u001a\u0004\b\n\u0010\b¨\u0006\u0013"}, d2 = {"Lkotlin/ranges/ComparableRange;", "T", "", "Lkotlin/ranges/ClosedRange;", "start", "endInclusive", "(Ljava/lang/Comparable;Ljava/lang/Comparable;)V", "getEndInclusive", "()Ljava/lang/Comparable;", "Ljava/lang/Comparable;", "getStart", "equals", "", "other", "", com.android.server.oplus.osense.OsenseConstants.KEY_INTEGER_HASH, "", "toString", "", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
class ComparableRange<T extends java.lang.Comparable<? super T>> implements kotlin.ranges.ClosedRange<T> {
    private final T endInclusive;
    private final T start;

    public ComparableRange(T start, T endInclusive) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(start, "start");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(endInclusive, "endInclusive");
        this.start = start;
        this.endInclusive = endInclusive;
    }

    @Override // kotlin.ranges.ClosedRange, kotlin.ranges.OpenEndRange
    public boolean contains(T t) {
        return kotlin.ranges.ClosedRange.DefaultImpls.contains(this, t);
    }

    @Override // kotlin.ranges.ClosedRange, kotlin.ranges.OpenEndRange
    public boolean isEmpty() {
        return kotlin.ranges.ClosedRange.DefaultImpls.isEmpty(this);
    }

    @Override // kotlin.ranges.ClosedRange, kotlin.ranges.OpenEndRange
    public T getStart() {
        return this.start;
    }

    @Override // kotlin.ranges.ClosedRange
    public T getEndInclusive() {
        return this.endInclusive;
    }

    public boolean equals(java.lang.Object other) {
        return (other instanceof kotlin.ranges.ComparableRange) && ((isEmpty() && ((kotlin.ranges.ComparableRange) other).isEmpty()) || (kotlin.jvm.internal.Intrinsics.areEqual(getStart(), ((kotlin.ranges.ComparableRange) other).getStart()) && kotlin.jvm.internal.Intrinsics.areEqual(getEndInclusive(), ((kotlin.ranges.ComparableRange) other).getEndInclusive())));
    }

    public int hashCode() {
        if (isEmpty()) {
            return -1;
        }
        return (getStart().hashCode() * 31) + getEndInclusive().hashCode();
    }

    public java.lang.String toString() {
        return getStart() + ".." + getEndInclusive();
    }
}
