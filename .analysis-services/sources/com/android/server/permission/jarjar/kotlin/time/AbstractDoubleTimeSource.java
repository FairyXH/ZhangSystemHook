package com.android.server.permission.jarjar.kotlin.time;

/* JADX INFO: compiled from: TimeSources.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0002\b'\u0018\u00002\u00020\u0001:\u0001\u000bB\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\b\u0010\u0007\u001a\u00020\bH\u0016J\b\u0010\t\u001a\u00020\nH$R\u0014\u0010\u0002\u001a\u00020\u0003X\u0084\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006¨\u0006\f"}, d2 = {"Lkotlin/time/AbstractDoubleTimeSource;", "Lkotlin/time/TimeSource$WithComparableMarks;", "unit", "Lkotlin/time/DurationUnit;", "(Lkotlin/time/DurationUnit;)V", "getUnit", "()Lkotlin/time/DurationUnit;", "markNow", "Lkotlin/time/ComparableTimeMark;", "read", "", "DoubleTimeMark", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
@com.android.server.permission.jarjar.kotlin.Deprecated(message = "Using AbstractDoubleTimeSource is no longer recommended, use AbstractLongTimeSource instead.")
public abstract class AbstractDoubleTimeSource implements com.android.server.permission.jarjar.kotlin.time.TimeSource.WithComparableMarks {
    private final com.android.server.permission.jarjar.kotlin.time.DurationUnit unit;

    protected abstract double read();

    public AbstractDoubleTimeSource(com.android.server.permission.jarjar.kotlin.time.DurationUnit unit) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(unit, "unit");
        this.unit = unit;
    }

    protected final com.android.server.permission.jarjar.kotlin.time.DurationUnit getUnit() {
        return this.unit;
    }

    /* JADX INFO: compiled from: TimeSources.kt */
    @com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0006\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\b\n\u0002\u0010\u000e\n\u0000\b\u0002\u0018\u00002\u00020\u0001B\u001d\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007¢\u0006\u0002\u0010\bJ\u0012\u0010\n\u001a\u00020\u0007H\u0016ø\u0001\u0000¢\u0006\u0004\b\u000b\u0010\fJ\u0013\u0010\r\u001a\u00020\u000e2\b\u0010\u000f\u001a\u0004\u0018\u00010\u0010H\u0096\u0002J\b\u0010\u0011\u001a\u00020\u0012H\u0016J\u001b\u0010\u0013\u001a\u00020\u00072\u0006\u0010\u000f\u001a\u00020\u0001H\u0096\u0002ø\u0001\u0000¢\u0006\u0004\b\u0014\u0010\u0015J\u0018\u0010\u0016\u001a\u00020\u00012\u0006\u0010\u0017\u001a\u00020\u0007H\u0096\u0002¢\u0006\u0004\b\u0018\u0010\u0019J\b\u0010\u001a\u001a\u00020\u001bH\u0016R\u0013\u0010\u0006\u001a\u00020\u0007X\u0082\u0004ø\u0001\u0000¢\u0006\u0004\n\u0002\u0010\tR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004¢\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b!¨\u0006\u001c"}, d2 = {"Lkotlin/time/AbstractDoubleTimeSource$DoubleTimeMark;", "Lkotlin/time/ComparableTimeMark;", "startedAt", "", "timeSource", "Lkotlin/time/AbstractDoubleTimeSource;", "offset", "Lkotlin/time/Duration;", "(DLkotlin/time/AbstractDoubleTimeSource;JLkotlin/jvm/internal/DefaultConstructorMarker;)V", "J", "elapsedNow", "elapsedNow-UwyO8pc", "()J", "equals", "", "other", "", com.android.server.oplus.osense.OsenseConstants.KEY_INTEGER_HASH, "", "minus", "minus-UwyO8pc", "(Lkotlin/time/ComparableTimeMark;)J", "plus", "duration", "plus-LRDsOJo", "(J)Lkotlin/time/ComparableTimeMark;", "toString", "", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
    private static final class DoubleTimeMark implements com.android.server.permission.jarjar.kotlin.time.ComparableTimeMark {
        private final long offset;
        private final double startedAt;
        private final com.android.server.permission.jarjar.kotlin.time.AbstractDoubleTimeSource timeSource;

        public /* synthetic */ DoubleTimeMark(double d, com.android.server.permission.jarjar.kotlin.time.AbstractDoubleTimeSource abstractDoubleTimeSource, long j, com.android.server.permission.jarjar.kotlin.jvm.internal.DefaultConstructorMarker defaultConstructorMarker) {
            this(d, abstractDoubleTimeSource, j);
        }

        private DoubleTimeMark(double startedAt, com.android.server.permission.jarjar.kotlin.time.AbstractDoubleTimeSource timeSource, long offset) {
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(timeSource, "timeSource");
            this.startedAt = startedAt;
            this.timeSource = timeSource;
            this.offset = offset;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.lang.Comparable
        public int compareTo(com.android.server.permission.jarjar.kotlin.time.ComparableTimeMark other) {
            return com.android.server.permission.jarjar.kotlin.time.ComparableTimeMark.DefaultImpls.compareTo(this, other);
        }

        @Override // com.android.server.permission.jarjar.kotlin.time.TimeMark
        public boolean hasNotPassedNow() {
            return com.android.server.permission.jarjar.kotlin.time.ComparableTimeMark.DefaultImpls.hasNotPassedNow(this);
        }

        @Override // com.android.server.permission.jarjar.kotlin.time.TimeMark
        public boolean hasPassedNow() {
            return com.android.server.permission.jarjar.kotlin.time.ComparableTimeMark.DefaultImpls.hasPassedNow(this);
        }

        @Override // com.android.server.permission.jarjar.kotlin.time.TimeMark
        /* JADX INFO: renamed from: minus-LRDsOJo, reason: not valid java name */
        public com.android.server.permission.jarjar.kotlin.time.ComparableTimeMark mo7405minusLRDsOJo(long duration) {
            return com.android.server.permission.jarjar.kotlin.time.ComparableTimeMark.DefaultImpls.m7409minusLRDsOJo(this, duration);
        }

        @Override // com.android.server.permission.jarjar.kotlin.time.TimeMark
        /* JADX INFO: renamed from: elapsedNow-UwyO8pc, reason: not valid java name */
        public long mo7404elapsedNowUwyO8pc() {
            return com.android.server.permission.jarjar.kotlin.time.Duration.m7449minusLRDsOJo(com.android.server.permission.jarjar.kotlin.time.DurationKt.toDuration(this.timeSource.read() - this.startedAt, this.timeSource.getUnit()), this.offset);
        }

        @Override // com.android.server.permission.jarjar.kotlin.time.TimeMark
        /* JADX INFO: renamed from: plus-LRDsOJo, reason: not valid java name */
        public com.android.server.permission.jarjar.kotlin.time.ComparableTimeMark mo7407plusLRDsOJo(long duration) {
            return new com.android.server.permission.jarjar.kotlin.time.AbstractDoubleTimeSource.DoubleTimeMark(this.startedAt, this.timeSource, com.android.server.permission.jarjar.kotlin.time.Duration.m7450plusLRDsOJo(this.offset, duration), null);
        }

        @Override // com.android.server.permission.jarjar.kotlin.time.ComparableTimeMark
        /* JADX INFO: renamed from: minus-UwyO8pc, reason: not valid java name */
        public long mo7406minusUwyO8pc(com.android.server.permission.jarjar.kotlin.time.ComparableTimeMark other) {
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(other, "other");
            if (!(other instanceof com.android.server.permission.jarjar.kotlin.time.AbstractDoubleTimeSource.DoubleTimeMark) || !com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.areEqual(this.timeSource, ((com.android.server.permission.jarjar.kotlin.time.AbstractDoubleTimeSource.DoubleTimeMark) other).timeSource)) {
                throw new java.lang.IllegalArgumentException("Subtracting or comparing time marks from different time sources is not possible: " + this + " and " + other);
            }
            if (com.android.server.permission.jarjar.kotlin.time.Duration.m7419equalsimpl0(this.offset, ((com.android.server.permission.jarjar.kotlin.time.AbstractDoubleTimeSource.DoubleTimeMark) other).offset) && com.android.server.permission.jarjar.kotlin.time.Duration.m7446isInfiniteimpl(this.offset)) {
                return com.android.server.permission.jarjar.kotlin.time.Duration.Companion.m7517getZEROUwyO8pc();
            }
            long offsetDiff = com.android.server.permission.jarjar.kotlin.time.Duration.m7449minusLRDsOJo(this.offset, ((com.android.server.permission.jarjar.kotlin.time.AbstractDoubleTimeSource.DoubleTimeMark) other).offset);
            long startedAtDiff = com.android.server.permission.jarjar.kotlin.time.DurationKt.toDuration(this.startedAt - ((com.android.server.permission.jarjar.kotlin.time.AbstractDoubleTimeSource.DoubleTimeMark) other).startedAt, this.timeSource.getUnit());
            return com.android.server.permission.jarjar.kotlin.time.Duration.m7419equalsimpl0(startedAtDiff, com.android.server.permission.jarjar.kotlin.time.Duration.m7467unaryMinusUwyO8pc(offsetDiff)) ? com.android.server.permission.jarjar.kotlin.time.Duration.Companion.m7517getZEROUwyO8pc() : com.android.server.permission.jarjar.kotlin.time.Duration.m7450plusLRDsOJo(startedAtDiff, offsetDiff);
        }

        @Override // com.android.server.permission.jarjar.kotlin.time.ComparableTimeMark
        public boolean equals(java.lang.Object other) {
            return (other instanceof com.android.server.permission.jarjar.kotlin.time.AbstractDoubleTimeSource.DoubleTimeMark) && com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.areEqual(this.timeSource, ((com.android.server.permission.jarjar.kotlin.time.AbstractDoubleTimeSource.DoubleTimeMark) other).timeSource) && com.android.server.permission.jarjar.kotlin.time.Duration.m7419equalsimpl0(mo7406minusUwyO8pc((com.android.server.permission.jarjar.kotlin.time.ComparableTimeMark) other), com.android.server.permission.jarjar.kotlin.time.Duration.Companion.m7517getZEROUwyO8pc());
        }

        @Override // com.android.server.permission.jarjar.kotlin.time.ComparableTimeMark
        public int hashCode() {
            return com.android.server.permission.jarjar.kotlin.time.Duration.m7442hashCodeimpl(com.android.server.permission.jarjar.kotlin.time.Duration.m7450plusLRDsOJo(com.android.server.permission.jarjar.kotlin.time.DurationKt.toDuration(this.startedAt, this.timeSource.getUnit()), this.offset));
        }

        public java.lang.String toString() {
            return "DoubleTimeMark(" + this.startedAt + com.android.server.permission.jarjar.kotlin.time.DurationUnitKt.shortName(this.timeSource.getUnit()) + " + " + ((java.lang.Object) com.android.server.permission.jarjar.kotlin.time.Duration.m7463toStringimpl(this.offset)) + ", " + this.timeSource + ')';
        }
    }

    @Override // com.android.server.permission.jarjar.kotlin.time.TimeSource
    public com.android.server.permission.jarjar.kotlin.time.ComparableTimeMark markNow() {
        return new com.android.server.permission.jarjar.kotlin.time.AbstractDoubleTimeSource.DoubleTimeMark(read(), this, com.android.server.permission.jarjar.kotlin.time.Duration.Companion.m7517getZEROUwyO8pc(), null);
    }
}
