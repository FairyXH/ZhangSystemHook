package kotlinx.coroutines;

/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"kotlinx/coroutines/ThreadPoolDispatcherKt__MultithreadedDispatchers_commonKt", "kotlinx/coroutines/ThreadPoolDispatcherKt__ThreadPoolDispatcherKt"}, k = 4, mv = {1, 9, 0}, xi = 48)
public final class ThreadPoolDispatcherKt {
    public static final kotlinx.coroutines.ExecutorCoroutineDispatcher newFixedThreadPoolContext(int nThreads, java.lang.String name) {
        return kotlinx.coroutines.ThreadPoolDispatcherKt__ThreadPoolDispatcherKt.newFixedThreadPoolContext(nThreads, name);
    }

    public static final kotlinx.coroutines.ExecutorCoroutineDispatcher newSingleThreadContext(java.lang.String name) {
        return kotlinx.coroutines.ThreadPoolDispatcherKt__MultithreadedDispatchers_commonKt.newSingleThreadContext(name);
    }
}
