package com.android.server.permission.jarjar.kotlin.time;

/* JADX INFO: compiled from: TimeSources.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\t\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0003\b'\u0018\u00002\u00020\u0001:\u0001\u0011B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\b\u0010\r\u001a\u00020\bH\u0002J\b\u0010\u000e\u001a\u00020\u000fH\u0016J\b\u0010\u0010\u001a\u00020\bH$R\u0014\u0010\u0002\u001a\u00020\u0003X\u0084\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006R\u001b\u0010\u0007\u001a\u00020\b8BX\u0082\u0084\u0002¢\u0006\f\n\u0004\b\u000b\u0010\f\u001a\u0004\b\t\u0010\n¨\u0006\u0012"}, d2 = {"Lkotlin/time/AbstractLongTimeSource;", "Lkotlin/time/TimeSource$WithComparableMarks;", "unit", "Lkotlin/time/DurationUnit;", "(Lkotlin/time/DurationUnit;)V", "getUnit", "()Lkotlin/time/DurationUnit;", "zero", "", "getZero", "()J", "zero$delegate", "Lkotlin/Lazy;", "adjustedRead", "markNow", "Lkotlin/time/ComparableTimeMark;", "read", "LongTimeMark", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
public abstract class AbstractLongTimeSource implements com.android.server.permission.jarjar.kotlin.time.TimeSource.WithComparableMarks {
    private final com.android.server.permission.jarjar.kotlin.time.DurationUnit unit;
    private final com.android.server.permission.jarjar.kotlin.Lazy zero$delegate;

    protected abstract long read();

    public AbstractLongTimeSource(com.android.server.permission.jarjar.kotlin.time.DurationUnit unit) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(unit, "unit");
        this.unit = unit;
        this.zero$delegate = com.android.server.permission.jarjar.kotlin.LazyKt.lazy(new com.android.server.permission.jarjar.kotlin.time.AbstractLongTimeSource$zero$2(this));
    }

    protected final com.android.server.permission.jarjar.kotlin.time.DurationUnit getUnit() {
        return this.unit;
    }

    private final long getZero() {
        return ((java.lang.Number) this.zero$delegate.getValue()).longValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final long adjustedRead() {
        return read() - getZero();
    }

    /* JADX INFO: compiled from: TimeSources.kt */
    @com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\b\n\u0002\u0010\u000e\n\u0000\b\u0002\u0018\u00002\u00020\u0001B\u001d\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007¢\u0006\u0002\u0010\bJ\u0012\u0010\n\u001a\u00020\u0007H\u0016ø\u0001\u0000¢\u0006\u0004\b\u000b\u0010\fJ\u0013\u0010\r\u001a\u00020\u000e2\b\u0010\u000f\u001a\u0004\u0018\u00010\u0010H\u0096\u0002J\b\u0010\u0011\u001a\u00020\u0012H\u0016J\u001b\u0010\u0013\u001a\u00020\u00072\u0006\u0010\u000f\u001a\u00020\u0001H\u0096\u0002ø\u0001\u0000¢\u0006\u0004\b\u0014\u0010\u0015J\u0018\u0010\u0016\u001a\u00020\u00012\u0006\u0010\u0017\u001a\u00020\u0007H\u0096\u0002¢\u0006\u0004\b\u0018\u0010\u0019J\b\u0010\u001a\u001a\u00020\u001bH\u0016R\u0013\u0010\u0006\u001a\u00020\u0007X\u0082\u0004ø\u0001\u0000¢\u0006\u0004\n\u0002\u0010\tR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004¢\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b!¨\u0006\u001c"}, d2 = {"Lkotlin/time/AbstractLongTimeSource$LongTimeMark;", "Lkotlin/time/ComparableTimeMark;", "startedAt", "", "timeSource", "Lkotlin/time/AbstractLongTimeSource;", "offset", "Lkotlin/time/Duration;", "(JLkotlin/time/AbstractLongTimeSource;JLkotlin/jvm/internal/DefaultConstructorMarker;)V", "J", "elapsedNow", "elapsedNow-UwyO8pc", "()J", "equals", "", "other", "", com.android.server.oplus.osense.OsenseConstants.KEY_INTEGER_HASH, "", "minus", "minus-UwyO8pc", "(Lkotlin/time/ComparableTimeMark;)J", "plus", "duration", "plus-LRDsOJo", "(J)Lkotlin/time/ComparableTimeMark;", "toString", "", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
    private static final class LongTimeMark implements com.android.server.permission.jarjar.kotlin.time.ComparableTimeMark {
        private final long offset;
        private final long startedAt;
        private final com.android.server.permission.jarjar.kotlin.time.AbstractLongTimeSource timeSource;

        public /* synthetic */ LongTimeMark(long j, com.android.server.permission.jarjar.kotlin.time.AbstractLongTimeSource abstractLongTimeSource, long j2, com.android.server.permission.jarjar.kotlin.jvm.internal.DefaultConstructorMarker defaultConstructorMarker) {
            this(j, abstractLongTimeSource, j2);
        }

        private LongTimeMark(long startedAt, com.android.server.permission.jarjar.kotlin.time.AbstractLongTimeSource timeSource, long offset) {
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
        /* JADX INFO: renamed from: minus-LRDsOJo */
        public com.android.server.permission.jarjar.kotlin.time.ComparableTimeMark mo7405minusLRDsOJo(long duration) {
            return com.android.server.permission.jarjar.kotlin.time.ComparableTimeMark.DefaultImpls.m7409minusLRDsOJo(this, duration);
        }

        @Override // com.android.server.permission.jarjar.kotlin.time.TimeMark
        /* JADX INFO: renamed from: elapsedNow-UwyO8pc */
        public long mo7404elapsedNowUwyO8pc() {
            return com.android.server.permission.jarjar.kotlin.time.Duration.m7449minusLRDsOJo(com.android.server.permission.jarjar.kotlin.time.LongSaturatedMathKt.saturatingOriginsDiff(this.timeSource.adjustedRead(), this.startedAt, this.timeSource.getUnit()), this.offset);
        }

        @Override // com.android.server.permission.jarjar.kotlin.time.TimeMark
        /* JADX INFO: renamed from: plus-LRDsOJo */
        public com.android.server.permission.jarjar.kotlin.time.ComparableTimeMark mo7407plusLRDsOJo(long duration) {
            com.android.server.permission.jarjar.kotlin.time.DurationUnit unit = this.timeSource.getUnit();
            if (com.android.server.permission.jarjar.kotlin.time.Duration.m7446isInfiniteimpl(duration)) {
                return new com.android.server.permission.jarjar.kotlin.time.AbstractLongTimeSource.LongTimeMark(com.android.server.permission.jarjar.kotlin.time.LongSaturatedMathKt.m7543saturatingAddNuflL3o(this.startedAt, unit, duration), this.timeSource, com.android.server.permission.jarjar.kotlin.time.Duration.Companion.m7517getZEROUwyO8pc(), null);
            }
            long durationInUnit = com.android.server.permission.jarjar.kotlin.time.Duration.m7466truncateToUwyO8pc$kotlin_stdlib(duration, unit);
            long rest = com.android.server.permission.jarjar.kotlin.time.Duration.m7450plusLRDsOJo(com.android.server.permission.jarjar.kotlin.time.Duration.m7449minusLRDsOJo(duration, durationInUnit), this.offset);
            long sum = com.android.server.permission.jarjar.kotlin.time.LongSaturatedMathKt.m7543saturatingAddNuflL3o(this.startedAt, unit, durationInUnit);
            long restInUnit = com.android.server.permission.jarjar.kotlin.time.Duration.m7466truncateToUwyO8pc$kotlin_stdlib(rest, unit);
            long sum2 = com.android.server.permission.jarjar.kotlin.time.LongSaturatedMathKt.m7543saturatingAddNuflL3o(sum, unit, restInUnit);
            long restUnderUnit = com.android.server.permission.jarjar.kotlin.time.Duration.m7449minusLRDsOJo(rest, restInUnit);
            long restUnderUnitNs = com.android.server.permission.jarjar.kotlin.time.Duration.m7434getInWholeNanosecondsimpl(restUnderUnit);
            if (sum2 != 0 && restUnderUnitNs != 0 && (sum2 ^ restUnderUnitNs) < 0) {
                long correction = com.android.server.permission.jarjar.kotlin.time.DurationKt.toDuration(com.android.server.permission.jarjar.kotlin.math.MathKt.getSign(restUnderUnitNs), unit);
                sum2 = com.android.server.permission.jarjar.kotlin.time.LongSaturatedMathKt.m7543saturatingAddNuflL3o(sum2, unit, correction);
                restUnderUnit = com.android.server.permission.jarjar.kotlin.time.Duration.m7449minusLRDsOJo(restUnderUnit, correction);
            }
            long newValue = sum2;
            long newOffset = (((newValue - 1) | 1) > Long.MAX_VALUE ? 1 : (((newValue - 1) | 1) == Long.MAX_VALUE ? 0 : -1)) == 0 ? com.android.server.permission.jarjar.kotlin.time.Duration.Companion.m7517getZEROUwyO8pc() : restUnderUnit;
            return new com.android.server.permission.jarjar.kotlin.time.AbstractLongTimeSource.LongTimeMark(newValue, this.timeSource, newOffset, null);
        }

        @Override // com.android.server.permission.jarjar.kotlin.time.ComparableTimeMark
        /* JADX INFO: renamed from: minus-UwyO8pc */
        public long mo7406minusUwyO8pc(com.android.server.permission.jarjar.kotlin.time.ComparableTimeMark other) {
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(other, "other");
            if (!(other instanceof com.android.server.permission.jarjar.kotlin.time.AbstractLongTimeSource.LongTimeMark) || !com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.areEqual(this.timeSource, ((com.android.server.permission.jarjar.kotlin.time.AbstractLongTimeSource.LongTimeMark) other).timeSource)) {
                throw new java.lang.IllegalArgumentException("Subtracting or comparing time marks from different time sources is not possible: " + this + " and " + other);
            }
            long startedAtDiff = com.android.server.permission.jarjar.kotlin.time.LongSaturatedMathKt.saturatingOriginsDiff(this.startedAt, ((com.android.server.permission.jarjar.kotlin.time.AbstractLongTimeSource.LongTimeMark) other).startedAt, this.timeSource.getUnit());
            return com.android.server.permission.jarjar.kotlin.time.Duration.m7450plusLRDsOJo(startedAtDiff, com.android.server.permission.jarjar.kotlin.time.Duration.m7449minusLRDsOJo(this.offset, ((com.android.server.permission.jarjar.kotlin.time.AbstractLongTimeSource.LongTimeMark) other).offset));
        }

        @Override // com.android.server.permission.jarjar.kotlin.time.ComparableTimeMark
        public boolean equals(java.lang.Object other) {
            return (other instanceof com.android.server.permission.jarjar.kotlin.time.AbstractLongTimeSource.LongTimeMark) && com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.areEqual(this.timeSource, ((com.android.server.permission.jarjar.kotlin.time.AbstractLongTimeSource.LongTimeMark) other).timeSource) && com.android.server.permission.jarjar.kotlin.time.Duration.m7419equalsimpl0(mo7406minusUwyO8pc((com.android.server.permission.jarjar.kotlin.time.ComparableTimeMark) other), com.android.server.permission.jarjar.kotlin.time.Duration.Companion.m7517getZEROUwyO8pc());
        }

        @Override // com.android.server.permission.jarjar.kotlin.time.ComparableTimeMark
        public int hashCode() {
            return (com.android.server.permission.jarjar.kotlin.time.Duration.m7442hashCodeimpl(this.offset) * 37) + java.lang.Long.hashCode(this.startedAt);
        }

        public java.lang.String toString() {
            return "LongTimeMark(" + this.startedAt + com.android.server.permission.jarjar.kotlin.time.DurationUnitKt.shortName(this.timeSource.getUnit()) + " + " + ((java.lang.Object) com.android.server.permission.jarjar.kotlin.time.Duration.m7463toStringimpl(this.offset)) + ", " + this.timeSource + ')';
        }
    }

    @Override // com.android.server.permission.jarjar.kotlin.time.TimeSource
    public com.android.server.permission.jarjar.kotlin.time.ComparableTimeMark markNow() {
        return new com.android.server.permission.jarjar.kotlin.time.AbstractLongTimeSource.LongTimeMark(adjustedRead(), this, com.android.server.permission.jarjar.kotlin.time.Duration.Companion.m7517getZEROUwyO8pc(), null);
    }
}
