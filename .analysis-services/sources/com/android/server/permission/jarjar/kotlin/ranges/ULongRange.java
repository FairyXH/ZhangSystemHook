package com.android.server.permission.jarjar.kotlin.ranges;

/* JADX INFO: compiled from: ULongRange.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0007\u0018\u0000 \u001c2\u00020\u00012\b\u0012\u0004\u0012\u00020\u00030\u00022\b\u0012\u0004\u0012\u00020\u00030\u0004:\u0001\u001cB\u0015\u0012\u0006\u0010\u0005\u001a\u00020\u0003\u0012\u0006\u0010\u0006\u001a\u00020\u0003¢\u0006\u0002\u0010\u0007J\u0018\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0003H\u0096\u0002¢\u0006\u0004\b\u0012\u0010\u0013J\u0013\u0010\u0014\u001a\u00020\u00102\b\u0010\u0015\u001a\u0004\u0018\u00010\u0016H\u0096\u0002J\b\u0010\u0017\u001a\u00020\u0018H\u0016J\b\u0010\u0019\u001a\u00020\u0010H\u0016J\b\u0010\u001a\u001a\u00020\u001bH\u0016R\u001d\u0010\b\u001a\u00020\u00038VX\u0097\u0004ø\u0001\u0000¢\u0006\f\u0012\u0004\b\t\u0010\n\u001a\u0004\b\u000b\u0010\fR\u0017\u0010\u0006\u001a\u00020\u00038VX\u0096\u0004ø\u0001\u0000¢\u0006\u0006\u001a\u0004\b\r\u0010\fR\u0017\u0010\u0005\u001a\u00020\u00038VX\u0096\u0004ø\u0001\u0000¢\u0006\u0006\u001a\u0004\b\u000e\u0010\f\u0082\u0002\u0004\n\u0002\b!¨\u0006\u001d"}, d2 = {"Lkotlin/ranges/ULongRange;", "Lkotlin/ranges/ULongProgression;", "Lkotlin/ranges/ClosedRange;", "Lkotlin/ULong;", "Lkotlin/ranges/OpenEndRange;", "start", "endInclusive", "(JJLkotlin/jvm/internal/DefaultConstructorMarker;)V", "endExclusive", "getEndExclusive-s-VKNKU$annotations", "()V", "getEndExclusive-s-VKNKU", "()J", "getEndInclusive-s-VKNKU", "getStart-s-VKNKU", "contains", "", "value", "contains-VKZWuLQ", "(J)Z", "equals", "other", "", com.android.server.oplus.osense.OsenseConstants.KEY_INTEGER_HASH, "", "isEmpty", "toString", "", "Companion", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class ULongRange extends com.android.server.permission.jarjar.kotlin.ranges.ULongProgression implements com.android.server.permission.jarjar.kotlin.ranges.ClosedRange<com.android.server.permission.jarjar.kotlin.ULong>, com.android.server.permission.jarjar.kotlin.ranges.OpenEndRange<com.android.server.permission.jarjar.kotlin.ULong> {
    public static final com.android.server.permission.jarjar.kotlin.ranges.ULongRange.Companion Companion = new com.android.server.permission.jarjar.kotlin.ranges.ULongRange.Companion(null);
    private static final com.android.server.permission.jarjar.kotlin.ranges.ULongRange EMPTY = new com.android.server.permission.jarjar.kotlin.ranges.ULongRange(-1, 0, null);

    public /* synthetic */ ULongRange(long j, long j2, com.android.server.permission.jarjar.kotlin.jvm.internal.DefaultConstructorMarker defaultConstructorMarker) {
        this(j, j2);
    }

    @com.android.server.permission.jarjar.kotlin.Deprecated(message = "Can throw an exception when it's impossible to represent the value with ULong type, for example, when the range includes MAX_VALUE. It's recommended to use 'endInclusive' property that doesn't throw.")
    /* JADX INFO: renamed from: getEndExclusive-s-VKNKU$annotations, reason: not valid java name */
    public static /* synthetic */ void m7325getEndExclusivesVKNKU$annotations() {
    }

    @Override // com.android.server.permission.jarjar.kotlin.ranges.ClosedRange, com.android.server.permission.jarjar.kotlin.ranges.OpenEndRange
    public /* bridge */ /* synthetic */ boolean contains(java.lang.Comparable value) {
        return m7326containsVKZWuLQ(((com.android.server.permission.jarjar.kotlin.ULong) value).m6315unboximpl());
    }

    @Override // com.android.server.permission.jarjar.kotlin.ranges.OpenEndRange
    public /* bridge */ /* synthetic */ java.lang.Comparable getEndExclusive() {
        return com.android.server.permission.jarjar.kotlin.ULong.m6257boximpl(m7327getEndExclusivesVKNKU());
    }

    @Override // com.android.server.permission.jarjar.kotlin.ranges.ClosedRange
    public /* bridge */ /* synthetic */ java.lang.Comparable getEndInclusive() {
        return com.android.server.permission.jarjar.kotlin.ULong.m6257boximpl(m7328getEndInclusivesVKNKU());
    }

    @Override // com.android.server.permission.jarjar.kotlin.ranges.ClosedRange, com.android.server.permission.jarjar.kotlin.ranges.OpenEndRange
    public /* bridge */ /* synthetic */ java.lang.Comparable getStart() {
        return com.android.server.permission.jarjar.kotlin.ULong.m6257boximpl(m7329getStartsVKNKU());
    }

    private ULongRange(long start, long endInclusive) {
        super(start, endInclusive, 1L, null);
    }

    /* JADX INFO: renamed from: getStart-s-VKNKU, reason: not valid java name */
    public long m7329getStartsVKNKU() {
        return m7321getFirstsVKNKU();
    }

    /* JADX INFO: renamed from: getEndInclusive-s-VKNKU, reason: not valid java name */
    public long m7328getEndInclusivesVKNKU() {
        return m7322getLastsVKNKU();
    }

    /* JADX INFO: renamed from: getEndExclusive-s-VKNKU, reason: not valid java name */
    public long m7327getEndExclusivesVKNKU() {
        if (m7322getLastsVKNKU() == -1) {
            throw new java.lang.IllegalStateException("Cannot return the exclusive upper bound of a range that includes MAX_VALUE.".toString());
        }
        return com.android.server.permission.jarjar.kotlin.ULong.m6263constructorimpl(m7322getLastsVKNKU() + com.android.server.permission.jarjar.kotlin.ULong.m6263constructorimpl(((long) 1) & 4294967295L));
    }

    /* JADX INFO: renamed from: contains-VKZWuLQ, reason: not valid java name */
    public boolean m7326containsVKZWuLQ(long value) {
        return java.lang.Long.compareUnsigned(m7321getFirstsVKNKU(), value) <= 0 && java.lang.Long.compareUnsigned(value, m7322getLastsVKNKU()) <= 0;
    }

    @Override // com.android.server.permission.jarjar.kotlin.ranges.ULongProgression, com.android.server.permission.jarjar.kotlin.ranges.ClosedRange, com.android.server.permission.jarjar.kotlin.ranges.OpenEndRange
    public boolean isEmpty() {
        return java.lang.Long.compareUnsigned(m7321getFirstsVKNKU(), m7322getLastsVKNKU()) > 0;
    }

    @Override // com.android.server.permission.jarjar.kotlin.ranges.ULongProgression
    public boolean equals(java.lang.Object other) {
        return (other instanceof com.android.server.permission.jarjar.kotlin.ranges.ULongRange) && ((isEmpty() && ((com.android.server.permission.jarjar.kotlin.ranges.ULongRange) other).isEmpty()) || (m7321getFirstsVKNKU() == ((com.android.server.permission.jarjar.kotlin.ranges.ULongRange) other).m7321getFirstsVKNKU() && m7322getLastsVKNKU() == ((com.android.server.permission.jarjar.kotlin.ranges.ULongRange) other).m7322getLastsVKNKU()));
    }

    @Override // com.android.server.permission.jarjar.kotlin.ranges.ULongProgression
    public int hashCode() {
        if (isEmpty()) {
            return -1;
        }
        return (((int) com.android.server.permission.jarjar.kotlin.ULong.m6263constructorimpl(m7321getFirstsVKNKU() ^ com.android.server.permission.jarjar.kotlin.ULong.m6263constructorimpl(m7321getFirstsVKNKU() >>> 32))) * 31) + ((int) com.android.server.permission.jarjar.kotlin.ULong.m6263constructorimpl(m7322getLastsVKNKU() ^ com.android.server.permission.jarjar.kotlin.ULong.m6263constructorimpl(m7322getLastsVKNKU() >>> 32)));
    }

    @Override // com.android.server.permission.jarjar.kotlin.ranges.ULongProgression
    public java.lang.String toString() {
        return ((java.lang.Object) com.android.server.permission.jarjar.kotlin.ULong.m6309toStringimpl(m7321getFirstsVKNKU())) + ".." + ((java.lang.Object) com.android.server.permission.jarjar.kotlin.ULong.m6309toStringimpl(m7322getLastsVKNKU()));
    }

    /* JADX INFO: compiled from: ULongRange.kt */
    @com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u0011\u0010\u0003\u001a\u00020\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006¨\u0006\u0007"}, d2 = {"Lkotlin/ranges/ULongRange$Companion;", "", "()V", "EMPTY", "Lkotlin/ranges/ULongRange;", "getEMPTY", "()Lkotlin/ranges/ULongRange;", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public static final class Companion {
        public /* synthetic */ Companion(com.android.server.permission.jarjar.kotlin.jvm.internal.DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final com.android.server.permission.jarjar.kotlin.ranges.ULongRange getEMPTY() {
            return com.android.server.permission.jarjar.kotlin.ranges.ULongRange.EMPTY;
        }
    }
}
