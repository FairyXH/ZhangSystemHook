package kotlinx.coroutines.flow.internal;

/* JADX INFO: compiled from: FlowExceptions.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0003\n\u0000\b\u0000\u0018\u00002\u00060\u0001j\u0002`\u0002B\u0011\u0012\n\u0010\u0003\u001a\u0006\u0012\u0002\b\u00030\u0004¢\u0006\u0002\u0010\u0005J\b\u0010\u0006\u001a\u00020\u0007H\u0016R\u0014\u0010\u0003\u001a\u0006\u0012\u0002\b\u00030\u00048\u0006X\u0087\u0004¢\u0006\u0002\n\u0000¨\u0006\b"}, d2 = {"Lkotlinx/coroutines/flow/internal/AbortFlowException;", "Ljava/util/concurrent/CancellationException;", "Lkotlinx/coroutines/CancellationException;", "owner", "Lkotlinx/coroutines/flow/FlowCollector;", "(Lkotlinx/coroutines/flow/FlowCollector;)V", "fillInStackTrace", "", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class AbortFlowException extends java.util.concurrent.CancellationException {
    public final transient kotlinx.coroutines.flow.FlowCollector<?> owner;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public AbortFlowException(kotlinx.coroutines.flow.FlowCollector<?> owner) {
        super("Flow was aborted, no more elements needed");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(owner, "owner");
        this.owner = owner;
    }

    @Override // java.lang.Throwable
    public java.lang.Throwable fillInStackTrace() {
        if (!kotlinx.coroutines.DebugKt.getDEBUG()) {
            setStackTrace(new java.lang.StackTraceElement[0]);
            return this;
        }
        java.lang.Throwable thFillInStackTrace = super.fillInStackTrace();
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(thFillInStackTrace, "fillInStackTrace(...)");
        return thFillInStackTrace;
    }
}
