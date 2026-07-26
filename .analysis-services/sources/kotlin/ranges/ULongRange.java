package kotlin.ranges;

/* JADX INFO: compiled from: ULongRange.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0007\u0018\u0000 \u001c2\u00020\u00012\b\u0012\u0004\u0012\u00020\u00030\u00022\b\u0012\u0004\u0012\u00020\u00030\u0004:\u0001\u001cB\u0015\u0012\u0006\u0010\u0005\u001a\u00020\u0003\u0012\u0006\u0010\u0006\u001a\u00020\u0003¢\u0006\u0002\u0010\u0007J\u0018\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0003H\u0096\u0002¢\u0006\u0004\b\u0012\u0010\u0013J\u0013\u0010\u0014\u001a\u00020\u00102\b\u0010\u0015\u001a\u0004\u0018\u00010\u0016H\u0096\u0002J\b\u0010\u0017\u001a\u00020\u0018H\u0016J\b\u0010\u0019\u001a\u00020\u0010H\u0016J\b\u0010\u001a\u001a\u00020\u001bH\u0016R\u001d\u0010\b\u001a\u00020\u00038VX\u0097\u0004ø\u0001\u0000¢\u0006\f\u0012\u0004\b\t\u0010\n\u001a\u0004\b\u000b\u0010\fR\u0017\u0010\u0006\u001a\u00020\u00038VX\u0096\u0004ø\u0001\u0000¢\u0006\u0006\u001a\u0004\b\r\u0010\fR\u0017\u0010\u0005\u001a\u00020\u00038VX\u0096\u0004ø\u0001\u0000¢\u0006\u0006\u001a\u0004\b\u000e\u0010\f\u0082\u0002\u0004\n\u0002\b!¨\u0006\u001d"}, d2 = {"Lkotlin/ranges/ULongRange;", "Lkotlin/ranges/ULongProgression;", "Lkotlin/ranges/ClosedRange;", "Lkotlin/ULong;", "Lkotlin/ranges/OpenEndRange;", "start", "endInclusive", "(JJLkotlin/jvm/internal/DefaultConstructorMarker;)V", "endExclusive", "getEndExclusive-s-VKNKU$annotations", "()V", "getEndExclusive-s-VKNKU", "()J", "getEndInclusive-s-VKNKU", "getStart-s-VKNKU", "contains", "", "value", "contains-VKZWuLQ", "(J)Z", "equals", "other", "", com.android.server.oplus.osense.OsenseConstants.KEY_INTEGER_HASH, "", "isEmpty", "toString", "", "Companion", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class ULongRange extends kotlin.ranges.ULongProgression implements kotlin.ranges.ClosedRange<kotlin.ULong>, kotlin.ranges.OpenEndRange<kotlin.ULong> {

    /* JADX INFO: renamed from: Companion, reason: from kotlin metadata */
    public static final kotlin.ranges.ULongRange.Companion INSTANCE = new kotlin.ranges.ULongRange.Companion(null);
    private static final kotlin.ranges.ULongRange EMPTY = new kotlin.ranges.ULongRange(-1, 0, null);

    public /* synthetic */ ULongRange(long j, long j2, kotlin.jvm.internal.DefaultConstructorMarker defaultConstructorMarker) {
        this(j, j2);
    }

    @kotlin.Deprecated(message = "Can throw an exception when it's impossible to represent the value with ULong type, for example, when the range includes MAX_VALUE. It's recommended to use 'endInclusive' property that doesn't throw.")
    /* JADX INFO: renamed from: getEndExclusive-s-VKNKU$annotations, reason: not valid java name */
    public static /* synthetic */ void m12543getEndExclusivesVKNKU$annotations() {
    }

    @Override // kotlin.ranges.ClosedRange, kotlin.ranges.OpenEndRange
    public /* bridge */ /* synthetic */ boolean contains(java.lang.Comparable value) {
        return m12544containsVKZWuLQ(((kotlin.ULong) value).getData());
    }

    @Override // kotlin.ranges.OpenEndRange
    public /* bridge */ /* synthetic */ java.lang.Comparable getEndExclusive() {
        return kotlin.ULong.m11475boximpl(m12545getEndExclusivesVKNKU());
    }

    @Override // kotlin.ranges.ClosedRange
    public /* bridge */ /* synthetic */ java.lang.Comparable getEndInclusive() {
        return kotlin.ULong.m11475boximpl(m12546getEndInclusivesVKNKU());
    }

    @Override // kotlin.ranges.ClosedRange, kotlin.ranges.OpenEndRange
    public /* bridge */ /* synthetic */ java.lang.Comparable getStart() {
        return kotlin.ULong.m11475boximpl(m12547getStartsVKNKU());
    }

    private ULongRange(long start, long endInclusive) {
        super(start, endInclusive, 1L, null);
    }

    /* JADX INFO: renamed from: getStart-s-VKNKU, reason: not valid java name */
    public long m12547getStartsVKNKU() {
        return getFirst();
    }

    /* JADX INFO: renamed from: getEndInclusive-s-VKNKU, reason: not valid java name */
    public long m12546getEndInclusivesVKNKU() {
        return getLast();
    }

    /* JADX INFO: renamed from: getEndExclusive-s-VKNKU, reason: not valid java name */
    public long m12545getEndExclusivesVKNKU() {
        if (getLast() == -1) {
            throw new java.lang.IllegalStateException("Cannot return the exclusive upper bound of a range that includes MAX_VALUE.".toString());
        }
        return kotlin.ULong.m11481constructorimpl(getLast() + kotlin.ULong.m11481constructorimpl(((long) 1) & 4294967295L));
    }

    /* JADX INFO: renamed from: contains-VKZWuLQ, reason: not valid java name */
    public boolean m12544containsVKZWuLQ(long value) {
        return java.lang.Long.compareUnsigned(getFirst(), value) <= 0 && java.lang.Long.compareUnsigned(value, getLast()) <= 0;
    }

    @Override // kotlin.ranges.ULongProgression, kotlin.ranges.ClosedRange, kotlin.ranges.OpenEndRange
    public boolean isEmpty() {
        return java.lang.Long.compareUnsigned(getFirst(), getLast()) > 0;
    }

    @Override // kotlin.ranges.ULongProgression
    public boolean equals(java.lang.Object other) {
        return (other instanceof kotlin.ranges.ULongRange) && ((isEmpty() && ((kotlin.ranges.ULongRange) other).isEmpty()) || (getFirst() == ((kotlin.ranges.ULongRange) other).getFirst() && getLast() == ((kotlin.ranges.ULongRange) other).getLast()));
    }

    @Override // kotlin.ranges.ULongProgression
    public int hashCode() {
        if (isEmpty()) {
            return -1;
        }
        return (((int) kotlin.ULong.m11481constructorimpl(getFirst() ^ kotlin.ULong.m11481constructorimpl(getFirst() >>> 32))) * 31) + ((int) kotlin.ULong.m11481constructorimpl(getLast() ^ kotlin.ULong.m11481constructorimpl(getLast() >>> 32)));
    }

    @Override // kotlin.ranges.ULongProgression
    public java.lang.String toString() {
        return ((java.lang.Object) kotlin.ULong.m11527toStringimpl(getFirst())) + ".." + ((java.lang.Object) kotlin.ULong.m11527toStringimpl(getLast()));
    }

    /* JADX INFO: compiled from: ULongRange.kt */
    @kotlin.Metadata(d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u0011\u0010\u0003\u001a\u00020\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006¨\u0006\u0007"}, d2 = {"Lkotlin/ranges/ULongRange$Companion;", "", "()V", "EMPTY", "Lkotlin/ranges/ULongRange;", "getEMPTY", "()Lkotlin/ranges/ULongRange;", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public static final class Companion {
        public /* synthetic */ Companion(kotlin.jvm.internal.DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final kotlin.ranges.ULongRange getEMPTY() {
            return kotlin.ranges.ULongRange.EMPTY;
        }
    }
}
