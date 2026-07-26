package com.android.server.permission.jarjar.kotlin.time;

/* JADX INFO: compiled from: TimeSources.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\b\u0007\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u0017\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0002¢\u0006\u0004\b\t\u0010\nJ\u0018\u0010\u000b\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0086\u0002¢\u0006\u0004\b\f\u0010\nJ\b\u0010\r\u001a\u00020\u0004H\u0014R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006\u000e"}, d2 = {"Lkotlin/time/TestTimeSource;", "Lkotlin/time/AbstractLongTimeSource;", "()V", "reading", "", "overflow", "", "duration", "Lkotlin/time/Duration;", "overflow-LRDsOJo", "(J)V", "plusAssign", "plusAssign-LRDsOJo", "read", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class TestTimeSource extends com.android.server.permission.jarjar.kotlin.time.AbstractLongTimeSource {
    private long reading;

    public TestTimeSource() {
        super(com.android.server.permission.jarjar.kotlin.time.DurationUnit.NANOSECONDS);
        markNow();
    }

    @Override // com.android.server.permission.jarjar.kotlin.time.AbstractLongTimeSource
    protected long read() {
        return this.reading;
    }

    /* JADX INFO: renamed from: plusAssign-LRDsOJo, reason: not valid java name */
    public final void m7550plusAssignLRDsOJo(long duration) {
        long longDelta = com.android.server.permission.jarjar.kotlin.time.Duration.m7460toLongimpl(duration, getUnit());
        int $i$f$isSaturated = ((longDelta - 1) | 1) == Long.MAX_VALUE ? 1 : 0;
        if ($i$f$isSaturated == 0) {
            long newReading = this.reading + longDelta;
            if ((this.reading ^ longDelta) >= 0 && (this.reading ^ newReading) < 0) {
                m7549overflowLRDsOJo(duration);
            }
            this.reading = newReading;
            return;
        }
        long half = com.android.server.permission.jarjar.kotlin.time.Duration.m7417divUwyO8pc(duration, 2);
        long $this$isSaturated$iv = com.android.server.permission.jarjar.kotlin.time.Duration.m7460toLongimpl(half, getUnit());
        if (!((($this$isSaturated$iv - 1) | 1) == Long.MAX_VALUE)) {
            long readingBefore = this.reading;
            try {
                m7550plusAssignLRDsOJo(half);
                m7550plusAssignLRDsOJo(com.android.server.permission.jarjar.kotlin.time.Duration.m7449minusLRDsOJo(duration, half));
                return;
            } catch (java.lang.IllegalStateException e) {
                this.reading = readingBefore;
                throw e;
            }
        }
        m7549overflowLRDsOJo(duration);
    }

    /* JADX INFO: renamed from: overflow-LRDsOJo, reason: not valid java name */
    private final void m7549overflowLRDsOJo(long duration) {
        throw new java.lang.IllegalStateException("TestTimeSource will overflow if its reading " + this.reading + com.android.server.permission.jarjar.kotlin.time.DurationUnitKt.shortName(getUnit()) + " is advanced by " + ((java.lang.Object) com.android.server.permission.jarjar.kotlin.time.Duration.m7463toStringimpl(duration)) + '.');
    }
}
