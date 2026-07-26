package com.android.server.permission.jarjar.kotlin.time;

/* JADX INFO: compiled from: TimeSource.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000f\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\b\bg\u0018\u00002\u00020\u00012\b\u0012\u0004\u0012\u00020\u00000\u0002J\u0011\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0000H\u0096\u0002J\u0013\u0010\u0006\u001a\u00020\u00072\b\u0010\u0005\u001a\u0004\u0018\u00010\bH¦\u0002J\b\u0010\t\u001a\u00020\u0004H&J\u001b\u0010\n\u001a\u00020\u000b2\u0006\u0010\u0005\u001a\u00020\u0000H¦\u0002ø\u0001\u0000¢\u0006\u0004\b\f\u0010\rJ\u0018\u0010\n\u001a\u00020\u00002\u0006\u0010\u000e\u001a\u00020\u000bH\u0096\u0002¢\u0006\u0004\b\u000f\u0010\u0010J\u0018\u0010\u0011\u001a\u00020\u00002\u0006\u0010\u000e\u001a\u00020\u000bH¦\u0002¢\u0006\u0004\b\u0012\u0010\u0010\u0082\u0002\u0004\n\u0002\b!¨\u0006\u0013"}, d2 = {"Lkotlin/time/ComparableTimeMark;", "Lkotlin/time/TimeMark;", "", "compareTo", "", "other", "equals", "", "", com.android.server.oplus.osense.OsenseConstants.KEY_INTEGER_HASH, "minus", "Lkotlin/time/Duration;", "minus-UwyO8pc", "(Lkotlin/time/ComparableTimeMark;)J", "duration", "minus-LRDsOJo", "(J)Lkotlin/time/ComparableTimeMark;", "plus", "plus-LRDsOJo", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
public interface ComparableTimeMark extends com.android.server.permission.jarjar.kotlin.time.TimeMark, java.lang.Comparable<com.android.server.permission.jarjar.kotlin.time.ComparableTimeMark> {
    int compareTo(com.android.server.permission.jarjar.kotlin.time.ComparableTimeMark comparableTimeMark);

    boolean equals(java.lang.Object obj);

    int hashCode();

    @Override // com.android.server.permission.jarjar.kotlin.time.TimeMark
    /* JADX INFO: renamed from: minus-LRDsOJo */
    com.android.server.permission.jarjar.kotlin.time.ComparableTimeMark mo7405minusLRDsOJo(long j);

    /* JADX INFO: renamed from: minus-UwyO8pc */
    long mo7406minusUwyO8pc(com.android.server.permission.jarjar.kotlin.time.ComparableTimeMark comparableTimeMark);

    @Override // com.android.server.permission.jarjar.kotlin.time.TimeMark
    /* JADX INFO: renamed from: plus-LRDsOJo */
    com.android.server.permission.jarjar.kotlin.time.ComparableTimeMark mo7407plusLRDsOJo(long j);

    /* JADX INFO: compiled from: TimeSource.kt */
    @com.android.server.permission.jarjar.kotlin.Metadata(k = 3, mv = {1, 9, 0}, xi = 48)
    public static final class DefaultImpls {
        public static boolean hasNotPassedNow(com.android.server.permission.jarjar.kotlin.time.ComparableTimeMark $this) {
            return com.android.server.permission.jarjar.kotlin.time.TimeMark.DefaultImpls.hasNotPassedNow($this);
        }

        public static boolean hasPassedNow(com.android.server.permission.jarjar.kotlin.time.ComparableTimeMark $this) {
            return com.android.server.permission.jarjar.kotlin.time.TimeMark.DefaultImpls.hasPassedNow($this);
        }

        /* JADX INFO: renamed from: minus-LRDsOJo, reason: not valid java name */
        public static com.android.server.permission.jarjar.kotlin.time.ComparableTimeMark m7409minusLRDsOJo(com.android.server.permission.jarjar.kotlin.time.ComparableTimeMark $this, long duration) {
            return $this.mo7407plusLRDsOJo(com.android.server.permission.jarjar.kotlin.time.Duration.m7467unaryMinusUwyO8pc(duration));
        }

        public static int compareTo(com.android.server.permission.jarjar.kotlin.time.ComparableTimeMark $this, com.android.server.permission.jarjar.kotlin.time.ComparableTimeMark other) {
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(other, "other");
            return com.android.server.permission.jarjar.kotlin.time.Duration.m7413compareToLRDsOJo($this.mo7406minusUwyO8pc(other), com.android.server.permission.jarjar.kotlin.time.Duration.Companion.m7517getZEROUwyO8pc());
        }
    }
}
