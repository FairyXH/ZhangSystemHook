package com.android.server.permission.jarjar.kotlin.time;

/* JADX INFO: compiled from: measureTime.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000(\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a,\u0010\u0000\u001a\u00020\u00012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H\u0087\bø\u0001\u0000\u0082\u0002\n\n\b\b\u0001\u0012\u0002\u0010\u0001 \u0001¢\u0006\u0002\u0010\u0005\u001a3\u0010\u0006\u001a\b\u0012\u0004\u0012\u0002H\b0\u0007\"\u0004\b\u0000\u0010\b2\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u0002H\b0\u0003H\u0087\bø\u0001\u0000\u0082\u0002\n\n\b\b\u0001\u0012\u0002\u0010\u0001 \u0001\u001a0\u0010\u0000\u001a\u00020\u0001*\u00020\t2\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H\u0087\bø\u0001\u0000\u0082\u0002\n\n\b\b\u0001\u0012\u0002\u0010\u0001 \u0001¢\u0006\u0002\u0010\n\u001a0\u0010\u0000\u001a\u00020\u0001*\u00020\u000b2\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H\u0087\bø\u0001\u0000\u0082\u0002\n\n\b\b\u0001\u0012\u0002\u0010\u0001 \u0001¢\u0006\u0002\u0010\f\u001a7\u0010\u0006\u001a\b\u0012\u0004\u0012\u0002H\b0\u0007\"\u0004\b\u0000\u0010\b*\u00020\t2\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u0002H\b0\u0003H\u0087\bø\u0001\u0000\u0082\u0002\n\n\b\b\u0001\u0012\u0002\u0010\u0001 \u0001\u001a7\u0010\u0006\u001a\b\u0012\u0004\u0012\u0002H\b0\u0007\"\u0004\b\u0000\u0010\b*\u00020\u000b2\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u0002H\b0\u0003H\u0087\bø\u0001\u0000\u0082\u0002\n\n\b\b\u0001\u0012\u0002\u0010\u0001 \u0001\u0082\u0002\u0007\n\u0005\b\u009920\u0001¨\u0006\r"}, d2 = {"measureTime", "Lkotlin/time/Duration;", "block", "Lkotlin/Function0;", "", "(Lkotlin/jvm/functions/Function0;)J", "measureTimedValue", "Lkotlin/time/TimedValue;", "T", "Lkotlin/time/TimeSource;", "(Lkotlin/time/TimeSource;Lkotlin/jvm/functions/Function0;)J", "Lkotlin/time/TimeSource$Monotonic;", "(Lkotlin/time/TimeSource$Monotonic;Lkotlin/jvm/functions/Function0;)J", "kotlin-stdlib"}, k = 2, mv = {1, 9, 0}, xi = 48)
public final class MeasureTimeKt {
    public static final long measureTime(com.android.server.permission.jarjar.kotlin.jvm.functions.Function0<com.android.server.permission.jarjar.kotlin.Unit> function0) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function0, "block");
        com.android.server.permission.jarjar.kotlin.time.TimeSource.Monotonic $this$measureTime$iv = com.android.server.permission.jarjar.kotlin.time.TimeSource.Monotonic.INSTANCE;
        long mark$iv = $this$measureTime$iv.m7553markNowz9LOYto();
        function0.invoke();
        return com.android.server.permission.jarjar.kotlin.time.TimeSource.Monotonic.ValueTimeMark.m7558elapsedNowUwyO8pc(mark$iv);
    }

    public static final long measureTime(com.android.server.permission.jarjar.kotlin.time.TimeSource $this$measureTime, com.android.server.permission.jarjar.kotlin.jvm.functions.Function0<com.android.server.permission.jarjar.kotlin.Unit> function0) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$measureTime, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function0, "block");
        com.android.server.permission.jarjar.kotlin.time.TimeMark mark = $this$measureTime.markNow();
        function0.invoke();
        return mark.mo7404elapsedNowUwyO8pc();
    }

    public static final long measureTime(com.android.server.permission.jarjar.kotlin.time.TimeSource.Monotonic $this$measureTime, com.android.server.permission.jarjar.kotlin.jvm.functions.Function0<com.android.server.permission.jarjar.kotlin.Unit> function0) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$measureTime, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function0, "block");
        long mark = $this$measureTime.m7553markNowz9LOYto();
        function0.invoke();
        return com.android.server.permission.jarjar.kotlin.time.TimeSource.Monotonic.ValueTimeMark.m7558elapsedNowUwyO8pc(mark);
    }

    public static final <T> com.android.server.permission.jarjar.kotlin.time.TimedValue<T> measureTimedValue(com.android.server.permission.jarjar.kotlin.jvm.functions.Function0<? extends T> function0) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function0, "block");
        com.android.server.permission.jarjar.kotlin.time.TimeSource.Monotonic $this$measureTimedValue$iv = com.android.server.permission.jarjar.kotlin.time.TimeSource.Monotonic.INSTANCE;
        long mark$iv = $this$measureTimedValue$iv.m7553markNowz9LOYto();
        java.lang.Object result$iv = function0.invoke();
        return new com.android.server.permission.jarjar.kotlin.time.TimedValue<>(result$iv, com.android.server.permission.jarjar.kotlin.time.TimeSource.Monotonic.ValueTimeMark.m7558elapsedNowUwyO8pc(mark$iv), null);
    }

    public static final <T> com.android.server.permission.jarjar.kotlin.time.TimedValue<T> measureTimedValue(com.android.server.permission.jarjar.kotlin.time.TimeSource $this$measureTimedValue, com.android.server.permission.jarjar.kotlin.jvm.functions.Function0<? extends T> function0) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$measureTimedValue, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function0, "block");
        com.android.server.permission.jarjar.kotlin.time.TimeMark mark = $this$measureTimedValue.markNow();
        java.lang.Object result = function0.invoke();
        return new com.android.server.permission.jarjar.kotlin.time.TimedValue<>(result, mark.mo7404elapsedNowUwyO8pc(), null);
    }

    public static final <T> com.android.server.permission.jarjar.kotlin.time.TimedValue<T> measureTimedValue(com.android.server.permission.jarjar.kotlin.time.TimeSource.Monotonic $this$measureTimedValue, com.android.server.permission.jarjar.kotlin.jvm.functions.Function0<? extends T> function0) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$measureTimedValue, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function0, "block");
        long mark = $this$measureTimedValue.m7553markNowz9LOYto();
        java.lang.Object result = function0.invoke();
        return new com.android.server.permission.jarjar.kotlin.time.TimedValue<>(result, com.android.server.permission.jarjar.kotlin.time.TimeSource.Monotonic.ValueTimeMark.m7558elapsedNowUwyO8pc(mark), null);
    }
}
