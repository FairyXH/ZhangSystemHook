package kotlinx.coroutines;

/* JADX INFO: compiled from: MultithreadedDispatchers.common.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000\u0012\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\u001a\u0014\u0010\u0000\u001a\u00060\u0001j\u0002`\u00022\u0006\u0010\u0003\u001a\u00020\u0004H\u0007¨\u0006\u0005"}, d2 = {"newSingleThreadContext", "Lkotlinx/coroutines/ExecutorCoroutineDispatcher;", "Lkotlinx/coroutines/CloseableCoroutineDispatcher;", "name", "", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 5, mv = {1, 9, 0}, xi = 48, xs = "kotlinx/coroutines/ThreadPoolDispatcherKt")
final /* synthetic */ class ThreadPoolDispatcherKt__MultithreadedDispatchers_commonKt {
    public static final kotlinx.coroutines.ExecutorCoroutineDispatcher newSingleThreadContext(java.lang.String name) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(name, "name");
        return kotlinx.coroutines.ThreadPoolDispatcherKt.newFixedThreadPoolContext(1, name);
    }
}
