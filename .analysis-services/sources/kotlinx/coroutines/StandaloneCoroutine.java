package kotlinx.coroutines;

/* JADX INFO: compiled from: Builders.common.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u0003\n\u0000\b\u0012\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u0015\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0006¢\u0006\u0002\u0010\u0007J\u0010\u0010\b\u001a\u00020\u00062\u0006\u0010\t\u001a\u00020\nH\u0014¨\u0006\u000b"}, d2 = {"Lkotlinx/coroutines/StandaloneCoroutine;", "Lkotlinx/coroutines/AbstractCoroutine;", "", "parentContext", "Lkotlin/coroutines/CoroutineContext;", com.android.server.pm.verify.domain.DomainVerificationPersistence.TAG_ACTIVE, "", "(Lkotlin/coroutines/CoroutineContext;Z)V", "handleJobException", "exception", "", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
class StandaloneCoroutine extends kotlinx.coroutines.AbstractCoroutine<kotlin.Unit> {
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public StandaloneCoroutine(kotlin.coroutines.CoroutineContext parentContext, boolean active) {
        super(parentContext, true, active);
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(parentContext, "parentContext");
    }

    @Override // kotlinx.coroutines.JobSupport
    protected boolean handleJobException(java.lang.Throwable exception) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(exception, "exception");
        kotlinx.coroutines.CoroutineExceptionHandlerKt.handleCoroutineException(getContext(), exception);
        return true;
    }
}
