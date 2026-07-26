package kotlin.time;

/* JADX INFO: compiled from: TimeSources.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\t\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0003\b'\u0018\u00002\u00020\u0001:\u0001\u0011B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\b\u0010\r\u001a\u00020\bH\u0002J\b\u0010\u000e\u001a\u00020\u000fH\u0016J\b\u0010\u0010\u001a\u00020\bH$R\u0014\u0010\u0002\u001a\u00020\u0003X\u0084\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006R\u001b\u0010\u0007\u001a\u00020\b8BX\u0082\u0084\u0002¢\u0006\f\n\u0004\b\u000b\u0010\f\u001a\u0004\b\t\u0010\n¨\u0006\u0012"}, d2 = {"Lkotlin/time/AbstractLongTimeSource;", "Lkotlin/time/TimeSource$WithComparableMarks;", "unit", "Lkotlin/time/DurationUnit;", "(Lkotlin/time/DurationUnit;)V", "getUnit", "()Lkotlin/time/DurationUnit;", "zero", "", "getZero", "()J", "zero$delegate", "Lkotlin/Lazy;", "adjustedRead", "markNow", "Lkotlin/time/ComparableTimeMark;", "read", "LongTimeMark", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
public abstract class AbstractLongTimeSource implements kotlin.time.TimeSource.WithComparableMarks {
    private final kotlin.time.DurationUnit unit;

    /* JADX INFO: renamed from: zero$delegate, reason: from kotlin metadata */
    private final kotlin.Lazy zero;

    protected abstract long read();

    public AbstractLongTimeSource(kotlin.time.DurationUnit unit) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(unit, "unit");
        this.unit = unit;
        this.zero = kotlin.LazyKt.lazy(new kotlin.jvm.functions.Function0<java.lang.Long>() { // from class: kotlin.time.AbstractLongTimeSource$zero$2
            {
                super(0);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // kotlin.jvm.functions.Function0
            public final java.lang.Long invoke() {
                return java.lang.Long.valueOf(this.this$0.read());
            }
        });
    }

    protected final kotlin.time.DurationUnit getUnit() {
        return this.unit;
    }

    private final long getZero() {
        return ((java.lang.Number) this.zero.getValue()).longValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final long adjustedRead() {
        return read() - getZero();
    }

    /* JADX INFO: compiled from: TimeSources.kt */
    @kotlin.Metadata(d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\b\n\u0002\u0010\u000e\n\u0000\b\u0002\u0018\u00002\u00020\u0001B\u001d\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007¢\u0006\u0002\u0010\bJ\u0012\u0010\n\u001a\u00020\u0007H\u0016ø\u0001\u0000¢\u0006\u0004\b\u000b\u0010\fJ\u0013\u0010\r\u001a\u00020\u000e2\b\u0010\u000f\u001a\u0004\u0018\u00010\u0010H\u0096\u0002J\b\u0010\u0011\u001a\u00020\u0012H\u0016J\u001b\u0010\u0013\u001a\u00020\u00072\u0006\u0010\u000f\u001a\u00020\u0001H\u0096\u0002ø\u0001\u0000¢\u0006\u0004\b\u0014\u0010\u0015J\u0018\u0010\u0016\u001a\u00020\u00012\u0006\u0010\u0017\u001a\u00020\u0007H\u0096\u0002¢\u0006\u0004\b\u0018\u0010\u0019J\b\u0010\u001a\u001a\u00020\u001bH\u0016R\u0013\u0010\u0006\u001a\u00020\u0007X\u0082\u0004ø\u0001\u0000¢\u0006\u0004\n\u0002\u0010\tR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004¢\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b!¨\u0006\u001c"}, d2 = {"Lkotlin/time/AbstractLongTimeSource$LongTimeMark;", "Lkotlin/time/ComparableTimeMark;", "startedAt", "", "timeSource", "Lkotlin/time/AbstractLongTimeSource;", "offset", "Lkotlin/time/Duration;", "(JLkotlin/time/AbstractLongTimeSource;JLkotlin/jvm/internal/DefaultConstructorMarker;)V", "J", "elapsedNow", "elapsedNow-UwyO8pc", "()J", "equals", "", "other", "", com.android.server.oplus.osense.OsenseConstants.KEY_INTEGER_HASH, "", "minus", "minus-UwyO8pc", "(Lkotlin/time/ComparableTimeMark;)J", "plus", "duration", "plus-LRDsOJo", "(J)Lkotlin/time/ComparableTimeMark;", "toString", "", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
    private static final class LongTimeMark implements kotlin.time.ComparableTimeMark {
        private final long offset;
        private final long startedAt;
        private final kotlin.time.AbstractLongTimeSource timeSource;

        public /* synthetic */ LongTimeMark(long j, kotlin.time.AbstractLongTimeSource abstractLongTimeSource, long j2, kotlin.jvm.internal.DefaultConstructorMarker defaultConstructorMarker) {
            this(j, abstractLongTimeSource, j2);
        }

        private LongTimeMark(long startedAt, kotlin.time.AbstractLongTimeSource timeSource, long offset) {
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(timeSource, "timeSource");
            this.startedAt = startedAt;
            this.timeSource = timeSource;
            this.offset = offset;
        }

        @Override // java.lang.Comparable
        public int compareTo(kotlin.time.ComparableTimeMark other) {
            return kotlin.time.ComparableTimeMark.DefaultImpls.compareTo(this, other);
        }

        @Override // kotlin.time.TimeMark
        public boolean hasNotPassedNow() {
            return kotlin.time.ComparableTimeMark.DefaultImpls.hasNotPassedNow(this);
        }

        @Override // kotlin.time.TimeMark
        public boolean hasPassedNow() {
            return kotlin.time.ComparableTimeMark.DefaultImpls.hasPassedNow(this);
        }

        @Override // kotlin.time.TimeMark
        /* JADX INFO: renamed from: minus-LRDsOJo */
        public kotlin.time.ComparableTimeMark mo12624minusLRDsOJo(long duration) {
            return kotlin.time.ComparableTimeMark.DefaultImpls.m12628minusLRDsOJo(this, duration);
        }

        @Override // kotlin.time.TimeMark
        /* JADX INFO: renamed from: elapsedNow-UwyO8pc */
        public long mo12623elapsedNowUwyO8pc() {
            return kotlin.time.Duration.m12668minusLRDsOJo(kotlin.time.LongSaturatedMathKt.saturatingOriginsDiff(this.timeSource.adjustedRead(), this.startedAt, this.timeSource.getUnit()), this.offset);
        }

        @Override // kotlin.time.TimeMark
        /* JADX INFO: renamed from: plus-LRDsOJo */
        public kotlin.time.ComparableTimeMark mo12626plusLRDsOJo(long duration) {
            kotlin.time.DurationUnit unit = this.timeSource.getUnit();
            if (kotlin.time.Duration.m12665isInfiniteimpl(duration)) {
                return new kotlin.time.AbstractLongTimeSource.LongTimeMark(kotlin.time.LongSaturatedMathKt.m12762saturatingAddNuflL3o(this.startedAt, unit, duration), this.timeSource, kotlin.time.Duration.INSTANCE.m12736getZEROUwyO8pc(), null);
            }
            long durationInUnit = kotlin.time.Duration.m12685truncateToUwyO8pc$kotlin_stdlib(duration, unit);
            long rest = kotlin.time.Duration.m12669plusLRDsOJo(kotlin.time.Duration.m12668minusLRDsOJo(duration, durationInUnit), this.offset);
            long sum = kotlin.time.LongSaturatedMathKt.m12762saturatingAddNuflL3o(this.startedAt, unit, durationInUnit);
            long restInUnit = kotlin.time.Duration.m12685truncateToUwyO8pc$kotlin_stdlib(rest, unit);
            long sum2 = kotlin.time.LongSaturatedMathKt.m12762saturatingAddNuflL3o(sum, unit, restInUnit);
            long restUnderUnit = kotlin.time.Duration.m12668minusLRDsOJo(rest, restInUnit);
            long restUnderUnitNs = kotlin.time.Duration.m12653getInWholeNanosecondsimpl(restUnderUnit);
            if (sum2 != 0 && restUnderUnitNs != 0 && (sum2 ^ restUnderUnitNs) < 0) {
                long correction = kotlin.time.DurationKt.toDuration(kotlin.math.MathKt.getSign(restUnderUnitNs), unit);
                sum2 = kotlin.time.LongSaturatedMathKt.m12762saturatingAddNuflL3o(sum2, unit, correction);
                restUnderUnit = kotlin.time.Duration.m12668minusLRDsOJo(restUnderUnit, correction);
            }
            long newValue = sum2;
            long newOffset = (((newValue - 1) | 1) > Long.MAX_VALUE ? 1 : (((newValue - 1) | 1) == Long.MAX_VALUE ? 0 : -1)) == 0 ? kotlin.time.Duration.INSTANCE.m12736getZEROUwyO8pc() : restUnderUnit;
            return new kotlin.time.AbstractLongTimeSource.LongTimeMark(newValue, this.timeSource, newOffset, null);
        }

        @Override // kotlin.time.ComparableTimeMark
        /* JADX INFO: renamed from: minus-UwyO8pc */
        public long mo12625minusUwyO8pc(kotlin.time.ComparableTimeMark other) {
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(other, "other");
            if (!(other instanceof kotlin.time.AbstractLongTimeSource.LongTimeMark) || !kotlin.jvm.internal.Intrinsics.areEqual(this.timeSource, ((kotlin.time.AbstractLongTimeSource.LongTimeMark) other).timeSource)) {
                throw new java.lang.IllegalArgumentException("Subtracting or comparing time marks from different time sources is not possible: " + this + " and " + other);
            }
            long startedAtDiff = kotlin.time.LongSaturatedMathKt.saturatingOriginsDiff(this.startedAt, ((kotlin.time.AbstractLongTimeSource.LongTimeMark) other).startedAt, this.timeSource.getUnit());
            return kotlin.time.Duration.m12669plusLRDsOJo(startedAtDiff, kotlin.time.Duration.m12668minusLRDsOJo(this.offset, ((kotlin.time.AbstractLongTimeSource.LongTimeMark) other).offset));
        }

        @Override // kotlin.time.ComparableTimeMark
        public boolean equals(java.lang.Object other) {
            return (other instanceof kotlin.time.AbstractLongTimeSource.LongTimeMark) && kotlin.jvm.internal.Intrinsics.areEqual(this.timeSource, ((kotlin.time.AbstractLongTimeSource.LongTimeMark) other).timeSource) && kotlin.time.Duration.m12638equalsimpl0(mo12625minusUwyO8pc((kotlin.time.ComparableTimeMark) other), kotlin.time.Duration.INSTANCE.m12736getZEROUwyO8pc());
        }

        @Override // kotlin.time.ComparableTimeMark
        public int hashCode() {
            return (kotlin.time.Duration.m12661hashCodeimpl(this.offset) * 37) + java.lang.Long.hashCode(this.startedAt);
        }

        public java.lang.String toString() {
            return "LongTimeMark(" + this.startedAt + kotlin.time.DurationUnitKt.shortName(this.timeSource.getUnit()) + " + " + ((java.lang.Object) kotlin.time.Duration.m12682toStringimpl(this.offset)) + ", " + this.timeSource + ')';
        }
    }

    @Override // kotlin.time.TimeSource
    public kotlin.time.ComparableTimeMark markNow() {
        return new kotlin.time.AbstractLongTimeSource.LongTimeMark(adjustedRead(), this, kotlin.time.Duration.INSTANCE.m12736getZEROUwyO8pc(), null);
    }
}
