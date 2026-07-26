package kotlinx.coroutines;

/* JADX INFO: compiled from: CancellableContinuation.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000(\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\u001a\"\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u00022\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0004H\u0000\u001a0\u0010\u0005\u001a\u0002H\u0002\"\u0004\b\u0000\u0010\u00022\u001a\b\u0004\u0010\u0006\u001a\u0014\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020\b\u0012\u0004\u0012\u00020\t0\u0007H\u0086H¢\u0006\u0002\u0010\n\u001a0\u0010\u000b\u001a\u0002H\u0002\"\u0004\b\u0000\u0010\u00022\u001a\b\u0004\u0010\u0006\u001a\u0014\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020\u0001\u0012\u0004\u0012\u00020\t0\u0007H\u0080H¢\u0006\u0002\u0010\n\u001a\u0018\u0010\f\u001a\u00020\t*\u0006\u0012\u0002\b\u00030\b2\u0006\u0010\r\u001a\u00020\u000eH\u0007¨\u0006\u000f"}, d2 = {"getOrCreateCancellableContinuation", "Lkotlinx/coroutines/CancellableContinuationImpl;", "T", "delegate", "Lkotlin/coroutines/Continuation;", "suspendCancellableCoroutine", "block", "Lkotlin/Function1;", "Lkotlinx/coroutines/CancellableContinuation;", "", "(Lkotlin/jvm/functions/Function1;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "suspendCancellableCoroutineReusable", "disposeOnCancellation", "handle", "Lkotlinx/coroutines/DisposableHandle;", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 2, mv = {1, 9, 0}, xi = 48)
public final class CancellableContinuationKt {
    public static final <T> java.lang.Object suspendCancellableCoroutine(kotlin.jvm.functions.Function1<? super kotlinx.coroutines.CancellableContinuation<? super T>, kotlin.Unit> function1, kotlin.coroutines.Continuation<? super T> continuation) {
        kotlinx.coroutines.CancellableContinuationImpl cancellable = new kotlinx.coroutines.CancellableContinuationImpl(kotlin.coroutines.intrinsics.IntrinsicsKt.intercepted(continuation), 1);
        cancellable.initCancellability();
        function1.invoke(cancellable);
        java.lang.Object result = cancellable.getResult();
        if (result == kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
            kotlin.coroutines.jvm.internal.DebugProbesKt.probeCoroutineSuspended(continuation);
        }
        return result;
    }

    private static final <T> java.lang.Object suspendCancellableCoroutine$$forInline(kotlin.jvm.functions.Function1<? super kotlinx.coroutines.CancellableContinuation<? super T>, kotlin.Unit> function1, kotlin.coroutines.Continuation<? super T> continuation) {
        kotlin.jvm.internal.InlineMarker.mark(0);
        kotlinx.coroutines.CancellableContinuationImpl cancellable = new kotlinx.coroutines.CancellableContinuationImpl(kotlin.coroutines.intrinsics.IntrinsicsKt.intercepted(continuation), 1);
        cancellable.initCancellability();
        function1.invoke(cancellable);
        java.lang.Object result = cancellable.getResult();
        if (result == kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
            kotlin.coroutines.jvm.internal.DebugProbesKt.probeCoroutineSuspended(continuation);
        }
        kotlin.jvm.internal.InlineMarker.mark(1);
        return result;
    }

    public static final <T> java.lang.Object suspendCancellableCoroutineReusable(kotlin.jvm.functions.Function1<? super kotlinx.coroutines.CancellableContinuationImpl<? super T>, kotlin.Unit> function1, kotlin.coroutines.Continuation<? super T> continuation) {
        kotlinx.coroutines.CancellableContinuationImpl cancellable = getOrCreateCancellableContinuation(kotlin.coroutines.intrinsics.IntrinsicsKt.intercepted(continuation));
        try {
            function1.invoke(cancellable);
            java.lang.Object result = cancellable.getResult();
            if (result == kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
                kotlin.coroutines.jvm.internal.DebugProbesKt.probeCoroutineSuspended(continuation);
            }
            return result;
        } catch (java.lang.Throwable e) {
            cancellable.releaseClaimedReusableContinuation$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host();
            throw e;
        }
    }

    private static final <T> java.lang.Object suspendCancellableCoroutineReusable$$forInline(kotlin.jvm.functions.Function1<? super kotlinx.coroutines.CancellableContinuationImpl<? super T>, kotlin.Unit> function1, kotlin.coroutines.Continuation<? super T> continuation) {
        kotlin.jvm.internal.InlineMarker.mark(0);
        kotlinx.coroutines.CancellableContinuationImpl cancellable = getOrCreateCancellableContinuation(kotlin.coroutines.intrinsics.IntrinsicsKt.intercepted(continuation));
        try {
            function1.invoke(cancellable);
            java.lang.Object result = cancellable.getResult();
            if (result == kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
                kotlin.coroutines.jvm.internal.DebugProbesKt.probeCoroutineSuspended(continuation);
            }
            kotlin.jvm.internal.InlineMarker.mark(1);
            return result;
        } catch (java.lang.Throwable e) {
            cancellable.releaseClaimedReusableContinuation$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host();
            throw e;
        }
    }

    public static final <T> kotlinx.coroutines.CancellableContinuationImpl<T> getOrCreateCancellableContinuation(kotlin.coroutines.Continuation<? super T> delegate) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(delegate, "delegate");
        if (!(delegate instanceof kotlinx.coroutines.internal.DispatchedContinuation)) {
            return new kotlinx.coroutines.CancellableContinuationImpl<>(delegate, 1);
        }
        kotlinx.coroutines.CancellableContinuationImpl<T> cancellableContinuationImplClaimReusableCancellableContinuation$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host = ((kotlinx.coroutines.internal.DispatchedContinuation) delegate).claimReusableCancellableContinuation$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host();
        if (cancellableContinuationImplClaimReusableCancellableContinuation$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host != null) {
            if (!cancellableContinuationImplClaimReusableCancellableContinuation$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host.resetStateReusable()) {
                cancellableContinuationImplClaimReusableCancellableContinuation$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host = null;
            }
            if (cancellableContinuationImplClaimReusableCancellableContinuation$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host != null) {
                return cancellableContinuationImplClaimReusableCancellableContinuation$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host;
            }
        }
        return new kotlinx.coroutines.CancellableContinuationImpl<>(delegate, 2);
    }

    public static final void disposeOnCancellation(kotlinx.coroutines.CancellableContinuation<?> cancellableContinuation, kotlinx.coroutines.DisposableHandle handle) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(cancellableContinuation, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(handle, "handle");
        kotlinx.coroutines.CancelHandlerBase $this$asHandler$iv = new kotlinx.coroutines.DisposeOnCancel(handle);
        cancellableContinuation.invokeOnCancellation($this$asHandler$iv);
    }
}
