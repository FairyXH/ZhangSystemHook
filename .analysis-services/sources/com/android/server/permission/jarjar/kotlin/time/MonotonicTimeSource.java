package com.android.server.permission.jarjar.kotlin.time;

/* JADX INFO: compiled from: MonoTimeSource.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u000e\n\u0002\u0010\u000e\n\u0000\bÁ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u001d\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u00062\u0006\u0010\b\u001a\u00020\t¢\u0006\u0004\b\n\u0010\u000bJ\u001d\u0010\f\u001a\u00020\t2\u0006\u0010\r\u001a\u00020\u00062\u0006\u0010\u000e\u001a\u00020\u0006¢\u0006\u0004\b\u000f\u0010\u000bJ\u0015\u0010\u0010\u001a\u00020\t2\u0006\u0010\u0007\u001a\u00020\u0006¢\u0006\u0004\b\u0011\u0010\u0012J\u0012\u0010\u0013\u001a\u00020\u0006H\u0016ø\u0001\u0000¢\u0006\u0004\b\u0014\u0010\u0015J\b\u0010\u0016\u001a\u00020\u0004H\u0002J\b\u0010\u0017\u001a\u00020\u0018H\u0016R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b!¨\u0006\u0019"}, d2 = {"Lkotlin/time/MonotonicTimeSource;", "Lkotlin/time/TimeSource$WithComparableMarks;", "()V", "zero", "", "adjustReading", "Lkotlin/time/TimeSource$Monotonic$ValueTimeMark;", "timeMark", "duration", "Lkotlin/time/Duration;", "adjustReading-6QKq23U", "(JJ)J", "differenceBetween", "one", "another", "differenceBetween-fRLX17w", "elapsedFrom", "elapsedFrom-6eNON_k", "(J)J", "markNow", "markNow-z9LOYto", "()J", "read", "toString", "", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class MonotonicTimeSource implements com.android.server.permission.jarjar.kotlin.time.TimeSource.WithComparableMarks {
    public static final com.android.server.permission.jarjar.kotlin.time.MonotonicTimeSource INSTANCE = new com.android.server.permission.jarjar.kotlin.time.MonotonicTimeSource();
    private static final long zero = java.lang.System.nanoTime();

    private MonotonicTimeSource() {
    }

    @Override // com.android.server.permission.jarjar.kotlin.time.TimeSource.WithComparableMarks, com.android.server.permission.jarjar.kotlin.time.TimeSource
    public /* bridge */ /* synthetic */ com.android.server.permission.jarjar.kotlin.time.ComparableTimeMark markNow() {
        return com.android.server.permission.jarjar.kotlin.time.TimeSource.Monotonic.ValueTimeMark.m7554boximpl(m7548markNowz9LOYto());
    }

    @Override // com.android.server.permission.jarjar.kotlin.time.TimeSource
    public /* bridge */ /* synthetic */ com.android.server.permission.jarjar.kotlin.time.TimeMark markNow() {
        return com.android.server.permission.jarjar.kotlin.time.TimeSource.Monotonic.ValueTimeMark.m7554boximpl(m7548markNowz9LOYto());
    }

    private final long read() {
        return java.lang.System.nanoTime() - zero;
    }

    public java.lang.String toString() {
        return "TimeSource(System.nanoTime())";
    }

    /* JADX INFO: renamed from: markNow-z9LOYto, reason: not valid java name */
    public long m7548markNowz9LOYto() {
        return com.android.server.permission.jarjar.kotlin.time.TimeSource.Monotonic.ValueTimeMark.m7557constructorimpl(read());
    }

    /* JADX INFO: renamed from: elapsedFrom-6eNON_k, reason: not valid java name */
    public final long m7547elapsedFrom6eNON_k(long timeMark) {
        return com.android.server.permission.jarjar.kotlin.time.LongSaturatedMathKt.saturatingDiff(read(), timeMark, com.android.server.permission.jarjar.kotlin.time.DurationUnit.NANOSECONDS);
    }

    /* JADX INFO: renamed from: differenceBetween-fRLX17w, reason: not valid java name */
    public final long m7546differenceBetweenfRLX17w(long one, long another) {
        return com.android.server.permission.jarjar.kotlin.time.LongSaturatedMathKt.saturatingOriginsDiff(one, another, com.android.server.permission.jarjar.kotlin.time.DurationUnit.NANOSECONDS);
    }

    /* JADX INFO: renamed from: adjustReading-6QKq23U, reason: not valid java name */
    public final long m7545adjustReading6QKq23U(long timeMark, long duration) {
        return com.android.server.permission.jarjar.kotlin.time.TimeSource.Monotonic.ValueTimeMark.m7557constructorimpl(com.android.server.permission.jarjar.kotlin.time.LongSaturatedMathKt.m7543saturatingAddNuflL3o(timeMark, com.android.server.permission.jarjar.kotlin.time.DurationUnit.NANOSECONDS, duration));
    }
}
