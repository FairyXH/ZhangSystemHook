package kotlinx.coroutines;

/* JADX INFO: compiled from: EventLoop.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000&\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\u0018\u0002\n\u0000\u001a\b\u0010\u0000\u001a\u00020\u0001H\u0000\u001a\u001c\u0010\u0002\u001a\u00020\u00032\u000e\b\u0004\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00030\u0005H\u0080\bø\u0001\u0000\u001a\b\u0010\u0006\u001a\u00020\u0007H\u0007\u001a\b\u0010\b\u001a\u00020\u0007H\u0001\u001a\f\u0010\t\u001a\u00020\n*\u00020\u000bH\u0001\u0082\u0002\u0007\n\u0005\b\u009920\u0001¨\u0006\f"}, d2 = {"createEventLoop", "Lkotlinx/coroutines/EventLoop;", "platformAutoreleasePool", "", "block", "Lkotlin/Function0;", "processNextEventInCurrentThread", "", "runSingleTaskFromCurrentSystemDispatcher", "isIoDispatcherThread", "", "Ljava/lang/Thread;", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 2, mv = {1, 9, 0}, xi = 48)
public final class EventLoopKt {
    public static final kotlinx.coroutines.EventLoop createEventLoop() {
        java.lang.Thread threadCurrentThread = java.lang.Thread.currentThread();
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(threadCurrentThread, "currentThread(...)");
        return new kotlinx.coroutines.BlockingEventLoop(threadCurrentThread);
    }

    public static final long processNextEventInCurrentThread() {
        kotlinx.coroutines.EventLoop eventLoopCurrentOrNull$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host = kotlinx.coroutines.ThreadLocalEventLoop.INSTANCE.currentOrNull$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host();
        if (eventLoopCurrentOrNull$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host != null) {
            return eventLoopCurrentOrNull$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host.processNextEvent();
        }
        return Long.MAX_VALUE;
    }

    public static final void platformAutoreleasePool(kotlin.jvm.functions.Function0<kotlin.Unit> block) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(block, "block");
        block.invoke();
    }

    public static final long runSingleTaskFromCurrentSystemDispatcher() {
        java.lang.Thread thread = java.lang.Thread.currentThread();
        if (!(thread instanceof kotlinx.coroutines.scheduling.CoroutineScheduler.Worker)) {
            throw new java.lang.IllegalStateException("Expected CoroutineScheduler.Worker, but got " + thread);
        }
        return ((kotlinx.coroutines.scheduling.CoroutineScheduler.Worker) thread).runSingleTask();
    }

    public static final boolean isIoDispatcherThread(java.lang.Thread $this$isIoDispatcherThread) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$isIoDispatcherThread, "<this>");
        if ($this$isIoDispatcherThread instanceof kotlinx.coroutines.scheduling.CoroutineScheduler.Worker) {
            return ((kotlinx.coroutines.scheduling.CoroutineScheduler.Worker) $this$isIoDispatcherThread).isIo();
        }
        return false;
    }
}
