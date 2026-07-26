package com.android.server.permission.jarjar.kotlin.coroutines.jvm.internal;

/* JADX INFO: compiled from: ContinuationImpl.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\bÀ\u0002\u0018\u00002\n\u0012\u0006\u0012\u0004\u0018\u00010\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0003J\u001d\u0010\b\u001a\u00020\t2\u000e\u0010\n\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00020\u000bH\u0016¢\u0006\u0002\u0010\fJ\b\u0010\r\u001a\u00020\u000eH\u0016R\u0014\u0010\u0004\u001a\u00020\u00058VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\u0006\u0010\u0007¨\u0006\u000f"}, d2 = {"Lkotlin/coroutines/jvm/internal/CompletedContinuation;", "Lkotlin/coroutines/Continuation;", "", "()V", "context", "Lkotlin/coroutines/CoroutineContext;", "getContext", "()Lkotlin/coroutines/CoroutineContext;", "resumeWith", "", "result", "Lkotlin/Result;", "(Ljava/lang/Object;)V", "toString", "", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class CompletedContinuation implements com.android.server.permission.jarjar.kotlin.coroutines.Continuation<java.lang.Object> {
    public static final com.android.server.permission.jarjar.kotlin.coroutines.jvm.internal.CompletedContinuation INSTANCE = new com.android.server.permission.jarjar.kotlin.coroutines.jvm.internal.CompletedContinuation();

    private CompletedContinuation() {
    }

    @Override // com.android.server.permission.jarjar.kotlin.coroutines.Continuation
    public com.android.server.permission.jarjar.kotlin.coroutines.CoroutineContext getContext() {
        throw new java.lang.IllegalStateException("This continuation is already complete".toString());
    }

    @Override // com.android.server.permission.jarjar.kotlin.coroutines.Continuation
    public void resumeWith(java.lang.Object result) {
        throw new java.lang.IllegalStateException("This continuation is already complete".toString());
    }

    public java.lang.String toString() {
        return "This continuation is already complete";
    }
}
