package com.android.server.permission.jarjar.kotlin.time;

/* JADX INFO: compiled from: TimeSource.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\b\bg\u0018\u00002\u00020\u0001J\u0012\u0010\u0002\u001a\u00020\u0003H&ø\u0001\u0000¢\u0006\u0004\b\u0004\u0010\u0005J\b\u0010\u0006\u001a\u00020\u0007H\u0016J\b\u0010\b\u001a\u00020\u0007H\u0016J\u0018\u0010\t\u001a\u00020\u00002\u0006\u0010\n\u001a\u00020\u0003H\u0096\u0002¢\u0006\u0004\b\u000b\u0010\fJ\u0018\u0010\r\u001a\u00020\u00002\u0006\u0010\n\u001a\u00020\u0003H\u0096\u0002¢\u0006\u0004\b\u000e\u0010\f\u0082\u0002\u0004\n\u0002\b!¨\u0006\u000f"}, d2 = {"Lkotlin/time/TimeMark;", "", "elapsedNow", "Lkotlin/time/Duration;", "elapsedNow-UwyO8pc", "()J", "hasNotPassedNow", "", "hasPassedNow", "minus", "duration", "minus-LRDsOJo", "(J)Lkotlin/time/TimeMark;", "plus", "plus-LRDsOJo", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
public interface TimeMark {
    /* JADX INFO: renamed from: elapsedNow-UwyO8pc */
    long mo7404elapsedNowUwyO8pc();

    boolean hasNotPassedNow();

    boolean hasPassedNow();

    /* JADX INFO: renamed from: minus-LRDsOJo */
    com.android.server.permission.jarjar.kotlin.time.TimeMark mo7405minusLRDsOJo(long j);

    /* JADX INFO: renamed from: plus-LRDsOJo */
    com.android.server.permission.jarjar.kotlin.time.TimeMark mo7407plusLRDsOJo(long j);

    /* JADX INFO: compiled from: TimeSource.kt */
    @com.android.server.permission.jarjar.kotlin.Metadata(k = 3, mv = {1, 9, 0}, xi = 48)
    public static final class DefaultImpls {
        /* JADX INFO: renamed from: plus-LRDsOJo, reason: not valid java name */
        public static com.android.server.permission.jarjar.kotlin.time.TimeMark m7552plusLRDsOJo(com.android.server.permission.jarjar.kotlin.time.TimeMark $this, long duration) {
            return new com.android.server.permission.jarjar.kotlin.time.AdjustedTimeMark($this, duration, null);
        }

        /* JADX INFO: renamed from: minus-LRDsOJo, reason: not valid java name */
        public static com.android.server.permission.jarjar.kotlin.time.TimeMark m7551minusLRDsOJo(com.android.server.permission.jarjar.kotlin.time.TimeMark $this, long duration) {
            return $this.mo7407plusLRDsOJo(com.android.server.permission.jarjar.kotlin.time.Duration.m7467unaryMinusUwyO8pc(duration));
        }

        public static boolean hasPassedNow(com.android.server.permission.jarjar.kotlin.time.TimeMark $this) {
            return !com.android.server.permission.jarjar.kotlin.time.Duration.m7447isNegativeimpl($this.mo7404elapsedNowUwyO8pc());
        }

        public static boolean hasNotPassedNow(com.android.server.permission.jarjar.kotlin.time.TimeMark $this) {
            return com.android.server.permission.jarjar.kotlin.time.Duration.m7447isNegativeimpl($this.mo7404elapsedNowUwyO8pc());
        }
    }
}
