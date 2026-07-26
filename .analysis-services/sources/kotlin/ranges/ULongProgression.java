package kotlin.ranges;

/* JADX INFO: compiled from: ULongRange.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0010\u001c\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\t\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010(\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0017\u0018\u0000 \u001a2\b\u0012\u0004\u0012\u00020\u00020\u0001:\u0001\u001aB\u001f\b\u0000\u0012\u0006\u0010\u0003\u001a\u00020\u0002\u0012\u0006\u0010\u0004\u001a\u00020\u0002\u0012\u0006\u0010\u0005\u001a\u00020\u0006¢\u0006\u0002\u0010\u0007J\u0013\u0010\u000f\u001a\u00020\u00102\b\u0010\u0011\u001a\u0004\u0018\u00010\u0012H\u0096\u0002J\b\u0010\u0013\u001a\u00020\u0014H\u0016J\b\u0010\u0015\u001a\u00020\u0010H\u0016J\u000f\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00020\u0017H\u0086\u0002J\b\u0010\u0018\u001a\u00020\u0019H\u0016R\u0016\u0010\b\u001a\u00020\u0002ø\u0001\u0000¢\u0006\n\n\u0002\u0010\u000b\u001a\u0004\b\t\u0010\nR\u0016\u0010\f\u001a\u00020\u0002ø\u0001\u0000¢\u0006\n\n\u0002\u0010\u000b\u001a\u0004\b\r\u0010\nR\u0011\u0010\u0005\u001a\u00020\u0006¢\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\n\u0082\u0002\u0004\n\u0002\b!¨\u0006\u001b"}, d2 = {"Lkotlin/ranges/ULongProgression;", "", "Lkotlin/ULong;", "start", "endInclusive", "step", "", "(JJJLkotlin/jvm/internal/DefaultConstructorMarker;)V", "first", "getFirst-s-VKNKU", "()J", "J", "last", "getLast-s-VKNKU", "getStep", "equals", "", "other", "", com.android.server.oplus.osense.OsenseConstants.KEY_INTEGER_HASH, "", "isEmpty", "iterator", "", "toString", "", "Companion", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
public class ULongProgression implements java.lang.Iterable<kotlin.ULong>, kotlin.jvm.internal.markers.KMappedMarker {

    /* JADX INFO: renamed from: Companion, reason: from kotlin metadata */
    public static final kotlin.ranges.ULongProgression.Companion INSTANCE = new kotlin.ranges.ULongProgression.Companion(null);
    private final long first;
    private final long last;
    private final long step;

    public /* synthetic */ ULongProgression(long j, long j2, long j3, kotlin.jvm.internal.DefaultConstructorMarker defaultConstructorMarker) {
        this(j, j2, j3);
    }

    private ULongProgression(long start, long endInclusive, long step) {
        if (step == 0) {
            throw new java.lang.IllegalArgumentException("Step must be non-zero.");
        }
        if (step == Long.MIN_VALUE) {
            throw new java.lang.IllegalArgumentException("Step must be greater than Long.MIN_VALUE to avoid overflow on negation.");
        }
        this.first = start;
        this.last = kotlin.internal.UProgressionUtilKt.m12515getProgressionLastElement7ftBX0g(start, endInclusive, step);
        this.step = step;
    }

    /* JADX INFO: renamed from: getFirst-s-VKNKU, reason: not valid java name and from getter */
    public final long getFirst() {
        return this.first;
    }

    /* JADX INFO: renamed from: getLast-s-VKNKU, reason: not valid java name and from getter */
    public final long getLast() {
        return this.last;
    }

    public final long getStep() {
        return this.step;
    }

    @Override // java.lang.Iterable
    public final java.util.Iterator<kotlin.ULong> iterator() {
        return new kotlin.ranges.ULongProgressionIterator(this.first, this.last, this.step, null);
    }

    public boolean isEmpty() {
        long j = this.step;
        long j2 = this.first;
        long j3 = this.last;
        if (j > 0) {
            if (java.lang.Long.compareUnsigned(j2, j3) > 0) {
                return true;
            }
        } else if (java.lang.Long.compareUnsigned(j2, j3) < 0) {
            return true;
        }
        return false;
    }

    public boolean equals(java.lang.Object other) {
        return (other instanceof kotlin.ranges.ULongProgression) && ((isEmpty() && ((kotlin.ranges.ULongProgression) other).isEmpty()) || (this.first == ((kotlin.ranges.ULongProgression) other).first && this.last == ((kotlin.ranges.ULongProgression) other).last && this.step == ((kotlin.ranges.ULongProgression) other).step));
    }

    public int hashCode() {
        if (isEmpty()) {
            return -1;
        }
        return (((((int) kotlin.ULong.m11481constructorimpl(this.first ^ kotlin.ULong.m11481constructorimpl(this.first >>> 32))) * 31) + ((int) kotlin.ULong.m11481constructorimpl(this.last ^ kotlin.ULong.m11481constructorimpl(this.last >>> 32)))) * 31) + ((int) (this.step ^ (this.step >>> 32)));
    }

    public java.lang.String toString() {
        java.lang.StringBuilder sbAppend;
        long j;
        if (this.step > 0) {
            sbAppend = new java.lang.StringBuilder().append((java.lang.Object) kotlin.ULong.m11527toStringimpl(this.first)).append("..").append((java.lang.Object) kotlin.ULong.m11527toStringimpl(this.last)).append(" step ");
            j = this.step;
        } else {
            sbAppend = new java.lang.StringBuilder().append((java.lang.Object) kotlin.ULong.m11527toStringimpl(this.first)).append(" downTo ").append((java.lang.Object) kotlin.ULong.m11527toStringimpl(this.last)).append(" step ");
            j = -this.step;
        }
        return sbAppend.append(j).toString();
    }

    /* JADX INFO: compiled from: ULongRange.kt */
    @kotlin.Metadata(d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J%\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u00062\u0006\u0010\b\u001a\u00020\t¢\u0006\u0004\b\n\u0010\u000b¨\u0006\f"}, d2 = {"Lkotlin/ranges/ULongProgression$Companion;", "", "()V", "fromClosedRange", "Lkotlin/ranges/ULongProgression;", "rangeStart", "Lkotlin/ULong;", "rangeEnd", "step", "", "fromClosedRange-7ftBX0g", "(JJJ)Lkotlin/ranges/ULongProgression;", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public static final class Companion {
        public /* synthetic */ Companion(kotlin.jvm.internal.DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        /* JADX INFO: renamed from: fromClosedRange-7ftBX0g, reason: not valid java name */
        public final kotlin.ranges.ULongProgression m12541fromClosedRange7ftBX0g(long rangeStart, long rangeEnd, long step) {
            return new kotlin.ranges.ULongProgression(rangeStart, rangeEnd, step, null);
        }
    }
}
