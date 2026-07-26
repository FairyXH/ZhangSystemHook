package kotlin.time;

/* JADX INFO: compiled from: TimeSources.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Deprecated(message = "Using AbstractDoubleTimeSource is no longer recommended, use AbstractLongTimeSource instead.")
@kotlin.Metadata(d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0002\b'\u0018\u00002\u00020\u0001:\u0001\u000bB\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\b\u0010\u0007\u001a\u00020\bH\u0016J\b\u0010\t\u001a\u00020\nH$R\u0014\u0010\u0002\u001a\u00020\u0003X\u0084\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006¨\u0006\f"}, d2 = {"Lkotlin/time/AbstractDoubleTimeSource;", "Lkotlin/time/TimeSource$WithComparableMarks;", "unit", "Lkotlin/time/DurationUnit;", "(Lkotlin/time/DurationUnit;)V", "getUnit", "()Lkotlin/time/DurationUnit;", "markNow", "Lkotlin/time/ComparableTimeMark;", "read", "", "DoubleTimeMark", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
public abstract class AbstractDoubleTimeSource implements kotlin.time.TimeSource.WithComparableMarks {
    private final kotlin.time.DurationUnit unit;

    protected abstract double read();

    public AbstractDoubleTimeSource(kotlin.time.DurationUnit unit) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(unit, "unit");
        this.unit = unit;
    }

    protected final kotlin.time.DurationUnit getUnit() {
        return this.unit;
    }

    /* JADX INFO: compiled from: TimeSources.kt */
    @kotlin.Metadata(d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0006\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\b\n\u0002\u0010\u000e\n\u0000\b\u0002\u0018\u00002\u00020\u0001B\u001d\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007¢\u0006\u0002\u0010\bJ\u0012\u0010\n\u001a\u00020\u0007H\u0016ø\u0001\u0000¢\u0006\u0004\b\u000b\u0010\fJ\u0013\u0010\r\u001a\u00020\u000e2\b\u0010\u000f\u001a\u0004\u0018\u00010\u0010H\u0096\u0002J\b\u0010\u0011\u001a\u00020\u0012H\u0016J\u001b\u0010\u0013\u001a\u00020\u00072\u0006\u0010\u000f\u001a\u00020\u0001H\u0096\u0002ø\u0001\u0000¢\u0006\u0004\b\u0014\u0010\u0015J\u0018\u0010\u0016\u001a\u00020\u00012\u0006\u0010\u0017\u001a\u00020\u0007H\u0096\u0002¢\u0006\u0004\b\u0018\u0010\u0019J\b\u0010\u001a\u001a\u00020\u001bH\u0016R\u0013\u0010\u0006\u001a\u00020\u0007X\u0082\u0004ø\u0001\u0000¢\u0006\u0004\n\u0002\u0010\tR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004¢\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b!¨\u0006\u001c"}, d2 = {"Lkotlin/time/AbstractDoubleTimeSource$DoubleTimeMark;", "Lkotlin/time/ComparableTimeMark;", "startedAt", "", "timeSource", "Lkotlin/time/AbstractDoubleTimeSource;", "offset", "Lkotlin/time/Duration;", "(DLkotlin/time/AbstractDoubleTimeSource;JLkotlin/jvm/internal/DefaultConstructorMarker;)V", "J", "elapsedNow", "elapsedNow-UwyO8pc", "()J", "equals", "", "other", "", com.android.server.oplus.osense.OsenseConstants.KEY_INTEGER_HASH, "", "minus", "minus-UwyO8pc", "(Lkotlin/time/ComparableTimeMark;)J", "plus", "duration", "plus-LRDsOJo", "(J)Lkotlin/time/ComparableTimeMark;", "toString", "", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
    private static final class DoubleTimeMark implements kotlin.time.ComparableTimeMark {
        private final long offset;
        private final double startedAt;
        private final kotlin.time.AbstractDoubleTimeSource timeSource;

        public /* synthetic */ DoubleTimeMark(double d, kotlin.time.AbstractDoubleTimeSource abstractDoubleTimeSource, long j, kotlin.jvm.internal.DefaultConstructorMarker defaultConstructorMarker) {
            this(d, abstractDoubleTimeSource, j);
        }

        private DoubleTimeMark(double startedAt, kotlin.time.AbstractDoubleTimeSource timeSource, long offset) {
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
        /* JADX INFO: renamed from: minus-LRDsOJo, reason: not valid java name */
        public kotlin.time.ComparableTimeMark mo12624minusLRDsOJo(long duration) {
            return kotlin.time.ComparableTimeMark.DefaultImpls.m12628minusLRDsOJo(this, duration);
        }

        @Override // kotlin.time.TimeMark
        /* JADX INFO: renamed from: elapsedNow-UwyO8pc, reason: not valid java name */
        public long mo12623elapsedNowUwyO8pc() {
            return kotlin.time.Duration.m12668minusLRDsOJo(kotlin.time.DurationKt.toDuration(this.timeSource.read() - this.startedAt, this.timeSource.getUnit()), this.offset);
        }

        @Override // kotlin.time.TimeMark
        /* JADX INFO: renamed from: plus-LRDsOJo, reason: not valid java name */
        public kotlin.time.ComparableTimeMark mo12626plusLRDsOJo(long duration) {
            return new kotlin.time.AbstractDoubleTimeSource.DoubleTimeMark(this.startedAt, this.timeSource, kotlin.time.Duration.m12669plusLRDsOJo(this.offset, duration), null);
        }

        @Override // kotlin.time.ComparableTimeMark
        /* JADX INFO: renamed from: minus-UwyO8pc, reason: not valid java name */
        public long mo12625minusUwyO8pc(kotlin.time.ComparableTimeMark other) {
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(other, "other");
            if (!(other instanceof kotlin.time.AbstractDoubleTimeSource.DoubleTimeMark) || !kotlin.jvm.internal.Intrinsics.areEqual(this.timeSource, ((kotlin.time.AbstractDoubleTimeSource.DoubleTimeMark) other).timeSource)) {
                throw new java.lang.IllegalArgumentException("Subtracting or comparing time marks from different time sources is not possible: " + this + " and " + other);
            }
            if (kotlin.time.Duration.m12638equalsimpl0(this.offset, ((kotlin.time.AbstractDoubleTimeSource.DoubleTimeMark) other).offset) && kotlin.time.Duration.m12665isInfiniteimpl(this.offset)) {
                return kotlin.time.Duration.INSTANCE.m12736getZEROUwyO8pc();
            }
            long offsetDiff = kotlin.time.Duration.m12668minusLRDsOJo(this.offset, ((kotlin.time.AbstractDoubleTimeSource.DoubleTimeMark) other).offset);
            long startedAtDiff = kotlin.time.DurationKt.toDuration(this.startedAt - ((kotlin.time.AbstractDoubleTimeSource.DoubleTimeMark) other).startedAt, this.timeSource.getUnit());
            return kotlin.time.Duration.m12638equalsimpl0(startedAtDiff, kotlin.time.Duration.m12686unaryMinusUwyO8pc(offsetDiff)) ? kotlin.time.Duration.INSTANCE.m12736getZEROUwyO8pc() : kotlin.time.Duration.m12669plusLRDsOJo(startedAtDiff, offsetDiff);
        }

        @Override // kotlin.time.ComparableTimeMark
        public boolean equals(java.lang.Object other) {
            return (other instanceof kotlin.time.AbstractDoubleTimeSource.DoubleTimeMark) && kotlin.jvm.internal.Intrinsics.areEqual(this.timeSource, ((kotlin.time.AbstractDoubleTimeSource.DoubleTimeMark) other).timeSource) && kotlin.time.Duration.m12638equalsimpl0(mo12625minusUwyO8pc((kotlin.time.ComparableTimeMark) other), kotlin.time.Duration.INSTANCE.m12736getZEROUwyO8pc());
        }

        @Override // kotlin.time.ComparableTimeMark
        public int hashCode() {
            return kotlin.time.Duration.m12661hashCodeimpl(kotlin.time.Duration.m12669plusLRDsOJo(kotlin.time.DurationKt.toDuration(this.startedAt, this.timeSource.getUnit()), this.offset));
        }

        public java.lang.String toString() {
            return "DoubleTimeMark(" + this.startedAt + kotlin.time.DurationUnitKt.shortName(this.timeSource.getUnit()) + " + " + ((java.lang.Object) kotlin.time.Duration.m12682toStringimpl(this.offset)) + ", " + this.timeSource + ')';
        }
    }

    @Override // kotlin.time.TimeSource
    public kotlin.time.ComparableTimeMark markNow() {
        return new kotlin.time.AbstractDoubleTimeSource.DoubleTimeMark(read(), this, kotlin.time.Duration.INSTANCE.m12736getZEROUwyO8pc(), null);
    }
}
