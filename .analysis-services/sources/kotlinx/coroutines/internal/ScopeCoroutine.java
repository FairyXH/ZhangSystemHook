package kotlinx.coroutines.internal;

/* JADX INFO: compiled from: Scopes.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b\u0010\u0018\u0000*\u0006\b\u0000\u0010\u0001 \u00002\b\u0012\u0004\u0012\u0002H\u00010\u00022\u00060\u0003j\u0002`\u0004B\u001b\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00028\u00000\b¢\u0006\u0002\u0010\tJ\u0012\u0010\u0010\u001a\u00020\u00112\b\u0010\u0012\u001a\u0004\u0018\u00010\u0013H\u0014J\u0012\u0010\u0014\u001a\u00020\u00112\b\u0010\u0012\u001a\u0004\u0018\u00010\u0013H\u0014J\u000e\u0010\u0015\u001a\n\u0018\u00010\u0016j\u0004\u0018\u0001`\u0017R\u0019\u0010\n\u001a\n\u0018\u00010\u0003j\u0004\u0018\u0001`\u00048F¢\u0006\u0006\u001a\u0004\b\u000b\u0010\fR\u0014\u0010\r\u001a\u00020\u000e8DX\u0084\u0004¢\u0006\u0006\u001a\u0004\b\r\u0010\u000fR\u0016\u0010\u0007\u001a\b\u0012\u0004\u0012\u00028\u00000\b8\u0006X\u0087\u0004¢\u0006\u0002\n\u0000¨\u0006\u0018"}, d2 = {"Lkotlinx/coroutines/internal/ScopeCoroutine;", "T", "Lkotlinx/coroutines/AbstractCoroutine;", "Lkotlin/coroutines/jvm/internal/CoroutineStackFrame;", "Lkotlinx/coroutines/internal/CoroutineStackFrame;", "context", "Lkotlin/coroutines/CoroutineContext;", "uCont", "Lkotlin/coroutines/Continuation;", "(Lkotlin/coroutines/CoroutineContext;Lkotlin/coroutines/Continuation;)V", "callerFrame", "getCallerFrame", "()Lkotlin/coroutines/jvm/internal/CoroutineStackFrame;", "isScopedCoroutine", "", "()Z", "afterCompletion", "", "state", "", "afterResume", "getStackTraceElement", "Ljava/lang/StackTraceElement;", "Lkotlinx/coroutines/internal/StackTraceElement;", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
public class ScopeCoroutine<T> extends kotlinx.coroutines.AbstractCoroutine<T> implements kotlin.coroutines.jvm.internal.CoroutineStackFrame {
    public final kotlin.coroutines.Continuation<T> uCont;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    /* JADX WARN: Multi-variable type inference failed */
    public ScopeCoroutine(kotlin.coroutines.CoroutineContext context, kotlin.coroutines.Continuation<? super T> uCont) {
        super(context, true, true);
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(context, "context");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(uCont, "uCont");
        this.uCont = uCont;
    }

    @Override // kotlin.coroutines.jvm.internal.CoroutineStackFrame
    public final kotlin.coroutines.jvm.internal.CoroutineStackFrame getCallerFrame() {
        kotlin.coroutines.Continuation<T> continuation = this.uCont;
        if (continuation instanceof kotlin.coroutines.jvm.internal.CoroutineStackFrame) {
            return (kotlin.coroutines.jvm.internal.CoroutineStackFrame) continuation;
        }
        return null;
    }

    @Override // kotlin.coroutines.jvm.internal.CoroutineStackFrame
    public final java.lang.StackTraceElement getStackTraceElement() {
        return null;
    }

    @Override // kotlinx.coroutines.JobSupport
    protected final boolean isScopedCoroutine() {
        return true;
    }

    @Override // kotlinx.coroutines.JobSupport
    protected void afterCompletion(java.lang.Object state) {
        kotlinx.coroutines.internal.DispatchedContinuationKt.resumeCancellableWith$default(kotlin.coroutines.intrinsics.IntrinsicsKt.intercepted(this.uCont), kotlinx.coroutines.CompletionStateKt.recoverResult(state, this.uCont), null, 2, null);
    }

    @Override // kotlinx.coroutines.AbstractCoroutine
    protected void afterResume(java.lang.Object state) {
        this.uCont.resumeWith(kotlinx.coroutines.CompletionStateKt.recoverResult(state, this.uCont));
    }
}
