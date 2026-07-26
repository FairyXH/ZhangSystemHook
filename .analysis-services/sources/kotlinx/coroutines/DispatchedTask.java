package kotlinx.coroutines;

/* JADX INFO: compiled from: DispatchedTask.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u00004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0003\n\u0002\b\u000f\b!\u0018\u0000*\u0006\b\u0000\u0010\u0001 \u00002\u00060\u0002j\u0002`\u0003B\u000f\b\u0000\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\u001f\u0010\u000b\u001a\u00020\f2\b\u0010\r\u001a\u0004\u0018\u00010\u000e2\u0006\u0010\u000f\u001a\u00020\u0010H\u0010¢\u0006\u0002\b\u0011J\u0019\u0010\u0012\u001a\u0004\u0018\u00010\u00102\b\u0010\u0013\u001a\u0004\u0018\u00010\u000eH\u0010¢\u0006\u0002\b\u0014J\u001f\u0010\u0015\u001a\u0002H\u0001\"\u0004\b\u0001\u0010\u00012\b\u0010\u0013\u001a\u0004\u0018\u00010\u000eH\u0010¢\u0006\u0004\b\u0016\u0010\u0017J!\u0010\u0018\u001a\u00020\f2\b\u0010\u0019\u001a\u0004\u0018\u00010\u00102\b\u0010\u001a\u001a\u0004\u0018\u00010\u0010H\u0000¢\u0006\u0002\b\u001bJ\u0006\u0010\u001c\u001a\u00020\fJ\u000f\u0010\u001d\u001a\u0004\u0018\u00010\u000eH ¢\u0006\u0002\b\u001eR\u0018\u0010\u0007\u001a\b\u0012\u0004\u0012\u00028\u00000\bX \u0004¢\u0006\u0006\u001a\u0004\b\t\u0010\nR\u0012\u0010\u0004\u001a\u00020\u00058\u0006@\u0006X\u0087\u000e¢\u0006\u0002\n\u0000¨\u0006\u001f"}, d2 = {"Lkotlinx/coroutines/DispatchedTask;", "T", "Lkotlinx/coroutines/scheduling/Task;", "Lkotlinx/coroutines/SchedulerTask;", "resumeMode", "", "(I)V", "delegate", "Lkotlin/coroutines/Continuation;", "getDelegate$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host", "()Lkotlin/coroutines/Continuation;", "cancelCompletedResult", "", "takenState", "", "cause", "", "cancelCompletedResult$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host", "getExceptionalResult", "state", "getExceptionalResult$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host", "getSuccessfulResult", "getSuccessfulResult$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host", "(Ljava/lang/Object;)Ljava/lang/Object;", "handleFatalException", "exception", "finallyException", "handleFatalException$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host", "run", "takeState", "takeState$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
public abstract class DispatchedTask<T> extends kotlinx.coroutines.scheduling.Task {
    public int resumeMode;

    public abstract kotlin.coroutines.Continuation<T> getDelegate$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host();

    public abstract java.lang.Object takeState$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host();

    public DispatchedTask(int resumeMode) {
        this.resumeMode = resumeMode;
    }

    public void cancelCompletedResult$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(java.lang.Object takenState, java.lang.Throwable cause) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(cause, "cause");
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <T> T getSuccessfulResult$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(java.lang.Object state) {
        return state;
    }

    public java.lang.Throwable getExceptionalResult$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(java.lang.Object state) {
        kotlinx.coroutines.CompletedExceptionally completedExceptionally = state instanceof kotlinx.coroutines.CompletedExceptionally ? (kotlinx.coroutines.CompletedExceptionally) state : null;
        if (completedExceptionally != null) {
            return completedExceptionally.cause;
        }
        return null;
    }

    /* JADX WARN: Removed duplicated region for block: B:44:0x00c1  */
    /* JADX WARN: Removed duplicated region for block: B:46:0x00c9 A[Catch: all -> 0x0106, TryCatch #1 {all -> 0x0106, blocks: (B:42:0x00b1, B:43:0x00b4, B:48:0x00e4, B:38:0x00a1, B:46:0x00c9, B:47:0x00d7), top: B:78:0x0072 }] */
    /* JADX WARN: Removed duplicated region for block: B:47:0x00d7 A[Catch: all -> 0x0106, TryCatch #1 {all -> 0x0106, blocks: (B:42:0x00b1, B:43:0x00b4, B:48:0x00e4, B:38:0x00a1, B:46:0x00c9, B:47:0x00d7), top: B:78:0x0072 }] */
    /* JADX WARN: Removed duplicated region for block: B:52:0x00f0 A[Catch: all -> 0x011a, TRY_LEAVE, TryCatch #4 {all -> 0x011a, blocks: (B:13:0x001e, B:15:0x0041, B:50:0x00ea, B:52:0x00f0, B:63:0x010f, B:67:0x0119, B:65:0x0115), top: B:84:0x001e }] */
    /* JADX WARN: Removed duplicated region for block: B:65:0x0115 A[Catch: all -> 0x011a, TryCatch #4 {all -> 0x011a, blocks: (B:13:0x001e, B:15:0x0041, B:50:0x00ea, B:52:0x00f0, B:63:0x010f, B:67:0x0119, B:65:0x0115), top: B:84:0x001e }] */
    @Override // java.lang.Runnable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void run() {
        /*
            Method dump skipped, instruction units count: 319
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.DispatchedTask.run():void");
    }

    public final void handleFatalException$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(java.lang.Throwable exception, java.lang.Throwable finallyException) {
        if (exception == null && finallyException == null) {
            return;
        }
        if (exception != null && finallyException != null) {
            kotlin.ExceptionsKt.addSuppressed(exception, finallyException);
        }
        java.lang.Throwable cause = exception == null ? finallyException : exception;
        kotlin.jvm.internal.Intrinsics.checkNotNull(cause);
        kotlinx.coroutines.CoroutinesInternalError reason = new kotlinx.coroutines.CoroutinesInternalError("Fatal exception in coroutines machinery for " + this + ". Please read KDoc to 'handleFatalException' method and report this incident to maintainers", cause);
        kotlinx.coroutines.CoroutineExceptionHandlerKt.handleCoroutineException(getDelegate$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host().get$context(), reason);
    }
}
