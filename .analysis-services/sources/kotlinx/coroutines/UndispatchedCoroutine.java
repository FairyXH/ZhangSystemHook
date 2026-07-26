package kotlinx.coroutines;

/* JADX INFO: compiled from: CoroutineContext.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u00006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0005\b\u0000\u0018\u0000*\u0006\b\u0000\u0010\u0001 \u00002\b\u0012\u0004\u0012\u0002H\u00010\u0002B\u001b\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00028\u00000\u0006¢\u0006\u0002\u0010\u0007J\u0012\u0010\u000e\u001a\u00020\u000f2\b\u0010\u0010\u001a\u0004\u0018\u00010\rH\u0014J\u0006\u0010\u0011\u001a\u00020\tJ\u0018\u0010\u0012\u001a\u00020\u000f2\u0006\u0010\u0003\u001a\u00020\u00042\b\u0010\u0013\u001a\u0004\u0018\u00010\rR\u000e\u0010\b\u001a\u00020\tX\u0082\u000e¢\u0006\u0002\n\u0000R\"\u0010\n\u001a\u0016\u0012\u0012\u0012\u0010\u0012\u0004\u0012\u00020\u0004\u0012\u0006\u0012\u0004\u0018\u00010\r0\f0\u000bX\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0014"}, d2 = {"Lkotlinx/coroutines/UndispatchedCoroutine;", "T", "Lkotlinx/coroutines/internal/ScopeCoroutine;", "context", "Lkotlin/coroutines/CoroutineContext;", "uCont", "Lkotlin/coroutines/Continuation;", "(Lkotlin/coroutines/CoroutineContext;Lkotlin/coroutines/Continuation;)V", "threadLocalIsSet", "", "threadStateToRecover", "Ljava/lang/ThreadLocal;", "Lkotlin/Pair;", "", "afterResume", "", "state", "clearThreadContext", "saveThreadContext", "oldValue", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class UndispatchedCoroutine<T> extends kotlinx.coroutines.internal.ScopeCoroutine<T> {
    private volatile boolean threadLocalIsSet;
    private final java.lang.ThreadLocal<kotlin.Pair<kotlin.coroutines.CoroutineContext, java.lang.Object>> threadStateToRecover;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public UndispatchedCoroutine(kotlin.coroutines.CoroutineContext context, kotlin.coroutines.Continuation<? super T> uCont) {
        super(context.get(kotlinx.coroutines.UndispatchedMarker.INSTANCE) == null ? context.plus(kotlinx.coroutines.UndispatchedMarker.INSTANCE) : context, uCont);
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(context, "context");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(uCont, "uCont");
        this.threadStateToRecover = new java.lang.ThreadLocal<>();
        if (uCont.getContext().get(kotlin.coroutines.ContinuationInterceptor.INSTANCE) instanceof kotlinx.coroutines.CoroutineDispatcher) {
            return;
        }
        java.lang.Object values = kotlinx.coroutines.internal.ThreadContextKt.updateThreadContext(context, null);
        kotlinx.coroutines.internal.ThreadContextKt.restoreThreadContext(context, values);
        saveThreadContext(context, values);
    }

    public final void saveThreadContext(kotlin.coroutines.CoroutineContext context, java.lang.Object oldValue) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(context, "context");
        this.threadLocalIsSet = true;
        this.threadStateToRecover.set(kotlin.TuplesKt.to(context, oldValue));
    }

    public final boolean clearThreadContext() {
        boolean z = this.threadLocalIsSet && this.threadStateToRecover.get() == null;
        this.threadStateToRecover.remove();
        return !z;
    }

    @Override // kotlinx.coroutines.internal.ScopeCoroutine, kotlinx.coroutines.AbstractCoroutine
    protected void afterResume(java.lang.Object state) {
        kotlinx.coroutines.UndispatchedCoroutine<?> undispatchedCoroutineUpdateUndispatchedCompletion;
        if (this.threadLocalIsSet) {
            kotlin.Pair<kotlin.coroutines.CoroutineContext, java.lang.Object> pair = this.threadStateToRecover.get();
            if (pair != null) {
                kotlin.coroutines.CoroutineContext ctx = pair.component1();
                java.lang.Object value = pair.component2();
                kotlinx.coroutines.internal.ThreadContextKt.restoreThreadContext(ctx, value);
            }
            this.threadStateToRecover.remove();
        }
        java.lang.Object result = kotlinx.coroutines.CompletionStateKt.recoverResult(state, this.uCont);
        kotlin.coroutines.Continuation<T> continuation = this.uCont;
        kotlin.coroutines.CoroutineContext context$iv = continuation.getContext();
        java.lang.Object oldValue$iv = kotlinx.coroutines.internal.ThreadContextKt.updateThreadContext(context$iv, null);
        if (oldValue$iv != kotlinx.coroutines.internal.ThreadContextKt.NO_THREAD_ELEMENTS) {
            undispatchedCoroutineUpdateUndispatchedCompletion = kotlinx.coroutines.CoroutineContextKt.updateUndispatchedCompletion(continuation, context$iv, oldValue$iv);
        } else {
            undispatchedCoroutineUpdateUndispatchedCompletion = null;
        }
        try {
            this.uCont.resumeWith(result);
            kotlin.Unit unit = kotlin.Unit.INSTANCE;
        } finally {
            if (undispatchedCoroutineUpdateUndispatchedCompletion == null || undispatchedCoroutineUpdateUndispatchedCompletion.clearThreadContext()) {
                kotlinx.coroutines.internal.ThreadContextKt.restoreThreadContext(context$iv, oldValue$iv);
            }
        }
    }
}
