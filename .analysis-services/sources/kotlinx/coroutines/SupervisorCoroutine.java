package kotlinx.coroutines;

/* JADX INFO: compiled from: Supervisor.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0003\n\u0000\b\u0002\u0018\u0000*\u0006\b\u0000\u0010\u0001 \u00002\b\u0012\u0004\u0012\u0002H\u00010\u0002B\u001b\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00028\u00000\u0006¢\u0006\u0002\u0010\u0007J\u0010\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000bH\u0016¨\u0006\f"}, d2 = {"Lkotlinx/coroutines/SupervisorCoroutine;", "T", "Lkotlinx/coroutines/internal/ScopeCoroutine;", "context", "Lkotlin/coroutines/CoroutineContext;", "uCont", "Lkotlin/coroutines/Continuation;", "(Lkotlin/coroutines/CoroutineContext;Lkotlin/coroutines/Continuation;)V", "childCancelled", "", "cause", "", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
final class SupervisorCoroutine<T> extends kotlinx.coroutines.internal.ScopeCoroutine<T> {
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public SupervisorCoroutine(kotlin.coroutines.CoroutineContext context, kotlin.coroutines.Continuation<? super T> uCont) {
        super(context, uCont);
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(context, "context");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(uCont, "uCont");
    }

    @Override // kotlinx.coroutines.JobSupport
    public boolean childCancelled(java.lang.Throwable cause) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(cause, "cause");
        return false;
    }
}
