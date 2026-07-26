package com.android.server.permission.jarjar.kotlin.time;

/* JADX INFO: compiled from: TimeSource.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\r\b\u0002\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0001\u0012\u0006\u0010\u0003\u001a\u00020\u0004¢\u0006\u0002\u0010\u0005J\u0012\u0010\u000b\u001a\u00020\u0004H\u0016ø\u0001\u0000¢\u0006\u0004\b\f\u0010\u0007J\u0018\u0010\r\u001a\u00020\u00012\u0006\u0010\u000e\u001a\u00020\u0004H\u0096\u0002¢\u0006\u0004\b\u000f\u0010\u0010R\u0016\u0010\u0003\u001a\u00020\u0004ø\u0001\u0000¢\u0006\n\n\u0002\u0010\b\u001a\u0004\b\u0006\u0010\u0007R\u0011\u0010\u0002\u001a\u00020\u0001¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\n\u0082\u0002\u0004\n\u0002\b!¨\u0006\u0011"}, d2 = {"Lkotlin/time/AdjustedTimeMark;", "Lkotlin/time/TimeMark;", "mark", "adjustment", "Lkotlin/time/Duration;", "(Lkotlin/time/TimeMark;JLkotlin/jvm/internal/DefaultConstructorMarker;)V", "getAdjustment-UwyO8pc", "()J", "J", "getMark", "()Lkotlin/time/TimeMark;", "elapsedNow", "elapsedNow-UwyO8pc", "plus", "duration", "plus-LRDsOJo", "(J)Lkotlin/time/TimeMark;", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
final class AdjustedTimeMark implements com.android.server.permission.jarjar.kotlin.time.TimeMark {
    private final long adjustment;
    private final com.android.server.permission.jarjar.kotlin.time.TimeMark mark;

    public /* synthetic */ AdjustedTimeMark(com.android.server.permission.jarjar.kotlin.time.TimeMark timeMark, long j, com.android.server.permission.jarjar.kotlin.jvm.internal.DefaultConstructorMarker defaultConstructorMarker) {
        this(timeMark, j);
    }

    private AdjustedTimeMark(com.android.server.permission.jarjar.kotlin.time.TimeMark mark, long adjustment) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(mark, "mark");
        this.mark = mark;
        this.adjustment = adjustment;
    }

    /* JADX INFO: renamed from: getAdjustment-UwyO8pc, reason: not valid java name */
    public final long m7408getAdjustmentUwyO8pc() {
        return this.adjustment;
    }

    public final com.android.server.permission.jarjar.kotlin.time.TimeMark getMark() {
        return this.mark;
    }

    @Override // com.android.server.permission.jarjar.kotlin.time.TimeMark
    public boolean hasNotPassedNow() {
        return com.android.server.permission.jarjar.kotlin.time.TimeMark.DefaultImpls.hasNotPassedNow(this);
    }

    @Override // com.android.server.permission.jarjar.kotlin.time.TimeMark
    public boolean hasPassedNow() {
        return com.android.server.permission.jarjar.kotlin.time.TimeMark.DefaultImpls.hasPassedNow(this);
    }

    @Override // com.android.server.permission.jarjar.kotlin.time.TimeMark
    /* JADX INFO: renamed from: minus-LRDsOJo */
    public com.android.server.permission.jarjar.kotlin.time.TimeMark mo7405minusLRDsOJo(long duration) {
        return com.android.server.permission.jarjar.kotlin.time.TimeMark.DefaultImpls.m7551minusLRDsOJo(this, duration);
    }

    @Override // com.android.server.permission.jarjar.kotlin.time.TimeMark
    /* JADX INFO: renamed from: elapsedNow-UwyO8pc */
    public long mo7404elapsedNowUwyO8pc() {
        return com.android.server.permission.jarjar.kotlin.time.Duration.m7449minusLRDsOJo(this.mark.mo7404elapsedNowUwyO8pc(), this.adjustment);
    }

    @Override // com.android.server.permission.jarjar.kotlin.time.TimeMark
    /* JADX INFO: renamed from: plus-LRDsOJo */
    public com.android.server.permission.jarjar.kotlin.time.TimeMark mo7407plusLRDsOJo(long duration) {
        return new com.android.server.permission.jarjar.kotlin.time.AdjustedTimeMark(this.mark, com.android.server.permission.jarjar.kotlin.time.Duration.m7450plusLRDsOJo(this.adjustment, duration), null);
    }
}
