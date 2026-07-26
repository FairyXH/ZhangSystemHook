package com.android.server.permission.jarjar.kotlin.ranges;

/* JADX INFO: compiled from: UIntRange.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0007\u0018\u0000 \u001c2\u00020\u00012\b\u0012\u0004\u0012\u00020\u00030\u00022\b\u0012\u0004\u0012\u00020\u00030\u0004:\u0001\u001cB\u0015\u0012\u0006\u0010\u0005\u001a\u00020\u0003\u0012\u0006\u0010\u0006\u001a\u00020\u0003¢\u0006\u0002\u0010\u0007J\u0018\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0003H\u0096\u0002¢\u0006\u0004\b\u0012\u0010\u0013J\u0013\u0010\u0014\u001a\u00020\u00102\b\u0010\u0015\u001a\u0004\u0018\u00010\u0016H\u0096\u0002J\b\u0010\u0017\u001a\u00020\u0018H\u0016J\b\u0010\u0019\u001a\u00020\u0010H\u0016J\b\u0010\u001a\u001a\u00020\u001bH\u0016R\u001d\u0010\b\u001a\u00020\u00038VX\u0097\u0004ø\u0001\u0000¢\u0006\f\u0012\u0004\b\t\u0010\n\u001a\u0004\b\u000b\u0010\fR\u0017\u0010\u0006\u001a\u00020\u00038VX\u0096\u0004ø\u0001\u0000¢\u0006\u0006\u001a\u0004\b\r\u0010\fR\u0017\u0010\u0005\u001a\u00020\u00038VX\u0096\u0004ø\u0001\u0000¢\u0006\u0006\u001a\u0004\b\u000e\u0010\f\u0082\u0002\u0004\n\u0002\b!¨\u0006\u001d"}, d2 = {"Lkotlin/ranges/UIntRange;", "Lkotlin/ranges/UIntProgression;", "Lkotlin/ranges/ClosedRange;", "Lkotlin/UInt;", "Lkotlin/ranges/OpenEndRange;", "start", "endInclusive", "(IILkotlin/jvm/internal/DefaultConstructorMarker;)V", "endExclusive", "getEndExclusive-pVg5ArA$annotations", "()V", "getEndExclusive-pVg5ArA", "()I", "getEndInclusive-pVg5ArA", "getStart-pVg5ArA", "contains", "", "value", "contains-WZ4Q5Ns", "(I)Z", "equals", "other", "", com.android.server.oplus.osense.OsenseConstants.KEY_INTEGER_HASH, "", "isEmpty", "toString", "", "Companion", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class UIntRange extends com.android.server.permission.jarjar.kotlin.ranges.UIntProgression implements com.android.server.permission.jarjar.kotlin.ranges.ClosedRange<com.android.server.permission.jarjar.kotlin.UInt>, com.android.server.permission.jarjar.kotlin.ranges.OpenEndRange<com.android.server.permission.jarjar.kotlin.UInt> {
    public static final com.android.server.permission.jarjar.kotlin.ranges.UIntRange.Companion Companion;
    private static final com.android.server.permission.jarjar.kotlin.ranges.UIntRange EMPTY;

    public /* synthetic */ UIntRange(int i, int i2, com.android.server.permission.jarjar.kotlin.jvm.internal.DefaultConstructorMarker defaultConstructorMarker) {
        this(i, i2);
    }

    @com.android.server.permission.jarjar.kotlin.Deprecated(message = "Can throw an exception when it's impossible to represent the value with UInt type, for example, when the range includes MAX_VALUE. It's recommended to use 'endInclusive' property that doesn't throw.")
    /* JADX INFO: renamed from: getEndExclusive-pVg5ArA$annotations, reason: not valid java name */
    public static /* synthetic */ void m7316getEndExclusivepVg5ArA$annotations() {
    }

    @Override // com.android.server.permission.jarjar.kotlin.ranges.ClosedRange, com.android.server.permission.jarjar.kotlin.ranges.OpenEndRange
    public /* bridge */ /* synthetic */ boolean contains(java.lang.Comparable value) {
        return m7317containsWZ4Q5Ns(((com.android.server.permission.jarjar.kotlin.UInt) value).m6236unboximpl());
    }

    @Override // com.android.server.permission.jarjar.kotlin.ranges.OpenEndRange
    public /* bridge */ /* synthetic */ java.lang.Comparable getEndExclusive() {
        return com.android.server.permission.jarjar.kotlin.UInt.m6178boximpl(m7318getEndExclusivepVg5ArA());
    }

    @Override // com.android.server.permission.jarjar.kotlin.ranges.ClosedRange
    public /* bridge */ /* synthetic */ java.lang.Comparable getEndInclusive() {
        return com.android.server.permission.jarjar.kotlin.UInt.m6178boximpl(m7319getEndInclusivepVg5ArA());
    }

    @Override // com.android.server.permission.jarjar.kotlin.ranges.ClosedRange, com.android.server.permission.jarjar.kotlin.ranges.OpenEndRange
    public /* bridge */ /* synthetic */ java.lang.Comparable getStart() {
        return com.android.server.permission.jarjar.kotlin.UInt.m6178boximpl(m7320getStartpVg5ArA());
    }

    private UIntRange(int start, int endInclusive) {
        super(start, endInclusive, 1, null);
    }

    /* JADX INFO: renamed from: getStart-pVg5ArA, reason: not valid java name */
    public int m7320getStartpVg5ArA() {
        return m7312getFirstpVg5ArA();
    }

    /* JADX INFO: renamed from: getEndInclusive-pVg5ArA, reason: not valid java name */
    public int m7319getEndInclusivepVg5ArA() {
        return m7313getLastpVg5ArA();
    }

    /* JADX INFO: renamed from: getEndExclusive-pVg5ArA, reason: not valid java name */
    public int m7318getEndExclusivepVg5ArA() {
        if (m7313getLastpVg5ArA() == -1) {
            throw new java.lang.IllegalStateException("Cannot return the exclusive upper bound of a range that includes MAX_VALUE.".toString());
        }
        return com.android.server.permission.jarjar.kotlin.UInt.m6184constructorimpl(m7313getLastpVg5ArA() + 1);
    }

    /* JADX INFO: renamed from: contains-WZ4Q5Ns, reason: not valid java name */
    public boolean m7317containsWZ4Q5Ns(int value) {
        return java.lang.Integer.compareUnsigned(m7312getFirstpVg5ArA(), value) <= 0 && java.lang.Integer.compareUnsigned(value, m7313getLastpVg5ArA()) <= 0;
    }

    @Override // com.android.server.permission.jarjar.kotlin.ranges.UIntProgression, com.android.server.permission.jarjar.kotlin.ranges.ClosedRange, com.android.server.permission.jarjar.kotlin.ranges.OpenEndRange
    public boolean isEmpty() {
        return java.lang.Integer.compareUnsigned(m7312getFirstpVg5ArA(), m7313getLastpVg5ArA()) > 0;
    }

    @Override // com.android.server.permission.jarjar.kotlin.ranges.UIntProgression
    public boolean equals(java.lang.Object other) {
        return (other instanceof com.android.server.permission.jarjar.kotlin.ranges.UIntRange) && ((isEmpty() && ((com.android.server.permission.jarjar.kotlin.ranges.UIntRange) other).isEmpty()) || (m7312getFirstpVg5ArA() == ((com.android.server.permission.jarjar.kotlin.ranges.UIntRange) other).m7312getFirstpVg5ArA() && m7313getLastpVg5ArA() == ((com.android.server.permission.jarjar.kotlin.ranges.UIntRange) other).m7313getLastpVg5ArA()));
    }

    @Override // com.android.server.permission.jarjar.kotlin.ranges.UIntProgression
    public int hashCode() {
        if (isEmpty()) {
            return -1;
        }
        return (m7312getFirstpVg5ArA() * 31) + m7313getLastpVg5ArA();
    }

    @Override // com.android.server.permission.jarjar.kotlin.ranges.UIntProgression
    public java.lang.String toString() {
        return ((java.lang.Object) com.android.server.permission.jarjar.kotlin.UInt.m6230toStringimpl(m7312getFirstpVg5ArA())) + ".." + ((java.lang.Object) com.android.server.permission.jarjar.kotlin.UInt.m6230toStringimpl(m7313getLastpVg5ArA()));
    }

    /* JADX INFO: compiled from: UIntRange.kt */
    @com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u0011\u0010\u0003\u001a\u00020\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006¨\u0006\u0007"}, d2 = {"Lkotlin/ranges/UIntRange$Companion;", "", "()V", "EMPTY", "Lkotlin/ranges/UIntRange;", "getEMPTY", "()Lkotlin/ranges/UIntRange;", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public static final class Companion {
        public /* synthetic */ Companion(com.android.server.permission.jarjar.kotlin.jvm.internal.DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final com.android.server.permission.jarjar.kotlin.ranges.UIntRange getEMPTY() {
            return com.android.server.permission.jarjar.kotlin.ranges.UIntRange.EMPTY;
        }
    }

    static {
        com.android.server.permission.jarjar.kotlin.jvm.internal.DefaultConstructorMarker defaultConstructorMarker = null;
        Companion = new com.android.server.permission.jarjar.kotlin.ranges.UIntRange.Companion(defaultConstructorMarker);
        EMPTY = new com.android.server.permission.jarjar.kotlin.ranges.UIntRange(-1, 0, defaultConstructorMarker);
    }
}
