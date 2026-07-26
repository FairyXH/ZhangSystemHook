package kotlinx.coroutines;

/* JADX INFO: compiled from: Exceptions.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0003\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0000\u0018\u00002\u00060\u0001j\u0002`\u00022\b\u0012\u0004\u0012\u00020\u00000\u0003B\u001f\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\b\u0010\u0006\u001a\u0004\u0018\u00010\u0007\u0012\u0006\u0010\b\u001a\u00020\t¢\u0006\u0002\u0010\nJ\n\u0010\u000b\u001a\u0004\u0018\u00010\u0000H\u0016J\u0013\u0010\f\u001a\u00020\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\u000fH\u0096\u0002J\b\u0010\u0010\u001a\u00020\u0007H\u0016J\b\u0010\u0011\u001a\u00020\u0012H\u0016J\b\u0010\u0013\u001a\u00020\u0005H\u0016R\u0010\u0010\b\u001a\u00020\t8\u0000X\u0081\u0004¢\u0006\u0002\n\u0000¨\u0006\u0014"}, d2 = {"Lkotlinx/coroutines/JobCancellationException;", "Ljava/util/concurrent/CancellationException;", "Lkotlinx/coroutines/CancellationException;", "Lkotlinx/coroutines/CopyableThrowable;", "message", "", "cause", "", com.android.server.am.HostingRecord.TRIGGER_TYPE_JOB, "Lkotlinx/coroutines/Job;", "(Ljava/lang/String;Ljava/lang/Throwable;Lkotlinx/coroutines/Job;)V", "createCopy", "equals", "", "other", "", "fillInStackTrace", com.android.server.oplus.osense.OsenseConstants.KEY_INTEGER_HASH, "", "toString", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class JobCancellationException extends java.util.concurrent.CancellationException implements kotlinx.coroutines.CopyableThrowable<kotlinx.coroutines.JobCancellationException> {
    public final transient kotlinx.coroutines.Job job;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public JobCancellationException(java.lang.String message, java.lang.Throwable cause, kotlinx.coroutines.Job job) {
        super(message);
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(message, "message");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(job, "job");
        this.job = job;
        if (cause != null) {
            initCause(cause);
        }
    }

    @Override // java.lang.Throwable
    public java.lang.Throwable fillInStackTrace() {
        if (kotlinx.coroutines.DebugKt.getDEBUG()) {
            java.lang.Throwable thFillInStackTrace = super.fillInStackTrace();
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(thFillInStackTrace, "fillInStackTrace(...)");
            return thFillInStackTrace;
        }
        setStackTrace(new java.lang.StackTraceElement[0]);
        return this;
    }

    @Override // kotlinx.coroutines.CopyableThrowable
    public kotlinx.coroutines.JobCancellationException createCopy() {
        if (kotlinx.coroutines.DebugKt.getDEBUG()) {
            java.lang.String message = getMessage();
            kotlin.jvm.internal.Intrinsics.checkNotNull(message);
            return new kotlinx.coroutines.JobCancellationException(message, this, this.job);
        }
        return null;
    }

    @Override // java.lang.Throwable
    public java.lang.String toString() {
        return super.toString() + "; job=" + this.job;
    }

    public boolean equals(java.lang.Object other) {
        return other == this || ((other instanceof kotlinx.coroutines.JobCancellationException) && kotlin.jvm.internal.Intrinsics.areEqual(((kotlinx.coroutines.JobCancellationException) other).getMessage(), getMessage()) && kotlin.jvm.internal.Intrinsics.areEqual(((kotlinx.coroutines.JobCancellationException) other).job, this.job) && kotlin.jvm.internal.Intrinsics.areEqual(((kotlinx.coroutines.JobCancellationException) other).getCause(), getCause()));
    }

    public int hashCode() {
        java.lang.String message = getMessage();
        kotlin.jvm.internal.Intrinsics.checkNotNull(message);
        int iHashCode = ((message.hashCode() * 31) + this.job.hashCode()) * 31;
        java.lang.Throwable cause = getCause();
        return iHashCode + (cause != null ? cause.hashCode() : 0);
    }
}
