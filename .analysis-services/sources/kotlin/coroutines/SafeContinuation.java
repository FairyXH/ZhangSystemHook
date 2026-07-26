package kotlin.coroutines;

/* JADX INFO: compiled from: SafeContinuationJvm.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0000\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0001\u0018\u0000 \u001a*\u0006\b\u0000\u0010\u0001 \u00002\b\u0012\u0004\u0012\u0002H\u00010\u00022\u00020\u0003:\u0001\u001aB\u0015\b\u0011\u0012\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00028\u00000\u0002¢\u0006\u0002\u0010\u0005B\u001f\b\u0000\u0012\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00028\u00000\u0002\u0012\b\u0010\u0006\u001a\u0004\u0018\u00010\u0007¢\u0006\u0002\u0010\bJ\n\u0010\u0011\u001a\u0004\u0018\u00010\u0007H\u0001J\n\u0010\u0012\u001a\u0004\u0018\u00010\u0013H\u0016J\u001b\u0010\u0014\u001a\u00020\u00152\f\u0010\u0010\u001a\b\u0012\u0004\u0012\u00028\u00000\u0016H\u0016¢\u0006\u0002\u0010\u0017J\b\u0010\u0018\u001a\u00020\u0019H\u0016R\u0016\u0010\t\u001a\u0004\u0018\u00010\u00038VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\n\u0010\u000bR\u0014\u0010\f\u001a\u00020\r8VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\u000e\u0010\u000fR\u0014\u0010\u0004\u001a\b\u0012\u0004\u0012\u00028\u00000\u0002X\u0082\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0010\u001a\u0004\u0018\u00010\u0007X\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006\u001b"}, d2 = {"Lkotlin/coroutines/SafeContinuation;", "T", "Lkotlin/coroutines/Continuation;", "Lkotlin/coroutines/jvm/internal/CoroutineStackFrame;", "delegate", "(Lkotlin/coroutines/Continuation;)V", "initialResult", "", "(Lkotlin/coroutines/Continuation;Ljava/lang/Object;)V", "callerFrame", "getCallerFrame", "()Lkotlin/coroutines/jvm/internal/CoroutineStackFrame;", "context", "Lkotlin/coroutines/CoroutineContext;", "getContext", "()Lkotlin/coroutines/CoroutineContext;", "result", "getOrThrow", "getStackTraceElement", "Ljava/lang/StackTraceElement;", "resumeWith", "", "Lkotlin/Result;", "(Ljava/lang/Object;)V", "toString", "", "Companion", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class SafeContinuation<T> implements kotlin.coroutines.Continuation<T>, kotlin.coroutines.jvm.internal.CoroutineStackFrame {
    private static final kotlin.coroutines.SafeContinuation.Companion Companion = new kotlin.coroutines.SafeContinuation.Companion(null);
    private static final java.util.concurrent.atomic.AtomicReferenceFieldUpdater<kotlin.coroutines.SafeContinuation<?>, java.lang.Object> RESULT = java.util.concurrent.atomic.AtomicReferenceFieldUpdater.newUpdater(kotlin.coroutines.SafeContinuation.class, java.lang.Object.class, "result");
    private final kotlin.coroutines.Continuation<T> delegate;
    private volatile java.lang.Object result;

    /* JADX WARN: Multi-variable type inference failed */
    public SafeContinuation(kotlin.coroutines.Continuation<? super T> delegate, java.lang.Object initialResult) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(delegate, "delegate");
        this.delegate = delegate;
        this.result = initialResult;
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public SafeContinuation(kotlin.coroutines.Continuation<? super T> delegate) {
        this(delegate, kotlin.coroutines.intrinsics.CoroutineSingletons.UNDECIDED);
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(delegate, "delegate");
    }

    @Override // kotlin.coroutines.Continuation
    public kotlin.coroutines.CoroutineContext getContext() {
        return this.delegate.getContext();
    }

    /* JADX INFO: compiled from: SafeContinuationJvm.kt */
    @kotlin.Metadata(d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0082\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002Rd\u0010\u0003\u001aR\u0012\u0014\u0012\u0012\u0012\u0002\b\u0003 \u0006*\b\u0012\u0002\b\u0003\u0018\u00010\u00050\u0005\u0012\f\u0012\n \u0006*\u0004\u0018\u00010\u00010\u0001 \u0006*(\u0012\u0014\u0012\u0012\u0012\u0002\b\u0003 \u0006*\b\u0012\u0002\b\u0003\u0018\u00010\u00050\u0005\u0012\f\u0012\n \u0006*\u0004\u0018\u00010\u00010\u0001\u0018\u00010\u00040\u0004X\u0082\u0004¢\u0006\b\n\u0000\u0012\u0004\b\u0007\u0010\u0002¨\u0006\b"}, d2 = {"Lkotlin/coroutines/SafeContinuation$Companion;", "", "()V", "RESULT", "Ljava/util/concurrent/atomic/AtomicReferenceFieldUpdater;", "Lkotlin/coroutines/SafeContinuation;", "kotlin.jvm.PlatformType", "getRESULT$annotations", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
    private static final class Companion {
        public /* synthetic */ Companion(kotlin.jvm.internal.DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private static /* synthetic */ void getRESULT$annotations() {
        }

        private Companion() {
        }
    }

    @Override // kotlin.coroutines.Continuation
    public void resumeWith(java.lang.Object result) {
        while (true) {
            java.lang.Object cur = this.result;
            if (cur == kotlin.coroutines.intrinsics.CoroutineSingletons.UNDECIDED) {
                if (RESULT.compareAndSet(this, kotlin.coroutines.intrinsics.CoroutineSingletons.UNDECIDED, result)) {
                    return;
                }
            } else {
                if (cur != kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
                    throw new java.lang.IllegalStateException("Already resumed");
                }
                if (RESULT.compareAndSet(this, kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED(), kotlin.coroutines.intrinsics.CoroutineSingletons.RESUMED)) {
                    this.delegate.resumeWith(result);
                    return;
                }
            }
        }
    }

    public final java.lang.Object getOrThrow() throws java.lang.Throwable {
        java.lang.Object result = this.result;
        if (result == kotlin.coroutines.intrinsics.CoroutineSingletons.UNDECIDED) {
            if (RESULT.compareAndSet(this, kotlin.coroutines.intrinsics.CoroutineSingletons.UNDECIDED, kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED())) {
                return kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED();
            }
            result = this.result;
        }
        if (result == kotlin.coroutines.intrinsics.CoroutineSingletons.RESUMED) {
            return kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED();
        }
        if (result instanceof kotlin.Result.Failure) {
            throw ((kotlin.Result.Failure) result).exception;
        }
        return result;
    }

    @Override // kotlin.coroutines.jvm.internal.CoroutineStackFrame
    public kotlin.coroutines.jvm.internal.CoroutineStackFrame getCallerFrame() {
        kotlin.coroutines.Continuation<T> continuation = this.delegate;
        if (continuation instanceof kotlin.coroutines.jvm.internal.CoroutineStackFrame) {
            return (kotlin.coroutines.jvm.internal.CoroutineStackFrame) continuation;
        }
        return null;
    }

    @Override // kotlin.coroutines.jvm.internal.CoroutineStackFrame
    public java.lang.StackTraceElement getStackTraceElement() {
        return null;
    }

    public java.lang.String toString() {
        return "SafeContinuation for " + this.delegate;
    }
}
