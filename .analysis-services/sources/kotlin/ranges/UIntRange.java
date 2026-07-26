package kotlin.ranges;

/* JADX INFO: compiled from: UIntRange.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0007\u0018\u0000 \u001c2\u00020\u00012\b\u0012\u0004\u0012\u00020\u00030\u00022\b\u0012\u0004\u0012\u00020\u00030\u0004:\u0001\u001cB\u0015\u0012\u0006\u0010\u0005\u001a\u00020\u0003\u0012\u0006\u0010\u0006\u001a\u00020\u0003¢\u0006\u0002\u0010\u0007J\u0018\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0003H\u0096\u0002¢\u0006\u0004\b\u0012\u0010\u0013J\u0013\u0010\u0014\u001a\u00020\u00102\b\u0010\u0015\u001a\u0004\u0018\u00010\u0016H\u0096\u0002J\b\u0010\u0017\u001a\u00020\u0018H\u0016J\b\u0010\u0019\u001a\u00020\u0010H\u0016J\b\u0010\u001a\u001a\u00020\u001bH\u0016R\u001d\u0010\b\u001a\u00020\u00038VX\u0097\u0004ø\u0001\u0000¢\u0006\f\u0012\u0004\b\t\u0010\n\u001a\u0004\b\u000b\u0010\fR\u0017\u0010\u0006\u001a\u00020\u00038VX\u0096\u0004ø\u0001\u0000¢\u0006\u0006\u001a\u0004\b\r\u0010\fR\u0017\u0010\u0005\u001a\u00020\u00038VX\u0096\u0004ø\u0001\u0000¢\u0006\u0006\u001a\u0004\b\u000e\u0010\f\u0082\u0002\u0004\n\u0002\b!¨\u0006\u001d"}, d2 = {"Lkotlin/ranges/UIntRange;", "Lkotlin/ranges/UIntProgression;", "Lkotlin/ranges/ClosedRange;", "Lkotlin/UInt;", "Lkotlin/ranges/OpenEndRange;", "start", "endInclusive", "(IILkotlin/jvm/internal/DefaultConstructorMarker;)V", "endExclusive", "getEndExclusive-pVg5ArA$annotations", "()V", "getEndExclusive-pVg5ArA", "()I", "getEndInclusive-pVg5ArA", "getStart-pVg5ArA", "contains", "", "value", "contains-WZ4Q5Ns", "(I)Z", "equals", "other", "", com.android.server.oplus.osense.OsenseConstants.KEY_INTEGER_HASH, "", "isEmpty", "toString", "", "Companion", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class UIntRange extends kotlin.ranges.UIntProgression implements kotlin.ranges.ClosedRange<kotlin.UInt>, kotlin.ranges.OpenEndRange<kotlin.UInt> {

    /* JADX INFO: renamed from: Companion, reason: from kotlin metadata */
    public static final kotlin.ranges.UIntRange.Companion INSTANCE;
    private static final kotlin.ranges.UIntRange EMPTY;

    public /* synthetic */ UIntRange(int i, int i2, kotlin.jvm.internal.DefaultConstructorMarker defaultConstructorMarker) {
        this(i, i2);
    }

    @kotlin.Deprecated(message = "Can throw an exception when it's impossible to represent the value with UInt type, for example, when the range includes MAX_VALUE. It's recommended to use 'endInclusive' property that doesn't throw.")
    /* JADX INFO: renamed from: getEndExclusive-pVg5ArA$annotations, reason: not valid java name */
    public static /* synthetic */ void m12534getEndExclusivepVg5ArA$annotations() {
    }

    @Override // kotlin.ranges.ClosedRange, kotlin.ranges.OpenEndRange
    public /* bridge */ /* synthetic */ boolean contains(java.lang.Comparable value) {
        return m12535containsWZ4Q5Ns(((kotlin.UInt) value).getData());
    }

    @Override // kotlin.ranges.OpenEndRange
    public /* bridge */ /* synthetic */ java.lang.Comparable getEndExclusive() {
        return kotlin.UInt.m11396boximpl(m12536getEndExclusivepVg5ArA());
    }

    @Override // kotlin.ranges.ClosedRange
    public /* bridge */ /* synthetic */ java.lang.Comparable getEndInclusive() {
        return kotlin.UInt.m11396boximpl(m12537getEndInclusivepVg5ArA());
    }

    @Override // kotlin.ranges.ClosedRange, kotlin.ranges.OpenEndRange
    public /* bridge */ /* synthetic */ java.lang.Comparable getStart() {
        return kotlin.UInt.m11396boximpl(m12538getStartpVg5ArA());
    }

    private UIntRange(int start, int endInclusive) {
        super(start, endInclusive, 1, null);
    }

    /* JADX INFO: renamed from: getStart-pVg5ArA, reason: not valid java name */
    public int m12538getStartpVg5ArA() {
        return getFirst();
    }

    /* JADX INFO: renamed from: getEndInclusive-pVg5ArA, reason: not valid java name */
    public int m12537getEndInclusivepVg5ArA() {
        return getLast();
    }

    /* JADX INFO: renamed from: getEndExclusive-pVg5ArA, reason: not valid java name */
    public int m12536getEndExclusivepVg5ArA() {
        if (getLast() == -1) {
            throw new java.lang.IllegalStateException("Cannot return the exclusive upper bound of a range that includes MAX_VALUE.".toString());
        }
        return kotlin.UInt.m11402constructorimpl(getLast() + 1);
    }

    /* JADX INFO: renamed from: contains-WZ4Q5Ns, reason: not valid java name */
    public boolean m12535containsWZ4Q5Ns(int value) {
        return java.lang.Integer.compareUnsigned(getFirst(), value) <= 0 && java.lang.Integer.compareUnsigned(value, getLast()) <= 0;
    }

    @Override // kotlin.ranges.UIntProgression, kotlin.ranges.ClosedRange, kotlin.ranges.OpenEndRange
    public boolean isEmpty() {
        return java.lang.Integer.compareUnsigned(getFirst(), getLast()) > 0;
    }

    @Override // kotlin.ranges.UIntProgression
    public boolean equals(java.lang.Object other) {
        return (other instanceof kotlin.ranges.UIntRange) && ((isEmpty() && ((kotlin.ranges.UIntRange) other).isEmpty()) || (getFirst() == ((kotlin.ranges.UIntRange) other).getFirst() && getLast() == ((kotlin.ranges.UIntRange) other).getLast()));
    }

    @Override // kotlin.ranges.UIntProgression
    public int hashCode() {
        if (isEmpty()) {
            return -1;
        }
        return (getFirst() * 31) + getLast();
    }

    @Override // kotlin.ranges.UIntProgression
    public java.lang.String toString() {
        return ((java.lang.Object) kotlin.UInt.m11448toStringimpl(getFirst())) + ".." + ((java.lang.Object) kotlin.UInt.m11448toStringimpl(getLast()));
    }

    /* JADX INFO: compiled from: UIntRange.kt */
    @kotlin.Metadata(d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u0011\u0010\u0003\u001a\u00020\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006¨\u0006\u0007"}, d2 = {"Lkotlin/ranges/UIntRange$Companion;", "", "()V", "EMPTY", "Lkotlin/ranges/UIntRange;", "getEMPTY", "()Lkotlin/ranges/UIntRange;", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public static final class Companion {
        public /* synthetic */ Companion(kotlin.jvm.internal.DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final kotlin.ranges.UIntRange getEMPTY() {
            return kotlin.ranges.UIntRange.EMPTY;
        }
    }

    static {
        kotlin.jvm.internal.DefaultConstructorMarker defaultConstructorMarker = null;
        INSTANCE = new kotlin.ranges.UIntRange.Companion(defaultConstructorMarker);
        EMPTY = new kotlin.ranges.UIntRange(-1, 0, defaultConstructorMarker);
    }
}
