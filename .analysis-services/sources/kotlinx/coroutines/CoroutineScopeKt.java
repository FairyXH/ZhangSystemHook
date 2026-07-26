package kotlinx.coroutines;

/* JADX INFO: compiled from: CoroutineScope.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000F\n\u0000\n\u0002\u0010\u000b\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\u001a\u000e\u0010\u0006\u001a\u00020\u00022\u0006\u0010\u0007\u001a\u00020\b\u001a\u0006\u0010\t\u001a\u00020\u0002\u001aJ\u0010\n\u001a\u0002H\u000b\"\u0004\b\u0000\u0010\u000b2'\u0010\f\u001a#\b\u0001\u0012\u0004\u0012\u00020\u0002\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u000b0\u000e\u0012\u0006\u0012\u0004\u0018\u00010\u000f0\r¢\u0006\u0002\b\u0010H\u0086@\u0082\u0002\n\n\b\b\u0001\u0012\u0002\u0010\u0001 \u0001¢\u0006\u0002\u0010\u0011\u001a\u000e\u0010\u0012\u001a\u00020\bH\u0086H¢\u0006\u0002\u0010\u0013\u001a\u001e\u0010\u0014\u001a\u00020\u0015*\u00020\u00022\u0006\u0010\u0016\u001a\u00020\u00172\n\b\u0002\u0010\u0018\u001a\u0004\u0018\u00010\u0019\u001a\u001c\u0010\u0014\u001a\u00020\u0015*\u00020\u00022\u0010\b\u0002\u0010\u0018\u001a\n\u0018\u00010\u001aj\u0004\u0018\u0001`\u001b\u001a\n\u0010\u001c\u001a\u00020\u0015*\u00020\u0002\u001a\u0015\u0010\u001d\u001a\u00020\u0002*\u00020\u00022\u0006\u0010\u0007\u001a\u00020\bH\u0086\u0002\"\u001b\u0010\u0000\u001a\u00020\u0001*\u00020\u00028F¢\u0006\f\u0012\u0004\b\u0003\u0010\u0004\u001a\u0004\b\u0000\u0010\u0005¨\u0006\u001e"}, d2 = {"isActive", "", "Lkotlinx/coroutines/CoroutineScope;", "isActive$annotations", "(Lkotlinx/coroutines/CoroutineScope;)V", "(Lkotlinx/coroutines/CoroutineScope;)Z", "CoroutineScope", "context", "Lkotlin/coroutines/CoroutineContext;", "MainScope", "coroutineScope", "R", "block", "Lkotlin/Function2;", "Lkotlin/coroutines/Continuation;", "", "Lkotlin/ExtensionFunctionType;", "(Lkotlin/jvm/functions/Function2;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "currentCoroutineContext", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "cancel", "", "message", "", "cause", "", "Ljava/util/concurrent/CancellationException;", "Lkotlinx/coroutines/CancellationException;", "ensureActive", "plus", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 2, mv = {1, 9, 0}, xi = 48)
public final class CoroutineScopeKt {
    public static /* synthetic */ void isActive$annotations(kotlinx.coroutines.CoroutineScope coroutineScope) {
    }

    public static final kotlinx.coroutines.CoroutineScope plus(kotlinx.coroutines.CoroutineScope $this$plus, kotlin.coroutines.CoroutineContext context) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$plus, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(context, "context");
        return new kotlinx.coroutines.internal.CoroutineScope($this$plus.getCoroutineContext().plus(context));
    }

    public static final kotlinx.coroutines.CoroutineScope MainScope() {
        return new kotlinx.coroutines.internal.CoroutineScope(kotlinx.coroutines.SupervisorKt.SupervisorJob$default((kotlinx.coroutines.Job) null, 1, (java.lang.Object) null).plus(kotlinx.coroutines.Dispatchers.getMain()));
    }

    public static final boolean isActive(kotlinx.coroutines.CoroutineScope $this$isActive) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$isActive, "<this>");
        kotlinx.coroutines.Job job = (kotlinx.coroutines.Job) $this$isActive.getCoroutineContext().get(kotlinx.coroutines.Job.INSTANCE);
        if (job != null) {
            return job.isActive();
        }
        return true;
    }

    public static final <R> java.lang.Object coroutineScope(kotlin.jvm.functions.Function2<? super kotlinx.coroutines.CoroutineScope, ? super kotlin.coroutines.Continuation<? super R>, ? extends java.lang.Object> function2, kotlin.coroutines.Continuation<? super R> continuation) {
        kotlinx.coroutines.internal.ScopeCoroutine coroutine = new kotlinx.coroutines.internal.ScopeCoroutine(continuation.get$context(), continuation);
        java.lang.Object objStartUndispatchedOrReturn = kotlinx.coroutines.intrinsics.UndispatchedKt.startUndispatchedOrReturn(coroutine, coroutine, function2);
        if (objStartUndispatchedOrReturn == kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
            kotlin.coroutines.jvm.internal.DebugProbesKt.probeCoroutineSuspended(continuation);
        }
        return objStartUndispatchedOrReturn;
    }

    public static final kotlinx.coroutines.CoroutineScope CoroutineScope(kotlin.coroutines.CoroutineContext context) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(context, "context");
        return new kotlinx.coroutines.internal.CoroutineScope(context.get(kotlinx.coroutines.Job.INSTANCE) != null ? context : context.plus(kotlinx.coroutines.JobKt__JobKt.Job$default((kotlinx.coroutines.Job) null, 1, (java.lang.Object) null)));
    }

    public static /* synthetic */ void cancel$default(kotlinx.coroutines.CoroutineScope coroutineScope, java.util.concurrent.CancellationException cancellationException, int i, java.lang.Object obj) {
        if ((i & 1) != 0) {
            cancellationException = null;
        }
        cancel(coroutineScope, cancellationException);
    }

    public static final void cancel(kotlinx.coroutines.CoroutineScope $this$cancel, java.util.concurrent.CancellationException cause) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$cancel, "<this>");
        kotlinx.coroutines.Job job = (kotlinx.coroutines.Job) $this$cancel.getCoroutineContext().get(kotlinx.coroutines.Job.INSTANCE);
        if (job == null) {
            throw new java.lang.IllegalStateException(("Scope cannot be cancelled because it does not have a job: " + $this$cancel).toString());
        }
        job.cancel(cause);
    }

    public static final void cancel(kotlinx.coroutines.CoroutineScope $this$cancel, java.lang.String message, java.lang.Throwable cause) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$cancel, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(message, "message");
        cancel($this$cancel, kotlinx.coroutines.ExceptionsKt.CancellationException(message, cause));
    }

    public static /* synthetic */ void cancel$default(kotlinx.coroutines.CoroutineScope coroutineScope, java.lang.String str, java.lang.Throwable th, int i, java.lang.Object obj) {
        if ((i & 2) != 0) {
            th = null;
        }
        cancel(coroutineScope, str, th);
    }

    public static final void ensureActive(kotlinx.coroutines.CoroutineScope $this$ensureActive) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$ensureActive, "<this>");
        kotlinx.coroutines.JobKt.ensureActive($this$ensureActive.getCoroutineContext());
    }

    public static final java.lang.Object currentCoroutineContext(kotlin.coroutines.Continuation<? super kotlin.coroutines.CoroutineContext> continuation) {
        return continuation.get$context();
    }

    private static final java.lang.Object currentCoroutineContext$$forInline(kotlin.coroutines.Continuation<? super kotlin.coroutines.CoroutineContext> continuation) {
        kotlin.jvm.internal.InlineMarker.mark(3);
        kotlin.coroutines.Continuation $completion = null;
        $completion.get$context();
        throw null;
    }
}
