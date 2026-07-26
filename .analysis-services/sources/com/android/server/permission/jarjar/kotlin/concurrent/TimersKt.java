package com.android.server.permission.jarjar.kotlin.concurrent;

/* JADX INFO: compiled from: Timer.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u00004\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\b\b\u001aM\u0010\u0000\u001a\u00020\u00012\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\t2\u0019\b\u0004\u0010\n\u001a\u0013\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\r0\u000b¢\u0006\u0002\b\u000eH\u0087\bø\u0001\u0000\u001aO\u0010\u0000\u001a\u00020\u00012\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u000f\u001a\u00020\t2\u0006\u0010\b\u001a\u00020\t2\u0019\b\u0004\u0010\n\u001a\u0013\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\r0\u000b¢\u0006\u0002\b\u000eH\u0087\bø\u0001\u0000\u001a\u001a\u0010\u0010\u001a\u00020\u00012\b\u0010\u0002\u001a\u0004\u0018\u00010\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0001\u001aM\u0010\u0010\u001a\u00020\u00012\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\t2\u0019\b\u0004\u0010\n\u001a\u0013\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\r0\u000b¢\u0006\u0002\b\u000eH\u0087\bø\u0001\u0000\u001aO\u0010\u0010\u001a\u00020\u00012\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u000f\u001a\u00020\t2\u0006\u0010\b\u001a\u00020\t2\u0019\b\u0004\u0010\n\u001a\u0013\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\r0\u000b¢\u0006\u0002\b\u000eH\u0087\bø\u0001\u0000\u001a'\u0010\u0011\u001a\u00020\f2\u0019\b\u0004\u0010\n\u001a\u0013\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\r0\u000b¢\u0006\u0002\b\u000eH\u0087\bø\u0001\u0000\u001a3\u0010\u0012\u001a\u00020\f*\u00020\u00012\u0006\u0010\u0013\u001a\u00020\u00072\u0019\b\u0004\u0010\n\u001a\u0013\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\r0\u000b¢\u0006\u0002\b\u000eH\u0087\bø\u0001\u0000\u001a;\u0010\u0012\u001a\u00020\f*\u00020\u00012\u0006\u0010\u0013\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\t2\u0019\b\u0004\u0010\n\u001a\u0013\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\r0\u000b¢\u0006\u0002\b\u000eH\u0087\bø\u0001\u0000\u001a3\u0010\u0012\u001a\u00020\f*\u00020\u00012\u0006\u0010\u0014\u001a\u00020\t2\u0019\b\u0004\u0010\n\u001a\u0013\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\r0\u000b¢\u0006\u0002\b\u000eH\u0087\bø\u0001\u0000\u001a;\u0010\u0012\u001a\u00020\f*\u00020\u00012\u0006\u0010\u0014\u001a\u00020\t2\u0006\u0010\b\u001a\u00020\t2\u0019\b\u0004\u0010\n\u001a\u0013\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\r0\u000b¢\u0006\u0002\b\u000eH\u0087\bø\u0001\u0000\u001a;\u0010\u0015\u001a\u00020\f*\u00020\u00012\u0006\u0010\u0013\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\t2\u0019\b\u0004\u0010\n\u001a\u0013\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\r0\u000b¢\u0006\u0002\b\u000eH\u0087\bø\u0001\u0000\u001a;\u0010\u0015\u001a\u00020\f*\u00020\u00012\u0006\u0010\u0014\u001a\u00020\t2\u0006\u0010\b\u001a\u00020\t2\u0019\b\u0004\u0010\n\u001a\u0013\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\r0\u000b¢\u0006\u0002\b\u000eH\u0087\bø\u0001\u0000\u0082\u0002\u0007\n\u0005\b\u009920\u0001¨\u0006\u0016"}, d2 = {"fixedRateTimer", "Ljava/util/Timer;", "name", "", "daemon", "", "startAt", "Ljava/util/Date;", "period", "", "action", "Lkotlin/Function1;", "Ljava/util/TimerTask;", "", "Lkotlin/ExtensionFunctionType;", "initialDelay", "timer", "timerTask", "schedule", "time", "delay", "scheduleAtFixedRate", "kotlin-stdlib"}, k = 2, mv = {1, 9, 0}, xi = 48)
public final class TimersKt {
    private static final java.util.TimerTask schedule(java.util.Timer $this$schedule, long delay, com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<? super java.util.TimerTask, com.android.server.permission.jarjar.kotlin.Unit> function1) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$schedule, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function1, "action");
        java.util.TimerTask task = new com.android.server.permission.jarjar.kotlin.concurrent.TimersKt.AnonymousClass1(function1);
        $this$schedule.schedule(task, delay);
        return task;
    }

    private static final java.util.TimerTask schedule(java.util.Timer $this$schedule, java.util.Date time, com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<? super java.util.TimerTask, com.android.server.permission.jarjar.kotlin.Unit> function1) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$schedule, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(time, "time");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function1, "action");
        java.util.TimerTask task = new com.android.server.permission.jarjar.kotlin.concurrent.TimersKt.AnonymousClass1(function1);
        $this$schedule.schedule(task, time);
        return task;
    }

    private static final java.util.TimerTask schedule(java.util.Timer $this$schedule, long delay, long period, com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<? super java.util.TimerTask, com.android.server.permission.jarjar.kotlin.Unit> function1) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$schedule, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function1, "action");
        java.util.TimerTask task = new com.android.server.permission.jarjar.kotlin.concurrent.TimersKt.AnonymousClass1(function1);
        $this$schedule.schedule(task, delay, period);
        return task;
    }

    private static final java.util.TimerTask schedule(java.util.Timer $this$schedule, java.util.Date time, long period, com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<? super java.util.TimerTask, com.android.server.permission.jarjar.kotlin.Unit> function1) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$schedule, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(time, "time");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function1, "action");
        java.util.TimerTask task = new com.android.server.permission.jarjar.kotlin.concurrent.TimersKt.AnonymousClass1(function1);
        $this$schedule.schedule(task, time, period);
        return task;
    }

    private static final java.util.TimerTask scheduleAtFixedRate(java.util.Timer $this$scheduleAtFixedRate, long delay, long period, com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<? super java.util.TimerTask, com.android.server.permission.jarjar.kotlin.Unit> function1) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$scheduleAtFixedRate, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function1, "action");
        java.util.TimerTask task = new com.android.server.permission.jarjar.kotlin.concurrent.TimersKt.AnonymousClass1(function1);
        $this$scheduleAtFixedRate.scheduleAtFixedRate(task, delay, period);
        return task;
    }

    private static final java.util.TimerTask scheduleAtFixedRate(java.util.Timer $this$scheduleAtFixedRate, java.util.Date time, long period, com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<? super java.util.TimerTask, com.android.server.permission.jarjar.kotlin.Unit> function1) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$scheduleAtFixedRate, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(time, "time");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function1, "action");
        java.util.TimerTask task = new com.android.server.permission.jarjar.kotlin.concurrent.TimersKt.AnonymousClass1(function1);
        $this$scheduleAtFixedRate.scheduleAtFixedRate(task, time, period);
        return task;
    }

    public static final java.util.Timer timer(java.lang.String name, boolean daemon) {
        return name == null ? new java.util.Timer(daemon) : new java.util.Timer(name, daemon);
    }

    static /* synthetic */ java.util.Timer timer$default(java.lang.String name, boolean daemon, long initialDelay, long period, com.android.server.permission.jarjar.kotlin.jvm.functions.Function1 action, int i, java.lang.Object obj) {
        if ((i & 1) != 0) {
            name = null;
        }
        if ((i & 2) != 0) {
            daemon = false;
        }
        if ((i & 4) != 0) {
            initialDelay = 0;
        }
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(action, "action");
        java.util.Timer timer = timer(name, daemon);
        timer.schedule(new com.android.server.permission.jarjar.kotlin.concurrent.TimersKt.AnonymousClass1(action), initialDelay, period);
        return timer;
    }

    private static final java.util.Timer timer(java.lang.String name, boolean daemon, long initialDelay, long period, com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<? super java.util.TimerTask, com.android.server.permission.jarjar.kotlin.Unit> function1) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function1, "action");
        java.util.Timer timer = timer(name, daemon);
        timer.schedule(new com.android.server.permission.jarjar.kotlin.concurrent.TimersKt.AnonymousClass1(function1), initialDelay, period);
        return timer;
    }

    static /* synthetic */ java.util.Timer timer$default(java.lang.String name, boolean daemon, java.util.Date startAt, long period, com.android.server.permission.jarjar.kotlin.jvm.functions.Function1 action, int i, java.lang.Object obj) {
        if ((i & 1) != 0) {
            name = null;
        }
        if ((i & 2) != 0) {
            daemon = false;
        }
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(startAt, "startAt");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(action, "action");
        java.util.Timer timer = timer(name, daemon);
        timer.schedule(new com.android.server.permission.jarjar.kotlin.concurrent.TimersKt.AnonymousClass1(action), startAt, period);
        return timer;
    }

    private static final java.util.Timer timer(java.lang.String name, boolean daemon, java.util.Date startAt, long period, com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<? super java.util.TimerTask, com.android.server.permission.jarjar.kotlin.Unit> function1) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(startAt, "startAt");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function1, "action");
        java.util.Timer timer = timer(name, daemon);
        timer.schedule(new com.android.server.permission.jarjar.kotlin.concurrent.TimersKt.AnonymousClass1(function1), startAt, period);
        return timer;
    }

    static /* synthetic */ java.util.Timer fixedRateTimer$default(java.lang.String name, boolean daemon, long initialDelay, long period, com.android.server.permission.jarjar.kotlin.jvm.functions.Function1 action, int i, java.lang.Object obj) {
        if ((i & 1) != 0) {
            name = null;
        }
        if ((i & 2) != 0) {
            daemon = false;
        }
        if ((i & 4) != 0) {
            initialDelay = 0;
        }
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(action, "action");
        java.util.Timer timer = timer(name, daemon);
        timer.scheduleAtFixedRate(new com.android.server.permission.jarjar.kotlin.concurrent.TimersKt.AnonymousClass1(action), initialDelay, period);
        return timer;
    }

    private static final java.util.Timer fixedRateTimer(java.lang.String name, boolean daemon, long initialDelay, long period, com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<? super java.util.TimerTask, com.android.server.permission.jarjar.kotlin.Unit> function1) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function1, "action");
        java.util.Timer timer = timer(name, daemon);
        timer.scheduleAtFixedRate(new com.android.server.permission.jarjar.kotlin.concurrent.TimersKt.AnonymousClass1(function1), initialDelay, period);
        return timer;
    }

    static /* synthetic */ java.util.Timer fixedRateTimer$default(java.lang.String name, boolean daemon, java.util.Date startAt, long period, com.android.server.permission.jarjar.kotlin.jvm.functions.Function1 action, int i, java.lang.Object obj) {
        if ((i & 1) != 0) {
            name = null;
        }
        if ((i & 2) != 0) {
            daemon = false;
        }
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(startAt, "startAt");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(action, "action");
        java.util.Timer timer = timer(name, daemon);
        timer.scheduleAtFixedRate(new com.android.server.permission.jarjar.kotlin.concurrent.TimersKt.AnonymousClass1(action), startAt, period);
        return timer;
    }

    private static final java.util.Timer fixedRateTimer(java.lang.String name, boolean daemon, java.util.Date startAt, long period, com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<? super java.util.TimerTask, com.android.server.permission.jarjar.kotlin.Unit> function1) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(startAt, "startAt");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function1, "action");
        java.util.Timer timer = timer(name, daemon);
        timer.scheduleAtFixedRate(new com.android.server.permission.jarjar.kotlin.concurrent.TimersKt.AnonymousClass1(function1), startAt, period);
        return timer;
    }

    /* JADX INFO: renamed from: com.android.server.permission.jarjar.kotlin.concurrent.TimersKt$timerTask$1, reason: invalid class name */
    /* JADX INFO: compiled from: Timer.kt */
    @com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000\u0011\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\b\u0010\u0002\u001a\u00020\u0003H\u0016¨\u0006\u0004"}, d2 = {"com/android/server/permission/jarjar/kotlin/concurrent/TimersKt$timerTask$1", "Ljava/util/TimerTask;", "run", "", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 176)
    public static final class AnonymousClass1 extends java.util.TimerTask {
        final /* synthetic */ com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<java.util.TimerTask, com.android.server.permission.jarjar.kotlin.Unit> $action;

        /* JADX WARN: Multi-variable type inference failed */
        public AnonymousClass1(com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<? super java.util.TimerTask, com.android.server.permission.jarjar.kotlin.Unit> function1) {
            this.$action = function1;
        }

        @Override // java.util.TimerTask, java.lang.Runnable
        public void run() {
            this.$action.invoke(this);
        }
    }

    private static final java.util.TimerTask timerTask(com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<? super java.util.TimerTask, com.android.server.permission.jarjar.kotlin.Unit> function1) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function1, "action");
        return new com.android.server.permission.jarjar.kotlin.concurrent.TimersKt.AnonymousClass1(function1);
    }
}
