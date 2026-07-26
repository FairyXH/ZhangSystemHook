package com.android.server.permission.jarjar.kotlin.coroutines.jvm.internal;

/* JADX INFO: compiled from: ContinuationImpl.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\b!\u0018\u00002\u00020\u0001B\u0017\u0012\u0010\u0010\u0002\u001a\f\u0012\u0006\u0012\u0004\u0018\u00010\u0004\u0018\u00010\u0003¢\u0006\u0002\u0010\u0005R\u0014\u0010\u0006\u001a\u00020\u00078VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\b\u0010\t¨\u0006\n"}, d2 = {"Lkotlin/coroutines/jvm/internal/RestrictedContinuationImpl;", "Lkotlin/coroutines/jvm/internal/BaseContinuationImpl;", "completion", "Lkotlin/coroutines/Continuation;", "", "(Lkotlin/coroutines/Continuation;)V", "context", "Lkotlin/coroutines/CoroutineContext;", "getContext", "()Lkotlin/coroutines/CoroutineContext;", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
public abstract class RestrictedContinuationImpl extends com.android.server.permission.jarjar.kotlin.coroutines.jvm.internal.BaseContinuationImpl {
    public RestrictedContinuationImpl(com.android.server.permission.jarjar.kotlin.coroutines.Continuation<java.lang.Object> continuation) {
        super(continuation);
        if (continuation == null) {
            return;
        }
        if (continuation.getContext() == com.android.server.permission.jarjar.kotlin.coroutines.EmptyCoroutineContext.INSTANCE) {
        } else {
            throw new java.lang.IllegalArgumentException("Coroutines with restricted suspension must have EmptyCoroutineContext".toString());
        }
    }

    @Override // com.android.server.permission.jarjar.kotlin.coroutines.Continuation
    public com.android.server.permission.jarjar.kotlin.coroutines.CoroutineContext getContext() {
        return com.android.server.permission.jarjar.kotlin.coroutines.EmptyCoroutineContext.INSTANCE;
    }
}
