package kotlinx.coroutines.scheduling;

/* JADX INFO: compiled from: Tasks.kt */
/* JADX INFO: loaded from: classes.dex */
@kotlin.Metadata(d1 = {"\u0000.\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\t\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\u0018\u0002\n\u0002\b\u0002\"\u0010\u0010\u0000\u001a\u00020\u00018\u0000X\u0081\u0004¢\u0006\u0002\n\u0000\"\u0010\u0010\u0002\u001a\u00020\u00038\u0000X\u0081\u0004¢\u0006\u0002\n\u0000\"\u0010\u0010\u0004\u001a\u00020\u00058\u0000X\u0081\u0004¢\u0006\u0002\n\u0000\"\u0010\u0010\u0006\u001a\u00020\u00078\u0000X\u0081\u0004¢\u0006\u0002\n\u0000\"\u0010\u0010\b\u001a\u00020\u00038\u0000X\u0081\u0004¢\u0006\u0002\n\u0000\"\u0010\u0010\t\u001a\u00020\u00018\u0000X\u0081\u0004¢\u0006\u0002\n\u0000\"\u000e\u0010\n\u001a\u00020\u0003X\u0080T¢\u0006\u0002\n\u0000\"\u000e\u0010\u000b\u001a\u00020\u0003X\u0080T¢\u0006\u0002\n\u0000\"\u0010\u0010\f\u001a\u00020\u00078\u0000X\u0081\u0004¢\u0006\u0002\n\u0000\"\u0012\u0010\r\u001a\u00020\u000e8\u0000@\u0000X\u0081\u000e¢\u0006\u0002\n\u0000\"\u0019\u0010\u000f\u001a\u00020\u0010*\u00020\u00118À\u0002X\u0080\u0004¢\u0006\u0006\u001a\u0004\b\u000f\u0010\u0012¨\u0006\u0013"}, d2 = {"BlockingContext", "Lkotlinx/coroutines/scheduling/TaskContext;", "CORE_POOL_SIZE", "", "DEFAULT_SCHEDULER_NAME", "", "IDLE_WORKER_KEEP_ALIVE_NS", "", "MAX_POOL_SIZE", "NonBlockingContext", "TASK_NON_BLOCKING", "TASK_PROBABLY_BLOCKING", "WORK_STEALING_TIME_RESOLUTION_NS", "schedulerTimeSource", "Lkotlinx/coroutines/scheduling/SchedulerTimeSource;", "isBlocking", "", "Lkotlinx/coroutines/scheduling/Task;", "(Lkotlinx/coroutines/scheduling/Task;)Z", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 2, mv = {1, 9, 0}, xi = 48)
public final class TasksKt {
    public static final int TASK_NON_BLOCKING = 0;
    public static final int TASK_PROBABLY_BLOCKING = 1;
    public static final java.lang.String DEFAULT_SCHEDULER_NAME = kotlinx.coroutines.internal.SystemPropsKt.systemProp("kotlinx.coroutines.scheduler.default.name", "DefaultDispatcher");
    public static final long WORK_STEALING_TIME_RESOLUTION_NS = kotlinx.coroutines.internal.SystemPropsKt__SystemProps_commonKt.systemProp$default("kotlinx.coroutines.scheduler.resolution.ns", 100000L, 0L, 0L, 12, (java.lang.Object) null);
    public static final int CORE_POOL_SIZE = kotlinx.coroutines.internal.SystemPropsKt__SystemProps_commonKt.systemProp$default("kotlinx.coroutines.scheduler.core.pool.size", kotlin.ranges.RangesKt.coerceAtLeast(kotlinx.coroutines.internal.SystemPropsKt.getAVAILABLE_PROCESSORS(), 2), 1, 0, 8, (java.lang.Object) null);
    public static final int MAX_POOL_SIZE = kotlinx.coroutines.internal.SystemPropsKt__SystemProps_commonKt.systemProp$default("kotlinx.coroutines.scheduler.max.pool.size", kotlinx.coroutines.scheduling.CoroutineScheduler.MAX_SUPPORTED_POOL_SIZE, 0, kotlinx.coroutines.scheduling.CoroutineScheduler.MAX_SUPPORTED_POOL_SIZE, 4, (java.lang.Object) null);
    public static final long IDLE_WORKER_KEEP_ALIVE_NS = java.util.concurrent.TimeUnit.SECONDS.toNanos(kotlinx.coroutines.internal.SystemPropsKt__SystemProps_commonKt.systemProp$default("kotlinx.coroutines.scheduler.keep.alive.sec", 60L, 0L, 0L, 12, (java.lang.Object) null));
    public static kotlinx.coroutines.scheduling.SchedulerTimeSource schedulerTimeSource = kotlinx.coroutines.scheduling.NanoTimeSource.INSTANCE;
    public static final kotlinx.coroutines.scheduling.TaskContext NonBlockingContext = new kotlinx.coroutines.scheduling.TaskContextImpl(0);
    public static final kotlinx.coroutines.scheduling.TaskContext BlockingContext = new kotlinx.coroutines.scheduling.TaskContextImpl(1);

    public static final boolean isBlocking(kotlinx.coroutines.scheduling.Task $this$isBlocking) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$isBlocking, "<this>");
        return $this$isBlocking.taskContext.getTaskMode() == 1;
    }
}
