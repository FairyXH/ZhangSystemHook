package kotlinx.coroutines;

/* JADX INFO: compiled from: CompletableDeferred.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u00000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\t\n\u0002\u0010\u0003\n\u0002\b\u0003\b\u0002\u0018\u0000*\u0004\b\u0000\u0010\u00012\u00020\u00022\b\u0012\u0004\u0012\u0002H\u00010\u0003B\u000f\u0012\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005¢\u0006\u0002\u0010\u0006J\u000e\u0010\u000f\u001a\u00028\u0000H\u0096@¢\u0006\u0002\u0010\u0010J\u0015\u0010\u0011\u001a\u00020\f2\u0006\u0010\u0012\u001a\u00028\u0000H\u0016¢\u0006\u0002\u0010\u0013J\u0010\u0010\u0014\u001a\u00020\f2\u0006\u0010\u0015\u001a\u00020\u0016H\u0016J\r\u0010\u0017\u001a\u00028\u0000H\u0016¢\u0006\u0002\u0010\u0018R\u001a\u0010\u0007\u001a\b\u0012\u0004\u0012\u00028\u00000\b8VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\t\u0010\nR\u0014\u0010\u000b\u001a\u00020\f8PX\u0090\u0004¢\u0006\u0006\u001a\u0004\b\r\u0010\u000e¨\u0006\u0019"}, d2 = {"Lkotlinx/coroutines/CompletableDeferredImpl;", "T", "Lkotlinx/coroutines/JobSupport;", "Lkotlinx/coroutines/CompletableDeferred;", "parent", "Lkotlinx/coroutines/Job;", "(Lkotlinx/coroutines/Job;)V", "onAwait", "Lkotlinx/coroutines/selects/SelectClause1;", "getOnAwait", "()Lkotlinx/coroutines/selects/SelectClause1;", "onCancelComplete", "", "getOnCancelComplete$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host", "()Z", "await", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "complete", "value", "(Ljava/lang/Object;)Z", "completeExceptionally", "exception", "", "getCompleted", "()Ljava/lang/Object;", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
final class CompletableDeferredImpl<T> extends kotlinx.coroutines.JobSupport implements kotlinx.coroutines.CompletableDeferred<T> {
    public CompletableDeferredImpl(kotlinx.coroutines.Job parent) {
        super(true);
        initParentJob(parent);
    }

    @Override // kotlinx.coroutines.JobSupport
    public boolean getOnCancelComplete$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host() {
        return true;
    }

    @Override // kotlinx.coroutines.Deferred
    public T getCompleted() {
        return (T) getCompletedInternal$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host();
    }

    @Override // kotlinx.coroutines.Deferred
    public java.lang.Object await(kotlin.coroutines.Continuation<? super T> continuation) {
        java.lang.Object objAwaitInternal = awaitInternal(continuation);
        kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED();
        return objAwaitInternal;
    }

    @Override // kotlinx.coroutines.Deferred
    public kotlinx.coroutines.selects.SelectClause1<T> getOnAwait() {
        kotlinx.coroutines.selects.SelectClause1<T> selectClause1 = (kotlinx.coroutines.selects.SelectClause1<T>) getOnAwaitInternal();
        kotlin.jvm.internal.Intrinsics.checkNotNull(selectClause1, "null cannot be cast to non-null type kotlinx.coroutines.selects.SelectClause1<T of kotlinx.coroutines.CompletableDeferredImpl>");
        return selectClause1;
    }

    @Override // kotlinx.coroutines.CompletableDeferred
    public boolean complete(T value) {
        return makeCompleting$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(value);
    }

    @Override // kotlinx.coroutines.CompletableDeferred
    public boolean completeExceptionally(java.lang.Throwable exception) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(exception, "exception");
        return makeCompleting$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(new kotlinx.coroutines.CompletedExceptionally(exception, false, 2, null));
    }
}
