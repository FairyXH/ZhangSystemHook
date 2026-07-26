package kotlinx.coroutines;

/* JADX INFO: compiled from: ThreadPoolDispatcher.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000\u0014\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\u001a\u0018\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0007¨\u0006\u0006"}, d2 = {"newFixedThreadPoolContext", "Lkotlinx/coroutines/ExecutorCoroutineDispatcher;", "nThreads", "", "name", "", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 5, mv = {1, 9, 0}, xi = 48, xs = "kotlinx/coroutines/ThreadPoolDispatcherKt")
final /* synthetic */ class ThreadPoolDispatcherKt__ThreadPoolDispatcherKt {
    public static final kotlinx.coroutines.ExecutorCoroutineDispatcher newFixedThreadPoolContext(final int nThreads, final java.lang.String name) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(name, "name");
        if (!(nThreads >= 1)) {
            throw new java.lang.IllegalArgumentException(("Expected at least one thread, but " + nThreads + " specified").toString());
        }
        final java.util.concurrent.atomic.AtomicInteger threadNo = new java.util.concurrent.atomic.AtomicInteger();
        java.util.concurrent.ScheduledExecutorService executor = java.util.concurrent.Executors.newScheduledThreadPool(nThreads, new java.util.concurrent.ThreadFactory() { // from class: kotlinx.coroutines.ThreadPoolDispatcherKt__ThreadPoolDispatcherKt$newFixedThreadPoolContext$executor$1
            @Override // java.util.concurrent.ThreadFactory
            public final java.lang.Thread newThread(java.lang.Runnable runnable) {
                java.lang.String str;
                if (nThreads == 1) {
                    str = name;
                } else {
                    str = name + "-" + threadNo.incrementAndGet();
                }
                java.lang.Thread t = new java.lang.Thread(runnable, str);
                t.setDaemon(true);
                return t;
            }
        });
        kotlin.jvm.internal.Intrinsics.checkNotNull(executor);
        return kotlinx.coroutines.ExecutorsKt.from((java.util.concurrent.ExecutorService) executor);
    }
}
