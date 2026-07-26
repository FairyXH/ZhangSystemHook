package kotlinx.coroutines;

/* JADX INFO: compiled from: Builders.common.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\b\u0012\u0018\u0000*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u00022\b\u0012\u0004\u0012\u0002H\u00010\u0003B\u0015\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007¢\u0006\u0002\u0010\bJ\u000e\u0010\r\u001a\u00028\u0000H\u0096@¢\u0006\u0002\u0010\u000eJ\r\u0010\u000f\u001a\u00028\u0000H\u0016¢\u0006\u0002\u0010\u0010R\u001a\u0010\t\u001a\b\u0012\u0004\u0012\u00028\u00000\n8VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\u000b\u0010\f¨\u0006\u0011"}, d2 = {"Lkotlinx/coroutines/DeferredCoroutine;", "T", "Lkotlinx/coroutines/AbstractCoroutine;", "Lkotlinx/coroutines/Deferred;", "parentContext", "Lkotlin/coroutines/CoroutineContext;", com.android.server.pm.verify.domain.DomainVerificationPersistence.TAG_ACTIVE, "", "(Lkotlin/coroutines/CoroutineContext;Z)V", "onAwait", "Lkotlinx/coroutines/selects/SelectClause1;", "getOnAwait", "()Lkotlinx/coroutines/selects/SelectClause1;", "await", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getCompleted", "()Ljava/lang/Object;", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
class DeferredCoroutine<T> extends kotlinx.coroutines.AbstractCoroutine<T> implements kotlinx.coroutines.Deferred<T> {
    @Override // kotlinx.coroutines.Deferred
    public java.lang.Object await(kotlin.coroutines.Continuation<? super T> continuation) {
        return await$suspendImpl(this, continuation);
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public DeferredCoroutine(kotlin.coroutines.CoroutineContext parentContext, boolean active) {
        super(parentContext, true, active);
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(parentContext, "parentContext");
    }

    @Override // kotlinx.coroutines.Deferred
    public T getCompleted() {
        return (T) getCompletedInternal$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host();
    }

    static /* synthetic */ <T> java.lang.Object await$suspendImpl(kotlinx.coroutines.DeferredCoroutine<T> deferredCoroutine, kotlin.coroutines.Continuation<? super T> continuation) {
        java.lang.Object objAwaitInternal = deferredCoroutine.awaitInternal(continuation);
        kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED();
        return objAwaitInternal;
    }

    @Override // kotlinx.coroutines.Deferred
    public kotlinx.coroutines.selects.SelectClause1<T> getOnAwait() {
        kotlinx.coroutines.selects.SelectClause1<T> selectClause1 = (kotlinx.coroutines.selects.SelectClause1<T>) getOnAwaitInternal();
        kotlin.jvm.internal.Intrinsics.checkNotNull(selectClause1, "null cannot be cast to non-null type kotlinx.coroutines.selects.SelectClause1<T of kotlinx.coroutines.DeferredCoroutine>");
        return selectClause1;
    }
}
