package kotlinx.coroutines;

/* JADX INFO: compiled from: CoroutineDispatcher.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000H\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0002\b\u0002\b&\u0018\u0000 \u001a2\u00020\u00012\u00020\u0002:\u0001\u001aB\u0005¢\u0006\u0002\u0010\u0003J\u001c\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\n\u0010\b\u001a\u00060\tj\u0002`\nH&J\u001c\u0010\u000b\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\n\u0010\b\u001a\u00060\tj\u0002`\nH\u0017J \u0010\f\u001a\b\u0012\u0004\u0012\u0002H\u000e0\r\"\u0004\b\u0000\u0010\u000e2\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u0002H\u000e0\rJ\u0010\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0006\u001a\u00020\u0007H\u0016J\u0010\u0010\u0012\u001a\u00020\u00002\u0006\u0010\u0013\u001a\u00020\u0014H\u0017J\u0011\u0010\u0015\u001a\u00020\u00002\u0006\u0010\u0016\u001a\u00020\u0000H\u0087\u0002J\u0012\u0010\u0017\u001a\u00020\u00052\n\u0010\u000f\u001a\u0006\u0012\u0002\b\u00030\rJ\b\u0010\u0018\u001a\u00020\u0019H\u0016¨\u0006\u001b"}, d2 = {"Lkotlinx/coroutines/CoroutineDispatcher;", "Lkotlin/coroutines/AbstractCoroutineContextElement;", "Lkotlin/coroutines/ContinuationInterceptor;", "()V", "dispatch", "", "context", "Lkotlin/coroutines/CoroutineContext;", "block", "Ljava/lang/Runnable;", "Lkotlinx/coroutines/Runnable;", "dispatchYield", "interceptContinuation", "Lkotlin/coroutines/Continuation;", "T", "continuation", "isDispatchNeeded", "", "limitedParallelism", "parallelism", "", "plus", "other", "releaseInterceptedContinuation", "toString", "", "Key", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
public abstract class CoroutineDispatcher extends kotlin.coroutines.AbstractCoroutineContextElement implements kotlin.coroutines.ContinuationInterceptor {

    /* JADX INFO: renamed from: Key, reason: from kotlin metadata */
    public static final kotlinx.coroutines.CoroutineDispatcher.Companion INSTANCE = new kotlinx.coroutines.CoroutineDispatcher.Companion(null);

    /* JADX INFO: renamed from: dispatch */
    public abstract void mo12864dispatch(kotlin.coroutines.CoroutineContext context, java.lang.Runnable block);

    @Override // kotlin.coroutines.AbstractCoroutineContextElement, kotlin.coroutines.CoroutineContext.Element, kotlin.coroutines.CoroutineContext
    public <E extends kotlin.coroutines.CoroutineContext.Element> E get(kotlin.coroutines.CoroutineContext.Key<E> key) {
        return (E) kotlin.coroutines.ContinuationInterceptor.DefaultImpls.get(this, key);
    }

    @Override // kotlin.coroutines.AbstractCoroutineContextElement, kotlin.coroutines.CoroutineContext.Element, kotlin.coroutines.CoroutineContext
    public kotlin.coroutines.CoroutineContext minusKey(kotlin.coroutines.CoroutineContext.Key<?> key) {
        return kotlin.coroutines.ContinuationInterceptor.DefaultImpls.minusKey(this, key);
    }

    public CoroutineDispatcher() {
        super(kotlin.coroutines.ContinuationInterceptor.INSTANCE);
    }

    /* JADX INFO: renamed from: kotlinx.coroutines.CoroutineDispatcher$Key, reason: from kotlin metadata */
    /* JADX INFO: compiled from: CoroutineDispatcher.kt */
    @kotlin.Metadata(d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0087\u0003\u0018\u00002\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00030\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0004¨\u0006\u0005"}, d2 = {"Lkotlinx/coroutines/CoroutineDispatcher$Key;", "Lkotlin/coroutines/AbstractCoroutineContextKey;", "Lkotlin/coroutines/ContinuationInterceptor;", "Lkotlinx/coroutines/CoroutineDispatcher;", "()V", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public static final class Companion extends kotlin.coroutines.AbstractCoroutineContextKey<kotlin.coroutines.ContinuationInterceptor, kotlinx.coroutines.CoroutineDispatcher> {
        public /* synthetic */ Companion(kotlin.jvm.internal.DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
            super(kotlin.coroutines.ContinuationInterceptor.INSTANCE, new kotlin.jvm.functions.Function1<kotlin.coroutines.CoroutineContext.Element, kotlinx.coroutines.CoroutineDispatcher>() { // from class: kotlinx.coroutines.CoroutineDispatcher.Key.1
                @Override // kotlin.jvm.functions.Function1
                public final kotlinx.coroutines.CoroutineDispatcher invoke(kotlin.coroutines.CoroutineContext.Element it) {
                    kotlin.jvm.internal.Intrinsics.checkNotNullParameter(it, "it");
                    if (it instanceof kotlinx.coroutines.CoroutineDispatcher) {
                        return (kotlinx.coroutines.CoroutineDispatcher) it;
                    }
                    return null;
                }
            });
        }
    }

    public boolean isDispatchNeeded(kotlin.coroutines.CoroutineContext context) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(context, "context");
        return true;
    }

    public kotlinx.coroutines.CoroutineDispatcher limitedParallelism(int parallelism) {
        kotlinx.coroutines.internal.LimitedDispatcherKt.checkParallelism(parallelism);
        return new kotlinx.coroutines.internal.LimitedDispatcher(this, parallelism);
    }

    public void dispatchYield(kotlin.coroutines.CoroutineContext context, java.lang.Runnable block) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(context, "context");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(block, "block");
        mo12864dispatch(context, block);
    }

    @Override // kotlin.coroutines.ContinuationInterceptor
    public final <T> kotlin.coroutines.Continuation<T> interceptContinuation(kotlin.coroutines.Continuation<? super T> continuation) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(continuation, "continuation");
        return new kotlinx.coroutines.internal.DispatchedContinuation(this, continuation);
    }

    @Override // kotlin.coroutines.ContinuationInterceptor
    public final void releaseInterceptedContinuation(kotlin.coroutines.Continuation<?> continuation) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(continuation, "continuation");
        kotlinx.coroutines.internal.DispatchedContinuation dispatched = (kotlinx.coroutines.internal.DispatchedContinuation) continuation;
        dispatched.release$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host();
    }

    @kotlin.Deprecated(level = kotlin.DeprecationLevel.ERROR, message = "Operator '+' on two CoroutineDispatcher objects is meaningless. CoroutineDispatcher is a coroutine context element and `+` is a set-sum operator for coroutine contexts. The dispatcher to the right of `+` just replaces the dispatcher to the left.")
    public final kotlinx.coroutines.CoroutineDispatcher plus(kotlinx.coroutines.CoroutineDispatcher other) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(other, "other");
        return other;
    }

    public java.lang.String toString() {
        return kotlinx.coroutines.DebugStringsKt.getClassSimpleName(this) + "@" + kotlinx.coroutines.DebugStringsKt.getHexAddress(this);
    }
}
