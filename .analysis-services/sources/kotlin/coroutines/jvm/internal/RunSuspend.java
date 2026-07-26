package kotlin.coroutines.jvm.internal;

/* JADX INFO: compiled from: RunSuspend.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\b\b\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0003J\u0006\u0010\u000e\u001a\u00020\u0002J\u001b\u0010\u000f\u001a\u00020\u00022\f\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00020\tH\u0016¢\u0006\u0002\u0010\u0010R\u0014\u0010\u0004\u001a\u00020\u00058VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\u0006\u0010\u0007R%\u0010\b\u001a\n\u0012\u0004\u0012\u00020\u0002\u0018\u00010\tX\u0086\u000eø\u0001\u0000¢\u0006\u000e\n\u0000\u001a\u0004\b\n\u0010\u000b\"\u0004\b\f\u0010\r\u0082\u0002\u0004\n\u0002\b!¨\u0006\u0011"}, d2 = {"Lkotlin/coroutines/jvm/internal/RunSuspend;", "Lkotlin/coroutines/Continuation;", "", "()V", "context", "Lkotlin/coroutines/CoroutineContext;", "getContext", "()Lkotlin/coroutines/CoroutineContext;", "result", "Lkotlin/Result;", "getResult-xLWZpok", "()Lkotlin/Result;", "setResult", "(Lkotlin/Result;)V", "await", "resumeWith", "(Ljava/lang/Object;)V", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
final class RunSuspend implements kotlin.coroutines.Continuation<kotlin.Unit> {
    private kotlin.Result<kotlin.Unit> result;

    @Override // kotlin.coroutines.Continuation
    public kotlin.coroutines.CoroutineContext getContext() {
        return kotlin.coroutines.EmptyCoroutineContext.INSTANCE;
    }

    /* JADX INFO: renamed from: getResult-xLWZpok, reason: not valid java name */
    public final kotlin.Result<kotlin.Unit> m12512getResultxLWZpok() {
        return this.result;
    }

    public final void setResult(kotlin.Result<kotlin.Unit> result) {
        this.result = result;
    }

    @Override // kotlin.coroutines.Continuation
    public void resumeWith(java.lang.Object result) {
        synchronized (this) {
            this.result = kotlin.Result.m11306boximpl(result);
            kotlin.jvm.internal.Intrinsics.checkNotNull(this, "null cannot be cast to non-null type java.lang.Object");
            notifyAll();
            kotlin.Unit unit = kotlin.Unit.INSTANCE;
        }
    }

    public final void await() {
        synchronized (this) {
            while (true) {
                kotlin.Result<kotlin.Unit> result = this.result;
                if (result == null) {
                    kotlin.jvm.internal.Intrinsics.checkNotNull(this, "null cannot be cast to non-null type java.lang.Object");
                    wait();
                } else {
                    kotlin.ResultKt.throwOnFailure(result.getValue());
                }
            }
        }
    }
}
