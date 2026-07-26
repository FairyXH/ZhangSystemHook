package kotlinx.coroutines;

/* JADX INFO: compiled from: Builders.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u00006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0003\b\u0002\u0018\u0000*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u0002B\u001f\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\b\u0010\u0007\u001a\u0004\u0018\u00010\b¢\u0006\u0002\u0010\tJ\u0012\u0010\r\u001a\u00020\u000e2\b\u0010\u000f\u001a\u0004\u0018\u00010\u0010H\u0014J\u000b\u0010\u0011\u001a\u00028\u0000¢\u0006\u0002\u0010\u0012R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0007\u001a\u0004\u0018\u00010\bX\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\n\u001a\u00020\u000b8TX\u0094\u0004¢\u0006\u0006\u001a\u0004\b\n\u0010\f¨\u0006\u0013"}, d2 = {"Lkotlinx/coroutines/BlockingCoroutine;", "T", "Lkotlinx/coroutines/AbstractCoroutine;", "parentContext", "Lkotlin/coroutines/CoroutineContext;", "blockedThread", "Ljava/lang/Thread;", "eventLoop", "Lkotlinx/coroutines/EventLoop;", "(Lkotlin/coroutines/CoroutineContext;Ljava/lang/Thread;Lkotlinx/coroutines/EventLoop;)V", "isScopedCoroutine", "", "()Z", "afterCompletion", "", "state", "", "joinBlocking", "()Ljava/lang/Object;", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
final class BlockingCoroutine<T> extends kotlinx.coroutines.AbstractCoroutine<T> {
    private final java.lang.Thread blockedThread;
    private final kotlinx.coroutines.EventLoop eventLoop;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public BlockingCoroutine(kotlin.coroutines.CoroutineContext parentContext, java.lang.Thread blockedThread, kotlinx.coroutines.EventLoop eventLoop) {
        super(parentContext, true, true);
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(parentContext, "parentContext");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(blockedThread, "blockedThread");
        this.blockedThread = blockedThread;
        this.eventLoop = eventLoop;
    }

    @Override // kotlinx.coroutines.JobSupport
    protected boolean isScopedCoroutine() {
        return true;
    }

    @Override // kotlinx.coroutines.JobSupport
    protected void afterCompletion(java.lang.Object state) {
        kotlin.Unit unit;
        if (!kotlin.jvm.internal.Intrinsics.areEqual(java.lang.Thread.currentThread(), this.blockedThread)) {
            java.lang.Thread thread = this.blockedThread;
            kotlinx.coroutines.AbstractTimeSource timeSource = kotlinx.coroutines.AbstractTimeSourceKt.getTimeSource();
            if (timeSource != null) {
                timeSource.unpark(thread);
                unit = kotlin.Unit.INSTANCE;
            } else {
                unit = null;
            }
            if (unit == null) {
                java.util.concurrent.locks.LockSupport.unpark(thread);
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public final T joinBlocking() throws java.lang.Throwable {
        kotlin.Unit unit;
        kotlinx.coroutines.AbstractTimeSource timeSource = kotlinx.coroutines.AbstractTimeSourceKt.getTimeSource();
        if (timeSource != null) {
            timeSource.registerTimeLoopThread();
        }
        try {
            kotlinx.coroutines.EventLoop eventLoop = this.eventLoop;
            if (eventLoop != null) {
                kotlinx.coroutines.EventLoop.incrementUseCount$default(eventLoop, false, 1, null);
            }
            while (!java.lang.Thread.interrupted()) {
                try {
                    kotlinx.coroutines.EventLoop eventLoop2 = this.eventLoop;
                    long jProcessNextEvent = eventLoop2 != null ? eventLoop2.processNextEvent() : Long.MAX_VALUE;
                    if (!isCompleted()) {
                        kotlinx.coroutines.AbstractTimeSource timeSource2 = kotlinx.coroutines.AbstractTimeSourceKt.getTimeSource();
                        if (timeSource2 != null) {
                            timeSource2.parkNanos(this, jProcessNextEvent);
                            unit = kotlin.Unit.INSTANCE;
                        } else {
                            unit = null;
                        }
                        if (unit == null) {
                            java.util.concurrent.locks.LockSupport.parkNanos(this, jProcessNextEvent);
                        }
                    } else {
                        T t = (T) kotlinx.coroutines.JobSupportKt.unboxState(getState$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host());
                        completedExceptionally = t instanceof kotlinx.coroutines.CompletedExceptionally ? (kotlinx.coroutines.CompletedExceptionally) t : null;
                        if (completedExceptionally != null) {
                            throw completedExceptionally.cause;
                        }
                        return t;
                    }
                } finally {
                    kotlinx.coroutines.EventLoop eventLoop3 = this.eventLoop;
                    if (eventLoop3 != null) {
                        kotlinx.coroutines.EventLoop.decrementUseCount$default(eventLoop3, false, 1, null);
                    }
                }
            }
            java.lang.InterruptedException interruptedException = new java.lang.InterruptedException();
            cancelCoroutine(interruptedException);
            throw interruptedException;
        } finally {
            kotlinx.coroutines.AbstractTimeSource timeSource3 = kotlinx.coroutines.AbstractTimeSourceKt.getTimeSource();
            if (timeSource3 != null) {
                timeSource3.unregisterTimeLoopThread();
            }
        }
    }
}
