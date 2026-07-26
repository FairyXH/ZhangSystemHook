package kotlinx.coroutines;

/* JADX INFO: compiled from: DefaultExecutor.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000X\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\t\n\u0002\b\u0006\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\b\n\u0002\u0010\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0006\bÀ\u0002\u0018\u00002\u00020\u00012\u00060\u0002j\u0002`\u0003B\u0007\b\u0002¢\u0006\u0002\u0010\u0004J\b\u0010\u001d\u001a\u00020\u001eH\u0002J\b\u0010\u001f\u001a\u00020\u0011H\u0002J\u0014\u0010 \u001a\u00020\u001e2\n\u0010!\u001a\u00060\u0002j\u0002`\u0003H\u0016J\r\u0010\"\u001a\u00020\u001eH\u0000¢\u0006\u0002\b#J$\u0010$\u001a\u00020%2\u0006\u0010&\u001a\u00020\b2\n\u0010'\u001a\u00060\u0002j\u0002`\u00032\u0006\u0010(\u001a\u00020)H\u0016J\b\u0010*\u001a\u00020\u0015H\u0002J\u0018\u0010+\u001a\u00020\u001e2\u0006\u0010,\u001a\u00020\b2\u0006\u0010-\u001a\u00020.H\u0014J\b\u0010/\u001a\u00020\u001eH\u0016J\b\u00100\u001a\u00020\u001eH\u0016J\b\u00101\u001a\u00020\u001eH\u0002J\u000e\u00102\u001a\u00020\u001e2\u0006\u00103\u001a\u00020\bR\u000e\u0010\u0005\u001a\u00020\u0006X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0006X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\bX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0006X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0006X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u0006X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0086T¢\u0006\u0002\n\u0000R\u0016\u0010\u0010\u001a\u0004\u0018\u00010\u0011X\u0082\u000e¢\u0006\b\n\u0000\u0012\u0004\b\u0012\u0010\u0004R\u000e\u0010\u0013\u001a\u00020\u0006X\u0082\u000e¢\u0006\u0002\n\u0000R\u0014\u0010\u0014\u001a\u00020\u00158BX\u0082\u0004¢\u0006\u0006\u001a\u0004\b\u0014\u0010\u0016R\u0014\u0010\u0017\u001a\u00020\u00158BX\u0082\u0004¢\u0006\u0006\u001a\u0004\b\u0017\u0010\u0016R\u0014\u0010\u0018\u001a\u00020\u00158@X\u0080\u0004¢\u0006\u0006\u001a\u0004\b\u0019\u0010\u0016R\u0014\u0010\u001a\u001a\u00020\u00118TX\u0094\u0004¢\u0006\u0006\u001a\u0004\b\u001b\u0010\u001c¨\u00064"}, d2 = {"Lkotlinx/coroutines/DefaultExecutor;", "Lkotlinx/coroutines/EventLoopImplBase;", "Ljava/lang/Runnable;", "Lkotlinx/coroutines/Runnable;", "()V", "ACTIVE", "", "DEFAULT_KEEP_ALIVE_MS", "", "FRESH", "KEEP_ALIVE_NANOS", "SHUTDOWN", "SHUTDOWN_ACK", "SHUTDOWN_REQ", "THREAD_NAME", "", "_thread", "Ljava/lang/Thread;", "get_thread$annotations", "debugStatus", "isShutDown", "", "()Z", "isShutdownRequested", "isThreadPresent", "isThreadPresent$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host", "thread", "getThread", "()Ljava/lang/Thread;", "acknowledgeShutdownIfNeeded", "", "createThreadSync", "enqueue", "task", "ensureStarted", "ensureStarted$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host", "invokeOnTimeout", "Lkotlinx/coroutines/DisposableHandle;", "timeMillis", "block", "context", "Lkotlin/coroutines/CoroutineContext;", "notifyStartup", "reschedule", "now", "delayedTask", "Lkotlinx/coroutines/EventLoopImplBase$DelayedTask;", "run", "shutdown", "shutdownError", "shutdownForTests", "timeout", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class DefaultExecutor extends kotlinx.coroutines.EventLoopImplBase implements java.lang.Runnable {
    private static final int ACTIVE = 1;
    private static final long DEFAULT_KEEP_ALIVE_MS = 1000;
    private static final int FRESH = 0;
    public static final kotlinx.coroutines.DefaultExecutor INSTANCE = new kotlinx.coroutines.DefaultExecutor();
    private static final long KEEP_ALIVE_NANOS;
    private static final int SHUTDOWN = 4;
    private static final int SHUTDOWN_ACK = 3;
    private static final int SHUTDOWN_REQ = 2;
    public static final java.lang.String THREAD_NAME = "kotlinx.coroutines.DefaultExecutor";
    private static volatile java.lang.Thread _thread;
    private static volatile int debugStatus;

    private static /* synthetic */ void get_thread$annotations() {
    }

    private DefaultExecutor() {
    }

    static {
        long j;
        kotlinx.coroutines.EventLoop.incrementUseCount$default(INSTANCE, false, 1, null);
        java.util.concurrent.TimeUnit timeUnit = java.util.concurrent.TimeUnit.MILLISECONDS;
        try {
            j = java.lang.Long.getLong("kotlinx.coroutines.DefaultExecutor.keepAlive", 1000L);
        } catch (java.lang.SecurityException e) {
            j = 1000L;
        }
        kotlin.jvm.internal.Intrinsics.checkNotNull(j);
        KEEP_ALIVE_NANOS = timeUnit.toNanos(j.longValue());
    }

    @Override // kotlinx.coroutines.EventLoopImplPlatform
    protected java.lang.Thread getThread() {
        java.lang.Thread thread = _thread;
        return thread == null ? createThreadSync() : thread;
    }

    private final boolean isShutDown() {
        return debugStatus == 4;
    }

    private final boolean isShutdownRequested() {
        int debugStatus2 = debugStatus;
        return debugStatus2 == 2 || debugStatus2 == 3;
    }

    @Override // kotlinx.coroutines.EventLoopImplBase
    public void enqueue(java.lang.Runnable task) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(task, "task");
        if (isShutDown()) {
            shutdownError();
        }
        super.enqueue(task);
    }

    @Override // kotlinx.coroutines.EventLoopImplPlatform
    protected void reschedule(long now, kotlinx.coroutines.EventLoopImplBase.DelayedTask delayedTask) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(delayedTask, "delayedTask");
        shutdownError();
    }

    private final void shutdownError() {
        throw new java.util.concurrent.RejectedExecutionException("DefaultExecutor was shut down. This error indicates that Dispatchers.shutdown() was invoked prior to completion of exiting coroutines, leaving coroutines in incomplete state. Please refer to Dispatchers.shutdown documentation for more details");
    }

    @Override // kotlinx.coroutines.EventLoopImplBase, kotlinx.coroutines.EventLoop
    public void shutdown() {
        debugStatus = 4;
        super.shutdown();
    }

    @Override // kotlinx.coroutines.EventLoopImplBase, kotlinx.coroutines.Delay
    public kotlinx.coroutines.DisposableHandle invokeOnTimeout(long timeMillis, java.lang.Runnable block, kotlin.coroutines.CoroutineContext context) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(block, "block");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(context, "context");
        return scheduleInvokeOnTimeout(timeMillis, block);
    }

    @Override // java.lang.Runnable
    public void run() {
        kotlin.Unit unit;
        boolean zIsEmpty;
        kotlinx.coroutines.ThreadLocalEventLoop.INSTANCE.setEventLoop$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(this);
        kotlinx.coroutines.AbstractTimeSource timeSource = kotlinx.coroutines.AbstractTimeSourceKt.getTimeSource();
        if (timeSource != null) {
            timeSource.registerTimeLoopThread();
        }
        long shutdownNanos = Long.MAX_VALUE;
        try {
            if (!notifyStartup()) {
                if (zIsEmpty) {
                    return;
                } else {
                    return;
                }
            }
            while (true) {
                java.lang.Thread.interrupted();
                long parkNanos = processNextEvent();
                if (parkNanos == Long.MAX_VALUE) {
                    kotlinx.coroutines.AbstractTimeSource timeSource2 = kotlinx.coroutines.AbstractTimeSourceKt.getTimeSource();
                    long now = timeSource2 != null ? timeSource2.nanoTime() : java.lang.System.nanoTime();
                    if (shutdownNanos == Long.MAX_VALUE) {
                        shutdownNanos = now + KEEP_ALIVE_NANOS;
                    }
                    long tillShutdown = shutdownNanos - now;
                    if (tillShutdown <= 0) {
                        _thread = null;
                        acknowledgeShutdownIfNeeded();
                        kotlinx.coroutines.AbstractTimeSource timeSource3 = kotlinx.coroutines.AbstractTimeSourceKt.getTimeSource();
                        if (timeSource3 != null) {
                            timeSource3.unregisterTimeLoopThread();
                        }
                        if (!isEmpty()) {
                            getThread();
                        }
                        return;
                    }
                    parkNanos = kotlin.ranges.RangesKt.coerceAtMost(parkNanos, tillShutdown);
                } else {
                    shutdownNanos = Long.MAX_VALUE;
                }
                if (parkNanos > 0) {
                    if (isShutdownRequested()) {
                        _thread = null;
                        acknowledgeShutdownIfNeeded();
                        kotlinx.coroutines.AbstractTimeSource timeSource4 = kotlinx.coroutines.AbstractTimeSourceKt.getTimeSource();
                        if (timeSource4 != null) {
                            timeSource4.unregisterTimeLoopThread();
                        }
                        if (!isEmpty()) {
                            getThread();
                        }
                        return;
                    }
                    kotlinx.coroutines.AbstractTimeSource timeSource5 = kotlinx.coroutines.AbstractTimeSourceKt.getTimeSource();
                    if (timeSource5 != null) {
                        timeSource5.parkNanos(this, parkNanos);
                        unit = kotlin.Unit.INSTANCE;
                    } else {
                        unit = null;
                    }
                    if (unit == null) {
                        java.util.concurrent.locks.LockSupport.parkNanos(this, parkNanos);
                    }
                }
            }
        } finally {
            _thread = null;
            acknowledgeShutdownIfNeeded();
            kotlinx.coroutines.AbstractTimeSource timeSource6 = kotlinx.coroutines.AbstractTimeSourceKt.getTimeSource();
            if (timeSource6 != null) {
                timeSource6.unregisterTimeLoopThread();
            }
            if (!isEmpty()) {
                getThread();
            }
        }
    }

    private final synchronized java.lang.Thread createThreadSync() {
        java.lang.Thread $this$createThreadSync_u24lambda_u240;
        $this$createThreadSync_u24lambda_u240 = _thread;
        if ($this$createThreadSync_u24lambda_u240 == null) {
            $this$createThreadSync_u24lambda_u240 = new java.lang.Thread(this, THREAD_NAME);
            _thread = $this$createThreadSync_u24lambda_u240;
            $this$createThreadSync_u24lambda_u240.setDaemon(true);
            $this$createThreadSync_u24lambda_u240.start();
        }
        return $this$createThreadSync_u24lambda_u240;
    }

    public final synchronized void ensureStarted$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host() {
        boolean z = true;
        if (kotlinx.coroutines.DebugKt.getASSERTIONS_ENABLED()) {
            if ((_thread == null ? 1 : 0) == 0) {
                throw new java.lang.AssertionError();
            }
        }
        if (kotlinx.coroutines.DebugKt.getASSERTIONS_ENABLED()) {
            if (debugStatus != 0 && debugStatus != 3) {
                z = false;
            }
            if (!z) {
                throw new java.lang.AssertionError();
            }
        }
        debugStatus = 0;
        createThreadSync();
        while (debugStatus == 0) {
            kotlin.jvm.internal.Intrinsics.checkNotNull(this, "null cannot be cast to non-null type java.lang.Object");
            wait();
        }
    }

    private final synchronized boolean notifyStartup() {
        if (isShutdownRequested()) {
            return false;
        }
        debugStatus = 1;
        kotlin.jvm.internal.Intrinsics.checkNotNull(this, "null cannot be cast to non-null type java.lang.Object");
        notifyAll();
        return true;
    }

    public final synchronized void shutdownForTests(long timeout) {
        kotlin.Unit unit;
        long deadline = java.lang.System.currentTimeMillis() + timeout;
        if (!isShutdownRequested()) {
            debugStatus = 2;
        }
        while (debugStatus != 3 && _thread != null) {
            java.lang.Thread it = _thread;
            if (it != null) {
                kotlinx.coroutines.AbstractTimeSource timeSource = kotlinx.coroutines.AbstractTimeSourceKt.getTimeSource();
                if (timeSource != null) {
                    timeSource.unpark(it);
                    unit = kotlin.Unit.INSTANCE;
                } else {
                    unit = null;
                }
                if (unit == null) {
                    java.util.concurrent.locks.LockSupport.unpark(it);
                }
            }
            long remaining = deadline - java.lang.System.currentTimeMillis();
            if (remaining <= 0) {
                break;
            }
            kotlin.jvm.internal.Intrinsics.checkNotNull(this, "null cannot be cast to non-null type java.lang.Object");
            wait(timeout);
        }
        debugStatus = 0;
    }

    private final synchronized void acknowledgeShutdownIfNeeded() {
        if (isShutdownRequested()) {
            debugStatus = 3;
            resetAll();
            kotlin.jvm.internal.Intrinsics.checkNotNull(this, "null cannot be cast to non-null type java.lang.Object");
            notifyAll();
        }
    }

    public final boolean isThreadPresent$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host() {
        return _thread != null;
    }
}
