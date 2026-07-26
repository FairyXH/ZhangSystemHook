package com.android.server.permission.jarjar.kotlin.time.jdk8;

/* JADX INFO: compiled from: DurationConversions.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\u001a\u0017\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\u0087\bø\u0001\u0000¢\u0006\u0004\b\u0003\u0010\u0004\u001a\u0012\u0010\u0005\u001a\u00020\u0002*\u00020\u0001H\u0087\b¢\u0006\u0002\u0010\u0006\u0082\u0002\u0007\n\u0005\b¡\u001e0\u0001¨\u0006\u0007"}, d2 = {"toJavaDuration", "Ljava/time/Duration;", "Lkotlin/time/Duration;", "toJavaDuration-LRDsOJo", "(J)Ljava/time/Duration;", "toKotlinDuration", "(Ljava/time/Duration;)J", "kotlin-stdlib-jdk8"}, k = 2, mv = {1, 9, 0}, pn = "com.android.server.permission.jarjar.kotlin.time", xi = 48)
public final class DurationConversionsJDK8Kt {
    private static final long toKotlinDuration(java.time.Duration $this$toKotlinDuration) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$toKotlinDuration, "<this>");
        return com.android.server.permission.jarjar.kotlin.time.Duration.m7450plusLRDsOJo(com.android.server.permission.jarjar.kotlin.time.DurationKt.toDuration($this$toKotlinDuration.getSeconds(), com.android.server.permission.jarjar.kotlin.time.DurationUnit.SECONDS), com.android.server.permission.jarjar.kotlin.time.DurationKt.toDuration($this$toKotlinDuration.getNano(), com.android.server.permission.jarjar.kotlin.time.DurationUnit.NANOSECONDS));
    }

    /* JADX INFO: renamed from: toJavaDuration-LRDsOJo, reason: not valid java name */
    private static final java.time.Duration m7576toJavaDurationLRDsOJo(long $this$toJavaDuration_u2dLRDsOJo) {
        long seconds = com.android.server.permission.jarjar.kotlin.time.Duration.m7435getInWholeSecondsimpl($this$toJavaDuration_u2dLRDsOJo);
        int nanoseconds = com.android.server.permission.jarjar.kotlin.time.Duration.m7437getNanosecondsComponentimpl($this$toJavaDuration_u2dLRDsOJo);
        java.time.Duration durationOfSeconds = java.time.Duration.ofSeconds(seconds, nanoseconds);
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(durationOfSeconds, "toComponents-impl(...)");
        return durationOfSeconds;
    }
}
