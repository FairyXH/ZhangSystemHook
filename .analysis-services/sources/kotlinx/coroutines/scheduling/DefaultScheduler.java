package kotlinx.coroutines.scheduling;

/* JADX INFO: compiled from: Dispatcher.kt */
/* JADX INFO: loaded from: classes.dex */
@kotlin.Metadata(d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\bÀ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H\u0016J\u0010\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0017J\r\u0010\t\u001a\u00020\u0004H\u0000¢\u0006\u0002\b\nJ\b\u0010\u000b\u001a\u00020\fH\u0016¨\u0006\r"}, d2 = {"Lkotlinx/coroutines/scheduling/DefaultScheduler;", "Lkotlinx/coroutines/scheduling/SchedulerCoroutineDispatcher;", "()V", "close", "", "limitedParallelism", "Lkotlinx/coroutines/CoroutineDispatcher;", "parallelism", "", "shutdown", "shutdown$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host", "toString", "", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class DefaultScheduler extends kotlinx.coroutines.scheduling.SchedulerCoroutineDispatcher {
    public static final kotlinx.coroutines.scheduling.DefaultScheduler INSTANCE = new kotlinx.coroutines.scheduling.DefaultScheduler();

    private DefaultScheduler() {
        super(kotlinx.coroutines.scheduling.TasksKt.CORE_POOL_SIZE, kotlinx.coroutines.scheduling.TasksKt.MAX_POOL_SIZE, kotlinx.coroutines.scheduling.TasksKt.IDLE_WORKER_KEEP_ALIVE_NS, kotlinx.coroutines.scheduling.TasksKt.DEFAULT_SCHEDULER_NAME);
    }

    @Override // kotlinx.coroutines.CoroutineDispatcher
    public kotlinx.coroutines.CoroutineDispatcher limitedParallelism(int parallelism) {
        kotlinx.coroutines.internal.LimitedDispatcherKt.checkParallelism(parallelism);
        return parallelism >= kotlinx.coroutines.scheduling.TasksKt.CORE_POOL_SIZE ? this : super.limitedParallelism(parallelism);
    }

    public final void shutdown$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host() {
        super.close();
    }

    @Override // kotlinx.coroutines.scheduling.SchedulerCoroutineDispatcher, kotlinx.coroutines.ExecutorCoroutineDispatcher, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        throw new java.lang.UnsupportedOperationException("Dispatchers.Default cannot be closed");
    }

    @Override // kotlinx.coroutines.CoroutineDispatcher
    public java.lang.String toString() {
        return "Dispatchers.Default";
    }
}
