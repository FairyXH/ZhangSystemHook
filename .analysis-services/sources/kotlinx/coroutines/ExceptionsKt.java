package kotlinx.coroutines;

/* JADX INFO: compiled from: Exceptions.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000 \n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0003\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\u001a\u001e\u0010\u0000\u001a\u00060\u0001j\u0002`\u00022\b\u0010\u0003\u001a\u0004\u0018\u00010\u00042\b\u0010\u0005\u001a\u0004\u0018\u00010\u0006\u001a\u0015\u0010\u0007\u001a\u00020\b*\u00020\u00062\u0006\u0010\t\u001a\u00020\u0006H\u0080\b*\n\u0010\u0000\"\u00020\u00012\u00020\u0001¨\u0006\n"}, d2 = {"CancellationException", "Ljava/util/concurrent/CancellationException;", "Lkotlinx/coroutines/CancellationException;", "message", "", "cause", "", "addSuppressedThrowable", "", "other", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 2, mv = {1, 9, 0}, xi = 48)
public final class ExceptionsKt {
    public static final java.util.concurrent.CancellationException CancellationException(java.lang.String message, java.lang.Throwable cause) {
        java.util.concurrent.CancellationException $this$CancellationException_u24lambda_u240 = new java.util.concurrent.CancellationException(message);
        $this$CancellationException_u24lambda_u240.initCause(cause);
        return $this$CancellationException_u24lambda_u240;
    }

    public static final void addSuppressedThrowable(java.lang.Throwable $this$addSuppressedThrowable, java.lang.Throwable other) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$addSuppressedThrowable, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(other, "other");
        kotlin.ExceptionsKt.addSuppressed($this$addSuppressedThrowable, other);
    }
}
