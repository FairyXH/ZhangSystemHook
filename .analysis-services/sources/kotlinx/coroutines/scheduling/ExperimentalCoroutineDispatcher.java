package kotlinx.coroutines.scheduling;

/* JADX INFO: compiled from: Deprecated.kt */
/* JADX INFO: loaded from: classes.dex */
@kotlin.Metadata(d1 = {"\u0000^\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0005\b\u0011\u0018\u00002\u00020\u0001B%\b\u0016\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0006¢\u0006\u0002\u0010\u0007B\u001b\b\u0017\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0003¢\u0006\u0002\u0010\bB'\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\t\u001a\u00020\n\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0006¢\u0006\u0002\u0010\u000bJ\u0010\u0010\u0012\u001a\u00020\u00132\b\b\u0002\u0010\u0014\u001a\u00020\u0003J\b\u0010\u0015\u001a\u00020\u0016H\u0016J\b\u0010\u0017\u001a\u00020\rH\u0002J\u001c\u0010\u0018\u001a\u00020\u00162\u0006\u0010\u0019\u001a\u00020\u001a2\n\u0010\u001b\u001a\u00060\u001cj\u0002`\u001dH\u0016J)\u0010\u001e\u001a\u00020\u00162\n\u0010\u001b\u001a\u00060\u001cj\u0002`\u001d2\u0006\u0010\u0019\u001a\u00020\u001f2\u0006\u0010 \u001a\u00020!H\u0000¢\u0006\u0002\b\"J\u001c\u0010#\u001a\u00020\u00162\u0006\u0010\u0019\u001a\u00020\u001a2\n\u0010\u001b\u001a\u00060\u001cj\u0002`\u001dH\u0016J\u000e\u0010$\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0003J\b\u0010%\u001a\u00020\u0006H\u0016R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082\u000e¢\u0006\u0002\n\u0000R\u0014\u0010\u000e\u001a\u00020\u000f8VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\u0010\u0010\u0011R\u000e\u0010\t\u001a\u00020\nX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006&"}, d2 = {"Lkotlinx/coroutines/scheduling/ExperimentalCoroutineDispatcher;", "Lkotlinx/coroutines/ExecutorCoroutineDispatcher;", "corePoolSize", "", "maxPoolSize", "schedulerName", "", "(IILjava/lang/String;)V", "(II)V", "idleWorkerKeepAliveNs", "", "(IIJLjava/lang/String;)V", "coroutineScheduler", "Lkotlinx/coroutines/scheduling/CoroutineScheduler;", "executor", "Ljava/util/concurrent/Executor;", "getExecutor", "()Ljava/util/concurrent/Executor;", "blocking", "Lkotlinx/coroutines/CoroutineDispatcher;", "parallelism", "close", "", "createScheduler", "dispatch", "context", "Lkotlin/coroutines/CoroutineContext;", "block", "Ljava/lang/Runnable;", "Lkotlinx/coroutines/Runnable;", "dispatchWithContext", "Lkotlinx/coroutines/scheduling/TaskContext;", "tailDispatch", "", "dispatchWithContext$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host", "dispatchYield", "limited", "toString", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
public class ExperimentalCoroutineDispatcher extends kotlinx.coroutines.ExecutorCoroutineDispatcher {
    private final int corePoolSize;
    private kotlinx.coroutines.scheduling.CoroutineScheduler coroutineScheduler;
    private final long idleWorkerKeepAliveNs;
    private final int maxPoolSize;
    private final java.lang.String schedulerName;

    /* JADX WARN: Illegal instructions before constructor call */
    public /* synthetic */ ExperimentalCoroutineDispatcher(int i, int i2, long j, java.lang.String str, int i3, kotlin.jvm.internal.DefaultConstructorMarker defaultConstructorMarker) {
        java.lang.String str2;
        if ((i3 & 8) == 0) {
            str2 = str;
        } else {
            str2 = "CoroutineScheduler";
        }
        this(i, i2, j, str2);
    }

    public ExperimentalCoroutineDispatcher(int corePoolSize, int maxPoolSize, long idleWorkerKeepAliveNs, java.lang.String schedulerName) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(schedulerName, "schedulerName");
        this.corePoolSize = corePoolSize;
        this.maxPoolSize = maxPoolSize;
        this.idleWorkerKeepAliveNs = idleWorkerKeepAliveNs;
        this.schedulerName = schedulerName;
        this.coroutineScheduler = createScheduler();
    }

    public /* synthetic */ ExperimentalCoroutineDispatcher(int i, int i2, java.lang.String str, int i3, kotlin.jvm.internal.DefaultConstructorMarker defaultConstructorMarker) {
        this((i3 & 1) != 0 ? kotlinx.coroutines.scheduling.TasksKt.CORE_POOL_SIZE : i, (i3 & 2) != 0 ? kotlinx.coroutines.scheduling.TasksKt.MAX_POOL_SIZE : i2, (i3 & 4) != 0 ? kotlinx.coroutines.scheduling.TasksKt.DEFAULT_SCHEDULER_NAME : str);
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public ExperimentalCoroutineDispatcher(int corePoolSize, int maxPoolSize, java.lang.String schedulerName) {
        this(corePoolSize, maxPoolSize, kotlinx.coroutines.scheduling.TasksKt.IDLE_WORKER_KEEP_ALIVE_NS, schedulerName);
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(schedulerName, "schedulerName");
    }

    public /* synthetic */ ExperimentalCoroutineDispatcher(int i, int i2, int i3, kotlin.jvm.internal.DefaultConstructorMarker defaultConstructorMarker) {
        this((i3 & 1) != 0 ? kotlinx.coroutines.scheduling.TasksKt.CORE_POOL_SIZE : i, (i3 & 2) != 0 ? kotlinx.coroutines.scheduling.TasksKt.MAX_POOL_SIZE : i2);
    }

    @kotlin.Deprecated(level = kotlin.DeprecationLevel.HIDDEN, message = "Binary compatibility for Ktor 1.0-beta")
    public /* synthetic */ ExperimentalCoroutineDispatcher(int corePoolSize, int maxPoolSize) {
        this(corePoolSize, maxPoolSize, kotlinx.coroutines.scheduling.TasksKt.IDLE_WORKER_KEEP_ALIVE_NS, null, 8, null);
    }

    @Override // kotlinx.coroutines.ExecutorCoroutineDispatcher
    public java.util.concurrent.Executor getExecutor() {
        return this.coroutineScheduler;
    }

    @Override // kotlinx.coroutines.CoroutineDispatcher
    /* JADX INFO: renamed from: dispatch */
    public void mo12864dispatch(kotlin.coroutines.CoroutineContext context, java.lang.Runnable block) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(context, "context");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(block, "block");
        try {
            kotlinx.coroutines.scheduling.CoroutineScheduler.dispatch$default(this.coroutineScheduler, block, null, false, 6, null);
        } catch (java.util.concurrent.RejectedExecutionException e) {
            kotlinx.coroutines.DefaultExecutor.INSTANCE.mo12864dispatch(context, block);
        }
    }

    @Override // kotlinx.coroutines.CoroutineDispatcher
    public void dispatchYield(kotlin.coroutines.CoroutineContext context, java.lang.Runnable block) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(context, "context");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(block, "block");
        try {
            kotlinx.coroutines.scheduling.CoroutineScheduler.dispatch$default(this.coroutineScheduler, block, null, true, 2, null);
        } catch (java.util.concurrent.RejectedExecutionException e) {
            kotlinx.coroutines.DefaultExecutor.INSTANCE.dispatchYield(context, block);
        }
    }

    @Override // kotlinx.coroutines.ExecutorCoroutineDispatcher, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        this.coroutineScheduler.close();
    }

    @Override // kotlinx.coroutines.CoroutineDispatcher
    public java.lang.String toString() {
        return super.toString() + "[scheduler = " + this.coroutineScheduler + "]";
    }

    public static /* synthetic */ kotlinx.coroutines.CoroutineDispatcher blocking$default(kotlinx.coroutines.scheduling.ExperimentalCoroutineDispatcher experimentalCoroutineDispatcher, int i, int i2, java.lang.Object obj) {
        if (obj != null) {
            throw new java.lang.UnsupportedOperationException("Super calls with default arguments not supported in this target, function: blocking");
        }
        if ((i2 & 1) != 0) {
            i = 16;
        }
        return experimentalCoroutineDispatcher.blocking(i);
    }

    public final kotlinx.coroutines.CoroutineDispatcher blocking(int parallelism) {
        if (!(parallelism > 0)) {
            throw new java.lang.IllegalArgumentException(("Expected positive parallelism level, but have " + parallelism).toString());
        }
        return new kotlinx.coroutines.scheduling.LimitingDispatcher(this, parallelism, null, 1);
    }

    public final kotlinx.coroutines.CoroutineDispatcher limited(int parallelism) {
        if (!(parallelism > 0)) {
            throw new java.lang.IllegalArgumentException(("Expected positive parallelism level, but have " + parallelism).toString());
        }
        if (parallelism <= this.corePoolSize) {
            return new kotlinx.coroutines.scheduling.LimitingDispatcher(this, parallelism, null, 0);
        }
        throw new java.lang.IllegalArgumentException(("Expected parallelism level lesser than core pool size (" + this.corePoolSize + "), but have " + parallelism).toString());
    }

    public final void dispatchWithContext$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(java.lang.Runnable block, kotlinx.coroutines.scheduling.TaskContext context, boolean tailDispatch) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(block, "block");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(context, "context");
        try {
            this.coroutineScheduler.dispatch(block, context, tailDispatch);
        } catch (java.util.concurrent.RejectedExecutionException e) {
            kotlinx.coroutines.DefaultExecutor.INSTANCE.enqueue(this.coroutineScheduler.createTask(block, context));
        }
    }

    private final kotlinx.coroutines.scheduling.CoroutineScheduler createScheduler() {
        return new kotlinx.coroutines.scheduling.CoroutineScheduler(this.corePoolSize, this.maxPoolSize, this.idleWorkerKeepAliveNs, this.schedulerName);
    }
}
