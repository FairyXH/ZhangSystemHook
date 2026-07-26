package kotlinx.coroutines;

/* JADX INFO: compiled from: JobSupport.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0003\n\u0000\b\u0002\u0018\u0000*\u0004\b\u0000\u0010\u00012\u00020\u0002B\u0013\u0012\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u00028\u00000\u0004¢\u0006\u0002\u0010\u0005J\u0013\u0010\u0006\u001a\u00020\u00072\b\u0010\b\u001a\u0004\u0018\u00010\tH\u0096\u0002R\u0014\u0010\u0003\u001a\b\u0012\u0004\u0012\u00028\u00000\u0004X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\n"}, d2 = {"Lkotlinx/coroutines/ResumeAwaitOnCompletion;", "T", "Lkotlinx/coroutines/JobNode;", "continuation", "Lkotlinx/coroutines/CancellableContinuationImpl;", "(Lkotlinx/coroutines/CancellableContinuationImpl;)V", "invoke", "", "cause", "", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
final class ResumeAwaitOnCompletion<T> extends kotlinx.coroutines.JobNode {
    private final kotlinx.coroutines.CancellableContinuationImpl<T> continuation;

    @Override // kotlin.jvm.functions.Function1
    public /* bridge */ /* synthetic */ kotlin.Unit invoke(java.lang.Throwable th) {
        invoke2(th);
        return kotlin.Unit.INSTANCE;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public ResumeAwaitOnCompletion(kotlinx.coroutines.CancellableContinuationImpl<? super T> continuation) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(continuation, "continuation");
        this.continuation = continuation;
    }

    @Override // kotlinx.coroutines.CompletionHandlerBase
    /* JADX INFO: renamed from: invoke, reason: avoid collision after fix types in other method */
    public void invoke2(java.lang.Throwable cause) {
        java.lang.Object state = getJob().getState$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host();
        if (kotlinx.coroutines.DebugKt.getASSERTIONS_ENABLED() && !(!(state instanceof kotlinx.coroutines.Incomplete))) {
            throw new java.lang.AssertionError();
        }
        if (state instanceof kotlinx.coroutines.CompletedExceptionally) {
            kotlinx.coroutines.CancellableContinuationImpl<T> cancellableContinuationImpl = this.continuation;
            kotlin.Result.Companion companion = kotlin.Result.INSTANCE;
            cancellableContinuationImpl.resumeWith(kotlin.Result.m11307constructorimpl(kotlin.ResultKt.createFailure(((kotlinx.coroutines.CompletedExceptionally) state).cause)));
        } else {
            kotlinx.coroutines.CancellableContinuationImpl<T> cancellableContinuationImpl2 = this.continuation;
            kotlin.Result.Companion companion2 = kotlin.Result.INSTANCE;
            cancellableContinuationImpl2.resumeWith(kotlin.Result.m11307constructorimpl(kotlinx.coroutines.JobSupportKt.unboxState(state)));
        }
    }
}
