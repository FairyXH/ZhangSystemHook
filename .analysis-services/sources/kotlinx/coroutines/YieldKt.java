package kotlinx.coroutines;

/* JADX INFO: compiled from: Yield.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\u001a\u000e\u0010\u0000\u001a\u00020\u0001H\u0086@¢\u0006\u0002\u0010\u0002¨\u0006\u0003"}, d2 = {"yield", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 2, mv = {1, 9, 0}, xi = 48)
public final class YieldKt {
    public static final java.lang.Object yield(kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        java.lang.Object coroutine_suspended;
        kotlin.coroutines.CoroutineContext context = continuation.getContext();
        kotlinx.coroutines.JobKt.ensureActive(context);
        kotlin.coroutines.Continuation continuationIntercepted = kotlin.coroutines.intrinsics.IntrinsicsKt.intercepted(continuation);
        kotlinx.coroutines.internal.DispatchedContinuation cont = continuationIntercepted instanceof kotlinx.coroutines.internal.DispatchedContinuation ? (kotlinx.coroutines.internal.DispatchedContinuation) continuationIntercepted : null;
        if (cont == null) {
            coroutine_suspended = kotlin.Unit.INSTANCE;
        } else {
            if (cont.dispatcher.isDispatchNeeded(context)) {
                cont.dispatchYield$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(context, kotlin.Unit.INSTANCE);
            } else {
                kotlinx.coroutines.YieldContext yieldContext = new kotlinx.coroutines.YieldContext();
                cont.dispatchYield$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(context.plus(yieldContext), kotlin.Unit.INSTANCE);
                if (yieldContext.dispatcherWasUnconfined) {
                    coroutine_suspended = kotlinx.coroutines.internal.DispatchedContinuationKt.yieldUndispatched(cont) ? kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED() : kotlin.Unit.INSTANCE;
                }
            }
            coroutine_suspended = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED();
        }
        if (coroutine_suspended == kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
            kotlin.coroutines.jvm.internal.DebugProbesKt.probeCoroutineSuspended(continuation);
        }
        return coroutine_suspended == kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED() ? coroutine_suspended : kotlin.Unit.INSTANCE;
    }
}
