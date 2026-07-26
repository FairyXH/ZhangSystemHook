package kotlinx.coroutines;

/* JADX INFO: compiled from: Timeout.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\u00060\u0001j\u0002`\u00022\b\u0012\u0004\u0012\u00020\u00000\u0003B\u000f\b\u0010\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006B\u0019\b\u0000\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\b\u0010\u0007\u001a\u0004\u0018\u00010\b¢\u0006\u0002\u0010\tJ\b\u0010\n\u001a\u00020\u0000H\u0016R\u0012\u0010\u0007\u001a\u0004\u0018\u00010\b8\u0000X\u0081\u0004¢\u0006\u0002\n\u0000¨\u0006\u000b"}, d2 = {"Lkotlinx/coroutines/TimeoutCancellationException;", "Ljava/util/concurrent/CancellationException;", "Lkotlinx/coroutines/CancellationException;", "Lkotlinx/coroutines/CopyableThrowable;", "message", "", "(Ljava/lang/String;)V", "coroutine", "Lkotlinx/coroutines/Job;", "(Ljava/lang/String;Lkotlinx/coroutines/Job;)V", "createCopy", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class TimeoutCancellationException extends java.util.concurrent.CancellationException implements kotlinx.coroutines.CopyableThrowable<kotlinx.coroutines.TimeoutCancellationException> {
    public final transient kotlinx.coroutines.Job coroutine;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public TimeoutCancellationException(java.lang.String message, kotlinx.coroutines.Job coroutine) {
        super(message);
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(message, "message");
        this.coroutine = coroutine;
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public TimeoutCancellationException(java.lang.String message) {
        this(message, null);
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(message, "message");
    }

    @Override // kotlinx.coroutines.CopyableThrowable
    public kotlinx.coroutines.TimeoutCancellationException createCopy() {
        java.lang.String message = getMessage();
        if (message == null) {
            message = "";
        }
        kotlinx.coroutines.TimeoutCancellationException it = new kotlinx.coroutines.TimeoutCancellationException(message, this.coroutine);
        it.initCause(this);
        return it;
    }
}
