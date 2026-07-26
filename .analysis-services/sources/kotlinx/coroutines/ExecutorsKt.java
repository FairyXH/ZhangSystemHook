package kotlinx.coroutines;

/* JADX INFO: compiled from: Executors.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000\u001e\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\u001a\u0011\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\u0007¢\u0006\u0002\b\u0003\u001a\u0011\u0010\u0000\u001a\u00020\u0004*\u00020\u0005H\u0007¢\u0006\u0002\b\u0003\u001a\n\u0010\u0006\u001a\u00020\u0002*\u00020\u0001*\u0010\b\u0007\u0010\u0007\"\u00020\u00042\u00020\u0004B\u0002\b\b¨\u0006\t"}, d2 = {"asCoroutineDispatcher", "Lkotlinx/coroutines/CoroutineDispatcher;", "Ljava/util/concurrent/Executor;", "from", "Lkotlinx/coroutines/ExecutorCoroutineDispatcher;", "Ljava/util/concurrent/ExecutorService;", "asExecutor", "CloseableCoroutineDispatcher", "Lkotlinx/coroutines/ExperimentalCoroutinesApi;", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 2, mv = {1, 9, 0}, xi = 48)
public final class ExecutorsKt {
    public static /* synthetic */ void CloseableCoroutineDispatcher$annotations() {
    }

    public static final kotlinx.coroutines.ExecutorCoroutineDispatcher from(java.util.concurrent.ExecutorService $this$asCoroutineDispatcher) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$asCoroutineDispatcher, "<this>");
        return new kotlinx.coroutines.ExecutorCoroutineDispatcherImpl($this$asCoroutineDispatcher);
    }

    public static final kotlinx.coroutines.CoroutineDispatcher from(java.util.concurrent.Executor $this$asCoroutineDispatcher) {
        kotlinx.coroutines.CoroutineDispatcher coroutineDispatcher;
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$asCoroutineDispatcher, "<this>");
        kotlinx.coroutines.DispatcherExecutor dispatcherExecutor = $this$asCoroutineDispatcher instanceof kotlinx.coroutines.DispatcherExecutor ? (kotlinx.coroutines.DispatcherExecutor) $this$asCoroutineDispatcher : null;
        return (dispatcherExecutor == null || (coroutineDispatcher = dispatcherExecutor.dispatcher) == null) ? new kotlinx.coroutines.ExecutorCoroutineDispatcherImpl($this$asCoroutineDispatcher) : coroutineDispatcher;
    }

    public static final java.util.concurrent.Executor asExecutor(kotlinx.coroutines.CoroutineDispatcher $this$asExecutor) {
        java.util.concurrent.Executor executor;
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$asExecutor, "<this>");
        kotlinx.coroutines.ExecutorCoroutineDispatcher executorCoroutineDispatcher = $this$asExecutor instanceof kotlinx.coroutines.ExecutorCoroutineDispatcher ? (kotlinx.coroutines.ExecutorCoroutineDispatcher) $this$asExecutor : null;
        return (executorCoroutineDispatcher == null || (executor = executorCoroutineDispatcher.getExecutor()) == null) ? new kotlinx.coroutines.DispatcherExecutor($this$asExecutor) : executor;
    }
}
