package com.android.server.permission.jarjar.kotlin.coroutines.jvm.internal;

/* JADX INFO: compiled from: ContinuationImpl.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0000\b!\u0018\u00002\u00020\u0001B\u0019\b\u0016\u0012\u0010\u0010\u0002\u001a\f\u0012\u0006\u0012\u0004\u0018\u00010\u0004\u0018\u00010\u0003¢\u0006\u0002\u0010\u0005B!\u0012\u0010\u0010\u0002\u001a\f\u0012\u0006\u0012\u0004\u0018\u00010\u0004\u0018\u00010\u0003\u0012\b\u0010\u0006\u001a\u0004\u0018\u00010\u0007¢\u0006\u0002\u0010\bJ\u000e\u0010\f\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00040\u0003J\b\u0010\r\u001a\u00020\u000eH\u0014R\u0010\u0010\u0006\u001a\u0004\u0018\u00010\u0007X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\t\u001a\u00020\u00078VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\n\u0010\u000bR\u0018\u0010\f\u001a\f\u0012\u0006\u0012\u0004\u0018\u00010\u0004\u0018\u00010\u0003X\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006\u000f"}, d2 = {"Lkotlin/coroutines/jvm/internal/ContinuationImpl;", "Lkotlin/coroutines/jvm/internal/BaseContinuationImpl;", "completion", "Lkotlin/coroutines/Continuation;", "", "(Lkotlin/coroutines/Continuation;)V", "_context", "Lkotlin/coroutines/CoroutineContext;", "(Lkotlin/coroutines/Continuation;Lkotlin/coroutines/CoroutineContext;)V", "context", "getContext", "()Lkotlin/coroutines/CoroutineContext;", "intercepted", "releaseIntercepted", "", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
public abstract class ContinuationImpl extends com.android.server.permission.jarjar.kotlin.coroutines.jvm.internal.BaseContinuationImpl {
    private final com.android.server.permission.jarjar.kotlin.coroutines.CoroutineContext _context;
    private transient com.android.server.permission.jarjar.kotlin.coroutines.Continuation<java.lang.Object> intercepted;

    public ContinuationImpl(com.android.server.permission.jarjar.kotlin.coroutines.Continuation<java.lang.Object> continuation, com.android.server.permission.jarjar.kotlin.coroutines.CoroutineContext _context) {
        super(continuation);
        this._context = _context;
    }

    public ContinuationImpl(com.android.server.permission.jarjar.kotlin.coroutines.Continuation<java.lang.Object> continuation) {
        this(continuation, continuation != null ? continuation.getContext() : null);
    }

    @Override // com.android.server.permission.jarjar.kotlin.coroutines.Continuation
    public com.android.server.permission.jarjar.kotlin.coroutines.CoroutineContext getContext() {
        com.android.server.permission.jarjar.kotlin.coroutines.CoroutineContext coroutineContext = this._context;
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNull(coroutineContext);
        return coroutineContext;
    }

    public final com.android.server.permission.jarjar.kotlin.coroutines.Continuation<java.lang.Object> intercepted() {
        com.android.server.permission.jarjar.kotlin.coroutines.jvm.internal.ContinuationImpl continuationImplInterceptContinuation = this.intercepted;
        if (continuationImplInterceptContinuation == null) {
            com.android.server.permission.jarjar.kotlin.coroutines.ContinuationInterceptor continuationInterceptor = (com.android.server.permission.jarjar.kotlin.coroutines.ContinuationInterceptor) getContext().get(com.android.server.permission.jarjar.kotlin.coroutines.ContinuationInterceptor.Key);
            if (continuationInterceptor == null || (continuationImplInterceptContinuation = continuationInterceptor.interceptContinuation(this)) == null) {
                continuationImplInterceptContinuation = this;
            }
            this.intercepted = continuationImplInterceptContinuation;
        }
        return continuationImplInterceptContinuation;
    }

    @Override // com.android.server.permission.jarjar.kotlin.coroutines.jvm.internal.BaseContinuationImpl
    protected void releaseIntercepted() {
        com.android.server.permission.jarjar.kotlin.coroutines.Continuation<?> continuation = this.intercepted;
        if (continuation != null && continuation != this) {
            com.android.server.permission.jarjar.kotlin.coroutines.CoroutineContext.Element element = getContext().get(com.android.server.permission.jarjar.kotlin.coroutines.ContinuationInterceptor.Key);
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNull(element);
            ((com.android.server.permission.jarjar.kotlin.coroutines.ContinuationInterceptor) element).releaseInterceptedContinuation(continuation);
        }
        this.intercepted = com.android.server.permission.jarjar.kotlin.coroutines.jvm.internal.CompletedContinuation.INSTANCE;
    }
}
