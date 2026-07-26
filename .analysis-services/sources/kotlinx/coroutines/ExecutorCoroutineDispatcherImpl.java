package kotlinx.coroutines;

/* JADX INFO: compiled from: Executors.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000j\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b\u0000\u0018\u00002\u00020\u00012\u00020\u0002B\r\u0012\u0006\u0010\u0003\u001a\u00020\u0004¢\u0006\u0002\u0010\u0005J\u0018\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\rH\u0002J\b\u0010\u000e\u001a\u00020\tH\u0016J\u001c\u0010\u000f\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000b2\n\u0010\u0010\u001a\u00060\u0011j\u0002`\u0012H\u0016J\u0013\u0010\u0013\u001a\u00020\u00142\b\u0010\u0015\u001a\u0004\u0018\u00010\u0016H\u0096\u0002J\b\u0010\u0017\u001a\u00020\u0018H\u0016J$\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\u001b\u001a\u00020\u001c2\n\u0010\u0010\u001a\u00060\u0011j\u0002`\u00122\u0006\u0010\n\u001a\u00020\u000bH\u0016J\u001e\u0010\u001d\u001a\u00020\t2\u0006\u0010\u001b\u001a\u00020\u001c2\f\u0010\u001e\u001a\b\u0012\u0004\u0012\u00020\t0\u001fH\u0016J\b\u0010 \u001a\u00020!H\u0016J.\u0010\"\u001a\b\u0012\u0002\b\u0003\u0018\u00010#*\u00020$2\n\u0010\u0010\u001a\u00060\u0011j\u0002`\u00122\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\u001b\u001a\u00020\u001cH\u0002R\u0014\u0010\u0003\u001a\u00020\u0004X\u0096\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007¨\u0006%"}, d2 = {"Lkotlinx/coroutines/ExecutorCoroutineDispatcherImpl;", "Lkotlinx/coroutines/ExecutorCoroutineDispatcher;", "Lkotlinx/coroutines/Delay;", "executor", "Ljava/util/concurrent/Executor;", "(Ljava/util/concurrent/Executor;)V", "getExecutor", "()Ljava/util/concurrent/Executor;", "cancelJobOnRejection", "", "context", "Lkotlin/coroutines/CoroutineContext;", "exception", "Ljava/util/concurrent/RejectedExecutionException;", "close", "dispatch", "block", "Ljava/lang/Runnable;", "Lkotlinx/coroutines/Runnable;", "equals", "", "other", "", com.android.server.oplus.osense.OsenseConstants.KEY_INTEGER_HASH, "", "invokeOnTimeout", "Lkotlinx/coroutines/DisposableHandle;", "timeMillis", "", "scheduleResumeAfterDelay", "continuation", "Lkotlinx/coroutines/CancellableContinuation;", "toString", "", "scheduleBlock", "Ljava/util/concurrent/ScheduledFuture;", "Ljava/util/concurrent/ScheduledExecutorService;", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class ExecutorCoroutineDispatcherImpl extends kotlinx.coroutines.ExecutorCoroutineDispatcher implements kotlinx.coroutines.Delay {
    private final java.util.concurrent.Executor executor;

    public ExecutorCoroutineDispatcherImpl(java.util.concurrent.Executor executor) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(executor, "executor");
        this.executor = executor;
        kotlinx.coroutines.internal.ConcurrentKt.removeFutureOnCancel(getExecutor());
    }

    @Override // kotlinx.coroutines.Delay
    @kotlin.Deprecated(level = kotlin.DeprecationLevel.ERROR, message = "Deprecated without replacement as an internal method never intended for public use")
    public java.lang.Object delay(long time, kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return kotlinx.coroutines.Delay.DefaultImpls.delay(this, time, continuation);
    }

    @Override // kotlinx.coroutines.ExecutorCoroutineDispatcher
    public java.util.concurrent.Executor getExecutor() {
        return this.executor;
    }

    @Override // kotlinx.coroutines.CoroutineDispatcher
    /* JADX INFO: renamed from: dispatch */
    public void mo12864dispatch(kotlin.coroutines.CoroutineContext context, java.lang.Runnable block) {
        java.lang.Runnable runnableWrapTask;
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(context, "context");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(block, "block");
        try {
            java.util.concurrent.Executor executor = getExecutor();
            kotlinx.coroutines.AbstractTimeSource timeSource = kotlinx.coroutines.AbstractTimeSourceKt.getTimeSource();
            if (timeSource == null || (runnableWrapTask = timeSource.wrapTask(block)) == null) {
                runnableWrapTask = block;
            }
            executor.execute(runnableWrapTask);
        } catch (java.util.concurrent.RejectedExecutionException e) {
            kotlinx.coroutines.AbstractTimeSource timeSource2 = kotlinx.coroutines.AbstractTimeSourceKt.getTimeSource();
            if (timeSource2 != null) {
                timeSource2.unTrackTask();
            }
            cancelJobOnRejection(context, e);
            kotlinx.coroutines.Dispatchers.getIO().mo12864dispatch(context, block);
        }
    }

    @Override // kotlinx.coroutines.Delay
    /* JADX INFO: renamed from: scheduleResumeAfterDelay */
    public void mo12865scheduleResumeAfterDelay(long timeMillis, kotlinx.coroutines.CancellableContinuation<? super kotlin.Unit> continuation) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(continuation, "continuation");
        java.util.concurrent.Executor executor = getExecutor();
        java.util.concurrent.ScheduledExecutorService scheduledExecutorService = executor instanceof java.util.concurrent.ScheduledExecutorService ? (java.util.concurrent.ScheduledExecutorService) executor : null;
        java.util.concurrent.ScheduledFuture<?> scheduledFutureScheduleBlock = scheduledExecutorService != null ? scheduleBlock(scheduledExecutorService, new kotlinx.coroutines.ResumeUndispatchedRunnable(this, continuation), continuation.get$context(), timeMillis) : null;
        if (scheduledFutureScheduleBlock != null) {
            kotlinx.coroutines.JobKt.cancelFutureOnCancellation(continuation, scheduledFutureScheduleBlock);
        } else {
            kotlinx.coroutines.DefaultExecutor.INSTANCE.mo12865scheduleResumeAfterDelay(timeMillis, continuation);
        }
    }

    @Override // kotlinx.coroutines.Delay
    public kotlinx.coroutines.DisposableHandle invokeOnTimeout(long timeMillis, java.lang.Runnable block, kotlin.coroutines.CoroutineContext context) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(block, "block");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(context, "context");
        java.util.concurrent.Executor executor = getExecutor();
        java.util.concurrent.ScheduledExecutorService scheduledExecutorService = executor instanceof java.util.concurrent.ScheduledExecutorService ? (java.util.concurrent.ScheduledExecutorService) executor : null;
        java.util.concurrent.ScheduledFuture<?> scheduledFutureScheduleBlock = scheduledExecutorService != null ? scheduleBlock(scheduledExecutorService, block, context, timeMillis) : null;
        return scheduledFutureScheduleBlock != null ? new kotlinx.coroutines.DisposableFutureHandle(scheduledFutureScheduleBlock) : kotlinx.coroutines.DefaultExecutor.INSTANCE.invokeOnTimeout(timeMillis, block, context);
    }

    private final java.util.concurrent.ScheduledFuture<?> scheduleBlock(java.util.concurrent.ScheduledExecutorService $this$scheduleBlock, java.lang.Runnable block, kotlin.coroutines.CoroutineContext context, long timeMillis) {
        try {
            return $this$scheduleBlock.schedule(block, timeMillis, java.util.concurrent.TimeUnit.MILLISECONDS);
        } catch (java.util.concurrent.RejectedExecutionException e) {
            cancelJobOnRejection(context, e);
            return null;
        }
    }

    private final void cancelJobOnRejection(kotlin.coroutines.CoroutineContext context, java.util.concurrent.RejectedExecutionException exception) {
        kotlinx.coroutines.JobKt.cancel(context, kotlinx.coroutines.ExceptionsKt.CancellationException("The task was rejected", exception));
    }

    @Override // kotlinx.coroutines.ExecutorCoroutineDispatcher, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        java.util.concurrent.Executor executor = getExecutor();
        java.util.concurrent.ExecutorService executorService = executor instanceof java.util.concurrent.ExecutorService ? (java.util.concurrent.ExecutorService) executor : null;
        if (executorService != null) {
            executorService.shutdown();
        }
    }

    @Override // kotlinx.coroutines.CoroutineDispatcher
    public java.lang.String toString() {
        return getExecutor().toString();
    }

    public boolean equals(java.lang.Object other) {
        return (other instanceof kotlinx.coroutines.ExecutorCoroutineDispatcherImpl) && ((kotlinx.coroutines.ExecutorCoroutineDispatcherImpl) other).getExecutor() == getExecutor();
    }

    public int hashCode() {
        return java.lang.System.identityHashCode(getExecutor());
    }
}
