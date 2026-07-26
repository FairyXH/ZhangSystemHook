package kotlinx.coroutines;

/* JADX INFO: Access modifiers changed from: package-private */
/* JADX INFO: compiled from: Builders.common.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000J\n\u0000\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\u001aR\u0010\u0004\u001a\u0002H\u0005\"\u0004\b\u0000\u0010\u00052\u0006\u0010\u0006\u001a\u00020\u00072'\u0010\b\u001a#\b\u0001\u0012\u0004\u0012\u00020\n\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00050\u000b\u0012\u0006\u0012\u0004\u0018\u00010\f0\t¢\u0006\u0002\b\rH\u0086@\u0082\u0002\n\n\b\b\u0001\u0012\u0002\u0010\u0002 \u0001¢\u0006\u0002\u0010\u000e\u001aX\u0010\u000f\u001a\b\u0012\u0004\u0012\u0002H\u00050\u0010\"\u0004\b\u0000\u0010\u0005*\u00020\n2\b\b\u0002\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\u0011\u001a\u00020\u00122'\u0010\b\u001a#\b\u0001\u0012\u0004\u0012\u00020\n\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00050\u000b\u0012\u0006\u0012\u0004\u0018\u00010\f0\t¢\u0006\u0002\b\r¢\u0006\u0002\u0010\u0013\u001aC\u0010\u0014\u001a\u0002H\u0005\"\u0004\b\u0000\u0010\u0005*\u00020\u00152)\b\b\u0010\b\u001a#\b\u0001\u0012\u0004\u0012\u00020\n\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00050\u000b\u0012\u0006\u0012\u0004\u0018\u00010\f0\t¢\u0006\u0002\b\rH\u0086J¢\u0006\u0002\u0010\u0016\u001aL\u0010\u0017\u001a\u00020\u0018*\u00020\n2\b\b\u0002\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\u0011\u001a\u00020\u00122'\u0010\b\u001a#\b\u0001\u0012\u0004\u0012\u00020\n\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00190\u000b\u0012\u0006\u0012\u0004\u0018\u00010\f0\t¢\u0006\u0002\b\r¢\u0006\u0002\u0010\u001a\"\u000e\u0010\u0000\u001a\u00020\u0001X\u0082T¢\u0006\u0002\n\u0000\"\u000e\u0010\u0002\u001a\u00020\u0001X\u0082T¢\u0006\u0002\n\u0000\"\u000e\u0010\u0003\u001a\u00020\u0001X\u0082T¢\u0006\u0002\n\u0000¨\u0006\u001b"}, d2 = {"RESUMED", "", kotlinx.coroutines.debug.internal.DebugCoroutineInfoImplKt.SUSPENDED, "UNDECIDED", "withContext", "T", "context", "Lkotlin/coroutines/CoroutineContext;", "block", "Lkotlin/Function2;", "Lkotlinx/coroutines/CoroutineScope;", "Lkotlin/coroutines/Continuation;", "", "Lkotlin/ExtensionFunctionType;", "(Lkotlin/coroutines/CoroutineContext;Lkotlin/jvm/functions/Function2;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "async", "Lkotlinx/coroutines/Deferred;", "start", "Lkotlinx/coroutines/CoroutineStart;", "(Lkotlinx/coroutines/CoroutineScope;Lkotlin/coroutines/CoroutineContext;Lkotlinx/coroutines/CoroutineStart;Lkotlin/jvm/functions/Function2;)Lkotlinx/coroutines/Deferred;", "invoke", "Lkotlinx/coroutines/CoroutineDispatcher;", "(Lkotlinx/coroutines/CoroutineDispatcher;Lkotlin/jvm/functions/Function2;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "launch", "Lkotlinx/coroutines/Job;", "", "(Lkotlinx/coroutines/CoroutineScope;Lkotlin/coroutines/CoroutineContext;Lkotlinx/coroutines/CoroutineStart;Lkotlin/jvm/functions/Function2;)Lkotlinx/coroutines/Job;", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 5, mv = {1, 9, 0}, xi = 48, xs = "kotlinx/coroutines/BuildersKt")
public final /* synthetic */ class BuildersKt__Builders_commonKt {
    private static final int RESUMED = 2;
    private static final int SUSPENDED = 1;
    private static final int UNDECIDED = 0;

    public static /* synthetic */ kotlinx.coroutines.Job launch$default(kotlinx.coroutines.CoroutineScope coroutineScope, kotlin.coroutines.CoroutineContext coroutineContext, kotlinx.coroutines.CoroutineStart coroutineStart, kotlin.jvm.functions.Function2 function2, int i, java.lang.Object obj) {
        if ((i & 1) != 0) {
            coroutineContext = kotlin.coroutines.EmptyCoroutineContext.INSTANCE;
        }
        if ((i & 2) != 0) {
            coroutineStart = kotlinx.coroutines.CoroutineStart.DEFAULT;
        }
        return kotlinx.coroutines.BuildersKt.launch(coroutineScope, coroutineContext, coroutineStart, function2);
    }

    public static final kotlinx.coroutines.Job launch(kotlinx.coroutines.CoroutineScope $this$launch, kotlin.coroutines.CoroutineContext context, kotlinx.coroutines.CoroutineStart start, kotlin.jvm.functions.Function2<? super kotlinx.coroutines.CoroutineScope, ? super kotlin.coroutines.Continuation<? super kotlin.Unit>, ? extends java.lang.Object> block) {
        kotlinx.coroutines.LazyStandaloneCoroutine coroutine;
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$launch, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(context, "context");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(start, "start");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(block, "block");
        kotlin.coroutines.CoroutineContext newContext = kotlinx.coroutines.CoroutineContextKt.newCoroutineContext($this$launch, context);
        if (start.isLazy()) {
            coroutine = new kotlinx.coroutines.LazyStandaloneCoroutine(newContext, block);
        } else {
            coroutine = new kotlinx.coroutines.StandaloneCoroutine(newContext, true);
        }
        coroutine.start(start, coroutine, block);
        return coroutine;
    }

    public static /* synthetic */ kotlinx.coroutines.Deferred async$default(kotlinx.coroutines.CoroutineScope coroutineScope, kotlin.coroutines.CoroutineContext coroutineContext, kotlinx.coroutines.CoroutineStart coroutineStart, kotlin.jvm.functions.Function2 function2, int i, java.lang.Object obj) {
        if ((i & 1) != 0) {
            coroutineContext = kotlin.coroutines.EmptyCoroutineContext.INSTANCE;
        }
        if ((i & 2) != 0) {
            coroutineStart = kotlinx.coroutines.CoroutineStart.DEFAULT;
        }
        return kotlinx.coroutines.BuildersKt.async(coroutineScope, coroutineContext, coroutineStart, function2);
    }

    public static final <T> kotlinx.coroutines.Deferred<T> async(kotlinx.coroutines.CoroutineScope $this$async, kotlin.coroutines.CoroutineContext context, kotlinx.coroutines.CoroutineStart start, kotlin.jvm.functions.Function2<? super kotlinx.coroutines.CoroutineScope, ? super kotlin.coroutines.Continuation<? super T>, ? extends java.lang.Object> block) {
        kotlinx.coroutines.LazyDeferredCoroutine coroutine;
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$async, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(context, "context");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(start, "start");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(block, "block");
        kotlin.coroutines.CoroutineContext newContext = kotlinx.coroutines.CoroutineContextKt.newCoroutineContext($this$async, context);
        if (start.isLazy()) {
            coroutine = new kotlinx.coroutines.LazyDeferredCoroutine(newContext, block);
        } else {
            coroutine = new kotlinx.coroutines.DeferredCoroutine(newContext, true);
        }
        coroutine.start(start, coroutine, block);
        return coroutine;
    }

    public static final <T> java.lang.Object withContext(kotlin.coroutines.CoroutineContext context, kotlin.jvm.functions.Function2<? super kotlinx.coroutines.CoroutineScope, ? super kotlin.coroutines.Continuation<? super T>, ? extends java.lang.Object> function2, kotlin.coroutines.Continuation<? super T> continuation) throws java.lang.Throwable {
        java.lang.Object result$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host;
        kotlin.coroutines.CoroutineContext oldContext = continuation.get$context();
        kotlin.coroutines.CoroutineContext newContext = kotlinx.coroutines.CoroutineContextKt.newCoroutineContext(oldContext, context);
        kotlinx.coroutines.JobKt.ensureActive(newContext);
        if (newContext == oldContext) {
            kotlinx.coroutines.internal.ScopeCoroutine coroutine = new kotlinx.coroutines.internal.ScopeCoroutine(newContext, continuation);
            result$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host = kotlinx.coroutines.intrinsics.UndispatchedKt.startUndispatchedOrReturn(coroutine, coroutine, function2);
        } else if (kotlin.jvm.internal.Intrinsics.areEqual(newContext.get(kotlin.coroutines.ContinuationInterceptor.INSTANCE), oldContext.get(kotlin.coroutines.ContinuationInterceptor.INSTANCE))) {
            kotlinx.coroutines.UndispatchedCoroutine coroutine2 = new kotlinx.coroutines.UndispatchedCoroutine(newContext, continuation);
            kotlin.coroutines.CoroutineContext context$iv = coroutine2.get$context();
            java.lang.Object oldValue$iv = kotlinx.coroutines.internal.ThreadContextKt.updateThreadContext(context$iv, null);
            try {
                java.lang.Object objStartUndispatchedOrReturn = kotlinx.coroutines.intrinsics.UndispatchedKt.startUndispatchedOrReturn(coroutine2, coroutine2, function2);
                kotlinx.coroutines.internal.ThreadContextKt.restoreThreadContext(context$iv, oldValue$iv);
                result$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host = objStartUndispatchedOrReturn;
            } catch (java.lang.Throwable th) {
                kotlinx.coroutines.internal.ThreadContextKt.restoreThreadContext(context$iv, oldValue$iv);
                throw th;
            }
        } else {
            kotlinx.coroutines.DispatchedCoroutine coroutine3 = new kotlinx.coroutines.DispatchedCoroutine(newContext, continuation);
            kotlinx.coroutines.intrinsics.CancellableKt.startCoroutineCancellable$default(function2, coroutine3, coroutine3, null, 4, null);
            result$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host = coroutine3.getResult$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host();
        }
        if (result$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host == kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
            kotlin.coroutines.jvm.internal.DebugProbesKt.probeCoroutineSuspended(continuation);
        }
        return result$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host;
    }

    public static final <T> java.lang.Object invoke(kotlinx.coroutines.CoroutineDispatcher $this$invoke, kotlin.jvm.functions.Function2<? super kotlinx.coroutines.CoroutineScope, ? super kotlin.coroutines.Continuation<? super T>, ? extends java.lang.Object> function2, kotlin.coroutines.Continuation<? super T> continuation) {
        return kotlinx.coroutines.BuildersKt.withContext($this$invoke, function2, continuation);
    }

    private static final <T> java.lang.Object invoke$$forInline(kotlinx.coroutines.CoroutineDispatcher $this$invoke, kotlin.jvm.functions.Function2<? super kotlinx.coroutines.CoroutineScope, ? super kotlin.coroutines.Continuation<? super T>, ? extends java.lang.Object> function2, kotlin.coroutines.Continuation<? super T> continuation) throws java.lang.Throwable {
        kotlin.jvm.internal.InlineMarker.mark(0);
        java.lang.Object objWithContext = kotlinx.coroutines.BuildersKt.withContext($this$invoke, function2, continuation);
        kotlin.jvm.internal.InlineMarker.mark(1);
        return objWithContext;
    }
}
