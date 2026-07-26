package kotlin.time;

/* JADX INFO: compiled from: TimeSources.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\b\u0007\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u0017\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0002¢\u0006\u0004\b\t\u0010\nJ\u0018\u0010\u000b\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0086\u0002¢\u0006\u0004\b\f\u0010\nJ\b\u0010\r\u001a\u00020\u0004H\u0014R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006\u000e"}, d2 = {"Lkotlin/time/TestTimeSource;", "Lkotlin/time/AbstractLongTimeSource;", "()V", "reading", "", "overflow", "", "duration", "Lkotlin/time/Duration;", "overflow-LRDsOJo", "(J)V", "plusAssign", "plusAssign-LRDsOJo", "read", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class TestTimeSource extends kotlin.time.AbstractLongTimeSource {
    private long reading;

    public TestTimeSource() {
        super(kotlin.time.DurationUnit.NANOSECONDS);
        markNow();
    }

    @Override // kotlin.time.AbstractLongTimeSource
    /* JADX INFO: renamed from: read, reason: from getter */
    protected long getReading() {
        return this.reading;
    }

    /* JADX INFO: renamed from: plusAssign-LRDsOJo, reason: not valid java name */
    public final void m12769plusAssignLRDsOJo(long duration) {
        long longDelta = kotlin.time.Duration.m12679toLongimpl(duration, getUnit());
        int $i$f$isSaturated = ((longDelta - 1) | 1) == Long.MAX_VALUE ? 1 : 0;
        if ($i$f$isSaturated == 0) {
            long newReading = this.reading + longDelta;
            if ((this.reading ^ longDelta) >= 0 && (this.reading ^ newReading) < 0) {
                m12768overflowLRDsOJo(duration);
            }
            this.reading = newReading;
            return;
        }
        long half = kotlin.time.Duration.m12636divUwyO8pc(duration, 2);
        long $this$isSaturated$iv = kotlin.time.Duration.m12679toLongimpl(half, getUnit());
        if (!((($this$isSaturated$iv - 1) | 1) == Long.MAX_VALUE)) {
            long readingBefore = this.reading;
            try {
                m12769plusAssignLRDsOJo(half);
                m12769plusAssignLRDsOJo(kotlin.time.Duration.m12668minusLRDsOJo(duration, half));
                return;
            } catch (java.lang.IllegalStateException e) {
                this.reading = readingBefore;
                throw e;
            }
        }
        m12768overflowLRDsOJo(duration);
    }

    /* JADX INFO: renamed from: overflow-LRDsOJo, reason: not valid java name */
    private final void m12768overflowLRDsOJo(long duration) {
        throw new java.lang.IllegalStateException("TestTimeSource will overflow if its reading " + this.reading + kotlin.time.DurationUnitKt.shortName(getUnit()) + " is advanced by " + ((java.lang.Object) kotlin.time.Duration.m12682toStringimpl(duration)) + '.');
    }
}
